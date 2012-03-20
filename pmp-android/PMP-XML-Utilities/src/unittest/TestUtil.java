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

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import junit.framework.Assert;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.IIdentifierIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPreset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssueLocation;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IssueType;

public class TestUtil implements TestConstants {
    
    static class ContextBean {
        
        private String type, condition, overrideValue;
        
        
        public String getType() {
            return this.type;
        }
        
        
        public String getCondition() {
            return this.condition;
        }
        
        
        public String getOverrideValue() {
            return this.overrideValue;
        }
        
        
        public ContextBean(String type, String condition, String overrideValue) {
            this.type = type;
            this.condition = condition;
            this.overrideValue = overrideValue;
        }
        
    }
    
    static enum EmptyValueHandling {
        CORRECT,
        ALWAYS_FALSE,
        ALWAYS_TRUE,
        OMIT;
        
        public void applyToXMLNode(XMLNode node, String attribName) {
            switch (this) {
                case CORRECT:
                    Boolean empty = (node.getContent() == null) || (node.getContent().isEmpty());
                    node.addAttribute(new XMLAttribute(attribName, empty.toString()));
                    break;
                case ALWAYS_FALSE:
                    node.addAttribute(new XMLAttribute(attribName, Boolean.FALSE.toString()));
                    break;
                case ALWAYS_TRUE:
                    node.addAttribute(new XMLAttribute(attribName, Boolean.TRUE.toString()));
                    break;
                case OMIT:
                    // do nothing
                    break;
            }
        }
    }
    
    private static final boolean debugInput = true;
    
    /*
    * statics used to access results of creation methods
    */
    
    protected static XMLNode main;
    
    protected static XMLNode app, appDefName, appDefDesc, sfs;
    
    protected static XMLNode rg, rgRev, rgDefName, rgDefDesc, pss;
    
    protected static XMLAttribute rgIcon, rgClass;
    
    protected static XMLNode ps;
    
    
    /*
    * automatic creation methods
    */
    
