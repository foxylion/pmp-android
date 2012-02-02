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

import java.io.InputStream;

import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.RGISParser;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.PrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

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
		long a = System.currentTimeMillis();
		RGIS rgis = new RGISParser(xmlStream).parse();
		System.out.println("Parsing took " + Long.toString(System.currentTimeMillis() - a) + "ms.");
        return rgis;
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
