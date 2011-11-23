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
package de.unistuttgart.ipvs.pmp.util.xml.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractInformationSet;

/**
 * This is an information set of the app. It contains all basic informations
 * (names and descriptions in different locals) and all provided service
 * features.
 * 
 * @author Marcus Vetter
 */
public class AppInformationSet extends AbstractInformationSet implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 2629559699588037711L;

	/**
	 * This list contains all service features of the app.
	 */
	private List<ServiceFeature> serviceFeatures;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected AppInformationSet() {
		super();
		this.serviceFeatures = new ArrayList<ServiceFeature>();
	}

	/**
	 * Get all service features of the app
	 * 
	 * @return list of service featues
	 */
	public List<ServiceFeature> getServiceFeatures() {
		return this.serviceFeatures;
	}

	/**
	 * Add a service feature to the app
	 * 
	 * @param sf
	 *            service feature
	 */
	protected void addServiceFeature(ServiceFeature sf) {
		this.serviceFeatures.add(sf);
	}

}
