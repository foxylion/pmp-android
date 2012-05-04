/**
 * 
 */
package de.unistuttgart.ipvs.pmp.apps.vhike;

/**
 * @author Dang Huynh
 * 
 */
public class Application extends android.app.Application {
    
    private static android.app.Application instance;
    
    
    public static android.app.Application getApp() {
        return instance;
    }
    
    
    public Application() {
        instance = this;
    }
    
}
