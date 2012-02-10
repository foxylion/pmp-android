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
    
    //REGISTRATION MSG
    public static final int REG_NOT_SUCCESSFUL = 0;
    public static final int REG_STAT_REGISTERED = 1;
    public static final int REG_STAT_USED_USERNAME = 10;
    public static final int REG_STAT_USED_MAIL = 11;
    public static final int REG_STAT_INVALID_USERNAME = 20;
    public static final int REG_STAT_INVALID_PW = 21;
    public static final int REG_STAT_INVALID_FIRSTNAME = 22;
    public static final int REG_STAT_INVALID_LASTNAME = 23;
    public static final int REG_STAT_INVALID_TEL = 24;
    
    //STATUS MSG
    public static final int STATUS_UPDATED = 50;
    public static final int STATUS_UPTODATE = 51;
    public static final int STATUS_NOTRIP = 52;
    public static final int STATUS_HASENDED = 53;
    public static final int STATUS_INVALID_USER = 54;
    public static final int STATUS_QUERY_DELETED = 55;
    public static final int STATUS_NO_QUERY = 56;
    public static final int STATUS_SENT = 57;
    public static final int STATUS_INVALID_TRIP = 58;
    public static final int STATUS_INVALID_QUERY = 59;
    
    public static final int TRIP_STATUS_ANNOUNCED = 60;
    public static final int TRIP_STATUS_OPEN_TRIP = 61;
    
    public static final int STATUS_ALREADY_SENT = 62;
    public static final int STATUS_HANDLED = 63;
    public static final int STATUS_INVALID_OFFER = 64;
    public static final int STATUS_UNREAD = 65;
    public static final int STATUS_ACCEPTED = 66;
    public static final int STATUS_DENIED = 67;
    
    public static final int V_OBJ_SATUS_FOUND = 70;
    public static final int V_OBJ_SATUS_INVITED = 71;
    public static final int V_OBJ_SATUS_AWAIT_ACCEPTION = 72;
    public static final int V_OBJ_SATUS_ACCEPTED = 73;
    public static final int V_OBJ_SATUS_PICKED_UP = 74;
    public static final int V_OBJ_SATUS_BANNED = 75;
    // ERRORS
    public static final int STATUS_ERROR = 999;
    public static final int QUERY_ID_ERROR = 998;
    
    public static final String ROLE_DRIVER = "driver";
    public static final String ROLE_PASSENGER = "passenger";
    
}
