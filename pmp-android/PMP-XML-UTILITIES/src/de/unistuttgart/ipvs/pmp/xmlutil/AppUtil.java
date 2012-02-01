package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;
import de.unistuttgart.ipvs.pmp.xmlutil.app.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.app.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.app.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.app.Parser;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class AppUtil {
	
	protected AppUtil() {	
	}
	
    /**
     * This method creates an app information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return app information set
     */
	public AIS parseAISXML(InputStream xmlStream) {
		return new Parser(xmlStream).parse();
	}
	
	public boolean validateAIS(AIS ais) {
		return true;
	}
	
	public InputStream compileAIS(AIS ais) {
		return null;
	}
	
	public AIS createBlankAIS() {
		return new AIS();
	}
	
    
    /**
     * Print the app information set to the log
     * 
     * @param ais
     *            the app information set for printing
     */
    public void printAIS(AIS ais) {
        System.out.println("-----------------------");
        System.out.println("----- XML-Parser ------");
        System.out.println("-----------------------");
        System.out.println("-- App-Information: ---");
        System.out.println("-----------------------");
        for (Name name : ais.getNames()) {
            System.out.println("Name: " + name.getString() + " (Locale: " + name.getLocale().getLanguage() + ")");
        }
        for (Description descr : ais.getDescriptions()) {
            System.out.println("Description: " + descr.getString() + " (Locale: " + descr.getLocale().getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Service Features: --");
        System.out.println("-----------------------");
        for (ServiceFeature sf : ais.getServiceFeatures()) {
            System.out.println("Identifier: " + sf.getIdentifier());
            for (Name name : sf.getNames()) {
                System.out.println("Name: " + name.getString() + " (Locale: " + name.getLocale().getLanguage() + ")");
            }
            for (Description descr : sf.getDescriptions()) {
                System.out.println("Description: " + descr.getString() + " (Locale: " + descr.getLocale().getLanguage() + ")");
            }
            for (RequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
                System.out.println("Required Resource Group ID: " + rrg.getIdentifier());
                for (PrivacySetting ps : rrg.getPrivacySettings()) {
                    System.out.println("- Privacy Setting Value: "
                            + ps.getValue() + " (Identifier: "
                            + ps.getIdentifier() + ")");
                }
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }

}
