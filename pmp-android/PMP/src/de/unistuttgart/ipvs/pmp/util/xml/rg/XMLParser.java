package de.unistuttgart.ipvs.pmp.util.xml.rg;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.util.xml.AbstractXMLParser;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException;
import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

public class XMLParser extends AbstractXMLParser {

    /**
     * RgInformationSet
     */
    private RgInformationSet rgis = new RgInformationSet();

    /**
     * Constructor
     */
    protected XMLParser(InputStream xmlStream) {
        super(xmlStream);
    }

    /**
     * This method parses a given xml (by the xml url) and returns a created
     * resourcegroup information set
     * 
     * @return created app information set
     */
    protected RgInformationSet parse() {

        // The main nodes "resourceGroupInformation" and "privacySettings" are
        // required once.
        NodeList rgInformation = doc
                .getElementsByTagName("resourceGroupInformation");
        NodeList privacySettings = doc.getElementsByTagName("privacySettings");

        // Check, if there is exactly one resourceGroupInformation and one
        // privacySettings node
        if (rgInformation.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING,
                    "The node resourceGroupInformation is missing!");
        } else if (rgInformation.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                    "The node resourceGroupInformation occurred too often!");
        }
        if (privacySettings.getLength() < 1) {
            throw new XMLParserException(Type.NODE_MISSING,
                    "The node privacySettings is missing!");
        } else if (privacySettings.getLength() > 1) {
            throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN,
                    "The node privacySettings occurred too often!");
        }

        parseRgInformationNode((Element) rgInformation.item(0));
        parsePrivacySettingsNode((Element) privacySettings.item(0));

        return this.rgis;
    }
    
    /**
     * This method parses the rg information element
     * 
     * @param rgInformationElement
     *            starting with this root element
     */
    private void parseRgInformationNode(Element rgInformationElement) {
        // Create results
        List<String[]> defaultNameList = parseNodes(rgInformationElement,
                "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(rgInformationElement, "name", 0,
                Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(
                rgInformationElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(rgInformationElement,
                "description", 0, Integer.MAX_VALUE, "lang");

        // Validate the rg information node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);

        // Add to the rg information set
        this.rgis.addName(
                new Locale(defaultNameList.get(0)[1]),
                defaultNameList.get(0)[0].replaceAll("\t", "")
                        .replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            this.rgis.addName(new Locale(nameArray[1]),
                    nameArray[0].replaceAll("\t", "").replaceAll("\n", " ")
                            .trim());
        }
        this.rgis.addDescription(new Locale(defaultDescriptionList.get(0)[1]),
                defaultDescriptionList.get(0)[0].replaceAll("\t", "")
                        .replaceAll("\n", " ").trim());

        for (String[] descriptionArray : descriptionList) {
            this.rgis.addDescription(
                    new Locale(descriptionArray[1]),
                    descriptionArray[0].replaceAll("\t", "")
                            .replaceAll("\n", " ").trim());
        }

    }

    
    /**
     * This method parses the privacy settings element
     * 
     * @param privacySettingsElement
     *            starting with this root element
     */
    private void parsePrivacySettingsNode(Element privacySettingsElement) {
        NodeList privacySettingsNodeList = privacySettingsElement
                .getElementsByTagName("privacySetting");
        for (int itr = 0; itr < privacySettingsNodeList.getLength(); itr++) {
            parseOnePrivacySetting((Element) privacySettingsNodeList.item(itr));
        }
    }
    
    /**
     * This method parses one privacy setting
     * 
     * @param privacySettingElement
     *            starting with this root element
     */
    private void parseOnePrivacySetting(Element privacySettingsElement) {

        // Get the identifier
        String identifier = privacySettingsElement.getAttribute("identifier");

        // Create results
        List<String[]> defaultNameList = parseNodes(privacySettingsElement,
                "defaultName", 1, 1, "lang");
        List<String[]> nameList = parseNodes(privacySettingsElement, "name", 0,
                Integer.MAX_VALUE, "lang");
        List<String[]> defaultDescriptionList = parseNodes(
                privacySettingsElement, "defaultDescription", 1, 1, "lang");
        List<String[]> descriptionList = parseNodes(privacySettingsElement,
                "description", 0, Integer.MAX_VALUE, "lang");

        // Validate the privacy setting node
        validateLocaleAttribute(defaultNameList);
        validateLocaleAttribute(nameList);
        validateLocaleAttribute(defaultDescriptionList);
        validateLocaleAttribute(descriptionList);

        // Add to the rg information set
        PrivacySetting ps = new PrivacySetting();
        this.rgis.addPrivacySetting(identifier, ps);

        ps.addName(
                new Locale(defaultNameList.get(0)[1]),
                defaultNameList.get(0)[0].replaceAll("\t", "")
                        .replaceAll("\n", " ").trim());
        for (String[] nameArray : nameList) {
            ps.addName(new Locale(nameArray[1]),
                    nameArray[0].replaceAll("\t", "").replaceAll("\n", " ")
                            .trim());
        }
        ps.addDescription(new Locale(defaultDescriptionList.get(0)[1]),
                defaultDescriptionList.get(0)[0].replaceAll("\t", "")
                        .replaceAll("\n", " ").trim());
        for (String[] descriptionArray : descriptionList) {
            ps.addDescription(
                    new Locale(descriptionArray[1]),
                    descriptionArray[0].replaceAll("\t", "")
                            .replaceAll("\n", " ").trim());
        }
        
    }
}
