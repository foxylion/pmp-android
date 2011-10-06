/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * This activity creates a app information set with the given xml file. This is just an example.
 * 
 * @author Marcus Vetter
 */
public class AppInformationSetCreatingExample extends Activity {
    
    /**
     * Called, when activity is started.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        URL xmlURL = null;
        
        try {
            // The URL of the XML file
            xmlURL = new URL("http://marcus.mvvt.de/AppExample.xml");
            
            // Create the app information set
            AppInformationSet ais = null;
            try {
                ais = AppInformationSetParser.createAppInformationSet(xmlURL.openStream());
                
                // Print the app information set
                AppInformationSetParser.printAppInformationSet(ais);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        } catch (MalformedURLException e) {
            Log.e(e.getMessage());
        }
        
    }
    
}
