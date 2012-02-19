package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.GeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.PrivacySettingsPage;
import de.unistuttgart.ipvs.pmp.xmlutil.RGUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Editor, that allow the user to interactively change their RIS.
 * @author Patrick Strobel
 *
 */
public class RgisEditor extends FormEditor {
	
	private RGUtil rgutil = new RGUtil();

    @Override
    protected void addPages() {
    	try {    		
    		// Parse XML-File 
    		FileEditorInput input = (FileEditorInput)this.getEditorInput();
    		try {
    			// Synchronize if out of sync (better: show message)
    			if (!input.getFile().isSynchronized(IResource.DEPTH_ONE)) {
    				input.getFile().refreshLocal(IResource.DEPTH_ONE, null);
    			}
    			RGIS rgis = rgutil.parse(input.getFile().getContents());
    			Model.getInstance().setRgis(rgis);
    			
 
    		} catch (ParserException e) {
    		}
			addPage(new GeneralPage(this));
			addPage(new PrivacySettingsPage(this));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
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