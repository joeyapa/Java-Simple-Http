package jpa.serlean.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import com.sun.net.httpserver.HttpServer;

public class Server {
	
	private static final Logger LOG = Logger.getLogger( Server.class.getName() );
	
	private HttpServer httpServer;
	private ServerConfig config;
	
	/**
	 * Server main arguments
	 * 
	 * 0: configuration file path or 0 for no configuration  
	 * 1: port number
	 * 2: default root path
	 * 
	 * @param args
	 */
	public static void main(String[] args) {		
		Server server = new Server();
		ServerConfig config = server.getConfig();
		
		if( args!=null ) {			
			switch (args.length) {
				case 1: 
					config.setPort( Integer.valueOf(args[0]) );
					break;
				case 2: 
					config.setPort( Integer.valueOf(args[0]) );
					config.setDirRoot( args[1] );
					break;
				default:
					config.setPort( 80 );
					break;
			}			
		}		
		server.start();		
	}
	
	public void start(){
		try {
			LOG.info("Server start at port " + config.getPort());
			httpServer = HttpServer.create(new InetSocketAddress(config.getPort()), 0);
			httpServer.createContext("/", new ServerHandler());
			httpServer.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
			httpServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	public ServerConfig getConfig() {		
		return config = config==null ? new ServerConfig() : config;
	}

	public void setConfig(ServerConfig config) {
		this.config = config;
	}
	
}
