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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.compiler.AISCompiler;

public class CreateWizardAIS extends Wizard implements INewWizard {
    private WizardPageCreateAIS page;
    private ISelection selection;

    /**
     * Constructor for SampleNewWizard.
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
	final String containerName = page.getProjectName();
	final String fileName = "ais.xml";
	IRunnableWithProgress op = new IRunnableWithProgress() {
	    public void run(IProgressMonitor monitor)
		    throws InvocationTargetException {
		try {
		    doFinish(containerName, fileName, monitor);
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

    private void doFinish(String containerName, String fileName,
	    IProgressMonitor monitor) throws CoreException {

	monitor.beginTask("Creating ais.xml", 2);
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	IResource resource = root.findMember(new Path(containerName));
	if (!resource.exists()) {
	    new ContainerCreator(ResourcesPlugin.getWorkspace(), new Path(
		    containerName)).createContainer(monitor);
	}
	 AIS ais = new AIS();
	IContainer container = (IContainer) resource;
	final IFile file = container.getFile(new Path(fileName));
	try {
//	     InputStream stream = new AISCompiler().compile(ais);
	    InputStream stream = new ByteArrayInputStream("abc".getBytes());
	    if (file.exists()) {
		file.setContents(stream, true, true, monitor);
	    } else {
		file.create(stream, true, monitor);
	    }
	    stream.close();
	} catch (IOException e) {
	}
	monitor.worked(1);
	monitor.setTaskName("Opening file for editing...");
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

    private void throwCoreException(String message) throws CoreException {
	IStatus status = new Status(IStatus.ERROR,
		"Editor for App-Information-Sets", IStatus.OK, message, null);
	throw new CoreException(status);
    }

}