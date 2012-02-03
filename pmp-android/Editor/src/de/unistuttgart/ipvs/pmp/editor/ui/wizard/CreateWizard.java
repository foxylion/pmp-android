package de.unistuttgart.ipvs.pmp.editor.ui.wizard;

import java.io.ByteArrayInputStream;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class CreateWizard extends Wizard implements INewWizard {
    private WizardPageCreate page;

    private void writeFile() {
	throw new UnsupportedOperationException();
    }

    private ByteArrayInputStream getContentStream() {
	throw new UnsupportedOperationException();
    }

    @Override
    public void init(IWorkbench aWorkbench, IStructuredSelection aSelection) {
	throw new UnsupportedOperationException();
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