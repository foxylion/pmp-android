package de.unistuttgart.ipvs.pmp.model.implementations.test;

import junit.framework.TestCase;
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
public class ModelImplTest extends TestCase {

	IApp apps[];
	IResourceGroup ress[];

	public ModelImplTest(String name) {
		super(name);

	}

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
		// there are 7 Apps in the DB --> see testsql.sql
		assertEquals(7, apps.length);
	}

	/**
	 * Testing the queries, passed through testsql.sql
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
 * 
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
	 * 
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
 * 
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

	public void testSLPLokApp() {
		int actualAppIndex = 0;
		IApp app = apps[actualAppIndex];
		IServiceLevel levels[] = app.getServiceLevels();
		
		/*
		 * INSERT INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',0,'ZeroLevel','ZeroLevel'); 
		 * INSERT INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',1,'FirstLevel','FirstLevel'); 
		 * INSERT INTO "ServiceLevel"
		 * VALUES('Sample#pmp.test.okapp',2,'SecondLevel','SecondLevel');
		 */
		assertEquals(3, levels.length);
		assertEquals(0,levels[0].getLevel());
		assertEquals("ZeroLevel", levels[0].getName());
		assertEquals("ZeroLevel", levels[0].getDescription());
		assertEquals(1,levels[0].getPrivacyLevels().length);
		assertEquals(1,levels[1].getPrivacyLevels().length);
		assertEquals(2,levels[2].getPrivacyLevels().length);
	}
//	public void testSLPLwrongApp() {
//		/* Load next query #4 */
//		int actualResIndex = 3;
//		IResourceGroup toTestRes = ress[actualResIndex];
//		/*
//		 * INSERT INTO "ResourceGroup"
//		 * VALUES('Sample#pmp.test.resnoname','SameIdentifier','This ressource
//		 * has same identifier resnoname');
//		 */
//		assertEquals("Sample#pmp.test.resnoname", toTestRes.getIdentifier());
//		assertEquals("SameIdentifier", toTestRes.getName());
//		assertEquals("This ressource has same identifier resnoname",
//				toTestRes.getDescription());
//	}
}
