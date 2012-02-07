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

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class AISCompiler extends BasicISCompiler {
    
    /**
     * Compile an AIS and return the xml input stream
     * 
     * @param ais
     *            AIS to compile
     * @return xml input stream
     */
    public InputStream compile(AIS ais) {
        
        // Instantiate the root and two main child nodes
        XMLNode aisNode = new XMLNode(XMLConstants.AIS);
        XMLNode aiNode = new XMLNode(XMLConstants.AI);
        XMLNode sfsNode = new XMLNode(XMLConstants.SFS);
        
        // Add appInformation childs
        for (XMLNode nameDescrNode : createNameDescriptionNodes(ais)) {
            aiNode.addChild(nameDescrNode);
        }
        
        // Add serviceFeature childs
        for (XMLNode sfNode : createSFNodes(ais)) {
            sfsNode.addChild(sfNode);
        }
        
        // Add appInformation and serviceFeature to AIS
        aisNode.addChild(aiNode);
        aisNode.addChild(sfsNode);
        
        // Compile and return the result
        InputStream result = null;
        try {
            result = XMLCompiler.compileStream(aisNode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * Create the service feature node list
     * 
     * @param ais
     *            AIS
     * @return service feature node list
     */
    private List<XMLNode> createSFNodes(AIS ais) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (AISServiceFeature sf : ais.getServiceFeatures()) {
            XMLNode sfNode = new XMLNode(XMLConstants.SF);
            
            // Add identifier
            sfNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, sf.getIdentifier()));
            
            // Add the name and description nodes
            for (XMLNode nameDescrNode : createNameDescriptionNodes(sf)) {
                sfNode.addChild(nameDescrNode);
            }
            
            // Add required resource group nodes
            for (XMLNode rrgNode : createRRGNodes(sf)) {
                sfNode.addChild(rrgNode);
            }
            
            // Add SF to nodeList
            nodeList.add(sfNode);
        }
        
        return nodeList;
        
    }
    
    
    /**
     * Create the required resource group node list
     * 
     * @param sf
     *            SF of the rrgs
     * @return node list of required resource groups
     */
    private List<XMLNode> createRRGNodes(AISServiceFeature sf) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (AISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
            XMLNode rrgNode = new XMLNode(XMLConstants.RRG);
            
            // Add identifier and minRevision
            rrgNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, rrg.getIdentifier()));
            rrgNode.addAttribute(new XMLAttribute(XMLConstants.MINREVISION_ATTR, rrg.getMinRevision()));
            
            // Add required privacy settings
            for (AISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
                XMLNode rpsNode = new XMLNode(XMLConstants.RPS);
                rpsNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, rps.getIdentifier()));
                rpsNode.setCDATAContent(rps.getValue());
                rrgNode.addChild(rpsNode);
            }
            
            // Add the rrgNode to the nodeList
            nodeList.add(rrgNode);
        }
        return nodeList;
    }
}