package de.unistuttgart.ipvs.pmp.test.model;

import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.gui.main.ActivityMain;
import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class ModelTest extends ActivityInstrumentationTestCase2<ActivityMain> {
    
    public ModelTest() {
        super("de.unistuttgart.ipvs.pmp", ActivityMain.class);
    }
    
    /*
     * list of app/rg package constants
     */
    private static final String SIMPLE_APP = "de.unistuttgart.ipvs.pmp.apps.simpleapp";
    private static final String CALENDAR_APP = "de.unistuttgart.ipvs.pmp.apps.calendarapp";
    
    private static final String SWITCHES_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.switches";
    
    /**
     * list of apps to be installed (on the device, not in the model!)
     */
    private static final String[] INSTALLED_APPS = new String[] { SIMPLE_APP, CALENDAR_APP };
    private static boolean installChecked = false;
    
    
    @Override
    protected void setUp() throws Exception {
        try {
            if (!installChecked) {
                // check for the appropriate device setup
                for (String iapp : INSTALLED_APPS) {
                    getActivity().getPackageManager().getPackageInfo(iapp, PackageManager.GET_ACTIVITIES);
                }
                // only once though
                installChecked = true;
            }
        } catch (Exception e) {
            Log.e("PMP-Test", "Model test cannot be performed: Exception during setup check. See Java file.", e);
        }
        
        try {
            // clean possible remainders from previous test
            for (IApp app : Model.getInstance().getApps()) {
                Model.getInstance().unregisterApp(app.getIdentifier());
            }
            
            for (IResourceGroup rg : Model.getInstance().getResourceGroups()) {
                assertTrue(Model.getInstance().uninstallResourceGroup(rg.getIdentifier()));
            }
            
            for (IPreset preset : Model.getInstance().getPresets()) {
                assertTrue(Model.getInstance().removePreset(preset.getCreator(), preset.getLocalIdentifier()));
            }
            
        } catch (Exception e) {
            Log.e("PMP-Test", "Model test cannot be performed: Exception during cleaning model. See Java file.", e);
        }
    }
    
    
    public void testInstallApp() throws Exception {
        Model.getInstance().registerApp(SIMPLE_APP);
        Thread.sleep(1000L);
        assertEquals(1, Model.getInstance().getApps().size());
        assertNotNull(Model.getInstance().getApp(SIMPLE_APP));
    }
    
    
    public void testInstallRG() throws Exception {
        assertTrue(Model.getInstance().installResourceGroup(SWITCHES_RG, false));
        assertEquals(1, Model.getInstance().getResourceGroups().size());
        assertNotNull(Model.getInstance().getResourceGroup(SWITCHES_RG));
    }
    
}
