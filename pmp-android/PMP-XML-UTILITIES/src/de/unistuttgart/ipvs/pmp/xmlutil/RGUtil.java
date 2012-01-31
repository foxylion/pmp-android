package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.rg.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.Parser;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class RGUtil {
	
	protected RGUtil() {
	}
	
    /**
     * This method creates an resourcegroup information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return resourcegroup information set
     */
	public RGIS parseRGISXML(InputStream xmlStream) {
        return new Parser(xmlStream).parse();
	}
	
	public InputStream compileRGIS() {
		return null;
	}
	
	public RGIS createBlankRGIS() {
		return new RGIS();
	}
	
    /**
     * Print the rg information set to the log
     * 
     * @param rgis
     *            the rg information set for printing
     */
    public void printRGIS(RGIS rgis) {
        System.out.println("-----------------------");
        System.out.println("----- XML-Parser ------");
        System.out.println("-----------------------");
        System.out.println("--- Rg-Information: ---");
        System.out.println("-----------------------");
        System.out.println("Identifier: " + rgis.getIdentifier());
        System.out.println("IconLocation: " + rgis.getIconLocation());
        for (Locale l : rgis.getNames().keySet()) {
            System.out.println("Name: " + rgis.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        for (Locale l : rgis.getDescriptions().keySet()) {
            System.out.println("Description: " + rgis.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Privacy Settings: --");
        System.out.println("-----------------------");
        for (String rgIdentifier : rgis.getPrivacySettingsMap().keySet()) {
            PrivacySetting ps = rgis.getPrivacySettingsMap().get(rgIdentifier);
            System.out.println("Identifier: " + rgIdentifier);
            for (Locale l : ps.getNames().keySet()) {
                System.out.println("Name: " + ps.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (Locale l : ps.getDescriptions().keySet()) {
                System.out.println("Description: " + ps.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }

}
