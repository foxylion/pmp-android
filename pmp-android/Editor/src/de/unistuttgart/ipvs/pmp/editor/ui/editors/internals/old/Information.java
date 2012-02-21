/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.old;


/**
 * This class is a in-memory representation of a row in the information-table
 * @author Patrick Strobel
 *
 */
public class Information {
	
	private String locale;
	private String name;
	private String description;
	private StoredInformation store;
	
	public Information(String locale, String name, String description) {
		this.locale = locale;
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Stores a reference to the information stored.
	 * Required to update the hashmap's key whenever the locale is changed
	 * @param store
	 */
	protected void setStore(StoredInformation store) {
		this.store = store;
	}
	

	public void setLocale(String locale) {
		store.changeLocale(this.locale, locale);
		this.locale = locale;
	}
	
	public String getLocale() {
		return locale;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {

		return locale + ", " + name + ", " + description;
	}	
	

}
