package de.unistuttgart.ipvs.pmp.app.xmlparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.app.xmlparser.XMLParserException.Type;

/**
 * This XML Parser parses a given xml (for an app) and creates a app information set
 * 
 * @author Marcus Vetter
 * 
 */
public class XMLParser {
    
    /**
     * AppInformationSet
     */
    private AppInformationSet ais = new AppInformationSet();
    
    
    /**
     * This method parses a given xml (by the xml url) and returns a created app information set
     * 
     * @param xmlURL
     *            url for the xml
     * @return created app information set
     */
    public AppInformationSet parse(InputStream xmlStream) {
        
        try {
            // Instantiate the document builder and get the document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(xmlStream);
            doc.getDocumentElement().normalize();
            
            // The main nodes "appInformation" and "serviceLevels" are required
            // once.
            NodeList appInformation = doc.getElementsByTagName("appInformation");
            NodeList serviceLevels = doc.getElementsByTagName("serviceLevels");
            
            // Check, if there is exactly one appInformation and one service
            // levels node
            if (appInformation.getLength() < 1) {
                throw new XMLParserException(Type.NODE_MISSING, "The node appInformation is missing!");
            } else if (appInformation.getLength() > 1) {
                throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                        "The node appInformation occurred too often!");
            }
            if (serviceLevels.getLength() < 1) {
                throw new XMLParserException(Type.NODE_MISSING, "The node serviceLevels is missing!");
            } else if (serviceLevels.getLength() > 1) {
                throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node serviceLevels occurred too often!");
            }
            
            parseAppInformationNode((Element) appInformation.item(0));
            parseServiceLevelsNode((Element) serviceLevels.item(0));
            
        } catch (ParserConfigurationException e) {
            throw new XMLParserException(Type.CONFIGURATION_EXCEPTION, "ParserConfigurationException", e);
        } catch (SAXException e) {
            throw new XMLParserException(Type.SAX_EXCEPTION, "SAXException", e);
        } catch (IOException e) {
            throw new XMLParserException(Type.IO_EXCEPTION, "IOException", e);
        }
        
