package de.unistuttgart.ipvs.pmp.model.implementations.test;

import android.test.AndroidTestCase;

import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import android.database.sqlite.SQLiteDatabase;

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
	
	private IApp apps[] = null;
	private IApp app = null;
	private IResourceGroup ress[] = null;
	private IResourceGroup res = null;
	
	/**
	 * Set up the testing environment
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		/*
		 * Filling the DB with Apps
		 */
		DatabaseOpenHelper doh = DatabaseSingleton.getInstance()
				.getDatabaseHelper();
		doh.cleanTables();
		SQLiteDatabase DB = doh.getWritableDatabase();

		DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] {
				TEST_APP1_IDENT, TEST_APP1_NAME, TEST_APP1_DESCR });
		DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] {
				TEST_APP2_IDENT, TEST_APP2_NAME, TEST_APP2_DESCR });
		DB.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] {
				TEST_APP3_IDENT, TEST_APP3_NAME, TEST_APP3_DESCR });
		
		DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);",
				new String[] { TEST_RG1_IDENT, TEST_RG1_NAME, TEST_RG1_DESC });
		DB.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);",
				new String[] { TEST_RG2_IDENT, TEST_RG2_NAME, TEST_RG2_DESC });
	}
	
	public void testGetApps(){
		// No Apps Loaded
		assertNull(apps);
		// Load apps
		apps = ModelSingleton.getInstance().getModel().getApps();
		// Check if its loaded right
		assertNotNull(apps);
		assertEquals(3, apps.length);
	}
	
	public void testGetApp(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		assertEquals(TEST_APP1_IDENT, app.getIdentifier());
	}
	
	public void testGetResourceGroups(){
		assertNull(ress);
		ress = ModelSingleton.getInstance().getModel().getResourceGroups();
		assertNotNull(ress);
		assertEquals(2, ress.length);
	}
	public void testGetResourceGroup(){
		assertNull(res);
		res = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT);
		assertNotNull(res);
		assertEquals(TEST_RG1_IDENT, res.getIdentifier());
	}
}
