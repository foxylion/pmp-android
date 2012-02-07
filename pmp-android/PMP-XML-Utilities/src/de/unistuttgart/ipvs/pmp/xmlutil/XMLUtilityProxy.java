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
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.AISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.PresetSetCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.RGISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.AISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.PresetSetParser;
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
     * The AIS-/RGIS-/PresetSet-Parser and -Compiler
     */
    private static AISParser aisParser = new AISParser();
    private static AISCompiler aisCompiler = new AISCompiler();
    private static RGISParser rgisParser = new RGISParser();
    private static RGISCompiler rgisCompiler = new RGISCompiler();
    private static PresetSetParser presetSetParser = new PresetSetParser();
    private static PresetSetCompiler presetSetCompiler = new PresetSetCompiler();
    
    
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
        return aisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given AIS
     * 
     * @param ais
     *            AIS to compile
     * @return compiled xml file
     */
    public static InputStream compileAISXML(AIS ais) {
        return aisCompiler.compile(ais);
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
        return rgisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given ARG
     * 
     * @param rgis
     *            RGIS to compile
     * @return compiled xml file
     */
    public static InputStream compileRGISXML(RGIS rgis) {
        return rgisCompiler.compile(rgis);
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
