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
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.AISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.AISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;

/**
 * Utility class for apps
 * 
 * @author Marcus Vetter
 * 
 */
public class AppUtil {
    
    /**
     * The AISParser, -Compiler and -Validator
     */
    private static AISParser aisParser = new AISParser();
    private static AISCompiler aisCompiler = new AISCompiler();
    private static AISValidator aisValidator = new AISValidator();
    
    
    /**
     * This method creates an app information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return app information set
     */
    public IAIS parse(InputStream xmlStream) {
        return aisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given AIS
     * 
     * @param ais
     *            AIS to compile
     * @return compiled xml file
     */
    public InputStream compile(IAIS ais) {
        return aisCompiler.compile(ais);
    }
    
    
    /**
     * Get the validator for AIS
     * 
     * @return AISValidator
     */
    public AISValidator getValidator() {
        return aisValidator;
    }
    
    
    /**
     * Create a blank AIS-Object
     * 
     * @return blank AIS-Object
     */
    public IAIS createBlankAIS() {
        return new AIS();
    }
    
    
    /**
     * Print an AIS
     * 
     * @param ais
     *            AIS to print
     */
    public void print(IAIS ais) {
        Printer.printAIS(ais);
    }
    
}
