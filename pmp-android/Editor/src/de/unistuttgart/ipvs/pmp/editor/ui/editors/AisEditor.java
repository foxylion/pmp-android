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
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.validator.AISValidator;

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
		IAIS ais = XMLUtilityProxy.getAppUtil().parse(
			input.getFile().getContents());

		// Get the path to the project
		String[] split = input.getFile().getFullPath().toString()
			.split("/");
		String project = "/" + split[1];

		// Store ais in the Model
		Model.getInstance().setAis(ais);
		
		new AISValidator().validateAIS(Model.getInstance().getAis(), true);

		// Create the pages
		generalPage = new AISGeneralPage(this, project);
		sfPage = new AISServiceFeaturesPage(this);
	    } catch (ParserException e) {
		generalPage = new AISGeneralPage(this, null);
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