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
package de.unistuttgart.ipvs.pmp.xmlutil.ais;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;

/**
 * This is a service feature, which is assigned to an app and contains all
 * required resource groups of the app.
 * 
 * @author Marcus Vetter
 * 
 */
public class AISServiceFeature extends BasicIS implements Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -3279934293726339125L;

	/**
	 * The identifier
	 */
	private String identifier;

	/**
	 * This list contains all required resource groups of the service feature.
	 */
	private List<AISRequiredResourceGroup> requiredResourceGroups = new ArrayList<AISRequiredResourceGroup>();

	/**
	 * Constructor without attributes
	 */
	public AISServiceFeature() {
	}

	/**
	 * Constructor to set the identifier
	 * 
	 * @param identifier
	 *            identifier to set
	 */
	public AISServiceFeature(String identifier) {
		this.setIdentifier(identifier);
	}

	/**
	 * Get all required resource groups of the service feature
	 * 
	 * @return list with required resource groups
	 */
	public List<AISRequiredResourceGroup> getRequiredResourceGroups() {
		return this.requiredResourceGroups;
	}

	/**
	 * Add a required resource group to the service feature
	 * 
	 * @param rrg
	 *            the required Resourcegroup to add
	 */
	public void addRequiredResourceGroup(AISRequiredResourceGroup rrg) {
		this.requiredResourceGroups.add(rrg);
	}

	/**
	 * Remove a required resource group from the service feature
	 * 
	 * @param rrg
	 *            required resource group to remove
	 */
	public void removeRequiredResourceGroup(AISRequiredResourceGroup rrg) {
		this.requiredResourceGroups.remove(rrg);
	}

	/**
	 * Get a required resource group for a given identifier. Null, if no
	 * required resource group exists for the given identifier.
	 * 
	 * @param identifier
	 *            identifier of the required resource group
	 * @return required resource group with given identifier, null if none
	 *         exists.
	 */
	public AISRequiredResourceGroup getRequiredResourceGroupForIdentifier(
			String identifier) {
		for (AISRequiredResourceGroup rrg : this.requiredResourceGroups) {
			if (rrg.getIdentifier().equals(identifier))
				return rrg;
		}
		return null;
	}

	/**
	 * Get the identifier
	 * 
	 * @return identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Set the identifier
	 * 
	 * @param identifier
	 *            identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

}
