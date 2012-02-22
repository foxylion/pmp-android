package de.unistuttgart.ipvs.pmp.test.model;

import java.util.List;

import android.test.ApplicationTestCase;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
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
     * Test if all Service Features have their correct privacy settings.
     */
    public void testSF_PS_Match() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        IServiceFeature sf3 = app.getServiceFeature("sf3");
        IServiceFeature sf_bad = app.getServiceFeature("sf_bad");
        
        /*
         * Check all ServiceFeatures for != null.
         */
        assertNotNull("ServiceFeature 'sf1' is not found as expected.", sf1);
        assertNotNull("ServiceFeature 'sf2' is not found as expected.", sf2);
        assertNotNull("ServiceFeature 'sf3' is not found as expected.", sf3);
        assertNotNull("ServiceFeature 'sf_bad' is not found as expected.", sf_bad);
        
        /*
         * Check now the count of PrivacySettings for each service feature.
         */
        List<IPrivacySetting> sf1_ps = sf1.getRequiredPrivacySettings();
        List<MissingPrivacySettingValue> sf1_ps_missing = sf1.getMissingPrivacySettings();
        
        List<IPrivacySetting> sf2_ps = sf2.getRequiredPrivacySettings();
        List<MissingPrivacySettingValue> sf2_ps_missing = sf2.getMissingPrivacySettings();
        
        List<IPrivacySetting> sf3_ps = sf3.getRequiredPrivacySettings();
        List<MissingPrivacySettingValue> sf3_ps_missing = sf3.getMissingPrivacySettings();
        
        List<IPrivacySetting> sf_bad_ps = sf_bad.getRequiredPrivacySettings();
        List<MissingPrivacySettingValue> sf_bad_ps_missing = sf_bad.getMissingPrivacySettings();
        
        assertEquals("ServiceFeature 'sf1' should have 2 PrivacySettings.", 2, sf1_ps.size());
        assertEquals("ServiceFeature 'sf1' should have no missing PrivacySettings.", 0, sf1_ps_missing.size());
        
        assertEquals("ServiceFeature 'sf2' should have 5 PrivacySettings.", 5, sf2_ps.size());
        assertEquals("ServiceFeature 'sf2' should have no missing PrivacySettings.", 0, sf2_ps_missing.size());
        
        assertEquals("ServiceFeature 'sf3' should have 1 PrivacySettings.", 1, sf3_ps.size());
        assertEquals("ServiceFeature 'sf3' should have no missing PrivacySettings.", 0, sf3_ps_missing.size());
        
        assertEquals("ServiceFeature 'sf_bad' should have 0 PrivacySettings.", 0, sf_bad_ps.size());
        assertEquals("ServiceFeature 'sf_bad' should have 1 missing PrivacySettings.", 1, sf_bad_ps_missing.size());
    }
    
    
    /**
     * Test if the activation of a Service Feature succeeds.
     */
    public void testServiceFeatureActivation() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        IServiceFeature sf3 = app.getServiceFeature("sf3");
        IServiceFeature sf_bad = app.getServiceFeature("sf_bad");
        
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), sf2, true);
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertTrue("ServiceFeature 'sf2' is not active as expected.", sf2.isActive());
        assertFalse("ServiceFeature 'sf3' is not inactive as expected.", sf3.isActive());
        
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), sf_bad, true);
        
        assertFalse("ServiceFeature 'sf_bad' is not inactive as expected.", sf_bad.isAvailable());
        assertFalse("ServiceFeature 'sf_bad' is not inactive as expected.", sf_bad.isActive());
        assertEquals("ServiceFeature 'sf_bad' does not have 1 missing PrivacySetting.", 1, sf_bad
                .getMissingPrivacySettings().size());
    }
    
    
    /**
     * Test if the deactivation of a Service Feature works.
     */
    public void testServiceFeatureDeactivation() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), sf2, false);
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertFalse("ServiceFeature 'sf2' is not inactive as expected.", sf2.isActive());
    }
}
