/*
 * Copyright 2012 pmp-android development team
 * Project: JPMPPS
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
package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.File;

/**
 * Main Class.
 * 
 * @author Jakob Jarosch
 */
public class Main {
    
    /**
     * Starts a new instance of the JPMPPS.
     * 
     * @param args
     *            Requires one argument, the path to the folder with all
     *            ResourceGroups.
     */
    public static void main(String[] args) {
        // Check argument length
        if (args.length != 2) {
            System.out
                    .println("[E] Sorry, needs two arguments. \"jpmpps.jar <pathPackages> <pathToPresetSetDatastore>\". [-> Exit]");
            System.exit(1);
        }
        
        // Check if package folder exists
        if (!new File(args[0]).isDirectory()) {
            System.out.println("[E] Path to packages needs to reference a valid directory. [-> Exit]");
            System.exit(2);
        }
        
        // Check if preset set data store folder exists
        if (!new File(args[1]).isDirectory()) {
            System.out.println("[E] Path to preset set datastore needs to reference a valid directory. [-> Exit]");
            System.exit(2);
        }
        
        // Start the server
        JPMPPS.get(new File(args[0]), new File(args[1])).initialize();
        JPMPPS.get().startServer();
    }
}
