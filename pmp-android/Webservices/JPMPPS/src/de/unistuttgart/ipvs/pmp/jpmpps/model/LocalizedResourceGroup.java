package de.unistuttgart.ipvs.pmp.jpmpps.model;

import java.io.Serializable;


/**
 * A localized version of the {@link ResourceGroup}, saves memory and bandwidth
 * during transmission.
 * 
 * @author Jakob Jarosch
 * 
 */
public class LocalizedResourceGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a localized version of the given {@link ResourceGroup}.
	 * 
	 * @param rg
	 *            {@link ResourceGroup} which should be localized.
	 * @param locale
	 *            Locale in which the {@link ResourceGroup} should be localized.
	 */
	public LocalizedResourceGroup(ResourceGroup rg, String locale) {

	}
}
