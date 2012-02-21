/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.tools;

public class RideObject {

	int tripid =0;
	int seats=0;
	float cur_lat=0;
	float cur_lon=0;
	String destination="";
	int driverid=0;
	String username ="";
	float rating=0;
	float distance=0;
	public RideObject(int tripid, int seats, float cur_lat, float cur_lon,
			String destination, int driverid, String username, float rating,
			float distance) {
		super();
		this.tripid = tripid;
		this.seats = seats;
		this.cur_lat = cur_lat;
		this.cur_lon = cur_lon;
		this.destination = destination;
		this.driverid = driverid;
		this.username = username;
		this.rating = rating;
		this.distance = distance;
	}
	public int getTripid() {
		return tripid;
	}
	public int getSeats() {
		return seats;
	}
	public float getCur_lat() {
		return cur_lat;
	}
	public float getCur_lon() {
		return cur_lon;
	}
	public String getDestination() {
		return destination;
	}
	public int getDriverid() {
		return driverid;
	}
	public String getUsername() {
		return username;
	}
	public float getRating() {
		return rating;
	}
	public float getDistance() {
		return distance;
	}


}
