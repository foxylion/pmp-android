package de.unistuttgart.ipvs.pmp.xmlutil.common.informationset;

import java.util.Locale;

public abstract class AbstractLocaleString {
	
	private Locale locale;
	private String string;
	
	public Locale getLocale() {
		return locale;
	}
	public void setLocale(Locale locale) {
		this.locale = locale;
	}
	public String getString() {
		return string;
	}
	public void setString(String string) {
		this.string = string;
	}

}