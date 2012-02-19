package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.internal;

public class DescriptionString implements IEncapsulatedString  {
	
	private final String desc;
	
	public DescriptionString(String desc) {
		this.desc = desc;
	}
	
	public String getString() {
		return desc;
	}

}
