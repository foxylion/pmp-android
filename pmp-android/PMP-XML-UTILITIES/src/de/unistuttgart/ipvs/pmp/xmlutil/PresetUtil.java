package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.preset.PresetSet;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class PresetUtil {
	
	protected PresetUtil() {
	}
	
	public PresetSet parsePresetSetXML(InputStream xmlStream) {
		return null;
	}
	
	public InputStream compilePresetSet() {
		return null;
	}
	
	public PresetSet createBlankPresetSet() {
		return new PresetSet();
	}

}
