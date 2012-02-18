package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.util.List;

public interface IPresetAssignedPrivacySetting {
    
    /**
     * Get the resource group identifier
     * 
     * @return the rgIdentifier
     */
    public abstract String getRgIdentifier();
    
    
    /**
     * Set the resource group identifier
     * 
     * @param rgIdentifier
     *            the rgIdentifier to set
     */
    public abstract void setRgIdentifier(String rgIdentifier);
    
    
    /**
     * Get the resource group revision
     * 
     * @return the rgRevision
     */
    public abstract String getRgRevision();
    
    
    /**
     * Set the resource group revision
     * 
     * @param rgRevision
     *            the rgRevision to set
     */
    public abstract void setRgRevision(String rgRevision);
    
    
    /**
     * Get the privacy setting identifier
     * 
     * @return the psIdentifier
     */
    public abstract String getPsIdentifier();
    
    
    /**
     * Set the privacy setting identifier
     * 
     * @param psIdentifier
     *            the psIdentifier to set
     */
    public abstract void setPsIdentifier(String psIdentifier);
    
    
    /**
     * Get the value
     * 
     * @return the value
     */
    public abstract String getValue();
    
    
    /**
     * Set the value
     * 
     * @param value
     *            the value to set
     */
    public abstract void setValue(String value);
    
    
    /**
     * Get the list of contexts
     * 
     * @return the contexts
     */
    public abstract List<IPresetPSContext> getContexts();
    
    
    /**
     * Add a context
     * 
     * @param context
     *            Context to add
     */
    public abstract void addContext(IPresetPSContext context);
    
    
    /**
     * Remove a context
     * 
     * @param context
     *            Context to remove
     */
    public abstract void removeContext(IPresetPSContext context);
    
}
