package de.unistuttgart.ipvs.pmp.editor.model;

import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
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
     * The stored {@link AIS}
     */
    private AIS ais;

    /**
     * The stored {@link RGIS}
     */
    private RGIS rgis;

    /**
     * The stored {@link RGIS} list that was downloaded from the server
     */
    private List<RGIS> rgisList;

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
    public AIS getAis() {
	return ais;
    }

    /**
     * Sets the {@link AIS} to store
     * 
     * @param ais
     *            the ais to set
     */
    public void setAis(AIS ais) {
	this.ais = ais;
    }

    /**
     * Get the stored {@link RGIS}
     * 
     * @return the rgis
     */
    public RGIS getRgis() {
	return rgis;
    }

    /**
     * Set the {@link RGIS} to store
     * 
     * @param rgis
     *            the rgis to set
     */
    public void setRgis(RGIS rgis) {
	this.rgis = rgis;
    }

    /**
     * Gets the whole {@link RGIS} list from the server
     * 
     * @return the rgisList
     */
    public List<RGIS> getRgisList() {
	return rgisList;
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
}
