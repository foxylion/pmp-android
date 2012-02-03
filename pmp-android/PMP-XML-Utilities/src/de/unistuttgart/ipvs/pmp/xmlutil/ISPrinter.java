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
package de.unistuttgart.ipvs.pmp.xmlutil;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.RequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.ServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * This Class provides static methods to print a given AIS or RGIS.
 * 
 * @author Marcus Vetter
 * 
 */
public class ISPrinter {

	/**
	 * Print the app information set to the log
	 * 
	 * @param ais
	 *            the app information set for printing
	 */
	public static void printAIS(AIS ais) {
		System.out.println("-----------------------");
		System.out.println("----- XML-Parser ------");
		System.out.println("-----------------------");
		System.out.println("-- App-Information: ---");
		System.out.println("-----------------------");
		for (Name name : ais.getNames()) {
			System.out.println("Name: " + name.getString() + " (Locale: "
					+ name.getLocale().getLanguage() + ")");
		}
		for (Description descr : ais.getDescriptions()) {
			System.out.println("Description: " + descr.getString()
					+ " (Locale: " + descr.getLocale().getLanguage() + ")");
		}
		System.out.println("-----------------------");
		System.out.println("-- Service Features: --");
		System.out.println("-----------------------");
		for (ServiceFeature sf : ais.getServiceFeatures()) {
			System.out.println("Identifier: " + sf.getIdentifier());
			for (Name name : sf.getNames()) {
				System.out.println("Name: " + name.getString() + " (Locale: "
						+ name.getLocale().getLanguage() + ")");
			}
			for (Description descr : sf.getDescriptions()) {
				System.out.println("Description: " + descr.getString()
						+ " (Locale: " + descr.getLocale().getLanguage() + ")");
			}
			for (RequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
				System.out.println("Required Resource Group ID: "
						+ rrg.getIdentifier());
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

	/**
	 * Print the rg information set to the log
	 * 
	 * @param rgis
	 *            the rg information set for printing
	 */
	public static void printRGIS(RGIS rgis) {
		System.out.println("-----------------------");
		System.out.println("----- XML-Parser ------");
		System.out.println("-----------------------");
		System.out.println("--- Rg-Information: ---");
		System.out.println("-----------------------");
		System.out.println("Identifier: " + rgis.getIdentifier());
		System.out.println("Revision: " + rgis.getRevision());
		System.out.println("IconLocation: " + rgis.getIconLocation());
		System.out.println("Class Name: " + rgis.getClassName());
		for (Name name : rgis.getNames()) {
			System.out.println("Name: " + name.getString() + " (Locale: "
					+ name.getLocale().getLanguage() + ")");
		}
		for (Description descr : rgis.getDescriptions()) {
			System.out.println("Description: " + descr.getString()
					+ " (Locale: " + descr.getLocale().getLanguage() + ")");
		}
		System.out.println("-----------------------");
		System.out.println("-- Privacy Settings: --");
		System.out.println("-----------------------");
		for (de.unistuttgart.ipvs.pmp.xmlutil.rgis.PrivacySetting ps : rgis
				.getPrivacySettings()) {
			System.out.println("Identifier: " + ps.getIdentifier());
			System.out.println("Valid value description: "
					+ ps.getValidValueDescription());
			for (Name name : ps.getNames()) {
				System.out.println("Name: " + name.getString() + " (Locale: "
						+ name.getLocale().getLanguage() + ")");
			}
			for (Description descr : ps.getDescriptions()) {
				System.out.println("Description: " + descr.getString()
						+ " (Locale: " + descr.getLocale().getLanguage() + ")");
			}
			System.out.println("-----------------------");
		}
		System.out.println("-----------------------");
	}

}
