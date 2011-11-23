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
package de.unistuttgart.ipvs.pmp.model.implementations.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class ServiceLevelImplTest extends AndroidTestCase {
    
    private static final String TEST_APP1_IDENT = "TEST_APP1";
    private static final String TEST_APP1_NAME = "TEST_APP1_NAME";
    private static final String TEST_APP1_DESCR = "TEST_APP1_DESCR";
    
    private static final String TEST_RG1_IDENT = "TEST_RG1";
    private static final String TEST_RG1_NAME = "TEST_RG1_NAME";
    private static final String TEST_RG1_DESC = "TEST_RG1_DESC";
    
    private static final String TEST_APP1_SL0_NAME = "TEST_APP1_SL0_NAME";
    private static final String TEST_APP1_SL0_DESC = "TEST_APP1_SL0_DESC";
    private static final String TEST_PL1_IDENT = "TEST_PL1";
    private static final String TEST_PL1_NAME = "TEST_PL1_NAME";
    private static final String TEST_PL1_DESC = "TEST_PL1_DESC";
    private static final String TEST_PL1_VALUE = "TEST_PL1_VALUE";
    
    IServiceLevel slevel = null;
    
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        /*
         * Filling the DB with Apps
         */
        DatabaseOpenHelper doh = DatabaseSingleton.getInstance().getDatabaseHelper();
        doh.cleanTables();
        SQLiteDatabase DB = doh.getWritableDatabase();
        
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { TEST_APP1_IDENT, TEST_APP1_NAME,
                TEST_APP1_DESCR });
        
        DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_RG1_NAME,
                TEST_RG1_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceFeature\" VALUES(?, 0, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_APP1_SL0_NAME, TEST_APP1_SL0_DESC });
        
        DB.execSQL("INSERT INTO \"PrivacySetting\" VALUES(?, ?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_PL1_IDENT,
                TEST_PL1_NAME, TEST_PL1_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_RG1_IDENT, TEST_PL1_IDENT, TEST_PL1_VALUE });
    }
    
    
    public void testGetDescription() {
        assertNull(this.slevel);
        this.slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(this.slevel);
        assertEquals(TEST_APP1_SL0_DESC, this.slevel.getDescription());
    }
    
    
    public void testGetLevel() {
        assertNull(this.slevel);
        this.slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(this.slevel);
        assertEquals(0, this.slevel.getLevel());
    }
    
    
    public void testGetName() {
        assertNull(this.slevel);
        this.slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(this.slevel);
        assertEquals(TEST_APP1_SL0_NAME, this.slevel.getName());
    }
    
    
    public void testGetPrivacyLevels() {
        assertNull(this.slevel);
        this.slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(this.slevel);
        assertEquals(1, this.slevel.getPrivacySettings().length);
        assertEquals(TEST_PL1_IDENT, this.slevel.getPrivacySettings()[0].getIdentifier());
    }
    
    
    public void testIsAvailable() {
        assertNull(this.slevel);
        this.slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(this.slevel);
        assertEquals(true, this.slevel.isAvailable());
    }
}
