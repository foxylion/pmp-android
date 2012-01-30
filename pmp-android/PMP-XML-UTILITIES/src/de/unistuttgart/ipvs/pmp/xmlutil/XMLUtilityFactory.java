package de.unistuttgart.ipvs.pmp.xmlutil;

/**
 * This factory provides XML utilities for Apps, Resourcegroups and Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLUtilityFactory {

	/**
	 * Hide the constructor. Use static methods.
	 */
	private XMLUtilityFactory() {
	}

	/**
	 * Get an instance of the class, which provides utilities for Apps.
	 * 
	 * @return Instance of AppUtil
	 */
	public static AppUtil getAppUtilities() {
		return new AppUtil();
	}

	/**
	 * Get an instance of the class, which provides utilities for
	 * Resourcegroups.
	 * 
	 * @return Instance of RGUtil
	 */
	public static RGUtil getRGUtilities() {
		return new RGUtil();
	}

	/**
	 * Get an instance of the class, which provides utilities for Presets.
	 * 
	 * @return Instance of PresetUtil
	 */
	public static PresetUtil getPresetUtilities() {
		return new PresetUtil();
	}

}