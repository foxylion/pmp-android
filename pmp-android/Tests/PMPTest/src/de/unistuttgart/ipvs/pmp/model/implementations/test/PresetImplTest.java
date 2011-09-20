package de.unistuttgart.ipvs.pmp.model.implementations.test;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPreset;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

public class PresetImplTest extends AndroidTestCase {

	private static final String TEST_PRESET_NAME = "TEST_PRESET_NAME";
	private static final String TEST_PRESET_DESC = "TEST_PRESET_DESC";
	private static final String TEST_PRESET_IDENT = "TEST_PRESET_IDENT";
	private static final PMPComponentType TEST_PRESET_TYPE = PMPComponentType.NONE;

	private static final String TEST_APP_IDENT = "TEST_APP";
	private static final String TEST_APP_NAME = "TEST_APP_NAME";
	private static final String TEST_APP_DESC = "TEST_APP_DESC";
	
	private static final String TEST_APP_SL0_NAME = "TEST_APP_SL0_NAME";
	private static final String TEST_APP_SL0_DESC = "TEST_APP_SL0_DESC";	
	private static final String TEST_APP_SL1_NAME = "TEST_APP_SL1_NAME";
	private static final String TEST_APP_SL1_DESC = "TEST_APP_SL1_DESC";

	private static final String TEST_APP2_IDENT = "TEST_PPA";
	private static final String TEST_APP2_NAME = "TEST_PPA_NAME";
	private static final String TEST_APP2_DESC = "TEST_PPA_DESC";
	
	private static final String TEST_APP2_SL0_NAME = "TEST_PPA_SL0_NAME";
	private static final String TEST_APP2_SL0_DESC = "TEST_PPA_SL0_DESC";	
	private static final String TEST_APP2_SL1_NAME = "TEST_PPA_SL1_NAME";
	private static final String TEST_APP2_SL1_DESC = "TEST_PPA_SL1_DESC";

	private static final String TEST_RG_IDENT = "TEST_RG";
	private static final String TEST_RG_NAME = "TEST_RG_NAME";
	private static final String TEST_RG_DESC = "TEST_RG_DESC";

	private static final String TEST_PL_IDENT = "TEST_PL";
	private static final String TEST_PL_NAME = "TEST_PL_NAME";
	private static final String TEST_PL_DESC = "TEST_PL_DESC";
	private static final String TEST_PL_VALUE = "TEST_PL_VALUE";

	private static final String TEST_PL2_IDENT = "TEST_LP";
	private static final String TEST_PL2_NAME = "TEST_LP_NAME";
	private static final String TEST_PL2_DESC = "TEST_LP_DESC";
	private static final String TEST_PL2_VALUE = "TEST_LP_VALUE";
	
	private IPreset preset;
	private IApp app, app2;
	private IPrivacyLevel pl, pl2;

	protected void setUp() throws Exception {
		super.setUp();
		/*
		 * some serious magic about to happen here
		 * happy understanding!
		 */
		DatabaseOpenHelper doh = DatabaseSingleton.getInstance()
				.getDatabaseHelper();
		doh.cleanTables();
		SQLiteDatabase sqld = doh.getWritableDatabase();

		// apps
		sqld.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] {
				TEST_APP_IDENT, TEST_APP_NAME, TEST_APP_DESC });
		sqld.execSQL("INSERT INTO \"App\" VALUES(?, ?, ?, 0);", new String[] {
				TEST_APP2_IDENT, TEST_APP2_NAME, TEST_APP2_DESC });

		// rg
		sqld.execSQL("INSERT INTO \"ResourceGroup\" VALUES(?, ?, ?);",
				new String[] { TEST_RG_IDENT, TEST_RG_NAME, TEST_RG_DESC });

		// pl
		sqld.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG_IDENT, TEST_PL_IDENT, TEST_PL_NAME,
						TEST_PL_DESC });
		sqld.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG_IDENT, TEST_PL2_IDENT, TEST_PL2_NAME,
						TEST_PL2_DESC });
		
		// sl
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_APP_SL0_NAME, TEST_APP_SL0_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_APP_SL1_NAME, TEST_APP_SL1_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_APP2_SL0_NAME, TEST_APP2_SL0_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_APP2_SL1_NAME, TEST_APP2_SL1_DESC });
		
		// sl_pl
		sqld.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 1, ?, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_RG_IDENT, TEST_PL2_IDENT, TEST_PL2_VALUE });
		sqld.execSQL("INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 1, ?, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_RG_IDENT, TEST_PL_IDENT, TEST_PL_VALUE });

		preset = ModelSingleton.getInstance().getModel()
				.addPreset(TEST_PRESET_NAME, TEST_PRESET_DESC, TEST_PRESET_TYPE,
						TEST_PRESET_IDENT);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP_IDENT);
		app2 = ModelSingleton.getInstance().getModel().getApp(TEST_APP2_IDENT);
		pl = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG_IDENT).getPrivacyLevel(TEST_PL_IDENT);
		pl2 = ModelSingleton.getInstance().getModel().getResourceGroup(TEST_RG_IDENT).getPrivacyLevel(TEST_PL2_IDENT);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPresetImpl() {
		assertEquals(preset.getName(), TEST_PRESET_NAME);
		assertEquals(preset.getDescription(), TEST_PRESET_DESC);
		assertEquals(preset.getType(), TEST_PRESET_TYPE);
		assertEquals(preset.getIdentifier(), TEST_PRESET_IDENT);
	}

	public void testAddApps() {		
		preset.addApp(app, true);
		
		assertTrue(preset.isAppAssigned(app));
		assertFalse(preset.isAppAssigned(app2));

		assertEquals(preset.getAssignedApps().length, 1);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(ia.equals(app));
		}

		preset.addApp(app2, true);
		assertTrue(preset.isAppAssigned(app));
		assertTrue(preset.isAppAssigned(app2));

		assertEquals(preset.getAssignedApps().length, 2);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(ia.equals(app) || ia.equals(app2));
		}
	}

	public void testRemoveApps() {		
		preset.addApp(app, true);
		preset.addApp(app2, true);

		preset.removeApp(app, true);
		assertFalse(preset.isAppAssigned(app));
		assertTrue(preset.isAppAssigned(app2));

		assertEquals(preset.getAssignedApps().length, 1);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(ia.equals(app2));
		}

		preset.removeApp(app2, true);
		assertFalse(preset.isAppAssigned(app));
		assertFalse(preset.isAppAssigned(app2));

		assertEquals(preset.getAssignedApps().length, 0);
	}

	public void testAddPrivacyLevel() {
		preset.setPrivacyLevel(pl, true);
		assertEquals(preset.getUsedPrivacyLevels().length, 1);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pl));
		}

		preset.setPrivacyLevel(pl2, true);
		assertEquals(preset.getUsedPrivacyLevels().length, 2);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pl) || ipl.equals(pl2));
		}
	}

	public void testRemovePrivacyLevel() {
		preset.setPrivacyLevel(pl, true);
		preset.setPrivacyLevel(pl2, true);

		preset.removePrivacyLevel(pl, true);
		assertEquals(preset.getUsedPrivacyLevels().length, 1);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pl2));
		}

		preset.removePrivacyLevel(pl2, true);
		assertEquals(preset.getUsedPrivacyLevels().length, 0);
	}

}
