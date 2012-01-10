package de.unistuttgart.ipvs.pmp.util.xml;

import java.util.Locale;
import java.util.Map.Entry;

import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;
import de.unistuttgart.ipvs.pmp.util.xml.app.AppInformationSet;
import de.unistuttgart.ipvs.pmp.util.xml.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.util.xml.app.ServiceFeature;

/**
 * 
 * @author Marcus Vetter
 *
 */
public class InformationSetValidator {
    
    /**
     * Validate the given AppInformationSet (ais)
     * Check, that there are no different Service Features which address the same Privacy Settings and the same required
     * values of those Privacy Settings.
     * 
     * @param ais
     *            the AppInformationSet
     */
    public static void validateAISDiffPSValuesForDiffSFs(AppInformationSet ais) {
        
        for (Entry<String, ServiceFeature> entry : ais.getServiceFeaturesMap().entrySet()) {
            // The Service Feature to check
            ServiceFeature sf = entry.getValue();
            
            // Iterate through all other Service Features
            COMPARE_SF: for (Entry<String, ServiceFeature> compareEntry : ais.getServiceFeaturesMap().entrySet()) {
                // The Service Feature to compair with
                ServiceFeature sfCompare = compareEntry.getValue();
                
                // If it's the same sf identifier, continue
                if (entry.getKey().equals(compareEntry.getKey()))
                    continue COMPARE_SF;
                
                // Continue, if they have a different number of RRGs
                if (sf.getRequiredResourceGroups().size() != sfCompare.getRequiredResourceGroups().size())
                    continue COMPARE_SF;
                
                for (Entry<String, RequiredResourceGroup> rrgEntry : sf.getRequiredResourceGroups().entrySet()) {
                    // The RRG of the sf
                    RequiredResourceGroup rrg = rrgEntry.getValue();
                    
                    // Iterate through all RRGs of the sfCompare
                    for (Entry<String, RequiredResourceGroup> rrgEntryCompare : sfCompare.getRequiredResourceGroups()
                            .entrySet()) {
                        // The RRG of the sfCompare
                        RequiredResourceGroup rrgCompare = rrgEntryCompare.getValue();
                        
                        // Continue, if the RRGs do not have the same identifier
                        if (!rrgEntry.getKey().equals(rrgEntryCompare.getKey()))
                            continue COMPARE_SF;
                        
                        // Continue, if they have a different number of PSs within one RRG
                        if (rrg.getPrivacySettingsMap().size() != rrgCompare.getPrivacySettingsMap().size())
                            continue COMPARE_SF;
                        
                        // Iterate through all PSs of the rrg
                        for (Entry<String, String> psEntry : rrg.getPrivacySettingsMap().entrySet()) {
                            
                            // Iterate through all PSs of the rrgCompare
                            for (Entry<String, String> psEntryCompare : rrgCompare.getPrivacySettingsMap().entrySet()) {
                                
                                // Continue, if they have different PSs identifier
                                if (!psEntry.getKey().equals(psEntryCompare.getKey()))
                                    continue COMPARE_SF;
                                
                                // Continue, of they have different PSs values
                                if (!psEntry.getValue().equals(psEntryCompare.getValue()))
                                    continue COMPARE_SF;
                            }
                            
                        }
                        
                    }
                    
                }
                /*
                 * If we reach this line, the both Service Feature have the same RRGs, 
                 * the same PSs within the RGGs and the same values of the PSs
                 */
                throw new XMLParserException(
                        Type.AT_LEAST_TWO_SFS_ADDRESS_SAME_RRGS_AND_PSS,
                        ais.getNames().get(new Locale("en"))
                                + ": At least two Service Features address the same required Resourcegroups and the same Privacy Settings with keys and values. This is not allowed.");
                
            }
            
        }
    }
    
}
