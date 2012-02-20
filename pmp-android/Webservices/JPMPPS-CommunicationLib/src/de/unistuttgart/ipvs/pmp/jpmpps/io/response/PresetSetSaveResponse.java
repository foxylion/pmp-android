package de.unistuttgart.ipvs.pmp.jpmpps.io.response;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestPresetSetSave;

/**
 * A answer for a {@link RequestPresetSetSave}.
 * 
 * @author Jakob Jarosch
 */
public class PresetSetSaveResponse extends AbstractResponse {
    
    private static final long serialVersionUID = 1L;
    
    private boolean success;
    private String messageOrId;
    
    
    /**
     * Creates a new instance of the {@link PresetSetSaveResponse}.
     * 
     * @param success
     *            Whether the save request succeeded or not.
     * @param messageOrId
     *            The message on failure and the id on success.
     */
    public PresetSetSaveResponse(boolean success, String messageOrId) {
        this.success = success;
        this.messageOrId = messageOrId;
    }
    
    
    /**
     * @return Returns the success of the save request.
     */
    public boolean isSuccess() {
        return success;
    }
    
    
    /**
     * @return Returns the failure message when the save request failed.
     */
    public String getMessage() {
        if (success) {
            return null;
        }
        
        return messageOrId;
    }
    
    
    /**
     * @return Returns the id of the saved preset set when saving succeeded.
     */
    public String getId() {
        if (!success) {
            return null;
        }
        
        return messageOrId;
    }
}
