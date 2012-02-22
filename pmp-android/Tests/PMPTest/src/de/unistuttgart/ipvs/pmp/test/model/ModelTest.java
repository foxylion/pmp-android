package de.unistuttgart.ipvs.pmp.test.model;

import android.content.pm.PackageManager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import de.unistuttgart.ipvs.pmp.gui.main.ActivityMain;
import de.unistuttgart.ipvs.pmp.model.Model;

public class ModelTest extends ActivityInstrumentationTestCase2<ActivityMain> {

	public ModelTest() {
		super("de.unistuttgart.ipvs.pmp", ActivityMain.class);
	}

	/**
	 * App package constants
	 */
	private static final String SIMPLE_APP = "de.unistuttgart.ipvs.pmp.apps.simpleapp";
	private static final String CALENDAR_APP = "de.unistuttgart.ipvs.pmp.apps.calendarapp";
	private static final String CALENDAR_WIDGET = "de.unistuttgart.ipvs.pmp.apps.calendarwidget";
	private static final String VHIKE = "de.unistuttgart.ipvs.pmp.apps.vhike";
	private static final String INFO_APP = "de.unistuttgart.ipvs.pmp.apps.app3";
	private static final String LOCATION_TEST_APP = "de.unistuttgart.ipvs.pmp.apps.locationtestapp";

	/**
	 * RG package constants
	 */
	private static final String SWITCHES_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.switches";
	private static final String EMAIL_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.email";
	private static final String LOCATION_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.location";
	private static final String DATABASE_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.database";
	private static final String FILESYSTEM_RG = "de.unistuttgart.ipvs.pmp.resourcegroups.filesystem";

	/**
	 * list of apps to be installed (on the device, not in the model!)
	 */
	private static final String[] INSTALLED_APPS = new String[] { SIMPLE_APP,
			CALENDAR_APP, CALENDAR_WIDGET, VHIKE, INFO_APP, LOCATION_TEST_APP };
	private static boolean installChecked = false;

	@Override
	protected void setUp() throws Exception {

		/**
		 * 
		 * 
		 * Please reinstall PMP and install all apps named above, before you
		 * execute the tests!
		 * 
		 * 
		 */

		try {
			if (!installChecked) {
				// check for the appropriate device setup
				for (String iapp : INSTALLED_APPS) {
					getActivity().getPackageManager().getPackageInfo(iapp,
							PackageManager.GET_ACTIVITIES);
				}
				// only once though
				installChecked = true;
			}

		} catch (Exception e) {
			String errorMsg = "Model test cannot be performed: Exception during setup check. See Java file.";
			Log.e("PMP-Test", errorMsg, e);
			throw new Exception(errorMsg);
		}
	}

	public void testInstallApp() throws Exception {
		assertEquals(0, Model.getInstance().getApps().size());

		// SIMPLE_APP
		Model.getInstance().registerApp(SIMPLE_APP);
		assertEquals(1, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(SIMPLE_APP));

		// CALENDAR_APP
		Model.getInstance().registerApp(CALENDAR_APP);
		assertEquals(2, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(CALENDAR_APP));

		// CALENDAR_WIDGET
		Model.getInstance().registerApp(CALENDAR_WIDGET);
		assertEquals(3, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(CALENDAR_WIDGET));

		// VHIKE
		Model.getInstance().registerApp(VHIKE);
		assertEquals(4, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(VHIKE));

		// INFO_APP
		Model.getInstance().registerApp(INFO_APP);
		assertEquals(5, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(INFO_APP));

		// LOCATION_TEST_APP
		Model.getInstance().registerApp(LOCATION_TEST_APP);
		assertEquals(6, Model.getInstance().getApps().size());
		assertNotNull(Model.getInstance().getApp(LOCATION_TEST_APP));
	}

	public void testInstallRG() throws Exception {
		assertEquals(0, Model.getInstance().getResourceGroups().size());

		// SWITCHES_RG
		assertTrue(Model.getInstance().installResourceGroup(SWITCHES_RG, false));
		assertEquals(1, Model.getInstance().getResourceGroups().size());
		assertNotNull(Model.getInstance().getResourceGroup(SWITCHES_RG));

		// EMAIL_RG
		assertTrue(Model.getInstance().installResourceGroup(EMAIL_RG, false));
		assertEquals(2, Model.getInstance().getResourceGroups().size());
		assertNotNull(Model.getInstance().getResourceGroup(EMAIL_RG));

		// LOCATION_RG
		assertTrue(Model.getInstance().installResourceGroup(LOCATION_RG, false));
		assertEquals(3, Model.getInstance().getResourceGroups().size());
		assertNotNull(Model.getInstance().getResourceGroup(LOCATION_RG));

		// DATABASE_RG
		assertTrue(Model.getInstance().installResourceGroup(DATABASE_RG, false));
		assertEquals(4, Model.getInstance().getResourceGroups().size());
		assertNotNull(Model.getInstance().getResourceGroup(DATABASE_RG));

		// FILESYSTEM_RG
		assertTrue(Model.getInstance().installResourceGroup(FILESYSTEM_RG,
				false));
		assertEquals(5, Model.getInstance().getResourceGroups().size());
		assertNotNull(Model.getInstance().getResourceGroup(FILESYSTEM_RG));
	}

}
