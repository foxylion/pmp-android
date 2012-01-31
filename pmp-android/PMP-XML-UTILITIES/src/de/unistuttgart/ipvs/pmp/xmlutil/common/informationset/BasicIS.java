/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import java.util.ArrayList;
import java.util.List;

/**
 * This class abstracts common used fields and methods for information sets
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class BasicIS {

	/**
	 * This list contains all names.
	 */
	protected List<Name> names = new ArrayList<Name>();

	/**
	 * This list contains all descriptions.
	 */
	protected List<Description> descriptions = new ArrayList<Description>();

	/**
	 * Get all names.
	 * 
	 * @return list with names
	 */
	public List<Name> getNames() {
		return this.names;
	}

	/**
	 * Add a name.
	 * 
	 * @param name
	 *            name to add
	 */
	public void addName(Name name) {
		this.names.add(name);
	}

	/**
	 * Remove a name.
	 * 
	 * @param name
	 *            name to remove
	 */
	public void removeName(Name name) {
		this.names.remove(name);
	}

	/**
	 * Get all descriptions.
	 * 
	 * @return list with descriptions
	 */
	public List<Description> getDescriptions() {
		return this.descriptions;
	}

	/**
	 * Add a description
	 * 
	 * @param description
	 *            description to add
	 */
	public void addDescription(Description description) {
		this.descriptions.add(description);
	}

	/**
	 * Remove a description
	 * 
	 * @param description
	 *            description to remove
	 */
	public void removeDescription(Description description) {
		this.descriptions.remove(description);
	}

}
