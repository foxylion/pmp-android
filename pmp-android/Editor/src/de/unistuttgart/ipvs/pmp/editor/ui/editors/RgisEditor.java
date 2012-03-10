/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.RgisModel;
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

    private final RgisModel model = new RgisModel();

    @Override
    protected void addPages() {
	try {
	    model.setEditor(this);
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

	    addPage(new GeneralPage(this, model));
	    addPage(new PrivacySettingsPage(this, model));
	} catch (PartInitException e) {
	    e.printStackTrace();
	} catch (CoreException e) {
	    e.printStackTrace();
	}
    }

    public void firePropertyChangedDirty() {
	firePropertyChange(IEditorPart.PROP_DIRTY);
    }

    @Override
    public boolean isDirty() {
	return model.isDirty();
    }

    @Override
    public void doSave(IProgressMonitor monitor) {
	FileEditorInput input = (FileEditorInput) this.getEditorInput();
	InputStream is = XMLUtilityProxy.getRGUtil().compile(model.getRgis());
	try {
	    input.getFile().setContents(is, false, true, monitor);
	    model.setDirty(false);
	} catch (CoreException e) {
	    e.printStackTrace();
	}

    }

    @Override
    public void doSaveAs() {
    }

    @Override
    public boolean isSaveAsAllowed() {
	return false;
    }

}