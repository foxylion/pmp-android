package de.unistuttgart.ipvs.pmp.gui.activities.test;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.StartActivity;
import android.test.ActivityInstrumentationTestCase2;

/**
 * Test class: StartActivity
 * 
 * Test if localization is correct when setting language in device.
 * Test activity (not null).
 * 
 * @author Andre
 *
 */

public class StartActivityTest extends
        ActivityInstrumentationTestCase2<StartActivity> {

    StartActivity mActivity;
    String appString;
    String resString;

    public StartActivityTest() {
        super("de.unistuttgart.ipvs.pmp.gui.activities", StartActivity.class);
    }

    protected void setUp() {
        mActivity = this.getActivity();
        appString = mActivity.getString(R.string.apps);
        resString = mActivity.getString(R.string.ress);
    }

    public void testingPreconditions() {
        assertNotNull(mActivity);
    }

    public void testEnglishButtons() {
        assertEquals("Applications", appString);
        assertEquals("Resources", resString);
    }

    public void testGermanButtons() {
        assertEquals("Applikationen", appString);
        assertEquals("Ressourcen", resString);
    }

}
