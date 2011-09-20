package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.InputStream;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.Log;

/**
 * This Class provides the functionality to parse a given xml file (information set of the app) and return a complete
 * AppInformationSet.
 * 
 * @author marcus
 * 
 */
public class AppInformationSetParser {
    
    /**
     * This method creates an app information set for a given xml url.
     * 
     * @param xmlURL
     *            url to the xml file
     * @return app information set
     */
    public static AppInformationSet createAppInformationSet(InputStream xmlStream) {
        return new XMLParser().parse(xmlStream);
    }
    
    
    /**
     * Print the app information set to the log
     * 
     * @param ais
     *            the app information set for printing
     */
    public static void printAppInformationSet(AppInformationSet ais) {
        Log.d("----------------------");
        Log.d("----- XML-Parser -----");
        Log.d("----------------------");
        Log.d("-- App-Information: --");
        Log.d("----------------------");
        for (Locale l : ais.getNames().keySet()) {
            Log.d("Name: " + ais.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        for (Locale l : ais.getDescriptions().keySet()) {
            Log.d("Description: " + ais.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
        }
        Log.d("----------------------");
        Log.d("-- Service Levels: ---");
        Log.d("----------------------");
        
        for (Integer level : ais.getServiceLevels().keySet()) {
            ServiceLevel sl = ais.getServiceLevels().get(level);
            Log.d("Level: " + level);
            for (Locale l : sl.getNames().keySet()) {
                Log.d("Name: " + sl.getNames().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (Locale l : sl.getDescriptions().keySet()) {
                Log.d("Description: " + sl.getDescriptions().get(l) + " (Locale: " + l.getLanguage() + ")");
            }
            for (RequiredResourceGroup rrg : sl.getRequiredResourceGroups()) {
                Log.d("Required Resource Group ID: " + rrg.getRgIdentifier());
                for (String privacyLevelIdentifier : rrg.getPrivacyLevelMap().keySet()) {
                    Log.d("- Privacy Level Value: " + rrg.getPrivacyLevelMap().get(privacyLevelIdentifier)
                            + " (Identifier: " + privacyLevelIdentifier + ")");
                    
                }
            }
            Log.d("----------------------");
        }
        Log.d("----------------------");
    }
    
}
