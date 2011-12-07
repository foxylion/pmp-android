package de.unistuttgart.ipvs.pmp.resourcegroups.switches;

interface IWifiSwitch {
	
	/**
	 * 
	 * @return the current state of the wifi switch
	 */
	boolean getState();
	
	/**
	 * 
	 * @param newState the new state of the wifi switch
	 */
	void setState(boolean newState);

}
