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
package de.unistuttgart.ipvs.pmp.xmlutil.compiler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISRequiredResourceGroup;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAISServiceFeature;
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
    public InputStream compile(IAIS ais) {
        
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
    private List<XMLNode> createSFNodes(IAIS ais) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (IAISServiceFeature sf : ais.getServiceFeatures()) {
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
    private List<XMLNode> createRRGNodes(IAISServiceFeature sf) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (IAISRequiredResourceGroup rrg : sf.getRequiredResourceGroups()) {
            XMLNode rrgNode = new XMLNode(XMLConstants.RRG);
            
            // Add identifier
            rrgNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, rrg.getIdentifier()));
            
            // Add minRevision and use the simple date formatter
            String minRevision = rrg.getMinRevision();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            try {
                Date date = new Date(Long.valueOf(minRevision) * 1000);
                minRevision = sdf.format(date);
            } catch (NumberFormatException nfe) {
                // Ignore it. Something went wrong and the min revision was not an integer.
            }
            rrgNode.addAttribute(new XMLAttribute(XMLConstants.MINREVISION_ATTR, minRevision));
            
            // Add required privacy settings
            for (IAISRequiredPrivacySetting rps : rrg.getRequiredPrivacySettings()) {
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
