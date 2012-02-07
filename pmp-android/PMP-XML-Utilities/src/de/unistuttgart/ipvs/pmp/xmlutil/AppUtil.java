package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.AISCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.AISParser;

/**
 * Utility class for apps
 * 
 * @author Marcus Vetter
 * 
 */
public class AppUtil {
    
    /**
     * The AISParser and -Compiler
     */
    private static AISParser aisParser = new AISParser();
    private static AISCompiler aisCompiler = new AISCompiler();
    
    
    /**
     * This method creates an app information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return app information set
     */
    public static AIS parse(InputStream xmlStream) {
        return aisParser.parse(xmlStream);
    }
    
    
    /**
     * This method creates an xml file for a given AIS
     * 
     * @param ais
     *            AIS to compile
     * @return compiled xml file
     */
    public static InputStream compile(AIS ais) {
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
    public static void print(AIS ais) {
        ISPrinter.printAIS(ais);
    }
    
}
