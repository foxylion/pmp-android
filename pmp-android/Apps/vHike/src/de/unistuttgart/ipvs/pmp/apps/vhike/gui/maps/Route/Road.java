package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route;

/**
 * 
 * Holds information about a Road
 * 
 * @author Andre Nguyen
 * 
 */
public class Road {
    
    public String mName;
    public String mDescription;
    public int mColor;
    public int mWidth;
    public double[][] mRoute = new double[][] {};
    public RPoint[] mPoints = new RPoint[] {};
}
