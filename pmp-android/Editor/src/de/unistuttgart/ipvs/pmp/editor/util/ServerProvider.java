package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import de.unistuttgart.ipvs.pmp.editor.ui.preferences.PreferenceInitializer;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestRGIS;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestResourceGroups;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.RGISResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.ResourceGroupsResponse;
import de.unistuttgart.ipvs.pmp.jpmpps.model.LocalizedResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Handles the access to the resource-group server to gather all available
 * resource-groups and represents them as a list of {@code RGIS}
 *  
 * @author Patrick Strobel
 */
public class ServerProvider implements IServerProvider {

	private List<RGIS> rgisList = null;
	private static ServerProvider instance = null;
	
	/**
	 * @deprecated Use {@code getInstance()}
	 */
	public ServerProvider() {
		
	}
	
	public static ServerProvider getInstance() {
		if (instance == null) {
			instance = new ServerProvider();
		}
		return instance;
	}

	@Override
	public List<RGIS> getAvailableRessourceGroups() throws IOException {
		// If this is the first access to the list, gather RGIS from server
		if (rgisList == null) {
			updateResourceGroupList();
		}

		return rgisList;
	}

	@Override
	public void updateResourceGroupList() throws IOException {
		Socket server = null;
		ObjectOutputStream out = null;
		try {
			// Establish a TCP-Connection to the server
			server = new Socket(PreferenceInitializer.getJpmppsHostname(), 
					PreferenceInitializer.getJpmppsPort());
			server.setSoTimeout(PreferenceInitializer.getJpmppsTimeout() * 1000);

			// Request a list of all available RGs
			out = new ObjectOutputStream(
					server.getOutputStream());

			ObjectInputStream in = new ObjectInputStream(
					server.getInputStream());
			out.writeObject(new RequestResourceGroups("en"));

			// Parse list of resource-groups
			Object response = in.readObject();

			if (response instanceof ResourceGroupsResponse) {
				ResourceGroupsResponse rgs = (ResourceGroupsResponse) response;
				buildRGISList(rgs.getResourceGroups(), in, out);
			} else {
				throw new IOException("Unsupported response from server.");
			}

		} catch (ClassNotFoundException e) {
			throw new IOException("Invalid response from server.");
		} catch (UnknownHostException e) {
			throw new IOException("Unable to lookup the server's IP-address.");
		} catch (IOException e) {
			throw new IOException("Unable to contact the server. " + e.getLocalizedMessage());
		} finally {
			if (server != null) {
				try {
					// Disconnect from RG-Server
					if (out != null) {
						out.writeObject(new RequestCommunicationEnd());
					}
					
					// Close connection
					server.close();
				} catch (IOException e) {
				}
			}
		}
	}

	/**
	 * Builds the RGIS-List using the data received in
	 * {@code ResourceGroupsResponse}.
	 * 
	 * @param locRGArray
	 * @param in
	 * @param out
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void buildRGISList(LocalizedResourceGroup[] locRGArray,
			ObjectInputStream in, ObjectOutputStream out) throws IOException,
			ClassNotFoundException {
		
		// Clear-List
		if (rgisList == null) {
			rgisList = new ArrayList<RGIS>(locRGArray.length);
		} else {
			rgisList.clear();
		}

		// Request RGIS from Server and add them to the list
		for (LocalizedResourceGroup localizedRG : locRGArray) {
			String packageName = localizedRG.getIdentifier();
			out.writeObject(new RequestRGIS(packageName));
			
			Object response = in.readObject();
			if (response instanceof RGISResponse) {
				RGISResponse rgisRes = (RGISResponse) response;
				rgisList.add((RGIS) rgisRes.getRGIS());
			} else {
				throw new IOException("Unsupported response from server.");
			}
		}
	}
}