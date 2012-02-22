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
package example;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.AppUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.PresetUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.Printer;
import de.unistuttgart.ipvs.pmp.xmlutil.RGUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.PresetSetValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.RGISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.IIssue;

public class TestXMLFiles {
    
    private static String aisURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/AIS.xml";
    private static String rgisURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/RGIS.xml";
    private static String presetSetURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/presetSet.xml";
    
    private static String presetSetDefectURL = "http://mvvt.de/presetset_defect.xml";
    private static String aisDefectURL = "http://mvvt.de/ais_defect.xml";
    private static String rgisDefectURL = "http://mvvt.de/rgis_defect.xml";
    
    
    /**
     * Test method
     * 
     * @param args
     */
    public static void main(String[] args) {
        AppUtil appUtil = XMLUtilityProxy.getAppUtil();
        RGUtil rgUtil = XMLUtilityProxy.getRGUtil();
        PresetUtil presetUtil = XMLUtilityProxy.getPresetUtil();
        
        AISValidator aisValidator = appUtil.getValidator();
        RGISValidator rgisValidator = rgUtil.getValidator();
        PresetSetValidator presetSetValidator = presetUtil.getValidator();
        
        try {
            // Test AIS
            IAIS ais = appUtil.parse(new URL(aisURL).openStream());
            appUtil.print(ais);
            InputStream compiledXML = appUtil.compile(ais);
            ais = appUtil.parse(compiledXML);
            appUtil.print(ais);
            aisValidator.validateAIS(ais, true);
            appUtil.print(ais);
            
            // Test RGIS
            IRGIS rgis = rgUtil.parse(new URL(rgisURL).openStream());
            rgUtil.print(rgis);
            rgis = rgUtil.parse(rgUtil.compile(rgis));
            rgUtil.print(rgis);
            
            // Test PresetSet
            IPresetSet presetSet = presetUtil.parse(new URL(presetSetURL).openStream());
            presetUtil.print(presetSet);
            presetSet = presetUtil.parse(presetUtil.compile(presetSet));
            presetUtil.print(presetSet);
            
            // Test defect AIS
            IAIS defectAIS = appUtil.parse(new URL(aisDefectURL).openStream());
            List<IIssue> aisIssues = aisValidator.validateAIS(defectAIS, true);
            Printer.printIssues(aisIssues);
            Printer.printAIS(defectAIS);
            
            // Test defect RGIS
            IRGIS defectRGIS = rgUtil.parse(new URL(rgisDefectURL).openStream());
            List<IIssue> rgisIssues = rgisValidator.validateRGIS(defectRGIS, true);
            Printer.printRGIS(defectRGIS);
            rgisIssues = rgisValidator.validateRGIS(defectRGIS, true);
            Printer.printIssues(rgisIssues);
            Printer.printRGIS(defectRGIS);
            
            // Test defect PresetSet
            IPresetSet defectPresetSet = presetUtil.parse(new URL(presetSetDefectURL).openStream());
            List<IIssue> presetSetIssues = presetSetValidator.validatePresetSet(defectPresetSet, true);
            Printer.printPresetSet(defectPresetSet);
            presetSetIssues = presetSetValidator.validatePresetSet(defectPresetSet, true);
            Printer.printIssues(presetSetIssues);
            Printer.printPresetSet(defectPresetSet);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}
