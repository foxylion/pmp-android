/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.ui.wizard.rgis;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Thorsten
 * 
 */
public class WizardPageCreateRGIS extends WizardPage {

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public WizardPageCreateRGIS(ISelection selection) {
	super("Resourcegroup-Information-Set");
	setTitle("Resourcegroup-Information-Set File");
	setDescription("This wizard creates a new Resourcegroup-Information-Set for a PMP compatible Application.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
     * .Composite)
     */
    @Override
    public void createControl(Composite arg0) {
	// TODO Auto-generated method stub

    }

}
