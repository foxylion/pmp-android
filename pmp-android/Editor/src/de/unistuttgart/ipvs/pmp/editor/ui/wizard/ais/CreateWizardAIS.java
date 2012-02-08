package de.unistuttgart.ipvs.pmp.editor.ui.wizard.ais;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.filebuffers.manipulation.ContainerCreator;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

/**
 * Creates a new "ais.xml" in the given project in the assets folder
 * 
 * @author Thorsten Berberich
 * 
 */
public class CreateWizardAIS extends Wizard implements INewWizard {
    private WizardPageCreateAIS page;
    private ISelection selection;

    /**
     * Constructor for the ais CreateWizard.
     */
    public CreateWizardAIS() {
	super();
	setNeedsProgressMonitor(true);
    }

    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection) {
	this.selection = selection;
    }

    @Override
    public void addPages() {
	page = new WizardPageCreateAIS(selection);
	addPage(page);
    }

    @Override
    public boolean performFinish() {

	// Get the file to create
	final String containerName = page.getProjectName();
	final String fileName = "ais.xml";
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	final IResource resource = root.findMember(new Path(containerName));
	IContainer container = (IContainer) resource;

	// To create
	final IFile file = container.getFile(new Path(fileName));

	// If the file exists, show a warning that the file exists
	if (file.exists()) {
	    int style = SWT.ICON_QUESTION | SWT.YES | SWT.NO;
	    MessageBox messageBox = new MessageBox(getShell(), style);
	    messageBox.setText("Save \"ais.xml\"");
	    messageBox
		    .setMessage("\"ais.xml\" already existing.\n Should it be replaced?");
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

	// Runnable to show the progress
	IRunnableWithProgress op = new IRunnableWithProgress() {
	    public void run(IProgressMonitor monitor)
		    throws InvocationTargetException {
		try {
		    // If the file doesn't exist, create the files and folder
		    if (!resource.exists()) {
			new ContainerCreator(ResourcesPlugin.getWorkspace(),
				new Path(containerName))
				.createContainer(monitor);
		    }
		    doFinish(file, monitor);
		} catch (CoreException e) {
		    throw new InvocationTargetException(e);
		} finally {
		    monitor.done();
		}
	    }
	};

	try {
	    getContainer().run(true, false, op);
	} catch (InterruptedException e) {
	    return false;
	} catch (InvocationTargetException e) {
	    Throwable realException = e.getTargetException();
	    MessageDialog.openError(getShell(), "Error",
		    realException.getMessage());
	    return false;
	}
	return true;
    }

    private void doFinish(final IFile file, IProgressMonitor monitor)
	    throws CoreException {

	monitor.beginTask("Creating ais.xml", 2);

	// Write an empty file
	try {
	    InputStream stream = new ByteArrayInputStream("".getBytes());

	    // ais.xml exists -> write it
	    if (file.exists()) {
		file.setContents(stream, true, true, monitor);
	    } else {
		// ais.xml doesn't exist -> create it
		file.create(stream, true, monitor);
	    }
	    stream.close();
	} catch (IOException e) {
	}
	
	monitor.worked(1);
	monitor.setTaskName("Opening file for editing...");
	
	// Try to open the editor with the file
	getShell().getDisplay().asyncExec(new Runnable() {
	    public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench()
			.getActiveWorkbenchWindow().getActivePage();
		try {
		    IDE.openEditor(page, file, true);
		} catch (PartInitException e) {
		}
	    }
	});
	monitor.worked(1);
    }
}