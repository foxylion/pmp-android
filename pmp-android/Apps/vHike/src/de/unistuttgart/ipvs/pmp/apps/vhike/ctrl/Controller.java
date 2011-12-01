package de.unistuttgart.ipvs.pmp.apps.vhike.ctrl;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.JSonRequestReader;

/**
 * Controls the behaviour of vHike
 * @author Alexander Wassiljew
 *
 */
public class Controller {

	
	
	/**
	 * Constructor
	 */
	public Controller() {
	}
	
	/**
	 * Log on an user and save the session id in the {@link Model}
	 * @param username
	 * @param pw
	 * @return
	 */
	public boolean login(String username, String pw){
		
		String status = JSonRequestReader.login(username, pw);
		Log.i("Status im CTRL: " + status);
		if(status.equals("logged_in")){
			return true;
		}else {
			return false;	
		}
	}
	/**
	 * Log out an user 
	 * @param sid
	 * @return
	 */
	public boolean logout(String sid){
		String  status = JSonRequestReader.logout(sid);
		if(status.equals("logged_out")){
			return true;
		}else{
			return false;
		}
	}
}
