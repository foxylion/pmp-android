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
package de.unistuttgart.ipvs.pmp.util.xml.app;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractXMLParser;
import de.unistuttgart.ipvs.pmp.util.xml.InformationSetValidator;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * This XML Parser parses a given xml (for an app) and creates a app information
 * set
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLParser extends AbstractXMLParser {
    
    /**
     * AppInformationSet
     */
    private AppInformationSet ais = new AppInformationSet();
    
    
    /**
     * Constructor
     */
    protected XMLParser(InputStream xmlStream) {
        super(xmlStream);
    }
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created app
     * information set
     * 
     * @return created app information set
     */
    protected AppInformationSet parse() {
        
        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals("appInformationSet"))
            throw new XMLParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        
        // The main nodes "appInformation" and "serviceFeatures" are
        // required once.
        NodeList appInformation = this.doc.getElementsByTagName("appInformation");
        NodeList serviceFeatures = this.doc.getElementsByTagName("serviceFeatures");
        
        // Check, if there is exactly one appInformation and one service
        // features node
        if (appInformation.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING, "The node appInformation is missing!");
        } else if (appInformation.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node appInformation occurred too often!");
        }
        if (serviceFeatures.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING, "The node serviceFeatures is missing!");
        } else if (serviceFeatures.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node serviceFeatures occurred too often!");
        }
        
        // Check, if there are only 2 child nodes of appInformationSet
        checkNumberOfNodes(2, (Element) this.doc.getElementsByTagName("appInformationSet").item(0));
        
        // Parse the nodes
        parseAppInformationNode((Element) appInformation.item(0));
        parseServiceFeaturesNode((Element) serviceFeatures.item(0));
        
        // Validate the AppInformationSet
        InformationSetValidator.validateAISDiffPSValuesForDiffSFs(ais);
        
        return this.ais;
    }
    
    
    /**
     * This method parses the app information element
     * 
     * @param appInformationElement
     *            starting with this root element
     */
    private void parseAppInformationNode(Element appInformationElement) {
        // Create results
        List<String[]> defaultNameList = parseNodes(appInformationElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(appInformationElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(appInformationElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(appInformationElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the app information node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
        // Check, if the lang attributes of the default name and description is "en"
        validateLocaleAttributeEN(defaultNameList.get(0)[1]);
        validateLocaleAttributeEN(defaultDescriptionList.get(0)[1]);
        
        // Check, if all values are set
        validateValueListNotEmpty(defaultNameList);
        validateValueListNotEmpty(nameList);
        validateValueListNotEmpty(defaultDescriptionList);
        validateValueListNotEmpty(descriptionList);
        
        // Check, if there is a correct number of child nodes of appInformation
        int expectedNumber = defaultNameList.size() + nameList.size() + defaultDescriptionList.size() + descriptionList.size();
        checkNumberOfNodes(expectedNumber, appInformationElement);
        
        // Add to the app information set
        this.ais.addName(new Locale(defaultNameList.get(0)[1]), defaultNameList.get(0)[0].replaceAll("\t", "")
                .replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            this.ais.addName(new Locale(nameArray[1]), nameArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        this.ais.addDescription(new Locale(defaultDescriptionList.get(0)[1]), defaultDescriptionList.get(0)[0]
                .replaceAll("\t", "").replaceAll("\n", " ").trim());
        
        for (String[] descriptionArray : descriptionList) {
            this.ais.addDescription(new Locale(descriptionArray[1]), descriptionArray[0].replaceAll("\t", "")
                    .replaceAll("\n", " ").trim());
        }
        
    }
    
    
    /**
     * This method parses the service features element
     * 
     * @param serviceFeaturesElement
     *            starting with this root element
     */
    private void parseServiceFeaturesNode(Element serviceFeaturesElement) {
        NodeList serviceFeaturesNodeList = serviceFeaturesElement.getElementsByTagName("serviceFeature");
        
        // Check, if there is at least one Service Feature defined
        if (serviceFeaturesNodeList.getLength() == 0) {
            throw new XMLParserException(Type.SERVICE_FEATURE_MISSING,
                    "At least one Service Feature has to be defined.");
        }
        
        // Check, if there is a correct number of child nodes of serviceFeaturesElement
        int expectedNumber = serviceFeaturesNodeList.getLength();
        checkNumberOfNodes(expectedNumber, serviceFeaturesElement);
        
        // Parse the defined Service Features
        for (int itr = 0; itr < serviceFeaturesNodeList.getLength(); itr++) {
            parseOneServiceFeature((Element) serviceFeaturesNodeList.item(itr));
        }
    }
    
    
    /**
     * This method parses one service feature
     * 
     * @param serviceFeatureElement
     *            starting with this root element
     */
    private void parseOneServiceFeature(Element serviceFeaturesElement) {
        
        // Get the identifier
        String identifier = serviceFeaturesElement.getAttribute("identifier");
        
        // Create results
        List<String[]> defaultNameList = parseNodes(serviceFeaturesElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(serviceFeaturesElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(serviceFeaturesElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(serviceFeaturesElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the service feature node
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
        
        // Add to the app information set
        ServiceFeature sf = new ServiceFeature();
        this.ais.addServiceFeature(identifier, sf);
        
        sf.addName(new Locale(defaultNameList.get(0)[1]),
                defaultNameList.get(0)[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            sf.addName(new Locale(nameArray[1]), nameArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        sf.addDescription(new Locale(defaultDescriptionList.get(0)[1]),
                defaultDescriptionList.get(0)[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        for (String[] descriptionArray : descriptionList) {
            sf.addDescription(new Locale(descriptionArray[1]),
                    descriptionArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
        }
        
        // Get the node list of the required resource groups
        NodeList rrgNodeList = serviceFeaturesElement.getElementsByTagName("requiredResourceGroup");
        
        // Check, if there is a correct number of child nodes of serviceFeaturesElement
        int expectedNumber = defaultNameList.size() + nameList.size() + defaultDescriptionList.size() + descriptionList.size() + rrgNodeList.getLength();
        checkNumberOfNodes(expectedNumber, serviceFeaturesElement);
        
        // Check, if there is at least one required Resourcegroup defined
        if (rrgNodeList.getLength() == 0) {
            throw new XMLParserException(Type.REQUIRED_RESOURCE_GROUP_MISSING,
                    "At least one required Resourcegroup for each Service Feature has to be defined.");
        }
        
        // Parse all required resource groups
        for (int rrgItr = 0; rrgItr < rrgNodeList.getLength(); rrgItr++) {
            Element rrgElement = (Element) rrgNodeList.item(rrgItr);
            
            // Get the identifier and validate it
            String rrgIdentifier = rrgElement.getAttribute("identifier");
            validateIdentifier(rrgIdentifier);
            
            // Add to the app information set (building objects)
            RequiredResourceGroup rrg = new RequiredResourceGroup();
            sf.addRequiredResourceGroup(rrgIdentifier, rrg);
            
            // Parse the required resource group
            parseOneRequiredResourceGroup(rrgElement, rrg);
            
        }
        
    }
    
    
    /**
     * This method is used to are one required resource group element.
     * 
     * @param requiredResourceGroupElement
     *            the element of the required resource group
     * @param rrg
     *            The required resource group object, which is assign to the
     *            service feature.
     */
    private void parseOneRequiredResourceGroup(Element requiredResourceGroupElement, RequiredResourceGroup rrg) {
        // Create result
        List<String[]> privacySettingList = parseNodes(requiredResourceGroupElement, "privacySetting", 1,
                Integer.MAX_VALUE, "identifier");
        
        // Check, if there is a correct number of child nodes of requiredResourceGroupElement
        int expectedNumber = privacySettingList.size();
        checkNumberOfNodes(expectedNumber, requiredResourceGroupElement);
        
        // Check, if there is at least one Privacy Setting defined
        if (privacySettingList.size() == 0) {
            throw new XMLParserException(Type.PRIVACY_SETTING_MISSING,
                    "At least one Privacy Setting for each required Resourcegroup has to be defined.");
        }
        
        // Add to the app information set (building objects)
        for (String[] privacySettingArray : privacySettingList) {
            
            // Get identifier and value
            String identifier = privacySettingArray[1];
            String value = privacySettingArray[0];
            
            // Validate identifier and value
            validateIdentifier(identifier);
            validateValueNotEmpty(value);
            
            // Add identifier and value
            rrg.addPrivacySetting(identifier, value);
        }
    }
    
}
