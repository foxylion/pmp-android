package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.ApplicationsActivity;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;

public class ApplicationsActivityTest extends ActivityInstrumentationTestCase2<ApplicationsActivity> {
    
    ApplicationsActivity mActivity;
    
    String TEST_APP1_IDENT;
    String TEST_APP1_NAME;
    String TEST_APP1_DESCR;
    String headerString;
    
    IApp[] apps;
    
    int appCount;
    
    private ViewGroup vGroup;
    
    
    public ApplicationsActivityTest() {
        super("de.unistuttgart.ipvs.pmp", ApplicationsActivity.class);
    }
    
 
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.mActivity = getActivity();
        this.apps = ModelSingleton.getInstance().getModel().getApps();
        this.appCount = this.apps.length;
        this.headerString = this.mActivity.getString(R.string.apps);
        
        this.TEST_APP1_IDENT = "TEST_APP1";
        this.TEST_APP1_NAME = "TEST APP 1";
        this.TEST_APP1_DESCR = "TEST_APP1_DESCR";
        
        /*
         * Clean the DB
         */
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        // Fill with the App
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { this.TEST_APP1_IDENT, this.TEST_APP1_NAME,
                this.TEST_APP1_DESCR });
    }
    
    
    public void testingPreconditions() {
        assertNotNull(this.mActivity);
        assertNotNull(this.apps);
        assertNotNull(this.headerString);
    }
    
    
    public void testEnglishHeader() {
        assertEquals("Applications", this.headerString);
    }
    
    
    //    public void testGermanHeader() {
    //        assertEquals("Applikationen", headerString);
    //    }
    //    
    
    public void testAppCount() {
        assertEquals(1, appCount);
    }
    
    public void testLoadApps() {
        /*The way yout get the Views inside the Activity*/
        this.vGroup = (ViewGroup) this.mActivity.getWindow().getDecorView();
        assertEquals(1, this.vGroup.getChildCount());
        LinearLayout layout = (LinearLayout) this.vGroup.getChildAt(0);
        FrameLayout frameLayout = (FrameLayout) layout.getChildAt(1);
        //After the FrameLayout you can get the Views with getChildAt(index)
        //You have to cast to needed Class, else you get CastException
        
        ScrollView scroll = (ScrollView) frameLayout.getChildAt(0);
        TableLayout tLayout = (TableLayout) scroll.getChildAt(0);
        assertEquals(2, tLayout.getChildCount());// 0. Row have no Views!!!
        TableRow tRow = (TableRow) tLayout.getChildAt(1);// 0 Row have no Views!!!
        assertEquals(1, tRow.getChildCount());
        ImagedButton button = (ImagedButton) tRow.getChildAt(0);
        assertNotNull(button);
        assertEquals(this.TEST_APP1_NAME, button.getName());
    }
    
}
