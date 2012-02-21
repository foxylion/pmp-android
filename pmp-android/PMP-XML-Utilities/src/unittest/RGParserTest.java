package unittest;

import java.io.ByteArrayInputStream;
import java.util.Locale;
import java.util.Random;

import junit.framework.TestCase;

import org.junit.Test;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class RGParserTest extends TestCase implements TestConstants {
    
    /*
    * rg tests
    */
    @Test
    public void testRGMinimum() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, rgis.getNames().size());
        assertEquals(1, rgis.getDescriptions().size());
        assertEquals(1, rgis.getPrivacySettings().size());
        
        assertEquals(RG_NAME, rgis.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(RG_DESC, rgis.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        IRGISPrivacySetting ps1 = rgis.getPrivacySettingForIdentifier(RG_PS1_ID);
        assertNotNull(ps1);
        
        assertEquals(1, ps1.getNames().size());
        assertEquals(1, ps1.getDescriptions().size());
        assertEquals(RG_PS1_NAME, ps1.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(RG_PS1_DESC, ps1.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        assertTrue("Validator did not accept minimum RG.", TestUtil.assertRGISValidationEmpty(rgis));
    }
    
    
    @Test
    public void testRGNoIcon() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        TestUtil.rg.removeAttribute(TestUtil.rgIcon);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with no Icon.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, RG_ID, IssueType.ICON_MISSING));
    }
    
    
    @Test
    public void testRGNoClass() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        TestUtil.rg.removeAttribute(TestUtil.rgClass);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with no ClassName.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, RG_ID, IssueType.CLASSNAME_MISSING));
    }
    
    
    @Test
    public void testRGNoPS() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with no PS.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, RG_ID, IssueType.NO_PS_EXISTS));
    }
    
    
    @Test
    public void testRGLarge() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        for (int i = 1; i < 100; i++) {
            String nId = String.format(RG_PSn_ID, i);
            String nName = String.format(RG_PSn_NAME, i);
            String nDesc = String.format(RG_PSn_DESC, i);
            
            XMLNode xmlPSn = TestUtil.makePS(nId, nName, nDesc, RG_PS_CD, RG_PS_VVD);
            TestUtil.pss.addChild(xmlPSn);
        }
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, rgis.getNames().size());
        assertEquals(1, rgis.getDescriptions().size());
        assertEquals(99, rgis.getPrivacySettings().size());
        
        assertEquals(RG_NAME, rgis.getNameForLocale(XML_DEFAULT_EN_LOCALE));
        assertEquals(RG_DESC, rgis.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        
        for (int i = 1; i < 100; i++) {
            String nId = String.format(RG_PSn_ID, i);
            String nName = String.format(RG_PSn_NAME, i);
            String nDesc = String.format(RG_PSn_DESC, i);
            
            IRGISPrivacySetting psn = rgis.getPrivacySettingForIdentifier(nId);
            assertNotNull(psn);
            
            assertEquals(1, psn.getNames().size());
            assertEquals(1, psn.getDescriptions().size());
            assertEquals(nName, psn.getNameForLocale(XML_DEFAULT_EN_LOCALE));
            assertEquals(nDesc, psn.getDescriptionForLocale(XML_DEFAULT_EN_LOCALE));
        }
        
        assertTrue("Validator did not accept large RG.", TestUtil.assertRGISValidationEmpty(rgis));
    }
    
    
    @Test
    public void testRGNoCloseISBlock() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        TestUtil.main.setFlags(TestUtil.main.getFlags() | XMLNode.NO_CLOSE_TAG);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted RG with unclosed IS block.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.SAX_EXCEPTION, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testRGNoCloseAttrib() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        TestUtil.rgDefName.setFlags(TestUtil.rgDefName.getFlags() | XMLNode.NO_CLOSE_TAG);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted RG with unclosed name block.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.SAX_EXCEPTION, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testRGWrongMainTag() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        TestUtil.main.setName("iAmNotAResourceGroup");
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted RG with wrong main tag.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.BAD_ROOT_NODE_NAME, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testRGEmptyIdentifier() throws Exception {
        TestUtil.makeRG("", RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG without identifier.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, "", IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testRGEmptyPSIdentifier() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS("", RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with PS without identifier.",
                TestUtil.assertRGISValidation(rgis, IRGISPrivacySetting.class, "", IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testRGPSSameIdentifier() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        XMLNode xmlPS2 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS2);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with two PS with same identifier.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, RG_ID, IssueType.PS_IDENTIFIER_OCCURRED_TOO_OFTEN));
    }
    
    
    @Test
    public void testRGDoubleLocale() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        TestUtil.addLocale(TestUtil.rg, Locale.FRENCH.getLanguage(), RG_NAME, null);
        TestUtil.addLocale(TestUtil.rg, Locale.FRENCH.getLanguage(), RG_NAME, null);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with two same locales.",
                TestUtil.assertRGISValidation(rgis, IRGIS.class, RG_ID, IssueType.NAME_LOCALE_OCCURRED_TOO_OFTEN));
    }
    
    
    @Test
    public void testRGDoubleRG() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        // just add the same again
        TestUtil.main.addChild(TestUtil.main.getChildren().get(0));
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Parser accepted RG with double RG tag.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.NODE_OCCURRED_TOO_OFTEN, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testRGEmptyName() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, "", RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with empty name.",
                TestUtil.assertRGISValidation(rgis, ILocalizedString.class, null, IssueType.EMPTY_VALUE));
    }
    
    
    @Test
    public void testRGEmptyDesc() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, "");
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with empty description.",
                TestUtil.assertRGISValidation(rgis, ILocalizedString.class, null, IssueType.EMPTY_VALUE));
    }
    
    
    @Test
    public void testRGPSEmptyName() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, "", RG_PS1_DESC, RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with PS with empty name.",
                TestUtil.assertRGISValidation(rgis, ILocalizedString.class, null, IssueType.EMPTY_VALUE));
    }
    
    
    @Test
    public void testRGPSEmptyDesc() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, "", RG_PS_CD, RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with PS with empty description.",
                TestUtil.assertRGISValidation(rgis, ILocalizedString.class, null, IssueType.EMPTY_VALUE));
    }
    
    
    @Test
    public void testRGPSEmptyChangeDesc() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, "", RG_PS_VVD);
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with PS with empty change description.",
                TestUtil.assertRGISValidation(rgis, ILocalizedString.class, null, IssueType.EMPTY_VALUE));
    }
    
    
    @Test
    public void testRGPSEmptyValidValueDesc() throws Exception {
        TestUtil.makeRG(RG_ID, RG_ICON, RG_CLASS_NAME, RG_REVISION, RG_NAME, RG_DESC);
        XMLNode xmlPS1 = TestUtil.makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC, RG_PS_CD, "");
        TestUtil.pss.addChild(xmlPS1);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted RG with PS with empty valid value description.", TestUtil.assertRGISValidation(
                rgis, IRGISPrivacySetting.class, RG_PS1_ID, IssueType.VALID_VALUE_DESCRIPTION_MISSING));
    }
    
    
    /*
    * exception tests
    */
    @Test
    public void testInvalidFile() throws Exception {
        
        String str = "This is some plain-text, that should not be interpreted as"
                + " an <?xml version=\"1.0\" ?><resourceGroupInformationSet>" + "</resourceGroupInformationSet>";
        ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes("UTF-8"));
        try {
            XMLUtilityProxy.getRGUtil().parse(bais);
            fail("Parser accepted something invalid.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.SAX_EXCEPTION, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testTotallyInvalidFile() throws Exception {
        // !!!
        // Note that this test may fail in very, very, very rare circumstances!
        // It will be very, very, very rare but is not impossible.
        Random r = new Random();
        byte[] buf = new byte[32768];
        r.nextBytes(buf);
        
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        try {
            XMLUtilityProxy.getRGUtil().parse(bais);
            fail("Parser accepted something totally invalid.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.IO_EXCEPTION, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testNullFile() throws Exception {
        try {
            XMLUtilityProxy.getRGUtil().parse(null);
            fail("Parser accepted null.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.NULL_XML_STREAM, xmlpe.getType());
        }
    }
    
    
    public void testCleanRGISIssues() throws Exception {
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IRGIS rgis = XMLUtilityProxy.getRGUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        TestUtil.assertNoIssues(rgis);
        assertTrue("No issues in clean AIS issues test.",
                XMLUtilityProxy.getRGUtil().getValidator().validateRGIS(rgis, true).size() > 0);
        XMLUtilityProxy.getRGUtil().getValidator().clearIssuesAndPropagate(rgis);
        TestUtil.assertNoIssues(rgis);
        
    }
}
