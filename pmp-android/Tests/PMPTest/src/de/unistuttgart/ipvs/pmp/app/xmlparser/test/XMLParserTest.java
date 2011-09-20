package de.unistuttgart.ipvs.pmp.app.xmlparser.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.app.xmlparser.ServiceLevel;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParser;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;
import junit.framework.TestCase;

/**
 * Feeds the XML-Parser with different well- and malformed XML-files and compares it's behavior
 *  
 * @author Patrick Strobel
 * @version 0.1.0
 *
 */
public class XMLParserTest extends TestCase {

	private static final String FILES_PATH = "assets/xml-parser-test";
	
	private Locale de = new Locale("de");
	private Locale en = new Locale("en");
	private Locale sv = new Locale("sv");
	
	private XMLParser parser;

	@Override
	public void setUp() {
		parser = new XMLParser();
	}

	/**
	 * Tests a file containing no XML-data.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testNonXmlFile() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "NonXmlFile.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.SAX_EXCEPTION, e.getType());
		}
	}

	/**
	 * Tests a file with missing <code>AppInformation</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testMissingAppInformation() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "MissingAppInformation.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
		}
	}
	
	/**
	 * Tests a file with missing default-name in <code>AppInformation</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testMissingDefaultAppName() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "MissingDefaultAppName.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
		}
	}
	
	/**
	 * Tests a file with more then one default-names in <code>AppInformation</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testInvalidNumberOfDefaultAppName() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "InvalidNumberOfDefaultAppNames.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN, e.getType());
		}
	}

	/**
	 * Tests a file with missing <code>ServiceLevels</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testMissingServiceLevels() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "MissingServiceLevels.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
		}
	}

	/**
	 * Tests a file with more then one <code>AppInformation</code> sections.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testInvalidNumberOfAppInformation()
			throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "InvalidNumberOfAppInformation.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN,
					e.getType());
		}
	}

	/**
	 * Tests a file with more then one <code>ServiceLevels</code> sections.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testInvalidNumberOfServiceLevels() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "InvalidNumberOfServiceLevels.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN,
					e.getType());
		}
	}

	/**
	 * Tests a file with missing lang-attribute in <code>AppInformation</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testMissingLangAttribute() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "MissingLangAttribute.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.LOCALE_MISSING, e.getType());
		}
	}
	
	/**
	 * Tests a file with a invalid lang-attribute in <code>AppInformation</code> section.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testInvalidLangAttribute() throws FileNotFoundException {
		try {
			parser.parse(new FileInputStream(FILES_PATH + File.separator
					+ "InvalidLangAttribute.xml"));
			fail("Parser accepted malformed XML file!");
		} catch (XMLParserException e) {
			assertEquals("Test exception type", Type.LOCALE_INVALID, e.getType());
		}
	}
	
	/**
	 * Tests a well formed file.
	 * Checks <code>AppInformation</code> section only.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testReadAppData() throws FileNotFoundException {
		try {
			AppInformationSet info = parser.parse(new FileInputStream(
					FILES_PATH + File.separator + "WellFormedFile.xml"));

			// App names
			Map<Locale, String> appNames = info.getNames();
			assertEquals("Number of names", 3, appNames.size());
			assertEquals("German app-name", "KalenderDe", appNames.get(de));
			assertEquals("English app-name", "Calendar", appNames.get(en));
			assertEquals("Swedish app-name", "KalenderSv", appNames.get(sv));

			// App descriptions
			Map<Locale, String> appDescs = info.getDescriptions();
			assertEquals("Number of descriptions", 3, appNames.size());
			assertEquals(
					"German app-description",
					"Die App wird verwendet um die Privacy Manangement Platform zu testen. Sie stellt einfache Funktionalitäten eines Kalenders zur Verfügung.",
					appDescs.get(de));
			assertEquals(
					"English app-description",
					"This App is used to test the privacy management platform. It provides simple calendar functionalities.",
					appDescs.get(en));
			assertEquals(
					"Swedish app-description",
					"App används för att testa plattformen privatliv Manangement. Det ger enkel kalender funktioner.",
					appDescs.get(sv));

		} catch (XMLParserException e) {
			fail("Parser failed reading well formed file!");
		}
	}
	

	/**
	 * Tests a well formed file.
	 * Checks number of service-levels in <code>ServiceLevels</code> section only.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testReadServiceLevelData() throws FileNotFoundException {
		try {
			AppInformationSet info = parser.parse(new FileInputStream(
					FILES_PATH + File.separator + "WellFormedFile.xml"));

			// Service-Level
			Map<Integer, ServiceLevel> sl = info.getServiceLevels();
			assertEquals("Number of service-levels", 2, sl.size());

		} catch (XMLParserException e) {
			fail("Parser failed reading well formed file!");
		}
	}
	

	/**
	 * Tests a well formed file.
	 * Checks data of service-level zero in <code>ServiceLevels</code> section only.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testReadServiceLevel0Data() throws FileNotFoundException {
		try {
			AppInformationSet info = parser.parse(new FileInputStream(
					FILES_PATH + File.separator + "WellFormedFile.xml"));

			// Service-Level
			Map<Integer, ServiceLevel> sl = info.getServiceLevels();

			ServiceLevel slZero = sl.get(0);

			// Name
			Map<Locale, String> slZeroNames = slZero.getNames();
			assertEquals("Number of names in service-level 0", 3,
					slZeroNames.size());
			assertEquals("German name of service-level 0", "Null-LevelDe",
					slZeroNames.get(de));
			assertEquals("English name of service-level 0", "Null-LevelEn",
					slZeroNames.get(en));
			assertEquals("Swedish name of service-level 0", "null nivå",
					slZeroNames.get(sv));

			// Descriptions
			Map<Locale, String> slZeroDescs = slZero.getDescriptions();
			assertEquals("Number of descriptions in service-level 0", 3,
					slZeroDescs.size());
			assertEquals("German description of service-level 0",
					"Ich kann nichts für Sie tun!", slZeroDescs.get(de));
			assertEquals("English description of service-level 0",
					"I can't do anything for you!", slZeroDescs.get(en));
			assertEquals("Swedish description of service-level 0",
					"Jag kan inte göra något för dig!", slZeroDescs.get(sv));

			// Resource-groups
			List<RequiredResourceGroup> slZeroRgs = slZero
					.getRequiredResourceGroups();
			assertEquals("Number of required resource-groups", 0,
					slZeroRgs.size());

		} catch (XMLParserException e) {
			fail("Parser failed reading well formed file!");
		}
	}
	
	/**
	 * Tests a well formed file.
	 * Checks data of service-level one in <code>ServiceLevels</code> section only.
	 * @throws FileNotFoundException Thrown, if test-files does not exist
	 */
	public void testReadServiceLevel1Data() throws FileNotFoundException {
		try {
			AppInformationSet info = parser.parse(new FileInputStream(
					FILES_PATH + File.separator + "WellFormedFile.xml"));

			// Service-Level
			Map<Integer, ServiceLevel> sl = info.getServiceLevels();

			ServiceLevel slOne = sl.get(1);

			// Names
			Map<Locale, String> slOneNames = slOne.getNames();
			assertEquals("Number of names in service-level 1", 3,
					slOneNames.size());
			assertEquals("German name of service-level 1",
					"Vollständiges Level", slOneNames.get(de));
			assertEquals("English name of service-level 1", "Level Complete",
					slOneNames.get(en));
			assertEquals("Swedish name of service-level 1", "Klara nivå",
					slOneNames.get(sv));

			// Descriptions
			Map<Locale, String> slOneDescs = slOne.getDescriptions();
			assertEquals("Number of descriptions in service-level 1", 3,
					slOneDescs.size());
			assertEquals("German description of service-level 1",
					"Ich kann alles, was Sie wünschen!", slOneDescs.get(de));
			assertEquals("English description of service-level 1",
					"I can do anything you want", slOneDescs.get(en));
			assertEquals("Swedish description of service-level 1",
					"Jag kan göra vad du vill!", slOneDescs.get(sv));

			// Resource-groups
			List<RequiredResourceGroup> slOneRgs = slOne
					.getRequiredResourceGroups();
			assertEquals("Number of required resource-groups", 2,
					slOneRgs.size());
			RequiredResourceGroup rgZero = slOneRgs.get(0);
			RequiredResourceGroup rgOne = slOneRgs.get(1);

			// RG-ID
			assertEquals("Identifier of resource-group 0 in service-level 1",
					"de.unistuttgart.ipvs.pmp.resourceGroups.database",
					rgZero.getRgIdentifier());
			
			assertEquals("Identifier of resource-group 1 in service-level 1",
					"de.unistuttgart.ipvs.pmp.resourceGroups.filesystem",
					rgOne.getRgIdentifier());
			
			// RG-PLs
			Map<String,String> rgZeroPl= rgZero.getPrivacyLevelMap();
			Map<String,String> rgOnePl= rgOne.getPrivacyLevelMap();
			
			assertEquals("'Read' privacy-level in resource-group 0", "true", rgZeroPl.get("read"));
			assertEquals("'Write' privacy-level in resource-group 0", "true", rgZeroPl.get("write"));
			
			assertEquals("'Read' privacy-level in resource-group 1", "true", rgOnePl.get("read"));
			assertEquals("'Write' privacy-level in resource-group 1", "false", rgOnePl.get("write"));
			assertEquals("'maxFileSizeForWriting' privacy-level in resource-group 1", "<value>20</value><unit>mb</unit>", rgOnePl.get("maxFileSizeForWriting"));
			
			
		} catch (XMLParserException e) {
			fail("Parser failed reading well formed file!");
		}
	}
}
