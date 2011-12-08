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
        
        parseAppInformationNode((Element) appInformation.item(0));
        parseServiceFeaturesNode((Element) serviceFeatures.item(0));
        
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
        
        // Validate the service level node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
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
        
        // Parse all required resource groups
        for (int rrgItr = 0; rrgItr < rrgNodeList.getLength(); rrgItr++) {
            Element rrgElement = (Element) rrgNodeList.item(rrgItr);
            
            // Add to the app information set (building objects)
            RequiredResourceGroup rrg = new RequiredResourceGroup(rrgElement.getAttribute("identifier"));
            sf.addRequiredResourceGroup(rrg);
            
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
        
        // Add to the app information set (building objects)
        for (String[] privacySettingArray : privacySettingList) {
            rrg.addPrivacySetting(privacySettingArray[1], privacySettingArray[0]);
        }
    }
    
}
