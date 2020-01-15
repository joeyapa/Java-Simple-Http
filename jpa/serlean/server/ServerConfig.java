package jpa.serlean.server;

import java.util.ArrayList;
import java.util.List;

public class ServerConfig {

	protected int port;	
	protected String dirRoot;
	protected String indexName;
	
	protected List<String> processURI;	
	protected List<String> staticURI;
	protected List<String> dynamicURI;
	
	protected String pagehome;
	protected String page200;
	protected String page404;
	protected String page500;
	
	protected String loggerLevel;
	
	protected boolean cacheStaticURI;
	protected boolean markedURI;
	protected boolean strictURI;
	
	public ServerConfig() {
		this.markedURI = true;
	}
	
	public int getPort() {
		return port = port<=0 ? 80 : port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDirRoot() {
		return dirRoot = dirRoot==null ? "" : dirRoot;
	}

	public void setDirRoot(String dirRoot) {
		this.dirRoot = dirRoot;
	}

	public String getIndexName() {
		return indexName = indexName==null ? "index.html" : indexName;
	}

	public void setIndexName(String indexName) {
		this.indexName = indexName;
	}

	public String getPagehome() {
		return pagehome = pagehome==null 
			? "<html><head></head><body><h1>Home page</h1></body></html>" 
			: pagehome;
	}

	public void setPagehome(String pagehome) {
		this.pagehome = pagehome;
	}

	public String getPage200() {
		return page200 = page200==null ? "" : page200;
	}

	public void setPage200(String page200) {
		this.page200 = page200;
	}

	public String getPage404() {
		return page404 = page404==null 
			? "<html><head></head><body><h1>Page not found.</h1></body></html>" 
			: page404;
	}

	public void setPage404(String page404) {
		this.page404 = page404;
	}

	public String getPage500() {
		return page500 = page500==null 
				? "<html><head></head><body><h1>Server error.</h1></body></html>" 
				: page500;
	}

	public void setPage500(String page500) {
		this.page500 = page500;
	}

	public String getLoggerLevel() {
		if( loggerLevel==null ) {
			loggerLevel = "INFO";
		}
		return loggerLevel.toUpperCase();
	}

	public void setLoggerLevel(String loggerLevel) {
		this.loggerLevel = loggerLevel;
	}

	public List<String> getProcessURI() {
		if(processURI==null) {
			processURI = new ArrayList<String>();
		}
		return processURI;
	}

	public void setProcessURI(List<String> processURI) {
		this.processURI = processURI;
	}

	public List<String> getStaticURI() {
		if(staticURI==null) {
			staticURI = new ArrayList<String>();
		}
		return staticURI;
	}

	public void setStaticURI(List<String> staticURI) {
		this.staticURI = staticURI;
	}
	
	public List<String> getDynamicURI() {
		if(dynamicURI==null) {
			dynamicURI = new ArrayList<String>();
		}
		return dynamicURI;
	}

	public void setDynamicURI(List<String> dynamicURI) {
		this.dynamicURI = dynamicURI;
	}

	public boolean isCacheStaticURI() {
		return cacheStaticURI;
	}

	public void setCacheStaticURI(boolean cacheStaticURI) {
		this.cacheStaticURI = cacheStaticURI;
	}

	public boolean isMarkedURI() {
		return markedURI;
	}

	public void setMarkedURI(boolean markedURI) {
		this.markedURI = markedURI;
	}

	public boolean isStrictURI() {
		return strictURI;
	}

	public void setStrictURI(boolean strictURI) {
		this.strictURI = strictURI;
	}

	
	
}
