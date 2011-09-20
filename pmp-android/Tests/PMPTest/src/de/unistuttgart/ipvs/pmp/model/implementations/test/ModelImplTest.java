package de.unistuttgart.ipvs.pmp.model.implementations.test;

import android.test.AndroidTestCase;
import de.unistuttgart.ipvs.pmp.model.DatabaseOpenHelper;
import de.unistuttgart.ipvs.pmp.model.DatabaseSingleton;
import de.unistuttgart.ipvs.pmp.model.ModelSingleton;
import de.unistuttgart.ipvs.pmp.model.interfaces.IApp;
import de.unistuttgart.ipvs.pmp.model.interfaces.IResourceGroup;
import de.unistuttgart.ipvs.pmp.model.interfaces.IServiceLevel;

/**
 * Testing the Model
 * 
 * @author Alexander Wassiljew
 * 
 */
public class ModelImplTest extends AndroidTestCase {

	IApp apps[];
	IResourceGroup ress[];

	/**
	 * Set up the testing environment
	 */
	protected void setUp() throws Exception {
		DatabaseSingleton.getInstance().getDatabaseHelper().cleanTables();
		/* Load the testsql.sql with the testing queries */
		String sqlQueries = DatabaseSingleton.getInstance().getDatabaseHelper()
				.readSqlFile("testsql.sql");
		DatabaseOpenHelper.executeMultipleQueries(DatabaseSingleton
				.getInstance().getDatabaseHelper().getWritableDatabase(),
				sqlQueries);
		apps = ModelSingleton.getInstance().getModel().getApps();
		ress = ModelSingleton.getInstance().getModel().getResourceGroups();
	}

	/**
	 * Testing the Preconditions
	 */
	public void testPreconditions() {
		assertNotNull(apps);
		assertNotNull(ress);
	}

	public void testAppsLength() {
		// there are 7 Apps in the DB --> see PMP/assets/testsql.sql
		// If some are broken, there are fewer.
		assertEquals(7, apps.length);
	}

