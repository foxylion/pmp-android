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

import de.unistuttgart.ipvs.pmp.xmlutil.common.XMLConstants;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException.Type;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

/**
 * This class abstracts common used methods for the xml parsers of an app or
 * resourcegroup
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractParser {
    
    /**
     * The XML document
     */
    protected Document doc = null;
    
    
    /**
     * Initialize the parser
     * 
     * @param xmlStream
     *            the input stream
     */
    protected void initParser(InputStream xmlStream) {
        // Check if the xmlStream is null
        if (xmlStream == null) {
            throw new ParserException(Type.NULL_XML_STREAM, "The xml input stream was null.");
        }
        
        try {
            // Instantiate the document builder and get the document
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            this.doc = db.parse(xmlStream);
            this.doc.getDocumentElement().normalize();
            
        } catch (ParserConfigurationException e) {
            throw new ParserException(Type.CONFIGURATION_EXCEPTION, "ParserConfigurationException", e);
        } catch (SAXException e) {
            throw new ParserException(Type.SAX_EXCEPTION, "SAXException", e);
        } catch (IOException e) {
            throw new ParserException(Type.IO_EXCEPTION, "IOException", e);
        }
    }
    
    
    /**
     * This method is used to parse nodes directly. (optional with given
     * attributes). You can adjust the expected occurrences by giving a range of
     * occurrences.
     * 
     * @param rootElement
     *            Root element of all nodes
     * @param nodeName
     *            Name of the node
     * @param attributeNames
     *            the names for the attributes (optional)
     * @return The list contains one array for each parsed node. The values of
     *         the arrays are defined as follow: Array[0] = value of this node
     *         Array[1..i] = values of the given attributes
     */
    protected List<String[]> parseNodes(Element rootElement, String nodeName, String... attributeNames) {
        
        // Get the node list
        NodeList nodeList = rootElement.getElementsByTagName(nodeName);
        
        // Instantiate the result list array
        List<String[]> resultListArray = new ArrayList<String[]>();
        
        // iterate through all nodes
        for (int nodeListItr = 0; nodeListItr < nodeList.getLength(); nodeListItr++) {
            // Get the element
            Element element = (Element) nodeList.item(nodeListItr);
            
            // Build the result array
            int resultArrayLength = attributeNames.length + 1;
            String[] resultArray = new String[resultArrayLength];
            
            // Init
            resultArray[0] = "";
            
            // iterate through the node values
            for (int itemItr = 0; itemItr < element.getChildNodes().getLength(); itemItr++) {
                resultArray[0] = resultArray[0] + element.getChildNodes().item(itemItr).getNodeValue();
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
     * This method parses the nodes called "name" and "description" of the given
     * root element and adds the results to the given BasicIS.
     * 
     * @param rootElement
     *            starting with this root element
     * @param is
     *            add results to this basic information set
     */
    protected void parseNameDescriptionNodes(Element rootElement, BasicIS is) {
        // Create results
        List<String[]> nameList = parseNodes(rootElement, XMLConstants.NAME, XMLConstants.LANGUAGE_ATTRIBUTE);
        List<String[]> descriptionList = parseNodes(rootElement, XMLConstants.DESCRIPTION,
                XMLConstants.LANGUAGE_ATTRIBUTE);
        
        // Add to the app information set
        for (String[] nameArray : nameList) {
            Name name = new Name();
            name.setLocale(new Locale(nameArray[1]));
            name.setName(nameArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
            is.addName(name);
        }
        for (String[] descriptionArray : descriptionList) {
            Description descr = new Description();
            descr.setLocale(new Locale(descriptionArray[1]));
            descr.setDescription(descriptionArray[0].replaceAll("\t", "").replaceAll("\n", " ").trim());
            is.addDescription(descr);
        }
    }
    
    
    /**
     * This methods checks, if a parent has exactly the number of child nodes
     * expected by the parameter "expectedNumber"
     * 
     * @param expectedNumber
     *            expected number of occurrences of child nodes of the given
     *            parent (root element)
     * @param rootElement
     *            the root element to check it's children
     */
    public void checkNumberOfNodes(int expectedNumber, Element rootElement) {
        int numberOfNodes = 0;
        
        for (int itr = 0; itr < rootElement.getChildNodes().getLength(); itr++) {
            if (rootElement.getChildNodes().item(itr).getNodeType() == Node.ELEMENT_NODE) {
                numberOfNodes++;
            }
        }
        
        if (expectedNumber < numberOfNodes) {
            throw new ParserException(Type.UNEXPECTED_NODE, "Unexpected node found. It's parent is "
                    + rootElement.getNodeName());
        } else if (expectedNumber > numberOfNodes) {
            throw new ParserException(Type.NODE_MISSING, "There is at least one node missing. It's parent is "
                    + rootElement.getNodeName());
        }
    }
    
}
