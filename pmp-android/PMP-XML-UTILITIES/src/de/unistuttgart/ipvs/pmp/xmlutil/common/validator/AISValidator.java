package de.unistuttgart.ipvs.pmp.xmlutil.common.validator;

import de.unistuttgart.ipvs.pmp.xmlutil.app.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.app.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.app.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.app.PrivacySetting;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidator {

	/**
	 * Validate the given AppInformationSet (ais) Check, that there are no
	 * different Service Features which address the same Privacy Settings and
	 * the same required values of those Privacy Settings.
	 * 
	 * @param ais
	 *            the AppInformationSet
	 */
	public static void validateAISDiffPSValuesForDiffSFs(AIS ais) {

		for (ServiceFeature sf : ais.getServiceFeatures()) {

			// Iterate through all other Service Features
			COMPARE_SF: for (ServiceFeature sfCompare : ais
					.getServiceFeatures()) {

				// If it's the same sf identifier, continue
				if (sf.getIdentifier().equals(sfCompare.getIdentifier())) {
					continue COMPARE_SF;
				}

				// Continue, if they have a different number of RRGs
				if (sf.getRequiredResourceGroups().size() != sfCompare
						.getRequiredResourceGroups().size()) {
					continue COMPARE_SF;
				}
				for (RequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {

					// Iterate through all RRGs of the sfCompare
					for (RequiredResourceGroup rrgCompare : sfCompare
							.getRequiredResourceGroups()) {

						// Continue, if the RRGs do not have the same identifier
						if (!rrg.getIdentifier().equals(
								rrgCompare.getIdentifier())) {
							continue COMPARE_SF;
						}

						// Continue, if they have a different number of PSs
						// within one RRG
						if (rrg.getPrivacySettings().size() != rrgCompare
								.getPrivacySettings().size()) {
							continue COMPARE_SF;
						}

						// Iterate through all PSs of the rrg
						for (PrivacySetting ps : rrg.getPrivacySettings()) {

							// Iterate through all PSs of the rrgCompare
							for (PrivacySetting psCompare : rrgCompare
									.getPrivacySettings()) {

								// Continue, if they have different PSs
								// identifier
								if (!ps.getIdentifier().equals(
										psCompare.getIdentifier())) {
									continue COMPARE_SF;
								}

								// Continue, of they have different PSs values
								if (!ps.getValue().equals(psCompare.getValue())) {
									continue COMPARE_SF;
								}
							}

						}

					}

				}
				/*
				 * If we reach this line, the both Service Feature have the same
				 * RRGs, the same PSs within the RGGs and the same values of the
				 * PSs
				 */
				throw new ParserException(
						Type.AT_LEAST_TWO_SFS_ADDRESS_SAME_RRGS_AND_PSS,
						"At least two Service Features address the same required Resourcegroups and the same Privacy Settings with keys and values. This is not allowed.");

			}

		}
	}

}
