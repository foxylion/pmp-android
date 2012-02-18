package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedPrivacySetting implements Serializable, IPresetAssignedPrivacySetting {
    
    /**
     * Serial
     */
    private static final long serialVersionUID = -408591657122277763L;
    
    /**
     * The resource group identifier
     */
    private String rgIdentifier = "";
    
    /**
     * The resource group revision
     */
    private String rgRevision = "";
    
    /**
     * The privacy setting identifier
     */
    private String psIdentifier = "";
    
    /**
     * The value
     */
    private String value = "";
    
    /**
     * List of contexts
     */
    private List<IPresetPSContext> contexts = new ArrayList<IPresetPSContext>();
    
    
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
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#getRgIdentifier()
     */
    @Override
    public String getRgIdentifier() {
        return this.rgIdentifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#setRgIdentifier(java.lang.String)
     */
    @Override
    public void setRgIdentifier(String rgIdentifier) {
        this.rgIdentifier = rgIdentifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#getRgRevision()
     */
    @Override
    public String getRgRevision() {
        return this.rgRevision;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#setRgRevision(java.lang.String)
     */
    @Override
    public void setRgRevision(String rgRevision) {
        this.rgRevision = rgRevision;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#getPsIdentifier()
     */
    @Override
    public String getPsIdentifier() {
        return this.psIdentifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#setPsIdentifier(java.lang.String)
     */
    @Override
    public void setPsIdentifier(String psIdentifier) {
        this.psIdentifier = psIdentifier;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#getValue()
     */
    @Override
    public String getValue() {
        return this.value;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#setValue(java.lang.String)
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#getContexts()
     */
    @Override
    public List<IPresetPSContext> getContexts() {
        return this.contexts;
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#addContext(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext)
     */
    @Override
    public void addContext(IPresetPSContext context) {
        this.contexts.add(context);
    }
    
    
    /* (non-Javadoc)
     * @see de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting#removeContext(de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext)
     */
    @Override
    public void removeContext(IPresetPSContext context) {
        this.contexts.remove(context);
    }
    
}
