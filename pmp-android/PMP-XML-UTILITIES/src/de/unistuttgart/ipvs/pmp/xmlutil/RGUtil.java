package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.Parser;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rg.RGIS;

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
        System.out.println("Revision: " + rgis.getRevision());
        System.out.println("IconLocation: " + rgis.getIconLocation());
        for (Name name : rgis.getNames()) {
            System.out.println("Name: " + name.getString() + " (Locale: " + name.getLocale().getLanguage() + ")");
        }
        for (Description descr : rgis.getDescriptions()) {
            System.out.println("Description: " + descr.getString() + " (Locale: " + descr.getLocale().getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Privacy Settings: --");
        System.out.println("-----------------------");
        for (PrivacySetting ps : rgis.getPrivacySettings()) {
            System.out.println("Identifier: " + ps.getIdentifier());
            System.out.println("Valid value description: " + ps.getValidValueDescription());
            for (Name name : ps.getNames()) {
                System.out.println("Name: " + name.getString() + " (Locale: " + name.getLocale().getLanguage() + ")");
            }
            for (Description descr : ps.getDescriptions()) {
                System.out.println("Description: " + descr.getString() + " (Locale: " + descr.getLocale().getLanguage() + ")");
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }

}
