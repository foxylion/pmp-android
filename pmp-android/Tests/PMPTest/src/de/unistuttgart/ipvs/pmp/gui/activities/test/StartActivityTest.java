package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.test.ActivityInstrumentationTestCase2;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.StartActivity;

/**
 * Test class: StartActivity
 * 
 * Test if localization is correct when setting language in device. Test activity (not null).
 * 
 * @author Andre
 * 
 */

public class StartActivityTest extends ActivityInstrumentationTestCase2<StartActivity> {
    
    StartActivity mActivity;
    String appString;
    String resString;
    
    
    public StartActivityTest() {
        super("de.unistuttgart.ipvs.pmp", StartActivity.class);
    }
    
    
    @Override
    protected void setUp() {
        this.mActivity = getActivity();
        this.appString = this.mActivity.getString(R.string.apps);
        this.resString = this.mActivity.getString(R.string.ress);
    }
    
    
    public void testingPreconditions() {
        assertNotNull(this.mActivity);
    }
    
    
    public void testEnglishButtons() {
        assertEquals("Applications", this.appString);
        assertEquals("Resources", this.resString);
    }
    
    
    public void testGermanButtons() {
        assertEquals("Applikationen", this.appString);
        assertEquals("Ressourcen", this.resString);
    }
    
}
