package de.unistuttgart.ipvs.pmp.jpmpps;

import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.jpmpps.model.ResourceGroup;

/**
 * Java Privacy Management Platform Package Server Class for Connection handling
 * and initializing of service.
 * 
 * @author Jakob Jarosch
 */
public class JPMPPS {
	
	public static final int LISTEN_PORT = 3133;
	
	private String path;
	
	private Map<String, ResourceGroup> resourceGroups = new HashMap<String, ResourceGroup>();
	
	/**
	 * Creates a new instance of the service.
	 * @param path
	 */
	public JPMPPS(String path) {
		this.path = path;
		
		initialize();
		startServer();
	}

	/**
	 * Initializes the server and loads all informations sets from the apk files.
	 */
	private void initialize() {

	}

	/**
	 * Starts the server and begins to listen to the specified port.
	 */
	private void startServer() {

	}
}
