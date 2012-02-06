package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedPrivacySetting implements Serializable {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -408591657122277763L;
    
    /**
     * The resource group identifier
     */
    private String rgIdentifier;
    
    /**
     * The resource group revision
     */
    private String rgRevision;
    
    /**
     * The privacy setting identifier
     */
    private String psIdentifier;
    
    /**
     * The value
     */
    private String value;
    
    /**
     * List of contexts
     */
    private List<PresetPSContext> contexts = new ArrayList<PresetPSContext>();
    
    
    /**
     * Constructor to set the attributes
     * 
     * @param rgIdentifier
     *            Identifier of the resource group
     * @param rgRevision
     *            Revision of the resource group
     * @param psIdentifier
     *            Identifier of the privacy setting
     * @param value
     *            value of the privacy setting
     */
    public PresetAssignedPrivacySetting(String rgIdentifier, String rgRevision, String psIdentifier, String value) {
        this.rgIdentifier = rgIdentifier;
        this.rgRevision = rgRevision;
        this.psIdentifier = psIdentifier;
        this.value = value;
    }
    
    
    /**
     * Get the resource group identifier
     * 
     * @return the rgIdentifier
     */
    public String getRgIdentifier() {
        return rgIdentifier;
    }
    
    
    /**
     * Set the resource group identifier
     * 
     * @param rgIdentifier
     *            the rgIdentifier to set
     */
    public void setRgIdentifier(String rgIdentifier) {
        this.rgIdentifier = rgIdentifier;
    }
    
    
    /**
     * Get the resource group revision
     * 
     * @return the rgRevision
     */
    public String getRgRevision() {
        return rgRevision;
    }
    
    
    /**
     * Set the resource group revision
     * 
     * @param rgRevision
     *            the rgRevision to set
     */
    public void setRgRevision(String rgRevision) {
        this.rgRevision = rgRevision;
    }
    
    
    /**
     * Get the privacy setting identifier
     * 
     * @return the psIdentifier
     */
    public String getPsIdentifier() {
        return psIdentifier;
    }
    
    
    /**
     * Set the privacy setting identifier
     * 
     * @param psIdentifier
     *            the psIdentifier to set
     */
    public void setPsIdentifier(String psIdentifier) {
        this.psIdentifier = psIdentifier;
    }
    
    
    /**
     * Get the value
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }
    
    
    /**
     * Set the value
     * 
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /**
     * Get the list of contexts
     * 
     * @return the contexts
     */
    public List<PresetPSContext> getContexts() {
        return contexts;
    }
    
    
    /**
     * Add a context
     * 
     * @param context
     *            Context to add
     */
    public void addContext(PresetPSContext context) {
        contexts.add(context);
    }
    
    
    /**
     * Remove a context
     * 
     * @param context
     *            Context to remove
     */
    public void removeContext(PresetPSContext context) {
        contexts.remove(context);
    }
    
}
