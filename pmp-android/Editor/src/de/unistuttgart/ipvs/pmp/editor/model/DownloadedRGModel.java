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

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ServerProviderDialogWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * This class stores the downloaded {@link RGIS} from the server. This model is
 * a singleton because every opened editor instance should have the same
 * {@link RGIS} from the server and they should only be downloaded once.
 * 
 * @author Thorsten Berberich
 * 
 */
public class DownloadedRGModel {

    /**
     * The stored {@link RGIS} list that was downloaded from the server
     */
    private List<RGIS> rgisList = null;

    /**
     * The only instance of this classe
     */
    private static DownloadedRGModel instance;

    /**
     * Private constructor because of singleton
     */
    private DownloadedRGModel() {
    }

    /**
     * Method to get the instance of this class
     * 
     * @return only instance of {@link DownloadedRGModel}
     */
    public static DownloadedRGModel getInstance() {
	if (instance == null) {
	    instance = new DownloadedRGModel();
	}
	return instance;
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
	ServerProviderDialogWrapper.getInstance().updateServerListWithDialog(
		shell);
	rgisList = ServerProviderDialogWrapper.getInstance().getRGISList();
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
	// The callback method inside a job listener
	JobChangeAdapter changeAdapter = new JobChangeAdapter() {
	    @Override
	    public void done(IJobChangeEvent event) {
		if (event.getResult().isOK()) {
		    // Get the downloaded list
		    rgisList = ServerProviderDialogWrapper.getInstance()
			    .getRGISList();
		}
	    }
	};
	ServerProviderDialogWrapper.getInstance().updateServerListWithJob(
		shell, showErrors, changeAdapter);
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
