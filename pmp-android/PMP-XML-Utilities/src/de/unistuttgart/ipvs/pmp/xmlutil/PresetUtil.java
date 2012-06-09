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

import de.unistuttgart.ipvs.pmp.xmlutil.compiler.PresetSetCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.PresetSetParser;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.IPresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.PresetSetValidator;

/**
 * Utility class for Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetUtil {
    
    /**
     * The {@link PresetSetParser}, {@link PresetSetCompiler} and {@link PresetSetValidator}
     */
    private PresetSetParser presetSetParser = new PresetSetParser();
    private PresetSetCompiler presetSetCompiler = new PresetSetCompiler();
    private PresetSetValidator presetSetValidator = new PresetSetValidator();
    
    
    /**
     * This method creates a {@link IPresetSet} for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return {@link IPresetSet}
     */
    public IPresetSet parse(InputStream xmlStream) {
        return this.presetSetParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given {@link IPresetSet}
     * 
     * @param presetSet
     *            {@link IPresetSet} to compile
     * @return compiled xml file
     */
    public InputStream compile(IPresetSet presetSet) {
        return this.presetSetCompiler.compile(presetSet);
    }
    
    
    /**
     * Get the validator for {@link IPresetSet}
     * 
     * @return {@link PresetSetValidator}
     */
    public PresetSetValidator getValidator() {
        return this.presetSetValidator;
    }
    
    
    /**
     * Create a blank {@link IPresetSet}-Object
     * 
     * @return blank {@link IPresetSet}-Object
     */
    public IPresetSet createBlankPresetSet() {
        return new PresetSet();
    }
    
    
    /**
     * Print a {@link IPresetSet}
     * 
     * @param presetSet
     *            {@link IPresetSet} to print
     */
    public void print(IPresetSet presetSet) {
        Printer.printPresetSet(presetSet);
    }
    
}
