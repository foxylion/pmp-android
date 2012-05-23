/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.infoapp.common;

import java.util.concurrent.Semaphore;

import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;

/**
 * Abstract class for getting the url of a resource group
 * 
 * @author Thorsten Berberich
 * 
 */
public abstract class AbstractRequestRessourceHandler extends PMPRequestResourceHandler {
    
    /**
     * Stored url
     */
    private String URL = null;
    
    /**
     * Semaphore where the caller is aquired
     */
    protected Semaphore sem;
    
    
    /**
     * Constructor
     * 
     * @param sem
     *            {@link Semaphore}
     */
    public AbstractRequestRessourceHandler(Semaphore sem) {
        this.sem = sem;
    }
    
    
    /**
     * @return the uRL
     */
    public String getURL() {
        return URL;
    }
    
    
    /**
     * @param uRL
     *            the uRL to set
     */
    public void setURL(String url) {
        URL = url;
    }
}
