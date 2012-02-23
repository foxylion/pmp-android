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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;

/**
 * This class uses the {@link ServerProvider} to download the ResourceGroups and
 * shows progress dialogs while downloading
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServerProviderDialogWrapper {

    /**
     * The downloaded list
     */
    List<RGIS> rgisList;

    /**
     * Downloads the RG list in a job
     * 
     * @param shell
     *            shell to display error messages
     * @param showErrors
     *            flag if error should be shown or not
     * @param listener
     *            an {@link IJobChangeListener} where the caller can indicate
     *            the status of this job
     */
    public void updateServerListWithJob(final Shell shell,
	    final Boolean showErrors, IJobChangeListener listener) {
	Job job = new Job("Downloading Resource Groups") {

	    @Override
	    protected IStatus run(IProgressMonitor monitor) {
		monitor.beginTask("Downloading Resource Groups", 2);
		ServerProvider server = ServerProvider.getInstance();

		// Refresh the list
		try {
		    server.updateResourceGroupList();
		    monitor.worked(1);
		    rgisList = server.getAvailableRessourceGroups();
		    monitor.done();
		} catch (final IOException e) {
		    monitor.done();

		    // Show the error message if wanted
		    if (showErrors) {
			Display.getDefault().asyncExec(
				new Thread(new Runnable() {

				    @Override
				    public void run() {
					IStatus status = new Status(
						IStatus.ERROR,
						"PROGRESS_JOB",
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
			return Status.CANCEL_STATUS;
		    }
		}

		// Everything was ok
		return Status.OK_STATUS;
	    }
	};
	job.addJobChangeListener(listener);
	job.schedule();
    }

    /**
     * Updates the {@link RGIS} list from the server, while downloading a
     * {@link ProgressMonitorDialog} is displayed
     * 
     * @param shell
     *            {@link Shell} to display the {@link ProgressMonitorDialog}
     */
    public void updateServerListWithDialog(final Shell shell) {

	// Create the dialog
	ProgressMonitorDialog dialog = new ProgressMonitorDialog(shell);
	try {

	    // Run the dialog, not cancelable because the timeout is set to 1000
	    dialog.run(true, false, new IRunnableWithProgress() {

		@Override
		public void run(IProgressMonitor monitor)
			throws InvocationTargetException, InterruptedException {

		    // Start the task
		    monitor.beginTask("Downloading Resource Groups...", 2);
		    ServerProvider server = ServerProvider.getInstance();
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
     * Get the list that was downloaded
     * 
     * @return the downlaoded {@link RGIS} list
     */
    public List<RGIS> getRGISList() {
	return rgisList;
    }
}
