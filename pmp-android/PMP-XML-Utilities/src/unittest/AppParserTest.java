// /*
// * Copyright 2011 pmp-android development team
// * Project: PMP
// * Project-Site: http://code.google.com/p/pmp-android/
// *
// * ---------------------------------------------------------------------
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
package unittest;

import java.util.Locale;

import junit.framework.TestCase;

import org.junit.Test;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

/**
 * 
 * @author Tobias Kuhn (Adapted by Marcus Vetter)
 * 
 */
public class AppParserTest extends TestCase implements TestConstants {
    
    /*
    * app tests
    */
    @Test
    public void testAppMinimum() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS2_ID },
                new String[] { APP_SF1_REQ_PS2_VALUE });
        
        // disable CDATA for minimum
        XMLNode psValue = xmlSF1.getChildren().get(2).getChildren().get(0);
        psValue.setFlags(psValue.getFlags() & ~XMLNode.CDATA_VALUE);
        
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, ais.getNames().size());
        assertEquals(1, ais.getDescriptions().size());
        assertEquals(1, ais.getServiceFeatures().size());
        
        assertEquals(APP_DEF_NAME, ais.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_DEF_DESC, ais.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        IAISServiceFeature sf1 = ais.getServiceFeatureForIdentifier(APP_SF1_ID);
        assertNotNull(sf1);
        
        assertEquals(1, sf1.getNames().size());
        assertEquals(1, sf1.getDescriptions().size());
        assertEquals(APP_SF1_DEF_NAME, sf1.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_SF1_DEF_DESC, sf1.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        assertEquals(1, sf1.getRequiredResourceGroups().size());
        IAISRequiredResourceGroup rrg = sf1.getRequiredResourceGroupForIdentifier(APP_SF1_REQ_RG1);
        
        assertNotNull(rrg);
        assertEquals(1, rrg.getRequiredPrivacySettings().size());
        assertEquals(APP_SF1_REQ_PS2_VALUE, rrg.getRequiredPrivacySettingForIdentifier(APP_SF1_REQ_PS2_ID).getValue());
        
    }
    
    
    @Test
    public void testAppCDATA() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, ais.getNames().size());
        assertEquals(1, ais.getDescriptions().size());
        assertEquals(1, ais.getServiceFeatures().size());
        
        assertEquals(APP_DEF_NAME, ais.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_DEF_DESC, ais.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        IAISServiceFeature sf1 = ais.getServiceFeatureForIdentifier(APP_SF1_ID);
        assertNotNull(sf1);
        
        assertEquals(1, sf1.getNames().size());
        assertEquals(1, sf1.getDescriptions().size());
        assertEquals(APP_SF1_DEF_NAME, sf1.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_SF1_DEF_DESC, sf1.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        assertEquals(1, sf1.getRequiredResourceGroups().size());
        IAISRequiredResourceGroup rrg = sf1.getRequiredResourceGroupForIdentifier(APP_SF1_REQ_RG1);
        
        assertNotNull(rrg);
        assertEquals(1, rrg.getRequiredPrivacySettings().size());
        assertEquals(APP_SF1_REQ_PS1_VALUE, rrg.getRequiredPrivacySettingForIdentifier(APP_SF1_REQ_PS1_ID).getValue());
        
    }
    
    
    @Test
    public void testAppSFNoSF() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with no service features.",
                TestUtil.assertAISValidation(ais, IAIS.class, null, IssueType.NO_SF_EXISTS));
    }
    
    
    @Test
    public void testAppSFNoReqRG() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with service feature that requires no RGs.",
                TestUtil.assertAISValidation(ais, IAISServiceFeature.class, APP_SF1_ID, IssueType.NO_RRG_EXISTS));
    }
    
    
    @Test
    public void testAppLocalized() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        TestUtil.addLocale(TestUtil.app, APP_LOC_NAME_LOCALE.getLanguage(), APP_LOC_NAME, null);
        TestUtil.addLocale(TestUtil.app, APP_LOC_DESC_LOCALE.getLanguage(), null, APP_LOC_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.addLocale(xmlSF1, APP_SF1_LOC_NAME_LOCALE.getLanguage(), APP_SF1_LOC_NAME, null);
        TestUtil.addLocale(xmlSF1, "he", null, APP_SF1_LOC_DESC);
        // we cannot use the Locale itself here,
        // because Java has some severe problems with Hebrew compatibility mode
        
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(2, ais.getNames().size());
        assertEquals(2, ais.getDescriptions().size());
        assertEquals(1, ais.getServiceFeatures().size());
        
        assertEquals(APP_DEF_NAME, ais.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_DEF_DESC, ais.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_LOC_NAME, ais.getNameForLocale(APP_LOC_NAME_LOCALE));
        assertEquals(APP_LOC_DESC, ais.getDescriptionForLocale(APP_LOC_DESC_LOCALE));
        
        IAISServiceFeature sf1 = ais.getServiceFeatureForIdentifier(APP_SF1_ID);
        assertNotNull(sf1);
        
        assertEquals(2, sf1.getNames().size());
        assertEquals(2, sf1.getDescriptions().size());
        assertEquals(APP_SF1_DEF_NAME, sf1.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_SF1_DEF_DESC, sf1.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_SF1_LOC_NAME, sf1.getNameForLocale(APP_SF1_LOC_NAME_LOCALE));
        assertEquals(APP_SF1_LOC_DESC, sf1.getDescriptionForLocale(APP_SF1_LOC_DESC_LOCALE));
        
        assertEquals(1, sf1.getRequiredResourceGroups().size());
        IAISRequiredResourceGroup rrg = sf1.getRequiredResourceGroupForIdentifier(APP_SF1_REQ_RG1);
        
        assertNotNull(rrg);
        assertEquals(1, rrg.getRequiredPrivacySettings().size());
        assertEquals(APP_SF1_REQ_PS1_VALUE, rrg.getRequiredPrivacySettingForIdentifier(APP_SF1_REQ_PS1_ID).getValue());
    }
    
    
    @Test
    public void testAppInvalidLocale() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        TestUtil.addLocale(TestUtil.app, ILLEGAL_LOCALE, APP_LOC_NAME, APP_LOC_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with illegal locale.",
                TestUtil.assertAISValidation(ais, ILocalizedString.class, null, IssueType.LOCALE_INVALID));
    }
    
    
    @Test
    public void testAppEmptyLocale() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        TestUtil.addLocale(TestUtil.app, "", APP_LOC_NAME, APP_LOC_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with empty locale.",
                TestUtil.assertAISValidation(ais, ILocalizedString.class, null, IssueType.LOCALE_MISSING));
    }
    
    
    @Test
    public void testAppLocaleBelowWrongTag() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        TestUtil.addLocale(TestUtil.main, Locale.GERMAN.getLanguage(), APP_LOC_NAME, APP_LOC_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted app with locale below wrong tag.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.UNEXPECTED_NODE, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testAppSpaces() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        // change all flags
        TestUtil.setFlags(TestUtil.main, XMLNode.INSERT_SPACES_BENIGN);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, ais.getNames().size());
        assertEquals(1, ais.getDescriptions().size());
        assertEquals(1, ais.getServiceFeatures().size());
        
        assertEquals(APP_DEF_NAME, ais.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_DEF_DESC, ais.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        IAISServiceFeature sf1 = ais.getServiceFeatureForIdentifier(APP_SF1_ID);
        assertNotNull(sf1);
        
        assertEquals(1, sf1.getNames().size());
        assertEquals(1, sf1.getDescriptions().size());
        assertEquals(APP_SF1_DEF_NAME, sf1.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(APP_SF1_DEF_DESC, sf1.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        assertEquals(1, sf1.getRequiredResourceGroups().size());
        IAISRequiredResourceGroup rrg = sf1.getRequiredResourceGroupForIdentifier(APP_SF1_REQ_RG1);
        
        assertNotNull(rrg);
        assertEquals(1, rrg.getRequiredPrivacySettings().size());
        assertEquals(APP_SF1_REQ_PS1_VALUE, rrg.getRequiredPrivacySettingForIdentifier(APP_SF1_REQ_PS1_ID).getValue());
    }
    
    
    @Test
    public void testAppDoubleLocaleAttribute() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        TestUtil.appDefDesc.addAttribute(new XMLAttribute(XML_LANG, Locale.GERMAN.getLanguage()));
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted app with double locale attribute.");
        } catch (ParserException xmlpe) {
            //assertEquals(ParserException.Type.LOCALE_INVALID, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testAppEmptyReqRGIdentifier() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, "", new String[] { APP_SF1_REQ_PS1_ID }, new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with empty RG identifier.",
                TestUtil.assertAISValidation(ais, IAISRequiredResourceGroup.class, "", IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testAppEmptyReqRGPSIdentifier() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { "" }, new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with empty PS identifier.",
                TestUtil.assertAISValidation(ais, IAISRequiredPrivacySetting.class, "", IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testAppSFSameIdentifier() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        XMLNode xmlSF2 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF2, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.sfs.addChild(xmlSF2);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with two SF with same identifier.",
                TestUtil.assertAISValidation(ais, IAIS.class, null, IssueType.SF_IDENTIFIER_OCCURRED_TOO_OFTEN));
    }
    
    
    @Test
    public void testAppReqRGSameIdentifier() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE });
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS2_ID },
                new String[] { APP_SF1_REQ_PS2_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with SF with two RGs with same identifier.", TestUtil.assertAISValidation(
                ais, IAISServiceFeature.class, APP_SF1_ID, IssueType.RRG_IDENTIFIER_OCCURRED_TOO_OFTEN));
    }
    
    
    @Test
    public void testAppReqRGPSSameIdentifier() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID, APP_SF1_REQ_PS1_ID },
                new String[] { APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with SF with two PSs with same identifier.", TestUtil.assertAISValidation(
                ais, IAISRequiredResourceGroup.class, APP_SF1_REQ_RG1, IssueType.RPS_IDENTIFIER_OCCURRED_TOO_OFTEN));
    }
    
    
    @Test
    public void testAppTwoSameSFReqs() throws Exception {
        TestUtil.makeApp(APP_DEF_NAME, APP_DEF_DESC);
        XMLNode xmlSF1 = TestUtil.makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID }, new String[] {
                APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
        TestUtil.sfs.addChild(xmlSF1);
        XMLNode xmlSF2 = TestUtil.makeSF(APP_SF2_ID, APP_SF2_DEF_NAME, APP_SF2_DEF_DESC);
        TestUtil.addRequiredRG(xmlSF2, APP_SF1_REQ_RG1, new String[] { APP_SF1_REQ_PS1_ID }, new String[] {
                APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
        TestUtil.sfs.addChild(xmlSF2);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IAIS ais = XMLUtilityProxy.getAppUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted app with SF both requiring only the same PS.", TestUtil.assertAISValidation(ais,
                IAIS.class, null, IssueType.SFS_CONTAIN_SAME_RRG_AND_RPS_WITH_SAME_VALUE));
    }
    
}
