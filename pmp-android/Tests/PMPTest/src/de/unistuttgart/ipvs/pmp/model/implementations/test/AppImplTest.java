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
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

public class AppImplTest extends AndroidTestCase {
    
    private static final String TEST_APP1_IDENT = "TEST_APP1";
    private static final String TEST_APP1_NAME = "TEST_APP1_NAME";
    private static final String TEST_APP1_DESCR = "TEST_APP1_DESCR";
    
    private static final String TEST_APP2_IDENT = "TEST_APP2";
    private static final String TEST_APP2_NAME = "TEST_APP2_NAME";
    private static final String TEST_APP2_DESCR = "TEST_APP2_DESCR";
    
    private static final String TEST_APP3_IDENT = "TEST_APP3";
    private static final String TEST_APP3_NAME = "TEST_APP3_NAME";
    private static final String TEST_APP3_DESCR = "TEST_APP3_DESCR";
    
    private static final String TEST_RG1_IDENT = "TEST_RG1";
    private static final String TEST_RG1_NAME = "TEST_RG1_NAME";
    private static final String TEST_RG1_DESC = "TEST_RG1_DESC";
    
    private static final String TEST_RG2_IDENT = "TEST_RG2";
    private static final String TEST_RG2_NAME = "TEST_RG2_NAME";
    private static final String TEST_RG2_DESC = "TEST_RG2_DESC";
    
    private static final String TEST_APP1_SL0_NAME = "TEST_APP1_SL0_NAME";
    private static final String TEST_APP1_SL0_DESC = "TEST_APP1_SL0_DESC";
    private static final String TEST_APP1_SL1_NAME = "TEST_APP1_SL1_NAME";
    private static final String TEST_APP1_SL1_DESC = "TEST_APP1_SL1_DESC";
    
    private static final String TEST_PL1_IDENT = "TEST_PL1";
    private static final String TEST_PL1_NAME = "TEST_PL1_NAME";
    private static final String TEST_PL1_DESC = "TEST_PL1_DESC";
    private static final String TEST_PL1_VALUE = "TEST_PL1_VALUE";
    
    private static final String TEST_PL2_IDENT = "TEST_PL2";
    private static final String TEST_PL2_NAME = "TEST_PL2_NAME";
    private static final String TEST_PL2_DESC = "TEST_PL2_DESC";
    private static final String TEST_PL2_VALUE = "TEST_PL2_VALUE";
    
    private static final String TEST_PRESET_NAME = "TEST_PRESET_NAME";
    private static final String TEST_PRESET_DESC = "TEST_PRESET_DESC";
    private static final String TEST_PRESET_IDENT = "TEST_PRESET_IDENT";
    private static final PMPComponentType TEST_PRESET_TYPE = PMPComponentType.NONE;
    
    private IApp app = null;
    private IResourceGroup ress[] = null;
    private IResourceGroup res = null;
    private IServiceLevel levels[] = null;
    private IServiceLevel level = null;
    private IPrivacyLevel plevels[] = null;
    private IPreset presets[] = null;
    private IPreset preset = null;
    
    
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
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { TEST_APP2_IDENT, TEST_APP2_NAME,
                TEST_APP2_DESCR });
        DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] { TEST_APP3_IDENT, TEST_APP3_NAME,
                TEST_APP3_DESCR });
        
        DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_RG1_NAME,
                TEST_RG1_DESC });
        DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);", new String[] { TEST_RG2_IDENT, TEST_RG2_NAME,
                TEST_RG2_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_APP1_SL0_NAME, TEST_APP1_SL0_DESC });
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_APP1_SL1_NAME, TEST_APP1_SL1_DESC });
        
        DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_PL1_IDENT,
                TEST_PL1_NAME, TEST_PL1_DESC });
        DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_PL2_IDENT,
                TEST_PL2_NAME, TEST_PL2_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_RG1_IDENT, TEST_PL1_IDENT, TEST_PL1_VALUE });
        DB.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_RG1_IDENT, TEST_PL2_IDENT, TEST_PL2_VALUE });
        
        this.preset = ModelSingleton.getInstance().getModel()
                .addPreset(TEST_PRESET_NAME, TEST_PRESET_DESC, TEST_PRESET_TYPE, TEST_PRESET_IDENT);
    }
    
    
    public void testGetActiveServiceLevel() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        assertEquals(0, this.app.getActiveServiceLevel().getLevel());
    }
    
    
    public void testGetDescription() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        assertEquals(TEST_APP1_DESCR, this.app.getDescription());
    }
    
    
    public void testGetIdentifier() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        assertEquals(TEST_APP1_IDENT, this.app.getIdentifier());
    }
    
    
    public void testGetName() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        assertEquals(TEST_APP1_NAME, this.app.getName());
    }
    
    
    public void testGetServiceLevels() {
        assertNull(this.app);
        assertNull(this.levels);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        this.levels = this.app.getServiceLevels();
        assertNotNull(this.levels);
        assertEquals(2, this.levels.length);
    }
    
    
    public void testGetServiceLevel() {
        assertNull(this.app);
        assertNull(this.level);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        this.level = this.app.getServiceLevel(1);
        assertNotNull(this.level);
        assertEquals(TEST_APP1_SL1_NAME, this.level.getName());
    }
    
    
    public void testGetAllPrivacyLevelsUsedByActiveServiceLevel() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        this.res = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT);
        this.plevels = this.app.getAllPrivacyLevelsUsedByActiveServiceLevel(this.res);
        assertEquals(2, this.plevels.length);
    }
    
    
    public void testGetAllResourceGroupsUsedByServiceLevels() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        this.ress = this.app.getAllResourceGroupsUsedByServiceLevels();
        assertEquals(1, this.ress.length);
    }
    
    
    public void testGetAssignedPresets() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        this.preset.assignApp(this.app, true);
        assertNull(this.presets);
        this.presets = this.app.getAssignedPresets();
        assertEquals(1, this.presets.length);
        assertEquals(TEST_PRESET_IDENT, this.presets[0].getIdentifier());
    }
}
