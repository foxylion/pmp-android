package de.unistuttgart.ipvs.pmp.resource;

import de.unistuttgart.ipvs.pmp.resource.privacysetting.library.EnumPrivacySetting;

/**
 * What type of data the Resource Group is supposed to deliver. The definition order carries semantics for
 * {@link EnumPrivacySetting}.
 * 
 * @author Tobias Kuhn
 * 
 */
public enum RGMode {
    /**
     * Correct data
     */
    NORMAL,
    /**
     * Obviously faked data
     */
    MOCK,
    /**
     * Data that is fake but indistinguishable from "NORMAL"
     */
    CLOAK;
    
}
