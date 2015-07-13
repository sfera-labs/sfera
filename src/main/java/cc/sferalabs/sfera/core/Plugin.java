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

public class Plugin {

	final static String PLUGIN_POM_FILE = "pom.xml";

	private final Path path;
	private final String id;
	private final String name;
	private final String description;

	/**
	 * 
	 * @param jarFile
	 * @throws Exception
	 */
	public Plugin(Path jarFile) throws Exception {
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
		try (FileSystem pluginFileSystem = FileSystems.newFileSystem(jarFile, null)) {
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
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 
	 * @return
	 */
	public Path getPath() {
		return path;
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return
	 */
	public String getDescription() {
		return description;
	}
}
