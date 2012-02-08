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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.AppUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.PresetUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.Printer;
import de.unistuttgart.ipvs.pmp.xmlutil.RGUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.issue.AISIssue;

public class TestValidXMLFile {
    
    private static String aisURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/AIS.xml";
    private static String rgisURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/RGIS.xml";
    private static String presetSetURL = "http://pmp-android.googlecode.com/git-history/trunk/documentation/pmp/design/XML/presetSet.xml";
    
    private static String aisDefectURL = "http://mvvt.de/ais_defect.xml";
    
    
    /**
     * Test method
     * 
     * @param args
     */
    public static void main(String[] args) {
        AppUtil appUtil = XMLUtilityProxy.getAppUtil();
        RGUtil rgUtil = XMLUtilityProxy.getRGUtil();
        PresetUtil presetUtil = XMLUtilityProxy.getPresetUtil();
        
        try {
            // Test AIS
            AIS ais = appUtil.parse(new URL(aisURL).openStream());
            appUtil.print(ais);
            ais = appUtil.parse(appUtil.compile(ais));
            appUtil.print(ais);
            
            // Test RGIS
            RGIS rgis = rgUtil.parse(new URL(rgisURL).openStream());
            rgUtil.print(rgis);
            rgis = rgUtil.parse(rgUtil.compile(rgis));
            rgUtil.print(rgis);
            
            // Test PresetSet
            PresetSet presetSet = presetUtil.parse(new URL(presetSetURL).openStream());
            presetUtil.print(presetSet);
            presetSet = presetUtil.parse(presetUtil.compile(presetSet));
            presetUtil.print(presetSet);
            
            // Test empty AIS compilation
            File f = new File("C:/users/marcus/desktop/newais.xml");
            
            InputStream is = appUtil.compile(new AIS());
            
            OutputStream out = new FileOutputStream(f);
            byte buf[] = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0)
                out.write(buf, 0, len);
            out.close();
            is.close();
            
            // Test defect AIS
            AIS defectAIS = appUtil.parse(new URL(aisDefectURL).openStream());
            List<AISIssue> aisIssues = AISValidator.validateAIS(defectAIS);
            Printer.printAISIssues(aisIssues);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}
