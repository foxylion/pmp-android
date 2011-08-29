package de.unistuttgart.ipvs.pmp.model;

import de.unistuttgart.ipvs.pmp.PMPApplication;

/**
 * {@link DatabaseSingleton} provides an instance of the
 * {@link DatabaseOpenHelper}, and can be received by calling
 * <code>DatabaseSingleton.getInstance().getDatabaseHelper();</code>.
 * 
 * @author FoXyLiOn
 * 
 */
public class DatabaseSingleton {

	/**
	 * The instance.
	 */
	private static final DatabaseSingleton instance = new DatabaseSingleton();

	/**
	 * A {@link DatabaseOpenHelper} instance.
	 */
	private DatabaseOpenHelper doh = null;

	/**
	 * Private constructor, prevents direct object creation.
	 */
	private DatabaseSingleton() {

	}

	/**
	 * @return Returns an instance of the {@link DatabaseSingleton} class.
	 */
	public static DatabaseSingleton getInstance() {
		return instance;
	}

	/**
	 * @return Returns an instance of the {@link DatabaseOpenHelper} class.
	 */
	public DatabaseOpenHelper getDatabaseHelper() {
		if (doh == null) {
			doh = new DatabaseOpenHelper(PMPApplication.getContext());
		}

		return doh;
	}
}
