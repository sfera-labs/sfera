package com.homesystemsconsulting.drivers.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
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

import com.homesystemsconsulting.core.Configuration;
import com.homesystemsconsulting.core.FilesWatcher;
import com.homesystemsconsulting.core.Sfera;
import com.homesystemsconsulting.core.Task;
import com.homesystemsconsulting.drivers.webserver.HttpRequestHeader.Method;
import com.homesystemsconsulting.drivers.webserver.access.Token;
import com.homesystemsconsulting.drivers.webserver.util.DateUtil;
import com.homesystemsconsulting.drivers.webserver.util.ResourcesUtil;

public class InterfaceCache {
	
	static final Path CACHE_ROOT = WebServer.ROOT.resolve("cache/");
	static final Path ABSOLUTE_CACHE_ROOT_PATH = CACHE_ROOT.toAbsolutePath();
	
	private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();
	private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newInstance();
	private static final XMLEventFactory EVENT_FACTORY = XMLEventFactory.newInstance();
	
	private static final XMLEvent NL = EVENT_FACTORY.createDTD("\n");
	
	private static Set<String> interfaces;
	
	private static String defaultInterface;
	private static boolean usePermanentCache;

	private final String interfaceName;
	private final Path interfaceCacheRoot;
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
	private InterfaceCache(String interfaceName) throws IOException {
		this.interfaceName = interfaceName;
		this.interfaceTmpCacheRoot = Files.createTempDirectory("webappTmp");
		this.interfaceCacheRoot = CACHE_ROOT.resolve(interfaceName + "/");
	}
	
