/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
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
package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AndroidApplicationException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AppIdentifierNotFoundException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.NoMainActivityException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPActivityAlreadyExistsException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPServiceAlreadyExists;

/**
 * Parses the AndroidManifest.xml to get the identifier of the app and to put
 * the PMP registration activity to the XML file
 * 
 * @author Thorsten Berberich
 * 
 */
public class AndroidManifestParser {
    
    /**
     * The XML document
     */
    private Document doc = null;
    
    /**
     * Activity node
     */
    private final String ANDROID_ACTIVITY = "activity"; //$NON-NLS-1$
    
    /**
     * Path to the PMP registration activity
     */
    private final String ANDROID_NAME = "android:name"; //$NON-NLS-1$
    
    /**
     * String of the pmp activity
     */
    private final String PMP_ACTIVITY = "de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationActivity"; //$NON-NLS-1$
    
    /**
     * Android intent action main
     */
    private final String ANDROID_ACTION_MAIN = "android.intent.action.MAIN"; //$NON-NLS-1$
    
    /**
     * Style of the PMP registration activity
     */
    private final String ANDROID_THEME = "android:theme"; //$NON-NLS-1$
    
    /**
     * PMP Android theme
     */
    private final String PMP_ANDROID_THEME = "@android:style/Theme.NoTitleBar"; //$NON-NLS-1$
    
    /**
     * Android label attribute
     */
    private final String ANDROID_LABEL = "android:label"; //$NON-NLS-1$
    
    /**
     * Intent-filter node
     */
    private final String ANDROID_INTENT_FILTER = "intent-filter"; //$NON-NLS-1$
    
    /**
     * Android action node
     */
    private final String ANDROID_ACTION = "action"; //$NON-NLS-1$
    
    /**
     * Android category node
     */
    private final String ANDROID_CATEGORY = "category"; //$NON-NLS-1$
    
    /**
     * Android intent category Launcher
     */
    private final String ANDROID_CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"; //$NON-NLS-1$
    
    /**
     * Android meta-data node
     */
    private final String ANDROID_META_DATA = "meta-data"; //$NON-NLS-1$
    private final String ANDROID_VALUE = "android:value"; //$NON-NLS-1$
    private final String ANDROID_META_DATA_MAIN_ACTIVITY = "mainActivity"; //$NON-NLS-1$
    
    /**
     * Android application
     */
    private final String ANDROID_APPLICATION = "application"; //$NON-NLS-1$
    
    /**
     * Android package attribute
     */
    private final String ANDROID_PACKAGE = "package"; //$NON-NLS-1$
    
