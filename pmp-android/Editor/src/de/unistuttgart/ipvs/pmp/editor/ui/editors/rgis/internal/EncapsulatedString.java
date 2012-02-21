package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

public abstract class EncapsulatedString {
	
	private final RGISPrivacySetting ps;
	private final String value;
	
	public EncapsulatedString(String value, RGISPrivacySetting ps) {
		this.value = value;
		this.ps = ps;
	}
	
	public String getString() {
		return value;
	}
	
	public RGISPrivacySetting getPrivacySetting() {
		return ps;
	}
	
}
