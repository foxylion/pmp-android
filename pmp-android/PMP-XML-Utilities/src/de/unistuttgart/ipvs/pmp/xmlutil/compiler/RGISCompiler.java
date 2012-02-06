package de.unistuttgart.ipvs.pmp.xmlutil.compiler;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLAttribute;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLCompiler;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.common.XMLNode;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class RGISCompiler extends BasicISCompiler {
    
    /**
     * Compile an RGIS and return the xml input stream
     * 
     * @param rgis
     *            RGIS to compile
     * @return xml input stream
     */
    public InputStream compile(RGIS rgis) {
        
        // Instantiate the root and two main child nodes
        XMLNode rgisNode = new XMLNode(XMLConstants.RGIS);
        XMLNode rgiNode = new XMLNode(XMLConstants.RGI);
        XMLNode pssNode = new XMLNode(XMLConstants.PSS);
        
        // Add attributes to the rgiNode
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTRIBUTE, rgis.getIdentifier()));
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.ICON_ATTRIBUTE, rgis.getIconLocation()));
        rgiNode.addAttribute(new XMLAttribute(XMLConstants.CLASS_NAME_ATTRIBUTE, rgis.getClassName()));
        
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
     * Create the privacy setting node list
     * 
     * @param rgis
     *            RGIS
     * @return privacy setting node list
     */
    private List<XMLNode> createPSNodes(RGIS rgis) {
        List<XMLNode> nodeList = new ArrayList<XMLNode>();
        
        for (RGISPrivacySetting ps : rgis.getPrivacySettings()) {
            XMLNode psNode = new XMLNode(XMLConstants.PS);
            
            // Add identifier
            psNode.addAttribute(new XMLAttribute(XMLConstants.IDENTIFIER_ATTRIBUTE, ps.getIdentifier()));
            psNode.addAttribute(new XMLAttribute(XMLConstants.VALID_VALUE_DESCRIPTION_ATTRIBUTE, ps
                    .getValidValueDescription()));
            
            // Add the name and description nodes
            for (XMLNode nameDescrNode : createNameDescriptionNodes(ps)) {
                psNode.addChild(nameDescrNode);
            }
            
            // Add PS to nodeList
            nodeList.add(psNode);
        }
        
        return nodeList;
    }
    
}
