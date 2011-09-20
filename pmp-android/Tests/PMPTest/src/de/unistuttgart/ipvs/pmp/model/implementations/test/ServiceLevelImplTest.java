package de.unistuttgart.ipvs.pmp.model.implementations.test;

import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class ServiceLevelImplTest extends AndroidTestCase {
    
    private static final String TEST_APP1_IDENT = "TEST_APP1";
    private static final String TEST_APP1_NAME = "TEST_APP1_NAME";
    private static final String TEST_APP1_DESCR = "TEST_APP1_DESCR";
    
    private static final String TEST_RG1_IDENT = "TEST_RG1";
    private static final String TEST_RG1_NAME = "TEST_RG1_NAME";
    private static final String TEST_RG1_DESC = "TEST_RG1_DESC";
    
    private static final String TEST_APP1_SL0_NAME = "TEST_APP1_SL0_NAME";
    private static final String TEST_APP1_SL0_DESC = "TEST_APP1_SL0_DESC";
    private static final String TEST_APP1_SL1_NAME = "TEST_APP1_SL1_NAME";
    private static final String TEST_APP1_SL1_DESC = "TEST_APP1_SL1_DESC";
    
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
        
        DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_APP1_SL0_NAME, TEST_APP1_SL0_DESC });
        
        DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);", new String[] { TEST_RG1_IDENT, TEST_PL1_IDENT,
                TEST_PL1_NAME, TEST_PL1_DESC });
        
        DB.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);", new String[] { TEST_APP1_IDENT,
                TEST_RG1_IDENT, TEST_PL1_IDENT, TEST_PL1_VALUE });
    }
    
    
    public void testGetDescription() {
        assertNull(slevel);
        slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(slevel);
        assertEquals(TEST_APP1_SL0_DESC, slevel.getDescription());
    }
    
    
    public void testGetLevel() {
        assertNull(slevel);
        slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(slevel);
        assertEquals(0, slevel.getLevel());
    }
    
    
    public void testGetName() {
        assertNull(slevel);
        slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(slevel);
        assertEquals(TEST_APP1_SL0_NAME, slevel.getName());
    }
    
    
    public void testGetPrivacyLevels() {
        assertNull(slevel);
        slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(slevel);
        assertEquals(1, slevel.getPrivacyLevels().length);
        assertEquals(TEST_PL1_IDENT, slevel.getPrivacyLevels()[0].getIdentifier());
    }
    
    
    public void testIsAvailable() {
        assertNull(slevel);
        slevel = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT).getServiceLevel(0);
        assertNotNull(slevel);
        assertEquals(true, slevel.isAvailable());
    }
}
