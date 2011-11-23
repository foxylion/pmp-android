package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractInformationSet;

public class RgInformationSet extends AbstractInformationSet implements
		Serializable {

	/**
	 * Serial
	 */
	private static final long serialVersionUID = -5127998017294446211L;

	/**
	 * This list contains all privacy settings of the rg.
	 */
	private List<PrivacySetting> privacySettings;

	/**
	 * Constructor is used to instantiate the data structures.
	 */
	protected RgInformationSet() {
		super();
		this.privacySettings = new ArrayList<PrivacySetting>();
	}

	/**
	 * Get all privacy settings of the rg
	 * 
	 * @return all privacy settings
	 */
	public List<PrivacySetting> getPrivacySettings() {
		return privacySettings;
	}

	/**
	 * Add a privacy setting to the rg
	 * 
	 * @param ps
	 *            the privacy setting
	 */
	public void addPrivacySetting(PrivacySetting ps) {
		this.privacySettings.add(ps);
	}

}
