package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.BatteryEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Adapter;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Status;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class Battery extends Filler {
	
	public Battery(Service service, long fromMillis, long toMillis) {
		super(service, fromMillis, toMillis);
	}
	
	
	@Override
	protected int maxSpreading() {
		return 240;
	}
	
	private BatteryEvent.Adapter lastPlugged = Adapter.NOT_PLUGGED;
	private BatteryEvent.Status lastStatus = Status.NOT_CHARGING;
	private float lastTemp = 21f;
	private byte lastLevel = 50;
	
	
	@Override
	public Event generateEvent(long time) {
		
		// Set level depending on last charging status
		if (lastStatus == Status.CHARGING) {
			lastLevel = (byte) (lastLevel + Math.random() * 25);
			if (lastLevel > 100) {
				lastLevel = 100;
			}
		} else if (lastStatus == Status.DISCHARGING) {
			lastLevel = (byte) (lastLevel - Math.random() * 20);
			if (lastLevel < 0) {
				lastLevel = 0;
			}
		}
		
		// Set adapter status randomly
		int pluggedInt = (int) (Math.random() * 4);
		switch (pluggedInt) {
			case 0:
				lastPlugged = Adapter.AC;
				break;
			case 1:
			case 2:
				lastPlugged = Adapter.NOT_PLUGGED;
				break;
			case 3:
				lastPlugged = Adapter.USB;
				break;
		}
		
		// Set charging status depending on adapter status
		if (lastPlugged == Adapter.NOT_PLUGGED) {
			if (Math.random() < 0.2) {
				lastStatus = Status.UNKNOWN;
			} else {
				lastStatus = Status.DISCHARGING;
			}
		} else {
			if (lastLevel >= 95) {
				lastStatus = Status.FULL;
			} else if (Math.random() < 0.2) {
				lastStatus = Status.NOT_CHARGING;
			} else {
				lastStatus = Status.CHARGING;
			}
		}
		
		// "Remove" battery if adapter is plugged and charging status is unknown
		boolean present = true;
		if (lastPlugged != Adapter.NOT_PLUGGED && lastStatus == Status.UNKNOWN) {
			if (Math.random() < 0.8) {
				present = false;
			}
		}
		
		lastTemp = lastTemp + ((float) (Math.random() - 0.5)) * 4;
		if (lastTemp > 100) {
			lastTemp = 100;
		}
		if (lastTemp < -100) {
			lastTemp = -100;
		}
		
		BatteryEvent event = new BatteryEvent(++id, time, lastLevel, lastPlugged, present, lastStatus, lastTemp);
		return event;
	}
	
	
	@Override
	public void uploadEvents(List<Event> events) {
		BatteryEventManager bem = new BatteryEventManager(service);
		
		try {
			bem.commitEvents(events);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
