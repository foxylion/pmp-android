package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

public class NameString implements IEncapsulatedString {
	
	private final String name;
	
	public NameString(String name) {
		this.name = name;
	}
	
	public String getString() {
		return name;
	}

}
