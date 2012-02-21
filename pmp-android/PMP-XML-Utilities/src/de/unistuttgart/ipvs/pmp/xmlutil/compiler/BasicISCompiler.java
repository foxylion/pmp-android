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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.IBasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class BasicISCompiler {
    
    public static final SimpleDateFormat REVISION_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    
    
    /**
     * Create a list of XMLNodes with names and descriptions from a BasicIS
     * 
     * @param basicIS
     *            basicIS with data
     * @return list of XMLNodes
     */
    public List<XMLNode> createNameDescriptionNodes(IBasicIS basicIS) {
        
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        // Compile the names
        for (ILocalizedString name : basicIS.getNames()) {
            XMLNode nameNode = new XMLNode(XMLConstants.NAME);
            nameNode.setContent(name.getString());
            nameNode.addAttribute(new XMLAttribute(XMLConstants.LANGUAGE_ATTR, name.getLocale().getLanguage()));
            nodeList.add(nameNode);
        }
        
        // Compile the descriptions
        for (ILocalizedString descr : basicIS.getDescriptions()) {
            XMLNode descrNode = new XMLNode(XMLConstants.DESCRIPTION);
            descrNode.setContent(descr.getString());
            descrNode.addAttribute(new XMLAttribute(XMLConstants.LANGUAGE_ATTR, descr.getLocale().getLanguage()));
            nodeList.add(descrNode);
        }
        
        return nodeList;
    }
}
