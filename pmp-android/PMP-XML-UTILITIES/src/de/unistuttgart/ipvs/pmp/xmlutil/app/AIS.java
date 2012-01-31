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
package de.unistuttgart.ipvs.pmp.xmlutil.app;


import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;


/**
 * This is an information set of the app. It contains all basic informations
 * (names and descriptions in different locals) and all provided service
 * features.
 * 
 * @author Marcus Vetter
 */
public class AIS extends BasicIS {

	/**
	 * This list contains all service features of the app.
	 */
	private List<ServiceFeature> serviceFeatureList;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	public AIS() {
		super();
		this.serviceFeatureList = new ArrayList<ServiceFeature>();
	}

	/**
	 * Add a service feature to the app
	 * 
	 * @param sf
	 *            service feature
	 */
	public void addServiceFeature(ServiceFeature sf) {
		this.serviceFeatureList.add(sf);
	}

	/**
	 * Remove a service feature of the app
	 * 
	 * @param sf
	 *            service feature
	 */
	public void removeServiceFeature(ServiceFeature sf) {
		this.serviceFeatureList.remove(sf);
	}

	/**
	 * Get the list which contains all service features
	 * 
	 * @return list with service features
	 */
	public List<ServiceFeature> getServiceFeatures() {
		return this.serviceFeatureList;
	}

}
