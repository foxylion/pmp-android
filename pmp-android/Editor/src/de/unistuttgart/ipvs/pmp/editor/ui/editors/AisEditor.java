package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISGeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.StoredInformation;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.PrivacySettingsPage;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

public class AisEditor extends FormEditor {
    
	private AISGeneralPage generalPage;

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    @Override
    protected void addPages() {
	try {
	    // Parse XML-File
	    FileEditorInput input = (FileEditorInput) this.getEditorInput();
	    try {
		// Synchronize if out of sync (better: show message)
		if (!input.getFile().isSynchronized(IResource.DEPTH_ONE)) {
		    input.getFile().refreshLocal(IResource.DEPTH_ONE, null);
		}
		
		AIS ais = XMLUtilityProxy.getAppUtil().parse(input.getFile().getContents());
		
		// Store ais in the Model
		Model.getInstance().setAis(ais);
		generalPage = new AISGeneralPage(this);
		StoredInformation loc = generalPage.getLocalization();

		// Add names and descriptions to table
		for (Name name : ais.getNames()) {
		    loc.addName(name.getLocale().getLanguage(), name.getName());
		}
		for (Description desc : ais.getDescriptions()) {
		    loc.addDescription(desc.getLocale().getLanguage(),
			    desc.getDescription());
		}
	    } catch (ParserException e) {
		generalPage = new AISGeneralPage(this);
	    }

	    addPage(generalPage);
	    addPage(new PrivacySettingsPage(this));
	} catch (PartInitException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (CoreException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor arg0) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
	// TODO Auto-generated method stub
	return false;
    }
}