	/**
	 * Testing the query #0 which is right conformed
	 */
	public void testAppQueryNR0() {
		int actualAppIndex = 0;
		/*
		 * Testing the query #0: INSERT INTO "App"
		 * VALUES('Sample#pmp.test.okapp', 'OK App', 'This is conformaly defined
		 * App', 0);
		 */
		IApp toTestApp = apps[actualAppIndex];
		assertEquals("Sample#pmp.test.okapp", toTestApp.getIdentifier());
		assertEquals("OK App", toTestApp.getName());
		assertEquals("This is conformaly defined App",
				toTestApp.getDescription());
		assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #1, which has been modified: - empty Name
	 * 
	 */
	public void testAppQueryNR1() {
		/* Load next query */
		int actualAppIndex = 1;
		IApp toTestApp = apps[actualAppIndex];
		/*
		 * Testing the query #1: INSERT INTO "App"
		 * VALUES('Sample#pmp.test.noname', '', 'This app have no name', 0);
		 */
		assertEquals("Sample#pmp.test.noname", toTestApp.getIdentifier());
		assertEquals("", toTestApp.getName());
		assertEquals("This app have no name", toTestApp.getDescription());
		assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #2, which has been modified: - no Identifier Commented,
	 * because this query didn't make it to the DB
	 * 
	 * Test succeed, because broken queries shouldn't get to the DB
	 */

	// public void testAppQueryNR2() {
	// /* Load next query #2 */
	// int actualAppIndex = 2;
	// IApp toTestApp = apps[actualAppIndex];
	// /*
	// * INSERT INTO "App" VALUES(,'TestNoIdentifier','This app have no
	// * Identifier',0);
	// */
	// assertNull(toTestApp.getIdentifier());
	// assertEquals("TestNoIdentifier", toTestApp.getName());
	// assertEquals("This app have no Identifier", toTestApp.getDescription());
	// assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	// }

	/**
	 * Testing the query #3, which has been modified: - empty Identifier Apps
	 * with empty Identifier shouldn't be in the DB
	 */
	public void testAppQueryNR3() {
		/* Load next query #3 */
		int actualAppIndex = 2;
		IApp toTestApp = apps[actualAppIndex];
		/*
		 * INSERT INTO "App" VALUES('','TestEmptyIdentifier','This app have
		 * empty Identifier',0);
		 */
		assertEquals("", toTestApp.getIdentifier());
		assertEquals("TestEmptyIdentifier", toTestApp.getName());
		assertEquals("This app have no Identifier", toTestApp.getDescription());
		assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #4, which has been modified: - no description
	 */
	public void testAppQueryNR4() {
		/* Load next query #4 */
		int actualAppIndex = 3;
		IApp toTestApp = apps[actualAppIndex];
		/*
		 * INSERT INTO "App" VALUES('Sample#pmp.test.nodescription',
		 * 'TestNoDescription', '', 0);
		 */
		assertEquals("Sample#pmp.test.nodescription", toTestApp.getIdentifier());
		assertEquals("TestNoDescription", toTestApp.getName());
		assertEquals("", toTestApp.getDescription());
		assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #5, which has been modified: - no active level
	 * Commented, because this query didn't make it to the DB
	 * 
	 * Test succeed, because broken queries shouldn't get to the DB
	 */
	// public void testAppQueryNR5() {
	// /* Load next query #5 */
	// int actualAppIndex = 4;
	// IApp toTestApp = apps[actualAppIndex];
	// /*
	// * INSERT INTO "App" VALUES('Sample#pmp.test.nolevel','TestNoLevel',
	// * 'This app has no level',);
	// */
	// assertEquals("Sample#pmp.test.nolevel", toTestApp.getIdentifier());
	// assertEquals("TestNoLevel", toTestApp.getName());
	// assertEquals("This app has no level", toTestApp.getDescription());
	// assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	// }

	/**
	 * Testing the query #6, which has been modified: - false active level
	 * 
	 * Exception: NullPointerException If active level is not available, PMP
	 * should change it to the lowest available level
	 */
	public void testAppQueryNR6() {
		/* Load next query #6 */
		int actualAppIndex = 4;
		IApp toTestApp = apps[actualAppIndex];
		/*
		 * INSERT INTO "App" VALUES('Sample#pmp.test.falselevel',
		 * 'TestFalseLevel', 'This app has false level set', 5);
		 */
		assertEquals("Sample#pmp.test.falselevel", toTestApp.getIdentifier());
		assertEquals("TestFalseLevel", toTestApp.getName());
		assertEquals("This app has false level set", toTestApp.getDescription());
		assertEquals(5, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #7, which has been modified: </br>- Same Identifier as
	 * in query #0, but different Name, Description
	 * 
	 * </br></br>The first App with the same Identifier is in the DB, all the
	 * next Apps wont get in the DB with the same Identifier Exception:
	 * ArrayIndexOutOfBoundException
	 */
	public void testAppQueryNR7() {
		/* Load next query #7 */
		int actualAppIndex = 5;
		IApp toTestApp = apps[actualAppIndex];
		/*
		 * INSERT INTO "App" VALUES('Sample#pmp.test.okapp','OK App2nd','This is
		 * conformaly defined App 2nd', 0);
		 */
		assertEquals("Sample#pmp.test.okapp", toTestApp.getIdentifier());
		assertEquals("OK App2nd", toTestApp.getName());
		assertEquals("This is conformaly defined App 2nd",
				toTestApp.getDescription());
		assertEquals(0, toTestApp.getActiveServiceLevel().getLevel());
	}

	/**
	 * Testing the query #0, which has been modified: </br>- empty Identifier
	 * Res with empty Identifier shouldn't be in the DB
	 */
	public void testResGQueryNR0() {
		/* Load query #0 */
		int actualResIndex = 0;
		IResourceGroup toTestRes = ress[actualResIndex];
		/*
		 * INSERT INTO "ResourceGroup" VALUES('','TestResEmptyIdentifier','This
		 * ressource has empty identifier');
		 */
		assertEquals("", toTestRes.getIdentifier());
		assertEquals("TestResEmptyIdentifier", toTestRes.getName());
		assertEquals("This ressource has empty identifier",
				toTestRes.getDescription());
	}

	/**
	 * Testing the query #1, which has been modified: </br>- empty Name
	 */
	public void testResGQueryNR1() {
		/* Load next query #1 */
		int actualResIndex = 1;
		IResourceGroup toTestRes = ress[actualResIndex];
		/*
		 * INSERT INTO "ResourceGroup"
		 * VALUES('Sample#pmp.test.resnoname','','This ressource has no name');
		 */
		assertEquals("Sample#pmp.test.resnoname", toTestRes.getIdentifier());
		assertEquals("", toTestRes.getName());
		assertEquals("This ressource has no name", toTestRes.getDescription());
	}

	/**
	 * Testing the query #2, which has been modified: </br>- empty Description
	 */
	public void testResGQueryNR2() {
		/* Load next query #2 */
		int actualResIndex = 2;
		IResourceGroup toTestRes = ress[actualResIndex];
		/*
		 * INSERT INTO "ResourceGroup"
		 * VALUES('Sample#pmp.test.resnodescription','TestResNoDescription','');
		 */
		assertEquals("Sample#pmp.test.resnodescription",
				toTestRes.getIdentifier());
		assertEquals("TestResNoDescription", toTestRes.getName());
		assertEquals("", toTestRes.getDescription());
	}

	/**
	 * Testing the query #3, which has been modified: </br>- no Identifier
	 * Commented, because this query didn't make it to the DB
	 * 
	 * </br>Test succeed, because broken queries shouldn't get to the DB
	 */
	// public void testResGQueryNR3() {
	// /* Load next query #3 */
	// int actualResIndex = 3;
	// IResourceGroup toTestRes = ress[actualResIndex];
	// /*
	// * INSERT INTO "ResourceGroup" VALUES(,'TestResNoIdentifier','This
	// * ressource has no identifier');
	// */
	// assertNull(toTestRes.getIdentifier());
	// assertEquals("TestResNoIdentifier", toTestRes.getName());
	// assertEquals("This ressource has no identifier",
	// toTestRes.getDescription());
	// }

	/**
	 * Testing the query #4, which has been modified: </br>- same Identifier as
	 * in query #1, but different Name, Description
	 * 
	 * </br> Exception: ArrayIndexOutOfBoundException
	 */
	public void testResGQueryNR4() {
		/* Load next query #4 */
		int actualResIndex = 3;
		IResourceGroup toTestRes = ress[actualResIndex];
		/*
		 * INSERT INTO "ResourceGroup"
		 * VALUES('Sample#pmp.test.resnoname','SameIdentifier','This ressource
		 * has same identifier resnoname');
		 */
		assertEquals("Sample#pmp.test.resnoname", toTestRes.getIdentifier());
		assertEquals("SameIdentifier", toTestRes.getName());
		assertEquals("This ressource has same identifier resnoname",
				toTestRes.getDescription());
	}

	/**
	 * Testing the queries for Service Level/ Privacy Level of App with the
	 * Identifier: </br>- Sample#pmp.test.okapp
	 */

	public void testQuerySLPLokApp() {
		int actualAppIndex = 0;
		IApp app = apps[actualAppIndex];
		IServiceLevel levels[] = app.getServiceLevels();

		/*
		 * INSERT INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',0,'ZeroLevel','ZeroLevel'); INSERT
		 * INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',1,'FirstLevel','FirstLevel'); INSERT
		 * INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',2,'SecondLevel','SecondLevel');
		 */
		assertEquals(3, levels.length);
		assertEquals(0, levels[0].getLevel());
		assertEquals("ZeroLevel", levels[0].getName());
		assertEquals("ZeroLevel", levels[0].getDescription());
		/*
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.okapp',0,'Sample#pmp.test.resnoname','write','true');
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.okapp',1,'Sample#pmp.test.resnodescription','delete','true');
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.okapp',2,'Sample#pmp.test.resnoname','write','true');
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.okapp',2,'Sample#pmp.test.resnodescription','delete','true');
		 */
		assertEquals(1, levels[0].getPrivacyLevels().length);
		assertEquals(1, levels[1].getPrivacyLevels().length);
		assertEquals(2, levels[2].getPrivacyLevels().length);
	}

	/**
	 * Testing the queries for Service Level/ Privacy Level of App with the
	 * Identifier: </br>- Sample#pmp.test.noname
	 */
	public void testQuerySLPLNoName() {
		int actualAppIndex = 1;
		IApp app = apps[actualAppIndex];
		IServiceLevel levels[] = app.getServiceLevels();
		// INSERT INTO "ServiceLevel"
		// VALUES('Sample#pmp.test.noname',0,'ZeroLevel','ZeroLevel');
		assertEquals(1, levels.length);
		assertEquals(0, levels[0].getLevel());
		assertEquals("ZeroLevel", levels[0].getName());
		assertEquals("ZeroLevel", levels[0].getDescription());
		/*
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.noname',0,'Sample#pmp.test.resnodescription','delete','true');
		 * INSERT INTO "ServiceLevel_PrivacyLevels" VALUES(
		 * 'Sample#pmp.test.noname',0,'Sample#pmp.test.resNoRes','delete','true');
		 */
		// Waiting for 2 PrivacyLevels.
		// 1st is well formed
		// 2nd has no available RessourceGroup
		assertEquals(2, levels[0].getPrivacyLevels().length);
	}

	/**
	 * Testing the queries for Service Level/ Privacy Level of App with the
	 * Identifier: </br>- ""
	 */
	public void testQuerySLPLEmptyIdentifier() {
		int actualAppIndex = 2;
		IApp app = apps[actualAppIndex];
		IServiceLevel levels[] = app.getServiceLevels();
		// INSERT INTO "ServiceLevel" VALUES('',0,'ZeroLevel','ZeroLevel');
		assertEquals(1, levels.length);
		assertEquals(0, levels[0].getLevel());
		assertEquals("ZeroLevel", levels[0].getName());
		assertEquals("ZeroLevel", levels[0].getDescription());
		/*
		 * INSERT INTO "ServiceLevel_PrivacyLevels"
		 * VALUES('',0,'Sample#pmp.test.resnodescription','delete','true');
		 */
		assertEquals(1, levels[0].getPrivacyLevels().length);
	}

	/**
	 * Testing the queries for Service Level/ Privacy Level of App with the
	 * Identifier: </br>- "Sample#pmp.test.falselevel"
	 */
	public void testQuerySLPLFalseLevel() {
		int actualAppIndex = 4;
		IApp app = apps[actualAppIndex];
		IServiceLevel levels[] = app.getServiceLevels();
		// INSERT INTO "ServiceLevel"
		// VALUES('Sample#pmp.test.falselevel',3,'ThirdLevel','ThirdLevel');
		// INSERT INTO "ServiceLevel"
		// VALUES('Sample#pmp.test.falselevel',6,'SixthLevel','SixthLevel');
		assertEquals(2, levels.length);
		assertEquals(3, levels[0].getLevel());
		assertEquals("ThirdLevel", levels[0].getName());
		assertEquals("ThirdLevel", levels[0].getDescription());
		// INSERT INTO "ServiceLevel_PrivacyLevels"
		// VALUES('Sample#pmp.test.falselevel',3,'Sample#pmp.test.resnodescription','delete','true');
		assertEquals(1, levels[0].getPrivacyLevels().length);
	}
}
