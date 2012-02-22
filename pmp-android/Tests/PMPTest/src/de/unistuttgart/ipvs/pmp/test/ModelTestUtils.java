package de.unistuttgart.ipvs.pmp.test;

import junit.framework.Assert;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;

/**
 * {@link ModelTestUtils} provides some methods for general checks.
 * 
 * @author Jakob Jarosch
 */
public class ModelTestUtils {
    
    /**
     * All in the test used apps.
     * 
     * @author Jakob Jarosch
     */
    public enum App {
        PMP_TEST_APP_1("de.unistuttgart.ipvs.pmp.test.testapp1", 3);
        
        private String packageName;
        private int serviceFeaturesCount;
        
        
        App(String packageName, int serviceFeaturesCount) {
            this.packageName = packageName;
            this.serviceFeaturesCount = serviceFeaturesCount;
        }
        
        
        @Override
        public String toString() {
            return this.packageName;
        }
        
        
        public int getServiceFeaturesCount() {
            return serviceFeaturesCount;
        }
    }
    
    /**
     * All in the test used resource groups.
     * 
     * @author Jakob Jarosch
     */
    public enum ResourceGroup {
        DATABASE("de.unistuttgart.ipvs.pmp.resourcegroups.database"),
        LOCATION("de.unistuttgart.ipvs.pmp.resourcegroups.location"),
        SWITCHES("de.unistuttgart.ipvs.pmp.resourcegroups.switches");
        
        private String packageName;
        
        
        ResourceGroup(String packageName) {
            this.packageName = packageName;
        }
        
        
        @Override
        public String toString() {
            return this.packageName;
        }
    }
    
    
    /**
     * Check for a clean PMP installation.
     */
    public static void checkPreconditions() {
        /*
         * Check if apps, resource groups and presets are empty.
         */
        int appCount = ModelProxy.get().getApps().size();
        int rgCount = ModelProxy.get().getResourceGroups().size();
        int presetCount = ModelProxy.get().getPresets().size();
        
        Assert.assertEquals("There is an app installed, none should be installed.", 0, appCount);
        Assert.assertEquals("There is an resource group installed, none should be installed.", 0, rgCount);
        Assert.assertEquals("There is an preset in the model, none should be in.", 0, presetCount);
        
        /*
         * Check for an active simple model.
         */
        Assert.assertTrue("SimpleModel is not active.", SimpleModel.getInstance().isSimpleMode(ModelProxy.get()));
    }
    
    
    public static void checkInstalledApps(Context context) {
        for (App app : App.values()) {
            try {
                context.getPackageManager().getApplicationInfo(app.toString(), PackageManager.GET_ACTIVITIES);
            } catch (NameNotFoundException e) {
                Assert.fail("App '" + app.toString() + "' was not found on the device.");
            }
        }
    }
}
