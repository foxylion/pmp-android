package de.unistuttgart.ipvs.pmp.xmlutil;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map.Entry;

import de.unistuttgart.ipvs.pmp.xmlutil.app.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.app.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.app.Parser;

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
        for (Locale l : ais.getNames().keySet()) {
            System.out.println("Name: " + ais.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        for (Locale l : ais.getDescriptions().keySet()) {
            System.out.println("Description: " + ais.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        System.out.println("-----------------------");
        System.out.println("-- Service Features: --");
        System.out.println("-----------------------");
        for (String sfIdentifier : ais.getServiceFeaturesMap().keySet()) {
            ServiceFeature sf = ais.getServiceFeaturesMap().get(sfIdentifier);
            System.out.println("Identifier: " + sfIdentifier);
            for (Locale l : sf.getNames().keySet()) {
                System.out.println("Name: " + sf.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (Locale l : sf.getDescriptions().keySet()) {
                System.out.println("Description: " + sf.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (Entry<String, RequiredResourceGroup> entry : sf.getRequiredResourceGroups().entrySet()) {
                System.out.println("Required Resource Group ID: " + entry.getKey());
                for (String privacySettingIdentifier : entry.getValue().getPrivacySettingsMap().keySet()) {
                    System.out.println("- Privacy Setting Value: "
                            + entry.getValue().getPrivacySettingsMap().get(privacySettingIdentifier) + " (Identifier: "
                            + privacySettingIdentifier + ")");
                    
                }
            }
            System.out.println("-----------------------");
        }
        System.out.println("-----------------------");
    }

}
