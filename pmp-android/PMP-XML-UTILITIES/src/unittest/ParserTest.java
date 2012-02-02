/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
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
package unittest;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.util.Locale;
import java.util.Random;

import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityFactory;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ISException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * 
 * @author Tobias Kuhn (Adapted by Marcus Vetter)
 * 
 */
public class ParserTest {

	/*
	 * strings used during the tests.
	 */

	// app
	private static final String APP_DEF_NAME = "Testing App";
	private static final String APP_DEF_DESC = "The module test app.";

	private static final String APP_LOC_NAME = "\u30c6\u30b9\u30c8\u30a2\u30d7\u30ea";
	private static final Locale APP_LOC_NAME_LOCALE = Locale.JAPANESE;
	private static final String APP_LOC_DESC = "\u8a72\u6a21\u584a\u7684\u6e2c\u8a66\u7a0b\u5e8f\u3002";
	private static final Locale APP_LOC_DESC_LOCALE = Locale.CHINESE;

	private static final String APP_SF1_ID = "serviceFeature";
	private static final String APP_SF1_DEF_NAME = "A useless service feature";
	private static final String APP_SF1_DEF_DESC = "If it comes to service features, it can't get worse than this.";

	private static final String APP_SF2_ID = "OutOfServiceFeature";
	private static final String APP_SF2_DEF_NAME = "You didn't expect this";
	private static final String APP_SF2_DEF_DESC = "Maybe it can always get worse.";

	private static final String APP_SF1_LOC_NAME = "M\u1ed9t t\u00ednh n\u0103ng d\u1ecbch v\u1ee5 v\u00f4 d\u1ee5ng";
	private static final Locale APP_SF1_LOC_NAME_LOCALE = new Locale("vi");
	private static final String APP_SF1_LOC_DESC = "\u05d0\u05dd \u05de\u05d3\u05d5\u05d1\u05e8 \u05e2\u05dc \u05ea\u05db"
			+ "\u05d5\u05e0\u05d5\u05ea \u05d4\u05e9\u05d9\u05e8\u05d5\u05ea, \u05d6"
			+ "\u05d4 \u05dc\u05d0 \u05d9\u05db\u05d5\u05dc \u05dc\u05d4\u05d9\u05d5"
			+ "\u05ea \u05d9\u05d5\u05ea\u05e8 \u05d2\u05e8\u05d5\u05e2 \u05de\u05d6"
			+ "\u05d4.";
	private static final Locale APP_SF1_LOC_DESC_LOCALE = new Locale("he");

	private static final String APP_SF1_REQ_RG1 = "required.resource.group";
	private static final String APP_SF1_REQ_PS1_ID = "required.privacy.setting";
	private static final String APP_SF1_REQ_PS1_VALUE = "<logic> patterns 2\u2219\u03c0\u2219r;; <//\\>, ]]> <\\/> $";
	private static final String APP_SF1_REQ_PS2_ID = "just4fun";
	private static final String APP_SF1_REQ_PS2_VALUE = "lalalalala";

	// RG
	private static final String RG_ID = "best.resource.group.ever";
	private static final String RG_ICON = "icons/best.png";
	private static final String RG_REVISION = "123456789";
	private static final String RG_NAME = "The Champions";
	private static final String RG_DESC = "Best. Resource. Group. Ever.";

	private static final String RG_PS1_ID = "privacy.setting";
	private static final String RG_PS1_NAME = "Privacy sitting";
	private static final String RG_PS1_DESC = "Sits with you in your privacy.";

	private static final String RG_PSn_ID = "privacy.setting.%d";
	private static final String RG_PSn_NAME = "Privacy sitting %d";
	private static final String RG_PSn_DESC = "Sits with you in your %dth privacy.";

	// default
	private static final String ILLEGAL_LOCALE = "\u0623\u0646";

	/*
	 * statics used to access results of creation methods
	 */

	private static XMLNode main;

