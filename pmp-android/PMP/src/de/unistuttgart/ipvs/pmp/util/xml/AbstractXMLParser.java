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
package de.unistuttgart.ipvs.pmp.util.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.unistuttgart.ipvs.pmp.util.xml.XMLParserException.Type;

/**
 * This class abstracts common used methods for the xml parsers of an app or resourcegroup
 * 
 * @author Marcus Vetter
 * 
 */
public abstract class AbstractXMLParser {

	/**
	 * This method is used to parse nodes directly. (optional with given
	 * attributes). You can adjust the expected occurrences by giving a range of
	 * occurrences.
	 * 
	 * @param rootElement
	 *            Root element of all nodes
	 * @param nodeName
	 *            Name of the node
	 * @param minNodes
	 *            expected occurrences of the given node name (0 or 1 supported)
	 * @param maxNodes
	 *            expected occurrences of the given node name (1 or
	 *            Integer.MAX_VALUE supported)
	 * @param attributeNames
	 *            the names for the attributes (optional)
	 * @return The list contains one array for each parsed node. The values of
	 *         the arrays are defined as follow: Array[0] = value of this node
	 *         Array[1..i] = values of the given attributes
	 */
	protected List<String[]> parseNodes(Element rootElement, String nodeName,
			int minNodes, int maxNodes, String... attributeNames) {

		// Get the node list
		NodeList nodeList = rootElement.getElementsByTagName(nodeName);

		// Instantiate the result list array
		List<String[]> resultListArray = new ArrayList<String[]>();

		// Check the cardinalities
		if (minNodes == 1 && maxNodes == 1) {
			// Check, if there is exact one node
			if (nodeList.getLength() < 1) {
				throw new XMLParserException(Type.NODE_MISSING, "The node  "
						+ nodeName + " is missing!");
			} else if (nodeList.getLength() > 1) {
				throw new XMLParserException(Type.NODE_OCCURRED_TOO_OFTEN,
						"The node " + nodeName + " occurred too often!");
			}
		} else if (maxNodes == Integer.MAX_VALUE && minNodes == 1) {
			// Not restricted, but check, if there is at least one node
			if (!(nodeList.getLength() >= 1)) {
				throw new XMLParserException(Type.NODE_MISSING, "The node  "
						+ nodeName + " is missing!");
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
			if (nodeName.equals("privacySetting")) {
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
				resultArray[attrItr] = element
						.getAttribute(attributeNames[attrItr - 1]);
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
	protected void validateLocaleAttribute(List<String[]> nodeResultList) {
		// Check all nodes
		for (String[] nodeArray : nodeResultList) {
			// Check, if the locale is missing
			if (nodeArray[1].equals("")) {
				throw new XMLParserException(Type.LOCALE_MISSING,
						"The locale of " + nodeArray[0] + " is missing!");
			}
			// Check, if the locale is valid
			if (!checkLocale(nodeArray[1])) {
				throw new XMLParserException(Type.LOCALE_INVALID, "The locale "
						+ nodeArray[1] + " of " + nodeArray[0] + " is invalid!");
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