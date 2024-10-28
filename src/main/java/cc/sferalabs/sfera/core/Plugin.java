/*-
 * +======================================================================+
 * Sfera
 * ---
 * Copyright (C) 2015 - 2016 Sfera Labs S.r.l.
 * ---
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 * 
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0.html>.
 * -======================================================================-
 */

package cc.sferalabs.sfera.core;

import java.io.BufferedReader;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

/**
 * This class represents an installed sfera plugin
 * 
 * @author Giampiero Baggiani
 *
 * @version 1.0.0
 *
 */
public class Plugin {

	private final static String PLUGIN_POM_FILE = "pom.xml";

	private final Path path;
	private final String id;
	private final String name;
	private final String description;
	private final String version;

	/**
	 * Creates a plugin object from the specified jar file
	 * 
	 * @param jarFile
	 *            the jar file containing the plugin
	 * @throws Exception
	 *             if an error occours loadinf the plugin
	 */
	Plugin(Path jarFile) throws Exception {
		if (!Files.isRegularFile(jarFile)) {
			throw new Exception("not a regular file");
		}
		if (!jarFile.getFileName().toString().endsWith(".jar")) {
			throw new Exception("not a jar file");
		}

		this.path = jarFile;

		String groupId = null;
		String artifactId = null;
		String name = null;
		String description = null;
		String version = null;
		try (FileSystem pluginFileSystem = FileSystems.newFileSystem(jarFile, null, null)) {
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			XMLEventReader eventReader = null;
			try (BufferedReader br = Files
					.newBufferedReader(pluginFileSystem.getPath(PLUGIN_POM_FILE))) {

				eventReader = inputFactory.createXMLEventReader(br);
				boolean inProject = false;
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (event.isStartElement()) {
						StartElement startElement = event.asStartElement();
						if (!inProject) {
							if (startElement.getName().getLocalPart().equals("project")) {
								inProject = true;
							}
						} else {
							String elName = startElement.getName().getLocalPart();
							if (groupId == null && elName.equals("groupId")) {
								event = eventReader.nextEvent();
								if (event.isCharacters()) {
									groupId = event.asCharacters().getData();
								}
							} else if (artifactId == null && elName.equals("artifactId")) {
								event = eventReader.nextEvent();
								if (event.isCharacters()) {
									artifactId = event.asCharacters().getData();
								}
							} else if (name == null && elName.equals("name")) {
								event = eventReader.nextEvent();
								if (event.isCharacters()) {
									name = event.asCharacters().getData();
								}
							} else if (description == null && elName.equals("description")) {
								event = eventReader.nextEvent();
								if (event.isCharacters()) {
									description = event.asCharacters().getData();
								}
							} else if (version == null && elName.equals("version")) {
								event = eventReader.nextEvent();
								if (event.isCharacters()) {
									version = event.asCharacters().getData();
								}
							}
						}

					} else if (event.isEndElement()) {
						EndElement endElement = event.asEndElement();
						if (endElement.getName().getLocalPart().equals("project")) {
							inProject = false;
						}
					}
				}

				if (groupId == null || artifactId == null) {
					throw new Exception("id not found");
				}

				this.id = groupId + "." + artifactId;
				this.name = name;
				this.description = description;
				this.version = version;

			} finally {
				try {
					eventReader.close();
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			throw new Exception("Error reading plugin properties", e);
		}
	}

	/**
	 * 
	 * @return the id of the plugin
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return the path of the jar containing the plugin
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * 
	 * @return the name of the plugin
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the description of the plugin
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @return the version of the plugin
	 */
	public String getVersion() {
		return version;
	}
}
