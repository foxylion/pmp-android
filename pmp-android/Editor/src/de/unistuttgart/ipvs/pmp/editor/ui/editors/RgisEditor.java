package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.GeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.PrivacySettingsPage;

public class RgisEditor extends FormEditor {

    @Override
    protected void addPages() {
    	try {
			addPage(new GeneralPage(this));
			addPage(new PrivacySettingsPage(this));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    @Override
    public void doSave(IProgressMonitor arg0) {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void doSaveAs() {
	// TODO Auto-generated method stub
	
    }

    @Override
    public boolean isSaveAsAllowed() {
	// TODO Auto-generated method stub
	return false;
    }

}