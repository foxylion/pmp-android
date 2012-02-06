package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.eclipse.core.internal.runtime.Log;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestRGIS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.RGISResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

public class ServerProvider implements IServerProvider {

	/**
	 * URL and port could be set using preferences
	 */
	private String serverUrl = "localhost";
	private int serverPort = 3133;
	
	private List<RGIS> rgisList = null;
	
    public List<RGIS> getAvailableRessourceGroups() {
    	return null;
    }
    
    /**
     * Contacts the server to to gather all currently available 
     * resource-groups and update the RGIS-List.
     */
    public void updateRessourceGroupList() {
    	Socket server = null;
    	try {
        	// Establish a TCP-Connection to the server
			server = new Socket(serverUrl, serverPort);
			//server.setSoTimeout(10000);
			

			// Request a list of all available RGs
			ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(server.getInputStream());
			out.writeObject(new RequestResourceGroups("en"));
			System.out.println("Request sent!");
			
			// Parse list of resource-groups
			Object response = in.readObject();
			System.out.println("Response received!");
			
			if (response instanceof ResourceGroupsResponse) {
				ResourceGroupsResponse rgs = (ResourceGroupsResponse)response;
				buildRGISList(rgs.getResourceGroups(), in, out);
			}
    	} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (server != null) {
				try {
					server.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
    
    private void buildRGISList(LocalizedResourceGroup[] locRGArray, ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
    	for (LocalizedResourceGroup localizedRG : locRGArray) {
    		String packageName = "database";
			System.out.println("Res.: " + localizedRG.getIdentifier());
			out.writeObject(new RequestRGIS(packageName));
			Object response = in.readObject();
			if (response instanceof RGISResponse) {
				RGISResponse rgis = (RGISResponse)response;
				RGIS set = (RGIS)rgis.getRGIS();
				System.out.println("RGIS: " + set.getIdentifier());
			}
		}
    }
}