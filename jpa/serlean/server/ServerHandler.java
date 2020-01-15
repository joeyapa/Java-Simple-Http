package jpa.serlean.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import jpa.serlean.helper.FileHelper;

public class ServerHandler implements HttpHandler {

	private static final Logger LOG = Logger.getLogger( ServerHandler.class.getName() );
		
	private static Map<String,String> staticURIContent;
	private static Map<String,Long> markedURI;
	private static long markedURIExpiry = 300000;
	
	private ServerConfig config;
	
	static {		
		staticURIContent = new HashMap<String,String>();
		markedURI = new HashMap<String,Long>();
	}
	
	public ServerHandler(){		
		this.config = new ServerConfig();		
	}
	
	public ServerHandler(ServerConfig config){
		this.config = config;
		switch( config.getLoggerLevel() ) {
			case "WARNING": LOG.setLevel(Level.WARNING); break;
			case "INFO": LOG.setLevel(Level.INFO); break;
			case "FINE": LOG.setLevel(Level.FINE); break;
			case "FINEST": LOG.setLevel(Level.FINEST); break;			
			default: LOG.setLevel(Level.ALL); break;
		}
		
	}
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		int code = 200;
		byte[] response = config.getPage200().getBytes();
		boolean isPost = "POST".equalsIgnoreCase(t.getRequestMethod());
				
		String path = t.getRequestURI().getPath();
		path = path.length()>1 ? path.startsWith("/") ? path.substring(1) : path : "";
		String content = isPost ? "{\"state\":\"200\",\"method\":\"POST\"}"  : this.getStaticContent(path);
		
		String[] uri = path.split("/");
		Map<String,String> query = this.queryToMap(t.getRequestURI().getQuery());					
		
		if( isPost ) {
			String sb = FileHelper.getStringInputStream(t.getRequestBody());
			if( sb!=null && !sb.isEmpty() ) {
				query.putAll(this.queryToMap(sb));
			}			
		}		

		LOG.fine("Requesting URI " + path + ", Query " + query.keySet());
		 
		if( content==null ) {
			code = 404;
			content = config.getPage404();
		}		
		else if( uri!=null && uri.length>0 ) {
			
		}
				
		response = content.getBytes();
		t.sendResponseHeaders(code, response.length);
		OutputStream os = t.getResponseBody();
		os.write(response);
		os.close();
	}
	
	/**
	 * Convert the query request to hash map 
	 * 
	 * @param query
	 * @return
	 */
	private Map<String,String> queryToMap(String query){
		Map<String,String> map = new HashMap<String,String>();
		if( query!=null && !query.isEmpty()) {
			String[] pairs = query.split("&");
			for(String pair : pairs) {
				if( pair!=null && !pair.isEmpty() ) {
					String[] keyvalue = pair.split("=");
					if( keyvalue.length == 2 ) {
						map.put(keyvalue[0], keyvalue[1]);
					}					
				}
				
			}
		}
		return map;
	}	
	
	/**
	 * 
	 * @param path
	 * @return
	 */
	private String getStaticContent(String path) {
		String content = "{\"state\":\"200\",\"method\":\"GET\"}";
		
		// 1. Check if it refers to the default domain path 
		if( path==null || path.trim().isEmpty() ) {
			return config.getPagehome();
		}
		
		// 2. Grant exception to path that starts with dynamic URI
		if( !config.getDynamicURI().isEmpty() ) {
			for(String uri : config.getDynamicURI()) {
				if( path.startsWith(uri) ) {
					return content;
				}
			}
		}
		
		// 3. Is the URI restricted to the list of process or static URI
		if( config.isStrictURI()
			&& !config.getProcessURI().contains(path)
			&& !config.getStaticURI().contains(path) ) {
				return null;
		}
		
		// 4. Is the URI marked with expire, then wait until the refresh cycle 
		if( config.isMarkedURI() 
			&& markedURI.containsKey(path) 
			&& markedURI.get(path) > new Date().getTime()  ) {
				return null;
		}
		
		// 5. Is the URI cached
		if( config.isCacheStaticURI() ) {
			content = staticURIContent.get(path);
			if( content == null ) {
				content = FileHelper.getFileContent(path, null);				
			}
			if( content != null ) {
				staticURIContent.put(path, content);
			}								
		}
		else {
			content = FileHelper.getFileContent(path, null);
		}
		
		// 6. Flag marked null content
		if( config.isMarkedURI() && content==null ) {
			markedURI.put(path, new Date().getTime() + markedURIExpiry);
		}
					
		return content;	 
	}
}