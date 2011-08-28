package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.PMPApplication;


public class DatabaseSingleton {

	private static final DatabaseSingleton instance = new DatabaseSingleton();
	
	private DatabaseOpenHelper doh = null;
	
	private DatabaseSingleton() {
		
	}
	
	public static DatabaseSingleton getInstance() {
		return instance;
	}
	
	public DatabaseOpenHelper getDatabaseHelper() {
		if(doh == null) {
			doh = new DatabaseOpenHelper(PMPApplication.getContext());
		}
		
		return doh;
	}
}
