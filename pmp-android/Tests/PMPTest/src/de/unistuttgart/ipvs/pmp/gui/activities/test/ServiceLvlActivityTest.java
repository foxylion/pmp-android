package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;

import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLvlActivityTest extends ActivityInstrumentationTestCase2<ServiceLvlActivity> {
    
    ServiceLvlActivity mActivity;
    IApp app;
    IServiceLevel levelArray[];
    String headerString;
    
    String TEST_APP_IDENT = "TEST_APP_IDENT";
    String TEST_APP_NAME;
    String TEST_APP_DESC;
    
    String TEST_SERLVL_IDENT;
    String TEST_SERLVL_NAME;
    String TEST_SERLVL_DESC;
    
    RadioGroup rGroup;
    
    
    public ServiceLvlActivityTest() {
        super("de.unistuttgart.ipvs.pmp", ServiceLvlActivity.class);
    }
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        TEST_APP_IDENT = "APP_IDENT";
        TEST_APP_NAME = "APP_NAME";
        TEST_APP_DESC = "APP_DESC";
        
        TEST_SERLVL_IDENT = "SERLVL_IDENT";
        TEST_SERLVL_NAME = "SERLVL_NAME";
        TEST_SERLVL_DESC = "SERLVL_DESC";
        
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        // Fill with the App
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { this.TEST_APP_IDENT, this.TEST_APP_NAME,
                this.TEST_APP_DESC });
        
        // Fill Service Lvl for App
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);", new String[] { this.TEST_SERLVL_IDENT,
                this.TEST_SERLVL_NAME, this.TEST_SERLVL_DESC });
        
        Intent intent = new Intent();
        intent.setClassName("de.unistuttgart.ipvs.pmp", "de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity");
        intent.putExtra(Constants.INTENT_IDENTIFIER, TEST_APP_IDENT);
        setActivityIntent(intent);
        mActivity = getActivity();
        headerString = mActivity.getString(R.string.servive_level_for) + " " + TEST_APP_NAME;
        
    }
    
    
    public void testingPreconditions() {
        assertNotNull(mActivity);
        assertNotNull(headerString);
    }
    
    
    public void testEnglishHeader() {
        assertEquals("Service Levels for APP_NAME", headerString);
    }
    
    
    //    public void testGermanHeader() {
    //        assertEquals("Service Levels f�r APP_NAME", headerString);
    //    }
    
    public void testLoadServiceLvls() {
        
        app = ModelSingleton.getInstance().getModel().getApp(TEST_APP_IDENT);
        levelArray = app.getServiceLevels();
        rGroup = new RadioGroup(mActivity);
        RadioButton button = new RadioButton(mActivity);
        button.setText(levelArray[0].getName());
        //        assertNotNull(button);
        //        assertEquals(this.TEST_SERLVL_NAME, button.getText());
    }
    
}
