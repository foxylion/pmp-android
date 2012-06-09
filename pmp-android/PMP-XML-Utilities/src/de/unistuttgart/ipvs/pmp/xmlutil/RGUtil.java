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
package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.compiler.RGISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.RGISValidator;

/**
 * Utility class for resource groups
 * 
 * @author Marcus Vetter
 * 
 */
public class RGUtil {
    
    /**
     * The {@link RGISParser}, {@link RGISCompiler} and {@link RGISValidator}
     */
    private static RGISParser rgisParser = new RGISParser();
    private static RGISCompiler rgisCompiler = new RGISCompiler();
    private static RGISValidator rgisValidator = new RGISValidator();
    
    
    /**
     * This method creates a {@link IRGIS} for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return {@link IRGIS}
     */
    public IRGIS parse(InputStream xmlStream) {
        return rgisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given {@link IRGIS}
     * 
     * @param rgis
     *            {@link IRGIS} to compile
     * @return compiled xml file
     */
    public InputStream compile(IRGIS rgis) {
        return rgisCompiler.compile(rgis);
    }
    
    
    /**
     * Get the validator for {@link IRGIS}
     * 
     * @return {@link RGISValidator}
     */
    public RGISValidator getValidator() {
        return rgisValidator;
    }
    
    
    /**
     * Create a blank {@link IRGIS}-Object
     * 
     * @return blank {@link IRGIS}-Object
     */
    public IRGIS createBlankRGIS() {
        return new RGIS();
    }
    
    
    /**
     * Print a {@link IRGIS}
     * 
     * @param rgis
     *            {@link IRGIS} to print
     */
    public void print(IRGIS rgis) {
        Printer.printRGIS(rgis);
    }
    
}
