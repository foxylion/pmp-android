/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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

import junit.framework.TestCase;

import org.junit.Test;

import unittest.TestUtil.ContextBean;
import unittest.TestUtil.EmptyValueHandling;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class PresetParserTest extends TestCase implements TestConstants {
    
    @Test
    public void testPresetMinimum() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals(PRESET1_IDENTIFIER, p.getIdentifier());
        assertEquals(PRESET1_CREATOR, p.getCreator());
        assertEquals(PRESET1_NAME, p.getName());
        assertEquals(PRESET1_DESCRIPTION, p.getDescription());
        
        assertEquals(1, p.getAssignedApps().size());
        assertEquals(PRESET1_APP1_IDENTIFIER, p.getAssignedApps().get(0).getIdentifier());
        
        assertEquals(1, p.getAssignedPrivacySettings().size());
        IPresetAssignedPrivacySetting aps = p.getAssignedPrivacySettings().get(0);
        
        assertEquals(RG_ID, aps.getRgIdentifier());
        assertEquals(RG_REVISION, aps.getRgRevision());
        assertEquals(RG_PS1_ID, aps.getPsIdentifier());
        assertEquals(APP_SF1_REQ_PS1_VALUE, aps.getValue());
        
        assertEquals(0, aps.getContexts().size());
        
        assertTrue("Validator did not accept minimum Preset.", TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetContexts() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] {
                new ContextBean(CONTEXT1_TYPE, CONTEXT1_CONDITION, CONTEXT1_OVERRIDE),
                new ContextBean(CONTEXT2_TYPE, CONTEXT2_CONDITION, CONTEXT2_OVERRIDE) };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals(PRESET1_IDENTIFIER, p.getIdentifier());
        assertEquals(PRESET1_CREATOR, p.getCreator());
        assertEquals(PRESET1_NAME, p.getName());
        assertEquals(PRESET1_DESCRIPTION, p.getDescription());
        
        assertEquals(1, p.getAssignedApps().size());
        assertEquals(PRESET1_APP1_IDENTIFIER, p.getAssignedApps().get(0).getIdentifier());
        
        assertEquals(1, p.getAssignedPrivacySettings().size());
        IPresetAssignedPrivacySetting aps = p.getAssignedPrivacySettings().get(0);
        
        assertEquals(RG_ID, aps.getRgIdentifier());
        assertEquals(RG_REVISION, aps.getRgRevision());
        assertEquals(RG_PS1_ID, aps.getPsIdentifier());
        assertEquals(APP_SF1_REQ_PS1_VALUE, aps.getValue());
        
        assertEquals(contexts.length, aps.getContexts().size());
        for (int i = 0; i < contexts.length; i++) {
            assertEquals(contexts[i].getType(), aps.getContexts().get(i).getType());
            assertEquals(contexts[i].getCondition(), aps.getContexts().get(i).getCondition());
            assertEquals(contexts[i].getOverrideValue(), aps.getContexts().get(i).getOverrideValue());
        }
        
        assertTrue("Validator did not accept Preset with Contexts.", TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetContextWrongPlace() throws Exception {
        TestUtil.makePresetSet();
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        TestUtil.main.addChild(new XMLNode(XML_CONTEXT));
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        try {
            XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
            fail("Validator accepted Preset with context in the wrong place.");
        } catch (ParserException xmlpe) {
            assertEquals(ParserException.Type.UNEXPECTED_NODE, xmlpe.getType());
        }
    }
    
    
    @Test
    public void testPresetEmptyName() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, "", PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertTrue("Validator accepted Preset with empty name.",
                TestUtil.assertPSValidation(pset, IPreset.class, PRESET1_IDENTIFIER, IssueType.NAME_MISSING));
    }
    
    
    @Test
    public void testPresetEmptyCreator() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, "", PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertTrue("Validator accepted Preset with empty creator.",
                TestUtil.assertPSValidation(pset, IPreset.class, PRESET1_IDENTIFIER, IssueType.CREATOR_MISSING));
    }
    
    
    @Test
    public void testPresetEmptyDesc() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, "");
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals("", p.getDescription());
        
        assertTrue("Validator did not accept Preset with empty description.",
                TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetAAEmptyId() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, "");
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted Preset with empty app identifier assigned.",
                TestUtil.assertPSValidation(pset, IPresetAssignedApp.class, "", IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testPresetAAMissingId() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, (String) null);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted Preset with missing description.",
                TestUtil.assertPSValidation(pset, IPresetAssignedApp.class, null, IssueType.IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testPresetAPSEmptyIdent() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, "", RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertTrue("Validator accepted Preset with empty APS identifier.", TestUtil.assertPSValidation(pset,
                IPresetAssignedPrivacySetting.class, null, IssueType.RG_IDENTIFIER_MISSING));
    }
    
    
    @Test
    public void testPresetAPSEmptyRevision() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, "", RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, new ContextBean[] {},
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertTrue("Validator accepted Preset with empty revision.", TestUtil.assertPSValidation(pset,
                IPresetAssignedPrivacySetting.class, null, IssueType.RG_REVISION_MISSING));
    }
    
    
    @Test
    public void testPresetAPSInvalidRevision() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_INVALID_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE,
                new ContextBean[] {}, EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertTrue("Validator accepted Preset with invalid revision.", TestUtil.assertPSValidation(pset,
                IPresetAssignedPrivacySetting.class, null, IssueType.RG_REVISION_INVALID));
    }
    
    
    @Test
    public void testPresetAPSEmptyValue() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, "", new ContextBean[] {},
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals(1, p.getAssignedPrivacySettings().size());
        IPresetAssignedPrivacySetting aps = p.getAssignedPrivacySettings().get(0);
        
        assertEquals("", aps.getValue());
        
        assertTrue("Validator did not accept Preset with empty APS value.", TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetAPSMissingValue() throws Exception {
        TestUtil.makePresetSet();
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, null, new ContextBean[] {},
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted Preset with missing APS value.",
                TestUtil.assertPSValidation(pset, IPresetAssignedPrivacySetting.class, null, IssueType.VALUE_MISSING));
    }
    
    
    @Test
    public void testPresetContextEmptyValue() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] { new ContextBean(CONTEXT1_TYPE, CONTEXT1_CONDITION, "") };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals(1, p.getAssignedPrivacySettings().size());
        IPresetAssignedPrivacySetting aps = p.getAssignedPrivacySettings().get(0);
        
        assertEquals(1, aps.getContexts().size());
        IPresetPSContext c = aps.getContexts().get(0);
        
        assertEquals("", c.getOverrideValue());
        
        assertTrue("Validator did not accept Preset with empty Context override value.",
                TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetContextMissingValue() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] { new ContextBean(CONTEXT1_TYPE, CONTEXT1_CONDITION, null) };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted Preset with missing Context override value.",
                TestUtil.assertPSValidation(pset, IPresetPSContext.class, null, IssueType.OVERRIDE_VALUE_MISSING));
    }
    
    
    @Test
    public void testPresetContextEmptyCondition() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] { new ContextBean(CONTEXT1_TYPE, "", CONTEXT1_OVERRIDE) };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        
        assertEquals(1, pset.getPresets().size());
        IPreset p = pset.getPresets().get(0);
        
        assertEquals(1, p.getAssignedPrivacySettings().size());
        IPresetAssignedPrivacySetting aps = p.getAssignedPrivacySettings().get(0);
        
        assertEquals(1, aps.getContexts().size());
        IPresetPSContext c = aps.getContexts().get(0);
        
        assertEquals("", c.getCondition());
        
        assertTrue("Validator did not accept Preset with empty Context condition.",
                TestUtil.assertPresetValidationEmpty(pset));
    }
    
    
    @Test
    public void testPresetContextMissingCondition() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] { new ContextBean(CONTEXT1_TYPE, null, CONTEXT1_OVERRIDE) };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet pset = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        assertTrue("Validator accepted Preset with missing Context condition.",
                TestUtil.assertPSValidation(pset, IPresetPSContext.class, null, IssueType.CONDITION_MISSING));
    }
    
    
    @Test
    public void testCleanPresetIssues() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] { new ContextBean("", CONTEXT1_CONDITION, CONTEXT1_OVERRIDE) };
        
        XMLNode preset = TestUtil.makePreset("", PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, "");
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, "", APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet ps = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        TestUtil.assertNoIssues(ps);
        assertTrue("No issues in clean Preset issues test.", XMLUtilityProxy.getPresetUtil().getValidator()
                .validatePresetSet(ps, true).size() > 0);
        XMLUtilityProxy.getPresetUtil().getValidator().clearIssuesAndPropagate(ps);
        TestUtil.assertNoIssues(ps);
    }
    
    
    @Test
    public void testPresetCompiler() throws Exception {
        TestUtil.makePresetSet();
        
        ContextBean[] contexts = new ContextBean[] {
                new ContextBean(CONTEXT1_TYPE, CONTEXT1_CONDITION, CONTEXT1_OVERRIDE),
                new ContextBean(CONTEXT2_TYPE, CONTEXT2_CONDITION, CONTEXT2_OVERRIDE) };
        
        XMLNode preset = TestUtil.makePreset(PRESET1_IDENTIFIER, PRESET1_CREATOR, PRESET1_NAME, PRESET1_DESCRIPTION);
        TestUtil.addAssignedApps(preset, PRESET1_APP1_IDENTIFIER);
        TestUtil.addAssignedPrivacySetting(preset, RG_ID, RG_REVISION, RG_PS1_ID, APP_SF1_REQ_PS1_VALUE, contexts,
                EmptyValueHandling.CORRECT);
        TestUtil.main.addChild(preset);
        
        StackTraceElement ste = Thread.currentThread().getStackTrace()[1];
        TestUtil.debug(ste.getMethodName());
        
        IPresetSet ps = XMLUtilityProxy.getPresetUtil().parse(XMLCompiler.compileStream(TestUtil.main));
        XMLUtilityProxy.getPresetUtil().getValidator().validatePresetSet(ps, true);
        TestUtil.assertNoIssues(ps);
        String compilation = TestUtil.inputStreamToString(XMLUtilityProxy.getPresetUtil().compile(ps));
        
        assertTrue(compilation.contains(PRESET1_IDENTIFIER));
        assertTrue(compilation.contains(PRESET1_CREATOR));
        assertTrue(compilation.contains(PRESET1_NAME));
        assertTrue(compilation.contains(PRESET1_DESCRIPTION));
        assertTrue(compilation.contains(PRESET1_APP1_IDENTIFIER));
        assertTrue(compilation.contains(RG_ID));
        assertTrue(compilation.contains(RG_REVISION)
                || compilation.contains(XMLConstants.REVISION_DATE_FORMAT.format(RG_REVISION_DATE)));
        assertTrue(compilation.contains(RG_PS1_ID));
        assertTrue(compilation.contains(APP_SF1_REQ_PS1_VALUE));
        assertTrue(compilation.contains(CONTEXT1_TYPE));
        assertTrue(compilation.contains(CONTEXT1_CONDITION));
        assertTrue(compilation.contains(CONTEXT1_OVERRIDE));
        assertTrue(compilation.contains(CONTEXT2_TYPE));
        assertTrue(compilation.contains(CONTEXT2_CONDITION));
        assertTrue(compilation.contains(CONTEXT2_OVERRIDE));
    }
    
}
