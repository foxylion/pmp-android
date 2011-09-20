package de.unistuttgart.ipvs.pmp.model.implementations.test;

import de.unistuttgart.ipvs.pmp.PMPComponentType;
import de.unistuttgart.ipvs.pmp.model.implementations.AppImpl;
import de.unistuttgart.ipvs.pmp.model.implementations.PresetImpl;
import de.unistuttgart.ipvs.pmp.model.implementations.PrivacyLevelImpl;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IPrivacyLevel;
import android.test.AndroidTestCase;

public class PresetImplTest extends AndroidTestCase {

	private static final String TEST_NAME = "TEST_NAME";
	private static final String TEST_DESC = "TEST_DESC";
	private static final String TEST_IDENT = "TEST_IDENT";
	private static final PMPComponentType TEST_TYPE = PMPComponentType.NONE;

	private static final String TEST_IDENT_APP = "TEST_APP";
	private static final String TEST_NAME_APP = "TEST_APP_NAME";
	private static final String TEST_DESC_APP = "TEST_APP_DESC";

	private static final String TEST_IDENT_APP_2 = "TEST_PPA";
	private static final String TEST_NAME_APP_2 = "TEST_PPA_NAME";
	private static final String TEST_DESC_APP_2 = "TEST_PPA_DESC";

	private static final String TEST_RG_IDENT = "TEST_RG";

	private static final String TEST_IDENT_PL = "TEST_PL";
	private static final String TEST_NAME_PL = "TEST_PL_NAME";
	private static final String TEST_DESC_PL = "TEST_PL_DESC";

	private static final String TEST_IDENT_PL_2 = "TEST_LP";
	private static final String TEST_NAME_PL_2 = "TEST_LP_NAME";
	private static final String TEST_DESC_PL_2 = "TEST_LP_DESC";

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testPresetImpl() {
		PresetImpl pi = new PresetImpl(TEST_NAME, TEST_DESC, TEST_TYPE,
				TEST_IDENT);
		assertEquals(pi.getName(), TEST_NAME);
		assertEquals(pi.getDescription(), TEST_DESC);
		assertEquals(pi.getType(), TEST_TYPE);
		assertEquals(pi.getIdentifier(), TEST_IDENT);
	}

	public void testAddApps() {
		AppImpl ai = new AppImpl(TEST_IDENT_APP, TEST_NAME_APP, TEST_DESC_APP);
		AppImpl ai2 = new AppImpl(TEST_IDENT_APP_2, TEST_NAME_APP_2,
				TEST_DESC_APP_2);

		PresetImpl pi = new PresetImpl(TEST_NAME, TEST_DESC, TEST_TYPE,
				TEST_IDENT);
		pi.addApp(ai, true);
		assertTrue(pi.isAppAssigned(ai));
		assertFalse(pi.isAppAssigned(ai2));

		assertEquals(pi.getAssignedApps().length, 1);
		for (IApp ia : pi.getAssignedApps()) {
			assertTrue(ia.equals(ai));
		}

		pi.addApp(ai2, true);
		assertTrue(pi.isAppAssigned(ai));
		assertTrue(pi.isAppAssigned(ai2));

		assertEquals(pi.getAssignedApps().length, 2);
		for (IApp ia : pi.getAssignedApps()) {
			assertTrue(ia.equals(ai) || ia.equals(ai2));
		}
	}

	public void testRemoveApps() {
		AppImpl ai = new AppImpl(TEST_IDENT_APP, TEST_NAME_APP, TEST_DESC_APP);
		AppImpl ai2 = new AppImpl(TEST_IDENT_APP_2, TEST_NAME_APP_2,
				TEST_DESC_APP_2);

		PresetImpl pi = new PresetImpl(TEST_NAME, TEST_DESC, TEST_TYPE,
				TEST_IDENT);
		pi.addApp(ai, true);
		pi.addApp(ai2, true);

		pi.removeApp(ai, true);
		assertFalse(pi.isAppAssigned(ai));
		assertTrue(pi.isAppAssigned(ai2));

		assertEquals(pi.getAssignedApps().length, 1);
		for (IApp ia : pi.getAssignedApps()) {
			assertTrue(ia.equals(ai2));
		}

		pi.removeApp(ai2, true);
		assertFalse(pi.isAppAssigned(ai));
		assertFalse(pi.isAppAssigned(ai2));

		assertEquals(pi.getAssignedApps().length, 0);
	}

	public void testAddPrivacyLevel() {
		PrivacyLevelImpl pli = new PrivacyLevelImpl(TEST_RG_IDENT,
				TEST_IDENT_PL, TEST_NAME_PL, TEST_DESC_PL);
		PrivacyLevelImpl pli2 = new PrivacyLevelImpl(TEST_RG_IDENT,
				TEST_IDENT_PL_2, TEST_NAME_PL_2, TEST_DESC_PL_2);

		PresetImpl pi = new PresetImpl(TEST_NAME, TEST_DESC, TEST_TYPE,
				TEST_IDENT);
		pi.setPrivacyLevel(pli, true);
		assertEquals(pi.getUsedPrivacyLevels().length, 1);
		for (IPrivacyLevel ipl : pi.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pli));
		}

		pi.setPrivacyLevel(pli2, true);
		assertEquals(pi.getUsedPrivacyLevels().length, 2);
		for (IPrivacyLevel ipl : pi.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pli) || ipl.equals(pli2));
		}
	}

	public void testRemovePrivacyLevel() {
		PrivacyLevelImpl pli = new PrivacyLevelImpl(TEST_RG_IDENT,
				TEST_IDENT_PL, TEST_NAME_PL, TEST_DESC_PL);
		PrivacyLevelImpl pli2 = new PrivacyLevelImpl(TEST_RG_IDENT,
				TEST_IDENT_PL_2, TEST_NAME_PL_2, TEST_DESC_PL_2);

		PresetImpl pi = new PresetImpl(TEST_NAME, TEST_DESC, TEST_TYPE,
				TEST_IDENT);
		pi.setPrivacyLevel(pli, true);
		pi.setPrivacyLevel(pli2, true);

		pi.removePrivacyLevel(pli, true);
		assertEquals(pi.getUsedPrivacyLevels().length, 1);
		for (IPrivacyLevel ipl : pi.getUsedPrivacyLevels()) {
			assertTrue(ipl.equals(pli2));
		}

		pi.removePrivacyLevel(pli2, true);
		assertEquals(pi.getUsedPrivacyLevels().length, 0);
	}

}
