package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;

import de.unistuttgart.ipvs.pmp.jpmpps.io.response.PresetSetSaveResponse;

/**
 * Request for saving a presetSet on the server.
 * Server will answer with a {@link PresetSetSaveResponse}.
 * 
 * @author Jakob Jarosch
 */
public class RequestPresetSetSave extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private byte[] presetSet;
    
    
    /**
     * Creates a new {@link RequestPresetSetSave} instance.
     * 
     * @param presetSet
     *            The preset set which should be saved.
     */
    public RequestPresetSetSave(Object presetSet) {
        if (presetSet == null) {
            throw new IllegalArgumentException("presetSet should never be null.");
        }
        
        this.presetSet = toByteArray(presetSet);
    }
    
    
    public Object getPresetSet() throws ClassNotFoundException, InvalidClassException {
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
