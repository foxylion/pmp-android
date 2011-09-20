package de.unistuttgart.ipvs.pmp.model.implementations.test;

import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class PrivacyLevelImplTest extends AndroidTestCase {
    
    private static final String TEST_APP1_IDENT = "TEST_APP1";
    private static final String TEST_APP1_NAME = "TEST_APP1_NAME";
    private static final String TEST_APP1_DESCR = "TEST_APP1_DESCR";
    
    private static final String TEST_APP1_SL0_NAME = "TEST_APP1_SL0_NAME";
    private static final String TEST_APP1_SL0_DESC = "TEST_APP1_SL0_DESC";
    private static final String TEST_APP1_SL1_NAME = "TEST_APP1_SL1_NAME";
    private static final String TEST_APP1_SL1_DESC = "TEST_APP1_SL1_DESC";
    
    private static final String TEST_RG1_IDENT = "TEST_RG1";
    private static final String TEST_RG1_NAME = "TEST_RG1_NAME";
    private static final String TEST_RG1_DESC = "TEST_RG1_DESC";
    
    private static final String TEST_PL1_IDENT = "TEST_PL1";
    private static final String TEST_PL1_NAME = "TEST_PL1_NAME";
    private static final String TEST_PL1_DESC = "TEST_PL1_DESC";
    private static final String TEST_PL1_VALUE = "TEST_PL1_VALUE";
    
    private IPrivacyLevel plevels[] = null;
    private IPrivacyLevel plevel = null;
    private IResourceGroup res = null;
    private IApp app = null;
    
    
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
        
        DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_PL1_IDENT,
                TEST_PL1_NAME, TEST_PL1_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_APP1_SL0_NAME, TEST_APP1_SL0_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_RG1_IDENT, TEST_PL1_IDENT, TEST_PL1_VALUE });
    }
    
    
    public void testGetDescription() {
        assertNull(plevel);
        plevel = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT)
                .getPrivacyLevel(TEST_PL1_IDENT);
        assertNotNull(plevel);
        assertEquals(TEST_PL1_DESC, plevel.getDescription());
    }
    
    
    public void testGetIdentifier() {
        assertNull(plevel);
        plevel = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT)
                .getPrivacyLevel(TEST_PL1_IDENT);
        assertNotNull(plevel);
        assertEquals(TEST_PL1_IDENT, plevel.getIdentifier());
    }
    
    
    public void testGetName() {
        assertNull(plevel);
        plevel = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT)
                .getPrivacyLevel(TEST_PL1_IDENT);
        assertNotNull(plevel);
        assertEquals(TEST_PL1_NAME, plevel.getName());
    }
    
    
    public void testGetRessourceGroup() {
        assertNull(plevel);
        plevel = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT)
                .getPrivacyLevel(TEST_PL1_IDENT);
        assertNotNull(plevel);
        
        assertNull(res);
        res = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT);
        assertNotNull(res);
        assertEquals(res.getIdentifier(), plevel.getResourceGroup().getIdentifier());
    }

    
    public void testGetValue() {
        assertNull(plevel);
        plevel = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT)
                .getPrivacyLevel(TEST_PL1_IDENT);
        assertNotNull(plevel);
        assertNull(app);
        app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
        assertNotNull(app);
        String value = app.getServiceLevel(0).getPrivacyLevels()[0].getValue();
        
        assertEquals(TEST_PL1_VALUE, value);
    }
}
