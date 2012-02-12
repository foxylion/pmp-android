package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all information provided in the information table
 * 
 * @author Patrick Strobel
 * 
 */
public class StoredInformation {
	
	private Map<String,Information> information =  new HashMap<String,Information>();
	
	public StoredInformation() {
		
	}
	
	/**
	 * Adds a name to a given locale or creates a new information-set if there is no entry for the given locale
	 * @param locale
	 * @param name
	 */
	public void addName(String locale, String name) {
		if (information.containsKey(locale)) {
			information.get(locale).setName(name);
		} else {
			add(new Information(locale, name, null));
		}
	}
	
	/**
	 * Adds a description to a given locale or creates a new information-set if there is no entry for the given locale
	 * @param locale
	 * @param name
	 */
	public void addDescription(String locale, String desc) {
		if (information.containsKey(locale)) {
			information.get(locale).setDescription(desc);
		} else {
			add(new Information(locale, null, desc));
		}
	}

	/**
	 * Adds a new information-set to the store
	 * @param i
	 */
	public void add(Information i) {
		i.setStore(this);
		information.put(i.getLocale(), i);
	}
	
	/**
	 * Removes a existing information-set
	 * @param locale	Locale of the information-set to remove
	 */
	public void remove(String locale) {
		information.remove(locale);
	}
	
	public boolean localeExists(String locale) {
		return information.containsKey(locale);
	}
	
	protected Map<String,Information> getMap() {
		return information;
	}
	
	/**
	 * Changes the key of a map-entry into a new key
	 * @param oldL	Old locale
	 * @param newL	New Locale
	 */
	protected void changeLocale(String oldL, String newL) {
		Information i = information.remove(oldL);
		information.put(newL, i);
	}
 
}
