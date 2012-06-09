/*
 * Copyright 2012 pmp-android development team
 * Project: PMP-XML-UTILITIES
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
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParsedNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * This XML Parser parses a given xml (for a rg) and creates a {@link IRGIS}
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISParser extends AbstractParser {
    
    /**
     * The {@link IRGIS}
     */
    private IRGIS rgis;
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created {@link IRGIS}
     * 
     * @return created {@link IRGIS}
     */
    public IRGIS parse(InputStream xmlStream) {
        // Initialize
        initParser(xmlStream);
        
        // Create new RGIS
        this.rgis = new RGIS();
        
        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals(XMLConstants.RGIS)) {
            throw new ParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        }
        
        // The main nodes "resourceGroupInformation" and "privacySettings" are
        // required once.
        NodeList rgInformation = this.doc.getElementsByTagName(XMLConstants.RGI);
        NodeList privacySettings = this.doc.getElementsByTagName(XMLConstants.PSS);
        
        // Check, if there is a maximum of one resourceGroupInformation and one
        // privacySettings node
        int maxValid = 0;
        if (rgInformation.getLength() == 1) {
            // Parse the rg information node
            parseRgInformationNode((Element) rgInformation.item(0));
            maxValid++;
        } else if (rgInformation.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                    "The node resourceGroupInformation occurred too often!");
        }
        if (privacySettings.getLength() == 1) {
            // Parse the privacy settings node
            parsePrivacySettingsNode((Element) privacySettings.item(0));
            maxValid++;
        } else if (privacySettings.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node privacySettings occurred too often!");
        }
        
        // Check, if there are a maximum of maxValid child nodes of the root node
        checkMaxNumberOfNodes(maxValid, (Element) this.doc.getElementsByTagName(XMLConstants.RGIS).item(0));
        
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
        this.rgis.setIdentifier(rgInformationElement.getAttribute(XMLConstants.IDENTIFIER_ATTR));
        this.rgis.setIconLocation(rgInformationElement.getAttribute(XMLConstants.ICON_ATTR));
        this.rgis.setClassName(rgInformationElement.getAttribute(XMLConstants.CLASS_NAME_ATTR));
    }
    
    
    /**
     * This method parses the {@link IRGISPrivacySetting} element
     * 
     * @param privacySettingsElement
     *            starting with this root element
     */
    private void parsePrivacySettingsNode(Element privacySettingsElement) {
        NodeList privacySettingsNodeList = privacySettingsElement.getElementsByTagName(XMLConstants.PRIVACY_SETTING);
        
        // Parse the Privacy Settings
        for (int itr = 0; itr < privacySettingsNodeList.getLength(); itr++) {
            // Get the element
            Element privacySettingElement = (Element) privacySettingsNodeList.item(itr);
            
            // Instantiate a new Privacy Setting and add the identifier and
            // validValueDescription
            RGISPrivacySetting ps = new RGISPrivacySetting(
                    privacySettingElement.getAttribute(XMLConstants.IDENTIFIER_ATTR),
                    privacySettingElement.getAttribute(XMLConstants.VALID_VALUE_DESCRIPTION_ATTR));
            
            // Add the attribute requestable
            String reqAttribute = privacySettingElement.getAttribute(XMLConstants.REQUESTABLE_ATTR);
            if (reqAttribute.toLowerCase().equals("false")) {
                ps.setRequestable(false);
            } else if (!reqAttribute.toLowerCase().equals("true") && !reqAttribute.equals("")) {
                throw new ParserException(Type.REQUESTABLE_BOOLEAN_EXCEPTION,
                        "The value of the attribute \"requestable\" of a Privacy Setting is not a boolean.");
            }
            
            // Parse names and descriptions
            parseNameDescriptionNodes(privacySettingElement, ps);
            
            // Parse change descriptions and add them
            List<ParsedNode> changeDescriptionList = parseNodes(privacySettingElement,
                    XMLConstants.CHANGE_DESCRIPTION_ATTR, XMLConstants.LANGUAGE_ATTR);
            for (ParsedNode changeDescriptionNode : changeDescriptionList) {
                LocalizedString changeDescr = new LocalizedString();
                changeDescr.setLocale(new Locale(changeDescriptionNode.getAttribute(XMLConstants.LANGUAGE_ATTR)));
                if (changeDescriptionNode.getValue() != null) {
                    changeDescr.setString(changeDescriptionNode.getValue().replaceAll("\t", "").replaceAll("\n", " ")
                            .trim());
                }
                ps.addChangeDescription(changeDescr);
            }
            
            // Add the Privacy Setting to the RGIS
            this.rgis.addPrivacySetting(ps);
        }
    }
}
