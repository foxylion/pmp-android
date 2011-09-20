package de.unistuttgart.ipvs.pmp.model.implementations.test;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

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
	
	private static final String TEST_APP2_SL0_NAME = "TEST_APP2_SL0_NAME";
	private static final String TEST_APP2_SL0_DESC = "TEST_APP2_SL0_DESC";
	private static final String TEST_APP2_SL1_NAME = "TEST_APP2_SL1_NAME";
	private static final String TEST_APP2_SL1_DESC = "TEST_APP2_SL1_DESC";
	
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
	
	private IApp apps[] = null;
	private IApp app = null;
	private IResourceGroup ress[] = null;
	private IResourceGroup res = null;
	private IServiceLevel levels[] = null;
	private IServiceLevel level = null;
	private IPrivacyLevel plevels[] = null;
	private IPrivacyLevel plevel = null;
	private IPreset presets[] = null;
	private IPreset preset = null;
	
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
		
		DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);",
				new String[] { TEST_APP1_IDENT, TEST_APP1_SL0_NAME,
						TEST_APP1_SL0_DESC });
		DB.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);",
				new String[] { TEST_APP1_IDENT, TEST_APP1_SL1_NAME,
						TEST_APP1_SL1_DESC });
		
		DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG1_IDENT, TEST_PL1_IDENT, TEST_PL1_NAME,
						TEST_PL1_DESC });
		DB.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG1_IDENT, TEST_PL2_IDENT, TEST_PL2_NAME,
						TEST_PL2_DESC });
		
		DB.execSQL(
				"INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);",
				new String[] { TEST_APP1_IDENT, TEST_RG1_IDENT, TEST_PL1_IDENT,
						TEST_PL1_VALUE });
		DB.execSQL(
				"INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 0, ?, ?, ?);",
				new String[] { TEST_APP1_IDENT, TEST_RG1_IDENT, TEST_PL2_IDENT,
						TEST_PL2_VALUE });
		
		preset = ModelSingleton
				.getInstance()
				.getModel()
				.addPreset(TEST_PRESET_NAME, TEST_PRESET_DESC,
						TEST_PRESET_TYPE, TEST_PRESET_IDENT);
	}
	
	public void testGetActiveServiceLevel(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		assertEquals(0, app.getActiveServiceLevel().getLevel());
	}
	public void testGetDescription(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		assertEquals(TEST_APP1_DESCR, app.getDescription());
	}
	public void testGetIdentifier(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		assertEquals(TEST_APP1_IDENT, app.getIdentifier());
	}
	public void testGetName(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		assertEquals(TEST_APP1_NAME, app.getName());
	}
	public void testGetServiceLevels(){
		assertNull(app);
		assertNull(levels);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		levels = app.getServiceLevels();
		assertNotNull(levels);
		assertEquals(2,levels.length);
	}
	public void testGetServiceLevel(){
		assertNull(app);
		assertNull(level);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		level = app.getServiceLevel(1);
		assertNotNull(level);
		assertEquals(TEST_APP1_SL1_NAME,level.getName());
	}
	
	public void testGetAllPrivacyLevelsUsedByActiveServiceLevel(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		res = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG1_IDENT);
		plevels = app.getAllPrivacyLevelsUsedByActiveServiceLevel(res);
		assertEquals(2, plevels.length);
	}
	public void testGetAllResourceGroupsUsedByServiceLevels(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		assertNotNull(app);
		ress = app.getAllResourceGroupsUsedByServiceLevels();
		assertEquals(1, ress.length);
	}
	public void testGetAssignedPresets(){
		assertNull(app);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP1_IDENT);
		preset.addApp(app);
		assertNull(presets);
		presets = app.getAssignedPresets();
		assertEquals(1, presets.length);
		assertEquals(TEST_PRESET_IDENT, presets[0].getIdentifier());
	}
}
