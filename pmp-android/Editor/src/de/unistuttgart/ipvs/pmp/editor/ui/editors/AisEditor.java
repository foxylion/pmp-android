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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
import de.unistuttgart.ipvs.pmp.editor.model.DownloadedRGModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISGeneralPage;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISServiceFeaturesPage;
import de.unistuttgart.ipvs.pmp.editor.util.I18N;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.XMLUtilityProxy;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.parser.common.ParserException;

/**
 * The editor for App-Information-Sets that contains the {@link AISGeneralPage} and the {@link AISServiceFeaturesPage}
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
     * The model of this editor instance
     */
    private AisModel model = new AisModel();
    
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
        this.model.setEditor(this);
        
        // Download the RGs from the server at startup if it's not done
        if (!DownloadedRGModel.getInstance().isRGListAvailable()) {
            DownloadedRGModel.getInstance().updateRgisListWithJob(Display.getCurrent().getActiveShell(), false);
        }
        // Parse XML-File
        FileEditorInput input = (FileEditorInput) getEditorInput();
        
        // Get the path to the project
        String[] split = input.getFile().getFullPath().toString().split("/"); //$NON-NLS-1$
        String project = "/" + split[1]; //$NON-NLS-1$
        
        try {
            try {
                // Synchronize if out of sync (better: show message)
                if (!input.getFile().isSynchronized(IResource.DEPTH_ONE)) {
                    input.getFile().refreshLocal(IResource.DEPTH_ONE, null);
                }
                IAIS ais = XMLUtilityProxy.getAppUtil().parse(input.getFile().getContents());
                
                // Store ais in the Model
                this.model.setAis(ais);
                
                AISValidatorWrapper.getInstance().validateAIS(this.model.getAis(), true);
            } catch (ParserException e) {
                this.model.setAis(XMLUtilityProxy.getAppUtil().createBlankAIS());
                
            }
            /*
             * Reset the dirty flag in the model and store this instance of this
             * editor that the model can call the firepropertyChanged
             */
            this.model.setDirty(false);
            
            /*
             * Add the pages
             */
            this.generalPage = new AISGeneralPage(this, project, this.model);
            this.sfPage = new AISServiceFeaturesPage(this, this.model);
            
            // Add the pages
            addPage(this.generalPage);
            addPage(this.sfPage);
        } catch (PartInitException e) {
            MessageDialog.openError(getSite().getShell(), I18N.general_notopenedmsg_title,
                    I18N.general_notopenedmsg_text);
        } catch (CoreException e) {
            MessageDialog.openError(getSite().getShell(), I18N.general_notopenedmsg_title,
                    I18N.general_notopenedmsg_text);
        }
        setPartName(split[1] + " AIS");
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.
     * IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor mon) {
        FileEditorInput input = (FileEditorInput) getEditorInput();
        InputStream is = XMLUtilityProxy.getAppUtil().compile(this.model.getAis());
        try {
            // Save the file
            input.getFile().setContents(is, true, true, mon);
            
            // Set the dirty flag to false because it was just saved
            this.model.setDirty(false);
        } catch (CoreException e) {
            MessageDialog
                    .openError(getSite().getShell(), I18N.general_notsavedmsg_title, I18N.general_notsavedmsg_text);
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
        return this.model.isDirty();
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
     * Called from the {@link AisModel} if the model is dirty to update the view
     * and show that sth. has changed. This only calls {@link FormEditor#firePropertyChange(FormEditor.PROP_DIRTY)}
     */
    public void firePropertyChangedDirty() {
        firePropertyChange(IEditorPart.PROP_DIRTY);
    }
}
