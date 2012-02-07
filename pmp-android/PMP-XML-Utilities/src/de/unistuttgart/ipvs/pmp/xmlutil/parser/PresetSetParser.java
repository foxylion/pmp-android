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

import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParsedNode;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.Preset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * This class parses a given xml file and creates a PresetSet
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetSetParser extends AbstractParser {
    
    /**
     * PresetSet
     */
    private PresetSet presetSet;
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created preset set
     * 
     * @return created preset set
     */
    public PresetSet parse(InputStream xmlStream) {
        // Initialize
        initParser(xmlStream);
        
        // Create new AIS
        this.presetSet = new PresetSet();
        
        // Check, if the root node is named correctly
        if (!this.doc.getDocumentElement().getNodeName().equals(XMLConstants.PRESET_SET)) {
            throw new ParserException(Type.BAD_ROOT_NODE_NAME, "The name of the root node is invalid.");
        }
        
        // The main nodes called "preset", parse them
        NodeList presetNodes = this.doc.getElementsByTagName(XMLConstants.PRESET);
        for (int itr = 0; itr < presetNodes.getLength(); itr++) {
            parsePreset((Element) presetNodes.item(itr));
        }
        
        return this.presetSet;
    }
    
    
    /**
     * Parse one preset
     * 
     * @param presetElement
     */
    private void parsePreset(Element presetElement) {
        
        // Get the attributes
        String identifier = presetElement.getAttribute(XMLConstants.IDENTIFIER_ATTR);
        String creator = presetElement.getAttribute(XMLConstants.CREATOR_ATTR);
        String name = presetElement.getAttribute(XMLConstants.NAME_ATTR);
        String description = presetElement.getAttribute(XMLConstants.DESCRIPTION_ATTR);
        
        // Instantiate new Preset
        Preset preset = new Preset(identifier, creator, name, description);
        
        // Get the assignedApps-NodeList and parse the elements
        NodeList assignedAppsNodeList = presetElement.getElementsByTagName(XMLConstants.ASSIGNED_APPS);
        for (int itr = 0; itr < assignedAppsNodeList.getLength(); itr++) {
            // Parse the assignedAppsElement
            parseAssignedApps((Element) assignedAppsNodeList.item(itr), preset);
        }
        
        // Get the assignedPrivacySettings-NodeList and parse the elements
        NodeList assignedPSsNodeList = presetElement.getElementsByTagName(XMLConstants.ASSIGNED_PRIVACY_SETTINGS);
        for (int itr = 0; itr < assignedPSsNodeList.getLength(); itr++) {
            // Parse the assignedPrivacySettingsElement
            parseAssignedPSs((Element) assignedPSsNodeList.item(itr), preset);
        }
        
        // Add preset to Preset Set
        presetSet.addPreset(preset);
    }
    
    
    /**
     * Parse assigned apps
     * 
     * @param assignedAppElement
     *            the element of the assigned apps
     * @param preset
     *            the preset
     */
    private void parseAssignedApps(Element assignedAppElement, Preset preset) {
        // Get the list of all assigned apps
        List<ParsedNode> assignedAppsNodes = parseNodes(assignedAppElement, XMLConstants.APP,
                XMLConstants.IDENTIFIER_ATTR);
        
        // Add all assigned apps to the preset
        for (ParsedNode appNode : assignedAppsNodes) {
            preset.addAssignedApp(new PresetAssignedApp(appNode.getAttribute(XMLConstants.IDENTIFIER_ATTR)));
        }
    }
    
    
    /**
     * Parse assigned privacy settings
     * 
     * @param assignedPSsElement
     *            the element of the assigned privacy settings
     * @param preset
     *            the preset
     */
    private void parseAssignedPSs(Element assignedPSsElement, Preset preset) {
        // Get the list of all assigned privacy settings
        List<ParsedNode> assignedPSNodes = parseNodes(assignedPSsElement, XMLConstants.PRIVACY_SETTING,
                XMLConstants.RG_IDENTIFIER_ATTR, XMLConstants.RG_REIVION_ATTR, XMLConstants.PS_IDENTIFIER_ATTR);
        
        // Add all assigned privacy settings to the preset and parse the contexts
        int psItr = 0;
        for (ParsedNode psNode : assignedPSNodes) {
            // The PS element
            Element psElement = (Element) assignedPSsElement.getElementsByTagName(XMLConstants.PRIVACY_SETTING).item(
                    psItr);
            
            // Get the attributes
            String rgIdentifier = psNode.getAttribute(XMLConstants.RG_IDENTIFIER_ATTR);
            String rgRevision = psNode.getAttribute(XMLConstants.RG_REIVION_ATTR);
            String psIdentifier = psNode.getAttribute(XMLConstants.PS_IDENTIFIER_ATTR);
            
            // Instantiate and add the PresetAssignedPrivacySetting
            PresetAssignedPrivacySetting assignedPS = new PresetAssignedPrivacySetting(rgIdentifier, rgRevision,
                    psIdentifier, "");
            preset.addAssignedPrivacySetting(assignedPS);
            
            // Get the value
            List<ParsedNode> valueList = parseNodes(psElement, XMLConstants.VALUE);
            for (ParsedNode valueNode : valueList) {
                assignedPS.setValue(valueNode.getValue());
            }
            
            // Parse the contexts
            parseContexts(psElement, assignedPS);
            
            psItr++;
        }
    }
    
    
    /**
     * Parse a context
     * 
     * @param psElement
     *            the element of the privacy setting
     * @param assignedPS
     *            the assigned privacy setting object
     */
    private void parseContexts(Element psElement, PresetAssignedPrivacySetting assignedPS) {
        // Get the list of all assigned privacy settings
        List<ParsedNode> contextLists = parseNodes(psElement, XMLConstants.CONTEXT, XMLConstants.CONTEXT_TYPE_ATTR,
                XMLConstants.CONTEXT_CONDITION_ATTR);
        
        // Add all contexts
        int contextItr = 0;
        for (ParsedNode contextNode : contextLists) {
            // The context element
            Element contextElement = (Element) psElement.getElementsByTagName(XMLConstants.CONTEXT).item(contextItr);
            
            // Get the attributes
            String type = contextNode.getAttribute(XMLConstants.CONTEXT_TYPE_ATTR);
            String condition = contextNode.getAttribute(XMLConstants.CONTEXT_CONDITION_ATTR);
            
            // Instantiate and add the PresetPSContext
            PresetPSContext context = new PresetPSContext(type, condition, "");
            assignedPS.addContext(context);
            
            // Get the override value
            List<ParsedNode> overrideValueList = parseNodes(contextElement, XMLConstants.CONTEXT_OVERRIDE_VALUE);
            for (ParsedNode valueNode : overrideValueList) {
                context.setOverrideValue(valueNode.getValue());
            }
        }
    }
    
}
