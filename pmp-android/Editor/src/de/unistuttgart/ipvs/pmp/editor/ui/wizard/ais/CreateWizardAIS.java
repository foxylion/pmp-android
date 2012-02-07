package de.unistuttgart.ipvs.pmp.editor.ui.wizard.ais;

import java.io.ByteArrayInputStream;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

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
	// TODO Auto-generated method stub
	return false;
    }

    private void writeFile() {
	throw new UnsupportedOperationException();
    }

    private ByteArrayInputStream getContentStream() {
	throw new UnsupportedOperationException();
    }

}