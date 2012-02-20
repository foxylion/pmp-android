package de.unistuttgart.ipvs.pmp.jpmpps.io.request;


/**
 * Message should be submitted when a RGIS is requested.
 * 
 * @author Jakob Jarosch
 */
public class RequestRGIS extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private String packageName;
    
    
    /**
     * Creates a new request for the given package name.
     * 
     * @param packageName
     *            Name of the package for which the RGIS should be returned.
     */
    public RequestRGIS(String packageName) {
        this.packageName = packageName;
    }
    
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    
    
    public String getPackageName() {
        return this.packageName;
    }
}
