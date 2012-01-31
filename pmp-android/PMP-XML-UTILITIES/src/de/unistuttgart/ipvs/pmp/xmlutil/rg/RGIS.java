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
package de.unistuttgart.ipvs.pmp.xmlutil.rg;

import java.util.ArrayList;
import java.util.List;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;

/**
 * 
 * The resource group information set contains all basic information about the
 * resource group. It also contains all provided privacy settings
 * 
 * @author Marcus Vetter
 * 
 */
public class RGIS extends BasicIS {

	/**
	 * The identifier of the resource group
	 */
	private String identifier;

	/**
	 * The revision of the resource group
	 */
	private String revision;

	/**
	 * The icon of the resource group
	 */
	private String iconLocation;

	/**
	 * This list contains all privacy settings of the resource group.
	 */
	private List<PrivacySetting> privacySettings = new ArrayList<PrivacySetting>();

	/**
	 * Add a privacy setting to the resourcegroup
	 * 
	 * @param privacySetting
	 *            privacy setting to add
	 */
	public void addPrivacySetting(PrivacySetting privacySetting) {
		this.privacySettings.add(privacySetting);
	}

	/**
	 * Get the list which contains all privacy settings
	 * 
	 * @return list with privacy settings
	 */
	public List<PrivacySetting> getPrivacySettings() {
		return this.privacySettings;
	}

	/**
	 * Remove a privacy setting from resource group
	 * 
	 * @param privacySetting
	 *            privacy setting to remove
	 */
	public void removePrivacySetting(PrivacySetting privacySetting) {
		this.privacySettings.remove(privacySetting);
	}

	/**
	 * Get the identifier of the resource group
	 * 
	 * @return the identifier
	 */
	public String getIdentifier() {
		return this.identifier;
	}

	/**
	 * Set the identifier of the resource group
	 * 
	 * @param identifier
	 *            the identifier of the resource group
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * Get the revision of the resource group
	 * 
	 * @return the revision
	 */
	public String getRevision() {
		return this.revision;
	}

	/**
	 * Set the revision of the resource group
	 * 
	 * @param revision
	 *            the revision of the resource group
	 */
	public void setRevision(String revision) {
		this.revision = revision;
	}

	/**
	 * Get the location of the icon of the resource group
	 * 
	 * @return location of the icon of the resource group
	 */
	public String getIconLocation() {
		return this.iconLocation;
	}

	/**
	 * Set the location of the icon of the resource group
	 * 
	 * @param iconLocation
	 *            the location of the icon of the resource group
	 */
	public void setIconLocation(String iconLocation) {
		this.iconLocation = iconLocation;
	}
}