	private static XMLNode app, appDefName, appDefDesc, sfs;

	private static XMLNode rg, rgIcon, rgRev, rgDefName, rgDefDesc, pss;

	/*
	 * constants for use in creation methods
	 */

	private static final String XML_APP_INFORMATION_SET = "appInformationSet";
	private static final String XML_APP_INFORMATION = "appInformation";
	private static final String XML_SERVICE_FEATURES = "serviceFeatures";
	private static final String XML_RESOURCE_GROUP_INFORMATION_SET = "resourceGroupInformationSet";
	private static final String XML_RESOURCE_GROUP_INFORMATION = "resourceGroupInformation";
	private static final String XML_ICON = "icon";
	private static final String XML_REVISION = "revision";
	private static final String XML_DEFAULT_EN = "en";
	private static final String XML_PRIVACY_SETTINGS = "privacySettings";
	private static final String XML_SERVICE_FEATURE = "serviceFeature";
	private static final String XML_REQUIRED_RESOURCE_GROUP = "requiredResourceGroup";
	private static final String XML_PRIVACY_SETTING = "privacySetting";
	private static final String XML_DEFAULT_DESCRIPTION = "defaultDescription";
	private static final String XML_DEFAULT_NAME = "defaultName";
	private static final String XML_IDENTIFIER = "identifier";
	private static final String XML_DESCRIPTION = "description";
	private static final String XML_LANG = "lang";
	private static final String XML_NAME = "name";

	private static final Locale XML_DEFAULT_EN_LOCALE = Locale.ENGLISH;

	/*
	 * automatic creation methods
	 */

	private void makeApp(String name, String desc) {
		main = new XMLNode(XML_APP_INFORMATION_SET);

		app = new XMLNode(XML_APP_INFORMATION);
		appDefName = new XMLNode(XML_DEFAULT_NAME);
		appDefName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		appDefName.setContent(name);
		appDefDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
		appDefDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		appDefDesc.setContent(desc);

		app.addChild(appDefName);
		app.addChild(appDefDesc);

		sfs = new XMLNode(XML_SERVICE_FEATURES);

		main.addChild(app);
		main.addChild(sfs);
	}

	private void makeRG(String id, String icon, String revision, String name,
			String desc) {
		main = new XMLNode(XML_RESOURCE_GROUP_INFORMATION_SET);

		rg = new XMLNode(XML_RESOURCE_GROUP_INFORMATION);
		rg.addAttribute(new XMLAttribute(XML_IDENTIFIER, id));

		rgIcon = new XMLNode(XML_ICON);
		rgIcon.setContent(icon);
		rgRev = new XMLNode(XML_REVISION);
		rgRev.setContent(revision);

		rgDefName = new XMLNode(XML_DEFAULT_NAME);
		rgDefName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		rgDefName.setContent(name);
		rgDefDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
		rgDefDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		rgDefDesc.setContent(desc);

		rg.addChild(rgIcon);
		rg.addChild(rgRev);
		rg.addChild(rgDefName);
		rg.addChild(rgDefDesc);

		pss = new XMLNode(XML_PRIVACY_SETTINGS);

		main.addChild(rg);
		main.addChild(pss);
	}

	private XMLNode makeSF(String id, String name, String desc) {
		XMLNode sf = new XMLNode(XML_SERVICE_FEATURE);
		sf.addAttribute(new XMLAttribute(XML_IDENTIFIER, id));

		XMLNode defName = new XMLNode(XML_DEFAULT_NAME);
		defName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		defName.setContent(name);
		sf.addChild(defName);

		XMLNode defDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
		defDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		defDesc.setContent(desc);
		sf.addChild(defDesc);

		return sf;
	}

