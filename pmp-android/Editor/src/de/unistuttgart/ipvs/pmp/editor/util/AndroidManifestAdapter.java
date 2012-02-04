package de.unistuttgart.ipvs.pmp.editor.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.xml.sax.SAXException;

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
     */
    public String getAppIdentifier(String xmlPath, String xmlFile)
	    throws ParserConfigurationException, SAXException, IOException {
	return new AndroidManifestParser().getAppIdentifier(getInputStream(
		xmlPath, xmlFile));
    }

    public void addPMPActivityToManifest(String xmlPath, String xmlFile) {
	throw new UnsupportedOperationException();
    }

    public boolean isMainActivityExisting(String xmlPath, String xmlFile)
	    throws FileNotFoundException, ParserConfigurationException, SAXException, IOException {
	return new AndroidManifestParser()
		.isMainActivityExisting(getInputStream(xmlPath, xmlFile));
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
}