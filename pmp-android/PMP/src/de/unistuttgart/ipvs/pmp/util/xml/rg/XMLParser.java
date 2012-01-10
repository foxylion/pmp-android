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
package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractXMLParser;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * This XML Parser parses a given xml (for a rg) and creates a rg information
 * set
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLParser extends AbstractXMLParser {
    
    /**
     * RgInformationSet
     */
    private RgInformationSet rgis = new RgInformationSet();
    
    
    /**
     * Constructor
     */
    protected XMLParser(InputStream xmlStream) {
        super(xmlStream);
    }
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created
     * resourcegroup information set
     * 
     * @return created rg information set
     */
    protected RgInformationSet parse() {

        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals("resourceGroupInformationSet"))
            throw new XMLParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        
        // The main nodes "resourceGroupInformation" and "privacySettings" are
        // required once.
        NodeList rgInformation = this.doc.getElementsByTagName("resourceGroupInformation");
        NodeList privacySettings = this.doc.getElementsByTagName("privacySettings");
        
        // Check, if there is exactly one resourceGroupInformation and one
        // privacySettings node
        if (rgInformation.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING, "The node resourceGroupInformation is missing!");
        } else if (rgInformation.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                    "The node resourceGroupInformation occurred too often!");
        }
        if (privacySettings.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING, "The node privacySettings is missing!");
        } else if (privacySettings.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node privacySettings occurred too often!");
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
        // Create results
        String identifier = rgInformationElement.getAttribute("identifier");
        List<String[]> iconList = parseNodes(rgInformationElement, "icon", 1, 1);
        List<String[]> revisionList = parseNodes(rgInformationElement, "revision", 1, 1);
        List<String[]> defaultNameList = parseNodes(rgInformationElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(rgInformationElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(rgInformationElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(rgInformationElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the rg information node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
        // Check, if the lang attributes of the default name and description is "en"
        validateLocaleAttributeEN(defaultNameList.get(0)[1]);
        validateLocaleAttributeEN(defaultDescriptionList.get(0)[1]);
        
        // Check, if the identifier is set
        validateIdentifier(identifier);
        
        // Check, if all values are set
        validateValueListNotEmpty(iconList);
        validateValueListNotEmpty(revisionList);
        validateValueListNotEmpty(defaultNameList);
        validateValueListNotEmpty(nameList);
        validateValueListNotEmpty(defaultDescriptionList);
        validateValueListNotEmpty(descriptionList);
        
        // Check, if there is a correct number of child nodes of rgInformationElement
        int expectedNumber = iconList.size() + revisionList.size() + defaultNameList.size() + nameList.size() + defaultDescriptionList.size() + descriptionList.size();
        checkNumberOfNodes(expectedNumber, rgInformationElement);
        
        // Add to the rg information set
        this.rgis.setIdentifier(identifier);
        this.rgis.setIconLocation(iconList.get(0)[0]);
        this.rgis.setRevision(revisionList.get(0)[0]);
        this.rgis.addName(new Locale(defaultNameList.get(0)[1]), defaultNameList.get(0)[0].replaceAll("\t", "")
                .replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            this.rgis.addName(new Locale(nameArray[1]), nameArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        this.rgis.addDescription(new Locale(defaultDescriptionList.get(0)[1]), defaultDescriptionList.get(0)[0]
                .replaceAll("\t", "").replaceAll("\n", " ").trim());
        
        for (String[] descriptionArray : descriptionList) {
            this.rgis.addDescription(new Locale(descriptionArray[1]), descriptionArray[0].replaceAll("\t", "")
                    .replaceAll("\n", " ").trim());
        }
        
    }
    
    
    /**
     * This method parses the privacy settings element
     * 
     * @param privacySettingsElement
     *            starting with this root element
     */
    private void parsePrivacySettingsNode(Element privacySettingsElement) {
        NodeList privacySettingsNodeList = privacySettingsElement.getElementsByTagName("privacySetting");
        
        // check, if there are Privacy Settings defined
        if (privacySettingsNodeList.getLength() == 0)
            throw new XMLParserException(Type.PRIVACY_SETTING_MISSING,
                    "You have to define at least one Privacy Setting.");
        
        // Check, if there is a correct number of child nodes of privacySettingsElement
        int expectedNumber = privacySettingsNodeList.getLength();
        checkNumberOfNodes(expectedNumber, privacySettingsElement);
        
        // Parse the Privacy Settings
        for (int itr = 0; itr < privacySettingsNodeList.getLength(); itr++) {
            parseOnePrivacySetting((Element) privacySettingsNodeList.item(itr));
        }
    }
    
    
    /**
     * This method parses one privacy setting
     * 
     * @param privacySettingElement
     *            starting with this root element
     */
    private void parseOnePrivacySetting(Element privacySettingsElement) {
        
        // Get the identifier
        String identifier = privacySettingsElement.getAttribute("identifier");
        
        // Create results
        List<String[]> defaultNameList = parseNodes(privacySettingsElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(privacySettingsElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(privacySettingsElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(privacySettingsElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the privacy setting node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
        // Check, if the lang attributes of the default name and description is "en"
        validateLocaleAttributeEN(defaultNameList.get(0)[1]);
        validateLocaleAttributeEN(defaultDescriptionList.get(0)[1]);
        
        // Check, if the identifier is set
        validateIdentifier(identifier);
        
        // Check, if all values are set
        validateValueListNotEmpty(defaultNameList);
        validateValueListNotEmpty(nameList);
        validateValueListNotEmpty(defaultDescriptionList);
        validateValueListNotEmpty(descriptionList);
        
        // Check, if there is a correct number of child nodes of privacySettingsElement
        int expectedNumber = defaultNameList.size() + nameList.size() + defaultDescriptionList.size() + descriptionList.size();
        checkNumberOfNodes(expectedNumber, privacySettingsElement);
        
        // Add to the rg information set
        PrivacySetting ps = new PrivacySetting();
        this.rgis.addPrivacySetting(identifier, ps);
        
        ps.addName(new Locale(defaultNameList.get(0)[1]),
                defaultNameList.get(0)[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            ps.addName(new Locale(nameArray[1]), nameArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        ps.addDescription(new Locale(defaultDescriptionList.get(0)[1]),
                defaultDescriptionList.get(0)[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        for (String[] descriptionArray : descriptionList) {
            ps.addDescription(new Locale(descriptionArray[1]),
                    descriptionArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        
    }
}