	private void addRequiredRG(XMLNode sf, String rgId, String[] psId,
			String[] psValue) {
		XMLNode reqRG = new XMLNode(XML_REQUIRED_RESOURCE_GROUP);
		reqRG.addAttribute(new XMLAttribute(XML_IDENTIFIER, rgId));

		for (int i = 0; i < Math.min(psId.length, psValue.length); i++) {
			XMLNode reqPS = new XMLNode(XML_PRIVACY_SETTING);
			reqPS.addAttribute(new XMLAttribute(XML_IDENTIFIER, psId[i]));
			reqPS.setCDATAContent(psValue[i]);
			reqRG.addChild(reqPS);
		}

		sf.addChild(reqRG);
	}

	private XMLNode makePS(String id, String name, String desc) {
		XMLNode ps = new XMLNode(XML_PRIVACY_SETTING);
		ps.addAttribute(new XMLAttribute(XML_IDENTIFIER, id));

		XMLNode defName = new XMLNode(XML_DEFAULT_NAME);
		defName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		defName.setContent(name);
		ps.addChild(defName);

		XMLNode defDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
		defDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
		defDesc.setContent(desc);
		ps.addChild(defDesc);

		return ps;
	}

	private void addLocale(XMLNode at, String locale, String name, String desc) {
		if (name != null) {
			XMLNode newName = new XMLNode(XML_NAME);
			newName.addAttribute(new XMLAttribute(XML_LANG, locale));
			newName.setContent(name);
			at.addChild(newName);
		}
		if (desc != null) {
			XMLNode newDesc = new XMLNode(XML_DESCRIPTION);
			newDesc.addAttribute(new XMLAttribute(XML_LANG, locale));
			newDesc.setContent(desc);
			at.addChild(newDesc);
		}
	}

	private void setFlags(XMLNode node, int flag) {
		node.setFlags(node.getFlags() | flag);
		for (XMLNode child : node.getChildren()) {
			setFlags(child, flag);
		}
	}

	protected void debug(String name) {
		System.out
				.println("################################################################");
		System.out.println("Input for " + name + " was:");
		System.out.println(XMLCompiler.compile(main));
		System.out
				.println("----------------------------------------------------------------");
	}

