package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AndroidApplicationException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AppIdentifierNotFoundException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.NoMainActivityException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPActivityAlreadyExistsException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPServiceAlreadyExists;

/**
 * Calls the {@link AndroidManifestParser} to provide the functions of the
 * parser
 * 
 * @author Thorsten Berberich
 * 
 */
public class AndroidManifestAdapter {

    /**
     * Calls the {@link AndroidManifestAdapter} to get the app identifier
     * 
     * @param xmlPath
     *            path to the XML file in the workspace (e.g. "/myProject/")
     * @param xmlFile
     *            file name (should be AndroidManifest.xml)
     * @return the app identifier
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws AppIdentifierNotFoundException
     */
    public String getAppIdentifier(String xmlPath, String xmlFile)
	    throws ParserConfigurationException, SAXException, IOException,
	    AppIdentifierNotFoundException {

	return new AndroidManifestParser().getAppIdentifier(getInputStream(
		xmlPath, xmlFile));
    }

    /**
     * Converts the main activity into a PMP registration activity in the
     * AndroidManifest.xml. It only adopts the
     * <code>android:label="@string/app_name"</code> and the attribute for the
     * main activity
     * <code>android:theme="@android:style/Theme.NoTitleBar"</code>
     * 
     * @param xmlPath
     *            path to the XML file
     * @param xmlFile
     *            filename
     * @throws FileNotFoundException
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerFactoryConfigurationError
     * @throws TransformerException
     * @throws PMPActivityAlreadyExistsException
     * @throws NoMainActivityException
     * @throws AppIdentifierNotFoundException
     */
    public void addPMPActivityToManifest(String xmlPath, String xmlFile)
	    throws FileNotFoundException, ParserConfigurationException,
	    SAXException, IOException, TransformerFactoryConfigurationError,
	    TransformerException, PMPActivityAlreadyExistsException,
	    NoMainActivityException, AppIdentifierNotFoundException {

	new AndroidManifestParser().addPMPActivity(
		getInputStream(xmlPath, xmlFile), getFile(xmlPath, xmlFile));
    }

    /**
     * Adds the PMP service to the AndroidManifest.xml
     * 
     * @param xmlPath
     *            path to the XML file
     * @param xmlFile
     *            filename
     * @throws FileNotFoundException
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
    public void addPMPServiceToManifest(String xmlPath, String xmlFile)
	    throws FileNotFoundException, ParserConfigurationException,
	    SAXException, IOException, AndroidApplicationException,
	    TransformerFactoryConfigurationError, TransformerException,
	    PMPServiceAlreadyExists, DOMException,
	    AppIdentifierNotFoundException {

	new AndroidManifestParser().addPMPServiceToManifest(
		getInputStream(xmlPath, xmlFile), getFile(xmlPath, xmlFile));
    }

    /**
     * Gets an {@link InputStream} to an path and a file name
     * 
     * @param xmlPath
     *            path to the file
     * @param xmlFile
     *            file name
     * @return {@link InputStream} to the file
     * @throws FileNotFoundException
     *             thrown when the file is not found
     */
    private InputStream getInputStream(String xmlPath, String xmlFile)
	    throws FileNotFoundException {
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IResource resource = root.findMember(new Path(xmlPath));
	IContainer container = (IContainer) resource;
	IFile file = container.getFile(new Path(xmlFile));
	InputStream xmlInputStream = new FileInputStream(file.getLocation()
		.toOSString());
	return xmlInputStream;
    }

    /**
     * Gets an {@link File} to an path and a file name
     * 
     * @param xmlPath
     *            path to the file
     * @param xmlFile
     *            file name
     * @return {@link File}
     * @throws FileNotFoundException
     *             thrown when the file is not found
     */
    private File getFile(String xmlPath, String xmlFile)
	    throws FileNotFoundException {
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IResource resource = root.findMember(new Path(xmlPath));
	IContainer container = (IContainer) resource;
	IFile file = container.getFile(new Path(xmlFile));
	return new File(file.getLocation().toOSString());
    }
}