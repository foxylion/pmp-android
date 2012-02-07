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
package de.unistuttgart.ipvs.pmp.xmlutil;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.Preset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * This Class provides static methods to print a given AIS or RGIS.
 * 
 * @author Marcus Vetter
 * 
 */
public class ISPrinter {
    
    /**
     * Print the app information set to the console
     * 
     * @param ais
     *            the app information set for printing
     */
    public static void printAIS(AIS ais) {
        System.out.println("-----------------------");
        System.out.println("----- XML-Parser ------");
        System.out.println("-----------------------");
        System.out.println("--------- AIS ---------");
        System.out.println("-----------------------");
        for (Name name : ais.getNames()) {
            System.out.println("Name: " + name.getName() + " (Locale: " + name.getLocale().getLanguage() + ")");
        }
        for (Description descr : ais.getDescriptions()) {
            System.out.println("Description: " + descr.getDescription() + " (Locale: "
                    + descr.getLocale().getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Service Features: --");
        System.out.println("-----------------------");
        for (AISServiceFeature sf : ais.getServiceFeatures()) {
            System.out.println("Identifier: " + sf.getIdentifier());
            for (Name name : sf.getNames()) {
                System.out.println("Name: " + name.getName() + " (Locale: " + name.getLocale().getLanguage() + ")");
            }
            for (Description descr : sf.getDescriptions()) {
                System.out.println("Description: " + descr.getDescription() + " (Locale: "
                        + descr.getLocale().getLanguage() + ")");
            }
            for (AISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                System.out.println("Required Resource Group (Identifier: " + rrg.getIdentifier() + ", minRevision: "
                        + rrg.getMinRevision() + ")");
                for (AISRequiredPrivacySetting ps : rrg.getRequiredPrivacySettings()) {
                    System.out.println("- Privacy Setting (Identifier: " + ps.getIdentifier() + ", Value: "
                            + ps.getValue() + ")");
                }
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }
    
    
    /**
     * Print the rgis set to the console
     * 
     * @param rgis
     *            the rg information set for printing
     */
    public static void printRGIS(RGIS rgis) {
        System.out.println("-----------------------");
        System.out.println("----- XML-Parser ------");
        System.out.println("-----------------------");
        System.out.println("-------- RGIS ---------");
        System.out.println("-----------------------");
        System.out.println("Identifier: " + rgis.getIdentifier());
        System.out.println("IconLocation: " + rgis.getIconLocation());
        System.out.println("Class Name: " + rgis.getClassName());
        for (Name name : rgis.getNames()) {
            System.out.println("Name: " + name.getName() + " (Locale: " + name.getLocale().getLanguage() + ")");
        }
        for (Description descr : rgis.getDescriptions()) {
            System.out.println("Description: " + descr.getDescription() + " (Locale: "
                    + descr.getLocale().getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Privacy Settings: --");
        System.out.println("-----------------------");
        for (RGISPrivacySetting ps : rgis.getPrivacySettings()) {
            System.out.println("Identifier: " + ps.getIdentifier());
            System.out.println("Valid value description: " + ps.getValidValueDescription());
            for (Name name : ps.getNames()) {
                System.out.println("Name: " + name.getName() + " (Locale: " + name.getLocale().getLanguage() + ")");
            }
            for (Description descr : ps.getDescriptions()) {
                System.out.println("Description: " + descr.getDescription() + " (Locale: "
                        + descr.getLocale().getLanguage() + ")");
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }
    
    
    /**
     * Print the presetSet set to the console
     * 
     * @param presetSet
     *            the presetSet set for printing
     */
    public static void printPresetSet(PresetSet presetSet) {
        System.out.println("-----------------------");
        System.out.println("----- XML-Parser ------");
        System.out.println("-----------------------");
        System.out.println("------ PresetSet ------");
        System.out.println("-----------------------");
        for (Preset preset : presetSet.getPresets()) {
            System.out.println("Identifier: " + preset.getIdentifier());
            System.out.println("Creator: " + preset.getCreator());
            System.out.println("Name: " + preset.getName());
            System.out.println("Description: " + preset.getDescription());
            System.out.println("Assigned Apps:");
            for (PresetAssignedApp app : preset.getAssignedApps()) {
                System.out.println("- Identifier: " + app.getIdentifier());
            }
            System.out.println("Assigned Privacy Settings:");
            for (PresetAssignedPrivacySetting ps : preset.getAssignedPrivacySettings()) {
                System.out.println("RG-Identifier: " + ps.getRgIdentifier());
                System.out.println("RG-Revision: " + ps.getRgRevision());
                System.out.println("PS-Identifier: " + ps.getPsIdentifier());
                System.out.println("Value: " + ps.getValue());
                for (PresetPSContext context : ps.getContexts()) {
                    System.out.println("- Context");
                    System.out.println("- Type: " + context.getType());
                    System.out.println("- Condition: " + context.getCondition());
                    System.out.println("- Override-Value: " + context.getOverrideValue());
                    System.out.println("----");
                }
                System.out.println("----");
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }
    
}
