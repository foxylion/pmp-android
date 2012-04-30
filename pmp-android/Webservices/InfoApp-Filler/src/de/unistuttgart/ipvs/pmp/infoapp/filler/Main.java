package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.ConnectionEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.ConnectionEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidEventIdException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.properties.BatteryProperties;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.properties.DeviceProperties;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.properties.DeviceProperties.DeviceOem;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.properties.DeviceProperties.Display;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.properties.DeviceProperties.Memory;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Setup service url and device ID
		//Service s = new Service("http://localhost/infoapp/src/json", "b7c2e4787e7f950c89909795907208d3");
		Service s = new Service(Service.DEFAUL_URL, "b7c2e4787e7f950c89909795907208d3");
		
		// Create some events and...
		ConnectionEvent e1 = new ConnectionEvent(1, 123, ConnectionEvent.Mediums.BLUETOOTH, true, true, "Stuttgart");
		ConnectionEvent e2 = new ConnectionEvent(2, 1337, ConnectionEvent.Mediums.WIFI, false, false, "Stuttgart");
		
		// ...bind them to a list
		List<ConnectionEvent> events = new ArrayList<ConnectionEvent>();
		events.add(e1);
		events.add(e2);
		
		// Contact the service and upload the events
		ConnectionEventManager cem = new ConnectionEventManager(s);
		try {
			System.out.println("LastID: "+cem.getLastId());
			cem.commitEvents(events);
		}catch (InvalidEventIdException e) {
			System.out.println("ID in use!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Set device properties
		DeviceOem deviceOem = new DeviceOem("Motorola", "Defy", "Motoblur");
		Display display = new Display((short)800, (short)600);
		Memory memoryInt = new Memory((short)2048, (short)123);
		Memory memoryExt = new Memory((short)8192, (short)1024);
		String[] sensors = {"compass", "gps", "tilt"};
		DeviceProperties dp = new DeviceProperties(s, deviceOem, (byte)7, "2.6", display, (short)800, memoryInt, memoryExt, 3.8f, sensors, 12345.76f);
		try {
			dp.commit();
		} catch (InternalDatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
