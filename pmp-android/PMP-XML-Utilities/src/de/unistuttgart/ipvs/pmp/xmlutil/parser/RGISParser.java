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
package de.unistuttgart.ipvs.pmp.xmlutil.parser;

import java.io.InputStream;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * This XML Parser parses a given xml (for a rg) and creates a rg information
 * set
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISParser extends AbstractParser {
    
    /**
     * RgInformationSet
     */
    private RGIS rgis;    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created
     * resourcegroup information set
     * 
     * @return created rg information set
     */
    public RGIS parse(InputStream xmlStream) {
		// Initialize
		initParser(xmlStream);
		
		// Create new RGIS
		rgis = new RGIS();
        
        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals("resourceGroupInformationSet")) {
            throw new ParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        }
        
        // The main nodes "resourceGroupInformation" and "privacySettings" are
        // required once.
        NodeList rgInformation = this.doc.getElementsByTagName("resourceGroupInformation");
        NodeList privacySettings = this.doc.getElementsByTagName("privacySettings");
        
        // Check, if there is exactly one resourceGroupInformation and one
        // privacySettings node
        if (rgInformation.getLength() < 1) {
            throw new ParserException(Type.NODE_MISSING, "The node resourceGroupInformation is missing!");
        } else if (rgInformation.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                    "The node resourceGroupInformation occurred too often!");
        }
        if (privacySettings.getLength() < 1) {
            throw new ParserException(Type.NODE_MISSING, "The node privacySettings is missing!");
        } else if (privacySettings.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node privacySettings occurred too often!");
        }
        
        // Check, if there are only 2 child nodes of appInformationSet
        checkNumberOfNodes(2, (Element) this.doc.getElementsByTagName("resourceGroupInformationSet").item(0));
        
        // Parse the nodes
        parseRgInformationNode((Element) rgInformation.item(0));
        parsePrivacySettingsNode((Element) privacySettings.item(0));
        
        return this.rgis;
    }
    
    
    /**
     * This method parses the rg information element
     * 
     * @param rgInformationElement
     *            starting with this root element
     */
    private void parseRgInformationNode(Element rgInformationElement) {
    	// Parse names and descriptions
    	parseNameDescriptionNodes(rgInformationElement, this.rgis);
    	
        // Create results and add them to the rg information set
        this.rgis.setIdentifier(rgInformationElement.getAttribute("identifier"));
        this.rgis.setIconLocation(rgInformationElement.getAttribute("icon"));
        this.rgis.setRevision(rgInformationElement.getAttribute("revision"));
        this.rgis.setClassName(rgInformationElement.getAttribute("className"));
    }
    
    
    /**
     * This method parses the privacy settings element
     * 
     * @param privacySettingsElement
     *            starting with this root element
     */
    private void parsePrivacySettingsNode(Element privacySettingsElement) {
        NodeList privacySettingsNodeList = privacySettingsElement.getElementsByTagName("privacySetting");
        
        // Parse the Privacy Settings
        for (int itr = 0; itr < privacySettingsNodeList.getLength(); itr++) {
        	// Get the element
        	Element privacySettingElement = (Element) privacySettingsNodeList.item(itr);
        	
        	// Instantiate a new Privacy Setting and add the identifier
        	RGISPrivacySetting ps = new RGISPrivacySetting(privacySettingElement.getAttribute("identifier"), null);
            
        	// Get the valid value description
            List<String[]> validValueDescrList = parseNodes(privacySettingElement, "validValueDescription");
            
            // Check, if the validValueDescription occurres too often or not at all
            if (validValueDescrList.size() > 1) {
            	throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                        "The node validValueDescription of the Privacy Setting " + ps.getIdentifier() + " occurred too often!");
            } else if (validValueDescrList.size() == 1) {
            	ps.setValidValueDescription(validValueDescrList.get(0)[0]);
            }
           
            // Parse names and descriptions
            parseNameDescriptionNodes(privacySettingElement, ps);
            
            // Add the Privacy Setting to the RGIS
            this.rgis.addPrivacySetting(ps);
        }
    }
}