    /**
     * Android Service
     */
    private final String ANDROID_SERVICE = "service"; //$NON-NLS-1$
    private final String PMP_SERVICE_NAME = "de.unistuttgart.ipvs.pmp.service.app.AppService"; //$NON-NLS-1$
    
    
    /**
     * Protected construct, access only through the {@link AndroidManifestAdapter}
     */
    protected AndroidManifestParser() {
    }
    
    
    /**
     * Gets the AppIdentifier out of an AndroidManifest.xml
     * 
     * @param xmlStream
     *            Stream to the AndroidManifest.xml
     * @return app identifier
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws AppIdentifierNotFoundException
     */
    protected String getAppIdentifier(InputStream xmlStream) throws ParserConfigurationException, SAXException,
            IOException, AppIdentifierNotFoundException {
        if (this.doc == null) {
            instantiate(xmlStream);
        }
        
        // Manifest node
        Node node = this.doc.getDocumentElement();
        
        // Get the attributes to get the package name
        NamedNodeMap attribute = node.getAttributes();
        String identifier = attribute.getNamedItem(this.ANDROID_PACKAGE).getNodeValue();
        if (identifier != null) {
            return identifier;
        } else {
            throw new AppIdentifierNotFoundException("App identifier not found"); //$NON-NLS-1$
        }
        
    }
    
    
    /**
     * Adds the registration activity to the given XML File
     * 
     * @param xmlStream
     *            XML stream to the file
     * @param file
     *            same XML {@link File}
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws PMPActivityAlreadyExistsException
     * @throws NoMainActivityException
     * @throws AppIdentifierNotFoundException
     */
    protected void addPMPActivity(InputStream xmlStream, File file) throws ParserConfigurationException, SAXException,
            IOException, TransformerFactoryConfigurationError, TransformerException, PMPActivityAlreadyExistsException,
            NoMainActivityException, AppIdentifierNotFoundException {
        instantiate(xmlStream);
        
        isPMPActivityExisting();
        
        isMainActivityExisting(xmlStream);
        
        String androidName = ""; //$NON-NLS-1$
        String androidLabel = ""; //$NON-NLS-1$
        String appIdentifier = getAppIdentifier(xmlStream);
        
        Node mainActivityNode = getMainActivityNode();
        Node parentNode = mainActivityNode.getParentNode();
        if (parentNode != null) {
            
            // Get the required information out of the old node
            NamedNodeMap attributes = mainActivityNode.getAttributes();
            Node androidNameNode = attributes.getNamedItem(this.ANDROID_NAME);
            Node androidLabelNode = attributes.getNamedItem(this.ANDROID_LABEL);
            if (androidNameNode != null && androidLabelNode != null) {
                
                androidLabel = androidLabelNode.getNodeValue();
                
                if (androidNameNode.getNodeValue().startsWith(appIdentifier)) {
                    androidName = androidNameNode.getNodeValue();
                } else {
                    androidName = appIdentifier + androidNameNode.getNodeValue();
                }
            } else {
                throw new NullPointerException("android:name or android:label was null"); //$NON-NLS-1$
            }
            
            // Create the new node
            Element newElement = this.doc.createElement(this.ANDROID_ACTIVITY);
            newElement.setAttribute(this.ANDROID_NAME, this.PMP_ACTIVITY);
            newElement.setAttribute(this.ANDROID_LABEL, androidLabel);
            newElement.setAttribute(this.ANDROID_THEME, this.PMP_ANDROID_THEME);
            
            // Create the intent-filter node
            Element newIntentFilter = this.doc.createElement(this.ANDROID_INTENT_FILTER);
            
            // Create the action element
            Element action = this.doc.createElement(this.ANDROID_ACTION);
            action.setAttribute(this.ANDROID_NAME, this.ANDROID_ACTION_MAIN);
            
            // Create the category element
            Element category = this.doc.createElement(this.ANDROID_CATEGORY);
            category.setAttribute(this.ANDROID_NAME, this.ANDROID_CATEGORY_LAUNCHER);
            
            // Append action and category to the intent filter
            newIntentFilter.appendChild(action);
            newIntentFilter.appendChild(category);
            
            // Append the intent filter
            newElement.appendChild(newIntentFilter);
            
            // Meta data node
            Element metaData = this.doc.createElement(this.ANDROID_META_DATA);
            metaData.setAttribute(this.ANDROID_NAME, this.ANDROID_META_DATA_MAIN_ACTIVITY);
            metaData.setAttribute(this.ANDROID_VALUE, androidName);
            
            newElement.appendChild(metaData);
            
            // Remove the intent filters from the old activity
            NodeList activityNodes = mainActivityNode.getChildNodes();
            Element intentFilters = null;
            
            for (int itr = 0; itr < activityNodes.getLength(); itr++) {
                
                // Get the intent filters
                if (activityNodes.item(itr).getNodeName().equals(this.ANDROID_INTENT_FILTER)) {
                    intentFilters = (Element) activityNodes.item(itr);
                    
                    // Get the actions node from the intent-filters
                    NodeList actions = intentFilters.getElementsByTagName(this.ANDROID_ACTION);
                    
                    // Search for the android:name="android.intent.action.MAIN"
                    // attribute and delete it
                    for (int itr2 = 0; itr2 < actions.getLength(); itr2++) {
                        Element actionTest = (Element) actions.item(itr2);
                        if (actionTest.getAttribute(this.ANDROID_NAME).equals(this.ANDROID_ACTION_MAIN)) {
                            intentFilters.removeChild(actionTest);
                            continue;
                        }
                    }
                    
                    // Get the categories from the intent-filters
                    NodeList categories = intentFilters.getElementsByTagName(this.ANDROID_CATEGORY);
                    
                    // Search for the
                    // android:name="android.intent.category.LAUNCHER" attribute
                    // and delete it
                    for (int itr3 = 0; itr3 < categories.getLength(); itr3++) {
                        Element categoryTest = (Element) categories.item(itr3);
                        if (categoryTest.getAttribute(this.ANDROID_NAME).equals(this.ANDROID_CATEGORY_LAUNCHER)) {
                            intentFilters.removeChild(categoryTest);
                            continue;
                        }
                    }
                }
            }
            
            parentNode.appendChild(newElement);
            newElement.normalize();
            this.doc.normalize();
        } else {
            throw new NullPointerException("Node was null"); //$NON-NLS-1$
        }
        
        writeBackChanges(file);
    }
    
    
    /**
     * Returns the {@link Node} that contains the MainActivity IntentFilter
     * 
     * @return {@link Node} that contains the MainActivity or <code>null</code>
     * @throws NoMainActivityException
     */
    private Node getMainActivityNode() throws NoMainActivityException {
        // Get all "action" nodes
        NodeList actions = this.doc.getElementsByTagName(this.ANDROID_ACTION);
        
        // Check the attributes if there is a MAIN activity
        if (actions.getLength() > 0) {
            for (int itr = 0; itr < actions.getLength(); itr++) {
                Node action = actions.item(itr);
                NamedNodeMap attributes = action.getAttributes();
                Node attribute = attributes.getNamedItem(this.ANDROID_NAME);
                if (attribute != null && attribute.getNodeValue().equals(this.ANDROID_ACTION_MAIN)) {
                    Node actionParent = action.getParentNode();
                    return actionParent.getParentNode();
                }
            }
        }
        throw new NoMainActivityException("No main activity found"); //$NON-NLS-1$
    }
    
    
    /**
     * Checks if there is a activity node with the attribute
     * "android:name="de.unistuttgart
     * .ipvs.pmp.api.gui.registration.RegistrationActivity""
     * 
     * @return false if the activity is found, true otherwise
     */
    private void isPMPActivityExisting() throws PMPActivityAlreadyExistsException {
        NodeList activities = this.doc.getElementsByTagName(this.ANDROID_ACTIVITY);
        for (int itr = 0; itr < activities.getLength(); itr++) {
            Element activity = (Element) activities.item(itr);
            if (activity.getAttribute(this.ANDROID_NAME).equals(this.PMP_ACTIVITY)) {
                throw new PMPActivityAlreadyExistsException("PMP registration activity already existing"); //$NON-NLS-1$
            }
        }
    }
    
    
    /**
     * Checks if there is a activity with an action child node that declares
     * android:name="android.intent.action.MAIN"
     * 
     * @param xmlStream
     *            Stream of the XML file
     * @return true if found, false if not
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws NoMainActivityException
     */
    protected void isMainActivityExisting(InputStream xmlStream) throws ParserConfigurationException, SAXException,
            IOException, NoMainActivityException {
        if (this.doc == null) {
            instantiate(xmlStream);
        }
        
        // Get all "action" nodes
        NodeList actions = this.doc.getElementsByTagName("action"); //$NON-NLS-1$
        
        // Check the attributes if there is a MAIN activity
        if (actions.getLength() > 0) {
            for (int itr = 0; itr < actions.getLength(); itr++) {
                Node action = actions.item(itr);
                NamedNodeMap attributes = action.getAttributes();
                Node attribute = attributes.getNamedItem("android:name"); //$NON-NLS-1$
                if (attribute != null && attribute.getNodeValue().equals("android.intent.action.MAIN")) { //$NON-NLS-1$
                    return;
                }
            }
        }
        throw new NoMainActivityException("No main activity declared"); //$NON-NLS-1$
    }
    
    
    /**
     * Instantiates the {@link Document}
     * 
     * @param xmlStream
     *            {@link InputStream} to the XML file
     * @throws ParserConfigurationException
     * @throws IOException
     * @throws SAXException
     */
    private void instantiate(InputStream xmlStream) throws ParserConfigurationException, SAXException, IOException {
        if (xmlStream == null) {
            throw new NullPointerException("XML-stream was null"); //$NON-NLS-1$
        }
        // Instantiate the document builder and get the document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        db = dbf.newDocumentBuilder();
        this.doc = db.parse(xmlStream);
        this.doc.getDocumentElement().normalize();
    }
    
    
    /**
     * Adds the PMP service to the AndroidManifest.xml
     * 
     * @param xmlStream
     *            Stream to the XML file
     * @param file
     *            same XML {@link File}
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws AndroidApplicationException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws PMPServiceAlreadyExists
     * @throws AppIdentifierNotFoundException
     * @throws DOMException
     */
    protected void addPMPServiceToManifest(InputStream xmlStream, File file) throws ParserConfigurationException,
            SAXException, IOException, AndroidApplicationException, TransformerFactoryConfigurationError,
            TransformerException, PMPServiceAlreadyExists, DOMException, AppIdentifierNotFoundException {
        instantiate(xmlStream);
        
        NodeList application = this.doc.getElementsByTagName(this.ANDROID_APPLICATION);
        if (application.getLength() > 1) {
            throw new AndroidApplicationException(application.getLength() + " application nodes were found."); //$NON-NLS-1$
        }
        Element applicationElement = (Element) application.item(0);
        
        isPMPServiceExisting(applicationElement);
        
        // Create the service element
        Element service = this.doc.createElement(this.ANDROID_SERVICE);
        service.setAttribute(this.ANDROID_NAME, this.PMP_SERVICE_NAME);
        
        // Create the intent-filter element
        Element intentFilter = this.doc.createElement(this.ANDROID_INTENT_FILTER);
        
        // Create the action element
        Element action = this.doc.createElement(this.ANDROID_ACTION);
        action.setAttribute(this.ANDROID_NAME, getAppIdentifier(xmlStream));
        
        intentFilter.appendChild(action);
        service.appendChild(intentFilter);
        
        applicationElement.appendChild(service);
        
        writeBackChanges(file);
    }
    
    
    /**
     * Checks if the PMP service is already declared at the AndroidManifest.xml
     * 
     * @param applicationElement
     *            {@link Element} to the application node
     * @throws PMPServiceAlreadyExists
     */
    private void isPMPServiceExisting(Element applicationElement) throws PMPServiceAlreadyExists {
        NodeList services = applicationElement.getElementsByTagName(this.ANDROID_SERVICE);
        for (int itr = 0; itr < services.getLength(); itr++) {
            Element service = (Element) services.item(itr);
            String attribute = service.getAttribute(this.ANDROID_NAME);
            if (attribute.equals(this.PMP_SERVICE_NAME)) {
                throw new PMPServiceAlreadyExists("PMP service already declared"); //$NON-NLS-1$
            }
        }
        
    }
    
    
    /**
     * Writes the changes back to the {@link File}
     * 
     * @param file
     *            file to write
     * @throws TransformerFactoryConfigurationError
     * @throws FileNotFoundException
     * @throws TransformerException
     */
    private void writeBackChanges(File file) throws TransformerFactoryConfigurationError, FileNotFoundException,
            TransformerException {
        // Write back the changes
        Transformer transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(getXSLStream()));
        
        // Format the XML document
        transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8"); //$NON-NLS-1$
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2"); //$NON-NLS-1$ //$NON-NLS-2$
        transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
        
        DOMSource source = new DOMSource(this.doc);
        FileOutputStream os = new FileOutputStream(file);
        StreamResult result = new StreamResult(os);
        transformer.transform(source, result);
    }
    
    
    /**
     * XSL stylesheet to remove the whitespaces out of the xml file
     * 
     * @return {@link InputStream} of the stylesheet
     */
    private InputStream getXSLStream() {
        String xsd = "<xsl:stylesheet version=\"1.0\" " + "xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\">" //$NON-NLS-1$ //$NON-NLS-2$
                + "<xsl:output method=\"xml\" omit-xml-declaration=\"yes\"/>" + "<xsl:strip-space elements=\"*\"/>" //$NON-NLS-1$ //$NON-NLS-2$
                + "<xsl:template match=\"@*|node()\">" + "<xsl:copy>" + "<xsl:apply-templates select=\"@*|node()\"/>" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + "</xsl:copy>" + "</xsl:template>" + "</xsl:stylesheet>"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
        return new ByteArrayInputStream(xsd.getBytes());
    }
}
