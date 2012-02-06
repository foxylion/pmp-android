package de.unistuttgart.ipvs.pmp.xmlutil.compiler;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class BasicISCompiler {
    
    /**
     * Create a list of XMLNodes with names and descriptions from a BasicIS
     * 
     * @param basicIS
     *            basicIS with data
     * @return list of XMLNodes
     */
    public List<XMLNode> createNameDescriptionNodes(BasicIS basicIS) {
        
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        // Compile the names
        for (Name name : basicIS.getNames()) {
            XMLNode nameNode = new XMLNode(XMLConstants.NAME);
            nameNode.setContent(name.getName());
            nameNode.addAttribute(new XMLAttribute(XMLConstants.LANGUAGE_ATTRIBUTE, name.getLocale().getLanguage()));
            nodeList.add(nameNode);
        }
        
        // Compile the descriptions
        for (Description descr : basicIS.getDescriptions()) {
            XMLNode descrNode = new XMLNode(XMLConstants.DESCRIPTION);
            descrNode.setContent(descr.getDescription());
            descrNode.addAttribute(new XMLAttribute(XMLConstants.LANGUAGE_ATTRIBUTE, descr.getLocale().getLanguage()));
            nodeList.add(descrNode);
        }
        
        return nodeList;
    }
}
