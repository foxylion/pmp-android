package de.unistuttgart.ipvs.pmp.apps.vhike;

/**
 * Constants used by vHike
 * 
 * @author Alexander Wassiljew
 * 
 */
public class Constants {

	/*
	 * URL of the webservice which is used by {@link JSonRequestReader}
	 */
	public static final String WEBSERVICE_URL = "http://vhike.no-ip.org/json/";
	/**
	 * Test logins
	 */
	public static final String USER = "demo";
	public static final String PW = "test";
	// public static final String WEBSERVICE_URL
	// ="http://10.0.2.2/jsontest/call.php";

	public static final int REG_NOT_SUCCESSFUL = 0;
	public static final int REG_STAT_REGISTERED = 1;
	public static final int REG_STAT_USED_USERNAME = 10;
	public static final int REG_STAT_USED_MAIL = 11;
	public static final int REG_STAT_INVALID_USERNAME = 20;
	public static final int REG_STAT_INVALID_PW = 21;
	public static final int REG_STAT_INVALID_FIRSTNAME = 22;
	public static final int REG_STAT_INVALID_LASTNAME = 23;
	public static final int REG_STAT_INVALID_TEL = 24;

}
