package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import java.io.IOException;
import java.io.ObjectInputStream;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetLoad;

/**
 * Response for an {@link RequestPresetSetLoad} request.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetLoadResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 1L;
    
    private byte[] presetSet;
    
    
    /**
     * Creates a new {@link PresetSetLoadResponse}.
     * 
     * @param presetSet
     *            PresetSet which should be returned.
     */
    public PresetSetLoadResponse(Object presetSet) {
        if (presetSet == null) {
            this.presetSet = null;
        }
        
        this.presetSet = toByteArray(presetSet);
    }
    
    
    /**
     * @return Returns the preset set or null if no preset set with the id was found.
     * @throws ClassNotFoundException
     *             Is thrown when the PMP-XML-UTILITIES are missing, or in an improper version.
     */
    public Object getPresetSet() throws ClassNotFoundException {
        if (this.presetSet == null) {
            return null;
        }
        
        try {
            return new ObjectInputStream(fromByteArray(this.presetSet)).readObject();
        } catch (IOException e) {
            System.err.println("Failed to deserialize the preset set.");
            e.printStackTrace();
            
            return null;
        }
    }
    
}
