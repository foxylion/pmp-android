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
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ServerProviderDialogWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AIS;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * Extends the {@link AbstractModel} to realize the model for the
 * {@link AisEditor}
 * 
 * @author Thorsten Berberich
 * 
 */
public class AisModel extends AbstractModel {

    /**
     * The stored {@link AIS}
     */
    private IAIS ais;

    /**
     * The stored {@link RGIS} list that was downloaded from the server
     */
    private List<RGIS> rgisList = null;

    /**
     * Sets the {@link IAIS} to store
     * 
     * @param ais
     *            {@link IAIS} to store
     */
    public void setAis(IAIS ais) {
	this.ais = ais;
    }

    /**
     * Gets the stored {@link AIS}
     * 
     * @return the ais
     */
    public IAIS getAis() {
	return ais;
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

}
