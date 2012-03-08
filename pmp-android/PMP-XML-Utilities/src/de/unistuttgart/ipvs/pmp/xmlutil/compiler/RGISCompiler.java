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

import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGISPrivacySetting;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISCompiler extends BasicISCompiler {
    
    /**
     * Compile an {@link IRGIS} and return the xml input stream
     * 
     * @param rgis
     *            {@link IRGIS} to compile
     * @return xml input stream
     */
    public InputStream compile(IRGIS rgis) {
        
        // Instantiate the root and two main child nodes
        XMLNode rgisNode = new XMLNode(XMLConstants.RGIS);
        XMLNode rgiNode = new XMLNode(XMLConstants.RGI);
        XMLNode pssNode = new XMLNode(XMLConstants.PSS);
        
        // Add attributes to the rgiNode
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, rgis.getIdentifier()));
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.ICON_ATTR, rgis.getIconLocation()));
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.CLASS_NAME_ATTR, rgis.getClassName()));
        
        // Add appInformation childs
        for (XMLNode nameDescrNode : createNameDescriptionNodes(rgis)) {
            rgiNode.addChild(nameDescrNode);
        }
        
        // Add serviceFeature childs
        for (XMLNode psNode : createPSNodes(rgis)) {
            pssNode.addChild(psNode);
        }
        
        // Add appInformation and serviceFeature to AIS
        rgisNode.addChild(rgiNode);
        rgisNode.addChild(pssNode);
        
        // Compile and return the result
        InputStream result = null;
        try {
            result = XMLCompiler.compileStream(rgisNode);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * Create the {@link RGISPrivacySetting} node list
     * 
     * @param rgis
     *            {@link IRGIS}
     * @return {@link RGISPrivacySetting} node list
     */
    private List<XMLNode> createPSNodes(IRGIS rgis) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (IRGISPrivacySetting ps : rgis.getPrivacySettings()) {
            XMLNode psNode = new XMLNode(XMLConstants.PRIVACY_SETTING);
            
            // Add identifier
            psNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTR, ps.getIdentifier()));
            psNode.addAttribute(new XMLAttribute(XMLConstants.VALID_VALUE_DESCRIPTION_ATTR, ps
                    .getValidValueDescription()));
            
            // Add the name and description nodes
            for (XMLNode nameDescrNode : createNameDescriptionNodes(ps)) {
                psNode.addChild(nameDescrNode);
            }
            
            // Compile the change descriptions
            for (ILocalizedString changeDescr : ps.getChangeDescriptions()) {
                XMLNode changeDescrNode = new XMLNode(XMLConstants.CHANGE_DESCRIPTION);
                changeDescrNode.setContent(changeDescr.getString());
                changeDescrNode.addAttribute(new XMLAttribute(XMLConstants.LANGUAGE_ATTR, changeDescr.getLocale()
                        .getLanguage()));
                psNode.addChild(changeDescrNode);
            }
            
            // Add PS to nodeList
            nodeList.add(psNode);
        }
        
        return nodeList;
    }
    
}
