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
package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.InputStream;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.Log;

/**
 * 
 * This Class provides the functionality to parse a given xml file (information
 * set of the resource group) and return a complete RgInformationSet.
 * 
 * @author Marcus Vetter
 * 
 */
public class RgInformationSetParser {
    
    /**
     * This method creates an resourcegroup information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return resourcegroup information set
     */
    public static RgInformationSet createRgInformationSet(InputStream xmlStream) {
        return new XMLParser(xmlStream).parse();
    }
    
    
    /**
     * Print the rg information set to the log
     * 
     * @param rgis
     *            the rg information set for printing
     */
    public static void printRgInformationSet(RgInformationSet rgis) {
        Log.d("-----------------------");
        Log.d("----- XML-Parser ------");
        Log.d("-----------------------");
        Log.d("--- Rg-Information: ---");
        Log.d("-----------------------");
        for (Locale l : rgis.getNames().keySet()) {
            Log.d("Name: " + rgis.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        for (Locale l : rgis.getDescriptions().keySet()) {
            Log.d("Description: " + rgis.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        Log.d("-----------------------");
        Log.d("-- Privacy Settings: --");
        Log.d("-----------------------");
        for (String rgIdentifier : rgis.getPrivacySettingsMap().keySet()) {
            PrivacySetting ps = rgis.getPrivacySettingsMap().get(rgIdentifier);
            Log.d("Identifier: " + rgIdentifier);
            for (Locale l : ps.getNames().keySet()) {
                Log.d("Name: " + ps.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (Locale l : ps.getDescriptions().keySet()) {
                Log.d("Description: " + ps.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            Log.d("-----------------------");
        }
        Log.d("-----------------------");
    }
}