	/**
	 * 
	 */
	public synchronized static void init(Configuration configuration) throws Exception {
		if (interfaces == null) {
			defaultInterface = configuration.getProperty("default_interface", null);
			usePermanentCache = configuration.getBoolProperty("use_permanent_cache", true);
			
			createCache();
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private static void createCache() throws IOException {
		Path interfacesPath = WebServer.ROOT.resolve("interfaces/");
		try {
			interfaces = new HashSet<String>();
			try {
				for (String interfaceName : ResourcesUtil.listDirectoriesNamesInDirectory(interfacesPath, true)) {
					try {
						createCacheFor(interfaceName);
						interfaces.add(interfaceName);
					} catch (Exception e) {
						WebServer.getLogger().error("error creating cache for interface '" + interfaceName + "': " + e);
					}
				}
				
				for (String interfaceName : ResourcesUtil.listDirectoriesNamesInDirectory(CACHE_ROOT, false)) {
					if (!interfaces.contains(interfaceName)) {
						ResourcesUtil.deleteRecursive(CACHE_ROOT.resolve(interfaceName + "/"));
					}
				}
			} catch (NoSuchFileException nsfe) {
				ResourcesUtil.deleteRecursive(CACHE_ROOT);
			}
			
		} finally {
			try {
				FilesWatcher.register(interfacesPath, new Task("WebApp files watcher") {
					
					@Override
					public void execute() {
						try {
							createCache();
						} catch (IOException e) {
							WebServer.getLogger().error("error creating cache: " + e);
						}
					}
				});
			} catch (Exception e) {
				WebServer.getLogger().error("error registering WebApp files watcher: " + e);
			}
		}
	}

	/**
	 * 
	 * @param interfaceName
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private synchronized static void createCacheFor(String interfaceName) throws IOException, XMLStreamException {
		WebServer.getLogger().debug("creating cache for interface: " + interfaceName);
		InterfaceCache icc = new InterfaceCache(interfaceName);
		icc.create();
		ResourcesUtil.release();
		WebServer.getLogger().debug("created cache for interface: " + interfaceName);
	}

	/**
	 * 
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	private void create() throws XMLStreamException, IOException {
		createIntefaceCache();
		createLoginCache();
		
		ResourcesUtil.deleteRecursive(interfaceCacheRoot);
		Files.createDirectories(CACHE_ROOT);
		Files.move(interfaceTmpCacheRoot, interfaceCacheRoot);
	}

	/**
	 * 
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void createIntefaceCache() throws IOException, XMLStreamException {
		createInterfaceXmlAndExtractAttributes();
		createDictionaryAndExtractSkinIconSet();
		Set<Path> resources = copyResources();
		createIndex("skins/index.html", "index.html", "/manifest", false);
		createInterfaceCode();
		createInterfaceCSS();
		if (usePermanentCache) {
			resources.add(interfaceTmpCacheRoot.resolve("style.css"));
			resources.add(interfaceTmpCacheRoot.resolve("code.js"));
			createManifest("manifest", resources);
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void createLoginCache() throws IOException {
		Files.createDirectories(interfaceTmpCacheRoot.resolve("login"));
		
		Set<String> imgs = createIndex("skins/login.html", "login/index.html", "/login/manifest", true);
		Set<Path> loginResources = copyLoginResources(imgs);
		createLoginCode();
		createLoginCSS();
		if (usePermanentCache) {
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
				interfaceTmpCacheRoot.resolve("style.css"), Sfera.CHARSET)) {
			
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
				interfaceTmpCacheRoot.resolve("login/style.css"), Sfera.CHARSET)) {
			
			writeContentFrom("skins/" + skin + "/login/style.css", writer);
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void createInterfaceCode() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("code.js"), Sfera.CHARSET)) {
			
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
			
			String interfacePath = "interfaces/" + interfaceName + "/";
			try {
				Set<String> files = ResourcesUtil.listRegularFilesNamesInDirectory(WebServer.ROOT.resolve(interfacePath), true);
				for (String file : files) {
					if (file.toLowerCase().endsWith(".js")) {
						writeContentFrom(interfacePath + file, writer);
					}
				}
			} catch (NoSuchFileException nsfe) {}
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void createLoginCode() throws IOException {
		try (BufferedWriter writer = Files.newBufferedWriter(
				interfaceTmpCacheRoot.resolve("login/code.js"), Sfera.CHARSET)) {
			
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
		Path filePath = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(WebServer.ROOT.resolve(file));
		try (BufferedReader reader = Files.newBufferedReader(filePath, Sfera.CHARSET)) {
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
	private Set<String> createIndex(String source, String target, String manifestPath, boolean extractImages) throws IOException {
		Path indexPath = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(
				WebServer.ROOT.resolve(source));

		Set<String> imgs = new HashSet<String>();
		List<String> lines = new ArrayList<String>();
		try (BufferedReader reader = Files.newBufferedReader(indexPath, Sfera.CHARSET)) {
			boolean manifestReplaced = false;
			String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if (!manifestReplaced && line.contains("$manifest;")) {
					String replacement;
					if (usePermanentCache) {
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
		
		Files.write(interfaceTmpCacheRoot.resolve(target), lines, Sfera.CHARSET);
		
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
		
		resources.addAll(ResourcesUtil.copyRecursive(
						WebServer.ROOT.resolve("interfaces/" + interfaceName + "/assets/"), 
						interfaceTmpCacheRoot.resolve("assets/"), 
						true));
		
		Files.createDirectories(interfaceTmpCacheRoot.resolve("images/objects/"));
		
		resources.addAll(ResourcesUtil.copyRecursive(
				WebServer.ROOT.resolve("skins/" + skin + "/images/"), 
				interfaceTmpCacheRoot.resolve("images/skin/"), 
				true));
		
		for (String o : objects) {
			resources.addAll(ResourcesUtil.copyRecursive(
					WebServer.ROOT.resolve("objects/" + o + "/images/" + iconSet + "/"), 
					interfaceTmpCacheRoot.resolve("images/objects/" + o + "/"), 
					true));
		}
		
		resources.addAll(ResourcesUtil.copyRecursive(
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
		
		loginResources.addAll(ResourcesUtil.copyRecursive(
				WebServer.ROOT.resolve("skins/" + skin + "/login/images/"), 
				interfaceTmpCacheRoot.resolve("login/images/skin/"), 
				true));

		Files.createDirectories(interfaceTmpCacheRoot.resolve("login/icons/"));
		
		for (String img : images) {
			Files.copy(ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(
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
				interfaceTmpCacheRoot.resolve(path), Sfera.CHARSET)) {
			
			writer.write("CACHE MANIFEST\r\n\r\n# ");
			writer.write(DateUtil.now());
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
				interfaceTmpCacheRoot.resolve("dictionary.xml"), Sfera.CHARSET)) {
			eventWriter = OUTPUT_FACTORY.createXMLEventWriter(out);
			
			StartDocument startDocument = EVENT_FACTORY.createStartDocument();
		    eventWriter.add(startDocument);
		    eventWriter.add(NL);
		    
		    StartElement dictionaryStartElement = EVENT_FACTORY.createStartElement("", "", "dictionary");
		    eventWriter.add(dictionaryStartElement);
		    eventWriter.add(NL);
		    
		    addSkinDefinitionAndExtractIconSet(eventWriter);
		    
		    addObjects(eventWriter);
		    
		    eventWriter.add(EVENT_FACTORY.createEndElement("", "", "dictionary"));
		    eventWriter.add(NL);
		    
		    eventWriter.add(EVENT_FACTORY.createEndDocument());
		    
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
		eventWriter.add(EVENT_FACTORY.createStartElement("", "", "objects"));
	    eventWriter.add(NL);
	    
		for (String obj : objects) {
			XMLEventReader eventReader = null;
			Path objXmlPath = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(
					WebServer.ROOT.resolve("objects/" + obj + "/" + obj + ".xml"));
			
			try (BufferedReader in = Files.newBufferedReader(objXmlPath, Sfera.CHARSET)) {
				eventReader = INPUT_FACTORY.createXMLEventReader(in);
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (!event.isStartDocument() && !event.isEndDocument()) {
						if (event.isEndElement()) {
							EndElement objEnd = event.asEndElement();
							if (objEnd.getName().getLocalPart().equals("obj")) {
								try {
									addElementWithCDataContentFromFile(WebServer.ROOT.resolve("objects/" + obj + "/" + obj + ".shtml"), "src", eventWriter, EVENT_FACTORY);
								} catch (NoSuchFileException e) {
									// this object has no src, and that's fine
								}
								try {
									addElementWithCDataContentFromFile(WebServer.ROOT.resolve("objects/" + obj + "/languages/" + language + ".ini"), "language", eventWriter, EVENT_FACTORY);
								} catch (NoSuchFileException e) {
									// this object has no languages, and that's fine
								}
							}
						}
						
						eventWriter.add(event);
					}
				}
				eventWriter.add(NL);
				
			} finally {
				try { eventReader.close(); } catch (Exception e) {}
			}
		}
	    
	    eventWriter.add(EVENT_FACTORY.createEndElement("", "", "objects"));
	    eventWriter.add(NL);
	}

	/**
	 * 
	 * @param eventWriter
	 * @throws IOException
	 * @throws XMLStreamException
	 */
	private void addSkinDefinitionAndExtractIconSet(XMLEventWriter eventWriter) throws IOException, XMLStreamException {
		Path skinXmlPath = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(
				WebServer.ROOT.resolve("skins/" + skin + "/" + "definition.xml"));
		
		XMLEventReader eventReader = null;
		try (BufferedReader in = Files.newBufferedReader(skinXmlPath, Sfera.CHARSET)) {
	    	eventReader = INPUT_FACTORY.createXMLEventReader(in);
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
			eventWriter.add(NL);
			
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
		Path indexXml = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(
				WebServer.ROOT.resolve("interfaces/" + interfaceName + "/index.xml"));
		Path cacheXml = interfaceTmpCacheRoot.resolve("index.xml");		
		Files.copy(indexXml, cacheXml);
		
		XMLEventReader eventReader = null;
		try (BufferedReader in = Files.newBufferedReader(cacheXml, Sfera.CHARSET)) {
			eventReader = INPUT_FACTORY.createXMLEventReader(in);
			
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
		
		Path filePath = ResourcesUtil.getResourceFromPluginsIfNotInLocalDirectory(file);
		
		try (BufferedReader reader = Files.newBufferedReader(filePath, Sfera.CHARSET)) {
			eventWriter.add(eventFactory.createStartElement("", "", elementLocalName));
		    
		    StringBuilder content = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	content.append(line).append('\n');
		    }
		    
		    eventWriter.add(eventFactory.createCData(content.substring(0, content.length() - 1)));
		    
		    eventWriter.add(eventFactory.createEndElement("", "", elementLocalName));
		    eventWriter.add(NL);
		}
	}
	
	/**
	 * 
	 * @param uri
	 * @param token
	 * @param httpRequestHeader
	 * @param connectionHandler
	 * @return
	 * @throws IOException
	 */
	public static boolean processFileRequest(String uri, Token token, 
			HttpRequestHeader httpRequestHeader,
			ConnectionHandler connectionHandler) throws IOException {
		
		if (uri.charAt(0) == '/') {
			uri = uri.substring(1);
		}
		Path path = CACHE_ROOT.resolve(uri).toAbsolutePath().normalize();
		
		if (!path.startsWith(ABSOLUTE_CACHE_ROOT_PATH)) {
			WebServer.getLogger().warning("resource out of root requested: " + path);
			return false;
		}
		
		path = ABSOLUTE_CACHE_ROOT_PATH.relativize(path);
		
		String pathStr = path.toString();
		if (pathStr.equals("favicon.ico") || pathStr.equals("favicon.png")) {
			serveCacheFile(httpRequestHeader.method, path, connectionHandler, httpRequestHeader);
			return true;
		}
		
		String requestedInterface = path.getName(0).toString();
		
		if (requestedInterface.length() == 0 && defaultInterface != null) {
			connectionHandler.redirectTo(defaultInterface, httpRequestHeader);
			return false;
		}
		
		if (!interfaces.contains(requestedInterface)) {
			WebServer.getLogger().warning("non-existing interface requested: " + requestedInterface);
			return false;
		}
		
		if (httpRequestHeader.method != HttpRequestHeader.Method.GET && httpRequestHeader.method != HttpRequestHeader.Method.HEAD) {
			connectionHandler.notImplementedError();
			return false;
		}
		
		if (isLoginComponent(path)) { // GET /<interface>/login/...
			serveCacheFile(httpRequestHeader.method, path, connectionHandler, httpRequestHeader);
			
		} else {
			if (isLoginAlias(path)) { // GET /<interface>/login
				if (token != null) { // already authenticated
					connectionHandler.redirectTo(requestedInterface, httpRequestHeader);
					return false;
					
				} else {
					serveCacheFile(httpRequestHeader.method, path.resolve("index.html"), connectionHandler, httpRequestHeader);
				}
				
			} else if (isInterfaceAlias(path)) { // GET /<interface>
				if (token != null) {
					if (token.getUser().isAuthorized(path)) {
						serveCacheFile(httpRequestHeader.method, path.resolve("index.html"), connectionHandler, httpRequestHeader);
					} else {
						WebServer.getLogger().warning("unauthorized interface request: " + path);
						return false;
					}
					
				} else {
					connectionHandler.redirectTo(requestedInterface + "/login", httpRequestHeader);
					return false;
				}
				
			} else { // GET /<interface>/<any_other_resource>
				if (token != null && token.getUser().isAuthorized(path)) {
					serveCacheFile(httpRequestHeader.method, path, connectionHandler, httpRequestHeader);
					
				} else {
					WebServer.getLogger().debug("unauthorized file request: " + path);
					connectionHandler.notFoundError();
				}
			}
		}
		
	    return true;
	}
	
	/**
	 * 
	 * @param method
	 * @param path
	 * @param out
	 * @param dataOut
	 * @param httpRequestHeader
	 * @throws IOException
	 */
	private synchronized static void serveCacheFile(Method method, Path path, 
			ConnectionHandler connectionHandler, HttpRequestHeader httpRequestHeader) throws IOException {
		
		path = ABSOLUTE_CACHE_ROOT_PATH.resolve(path);
		
		try {
    		long lastModified = Files.getLastModifiedTime(path).toMillis();
    		
			if (lastModified <= httpRequestHeader.getIfModifiedSinceTime()) {
				connectionHandler.write("HTTP/1.1 304 Not Modified\r\n");
    			connectionHandler.write("Date: " + DateUtil.now() + "\r\n");
		        connectionHandler.write("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		        connectionHandler.write("Last-Modified: " + DateUtil.format(lastModified) + "\r\n");
    			connectionHandler.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
    			connectionHandler.write("Content-length: 0\r\n");
    			connectionHandler.write("\r\n");
    			connectionHandler.flush();
    			
    		} else {
				byte[] fileData = Files.readAllBytes(path);
	    		String contentType = getMimeContentType(path);
		    	
		        connectionHandler.write("HTTP/1.1 200 OK\r\n");
		        connectionHandler.write("Date: " + DateUtil.now() + "\r\n");
		        connectionHandler.write("Server: " + WebServer.HTTP_HEADER_FIELD_SERVER + "\r\n");
		        connectionHandler.write("Last-Modified: " + DateUtil.format(lastModified) + "\r\n");
		        connectionHandler.write("Cache-Control: max-age=0, no-cache, no-store\r\n");
		        if (contentType != null) {
		        	connectionHandler.write("Content-type: " + contentType + "\r\n");
		        }
		        connectionHandler.write("Content-length: " + fileData.length + "\r\n");
		        connectionHandler.write("\r\n");
		        connectionHandler.flush();

		        if (method == HttpRequestHeader.Method.GET) {
		        	connectionHandler.write(fileData);
		        	connectionHandler.flush();
		        }
    		}
		} catch (NoSuchFileException e) {
    		WebServer.getLogger().warning("file not found: " + ABSOLUTE_CACHE_ROOT_PATH.relativize(path));
    		connectionHandler.notFoundError();
		}
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private static boolean isInterfaceAlias(Path path) {
		return path.getNameCount() == 1;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	private static boolean isLoginAlias(Path path) {
		return (path.getNameCount() == 2 && path.getName(1).toString().equals("login"));
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	private static boolean isLoginComponent(Path path) {
		return (path.getNameCount() > 2 && path.getName(1).toString().equals("login"));
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private static String getMimeContentType(Path path) throws IOException {
		if (path.getFileName().toString().equals("manifest")) {
			return "text/cache-manifest";
		}
		// TODO test on linux, on OS X it looks like it's not working...
		return Files.probeContentType(path);
	}	
}
