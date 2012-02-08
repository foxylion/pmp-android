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
package de.unistuttgart.ipvs.pmp.xmlutil.compiler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.Preset;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedApp;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetAssignedPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetPSContext;
import de.unistuttgart.ipvs.pmp.xmlutil.presetset.PresetSet;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetSetCompiler extends BasicISCompiler {
    
    /**
     * Compile an PresetSet and return the xml input stream
     * 
     * @param presetSet
     *            presetSet to compile
     * @return xml input stream
     */
    public InputStream compile(PresetSet presetSet) {
        // Instantiate the root node
        XMLNode presetSetNode = new XMLNode(XMLConstants.PRESET_SET);
        
        // Add preset childs
        for (XMLNode presetNode : createPresetNodes(presetSet)) {
            presetSetNode.addChild(presetNode);
        }
        
        // Compile and return the result
        InputStream result = null;
        try {
            result = XMLCompiler.compileStream(presetSetNode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * Create the XMLNode list of the Preset-Nodes
     * 
     * @param presetSet
     *            the PresetSet
     * @return created nodelist
     */
    private List<XMLNode> createPresetNodes(PresetSet presetSet) {
        List<XMLNode> nodes = new ArrayList<XMLNode>();
        
        // Iterate through all presets
        for (Preset preset : presetSet.getPresets()) {
            // Instantiate the XML-Node
            XMLNode presetNode = new XMLNode(XMLConstants.PRESET);
            
            // Add the attributes
            presetNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, preset.getIdentifier()));
            presetNode.addAttribute(new XMLAttribute(XMLConstants.CREATOR_ATTR, preset.getCreator()));
            presetNode.addAttribute(new XMLAttribute(XMLConstants.NAME_ATTR, preset.getName()));
            presetNode.addAttribute(new XMLAttribute(XMLConstants.DESCRIPTION_ATTR, preset.getDescription()));
            
            // Add the assigned apps
            XMLNode assignedApps = new XMLNode(XMLConstants.ASSIGNED_APPS);
            for (PresetAssignedApp app : preset.getAssignedApps()) {
                XMLNode appNode = new XMLNode(XMLConstants.APP);
                appNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, app.getIdentifier()));
                assignedApps.addChild(appNode);
            }
            
            // Add the assigned privacy settings
            XMLNode assignedPrivacySettings = new XMLNode(XMLConstants.ASSIGNED_PRIVACY_SETTINGS);
            for (XMLNode psNode : createPresetAssignedPSs(preset)) {
                assignedPrivacySettings.addChild(psNode);
            }
            
            // Now finally add the childs to the presetNode and add this one to the result list
            presetNode.addChild(assignedApps);
            presetNode.addChild(assignedPrivacySettings);
            nodes.add(presetNode);
        }
        
        return nodes;
        
    }
    
    
    /**
     * Create the XMLNode list of the assigned privacy settings
     * 
     * @param preset
     *            the Preset
     * @return created nodelist
     */
    private List<XMLNode> createPresetAssignedPSs(Preset preset) {
        List<XMLNode> nodes = new ArrayList<XMLNode>();
        
        // Iterate through all privacy settings
        for (PresetAssignedPrivacySetting ps : preset.getAssignedPrivacySettings()) {
            // Instantiate the XML-Node
            XMLNode privacySettingNode = new XMLNode(XMLConstants.PRIVACY_SETTING);
            
            // Add the attributes
            privacySettingNode.addAttribute(new XMLAttribute(XMLConstants.RG_IDENTIFIER_ATTR, ps.getRgIdentifier()));
            privacySettingNode.addAttribute(new XMLAttribute(XMLConstants.RG_REVISION_ATTR, ps.getRgRevision()));
            privacySettingNode.addAttribute(new XMLAttribute(XMLConstants.PS_IDENTIFIER_ATTR, ps.getPsIdentifier()));
            
            // Add the value
            XMLNode valueNode = new XMLNode(XMLConstants.VALUE);
            valueNode.setCDATAContent(ps.getValue());
            
            // Add the contexts
            for (PresetPSContext context : ps.getContexts()) {
                XMLNode contextNode = new XMLNode(XMLConstants.CONTEXT);
                contextNode.addAttribute(new XMLAttribute(XMLConstants.CONTEXT_TYPE_ATTR, context.getType()));
                contextNode.addAttribute(new XMLAttribute(XMLConstants.CONTEXT_CONDITION_ATTR, context.getCondition()));
                
                // Create the overrideValue-Node
                XMLNode overrideValueNode = new XMLNode(XMLConstants.CONTEXT_OVERRIDE_VALUE);
                overrideValueNode.setCDATAContent(context.getOverrideValue());
                contextNode.addChild(overrideValueNode);
                
                // Add it to his parent
                privacySettingNode.addChild(contextNode);
            }
            
            // Now finally add the childs to the AssignedPrivacySettingsNode and add this one to the result list
            privacySettingNode.addChild(valueNode);
            nodes.add(privacySettingNode);
        }
        
        return nodes;
    }
    
}
