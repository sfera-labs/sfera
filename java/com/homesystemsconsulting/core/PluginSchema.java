package com.homesystemsconsulting.core;

import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PluginSchema {
	
	private final String id;
	private final NodeList driverNodes;
	private final NodeList appNodes;

	public PluginSchema(Path jarFile) throws Exception {
		if (!Files.isRegularFile(jarFile)) {
			throw new Exception("not a regular file");
		}
		if (!jarFile.getFileName().toString().endsWith(".jar")) {
			throw new Exception("not a jar file");
		}
		
		Document doc;
		try (FileSystem pluginFileSystem = FileSystems.newFileSystem(jarFile, null)) {
			Path manifest = pluginFileSystem.getPath("manifest.xml");
			try (InputStream is = Files.newInputStream(manifest)) {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(is);
				doc.getDocumentElement().normalize();
				
				this.id = doc.getElementsByTagName("id").item(0).getTextContent();
				this.driverNodes = doc.getElementsByTagName("driver");
				this.appNodes = doc.getElementsByTagName("app");
			}
		} catch (Exception e) {
			throw new Exception("error reading manifest file", e);
		}
	}

	/**
	 * 
	 * @param driverName
	 * @return
	 */
	public Set<String> getDriverClasses(String driverName) {
		try {
			if (driverNodes != null) {
				for (int i = 0; i < driverNodes.getLength(); i++) {
					Element driverNode = (Element) driverNodes.item(i);
					
					if (driverNode.getElementsByTagName("name").item(0).getTextContent().equalsIgnoreCase(driverName)) {
						NodeList classList = driverNode.getElementsByTagName("class");
						Set<String> classNames = new HashSet<>(classList.getLength());
						for (int j = 0; j < classList.getLength(); j++) {
							classNames.add(classList.item(j).getTextContent());
						}
						return classNames;
					}
				}
			}
		} catch (Exception e) {}
		
		return null;
	}

	/**
	 * 
	 * @param appName
	 * @return
	 */
	public String getApplicationClass(String appName) {
		try {
			if (appNodes != null) {
				for (int i = 0; i < appNodes.getLength(); i++) {
					Element appNode = (Element) appNodes.item(i);
					
					if (appNode.getElementsByTagName("name").item(0).getTextContent().equalsIgnoreCase(appName)) {
						return appNode.getElementsByTagName("class").item(0).getTextContent();
					}
				}
			}
		} catch (Exception e) {}
		
		return null;
	}

}
