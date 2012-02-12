package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISGeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISServiceFeaturesPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.StoredInformation;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.exception.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

/**
 * The editor for App-Information-Sets that contains the {@link AISGeneralPage}
 * and the {@link AISServiceFeaturesPage}
 * 
 * @author Thorsten Berberich
 * 
 */
public class AisEditor extends FormEditor {

    /**
     * The {@link AISGeneralPage}
     */
    private AISGeneralPage generalPage;

    /**
     * The {@link AISServiceFeaturesPage}
     */
    private AISServiceFeaturesPage sfPage;

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
		AIS ais = XMLUtilityProxy.getAppUtil().parse(
			input.getFile().getContents());

		// Store ais in the Model
		Model.getInstance().setAis(ais);

		// Create the pages
		generalPage = new AISGeneralPage(this);
		sfPage = new AISServiceFeaturesPage(this);

		// Add names and descriptions to table
		StoredInformation loc = generalPage.getLocalization();
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
	    /*
	     * Reset the dirty flag in the model and store this instance of this
	     * editor that the model can call the firepropertyChanged
	     */
	    Model.getInstance().setAisEditor(this);
	    Model.getInstance().setAISDirty(false);

	    // Add the pages
	    addPage(generalPage);
	    addPage(sfPage);
	} catch (PartInitException e) {
	    MessageDialog.openError(this.getSite().getShell(), "Error",
		    "Could not open file.");
	} catch (CoreException e) {
	    MessageDialog.openError(this.getSite().getShell(), "Error",
		    "Could not open file.");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor mon) {
	FileEditorInput input = (FileEditorInput) this.getEditorInput();
	InputStream is = XMLUtilityProxy.getAppUtil().compile(
		Model.getInstance().getAis());
	try {
	    // Save the file
	    input.getFile().setContents(is, true, true, mon);

	    // Set the dirty flag to false because it was just saved
	    Model.getInstance().setAISDirty(false);
	} catch (CoreException e) {
	    MessageDialog.openError(this.getSite().getShell(), "Error",
		    "Could not save file.");
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
	// Not allowed
    }

    @Override
    public boolean isDirty() {
	return Model.getInstance().isAisDirty();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }

    /**
     * Called from the {@link Model} if the model is dirty to update the view
     * and show that sth. has changed. This only calls
     * {@link FormEditor#firePropertyChange(FormEditor.PROP_DIRTY)}
     */
    public void firePropertyChangedDirty() {
	firePropertyChange(FormEditor.PROP_DIRTY);
    }
}