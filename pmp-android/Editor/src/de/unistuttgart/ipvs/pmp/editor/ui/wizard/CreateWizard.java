package de.unistuttgart.ipvs.pmp.editor.ui.wizard;

import java.io.ByteArrayInputStream;
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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.editor.exceptions.AndroidApplicationException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.PMPActivityAlreadyExistsException;
import de.unistuttgart.ipvs.pmp.editor.util.AndroidManifestAdapter;
import de.unistuttgart.ipvs.pmp.editor.util.AndroidManifestParser;

public class CreateWizard extends Wizard implements INewWizard {
    private WizardPageCreate page;

    private void writeFile() {
	throw new UnsupportedOperationException();
    }

    private ByteArrayInputStream getContentStream() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void init(IWorkbench Workbench, IStructuredSelection Selection) {
	try {
	    new AndroidManifestAdapter().addPMPActivityToManifest("/Test2/", "AndroidManifest.xml");
	    new AndroidManifestAdapter().addPMPServiceToManifest("/Test2/", "AndroidManifest.xml");
	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (ParserConfigurationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (SAXException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerFactoryConfigurationError e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (TransformerException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (PMPActivityAlreadyExistsException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (AndroidApplicationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void addPages() {
	throw new UnsupportedOperationException();
    }

    @Override
    public boolean performFinish() {
	// TODO Auto-generated method stub
	return false;
    }
}