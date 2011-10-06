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

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import de.unistuttgart.ipvs.pmp.Constants;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity;
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
    
    String slName;
    
    RadioGroup rGroup;
    RadioButton button;
    
    
    public ServiceLvlActivityTest() {
        super("de.unistuttgart.ipvs.pmp", ServiceLvlActivity.class);
    }
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        this.TEST_APP_IDENT = "APP_IDENT";
        this.TEST_APP_NAME = "APP_NAME";
        this.TEST_APP_DESC = "APP_DESC";
        
        this.TEST_SERLVL_IDENT = "SERLVL_IDENT";
        this.TEST_SERLVL_NAME = "SERLVL_NAME";
        this.TEST_SERLVL_DESC = "SERLVL_DESC";
        
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        // Fill with the App
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { this.TEST_APP_IDENT, this.TEST_APP_NAME,
                this.TEST_APP_DESC });
        
        // Fill Service Lvl for App
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);", new String[] { this.TEST_APP_IDENT,
                this.TEST_SERLVL_NAME, this.TEST_SERLVL_DESC });
        
        Intent intent = new Intent();
        intent.setClassName("de.unistuttgart.ipvs.pmp", "de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity");
        intent.putExtra(Constants.INTENT_IDENTIFIER, this.TEST_APP_IDENT);
        setActivityIntent(intent);
        this.mActivity = getActivity();
        this.headerString = this.mActivity.getString(R.string.servive_level_for) + " " + this.TEST_APP_NAME;
        
        this.app = ModelSingleton.getInstance().getModel().getApp(this.TEST_APP_IDENT);
        this.levelArray = this.app.getServiceLevels();
        this.slName = this.levelArray[0].getName();
    }
    
    
    public void testingPreconditions() {
        assertNotNull(this.mActivity);
        assertNotNull(this.headerString);
    }
    
    
    public void testEnglishHeader() {
        assertEquals("Service Levels for APP_NAME", this.headerString);
    }
    
    
    //    public void testGermanHeader() {
    //        assertEquals("Service Levels f�r APP_NAME", headerString);
    //    }
    
    public void testLoadServiceLvls() {
        assertEquals(this.TEST_SERLVL_NAME, this.slName);
    }
    
}
