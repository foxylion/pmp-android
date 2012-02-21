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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.RgisEditor;
import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;
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
     * Instance of this class
     */
    private static Model instance = null;

    /**
     * Indicates if the ais file has unsaved changes
     */
    private Boolean isAISDirty = false;

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

    private AisEditor aisEditor;

    private RgisEditor rgisEditor;

    /**
     * Private constructor because of singleton
     */
    private Model() {
    }

    /**
     * Methode used to get the only instance of this class
     * 
     * @return only {@link Model} instance of this class
     */
    public static Model getInstance() {
	if (instance == null) {
	    instance = new Model();
	}
	return instance;
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

    public void setRgisEditor(RgisEditor editor) {
	rgisEditor = editor;

    }

    public void setRgisDirty(boolean dirty) {
	rgisDirty = dirty;
	rgisEditor.firePropertyChangedDirty();
    }

    public boolean isRgisDirty() {
	return rgisDirty;
    }

    /**
     * Gets the whole {@link RGIS} list from the server
     * 
     * @return the rgisList
     * @throws IOException
     */
    public List<RGIS> getRgisList(Shell shell) {
	if (rgisList == null) {
	    updateServerList(shell);
	}
	return rgisList;
    }

    /**
     * Updates the {@link RGIS} list from the server, while downloading a
     * {@link ProgressMonitorDialog} is displayed
     * 
     * @param shell
     *            {@link Shell} to display the {@link ProgressMonitorDialog}
     */
    public void updateServerList(final Shell shell) {

	// Create the dialog
	ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
	try {

	    // Run the dialog, not cancelable because the timeout is set to 1000
	    dialog.run(false, false, new IRunnableWithProgress() {

		@Override
		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		    // Start the task
		    monitor.beginTask("Downloading Resource Groups...", 2);
		    ServerProvider server = new ServerProvider();
		    try {

			// Refresh the list
			server.updateResourceGroupList();
			monitor.worked(1);
			rgisList = server.getAvailableRessourceGroups();
			monitor.done();
		    } catch (final IOException e) {

			// Show the error message in an asyncExectuable
			Display.getDefault().asyncExec(
				new Thread(new Runnable() {

				    @Override
				    public void run() {
					IStatus status = new Status(
						IStatus.ERROR,
						"PROGRESS_DIALOG",
						"See details", e);
					ErrorDialog
						.openError(
							shell,
							"Error",
							"A error happend while downloading the "
								+ "Resource Groups from the server.",
							status);
				    }
				}));

		    }

		}
	    });
	} catch (InvocationTargetException e) {
	    IStatus status = new Status(IStatus.ERROR, "PROGRESS_DIALOG",
		    "See details", e);
	    ErrorDialog
		    .openError(
			    shell,
			    "Error",
			    "A error happend while downloading the Resource Groups from the server.",
			    status);
	} catch (InterruptedException e) {
	    IStatus status = new Status(IStatus.ERROR, "PROGRESS_DIALOG",
		    "See details", e);
	    ErrorDialog
		    .openError(
			    shell,
			    "Error",
			    "A error happend while downloading the Resource Groups from the server.",
			    status);
	}

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
