package de.unistuttgart.ipvs.pmp.editor.ui.wizard.rgis;

import java.io.IOException;
import java.io.InputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Creates a new "rgis.xml" in the given project in the assets folder
 * 
 * @author Thorsten Berberich
 * 
 */
public class CreateWizardRGIS extends Wizard implements INewWizard {

    /**
     * {@link IWizardPage} that will be displayed
     */
    private WizardPageCreateRGIS page;
    private ISelection selection;

    /**
     * Filename of the XML
     */
    private final String FILENAME = "rgis.xml";

    /**
     * Constructor for the ais CreateWizard.
     */
    public CreateWizardRGIS() {
	super();
	setNeedsProgressMonitor(false);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
	this.selection = selection;
    }

    @Override
    public void addPages() {
	page = new WizardPageCreateRGIS(selection);
	addPage(page);
    }

    @Override
    public boolean performFinish() {

	// Get the workspace root
	IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace()
		.getRoot();

	// Get the project
	IProject aisProject = myWorkspaceRoot.getProject(page.getProjectOnly());
	IFile file = null;

	if (aisProject.exists()) {
	    try {
		if (!aisProject.isOpen()) {
		    aisProject.open(null);
		}

		// Enter the assets folder
		IFolder assetsFolder = aisProject.getFolder("assets");

		// Create the assets folder if it doesn't exist
		if (!assetsFolder.exists()) {
		    assetsFolder.create(true, true, null);
		}

		file = assetsFolder.getFile(FILENAME);
	    } catch (CoreException e) {
		MessageDialog.openError(getShell(), "Error",
			"Could not open project \"" + page.getProjectOnly()
				+ "\"");
		return false;
	    }
	} else {
	    ErrorDialog.openError(getShell(), "Error",
		    "Project \"" + page.getProjectOnly()
			    + "\" does not exist anymore,", null);
	    return false;
	}

	// Show dialog because an "rgis.xml" already exists
	if (file.exists()) {
	    int style = SWT.ICON_QUESTION | SWT.YES | SWT.NO;
	    MessageBox messageBox = new MessageBox(getShell(), style);
	    messageBox.setText("Save \"rgis.xml\"");
	    messageBox
		    .setMessage("\"rgis.xml\" already existing.\n Should it be replaced?");
	    int result = messageBox.open();
	    switch (result) {
	    // Overwrite it
	    case SWT.YES:
		break;
	    // Do nothing
	    case SWT.NO:
		return false;
	    }
	}

	// Write the file
	doFinish(file);

	return true;
    }

    /**
     * Creates the XML file
     * 
     * @param file
     *            {@link IFile} file to write
     */
    private void doFinish(final IFile file) {

	// Write RGIS XML file with the identifier
	try {

	    // Create the RGIS
	    RGIS rgis = new RGIS();
	    rgis.setIdentifier(page.getIdentifier());
	    InputStream stream = XMLUtilityProxy.getRGUtil().compile(rgis);

	    // rgis.xml exists -> write it
	    if (file.exists()) {
		file.setContents(stream, true, true, null);
	    } else {
		// rgis.xml doesn't exist -> create it
		file.create(stream, true, null);
	    }
	    stream.close();
	} catch (IOException e) {
	    ErrorDialog.openError(getShell(), "Error",
		    "Error while writing \"rgis.xml\"", null);
	} catch (CoreException e) {
	    ErrorDialog.openError(getShell(), "Error",
		    "Error while writing \"rgis.xml\"", null);
	}

	// Try to open the editor with the file
	getShell().getDisplay().asyncExec(new Runnable() {
	    @Override
	    public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		try {
		    IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
		}
	    }
	});
    }
}