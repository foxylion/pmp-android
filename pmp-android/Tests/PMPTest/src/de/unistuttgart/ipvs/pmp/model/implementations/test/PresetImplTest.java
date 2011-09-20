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
	private IPrivacyLevel plValue, plValue2;

	protected void setUp() throws Exception {
		super.setUp();
		/*
		 * some serious magic about to happen here, happy understanding!
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

		// plValue
		sqld.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG_IDENT, TEST_PL_IDENT, TEST_PL_NAME,
						TEST_PL_DESC });
		sqld.execSQL("INSERT INTO \"PrivacyLevel\" VALUES(?, ?, ?, ?);",
				new String[] { TEST_RG_IDENT, TEST_PL2_IDENT, TEST_PL2_NAME,
						TEST_PL2_DESC });

		// sl
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_APP_SL0_NAME,
						TEST_APP_SL0_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_APP_SL1_NAME,
						TEST_APP_SL1_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 0, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_APP2_SL0_NAME,
						TEST_APP2_SL0_DESC });
		sqld.execSQL("INSERT INTO \"ServiceLevel\" VALUES(?, 1, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_APP2_SL1_NAME,
						TEST_APP2_SL1_DESC });

		// sl_pl
		sqld.execSQL(
				"INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 1, ?, ?, ?);",
				new String[] { TEST_APP_IDENT, TEST_RG_IDENT, TEST_PL2_IDENT,
						TEST_PL2_VALUE });
		sqld.execSQL(
				"INSERT INTO \"ServiceLevel_PrivacyLevels\" VALUES(?, 1, ?, ?, ?);",
				new String[] { TEST_APP2_IDENT, TEST_RG_IDENT, TEST_PL_IDENT,
						TEST_PL_VALUE });

		preset = ModelSingleton
				.getInstance()
				.getModel()
				.addPreset(TEST_PRESET_NAME, TEST_PRESET_DESC,
						TEST_PRESET_TYPE, TEST_PRESET_IDENT);
		app = ModelSingleton.getInstance().getModel().getApp(TEST_APP_IDENT);
		app2 = ModelSingleton.getInstance().getModel().getApp(TEST_APP2_IDENT);
		plValue2 = app.getServiceLevel(1).getPrivacyLevels()[0];
		plValue = app2.getServiceLevel(1).getPrivacyLevels()[0];
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPresetImpl() {
		assertEquals(TEST_PRESET_NAME, preset.getName());
		assertEquals(TEST_PRESET_DESC, preset.getDescription());
		assertEquals(TEST_PRESET_TYPE, preset.getType());
		assertEquals(TEST_PRESET_IDENT, preset.getIdentifier());
	}

	public void testAddApps() {
		preset.addApp(app, true);

		assertTrue(preset.isAppAssigned(app));
		assertFalse(preset.isAppAssigned(app2));

		assertEquals(1, preset.getAssignedApps().length);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(isIdenticalApp(app, ia));
		}

		preset.addApp(app2, true);
		assertTrue(preset.isAppAssigned(app));
		assertTrue(preset.isAppAssigned(app2));

		assertEquals(2, preset.getAssignedApps().length);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(isIdenticalApp(app, ia) || isIdenticalApp(app2, ia));
		}
	}

	public void testRemoveApps() {
		preset.addApp(app, true);
		preset.addApp(app2, true);

		preset.removeApp(app, true);
		assertFalse(preset.isAppAssigned(app));
		assertTrue(preset.isAppAssigned(app2));

		assertEquals(1, preset.getAssignedApps().length);
		for (IApp ia : preset.getAssignedApps()) {
			assertTrue(isIdenticalApp(app2, ia));
		}

		preset.removeApp(app2, true);
		assertFalse(preset.isAppAssigned(app));
		assertFalse(preset.isAppAssigned(app2));

		assertEquals(0, preset.getAssignedApps().length);
	}

	public void testAddPrivacyLevel() {
		preset.setPrivacyLevel(plValue, true);
		assertEquals(1, preset.getUsedPrivacyLevels().length);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(isIdenticalPL(plValue, ipl));
		}

		preset.setPrivacyLevel(plValue2, true);
		assertEquals(2, preset.getUsedPrivacyLevels().length);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(isIdenticalPL(plValue, ipl) || isIdenticalPL(plValue2, ipl));
		}
	}

	public void testRemovePrivacyLevel() {
		preset.setPrivacyLevel(plValue, true);
		preset.setPrivacyLevel(plValue2, true);

		preset.removePrivacyLevel(plValue, true);
		assertEquals(1, preset.getUsedPrivacyLevels().length);
		for (IPrivacyLevel ipl : preset.getUsedPrivacyLevels()) {
			assertTrue(isIdenticalPL(plValue2, ipl));
		}

		preset.removePrivacyLevel(plValue2, true);
		assertEquals(0, preset.getUsedPrivacyLevels().length);
	}

	/**
	 * Checks whether two {@link IApp}s are identical.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	private boolean isIdenticalApp(IApp expected, IApp actual) {
		return expected.getIdentifier().equals(actual.getIdentifier());
	}

	/**
	 * Checks whether two {@link IPrivacyLevel}s are identical.
	 * 
	 * @param expected
	 * @param actual
	 * @return
	 */
	private boolean isIdenticalPL(IPrivacyLevel expected, IPrivacyLevel actual) {
		boolean sameRG = expected.getResourceGroup().getIdentifier()
				.equals(actual.getResourceGroup().getIdentifier());
		boolean sameIdent = expected.getIdentifier().equals(
				actual.getIdentifier());
		return sameRG && sameIdent;
	}

}
