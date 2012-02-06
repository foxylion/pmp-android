package de.unistuttgart.ipvs.pmp.resourcegroups.location;

import android.location.Address;
import android.location.Location;

/**
 * Class to calculate the distance between two points in arbitrary units.
 * 
 */
public class Distance {
	
	/** Names for the units to use */
	public final static int KILOMETERS = 0;
	public final static int STATUTE_MILES = 1;
	public final static int NAUTICAL_MILES = 2;
	
	/** Radius of the Earth in the units above */
	private final static double EARTHS_RADIUS[] = { 6378.1, // Kilometers
			3963.1676, // Statue miles
			3443.89849 // Nautical miles
	};
	
	/** Conversion factor to convert from degrees to radians */
	private static final double DEGREES_TO_RADIANS = (double) (180 / Math.PI);
	
	
	/**
	 * Calculates the "length" of an arc between two points on a sphere given the latitude &
	 * longitude of those points.
	 * 
	 * @param aLat
	 *            Latitude of point A
	 * @param aLong
	 *            Longitude of point A
	 * @param bLat
	 *            Latitude of point B
	 * @param bLong
	 *            Longitude of point B
	 * @return
	 */
	public static double calclateArc(double aLat, double aLong, double bLat, double bLong, int units) {
		/*
		 * Convert location a and b's lattitude and longitude
		 * from degrees to radians
		 */
		double aLatRad = aLat / DEGREES_TO_RADIANS;
		double aLongRad = aLong / DEGREES_TO_RADIANS;
		double bLatRad = bLat / DEGREES_TO_RADIANS;
		double bLongRad = bLong / DEGREES_TO_RADIANS;
		
		// Calculate the length of the arc that subtends point a and b
		double t1 = Math.cos(aLatRad) * Math.cos(aLongRad) * Math.cos(bLatRad) * Math.cos(bLongRad);
		double t2 = Math.cos(aLatRad) * Math.sin(aLongRad) * Math.cos(bLatRad) * Math.sin(bLongRad);
		double t3 = Math.sin(aLatRad) * Math.sin(bLatRad);
		double tt = Math.acos(t1 + t2 + t3);
		
		// Return a "naked" length for the calculated arc
		return tt * EARTHS_RADIUS[units];
	}
}
