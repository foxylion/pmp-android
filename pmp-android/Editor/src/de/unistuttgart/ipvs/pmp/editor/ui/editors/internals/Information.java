package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

public class Information {
	
	private String locale;
	private String name;
	private String description;
	
	public Information(String locale, String name, String description) {
		this.locale = locale;
		this.name = name;
		this.description = description;
	}
	public String getLocale() {
		return locale;
	}
	public void setLocale(String locale) {
		this.locale = locale;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String toString() {
		return locale + ", " + name + ", " + description;
	}
	

}
