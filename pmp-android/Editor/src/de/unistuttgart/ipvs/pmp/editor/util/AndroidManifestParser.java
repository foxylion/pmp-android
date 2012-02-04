package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class AndroidManifestParser {

    /**
     * The XML document
     */
    protected Document doc = null;

    /**
     * Gets the AppIdentifier out of an AndroidManifest.xml
     * 
     * @param xmlStream
     *            Stream to the AndroidManifest.xml
     * @return app identifier
     */
    public String getAppIdentifier(InputStream xmlStream) {
	instantiate(xmlStream);

	// Manifest node
	Node node = doc.getDocumentElement();

	// Get the attributes to get the package name
	NamedNodeMap attribute = node.getAttributes();

	return attribute.getNamedItem("package").getNodeValue();

    }

    public void addPMPActivity(InputStream xmlStream) {
	throw new UnsupportedOperationException();
    }

    public boolean isMainActivityExisting() {
	throw new UnsupportedOperationException();
    }

    private void instantiate(InputStream xmlStream) {
	if (xmlStream == null) {
	    throw new NullPointerException("XML-stream was null");
	}

	try {
	    // Instantiate the document builder and get the document
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder db;
	    db = dbf.newDocumentBuilder();
	    this.doc = db.parse(xmlStream);
	    this.doc.getDocumentElement().normalize();

	} catch (ParserConfigurationException e) {

	} catch (SAXException e) {

	} catch (IOException e) {

	}
    }
}