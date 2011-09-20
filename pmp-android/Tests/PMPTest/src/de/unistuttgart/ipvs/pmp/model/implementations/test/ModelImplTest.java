package de.unistuttgart.ipvs.pmp.model.implementations.test;

import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;

public class ModelImplTest extends AndroidTestCase {
    
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
    
    private static final String TEST_PRESET1_NAME = "TEST_PRESET1_NAME";
    private static final String TEST_PRESET1_DESC = "TEST_PRESET1_DESC";
    private static final String TEST_PRESET1_IDENT = "TEST_PRESET1_IDENT";
    private static final PMPComponentType TEST_PRESET1_TYPE = PMPComponentType.NONE;
    
    private static final String TEST_PRESET2_NAME = "TEST_PRESET2_NAME";
    private static final String TEST_PRESET2_DESC = "TEST_PRESET2_DESC";
    private static final String TEST_PRESET2_IDENT = "TEST_PRESET2_IDENT";
    private static final PMPComponentType TEST_PRESET2_TYPE = PMPComponentType.NONE;
    
    private IApp apps[] = null;
    private IApp app = null;
    private IResourceGroup ress[] = null;
    private IResourceGroup res = null;
    private IPreset presets[] = null;
    private IPreset preset1 = null;
    private IPreset preset2 = null;
    
    
    
    /**
     * Set up the testing environment
     */
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
        
        this.preset1  = ModelSingleton.getInstance().getModel()
                .addPreset(TEST_PRESET1_NAME, TEST_PRESET1_DESC, TEST_PRESET1_TYPE, TEST_PRESET1_IDENT);
        this.preset2  = ModelSingleton.getInstance().getModel()
                .addPreset(TEST_PRESET2_NAME, TEST_PRESET2_DESC, TEST_PRESET2_TYPE, TEST_PRESET2_IDENT);
    }
    
    
    public void testGetApps() {
        // No Apps Loaded
        assertNull(this.apps);
        // Load apps
        this.apps = ModelSingleton.getInstance().getModel().getApps();
        // Check if its loaded right
        assertNotNull(this.apps);
        assertEquals(3, this.apps.length);
    }
    
    
    public void testGetApp() {
        assertNull(this.app);
        this.app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(this.app);
        assertEquals(TEST_APP1_IDENT, this.app.getIdentifier());
    }
    
    
    public void testGetResourceGroups() {
        assertNull(this.ress);
        this.ress = ModelSingleton.getInstance().getModel().getResourceGroups();
        assertNotNull(this.ress);
        assertEquals(2, this.ress.length);
    }
    
    
    public void testGetResourceGroup() {
        assertNull(this.res);
        this.res = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT);
        assertNotNull(this.res);
        assertEquals(TEST_RG1_IDENT, this.res.getIdentifier());
    }
    
    public void testGetPreset(){
        assertNotNull(preset1);
        assertNotNull(preset2);
        assertNull(presets);
        presets = ModelSingleton.getInstance().getModel().getPresets();
        assertNotNull(presets);
        assertEquals(2, presets.length);
        assertEquals(TEST_PRESET1_IDENT, presets[0].getIdentifier());
        assertEquals(TEST_PRESET2_IDENT, presets[1].getIdentifier());
    }
}
