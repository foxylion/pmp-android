package de.unistuttgart.ipvs.pmp.test.model;

import android.test.ApplicationTestCase;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils.App;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils.ResourceGroup;

public class ModelAppTest extends ApplicationTestCase<PMPApplication> {
    
    private static boolean isSetIpCompleted = false;
    
    
    public ModelAppTest() {
        super(PMPApplication.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        /* Execute setUp only once */
        if (isSetIpCompleted) {
            return;
        }
        
        ModelTestUtils.checkPreconditions();
        ModelTestUtils.checkInstalledApps(getContext());
        
        isSetIpCompleted = true;
    }
    
    
    /**
     * Installs all required Apps and checks if they have all the required service features.
     */
    public void testApplicationInstall() throws Exception {
        int appCount = 0;
        
        for (App app : ModelTestUtils.App.values()) {
            ModelProxy.get().registerApp(app.toString());
            
            // Wait until registration should be succeeded.
            Thread.sleep(1000L);
            
            appCount++;
            
            // Load IApp from model.
            IApp modelApp = ModelProxy.get().getApp(app.toString());
            
            assertEquals("Count of installed apps is not '" + appCount + "'.", appCount, ModelProxy.get().getApps()
                    .size());
            assertNotNull("IApp should should not be null.", modelApp);
            assertEquals("Count of service features is not '" + app.getServiceFeaturesCount() + "'.",
                    app.getServiceFeaturesCount(), modelApp.getServiceFeatures().size());
            assertEquals("Count of active service features should be '0'", 0, modelApp.getActiveServiceFeatures()
                    .size());
            
            for (IServiceFeature serviceFeature : modelApp.getServiceFeatures()) {
                assertFalse("Service Feature should not be available.", serviceFeature.isAvailable());
                assertFalse("Service Feature should not be active.", serviceFeature.isActive());
            }
        }
    }
    
    
    /**
     * Test the installation of all required ResourceGroups.
     */
    public void testResourceGroupsInstall() throws Exception {
        int rgCount = 0;
        
        for (ResourceGroup rg : ModelTestUtils.ResourceGroup.values()) {
            ModelProxy.get().installResourceGroup(rg.toString(), false);
            rgCount++;
            assertEquals("Count of installed resource groups is not '" + rgCount + "'.", rgCount, ModelProxy.get()
                    .getResourceGroups().size());
        }
    }
    
    
    /**
     * Test if the activation of a Service Feature succeeds.
     */
    public void testServiceFeatureActivation() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        IServiceFeature sf3 = app.getServiceFeature("sf3");
        
        assertNotNull("ServiceFeature 'sf1' is not found as expected.", sf1);
        assertNotNull("ServiceFeature 'sf2' is not found as expected.", sf2);
        assertNotNull("ServiceFeature 'sf3' is not found as expected.", sf3);
        
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), sf2, true);
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertTrue("ServiceFeature 'sf2' is not active as expected.", sf2.isActive());
        assertFalse("ServiceFeature 'sf3' is not inactive as expected.", sf3.isActive());
    }
}
