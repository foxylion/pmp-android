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

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.AISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * This proxy provides XML utilities for Apps, Resourcegroups and Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLUtilityProxy {
    
    /**
     * The AISParser and RGISParser
     */
    private static AISParser aisParser = new AISParser();
    private static RGISParser rgisParser = new RGISParser();
    
    
    /**
     * Protect the constructor. Use static methods.
     */
    private XMLUtilityProxy() {
    }
    
    
    /**
     * This method creates an app information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return app information set
     */
    public static AIS parseAISXML(InputStream xmlStream) {
        long a = System.currentTimeMillis();
        AIS ais = aisParser.parse(xmlStream);
        System.out.println("Parsing AIS took " + Long.toString(System.currentTimeMillis() - a) + "ms.");
        return ais;
    }
    
    
    /**
     * Create a blank AIS-Object
     * 
     * @return blank AIS-Object
     */
    public static AIS createBlankAIS() {
        return new AIS();
    }
    
    
    /**
     * Print an AIS
     * 
     * @param ais
     *            AIS to print
     */
    public static void printAIS(AIS ais) {
        ISPrinter.printAIS(ais);
    }
    
    
    /**
     * This method creates an resourcegroup information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return resourcegroup information set
     */
    public static RGIS parseRGISXML(InputStream xmlStream) {
        long a = System.currentTimeMillis();
        RGIS rgis = rgisParser.parse(xmlStream);
        System.out.println("Parsing RGIS took " + Long.toString(System.currentTimeMillis() - a) + "ms.");
        return rgis;
    }
    
    
    /**
     * Create a blank RGIS-Object
     * 
     * @return blank RGIS-Object
     */
    public static RGIS createBlankRGIS() {
        return new RGIS();
    }
    
    
    /**
     * Print an RGIS
     * 
     * @param rgis
     *            RGIS to print
     */
    public static void printRGIS(RGIS rgis) {
        ISPrinter.printRGIS(rgis);
    }
    
}
