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
import java.util.Date;
import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParsedNode;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException.Type;

/**
 * This XML Parser parses a given xml (for an app) and creates an {@link IAIS}
 * 
 * @author Marcus Vetter
 * 
 */
public class AISParser extends AbstractParser {
    
    /**
     * The {@link IAIS}
     */
    private IAIS ais;
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created {@link IAIS}
     * 
     * @return created {@link IAIS}
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
     * This method parses the {@link AISServiceFeature} element
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
                try {
                    // Bugfix for Android 2.1.1: Timezone MEZ is not a valid timezone for this android version.
                    // So replace MEZ with GMT+01:00.+
                    minRevision = minRevision.replace("MEZ", "GMT+01:00");
                    minRevision = minRevision.replace("MESZ", "GMT+02:00");
                    
                    System.out.println("--------------- " + minRevision);
                    
                    Date date = XMLConstants.REVISION_DATE_FORMAT.parse(minRevision);
                    minRevision = String.valueOf(date.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                    // The parse exception can be ignored.
                    // If the time was in another format, the validator will find it.
                }
                
                AISRequiredResourceGroup rrg = new AISRequiredResourceGroup(
                        rrgElement.getAttribute(XMLConstants.IDENTIFIER_ATTR), minRevision);
                
                // Add the required resource group to the service feature
                sf.addRequiredResourceGroup(rrg);
                
                // Parse the required resource group
                List<ParsedNode> privacySettingList = parseNodes(rrgElement, XMLConstants.RPS,
                        XMLConstants.IDENTIFIER_ATTR, XMLConstants.EMPTY_VALUE_ATTR);
                
                // Add to the app information set (building objects)
                for (ParsedNode privacySettingNode : privacySettingList) {
                    // Instantiate the rps with identifier and value
                    IAISRequiredPrivacySetting ps = new AISRequiredPrivacySetting(
                            privacySettingNode.getAttribute(XMLConstants.IDENTIFIER_ATTR),
                            privacySettingNode.getValue());
                    
                    // Add the empty value attribute
                    String evAttribute = privacySettingNode.getAttribute(XMLConstants.EMPTY_VALUE_ATTR);
                    if (evAttribute.toLowerCase().equals("true")) {
                        ps.setEmptyValue(true);
                        // Set the value to "", if it is null
                        if (ps.getValue() == null) {
                            ps.setValue("");
                        }
                    } else if (!evAttribute.toLowerCase().equals("false") && !evAttribute.equals("")) {
                        throw new ParserException(Type.EMPTY_VALUE_BOOLEAN_EXCEPTION,
                                "The value of the attribute \"emptyValue\" of a required Privacy Setting is not a boolean.");
                    }
                    
                    // Add it to the rrg
                    rrg.addRequiredPrivacySetting(ps);
                    
                }
            }
        }
    }
}
