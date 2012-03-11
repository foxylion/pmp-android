package de.unistuttgart.ipvs.pmp.jpmpps.io.request;

/**
 * Message should be submitted when a {@link ResourceGroupPackage} is requested.
 * 
 * @author Jakob Jarosch
 */
public class RequestResourceGroupPackage extends AbstractRequest {
    
    private static final long serialVersionUID = 1L;
    
    private String packageName;
    
    
    /**
     * Creates a new request for the given package name.
     * 
     * @param packageName
     *            Name of the package which should be returned.
     */
    public RequestResourceGroupPackage(String packageName) {
        if (packageName == null) {
            throw new IllegalArgumentException("packageName should never be null.");
        }
        
        this.packageName = packageName;
    }
    
    
    public String getPackageName() {
        return this.packageName;
    }
}
