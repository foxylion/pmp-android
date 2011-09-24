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
import de.unistuttgart.ipvs.pmp.gui.activities.RessourcesActivity;
import de.unistuttgart.ipvs.pmp.gui.views.ImagedButton;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

/**
 * Test ResourceActivity
 * 
 * @author Andre
 * 
 */

public class ResourcesActivityTest extends ActivityInstrumentationTestCase2<RessourcesActivity> {
    
    RessourcesActivity mActivity;
    IResourceGroup[] ress;
    int resCount;
    String headerString;
    
    String TEST_RES1_IDENT;
    String TEST_RES1_NAME;
    String TEST_RES1_DESC;
    
    ViewGroup vGroup;
    
    
    public ResourcesActivityTest() {
        super("de.unistuttgart.ipvs.pmp", RessourcesActivity.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.mActivity = getActivity();
        this.ress = ModelSingleton.getInstance().getModel().getResourceGroups();
        this.resCount = this.ress.length;
        this.headerString = this.mActivity.getString(R.string.ress);
        
        this.TEST_RES1_IDENT = "TEST_RES1";
        this.TEST_RES1_NAME = "TEST RESOURCE";
        this.TEST_RES1_DESC = "This is a very long test description to see if a scrollbar appears, if not this test is an absolute failure and i will kill myself today. if it does i am the happiest person alive, that ever existed in this beautiful world. i am so happy. so happy that i could jump up and down all day until my legs break into half and i am yet to be unhappy like the test failed. then again i dont care about the test, because i want to eat. i like to eat very much. in fact i like to eat so much that i am eating my own legs that were broken from the jumping up and down all day. man my legs are so delicious that i could be happy again, so happy like this stupid test is running perfectly normal. i dunno if this text is long enough but ill just keep writing till my fingers break into half to and then ill eat'em up until my stomachs full because im am very hungry. so hungry from all this stupid writing until the text is so long that this stupid scrollbar is appearing. man i am hungry!. this better be long enough...........";
        
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);", new String[] { this.TEST_RES1_IDENT,
                this.TEST_RES1_NAME, this.TEST_RES1_DESC });
    }
    
    
    public void testingPreconditions() {
        assertNotNull(this.mActivity);
        assertNotNull(this.ress);
        assertNotNull(this.resCount);
        assertNotNull(this.headerString);
    }
    
    
    public void testEnglishHeader() {
        assertEquals("Resources", this.headerString);
    }
    
    
    //    public void testGermanHeader() {
    //        assertEquals("Ressourcen", this.headerString);
    //    }
    
    public void testResCount() {
        assertEquals(1, this.resCount);
    }
    
    
    public void testLoadRes() {
        
        this.vGroup = (ViewGroup) this.mActivity.getWindow().getDecorView();
        assertEquals(1, this.vGroup.getChildCount());
        LinearLayout layout = (LinearLayout) this.vGroup.getChildAt(0);
        FrameLayout frameLayout = (FrameLayout) layout.getChildAt(1);
        
        ScrollView scroll = (ScrollView) frameLayout.getChildAt(0);
        TableLayout tLayout = (TableLayout) scroll.getChildAt(0);
        assertEquals(2, tLayout.getChildCount());// 0. Row have no Views!!!
        TableRow tRow = (TableRow) tLayout.getChildAt(1);// 0 Row have no Views!!!
        assertEquals(1, tRow.getChildCount());
        ImagedButton button = (ImagedButton) tRow.getChildAt(0);
        assertNotNull(button);
        assertEquals(this.TEST_RES1_NAME, button.getName());
    }
    
}
