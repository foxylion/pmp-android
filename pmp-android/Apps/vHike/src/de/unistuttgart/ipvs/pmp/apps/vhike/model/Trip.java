package de.unistuttgart.ipvs.pmp.apps.vhike.model;

import java.util.Calendar;

import de.unistuttgart.ipvs.pmp.apps.vhike.tools.TripPersonObject;

/**
 * @author Alexander Wassiljew
 */
public class Trip {

	// `id` int(8) NOT NULL AUTO_INCREMENT,
	// `driver` int(8) NOT NULL,
	// `avail_seats` int(8) NOT NULL,
	// `destination` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
	// `creation` datetime NOT NULL,
	// `ending` datetime NOT NULL,

	TripPersonObject	driver;
	private int			id;
	private int			driverID;
	private int			availSeats;
	private String		destination;
	private Calendar	creation;
	private Calendar	ending;

	public Trip() {

	}

	public Trip(int id, int driverID, int availSeats, String destination, long creation, long ending) {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		// TODO Check ID
		this.id = id;
	}

}