        return this.ais;
    }
    
    
    /**
     * This method parses the app information element
     * 
     * @param appInformationElement
     *            starting with this root element
     */
    private void parseAppInformationNode(Element appInformationElement) {
        // Create results
        List<String[]> defaultNameList = parseNodes(appInformationElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(appInformationElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(appInformationElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(appInformationElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the app information node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
        // Add to the app information set
        this.ais.addName(new Locale(defaultNameList.get(0)[1]), defaultNameList.get(0)[0]);
        for (String[] nameArray : nameList) {
            this.ais.addName(new Locale(nameArray[1]), nameArray[0]);
        }
        this.ais.addDescription(new Locale(defaultDescriptionList.get(0)[1]), defaultDescriptionList.get(0)[0]);
        for (String[] descriptionArray : descriptionList) {
            this.ais.addDescription(new Locale(descriptionArray[1]), descriptionArray[0]);
        }
        
    }
    
    
    /**
     * This method parses the service levels element
     * 
     * @param serviceLevelsElement
     *            starting with this root element
     */
    private void parseServiceLevelsNode(Element serviceLevelsElement) {
        /*
         * 1.) Parse the default service level
         */
        parseOneServiceLevel((Element) serviceLevelsElement.getElementsByTagName("defaultServiceLevel").item(0), true);
        
        /*
         * 2.) Parse other service levels, if available
         */
        NodeList serviceLevelNodeList = serviceLevelsElement.getElementsByTagName("serviceLevel");
        for (int itr = 0; itr < serviceLevelNodeList.getLength(); itr++) {
            parseOneServiceLevel((Element) serviceLevelNodeList.item(itr), false);
        }
    }
    
    
    /**
     * This method parses one service level
     * 
     * @param serviceLevelElement
     *            starting with this root element
     * @param defaultServiceLevel
     *            true, if the given service level element is the default service level
     */
    private void parseOneServiceLevel(Element serviceLevelElement, Boolean defaultServiceLevel) {
        // Create results
        List<String[]> levelList = parseNodes(serviceLevelElement, "level", 1, 1);
        List<String[]> defaultNameList = parseNodes(serviceLevelElement, "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(serviceLevelElement, "name", 0, Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(serviceLevelElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(serviceLevelElement, "description", 0, Integer.MAX_VALUE, "lang");
        
        // Validate the service level node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);
        
        // Validate, if the default service level has level number 0
        if (defaultServiceLevel && !(levelList.get(0)[0].equals("0"))) {
            throw new XMLParserException(Type.DEFAULT_SERVICE_LEVEL_IS_NOT_ZERO,
                    "The level number of the default service level is not zero!");
        }
        
        // Add to the app information set
        ServiceLevel sl = new ServiceLevel();
        this.ais.addServiceLevel(Integer.valueOf(levelList.get(0)[0]), sl);
        
        sl.addName(new Locale(defaultNameList.get(0)[1]), defaultNameList.get(0)[0]);
        for (String[] nameArray : nameList) {
            sl.addName(new Locale(nameArray[1]), nameArray[0]);
        }
        sl.addDescription(new Locale(defaultDescriptionList.get(0)[1]), defaultDescriptionList.get(0)[0]);
        for (String[] descriptionArray : descriptionList) {
            sl.addDescription(new Locale(descriptionArray[1]), descriptionArray[0]);
        }
        
        // Get the node list of the required resource groups
        NodeList rrgNodeList = serviceLevelElement.getElementsByTagName("requiredResourceGroup");
        
        if (defaultServiceLevel) {
            if (!(rrgNodeList.getLength() == 0)) {
                throw new XMLParserException(Type.DEFAULT_SERVICE_LEVEL_MUST_HAVE_NO_REQUIRED_RESOURCE_GROUPS,
                        "The default service level must have no required resource groups!");
            }
        } else {
            // Parse all required resource groups
            for (int rrgItr = 0; rrgItr < rrgNodeList.getLength(); rrgItr++) {
                Element rrgElement = (Element) rrgNodeList.item(rrgItr);
                
                // Add to the app information set (building objects)
                RequiredResourceGroup rrg = new RequiredResourceGroup(rrgElement.getAttribute("identifier"));
                sl.addRequiredResourceGroup(rrg);
                
                // Parse the required resource group
                parseOneRequiredResourceGroup(rrgElement, rrg);
                
            }
            
        }
    }
    
    
    /**
     * This method is used to are one required resource group element.
     * 
     * @param requiredResourceGroupElement
     *            the element of the required resource group
     * @param rrg
     *            The required resource group object, which is assign to the service level.
     */
    private void parseOneRequiredResourceGroup(Element requiredResourceGroupElement, RequiredResourceGroup rrg) {
        // Create result
        List<String[]> privacyLevelList = parseNodes(requiredResourceGroupElement, "privacyLevel", 1,
                Integer.MAX_VALUE, "identifier");
        
        // Add to the app information set (building objects)
        for (String[] privacyLevelArray : privacyLevelList) {
            rrg.addPrivacyLevel(privacyLevelArray[1], privacyLevelArray[0]);
        }
    }
    
    
    /**
     * This method is used to parse nodes directly. (optional with given attributes). You can adjust the expected
     * occurrences by giving a range of occurrences.
     * 
     * @param rootElement
     *            Root element of all nodes
     * @param nodeName
     *            Name of the node
     * @param minNodes
     *            expected occurrences of the given node name (0 or 1 supported)
     * @param maxNodes
     *            expected occurrences of the given node name (1 or Integer.MAX_VALUE supported)
     * @param attributeNames
     *            the names for the attributes (optional)
     * @return The list contains one array for each parsed node. The values of the arrays are defined as follow:
     *         Array[0] = value of this node Array[1..i] = values of the given attributes
     */
    private List<String[]> parseNodes(Element rootElement, String nodeName, int minNodes, int maxNodes,
            String... attributeNames) {
        
        // Get the node list
        NodeList nodeList = rootElement.getElementsByTagName(nodeName);
        
        // Instantiate the result list array
        List<String[]> resultListArray = new ArrayList<String[]>();
        
        // Check the cardinalities
        if (minNodes == 1 && maxNodes == 1) {
            // Check, if there is exact one node
            if (nodeList.getLength() < 1) {
                throw new XMLParserException(Type.NODE_MISSING, "The node  " + nodeName + " is missing!");
            } else if (nodeList.getLength() > 1) {
                throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN, "The node " + nodeName
                        + " occurred too often!");
            }
        } else if (maxNodes == Integer.MAX_VALUE && minNodes == 1) {
            // Not restricted, but check, if there is at least one node
            if (!(nodeList.getLength() >= 1)) {
                throw new XMLParserException(Type.NODE_MISSING, "The node  " + nodeName + " is missing!");
            }
        }
        
        // iterate through all nodes
        for (int nodeListItr = 0; nodeListItr < nodeList.getLength(); nodeListItr++) {
            // Get the element
            Element element = (Element) nodeList.item(nodeListItr);
            
            // flag, if the node is a cdata section
            boolean cdataFlag = false;
            
            // Build the result array
            int resultArrayLength = attributeNames.length + 1;
            String[] resultArray = new String[resultArrayLength];
            
            // cdata sections are allowed as content of the privacy level nodes
            if (nodeName.equals("privacyLevel")) {
                for (int i = 0; i < element.getChildNodes().getLength(); i++) {
                    if (element.getChildNodes().item(i).getNodeType() == Node.CDATA_SECTION_NODE) {
                        cdataFlag = true;
                    }
                }
            }
            
            // if its a cdata section, the content is the second item, not the
            // first.
            if (cdataFlag) {
                resultArray[0] = element.getChildNodes().item(1).getNodeValue();
            } else {
                resultArray[0] = element.getChildNodes().item(0).getNodeValue();
            }
            
            // Get the attributes given as parameters
            for (int attrItr = 1; attrItr < resultArrayLength; attrItr++) {
                resultArray[attrItr] = element.getAttribute(attributeNames[attrItr - 1]);
            }
            
            // Add the result array to the result list of arrays
            resultListArray.add(resultArray);
        }
        
        return resultListArray;
    }
    
    
    /**
     * This method validates, if a given locale attribute exists and is valid.
     * 
     * @param nodeResultList
     *            the given node result list array.
     */
    private void validateLocaleAttribute(List<String[]> nodeResultList) {
        // Check all nodes
        for (String[] nodeArray : nodeResultList) {
            // Check, if the locale is missing
            if (nodeArray[1].equals("")) {
                throw new XMLParserException(Type.LOCALE_MISSING, "The locale of " + nodeArray[0] + " is missing!");
            }
            // Check, if the locale is valid
            if (!checkLocale(nodeArray[1])) {
                throw new XMLParserException(Type.LOCALE_MISSING, "The locale " + nodeArray[1] + " of " + nodeArray[0]
                        + " is invalid!");
            }
        }
    }
    
    
    /**
     * Check, if the given locale (as string) is valid.
     * 
     * @param givenLocale
     *            locale to check (as string)
     * @return flag, if the given local is valid or not.
     */
    private boolean checkLocale(String givenLocale) {
        for (Locale local : Locale.getAvailableLocales()) {
            if (String.valueOf(local).equals(givenLocale)) {
                return true;
            }
        }
        return false;
    }
    
}