    protected static void makeApp(String name, String desc) {
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
    
    
    protected static void makeRG(String id, String icon, String className, String revision, String name, String desc) {
        main = new XMLNode(XML_RESOURCE_GROUP_INFORMATION_SET);
        
        rg = new XMLNode(XML_RESOURCE_GROUP_INFORMATION);
        rg.addAttribute(new XMLAttribute(XML_IDENTIFIER, id));
        rgIcon = new XMLAttribute(XML_ICON, icon);
        rg.addAttribute(rgIcon);
        rgClass = new XMLAttribute(XML_CLASS_NAME, className);
        rg.addAttribute(rgClass);
        
        rgRev = new XMLNode(XML_REVISION);
        rgRev.setContent(revision);
        
        rgDefName = new XMLNode(XML_DEFAULT_NAME);
        rgDefName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
        rgDefName.setContent(name);
        rgDefDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
        rgDefDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
        rgDefDesc.setContent(desc);
        
        rg.addChild(rgRev);
        rg.addChild(rgDefName);
        rg.addChild(rgDefDesc);
        
        pss = new XMLNode(XML_PRIVACY_SETTINGS);
        
        main.addChild(rg);
        main.addChild(pss);
    }
    
    
    protected static void makePresetSet() {
        main = new XMLNode(XML_PRESET_SET);
    }
    
    
    protected static XMLNode makeSF(String id, String name, String desc) {
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
    
    
    protected static XMLNode makePreset(String identifier, String creator, String name, String desc) {
        XMLNode preset = new XMLNode(XML_PRESET);
        
        preset.addAttribute(new XMLAttribute(XML_IDENTIFIER, identifier));
        preset.addAttribute(new XMLAttribute(XML_CREATOR, creator));
        preset.addAttribute(new XMLAttribute(XML_NAME, name));
        if (desc != null) {
            preset.addAttribute(new XMLAttribute(XML_DESCRIPTION, desc));
        }
        
        return preset;
    }
    
    
    protected static void addAssignedApps(XMLNode at, String... appIdentifiers) {
        XMLNode aA = new XMLNode(XML_ASSIGNED_APPS);
        
        for (String appIdentifier : appIdentifiers) {
            XMLNode app = new XMLNode(XML_APP);
            if (appIdentifier != null) {
                app.addAttribute(new XMLAttribute(XML_IDENTIFIER, appIdentifier));
            }
            aA.addChild(app);
        }
        
        at.addChild(aA);
    }
    
    
    protected static void addAssignedPrivacySetting(XMLNode atPreset, String rgId, String rgRev, String psId,
            String value, ContextBean[] contexts, EmptyValueHandling emptyValueHandling) {
        // find assingPS node
        XMLNode aPS = null;
        for (XMLNode child : atPreset.getChildren()) {
            if (child.getName().equals(XML_ASSIGNED_PRIVACY_SETTINGS)) {
                aPS = child;
                break;
            }
        }
        if (aPS == null) {
            aPS = new XMLNode(XML_ASSIGNED_PRIVACY_SETTINGS);
            atPreset.addChild(aPS);
        }
        
        XMLNode ps = new XMLNode(XML_PRIVACY_SETTING);
        ps.addAttribute(new XMLAttribute(XML_RG_IDENTIFIER, rgId));
        ps.addAttribute(new XMLAttribute(XML_RG_REVISION, rgRev));
        ps.addAttribute(new XMLAttribute(XML_PS_IDENTIFIER, psId));
        
        if (value != null) {
            XMLNode valu = new XMLNode(XML_VALUE);
            emptyValueHandling.applyToXMLNode(valu, XML_EMPTY_VALUE);
            valu.setCDATAContent(value);
            ps.addChild(valu);
        }
        
        for (ContextBean context : contexts) {
            XMLNode ctx = new XMLNode(XML_CONTEXT);
            ctx.addAttribute(new XMLAttribute(XML_TYPE, context.getType()));
            if (context.getCondition() != null) {
                ctx.addAttribute(new XMLAttribute(XML_CONDITION, context.getCondition()));
                emptyValueHandling.applyToXMLNode(ctx, XML_EMPTY_CONDITION);
            }
            
            if (context.getOverrideValue() != null) {
                XMLNode ovrVal = new XMLNode(XML_OVERRIDE_VALUE);
                ovrVal.setCDATAContent(context.getOverrideValue());
                emptyValueHandling.applyToXMLNode(ovrVal, XML_EMPTY_OVERRIDE_VALUE);
                ctx.addChild(ovrVal);
            }
            
            ps.addChild(ctx);
        }
        
        aPS.addChild(ps);
    }
    
    
    protected static void addRequiredRG(XMLNode sf, String rgId, String[] psId, String[] psValue, String minRevision,
            EmptyValueHandling emptyValueHandling) {
        XMLNode reqRG = new XMLNode(XML_REQUIRED_RESOURCE_GROUP);
        reqRG.addAttribute(new XMLAttribute(XML_IDENTIFIER, rgId));
        reqRG.addAttribute(new XMLAttribute(XML_REQUIRED_RESOURCE_GROUP_REVISION, minRevision));
        
        // for each argument
        for (int i = 0; i < Math.min(psId.length, psValue.length); i++) {
            if (psId[i] == null) {
                continue;
            }
            
            // add reqPS
            XMLNode reqPS = new XMLNode(XML_REQUIRED_PRIVACY_SETTING);
            reqPS.addAttribute(new XMLAttribute(XML_IDENTIFIER, psId[i]));
            if (psValue[i] != null) {
                reqPS.setCDATAContent(psValue[i]);
            }
            // empty value sh*t
            emptyValueHandling.applyToXMLNode(reqPS, XML_EMPTY_VALUE);
            reqRG.addChild(reqPS);
        }
        
        sf.addChild(reqRG);
    }
    
    
    protected static XMLNode makePS(String id, String name, String desc, String changeDesc, String validValueDesc) {
        XMLNode ps = new XMLNode(XML_PRIVACY_SETTING);
        ps.addAttribute(new XMLAttribute(XML_IDENTIFIER, id));
        ps.addAttribute(new XMLAttribute(XML_VALID_VALUE_DESCRIPTION, validValueDesc));
        
        XMLNode defName = new XMLNode(XML_DEFAULT_NAME);
        defName.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
        defName.setContent(name);
        ps.addChild(defName);
        
        XMLNode defDesc = new XMLNode(XML_DEFAULT_DESCRIPTION);
        defDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
        defDesc.setContent(desc);
        ps.addChild(defDesc);
        
        XMLNode chgDesc = new XMLNode(XML_CHANGE_DESCRIPTION);
        chgDesc.addAttribute(new XMLAttribute(XML_LANG, XML_DEFAULT_EN));
        chgDesc.setContent(changeDesc);
        ps.addChild(chgDesc);
        
        return ps;
    }
    
    
    protected static void addLocale(XMLNode at, String locale, String name, String desc) {
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
    
    
    protected static void setFlags(XMLNode node, int flag) {
        node.setFlags(node.getFlags() | flag);
        for (XMLNode child : node.getChildren()) {
            setFlags(child, flag);
        }
    }
    
    
    protected static boolean assertAISValidation(IAIS ais, Class<? extends IIssueLocation> atClass,
            String atIdentifier, IssueType type) {
        List<IIssue> result = XMLUtilityProxy.getAppUtil().getValidator().validateAIS(ais, true);
        
        for (IIssue i : result) {
            String debug = "Issue (" + i.getType().toString() + ") at " + i.getLocation().getClass().getSimpleName();
            if (i.getLocation() instanceof IIdentifierIS) {
                debug += " with identifier '" + ((IIdentifierIS) i.getLocation()).getIdentifier() + "'";
            } else if (atIdentifier != null) {
                debug += " AND CLASS IS NOT IMPLEMENTING IIdentifierIS!";
            }
            System.out.println(debug);
            
            // the class and...
            if (atClass.isAssignableFrom(i.getLocation().getClass())) {
                // either the type suffices
                if ((atIdentifier == null) && type.equals(i.getType())) {
                    return true;
                    
                } else if (i.getLocation() instanceof IIdentifierIS) {
                    IIdentifierIS iiis = (IIdentifierIS) (i.getLocation());
                    
                    // or we want it for that specific identifier
                    if (iiis.getIdentifier().equals(atIdentifier) && i.getType().equals(type)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    
    protected static boolean assertAISValidationEmpty(IAIS ais) {
        List<IIssue> result = XMLUtilityProxy.getAppUtil().getValidator().validateAIS(ais, false);
        for (IIssue i : result) {
            System.out.println("Unexpected issue: ");
            System.out.println(issueToString(i));
        }
        return result.size() == 0;
    }
    
    
    protected static boolean assertRGISValidation(IRGIS rgis, Class<? extends IIssueLocation> atClass,
            String atIdentifier, IssueType type) {
        List<IIssue> result = XMLUtilityProxy.getRGUtil().getValidator().validateRGIS(rgis, true);
        
        for (IIssue i : result) {
            String debug = "Issue (" + i.getType().toString() + ") at " + i.getLocation().getClass().getSimpleName();
            if (i.getLocation() instanceof IIdentifierIS) {
                debug += " with identifier '" + ((IIdentifierIS) i.getLocation()).getIdentifier() + "'";
            } else if (atIdentifier != null) {
                debug += " AND CLASS IS NOT IMPLEMENTING IIdentifierIS!";
            }
            System.out.println(debug);
            
            // the class and...
            if (atClass.isAssignableFrom(i.getLocation().getClass())) {
                // either the type suffices
                if ((atIdentifier == null) && type.equals(i.getType())) {
                    return true;
                    
                } else if (i.getLocation() instanceof IIdentifierIS) {
                    IIdentifierIS iiis = (IIdentifierIS) (i.getLocation());
                    
                    // or we want it for that specific identifier
                    if (iiis.getIdentifier().equals(atIdentifier) && i.getType().equals(type)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    
    protected static boolean assertRGISValidationEmpty(IRGIS rgis) {
        List<IIssue> result = XMLUtilityProxy.getRGUtil().getValidator().validateRGIS(rgis, false);
        for (IIssue i : result) {
            System.out.println("Unexpected issue: ");
            System.out.println(issueToString(i));
        }
        return result.size() == 0;
    }
    
    
    protected static boolean assertPSValidation(IPresetSet pset, Class<? extends IIssueLocation> atClass,
            String atIdentifier, IssueType type) {
        List<IIssue> result = XMLUtilityProxy.getPresetUtil().getValidator().validatePresetSet(pset, true);
        
        for (IIssue i : result) {
            String debug = "Issue (" + i.getType().toString() + ") at " + i.getLocation().getClass().getSimpleName();
            if (i.getLocation() instanceof IIdentifierIS) {
                debug += " with identifier '" + ((IIdentifierIS) i.getLocation()).getIdentifier() + "'";
            } else if (atIdentifier != null) {
                debug += " AND CLASS IS NOT IMPLEMENTING IIdentifierIS!";
            }
            System.out.println(debug);
            
            // the class and...
            if (atClass.isAssignableFrom(i.getLocation().getClass())) {
                // either the type suffices
                if ((atIdentifier == null) && type.equals(i.getType())) {
                    return true;
                    
                } else if (i.getLocation() instanceof IIdentifierIS) {
                    IIdentifierIS iiis = (IIdentifierIS) (i.getLocation());
                    
                    // or we want it for that specific identifier
                    if (iiis.getIdentifier().equals(atIdentifier) && i.getType().equals(type)) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    
    protected static boolean assertPresetValidationEmpty(IPresetSet pset) {
        List<IIssue> result = XMLUtilityProxy.getPresetUtil().getValidator().validatePresetSet(pset, false);
        for (IIssue i : result) {
            System.out.println("Unexpected issue: ");
            System.out.println(issueToString(i));
        }
        return result.size() == 0;
    }
    
    
    protected static void debug(String name) {
        if (debugInput) {
            System.out.println("################################################################");
            System.out.println("Input for " + name + " was:");
            System.out.println(XMLCompiler.compile(main));
            System.out.println("----------------------------------------------------------------");
        }
    }
    
    
    protected static String issueToString(IIssue i) {
        StringBuilder sb = new StringBuilder();
        sb.append(i.getType().toString());
        sb.append(" (");
        boolean first = false;
        for (String p : i.getParameters()) {
            if (!first) {
                sb.append(", ");
            }
            sb.append(p);
            first = true;
        }
        sb.append(") @ ");
        sb.append(i.getLocation().getClass().getSimpleName());
        return sb.toString();
    }
    
    
    public static void assertNoIssues(IAIS ais) {
        Assert.assertEquals(0, ais.getIssues().size());
        
        // App.name, desc
        for (ILocalizedString ls : ais.getDescriptions()) {
            Assert.assertEquals(0, ls.getIssues().size());
        }
        for (ILocalizedString ls : ais.getNames()) {
            Assert.assertEquals(0, ls.getIssues().size());
        }
        
        // SFs
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
            Assert.assertEquals(0, sf.getIssues().size());
            
            // SF.name, desc
            for (ILocalizedString ls : sf.getDescriptions()) {
                Assert.assertEquals(0, ls.getIssues().size());
            }
            for (ILocalizedString ls : sf.getNames()) {
                Assert.assertEquals(0, ls.getIssues().size());
            }
            
            // ReqRGs
            for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                Assert.assertEquals(0, rrg.getIssues().size());
                
                // ReqPSs
                for (IAISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
                    Assert.assertEquals(0, rps.getIssues().size());
                }
            }
        }
    }
    
    
    public static void assertNoIssues(IRGIS rgis) {
        Assert.assertEquals(0, rgis.getIssues().size());
        
        // App.name, desc
        for (ILocalizedString ls : rgis.getDescriptions()) {
            Assert.assertEquals(0, ls.getIssues().size());
        }
        for (ILocalizedString ls : rgis.getNames()) {
            Assert.assertEquals(0, ls.getIssues().size());
        }
        
        // SFs
        for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
            Assert.assertEquals(0, ps.getIssues().size());
            
            // PS.name, desc, chngdesc
            for (ILocalizedString ls : ps.getDescriptions()) {
                Assert.assertEquals(0, ls.getIssues().size());
            }
            for (ILocalizedString ls : ps.getNames()) {
                Assert.assertEquals(0, ls.getIssues().size());
            }
            for (ILocalizedString ls : ps.getChangeDescriptions()) {
                Assert.assertEquals(0, ls.getIssues().size());
            }
        }
    }
    
    
    public static void assertNoIssues(IPresetSet ps) {
        Assert.assertEquals(0, ps.getIssues().size());
        
        for (IPreset p : ps.getPresets()) {
            Assert.assertEquals(0, p.getIssues().size());
            
            for (IPresetAssignedApp aa : p.getAssignedApps()) {
                Assert.assertEquals(0, aa.getIssues().size());
            }
            
            for (IPresetAssignedPrivacySetting psps : p.getAssignedPrivacySettings()) {
                Assert.assertEquals(0, psps.getIssues().size());
                
                for (IPresetPSContext c : psps.getContexts()) {
                    Assert.assertEquals(0, c.getIssues().size());
                }
            }
        }
        
    }
    
    
    public static String inputStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNextLine()) {
                sb.append(sc.nextLine());
                sb.append(System.getProperty("line.separator"));
            }
        } finally {
            sc.close();
        }
        return sb.toString();
    }
    
}
