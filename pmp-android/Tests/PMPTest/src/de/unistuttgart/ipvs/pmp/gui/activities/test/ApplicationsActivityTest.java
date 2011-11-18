/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        // FIXME this.headerString = this.mActivity.getString(R.string.apps);
        
        this.TEST_APP1_IDENT = "TEST_APP1";
        this.TEST_APP1_NAME = "TEST APP 1";
        this.TEST_APP1_DESCR = "This is a very long test description to see if a scrollbar appears, if not this test is an absolute failure and i will kill myself today. if it does i am the happiest person alive, that ever existed in this beautiful world. i am so happy. so happy that i could jump up and down all day until my legs break into half and i am yet to be unhappy like the test failed. then again i dont care about the test, because i want to eat. i like to eat very much. in fact i like to eat so much that i am eating my own legs that were broken from the jumping up and down all day. man my legs are so delicious that i could be happy again, so happy like this stupid test is running perfectly normal. i dunno if this text is long enough but ill just keep writing till my fingers break into half to and then ill eat'em up until my stomachs full because im am very hungry. so hungry from all this stupid writing until the text is so long that this stupid scrollbar is appearing. man i am hungry!. this better be long enough...........";
        
        /*
         * Clean the DB
         */
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        // Fill with the App
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { this.TEST_APP1_IDENT, this.TEST_APP1_NAME,
                this.TEST_APP1_DESCR });
        
        this.apps = ModelSingleton.getInstance().getModel().getApps();
        this.appCount = this.apps.length;
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
        assertEquals(1, this.appCount);
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
