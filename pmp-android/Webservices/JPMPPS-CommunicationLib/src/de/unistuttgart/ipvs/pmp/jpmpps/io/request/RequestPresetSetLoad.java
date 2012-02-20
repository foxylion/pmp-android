package de.unistuttgart.ipvs.pmp.jpmpps.io.request;


/**
 * Request for a preset set saved on the server.
 * 
 * @author Jakob Jarosch
 */
public class RequestPresetSetLoad extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private String presetSetId;
    
    
    public RequestPresetSetLoad(String presetSetId) {
        if (presetSetId == null) {
            throw new IllegalArgumentException("presetSetId should never be null.");
        }
        
        this.presetSetId = presetSetId;
    }
    
    
    public String getPresetSetId() {
        return presetSetId;
    }
}
