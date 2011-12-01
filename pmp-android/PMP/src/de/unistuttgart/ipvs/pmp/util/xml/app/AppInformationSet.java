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
import java.util.HashMap;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * This is an information set of the app. It contains all basic informations
 * (names and descriptions in different locals) and all provided service
 * features.
 * 
 * @author Marcus Vetter
 */
public class AppInformationSet extends AbstractInformationSet implements
		Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = 2629559699588037711L;

	/**
	 * This map contains all service features of the app. key = identifier of
	 * the service feature
	 */
	private Map<String, ServiceFeature> serviceFeaturesMap;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected AppInformationSet() {
		super();
		this.serviceFeaturesMap = new HashMap<String, ServiceFeature>();
	}

	/**
	 * Add a service feature to the app
	 * 
	 * @param identifier
	 *            of the service feature
	 * @param sf
	 *            service feature
	 */
	protected void addServiceFeature(String identifier, ServiceFeature sf) {
        if (this.serviceFeaturesMap.containsKey(identifier)) {
            throw new XMLParserException(Type.SERVICE_FEATURE_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
                    "A Service Feature with the identifier " + identifier + " already exists.");
        }
		this.serviceFeaturesMap.put(identifier, sf);
	}

	/**
	 * Get the map which contains all service features
	 * 
	 * @return map with service features
	 */
	public Map<String, ServiceFeature> getServiceFeaturesMap() {
		return serviceFeaturesMap;
	}

}