	/*
	 * app tests
	 */
	@Test
	public void testAppMinimum() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS2_ID },
				new String[] { APP_SF1_REQ_PS2_VALUE });

		// disable CDATA for minimum
		XMLNode psValue = xmlSF1.getChildren().get(2).getChildren().get(0);
		psValue.setFlags(psValue.getFlags() & ~XMLNode.CDATA_VALUE);

		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		AIS ais = XMLUtilityFactory.getAppUtilities().parseAISXML(
				XMLCompiler.compileStream(main));

		assertEquals(1, ais.getNames().size());
		assertEquals(1, ais.getDescriptions().size());
		assertEquals(1, ais.getServiceFeaturesMap().size());

		assertEquals(APP_DEF_NAME, ais.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_DEF_DESC,
				ais.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		ServiceFeature sf1 = ais.getServiceFeaturesMap().get(APP_SF1_ID);
		assertNotNull(sf1);

		assertEquals(1, sf1.getNames().size());
		assertEquals(1, sf1.getDescriptions().size());
		assertEquals(APP_SF1_DEF_NAME, sf1.getNames()
				.get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_SF1_DEF_DESC,
				sf1.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		assertEquals(1, sf1.getRequiredResourceGroups().size());
		RequiredResourceGroup rrg = sf1.getRequiredResourceGroups().get(
				APP_SF1_REQ_RG1);

		assertNotNull(rrg);
		assertEquals(1, rrg.getPrivacySettingsMap().size());
		assertEquals(APP_SF1_REQ_PS2_VALUE,
				rrg.getPrivacySettingsMap().get(APP_SF1_REQ_PS2_ID));

	}

	@Test
	public void testAppCDATA() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		AIS ais = XMLUtilityFactory.getAppUtilities().parseAISXML(
				XMLCompiler.compileStream(main));

		assertEquals(1, ais.getNames().size());
		assertEquals(1, ais.getDescriptions().size());
		assertEquals(1, ais.getServiceFeaturesMap().size());

		assertEquals(APP_DEF_NAME, ais.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_DEF_DESC,
				ais.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		ServiceFeature sf1 = ais.getServiceFeaturesMap().get(APP_SF1_ID);
		assertNotNull(sf1);

		assertEquals(1, sf1.getNames().size());
		assertEquals(1, sf1.getDescriptions().size());
		assertEquals(APP_SF1_DEF_NAME, sf1.getNames()
				.get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_SF1_DEF_DESC,
				sf1.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		assertEquals(1, sf1.getRequiredResourceGroups().size());
		RequiredResourceGroup rrg = sf1.getRequiredResourceGroups().get(
				APP_SF1_REQ_RG1);

		assertNotNull(rrg);
		assertEquals(1, rrg.getPrivacySettingsMap().size());
		assertEquals(APP_SF1_REQ_PS1_VALUE,
				rrg.getPrivacySettingsMap().get(APP_SF1_REQ_PS1_ID));

	}

	@Test
	public void testAppSFNoSF() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with no service features.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.SERVICE_FEATURE_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppSFNoReqRG() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with service feature that requires no RGs.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.REQUIRED_RESOURCE_GROUP_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppLocalized() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		addLocale(app, APP_LOC_NAME_LOCALE.getLanguage(), APP_LOC_NAME, null);
		addLocale(app, APP_LOC_DESC_LOCALE.getLanguage(), null, APP_LOC_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		addLocale(xmlSF1, APP_SF1_LOC_NAME_LOCALE.getLanguage(),
				APP_SF1_LOC_NAME, null);
		addLocale(xmlSF1, "he", null, APP_SF1_LOC_DESC);
		// we cannot use the Locale itself here,
		// because Java has some severe problems with Hebrew compatibility mode

		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		AIS ais = XMLUtilityFactory.getAppUtilities().parseAISXML(
				XMLCompiler.compileStream(main));

		assertEquals(2, ais.getNames().size());
		assertEquals(2, ais.getDescriptions().size());
		assertEquals(1, ais.getServiceFeaturesMap().size());

		assertEquals(APP_DEF_NAME, ais.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_DEF_DESC,
				ais.getDescriptions().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_LOC_NAME, ais.getNames().get(APP_LOC_NAME_LOCALE));
		assertEquals(APP_LOC_DESC,
				ais.getDescriptions().get(APP_LOC_DESC_LOCALE));

		ServiceFeature sf1 = ais.getServiceFeaturesMap().get(APP_SF1_ID);
		assertNotNull(sf1);

		assertEquals(2, sf1.getNames().size());
		assertEquals(2, sf1.getDescriptions().size());
		assertEquals(APP_SF1_DEF_NAME, sf1.getNames()
				.get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_SF1_DEF_DESC,
				sf1.getDescriptions().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_SF1_LOC_NAME,
				sf1.getNames().get(APP_SF1_LOC_NAME_LOCALE));
		assertEquals(APP_SF1_LOC_DESC,
				sf1.getDescriptions().get(APP_SF1_LOC_DESC_LOCALE));

		assertEquals(1, sf1.getRequiredResourceGroups().size());
		RequiredResourceGroup rrg = sf1.getRequiredResourceGroups().get(
				APP_SF1_REQ_RG1);

		assertNotNull(rrg);
		assertEquals(1, rrg.getPrivacySettingsMap().size());
		assertEquals(APP_SF1_REQ_PS1_VALUE,
				rrg.getPrivacySettingsMap().get(APP_SF1_REQ_PS1_ID));
	}

	@Test
	public void testAppInvalidLocale() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		addLocale(app, ILLEGAL_LOCALE, APP_LOC_NAME, APP_LOC_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with illegal locale.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.LOCALE_INVALID, xmlpe.getType());
		}
	}

	@Test
	public void testAppEmptyLocale() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		addLocale(app, "", APP_LOC_NAME, APP_LOC_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with empty locale.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.LOCALE_MISSING, xmlpe.getType());
		}
	}

	@Test
	public void testAppLocaleBelowWrongTag() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		addLocale(main, Locale.GERMAN.getLanguage(), APP_LOC_NAME, APP_LOC_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with locale below wrong tag.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.UNEXPECTED_NODE, xmlpe.getType());
		}
	}

	@Test
	public void testAppSpaces() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		// change all flags
		setFlags(main, XMLNode.INSERT_SPACES_BENIGN);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		AIS ais = XMLUtilityFactory.getAppUtilities().parseAISXML(
				XMLCompiler.compileStream(main));

		assertEquals(1, ais.getNames().size());
		assertEquals(1, ais.getDescriptions().size());
		assertEquals(1, ais.getServiceFeaturesMap().size());

		assertEquals(APP_DEF_NAME, ais.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_DEF_DESC,
				ais.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		ServiceFeature sf1 = ais.getServiceFeaturesMap().get(APP_SF1_ID);
		assertNotNull(sf1);

		assertEquals(1, sf1.getNames().size());
		assertEquals(1, sf1.getDescriptions().size());
		assertEquals(APP_SF1_DEF_NAME, sf1.getNames()
				.get(XML_DEFAULT_EN_LOCALE));
		assertEquals(APP_SF1_DEF_DESC,
				sf1.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		assertEquals(1, sf1.getRequiredResourceGroups().size());
		RequiredResourceGroup rrg = sf1.getRequiredResourceGroups().get(
				APP_SF1_REQ_RG1);

		assertNotNull(rrg);
		assertEquals(1, rrg.getPrivacySettingsMap().size());
		assertEquals(APP_SF1_REQ_PS1_VALUE,
				rrg.getPrivacySettingsMap().get(APP_SF1_REQ_PS1_ID));
	}

	@Test
	public void testAppWrongDefaultLocale() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);

		app.removeChild(appDefDesc);
		appDefDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
		appDefDesc.addAttribute(new XMLAttribute(XML_LANG, Locale.GERMAN
				.getLanguage()));
		appDefDesc.setContent(APP_DEF_DESC);
		app.addChild(appDefDesc);

		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with wrong default locale.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.LOCALE_INVALID, xmlpe.getType());
		}
	}

	@Test
	public void testAppDoubleLocaleAttribute() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		appDefDesc.addAttribute(new XMLAttribute(XML_LANG, Locale.GERMAN
				.getLanguage()));
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with double locale attribute.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.LOCALE_INVALID, xmlpe.getType());
		}
	}

	@Test
	public void testAppEmptyReqRGIdentifier() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, "", new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with empty RG identifier.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.IDENTIFIER_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppEmptyReqRGPSIdentifier() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] { "" },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with empty PS identifier.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.IDENTIFIER_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppSFSameIdentifier() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF1);

		XMLNode xmlSF2 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF2, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		sfs.addChild(xmlSF2);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with two SF with same identifier.");
		} catch (ISException xmlpe) {
			assertEquals(
					ISException.Type.SERVICE_FEATURE_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppReqRGSameIdentifier() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID },
				new String[] { APP_SF1_REQ_PS1_VALUE });
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS2_ID },
				new String[] { APP_SF1_REQ_PS2_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with SF with two RGs with same identifier.");
		} catch (ISException xmlpe) {
			assertEquals(
					ISException.Type.REQUIRED_RESOUCEGROUP_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppReqRGPSSameIdentifier() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1, new String[] {
				APP_SF1_REQ_PS1_ID, APP_SF1_REQ_PS1_ID }, new String[] {
				APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
		sfs.addChild(xmlSF1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with SF with two PSs with same identifier.");
		} catch (ISException xmlpe) {
			assertEquals(
					ISException.Type.PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testAppTwoSameSFReqs() throws Exception {
		makeApp(APP_DEF_NAME, APP_DEF_DESC);
		XMLNode xmlSF1 = makeSF(APP_SF1_ID, APP_SF1_DEF_NAME, APP_SF1_DEF_DESC);
		addRequiredRG(xmlSF1, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID }, new String[] {
						APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
		sfs.addChild(xmlSF1);
		XMLNode xmlSF2 = makeSF(APP_SF2_ID, APP_SF2_DEF_NAME, APP_SF2_DEF_DESC);
		addRequiredRG(xmlSF2, APP_SF1_REQ_RG1,
				new String[] { APP_SF1_REQ_PS1_ID }, new String[] {
						APP_SF1_REQ_PS1_VALUE, APP_SF1_REQ_PS2_VALUE });
		sfs.addChild(xmlSF2);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getAppUtilities().parseAISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted app with SF both requiring only the same PS.");
		} catch (ParserException xmlpe) {
			assertEquals(
					ParserException.Type.AT_LEAST_TWO_SFS_ADDRESS_SAME_RRGS_AND_PSS,
					xmlpe.getType());
		}
	}

	/*
	 * rg tests
	 */
	@Test
	public void testRGMinimum() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		RGIS rgis = XMLUtilityFactory.getRGUtilities().parseRGISXML(
				XMLCompiler.compileStream(main));

		assertEquals(1, rgis.getNames().size());
		assertEquals(1, rgis.getDescriptions().size());
		assertEquals(1, rgis.getPrivacySettingsMap().size());

		assertEquals(RG_NAME, rgis.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(RG_DESC, rgis.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		PrivacySetting ps1 = rgis.getPrivacySettingsMap().get(RG_PS1_ID);
		assertNotNull(ps1);

		assertEquals(1, ps1.getNames().size());
		assertEquals(1, ps1.getDescriptions().size());
		assertEquals(RG_PS1_NAME, ps1.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(RG_PS1_DESC,
				ps1.getDescriptions().get(XML_DEFAULT_EN_LOCALE));
	}

	@Test
	public void testRGNoPS() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with no PS.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.PRIVACY_SETTING_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGLarge() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		for (int i = 1; i < 100; i++) {
			String nId = String.format(RG_PSn_ID, i);
			String nName = String.format(RG_PSn_NAME, i);
			String nDesc = String.format(RG_PSn_DESC, i);

			XMLNode xmlPSn = makePS(nId, nName, nDesc);
			pss.addChild(xmlPSn);
		}

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		RGIS rgis = XMLUtilityFactory.getRGUtilities().parseRGISXML(
				XMLCompiler.compileStream(main));

		assertEquals(1, rgis.getNames().size());
		assertEquals(1, rgis.getDescriptions().size());
		assertEquals(99, rgis.getPrivacySettingsMap().size());

		assertEquals(RG_NAME, rgis.getNames().get(XML_DEFAULT_EN_LOCALE));
		assertEquals(RG_DESC, rgis.getDescriptions().get(XML_DEFAULT_EN_LOCALE));

		for (int i = 1; i < 100; i++) {
			String nId = String.format(RG_PSn_ID, i);
			String nName = String.format(RG_PSn_NAME, i);
			String nDesc = String.format(RG_PSn_DESC, i);

			PrivacySetting psn = rgis.getPrivacySettingsMap().get(nId);
			assertNotNull(psn);

			assertEquals(1, psn.getNames().size());
			assertEquals(1, psn.getDescriptions().size());
			assertEquals(nName, psn.getNames().get(XML_DEFAULT_EN_LOCALE));
			assertEquals(nDesc, psn.getDescriptions()
					.get(XML_DEFAULT_EN_LOCALE));
		}
	}

	@Test
	public void testRGNoCloseISBlock() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);
		main.setFlags(main.getFlags() | XMLNode.NO_CLOSE_TAG);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with unclosed IS block.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.SAX_EXCEPTION, xmlpe.getType());
		}
	}

	@Test
	public void testRGNoCloseAttrib() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		rgDefName.setFlags(rgDefName.getFlags() | XMLNode.NO_CLOSE_TAG);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with unclosed name block.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.SAX_EXCEPTION, xmlpe.getType());
		}
	}

	@Test
	public void testRGWrongMainTag() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);
		main.setName("iAmNotAResourceGroup");

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with wrong main tag.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.BAD_ROOT_NODE_NAME,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGEmptyIdentifier() throws Exception {
		makeRG("", RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG without identifier.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.IDENTIFIER_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGEmptyPSIdentifier() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS("", RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with PS without identifier.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.IDENTIFIER_MISSING,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGPSSameIdentifier() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);
		XMLNode xmlPS2 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS2);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with two PS with same identifier.");
		} catch (ISException xmlpe) {
			assertEquals(
					ISException.Type.PRIVACY_SETTING_WITH_SAME_IDENTIFIER_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGDoubleLocale() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		addLocale(rg, Locale.FRENCH.getLanguage(), RG_NAME, null);
		addLocale(rg, Locale.FRENCH.getLanguage(), RG_NAME, null);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with two same locales.");
		} catch (ISException xmlpe) {
			assertEquals(ISException.Type.NAME_WITH_SAME_LOCALE_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGDoubleDefNormLocale() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);
		addLocale(rg, Locale.ENGLISH.getLanguage(), null, RG_DESC);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with locale the same as default locale.");
		} catch (ISException xmlpe) {
			assertEquals(
					ISException.Type.DESCRIPTION_WITH_SAME_LOCALE_ALREADY_EXISTS,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGDoubleRG() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		// just add the same again
		main.addChild(main.getChildren().get(0));

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with double RG tag.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.NODE_OCCURRED_TOO_OFTEN,
					xmlpe.getType());
		}
	}

	@Test
	public void testRGEmptyName() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, "", RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with empty name.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.VALUE_MISSING, xmlpe.getType());
		}
	}

	@Test
	public void testRGEmptyDesc() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, "");
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with empty description.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.VALUE_MISSING, xmlpe.getType());
		}
	}

	@Test
	public void testRGPSEmptyName() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, "", RG_PS1_DESC);
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with PS with empty name.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.VALUE_MISSING, xmlpe.getType());
		}
	}

	@Test
	public void testRGPSEmptyDesc() throws Exception {
		makeRG(RG_ID, RG_ICON, RG_REVISION, RG_NAME, RG_DESC);
		XMLNode xmlPS1 = makePS(RG_PS1_ID, RG_PS1_NAME, "");
		pss.addChild(xmlPS1);

		StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
		debug(ste.getMethodName());

		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(
					XMLCompiler.compileStream(main));
			fail("Parser accepted RG with PS with empty description.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.VALUE_MISSING, xmlpe.getType());
		}
	}

	/*
	 * exception tests
	 */
	@Test
	public void testInvalidFile() throws Exception {

		String str = "This is some plain-text, that should not be interpreted as"
				+ " an <?xml version=\"1.0\" ?><resourceGroupInformationSet>"
				+ "</resourceGroupInformationSet>";
		ByteArrayInputStream bais = new ByteArrayInputStream(
				str.getBytes("UTF-8"));
		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(bais);
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
			XMLUtilityFactory.getRGUtilities().parseRGISXML(bais);
			fail("Parser accepted something totally invalid.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.IO_EXCEPTION, xmlpe.getType());
		}
	}

	@Test
	public void testNullFile() throws Exception {
		try {
			XMLUtilityFactory.getRGUtilities().parseRGISXML(null);
			fail("Parser accepted null.");
		} catch (ParserException xmlpe) {
			assertEquals(ParserException.Type.NULL_XML_STREAM, xmlpe.getType());
		}
	}

}
