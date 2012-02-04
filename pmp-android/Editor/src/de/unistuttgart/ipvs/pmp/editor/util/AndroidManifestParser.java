package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AndroidManifestParser {

    /**
     * The XML document
     */
    private Document doc = null;

    /**
     * Activity node
     */
    private final String ACTIVITY = "activity";

    /**
     * Path to the PMP registration activity
     */
    private final String ANDROID_NAME = "android:name";
    private final String PMP_ACTIVITY = "de.unistuttgart.ipvs.pmp.api.gui.registration.RegistrationActivity";

    /**
     * Android intent action main
     */
    private final String ANDROID_ACTION_MAIN = "android.intent.action.MAIN";

    /**
     * Style of the PMP registration activity
     */
    private final String ANDROID_THEME = "android:theme";
    private final String PMP_ANDROID_THEME = "@android:style/Theme.NoTitleBar";

    /**
     * Android label attribute
     */
    private final String ANDROID_LABEL = "android:label";

    /**
     * Intent-filter node
     */
    private final String ANDROID_INTENT_FILTER = "intent-filter";

    /**
     * Android action node
     */
    private final String ANDROID_ACTION = "action";

    /**
     * Android category node
     */
    private final String ANDROID_CATEGORY = "category";

    /**
     * Android intent category Launcher
     */
    private final String ANDROID_CATEGORY_LAUNCHER = "android.intent.action.MAIN";

    /**
     * Android meta-data node
     */
    private final String ANDROID_META_DATA = "meta-data";
    private final String ANDROID_VALUE = "android:value";
    private final String ANDROID_META_DATA_MAIN_ACTIVITY = "mainActivity";

    /**
     * Gets the AppIdentifier out of an AndroidManifest.xml
     * 
     * @param xmlStream
     *            Stream to the AndroidManifest.xml
     * @return app identifier
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public String getAppIdentifier(InputStream xmlStream)
	    throws ParserConfigurationException, SAXException, IOException {
	if (doc == null) {
	    instantiate(xmlStream);
	}

	// Manifest node
	Node node = doc.getDocumentElement();

	// Get the attributes to get the package name
	NamedNodeMap attribute = node.getAttributes();

	return attribute.getNamedItem("package").getNodeValue();

    }

    public void addPMPActivity(InputStream xmlStream)
	    throws ParserConfigurationException, SAXException, IOException {
	instantiate(xmlStream);
	String androidName = "";
	String androidLabel = "";
	String appIdentifier = getAppIdentifier(xmlStream);

	Node mainActivityNode = getMainActivityNode();
	Node parentNode = mainActivityNode.getParentNode();
	if (mainActivityNode != null) {

	    // Get the required information out of the old node
	    NamedNodeMap attributes = mainActivityNode.getAttributes();
	    Node androidNameNode = attributes.getNamedItem("android:name");
	    Node androidLabelNode = attributes.getNamedItem("android:label");
	    if (androidNameNode != null && androidLabelNode != null) {

		androidLabel = androidLabelNode.getNodeValue();

		if (androidNameNode.getNodeValue().startsWith(appIdentifier)) {
		    androidName = androidNameNode.getNodeValue();
		} else {
		    androidName = appIdentifier
			    + androidNameNode.getNodeValue();
		}
	    } else {
		throw new NullPointerException(
			"android:name or android:label was null");
	    }

	    // Create the new node
	    Element newElement = doc.createElement(ACTIVITY);
	    newElement.setAttribute(ANDROID_NAME, PMP_ACTIVITY);
	    newElement.setAttribute(ANDROID_LABEL, androidLabel);
	    newElement.setAttribute(ANDROID_THEME, PMP_ANDROID_THEME);

	    // Create the intent-filter node
	    Element intentFilter = doc.createElement(ANDROID_INTENT_FILTER);

	    // Create the action element
	    Element action = doc.createElement(ANDROID_ACTION);
	    action.setAttribute(ANDROID_NAME, ANDROID_ACTION_MAIN);

	    // Create the category element
	    Element category = doc.createElement(ANDROID_CATEGORY);
	    category.setAttribute(ANDROID_NAME, ANDROID_CATEGORY_LAUNCHER);

	    // Append action and category to the intent filter
	    intentFilter.appendChild(action);
	    intentFilter.appendChild(category);

	    // Append the intent filter
	    newElement.appendChild(intentFilter);

	    // Meta data node
	    Element metaData = doc.createElement(ANDROID_META_DATA);
	    metaData.setAttribute(ANDROID_NAME, ANDROID_META_DATA_MAIN_ACTIVITY);
	    metaData.setAttribute(ANDROID_VALUE, androidName);

	    newElement.appendChild(metaData);

	    if (parentNode != null) {
		parentNode.replaceChild(newElement, mainActivityNode);
	    } else {
		throw new NullPointerException(
			"No parent node found for the activty node");
	    }
	}
	
	// WRITE BACK THE CHANGE ???
    }

    /**
     * Returns the {@link Node} that contains the MainActivity IntentFilter
     * 
     * @return {@link Node} that contains the MainActivity or <code>null</code>
     */
    private Node getMainActivityNode() {
	// Get all "action" nodes
	NodeList actions = doc.getElementsByTagName(ANDROID_ACTION);

	// Check the attributes if there is a MAIN activity
	if (actions.getLength() > 0) {
	    for (int itr = 0; itr < actions.getLength(); itr++) {
		Node action = actions.item(itr);
		NamedNodeMap attributes = action.getAttributes();
		Node attribute = attributes.getNamedItem(ANDROID_NAME);
		if (attribute != null
			&& attribute.getNodeValue().equals(
				"android.intent.action.MAIN")) {
		    Node actionParent = action.getParentNode();
		    return actionParent.getParentNode();
		}
	    }
	}
	return null;
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
     */
    public boolean isMainActivityExisting(InputStream xmlStream)
	    throws ParserConfigurationException, SAXException, IOException {
	instantiate(xmlStream);

	// Get all "action" nodes
	NodeList actions = doc.getElementsByTagName("action");

	// Check the attributes if there is a MAIN activity
	if (actions.getLength() > 0) {
	    for (int itr = 0; itr < actions.getLength(); itr++) {
		Node action = actions.item(itr);
		NamedNodeMap attributes = action.getAttributes();
		Node attribute = attributes.getNamedItem("android:name");
		if (attribute != null
			&& attribute.getNodeValue().equals(
				"android.intent.action.MAIN")) {
		    return true;
		}
	    }
	}
	return false;
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
    private void instantiate(InputStream xmlStream)
	    throws ParserConfigurationException, SAXException, IOException {
	if (xmlStream == null) {
	    throw new NullPointerException("XML-stream was null");
	}
	// Instantiate the document builder and get the document
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	DocumentBuilder db;
	db = dbf.newDocumentBuilder();
	this.doc = db.parse(xmlStream);
	this.doc.getDocumentElement().normalize();

    }
}