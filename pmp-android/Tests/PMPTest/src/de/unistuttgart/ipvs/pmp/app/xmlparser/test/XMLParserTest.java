/*
 * Copyright 2011 pmp-android development team
 * Project: PMPTest
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.app.xmlparser.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.test.InstrumentationTestCase;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;
import de.unistuttgart.ipvs.pmp.app.xmlparser.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.app.xmlparser.ServiceLevel;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParser;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException;
import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;

/**
 * Feeds the XML-Parser with different well- and malformed XML-files and compares it's behavior
 * 
 * @author Frieder Schüler, Patrick Strobel
 * @version 0.3.0
 * 
 */
public class XMLParserTest extends InstrumentationTestCase {
    
    private Locale de = new Locale("de");
    private Locale en = new Locale("en");
    private Locale sv = new Locale("sv");
    
    private XMLParser parser;
    
    
    @Override
    public void setUp() {
        this.parser = new XMLParser();
        ;
    }
    
    
    private InputStream openAssetFile(String file) throws IOException {
        InputStream inputStream = getInstrumentation().getContext().getAssets().open("xml-parser-test/" + file);
        return inputStream;
    }
    
    
    /**
     * Tests a file containing no XML-data.
     * 
     * The nodes appInformationSet and serviceLevels are missing. It is not crucial, that text is between the nodes, so
     * a NODE_MISSING exception is thrown.
     * 
     * @throws IOException
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testNonXmlFile() throws IOException {
        try {
            this.parser.parse(openAssetFile("NonXmlFile.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file with missing <code>AppInformation</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testMissingAppInformation() throws IOException {
        try {
            this.parser.parse(openAssetFile("MissingAppInformation.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file with missing default-name in <code>AppInformation</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testMissingDefaultAppName() throws IOException {
        try {
            this.parser.parse(openAssetFile("MissingDefaultAppName.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file with more then one default-names in <code>AppInformation</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testInvalidNumberOfDefaultAppName() throws IOException {
        try {
            this.parser.parse(openAssetFile("InvalidNumberOfDefaultAppNames.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN, e.getType());
        }
    }
    
    
    /**
     * Tests a file with missing <code>ServiceLevels</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testMissingServiceLevels() throws IOException {
        try {
            this.parser.parse(openAssetFile("MissingServiceLevels.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file with more then one <code>AppInformation</code> sections.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testInvalidNumberOfAppInformation() throws IOException {
        try {
            this.parser.parse(openAssetFile("InvalidNumberOfAppInformation.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN, e.getType());
        }
    }
    
    
    /**
     * Tests a file with more then one <code>ServiceLevels</code> sections.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testInvalidNumberOfServiceLevels() throws IOException {
        try {
            this.parser.parse(openAssetFile("InvalidNumberOfServiceLevels.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_OCCURRED_TOO_OFTEN, e.getType());
        }
    }
    
    
    /**
     * Tests a file with missing lang-attribute in <code>AppInformation</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testMissingLangAttribute() throws IOException {
        try {
            this.parser.parse(openAssetFile("MissingLangAttribute.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.LOCALE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file with a invalid lang-attribute in <code>AppInformation</code> section.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testInvalidLangAttribute() throws IOException {
        try {
            this.parser.parse(openAssetFile("InvalidLangAttribute.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.LOCALE_INVALID, e.getType());
        }
    }
    
    
    /**
     * Tests a file having a resource-group in the default service-level.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testDefaultServiceLevelHasResourceGroup() throws IOException {
        try {
            this.parser.parse(openAssetFile("DefaultServiceLevelHasResourceGroup.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.DEFAULT_SERVICE_LEVEL_MUST_HAVE_NO_REQUIRED_RESOURCE_GROUPS,
                    e.getType());
        }
    }
    
    
    /**
     * Tests a file having no default service-level.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testMissingDefaultServiceLevel() throws IOException {
        try {
            this.parser.parse(openAssetFile("DefaultServiceLevelMissing.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NODE_MISSING, e.getType());
        }
    }
    
    
    /**
     * Tests a file having a invalid level for the default service-level.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testInvalidLevelForDefaultServiceLevel() throws IOException {
        try {
            this.parser.parse(openAssetFile("InvalidLevelForDefaultServiceLevel.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.DEFAULT_SERVICE_LEVEL_IS_NOT_ZERO, e.getType());
        }
    }
    
    
    /**
     * Tests a file having more than one descriptions with the same locale in the app-information.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testSameLocaleForDescAppInfo() throws IOException {
        try {
            this.parser.parse(openAssetFile("SameLocaleForDescAppInfo.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
     * Tests a file having more than one name with the same locale in the app-information.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testSameLocaleForNameAppInfo() throws IOException {
        try {
            this.parser.parse(openAssetFile("SameLocaleForNameAppInfo.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
     * Tests a file having more than one descriptions with the same locale in a service-level.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testSameLocaleForDescServiceLevel() throws IOException {
        try {
            this.parser.parse(openAssetFile("SameLocaleForDescServiceLevel.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
     * Tests a file having more than one name with the same locale in a service-level.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testSameLocaleForNameServiceLevel() throws IOException {
        try {
            this.parser.parse(openAssetFile("SameLocaleForNameServiceLevel.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
     * Tests a file having a service-level with privacy-levels that have the same identifier.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testPrivacyLevelWithSameIdentifier() throws IOException {
        try {
            this.parser.parse(openAssetFile("PrivacyLevelWithSameIdentifier.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.PRIVACY_LEVEL_WITH_SAME_IDENTIFIER_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
	 * 
	 */
    public void testSameServiceLevel() throws IOException {
        try {
            this.parser.parse(openAssetFile("SameServiceLevel.xml"));
            fail("Parser accepted malformed XML file!");
        } catch (XMLParserException e) {
            assertEquals("Test exception type", Type.SERVICE_LEVEL_WITH_SAME_LEVEL_ALREADY_EXISTS, e.getType());
        }
    }
    
    
    /**
     * Tests a well formed file. Checks <code>AppInformation</code> section only.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testReadAppData() throws IOException {
        try {
            AppInformationSet info = this.parser.parse(openAssetFile("WellFormedFile.xml"));
            
            // App names
            Map<Locale, String> appNames = info.getNames();
            assertEquals("Number of names", 3, appNames.size());
            assertEquals("German app-name", "KalenderDe", appNames.get(this.de));
            assertEquals("English app-name", "Calendar", appNames.get(this.en));
            assertEquals("Swedish app-name", "KalenderSv", appNames.get(this.sv));
            
            // App descriptions
            Map<Locale, String> appDescs = info.getDescriptions();
            assertEquals("Number of descriptions", 3, appNames.size());
            assertEquals(
                    "German app-description",
                    "Die App wird verwendet um die Privacy Manangement Platform zu testen. Sie stellt einfache Funktionalitäten eines Kalenders zur Verfügung.",
                    appDescs.get(this.de));
            assertEquals(
                    "English app-description",
                    "This App is used to test the privacy management platform. It provides simple calendar functionalities.",
                    appDescs.get(this.en));
            assertEquals("Swedish app-description",
                    "App används för att testa plattformen privatliv Manangement. Det ger enkel kalender funktioner.",
                    appDescs.get(this.sv));
            
        } catch (XMLParserException e) {
            fail("Parser failed reading well formed file!");
        }
    }
    
    
    /**
     * Tests a well formed file. Checks number of service-levels in <code>ServiceLevels</code> section only.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testReadServiceLevelData() throws IOException {
        try {
            AppInformationSet info = this.parser.parse(openAssetFile("WellFormedFile.xml"));
            
            // Service-Level
            Map<Integer, ServiceLevel> sl = info.getServiceLevels();
            assertEquals("Number of service-levels", 2, sl.size());
            
        } catch (XMLParserException e) {
            fail("Parser failed reading well formed file!");
        }
    }
    
    
    /**
     * Tests a well formed file. Checks data of service-level zero in <code>ServiceLevels</code> section only.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testReadServiceLevel0Data() throws IOException {
        try {
            AppInformationSet info = this.parser.parse(openAssetFile("WellFormedFile.xml"));
            
            // Service-Level
            Map<Integer, ServiceLevel> sl = info.getServiceLevels();
            
            ServiceLevel slZero = sl.get(0);
            
            // Name
            Map<Locale, String> slZeroNames = slZero.getNames();
            assertEquals("Number of names in service-level 0", 3, slZeroNames.size());
            assertEquals("German name of service-level 0", "Null-LevelDe", slZeroNames.get(this.de));
            assertEquals("English name of service-level 0", "Null-LevelEn", slZeroNames.get(this.en));
            assertEquals("Swedish name of service-level 0", "null nivå", slZeroNames.get(this.sv));
            
            // Descriptions
            Map<Locale, String> slZeroDescs = slZero.getDescriptions();
            assertEquals("Number of descriptions in service-level 0", 3, slZeroDescs.size());
            assertEquals("German description of service-level 0", "Ich kann nichts für Sie tun!",
                    slZeroDescs.get(this.de));
            assertEquals("English description of service-level 0", "I can't do anything for you!",
                    slZeroDescs.get(this.en));
            assertEquals("Swedish description of service-level 0", "Jag kan inte göra något för dig!",
                    slZeroDescs.get(this.sv));
            
            // Resource-groups
            List<RequiredResourceGroup> slZeroRgs = slZero.getRequiredResourceGroups();
            assertEquals("Number of required resource-groups", 0, slZeroRgs.size());
            
        } catch (XMLParserException e) {
            fail("Parser failed reading well formed file!");
        }
    }
    
    
    /**
     * Tests a well formed file. Checks data of service-level one in <code>ServiceLevels</code> section only.
     * 
     * @throws FileNotFoundException
     *             Thrown, if test-files does not exist
     */
    public void testReadServiceLevel1Data() throws IOException {
        try {
            AppInformationSet info = this.parser.parse(openAssetFile("WellFormedFile.xml"));
            
            // Service-Level
            Map<Integer, ServiceLevel> sl = info.getServiceLevels();
            
            ServiceLevel slOne = sl.get(1);
            
            // Names
            Map<Locale, String> slOneNames = slOne.getNames();
            assertEquals("Number of names in service-level 1", 3, slOneNames.size());
            assertEquals("German name of service-level 1", "Vollständiges Level", slOneNames.get(this.de));
            assertEquals("English name of service-level 1", "Level Complete", slOneNames.get(this.en));
            assertEquals("Swedish name of service-level 1", "Klara nivå", slOneNames.get(this.sv));
            
            // Descriptions
            Map<Locale, String> slOneDescs = slOne.getDescriptions();
            assertEquals("Number of descriptions in service-level 1", 3, slOneDescs.size());
            assertEquals("German description of service-level 1", "Ich kann alles, was Sie wünschen!",
                    slOneDescs.get(this.de));
            assertEquals("English description of service-level 1", "I can do anything you want",
                    slOneDescs.get(this.en));
            assertEquals("Swedish description of service-level 1", "Jag kan göra vad du vill!", slOneDescs.get(this.sv));
            
            // Resource-groups
            List<RequiredResourceGroup> slOneRgs = slOne.getRequiredResourceGroups();
            assertEquals("Number of required resource-groups", 2, slOneRgs.size());
            RequiredResourceGroup rgZero = slOneRgs.get(0);
            RequiredResourceGroup rgOne = slOneRgs.get(1);
            
            // RG-ID
            assertEquals("Identifier of resource-group 0 in service-level 1",
                    "de.unistuttgart.ipvs.pmp.resourceGroups.database", rgZero.getRgIdentifier());
            
            assertEquals("Identifier of resource-group 1 in service-level 1",
                    "de.unistuttgart.ipvs.pmp.resourceGroups.filesystem", rgOne.getRgIdentifier());
            
            // RG-PLs
            Map<String, String> rgZeroPl = rgZero.getPrivacyLevelMap();
            Map<String, String> rgOnePl = rgOne.getPrivacyLevelMap();
            
            assertEquals("'Read' privacy-level in resource-group 0", "true", rgZeroPl.get("read"));
            assertEquals("'Write' privacy-level in resource-group 0", "true", rgZeroPl.get("write"));
            
            assertEquals("'Read' privacy-level in resource-group 1", "true", rgOnePl.get("read"));
            assertEquals("'Write' privacy-level in resource-group 1", "false", rgOnePl.get("write"));
            assertEquals("'maxFileSizeForWriting' privacy-level in resource-group 1",
                    "<value>20</value><unit>mb</unit>", rgOnePl.get("maxFileSizeForWriting"));
            
        } catch (XMLParserException e) {
            fail("Parser failed reading well formed file!");
        }
    }
}
