package com.homesystemsconsulting.drivers.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.homesystemsconsulting.util.files.ResourcesUtils;

public class InterfaceCacheCreator {
	
	private static final XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	private static final XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
	private static final XMLEventFactory eventFactory = XMLEventFactory.newInstance();
	
	private static final XMLEvent nl = eventFactory.createDTD("\n");
	
	private static final Charset utf8cs = Charset.forName("UTF-8");

	private final String interfaceName;
	private final Path interfaceTmpCacheRoot;
	
	private Set<String> objects = new HashSet<String>();
	private String skin = null;
	private String language = null;
	private String iconSet = null;

	/**
	 * 
	 * @param interfaceName
	 * @throws IOException 
	 */
	public InterfaceCacheCreator(String interfaceName) throws IOException {
		this.interfaceName = interfaceName;
		this.interfaceTmpCacheRoot = Files.createTempDirectory("webappTmp");
	}

	/**
	 * 
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public void create(boolean createManifest) throws XMLStreamException, IOException {
		createIntefaceCache(createManifest);
		createLoginCache(createManifest);
		
		Path interfaceCacheRoot = WebServer.CACHE_ROOT.resolve(interfaceName + "/");
		ResourcesUtils.deleteRecursive(interfaceCacheRoot);
		Files.createDirectories(WebServer.CACHE_ROOT);
		Files.move(interfaceTmpCacheRoot, interfaceCacheRoot);
	}

	/**
	 * 
	 * @param createManifest
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void createIntefaceCache(boolean createManifest) throws IOException, XMLStreamException {
		createInterfaceXmlAndExtractAttributes();
		createDictionaryAndExtractSkinIconSet();
		Set<Path> resources = copyResources();
		createIndex("skins/index.html", "index.html", createManifest, "/manifest", false);
		createInterfaceCode();
		createInterfaceCSS();
		if (createManifest) {
			resources.add(interfaceTmpCacheRoot.resolve("style.css"));
			resources.add(interfaceTmpCacheRoot.resolve("code.js"));
			createManifest("manifest", resources);
		}
	}
	
	/**
	 * 
	 * @param createManifest
	 * @throws IOException
	 */
	private void createLoginCache(boolean createManifest) throws IOException {
		Files.createDirectories(interfaceTmpCacheRoot.resolve("login"));
		
		Set<String> imgs = createIndex("skins/login.html", "login/index.html", createManifest, "/login/manifest", true);
		Set<Path> loginResources = copyLoginResources(imgs);
		createLoginCode();
		createLoginCSS();
		if (createManifest) {
			loginResources.add(interfaceTmpCacheRoot.resolve("login/style.css"));
			loginResources.add(interfaceTmpCacheRoot.resolve("login/code.js"));
			createManifest("login/manifest", loginResources);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createInterfaceCSS() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("style.css"), utf8cs)) {
			
			writeContentFrom("skins/" + skin + "/style.css", writer);
			
			for (String o : objects) {
				try {
					writeContentFrom("objects/" + o + "/" + o + ".css", writer);
				} catch (NoSuchFileException e) {}
			}
			
			try {
				writeContentFrom("interfaces/" + interfaceName + "/style.css", writer);
			} catch (NoSuchFileException e) {}
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void createLoginCSS() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("login/style.css"), utf8cs)) {
			
			writeContentFrom("skins/" + skin + "/login/style.css", writer);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createInterfaceCode() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("code.js"), utf8cs)) {
			
			try {
				writeContentFrom("code/client.min.js", writer);
			} catch (NoSuchFileException e) {
				writeContentFrom("code/client.js", writer);
			}
			
			for (String o : objects) {
				try {
					writeContentFrom("objects/" + o + "/" + o + ".min.js", writer);
				} catch (NoSuchFileException e) {
					try {
						writeContentFrom("objects/" + o + "/" + o + ".js", writer);
					} catch (NoSuchFileException e1) {}
				}
			}
			
			Set<String> files = ResourcesUtils.listRegularFilesNamesInDirectory(
					WebServer.ROOT.resolve("interfaces/" + interfaceName + "/"), true);
			if (files != null) {
				for (String file : files) {
					if (file.toLowerCase().endsWith(".js")) {
						writeContentFrom("interfaces/" + interfaceName + "/" + file, writer);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void createLoginCode() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("login/code.js"), utf8cs)) {
			
			try {
				writeContentFrom("code/login.min.js", writer);
			} catch (NoSuchFileException e) {
				writeContentFrom("code/login.js", writer);
			}
		}
	}

	/**
	 * 
	 * @param file
	 * @param writer
	 * @throws IOException
	 * @throws NoSuchFileException
	 */
	private void writeContentFrom(String file, BufferedWriter writer) throws IOException, NoSuchFileException {
		Path filePath = ResourcesUtils.getResourceFromJarIfNotInFileSystem(WebServer.ROOT.resolve(file));
		try (BufferedReader reader = Files.newBufferedReader(filePath, utf8cs)) {
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	writer.write(line);
		    	writer.write("\n");
		    }
		}
	}

	/**
	 * 
	 * @param source
	 * @param target
	 * @param addManifest
	 * @param manifestPath
	 * @param extractImages
	 * @throws IOException
	 */
	private Set<String> createIndex(String source, String target, boolean addManifest, String manifestPath, boolean extractImages) throws IOException {
		Path indexPath = ResourcesUtils.getResourceFromJarIfNotInFileSystem(
				WebServer.ROOT.resolve(source));

		Set<String> imgs = new HashSet<String>();
		List<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(indexPath, utf8cs)) {
			boolean manifestReplaced = false;
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if (!manifestReplaced && line.contains("$manifest;")) {
					String replacement;
					if (addManifest) {
						replacement = "manifest=\"/" + interfaceName + manifestPath + "\"";
					} else {
						replacement = "";
					}
		    		line = line.replace("$manifest;", replacement);
		    		manifestReplaced = true;
				}
				if (line.contains("$interface;")) {
					if (extractImages) {
						String img = extractImage(line);
						if (img != null) {
							imgs.add(img);
						}
					}
					line = line.replace("$interface;", interfaceName);
				}
				lines.add(line);
		    }
		}
		
		Files.write(interfaceTmpCacheRoot.resolve(target), lines, utf8cs);
		
		return imgs;
	}

	/**
	 * 
	 * @param line
	 * @return
	 */
	private String extractImage(String line) {
		String imgPrefix = "/$interface;/login/icons/";
		int imgIdx = line.indexOf(imgPrefix);
		if (imgIdx >= 0) {
			int begin = imgIdx + imgPrefix.length();
			char[] terminators = {')', ',', '"'};
			int end = line.length();
			for (char t : terminators) {
				int tIdx = line.indexOf(t, begin);
				if (tIdx > 0 && tIdx < end) {
					end = tIdx;
				}
			}
			
			return line.substring(begin, end);
		}
		
		return null;
	}

	/**
	 * 
	 * @throws IOException
	 */
	private Set<Path> copyResources() throws IOException {
		Set<Path> resources = new HashSet<Path>();
		
		resources.addAll(ResourcesUtils.copyRecursive(
						WebServer.ROOT.resolve("interfaces/" + interfaceName + "/assets/"), 
						interfaceTmpCacheRoot.resolve("assets/"), 
						true));
		
		Files.createDirectories(interfaceTmpCacheRoot.resolve("images/objects/"));
		
		resources.addAll(ResourcesUtils.copyRecursive(
				WebServer.ROOT.resolve("skins/" + skin + "/images/"), 
				interfaceTmpCacheRoot.resolve("images/skin/"), 
				true));
		
		for (String o : objects) {
			resources.addAll(ResourcesUtils.copyRecursive(
					WebServer.ROOT.resolve("objects/" + o + "/images/" + iconSet + "/"), 
					interfaceTmpCacheRoot.resolve("images/objects/" + o + "/"), 
					true));
		}
		
		resources.addAll(ResourcesUtils.copyRecursive(
				WebServer.ROOT.resolve("icons/" + iconSet + "/"), 
				interfaceTmpCacheRoot.resolve("icons/"), 
				true));
		
		return resources;
	}
	
	/**
	 * 
	 * @param images 
	 * @return
	 * @throws IOException
	 */
	private Set<Path> copyLoginResources(Set<String> images) throws IOException {
		Set<Path> loginResources = new HashSet<Path>();
		
		Files.createDirectories(interfaceTmpCacheRoot.resolve("login/images/"));
		
		loginResources.addAll(ResourcesUtils.copyRecursive(
				WebServer.ROOT.resolve("skins/" + skin + "/login/images/"), 
				interfaceTmpCacheRoot.resolve("login/images/skin/"), 
				true));

		Files.createDirectories(interfaceTmpCacheRoot.resolve("login/icons/"));
		
		for (String img : images) {
			Files.copy(ResourcesUtils.getResourceFromJarIfNotInFileSystem(
					WebServer.ROOT.resolve("icons/" + iconSet + "/" + img)), 
					interfaceTmpCacheRoot.resolve("login/icons/" + img));
		}
		
		return loginResources;
	}

	/**
	 * 
	 * @param resources 
	 * @throws IOException
	 */
	private void createManifest(String path, Set<Path> resources) throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve(path), utf8cs)) {
			
			writer.write("CACHE MANIFEST\r\n\r\n# ");
			writer.write(WebServer.DATE_FORMAT.format(new Date()));
			writer.write("\r\n\r\nCACHE:\r\n");
			
			for (Path r : resources) {
				if (Files.isRegularFile(r)) {
					if (!r.startsWith(interfaceTmpCacheRoot.resolve("assets/no-cache/"))) {
						writer.write("/" + interfaceName + "/" + interfaceTmpCacheRoot.relativize(r).toString());
						writer.write("\r\n");
					}
				}
			}
			
			writer.write("\r\nNETWORK:\r\n*");
			
			writer.flush();
		}
	}

	/**
	 * 
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void createDictionaryAndExtractSkinIconSet() throws IOException, XMLStreamException {
		XMLEventWriter eventWriter = null;
		try (BufferedWriter out = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("dictionary.xml"), utf8cs)) {
			eventWriter = outputFactory.createXMLEventWriter(out);
			
			StartDocument startDocument = eventFactory.createStartDocument();
		    eventWriter.add(startDocument);
		    eventWriter.add(nl);
		    
		    StartElement dictionaryStartElement = eventFactory.createStartElement("", "", "dictionary");
		    eventWriter.add(dictionaryStartElement);
		    eventWriter.add(nl);
		    
		    addSkinDefinitionAndExtractIconSet(eventWriter);
		    
		    addObjects(eventWriter);
		    
		    eventWriter.add(eventFactory.createEndElement("", "", "dictionary"));
		    eventWriter.add(nl);
		    
		    eventWriter.add(eventFactory.createEndDocument());
		    
		} finally {
			try { eventWriter.close(); } catch (Exception e) {}
		}
	}

	/**
	 * 
	 * @param eventWriter
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void addObjects(XMLEventWriter eventWriter) throws XMLStreamException, IOException {
		eventWriter.add(eventFactory.createStartElement("", "", "objects"));
	    eventWriter.add(nl);
	    
		for (String obj : objects) {
			XMLEventReader eventReader = null;
			Path objXmlPath = ResourcesUtils.getResourceFromJarIfNotInFileSystem(
					WebServer.ROOT.resolve("objects/" + obj + "/" + obj + ".xml"));
			
			try (BufferedReader in = Files.newBufferedReader(objXmlPath, utf8cs)) {
				eventReader = inputFactory.createXMLEventReader(in);
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (!event.isStartDocument() && !event.isEndDocument()) {
						if (event.isEndElement()) {
							EndElement objEnd = event.asEndElement();
							if (objEnd.getName().getLocalPart().equals("obj")) {
								try {
									addElementWithCDataContentFromFile(WebServer.ROOT.resolve("objects/" + obj + "/" + obj + ".shtml"), "src", eventWriter, eventFactory);
								} catch (NoSuchFileException e) {
									// this object has no src, and that's fine
								}
								try {
									addElementWithCDataContentFromFile(WebServer.ROOT.resolve("objects/" + obj + "/languages/" + language + ".ini"), "language", eventWriter, eventFactory);
								} catch (NoSuchFileException e) {
									// this object has no languages, and that's fine
								}
							}
						}
						
						eventWriter.add(event);
					}
				}
				eventWriter.add(nl);
				
			} finally {
				try { eventReader.close(); } catch (Exception e) {}
			}
		}
	    
	    eventWriter.add(eventFactory.createEndElement("", "", "objects"));
	    eventWriter.add(nl);
	}

	/**
	 * 
	 * @param eventWriter
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void addSkinDefinitionAndExtractIconSet(XMLEventWriter eventWriter) throws IOException, XMLStreamException {
		Path skinXmlPath = ResourcesUtils.getResourceFromJarIfNotInFileSystem(
				WebServer.ROOT.resolve("skins/" + skin + "/" + "definition.xml"));
		
		XMLEventReader eventReader = null;
		try (BufferedReader in = Files.newBufferedReader(skinXmlPath, utf8cs)) {
	    	eventReader = inputFactory.createXMLEventReader(in);
	    	boolean nextIsIconSet = false;
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (!event.isStartDocument() && !event.isEndDocument()) {
					eventWriter.add(event);
					if (iconSet == null) {
						if (!nextIsIconSet && event.isStartElement()) {
				            if (event.asStartElement().getName().getLocalPart().equals("iconset")) {
				            	nextIsIconSet = true;
				            }
						} else if (nextIsIconSet) {
							iconSet = event.asCharacters().getData();
						}
					}
				}
			}
			eventWriter.add(nl);
			
		} finally {
			try { eventReader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * Extracts skin, language and objects used in the interface
	 * 
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void createInterfaceXmlAndExtractAttributes() throws IOException, XMLStreamException {
		Path indexXml = ResourcesUtils.getResourceFromJarIfNotInFileSystem(
				WebServer.ROOT.resolve("interfaces/" + interfaceName + "/index.xml"));
		Path cacheXml = interfaceTmpCacheRoot.resolve("index.xml");		
		Files.copy(indexXml, cacheXml);
		
		XMLEventReader eventReader = null;
		try (BufferedReader in = Files.newBufferedReader(cacheXml, utf8cs)) {
			eventReader = inputFactory.createXMLEventReader(in);
			
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					StartElement startElement = event.asStartElement();
					String obj = startElement.getName().getLocalPart();
					objects.add(obj);
					
					if (obj.equals("interface")) {
						Iterator<?> attributes = startElement.getAttributes();
						while (attributes.hasNext()) {
							Attribute attribute = (Attribute) attributes.next();
							String attributeName = attribute.getName().getLocalPart();
							if (attributeName.equals("language")) {
								language = attribute.getValue();
							} else if (attributeName.equals("skin")) {
								skin = attribute.getValue();
							}
						}
					}
				}
			}
			
		} finally {
			try { eventReader.close(); } catch (Exception e) {}
		}
	}

	/**
	 * 
	 * @param file
	 * @param elementLocalName
	 * @param eventWriter
	 * @param eventFactory
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void addElementWithCDataContentFromFile(Path file, String elementLocalName, XMLEventWriter eventWriter,
			XMLEventFactory eventFactory) throws XMLStreamException, IOException {
		
		Path filePath = ResourcesUtils.getResourceFromJarIfNotInFileSystem(file);
		
		try (BufferedReader reader = Files.newBufferedReader(filePath, utf8cs)) {
			eventWriter.add(eventFactory.createStartElement("", "", elementLocalName));
		    eventWriter.add(nl);
		    
		    StringBuilder content = new StringBuilder("\n");
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	content.append(line).append('\n');
		    }
		    
		    eventWriter.add(eventFactory.createCData(content.toString()));
		    eventWriter.add(nl);
		    
		    eventWriter.add(eventFactory.createEndElement("", "", elementLocalName));
		    eventWriter.add(nl);
		}
	}
}
