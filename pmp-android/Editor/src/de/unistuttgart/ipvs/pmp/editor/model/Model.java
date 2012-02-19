package de.unistuttgart.ipvs.pmp.editor.model;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.RgisEditor;
import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Model class to store the {@link AIS} of the XML file that is edited and the
 * {@link RGIS} that was eventually downloaded from the JPMPPS
 * 
 * @author Thorsten Berberich
 * 
 */
public class Model {

    /**
     * Instance of this class
     */
    private static Model instance = null;

    /**
     * Indicates if the ais file has unsaved changes
     */
    private Boolean isAISDirty = false;
    
    private boolean rgisDirty = false;

    /**
     * The stored {@link AIS}
     */
    private IAIS ais;

    /**
     * The stored {@link RGIS}
     */
    private IRGIS rgis;

    /**
     * The stored {@link RGIS} list that was downloaded from the server
     */
    private List<RGIS> rgisList = null;

    private AisEditor aisEditor;
    
    private RgisEditor rgisEditor;

    /**
     * Private constructor because of singleton
     */
    private Model() {
    }

    /**
     * Methode used to get the only instance of this class
     * 
     * @return only {@link Model} instance of this class
     */
    public static Model getInstance() {
	if (instance == null) {
	    instance = new Model();
	}
	return instance;
    }

    /**
     * Gets the stored {@link AIS}
     * 
     * @return the ais
     */
    public IAIS getAis() {
	return ais;
    }

    /**
     * Sets the {@link AIS} to store
     * 
     * @param ais
     *            the ais to set
     */
    public void setAis(IAIS ais) {
	this.ais = ais;
    }

    /**
     * Get the stored {@link RGIS}
     * 
     * @return the rgis
     */
    public IRGIS getRgis() {
	return rgis;
    }

    /**
     * Set the {@link RGIS} to store
     * 
     * @param rgis
     *            the rgis to set
     */
    public void setRgis(IRGIS rgis) {
	this.rgis = rgis;
    }
    
    public void setRgisEditor(RgisEditor editor) {
    	rgisEditor = editor;
    	
    }
    public void setRgisDirty(boolean dirty) {
    	rgisDirty = dirty;
    	rgisEditor.firePropertyChangedDirty();
    }
    
    public boolean isRgisDirty() {
    	return rgisDirty;
    }

    /**
     * Gets the whole {@link RGIS} list from the server
     * 
     * @return the rgisList
     * @throws IOException
     */
    public List<RGIS> getRgisList() throws IOException {
	if (rgisList == null) {
	    updateServerList();
	}
	return rgisList;
    }

    /**
     * Updates the {@link RGIS} list from the server
     * 
     * @throws IOException
     */
    public void updateServerList() throws IOException {
	ServerProvider server = new ServerProvider();
	server.updateResourceGroupList();
	rgisList = server.getAvailableRessourceGroups();
    }

    /**
     * Checks if the {@link RGIS} list of the server is cached locally
     * 
     * @return true if available
     */
    public Boolean isRGListAvailable() {
	if (rgisList == null) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * Sets the whole {@link RGIS} list from the server
     * 
     * @param rgisList
     *            the rgisList to set
     */
    public void setRgisList(List<RGIS> rgisList) {
	this.rgisList = rgisList;
    }

    /**
     * True if it has unsaved changes, false if not
     * 
     * @return true if there are unsaved changes, false otherwise
     */
    public Boolean isAisDirty() {
	return isAISDirty;
    }

    /**
     * Sets the dirty flag of the ais file
     * 
     * @param isAISDirty
     *            false if it was just saved, true otherwise
     */
    public void setAISDirty(Boolean isAISDirty) {
	this.isAISDirty = isAISDirty;
	aisEditor.firePropertyChangedDirty();
    }

    /**
     * Instance of the {@link AisEditor} to call the
     * {@link AisEditor#firePropertyChangedDirty()}
     * 
     * @param aisEditor
     *            the aisEditor to set
     */
    public void setAisEditor(AisEditor aisEditor) {
	this.aisEditor = aisEditor;
    }
}
