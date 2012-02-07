package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.compiler.PresetSetCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.PresetSetParser;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * Utility class for Presets
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetUtil {
    
    /**
     * The PresetSetParser and -Compiler
     */
    private static PresetSetParser presetSetParser = new PresetSetParser();
    private static PresetSetCompiler presetSetCompiler = new PresetSetCompiler();
    
    
    /**
     * This method creates a PresetSet for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return PresetSet
     */
    public static PresetSet parse(InputStream xmlStream) {
        return presetSetParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given PresetSet
     * 
     * @param presetSet
     *            RGIS to compile
     * @return compiled xml file
     */
    public static InputStream compile(PresetSet presetSet) {
        return presetSetCompiler.compile(presetSet);
    }
    
    
    /**
     * Create a blank PresetSet-Object
     * 
     * @return blank PresetSet-Object
     */
    public static PresetSet createBlankPresetSet() {
        return new PresetSet();
    }
    
    
    /**
     * Print a PresetSet
     * 
     * @param presetSet
     *            PresetSet to print
     */
    public static void print(PresetSet presetSet) {
        ISPrinter.printPresetSet(presetSet);
    }
    
}
