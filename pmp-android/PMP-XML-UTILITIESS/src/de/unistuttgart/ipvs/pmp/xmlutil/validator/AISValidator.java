/*
 * Copyright 2011 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.xmlutil.validator;

import java.util.List;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISValidator {
	
	/**
	 * This method validates, if a given locale attribute exists and is valid.
	 * 
	 * @param nodeResultList
	 *            the given node result list array.
	 */
	public void validateLocaleAttribute(List<String[]> nodeResultList) {
		// Check all nodes
		for (String[] nodeArray : nodeResultList) {
			// Check, if the locale is missing
			if (nodeArray[1].equals("")) {
				throw new ParserException(Type.LOCALE_MISSING, "The locale of "
						+ nodeArray[0] + " is missing!");
			}
			// Check, if the locale is valid
			if (!checkLocale(nodeArray[1])) {
				throw new ParserException(Type.LOCALE_INVALID, "The locale "
						+ nodeArray[1] + " of " + nodeArray[0] + " is invalid!");
			}
		}
	}
	
	/**
	 * Check, if the given locale (as string) is valid.
	 * 
	 * @param givenLocale
	 *            locale to check (as string)
	 * @return flag, if the given local is valid or not.
	 */
	public boolean checkLocale(String givenLocale) {
		for (String locale : Locale.getISOLanguages()) {
			if (locale.equals(givenLocale)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check, if the lang attribute value of a given lang attribute equals "en"
	 * 
	 * @param langAttributeValue
	 *            the lang attribute value
	 */
	public void validateLocaleAttributeEN(String langAttributeValue) {
		if (!langAttributeValue.equals("en")) {
			throw new ParserException(Type.LOCALE_INVALID,
					"The lang attribute value of the default name/description has to be \"en\"");
		}
	}

	/**
	 * The method validates, if the identifier is set
	 * 
	 * @param identifier
	 *            identifier to validate
	 */
	public void validateIdentifier(String identifier) {
		if (identifier.equals("") || identifier == null) {
			throw new ParserException(Type.IDENTIFIER_MISSING,
					"The identifier of the resource group is missing.");
		}
	}

	/**
	 * The method validates, if a given value is set
	 * 
	 * @param value
	 *            value to validate
	 */
	public void validateValueNotEmpty(String value) {
		if (value.equals("") || value == null) {
			throw new ParserException(Type.VALUE_MISSING,
					"The value of a node is empty.");
		}
	}

	/**
	 * The method validates, if a given list of string value are set
	 * 
	 * @param values
	 *            values to validate
	 */
	public void validateValueListNotEmpty(List<String[]> values) {
		for (String[] stringArray : values) {
			for (String element : stringArray) {
				validateValueNotEmpty(element);
			}
		}
	}

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
