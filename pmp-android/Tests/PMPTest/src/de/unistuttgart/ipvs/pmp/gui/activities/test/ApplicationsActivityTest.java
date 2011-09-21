package de.unistuttgart.ipvs.pmp.gui.activities.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import de.unistuttgart.ipvs.pmp.gui.activities.ApplicationsActivity;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;

public class ApplicationsActivityTest extends ActivityInstrumentationTestCase2<ApplicationsActivity> {
    
    private static final String TEST_APP1_IDENT = "TEST_APP1";
    private static final String TEST_APP1_NAME = "TEST_APP1_NAME";
    private static final String TEST_APP1_DESCR = "TEST_APP1_DESCR";
    
    private ApplicationsActivity activity;
    private ViewGroup vGroup;
    
    
    public ApplicationsActivityTest() {
        super("de.unistuttgart.ipvs.pmp", ApplicationsActivity.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        /*
         * Clean the DB
         */
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        // Fill with the App
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { TEST_APP1_IDENT, TEST_APP1_NAME,
                TEST_APP1_DESCR });
        
        activity = this.getActivity();
    }
    
    
    public void testLoadApps() {
        /*The way yout get the Views inside the Activity*/
        vGroup = (ViewGroup) activity.getWindow().getDecorView();
        assertEquals(1, vGroup.getChildCount());
        LinearLayout layout = (LinearLayout) vGroup.getChildAt(0);
        FrameLayout frameLayout = (FrameLayout) layout.getChildAt(1);
        //After the FrameLayout you can get the Views with getChildAt(index)
        //You have to cast to needed Class, else you get CastException
        
        ScrollView scroll = (ScrollView) frameLayout.getChildAt(0);
        TableLayout tLayout = (TableLayout) scroll.getChildAt(0);
        assertEquals(2, tLayout.getChildCount());// 0 Row have no Views!!!
        TableRow tRow = (TableRow) tLayout.getChildAt(1);// 0 Row have no Views!!!
        assertEquals(1, tRow.getChildCount());
        ImagedButton button = (ImagedButton) tRow.getChildAt(0);
        assertNotNull(button);
        assertEquals(TEST_APP1_NAME, button.getName());
    }
    
}
