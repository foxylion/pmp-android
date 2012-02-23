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
package de.unistuttgart.ipvs.pmp.editor.model;

import java.util.List;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.swt.widgets.Shell;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.RgisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ServerProviderDialogWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.IRGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Model class to store the {@link AIS} of the XML file that is edited and the
 * {@link RGIS} that was eventually downloaded from the JPMPPS
 * 
 * @author Thorsten Berberich
 * 
 */
public class Model {

    /**
     * Indicates if the ais file has unsaved changes
     */
    private Boolean isAISDirty = false;

    /**
     * Indicates if the rgis file has unsaved changes
     */
    private boolean rgisDirty = false;

    /**
     * The stored {@link AIS}
     */
    private IAIS ais;

    /**
     * The stored {@link RGIS}
     */
    private IRGIS rgis;

    /**
     * The stored {@link RGIS} list that was downloaded from the server
     */
    private List<RGIS> rgisList = null;

    /**
     * The editor files that the propertychanged can be fired from within the
     * model
     */
    private AisEditor aisEditor;
    private RgisEditor rgisEditor;

    /**
     * Gets the stored {@link AIS}
     * 
     * @return the ais
     */
    public IAIS getAis() {
	return ais;
    }

    /**
     * Sets the {@link AIS} to store
     * 
     * @param ais
     *            the ais to set
     */
    public void setAis(IAIS ais) {
	this.ais = ais;
    }

    /**
     * Get the stored {@link RGIS}
     * 
     * @return the rgis
     */
    public IRGIS getRgis() {
	return rgis;
    }

    /**
     * Set the {@link RGIS} to store
     * 
     * @param rgis
     *            the rgis to set
     */
    public void setRgis(IRGIS rgis) {
	this.rgis = rgis;
    }

    /**
     * Sets the {@link RgisEditor}
     * 
     * @param editor
     *            the {@link RgisEditor}
     */
    public void setRgisEditor(RgisEditor editor) {
	rgisEditor = editor;

    }

    /**
     * Sets whether the rgis model is dirty or not
     * 
     * @param dirty
     */
    public void setRgisDirty(boolean dirty) {
	rgisDirty = dirty;
	rgisEditor.firePropertyChangedDirty();
    }

    /**
     * Gets the dirty status of the rgis list
     * 
     * @return true if dirty, false otherwise
     */
    public boolean isRgisDirty() {
	return rgisDirty;
    }

    /**
     * Gets the whole {@link RGIS} list from the server
     * 
     * @return the rgisList
     */
    public List<RGIS> getRgisList(Shell shell) {
	if (rgisList == null) {
	    updateRgisListWithDialog(shell);
	}
	return rgisList;
    }

    /**
     * Updates the {@link RGIS} list and let the
     * {@link ServerProviderDialogWrapper} show a dialogF
     * 
     * @param shell
     *            shell to display errors
     */
    public void updateRgisListWithDialog(Shell shell) {
	ServerProviderDialogWrapper dialog = new ServerProviderDialogWrapper();
	dialog.updateServerListWithDialog(shell);
	rgisList = dialog.getRGISList();
    }

    /**
     * Updates the {@link RGIS} list and let the
     * {@link ServerProviderDialogWrapper} do this in a job that will be
     * displayed
     * 
     * @param shell
     *            shell to display errors
     * @param showErrors
     *            true if errors should be shown, else if not
     */
    public void updateRgisListWithJob(Shell shell, Boolean showErrors) {
	final ServerProviderDialogWrapper dialog = new ServerProviderDialogWrapper();

	// The callback method inside a job listener
	JobChangeAdapter changeAdapter = new JobChangeAdapter() {
	    public void done(IJobChangeEvent event) {
		if (event.getResult().isOK()) {
		    // Get the downloaded list
		    rgisList = dialog.getRGISList();
		}
	    }
	};
	dialog.updateServerListWithJob(shell, showErrors, changeAdapter);

    }

    /**
     * Checks if the {@link RGIS} list of the server is cached locally
     * 
     * @return true if available
     */
    public Boolean isRGListAvailable() {
	if (rgisList == null) {
	    return false;
	} else {
	    return true;
	}
    }

    /**
     * Sets the whole {@link RGIS} list from the server
     * 
     * @param rgisList
     *            the rgisList to set
     */
    public void setRgisList(List<RGIS> rgisList) {
	this.rgisList = rgisList;
    }

    /**
     * True if it has unsaved changes, false if not
     * 
     * @return true if there are unsaved changes, false otherwise
     */
    public Boolean isAisDirty() {
	return isAISDirty;
    }

    /**
     * Sets the dirty flag of the ais file
     * 
     * @param isAISDirty
     *            false if it was just saved, true otherwise
     */
    public void setAISDirty(Boolean isAISDirty) {
	this.isAISDirty = isAISDirty;
	aisEditor.firePropertyChangedDirty();
    }

    /**
     * Instance of the {@link AisEditor} to call the
     * {@link AisEditor#firePropertyChangedDirty()}
     * 
     * @param aisEditor
     *            the aisEditor to set
     */
    public void setAisEditor(AisEditor aisEditor) {
	this.aisEditor = aisEditor;
    }
}
