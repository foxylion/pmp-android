package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.GeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis.PrivacySettingsPage;
import de.unistuttgart.ipvs.pmp.editor.xml.RGISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.RGUtil;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;

/**
 * Editor, that allow the user to interactively change their RIS.
 * 
 * @author Patrick Strobel
 * 
 */
public class RgisEditor extends FormEditor {

	private Model model = Model.getInstance();

	@Override
	protected void addPages() {
		try {
			model.setRgisEditor(this);
			RGUtil rgutil = XMLUtilityProxy.getRGUtil();
			
			// Parse XML-File
			FileEditorInput input = (FileEditorInput) this.getEditorInput();
			IRGIS rgis;
			try {
				// Synchronize if out of sync (better: show message)
				if (!input.getFile().isSynchronized(IResource.DEPTH_ONE)) {
					input.getFile().refreshLocal(IResource.DEPTH_ONE, null);
				}
				rgis = rgutil.parse(input.getFile().getContents());
				
			} catch (ParserException e) {
				rgis = rgutil.createBlankRGIS();
			}
			model.setRgis(rgis);
			RGISValidatorWrapper.getInstance().validateRGIS(rgis, true);
			
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

	public void firePropertyChangedDirty() {
		firePropertyChange(FormEditor.PROP_DIRTY);
	}
	
	@Override
	public boolean isDirty() {
		return model.isRgisDirty();
	}
	

	@Override
	public void doSave(IProgressMonitor monitor) {
		FileEditorInput input = (FileEditorInput)this.getEditorInput();
		InputStream is = XMLUtilityProxy.getRGUtil().compile(model.getRgis());
		try {
			input.getFile().setContents(is, false, true, monitor);
			model.setRgisDirty(false);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		System.out.println("saveas");
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

}