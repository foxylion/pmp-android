package de.unistuttgart.ipvs.pmp.test.api.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.gui.main.ActivityMain;

public class AppTest extends ActivityInstrumentationTestCase2<ActivityMain> {
    
    public AppTest() {
        super("de.unistuttgart.ipvs.pmp", ActivityMain.class);
    }
    
    public static final Bundle serviceFeatures = new Bundle();
    public static final Bundle subFeatures = new Bundle();
    
    public static final String SF_1 = "TEST_SF_1";
    public static final String SF_2 = "TEST_SF_2";
    public static final String SF_3 = "TEST_SF_3";
    
    
    @Override
    protected void setUp() throws Exception {
        serviceFeatures.putBoolean(SF_1, true);
        serviceFeatures.putBoolean(SF_2, false);
        serviceFeatures.putBoolean(SF_3, true);
        
        subFeatures.putBoolean(SF_1, true);
        subFeatures.putBoolean(SF_2, false);
    }
    
    
    public void testServiceFeatureStorage() throws Exception {
        App test = new App() {
            
            @Override
            public void onRegistrationSuccess() {
            }
            
            
            @Override
            public void onRegistrationFailed(String message) {
            }
            
            
            @Override
            public SharedPreferences getSharedPreferences(String name, int mode) {
                return AppTest.this.getActivity().getSharedPreferences(name, mode);
            }
            
        };
        test.updateServiceFeatures(subFeatures);
        
        // check results
        assertTrue(test.isServiceFeatureEnabled(SF_1));
        assertFalse(test.isServiceFeatureEnabled(SF_2));
        assertFalse(test.isServiceFeatureEnabled(SF_3));
    }
    
}