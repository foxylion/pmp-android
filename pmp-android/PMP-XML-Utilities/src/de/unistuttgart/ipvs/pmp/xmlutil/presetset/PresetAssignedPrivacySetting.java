package de.unistuttgart.ipvs.pmp.xmlutil.presetset;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueLocation;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetAssignedPrivacySetting extends IssueLocation implements Serializable, IPresetAssignedPrivacySetting {
    
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
     * The {@link IPresetAssignedPrivacySetting} identifier
     */
    private String psIdentifier = "";
    
    /**
     * The value
     */
    private String value = "";
    
    /**
     * The flag, if its an empty value
     */
    private boolean emptyValue = false;
    
    /**
     * List of {@link IPresetPSContext}s
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
    
    
    @Override
    public String getRgIdentifier() {
        return this.rgIdentifier;
    }
    
    
    @Override
    public void setRgIdentifier(String rgIdentifier) {
        this.rgIdentifier = rgIdentifier;
    }
    
    
    @Override
    public String getRgRevision() {
        return this.rgRevision;
    }
    
    
    @Override
    public void setRgRevision(String rgRevision) {
        this.rgRevision = rgRevision;
    }
    
    
    @Override
    public String getPsIdentifier() {
        return this.psIdentifier;
    }
    
    
    @Override
    public void setPsIdentifier(String psIdentifier) {
        this.psIdentifier = psIdentifier;
    }
    
    
    @Override
    public String getValue() {
        return this.value;
    }
    
    
    @Override
    public void setValue(String value) {
        this.value = value;
    }
    
    
    @Override
    public boolean isEmptyValue() {
        return this.emptyValue;
    }
    
    
    @Override
    public void setEmptyValue(boolean emptyValue) {
        this.emptyValue = emptyValue;
    }
    
    
    @Override
    public List<IPresetPSContext> getContexts() {
        return this.contexts;
    }
    
    
    @Override
    public void addContext(IPresetPSContext context) {
        this.contexts.add(context);
    }
    
    
    @Override
    public void removeContext(IPresetPSContext context) {
        this.contexts.remove(context);
    }
}
