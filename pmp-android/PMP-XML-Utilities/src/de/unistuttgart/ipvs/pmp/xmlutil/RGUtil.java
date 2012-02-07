package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.compiler.RGISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Utility class for resource groups
 * 
 * @author Marcus Vetter
 * 
 */
public class RGUtil {
    
    /**
     * The RGISParser and -Compiler
     */
    private static RGISParser rgisParser = new RGISParser();
    private static RGISCompiler rgisCompiler = new RGISCompiler();
    
    
    /**
     * This method creates an resourcegroup information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return resourcegroup information set
     */
    public static RGIS parse(InputStream xmlStream) {
        return rgisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given ARG
     * 
     * @param rgis
     *            RGIS to compile
     * @return compiled xml file
     */
    public static InputStream compile(RGIS rgis) {
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
     * Print a RGIS
     * 
     * @param rgis
     *            RGIS to print
     */
    public static void print(RGIS rgis) {
        ISPrinter.printRGIS(rgis);
    }
    
}
