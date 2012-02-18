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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParsedNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException.Type;

/**
 * This XML Parser parses a given xml (for an app) and creates a app information
 * set
 * 
 * @author Marcus Vetter
 * 
 */
public class AISParser extends AbstractParser {
    
    /**
     * AppInformationSet
     */
    private IAIS ais;
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created app
     * information set
     * 
     * @return created app information set
     */
    public IAIS parse(InputStream xmlStream) {
        // Initialize
        initParser(xmlStream);
        
        // Create new AIS
        this.ais = new AIS();
        
        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals(XMLConstants.AIS)) {
            throw new ParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        }
        
        // The main nodes "appInformation" and "serviceFeatures" are
        // required once.
        NodeList appInformation = this.doc.getElementsByTagName(XMLConstants.AI);
        NodeList serviceFeatures = this.doc.getElementsByTagName(XMLConstants.SFS);
        
        // Check, if there is maximal one appInformation and one service
        // features node
        int maxValid = 0;
        if (appInformation.getLength() == 1) {
            // Parse the app information node
            parseNameDescriptionNodes((Element) appInformation.item(0), this.ais);
            maxValid++;
        } else if (appInformation.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node appInformation occurred too often!");
        }
        if (serviceFeatures.getLength() == 1) {
            // Parse the service features node
            parseServiceFeaturesNode((Element) serviceFeatures.item(0));
            maxValid++;
        } else if (serviceFeatures.getLength() > 1) {
            throw new ParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node serviceFeatures occurred too often!");
        }
        
        // Check, if there are a maximum of maxValid child nodes of the root node
        checkMaxNumberOfNodes(maxValid, (Element) this.doc.getElementsByTagName(XMLConstants.AIS).item(0));
        
        return this.ais;
    }
    
    
    /**
     * This method parses the service features element
     * 
     * @param serviceFeaturesElement
     *            starting with this root element
     */
    private void parseServiceFeaturesNode(Element serviceFeaturesElement) {
        NodeList serviceFeaturesNodeList = serviceFeaturesElement.getElementsByTagName(XMLConstants.SF);
        
        // Parse the defined Service Features
        for (int itr = 0; itr < serviceFeaturesNodeList.getLength(); itr++) {
            Element serviceFeatureElement = (Element) serviceFeaturesNodeList.item(itr);
            
            // Get the identifier
            String identifier = serviceFeatureElement.getAttribute(XMLConstants.IDENTIFIER_ATTR);
            
            // Instantiate the service feature and add it to the AIS
            AISServiceFeature sf = new AISServiceFeature(identifier);
            this.ais.addServiceFeature(sf);
            
            // Parse name and descriptions
            parseNameDescriptionNodes(serviceFeatureElement, sf);
            
            // Get the node list of the required resource groups
            NodeList rrgNodeList = serviceFeatureElement.getElementsByTagName(XMLConstants.RRG);
            
            // Parse all required resource groups
            for (int rrgItr = 0; rrgItr < rrgNodeList.getLength(); rrgItr++) {
                Element rrgElement = (Element) rrgNodeList.item(rrgItr);
                
                // Instantiate the required resource group and add the
                // identifier and min revision
                
                // If the min revision is a simple date format, convert it into an integer (seconds)
                String minRevision = rrgElement.getAttribute(XMLConstants.MINREVISION_ATTR);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                try {
                    Date date = sdf.parse(minRevision);
                    minRevision = String.valueOf(date.getTime() / 1000);
                } catch (ParseException e) {
                    // The parse exception can be ignored.
                    // If the time was in another format, the validator will find it.
                }
                
                AISRequiredResourceGroup rrg = new AISRequiredResourceGroup(
                        rrgElement.getAttribute(XMLConstants.IDENTIFIER_ATTR), minRevision);
                
                // Add the required resource group to the service feature
                sf.addRequiredResourceGroup(rrg);
                
                // Parse the required resource group
                List<ParsedNode> privacySettingList = parseNodes(rrgElement, XMLConstants.RPS,
                        XMLConstants.IDENTIFIER_ATTR);
                
                // Add to the app information set (building objects)
                for (ParsedNode privacySettingNode : privacySettingList) {
                    // Add identifier and value
                    rrg.addRequiredPrivacySetting(new AISRequiredPrivacySetting(privacySettingNode
                            .getAttribute(XMLConstants.IDENTIFIER_ATTR), privacySettingNode.getValue()));
                }
            }
        }
    }
}
