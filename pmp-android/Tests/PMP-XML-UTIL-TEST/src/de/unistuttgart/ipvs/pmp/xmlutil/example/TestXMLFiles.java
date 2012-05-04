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
package de.unistuttgart.ipvs.pmp.xmlutil.example;

import java.io.IOException;
import java.net.URL;

import de.unistuttgart.ipvs.pmp.xmlutil.AppUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.PresetUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.RGUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;

public class TestXMLFiles {
    
    private static String presetSet2URL = "http://mvvt.de/presetset2.xml";
    
    
    /**
     * Test method
     * 
     * @param args
     */
    public static void main(String[] args) {
        AppUtil appUtil = XMLUtilityProxy.getAppUtil();
        RGUtil rgUtil = XMLUtilityProxy.getRGUtil();
        PresetUtil presetUtil = XMLUtilityProxy.getPresetUtil();
        
        appUtil.getValidator();
        rgUtil.getValidator();
        presetUtil.getValidator();
        
        try {
            //            // Test AIS
            //            IAIS ais = appUtil.parse(new URL(ais3URL).openStream());
            //            appUtil.print(ais);
            //            appUtil.print(appUtil.parse(appUtil.compile(ais)));
            //            aisValidator.validateAIS(ais, true);
            //            appUtil.print(ais);
            //            Printer.printIssues(ais.getIssues());
            //            
            //                        // Test RGIS
            //                        IRGIS rgis = rgUtil.parse(new URL(rgisURL).openStream());
            //                        rgUtil.print(rgis);
            //                        rgis = rgUtil.parse(rgUtil.compile(rgis));
            //                        rgUtil.print(rgis);
            
            // Test PresetSet
            IPresetSet presetSet = presetUtil.parse(new URL(presetSet2URL).openStream());
            presetUtil.print(presetSet);
            presetSet = presetUtil.parse(presetUtil.compile(presetSet));
            presetUtil.print(presetSet);
            
            // Test defect AIS
            //                        IAIS defectAIS = appUtil.parse(new URL(aisDefectURL).openStream());
            //                        List<IIssue> aisIssues = aisValidator.validateAIS(defectAIS, true);
            //                        Printer.printAIS(defectAIS);
            //                        Printer.printIssues(aisIssues);
            //                        
            //                        // Test defect RGIS
            //                        IRGIS defectRGIS = rgUtil.parse(new URL(rgisDefectURL).openStream());
            //                        List<IIssue> rgisIssues = rgisValidator.validateRGIS(defectRGIS, true);
            //                        Printer.printRGIS(defectRGIS);
            //                        rgisIssues = rgisValidator.validateRGIS(defectRGIS, true);
            //                        Printer.printIssues(rgisIssues);
            //                        Printer.printRGIS(defectRGIS);
            //                        
            //                        // Test defect PresetSet
            //                        IPresetSet defectPresetSet = presetUtil.parse(new URL(presetSetDefectURL).openStream());
            //                        List<IIssue> presetSetIssues = presetSetValidator.validatePresetSet(defectPresetSet, true);
            //                        Printer.printPresetSet(defectPresetSet);
            //                        presetSetIssues = presetSetValidator.validatePresetSet(defectPresetSet, true);
            //                        Printer.printIssues(presetSetIssues);
            //                        Printer.printPresetSet(defectPresetSet);
            //            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
