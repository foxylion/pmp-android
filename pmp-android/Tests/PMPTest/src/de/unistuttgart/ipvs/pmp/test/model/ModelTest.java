package de.unistuttgart.ipvs.pmp.test.model;

import java.util.ArrayList;
import java.util.List;

import android.test.ApplicationTestCase;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextCondition;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextIntervalType;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextTime;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingPrivacySettingValue;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;
import de.unistuttgart.ipvs.pmp.model.element.privacysetting.IPrivacySetting;
import de.unistuttgart.ipvs.pmp.model.element.servicefeature.IServiceFeature;
import de.unistuttgart.ipvs.pmp.model.simple.SimpleModel;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils.App;
import de.unistuttgart.ipvs.pmp.test.ModelTestUtils.ResourceGroup;

public class ModelTest extends ApplicationTestCase<PMPApplication> {
    
    private static boolean isSetUpCompleted = false;
    
    
    public ModelTest() {
        super(PMPApplication.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        /* Execute setUp only once */
        if (isSetUpCompleted) {
            return;
        }
        
        ModelTestUtils.checkPreconditions();
        ModelTestUtils.checkInstalledApps(getContext());
        
        isSetUpCompleted = true;
    }
    
    
    /**
     * Installs all required Apps and checks if they have all the required service features.
     */
    public void test_01_ApplicationInstall() throws Exception {
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
    public void test_02_ResourceGroupsInstall() throws Exception {
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
    public void test_03_SF_PS_Match() throws Exception {
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
    public void test_04_ServiceFeatureActivation() throws Exception {
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
    }
    
    
    /**
     * Test if the deactivation of a Service Feature works.
     */
    public void test_05_ServiceFeatureDeactivation() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        
        SimpleModel.getInstance().setServiceFeatureActive(ModelProxy.get(), sf2, false);
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertFalse("ServiceFeature 'sf2' is not inactive as expected.", sf2.isActive());
    }
    
    
    /**
     * At this point there is the following configuration:
     * sf1 = true
     * sf2 = false
     * sf3 = false
     * sf_bad = not available
     */
    
    public void test_06_ConvertModelToExpertBackAndAgainToExpert() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf1 = app.getServiceFeature("sf1");
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        IServiceFeature sf3 = app.getServiceFeature("sf3");
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertFalse("ServiceFeature 'sf2' is not inactive as expected.", sf2.isActive());
        assertFalse("ServiceFeature 'sf3' is not inactive as expected.", sf3.isActive());
        
        IPreset preset = ModelProxy.get().addUserPreset("test", "test");
        preset.assignApp(app);
        preset.assignServiceFeature(sf3);
        
        assertTrue("ServiceFeature 'sf3' is not active as expected.", sf3.isActive());
        
        SimpleModel.getInstance().convertExpertToSimple(ModelProxy.get());
        
        assertTrue("ServiceFeature 'sf1' is not active as expected.", sf1.isActive());
        assertFalse("ServiceFeature 'sf2' is not inactive as expected.", sf2.isActive());
        assertTrue("ServiceFeature 'sf3' is not active as expected.", sf3.isActive());
        
        /*
         * Now test some things on a no longer existing preset from the expert mode.
         */
        List<IApp> apps = preset.getAssignedApps();
        assertEquals("Size of assigned IApp for the no longer existing Preset should be 0.", 0, apps.size());
        
        /* Assign privacy setting 'CanWifiState' (switches) with value 'true' to the preset. */
        preset.assignPrivacySetting(sf3.getRequiredPrivacySettings().get(0), "true");
        assertEquals("Size of the assigned Privacy Settings for a non existsing Preset should still be 0", 0, preset
                .getGrantedPrivacySettings().size());
    }
    
    
    /**
     * Create now a new Preset and check for a the changed value.
     */
    public void test_07_AddAContextToAPresetPrivacySetting() throws Exception {
        IApp app = ModelProxy.get().getApp(ModelTestUtils.App.PMP_TEST_APP_1.toString());
        IServiceFeature sf2 = app.getServiceFeature("sf2");
        IPreset preset = ModelProxy.get().addUserPreset("contextPreset", "description");
        IPrivacySetting privacySetting = ModelProxy.get()
                .getResourceGroup(ModelTestUtils.ResourceGroup.DATABASE.toString())
                .getPrivacySetting("allowedDatabases");
        
        /* Some notNull checks */
        assertNotNull("The IApp shouldn't be null", app);
        assertNotNull("The IServiceFeature shouldn't be null", sf2);
        assertNotNull("The IPreset shouldn't be null", preset);
        assertNotNull("The IPrivacySetting shouldn't be null", privacySetting);
        
        /* Try to find the TimeContext */
        List<IContext> contexts = ModelProxy.get().getContexts();
        IContext context = null;
        for (IContext c : contexts) {
            if (c.getIdentifier().equals("TimeContext")) {
                context = c;
            }
        }
        assertNotNull("The IContext (TimeContext) shouldn't be null", context);
        
        /* Create some ContextConditions */
        TimeContextTime tct_start = new TimeContextTime(0, 0, 0);
        TimeContextTime tct_end = new TimeContextTime(23, 59, 59);
        TimeContextIntervalType tcit = TimeContextIntervalType.REPEAT_DAILY;
        TimeContextCondition tcc_true = new TimeContextCondition(true, tct_start, tct_end, tcit,
                new ArrayList<Integer>());
        
        tct_start = new TimeContextTime(0, 0, 0);
        tct_end = new TimeContextTime(0, 0, 0);
        tcit = TimeContextIntervalType.REPEAT_DAILY;
        TimeContextCondition tcc_false = new TimeContextCondition(true, tct_start, tct_end, tcit,
                new ArrayList<Integer>());
        
        /* Prepare the Preset */
        preset.assignApp(app);
        
        /* first of all add all missing privacy settings for sf2, then "remove" the allowedDatabase privacySetting */
        preset.assignServiceFeature(sf2);
        preset.assignPrivacySetting(privacySetting, "testdb");
        assertFalse("The ServiceFeature 'sf2' should be inactive.", sf2.isActive());
        
        preset.assignContextAnnotation(privacySetting, context, tcc_false.toString(), "testdb;testdb2;testdb3");
        context.update(getContext());
        assertFalse("The ServiceFeature 'sf2' should be inactive.", sf2.isActive());
        
        preset.assignContextAnnotation(privacySetting, context, tcc_true.toString(), "testdb;testdb2");
        context.update(getContext());
        assertTrue("The ServiceFeature 'sf2' should be active.", sf2.isActive());
    }
}
