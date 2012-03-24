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

import de.unistuttgart.ipvs.pmp.editor.util.I18N;
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
    private List<RGIS> rgisList;
    
    /**
     * The instance of this class
     */
    private static ServerProviderDialogWrapper instance;
    
    /**
     * The update job
     */
    Job job;
    
    
    /**
     * Private constructor because of singleton
     */
    private ServerProviderDialogWrapper() {
    }
    
    
    /**
     * Returns the instance of this class
     * 
     * @return instance of {@link ServerProviderDialogWrapper}
     */
    public static ServerProviderDialogWrapper getInstance() {
        if (instance == null) {
            instance = new ServerProviderDialogWrapper();
        }
        return instance;
    }
    
    
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
    public void updateServerListWithJob(final Shell shell, final Boolean showErrors, IJobChangeListener listener) {
        this.job = new Job(I18N.general_rgdownloaddialog_job) {
            
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                monitor.beginTask(I18N.general_rgdownloaddialog_job, 2);
                ServerProvider server = ServerProvider.getInstance();
                
                // Refresh the list
                try {
                    server.updateResourceGroupList();
                    monitor.worked(1);
                    ServerProviderDialogWrapper.this.rgisList = server.getAvailableRessourceGroups();
                    monitor.done();
                } catch (final IOException e) {
                    monitor.done();
                    
                    // Show the error message if wanted
                    if (showErrors) {
                        Display.getDefault().asyncExec(new Thread(new Runnable() {
                            
                            @Override
                            public void run() {
                                IStatus status = new Status(IStatus.ERROR, "PROGRESS_JOB", I18N.general_seedetails, e); //$NON-NLS-1$
                                ErrorDialog.openError(shell, I18N.general_rgdownloaddialog_downloaderror_title,
                                        I18N.general_rgdownloaddialog_downloaderror_text, status);
                            }
                        }));
                        return Status.CANCEL_STATUS;
                    }
                }
                
                // Everything was ok
                return Status.OK_STATUS;
            }
        };
        this.job.addJobChangeListener(listener);
        this.job.schedule();
    }
    
    
    /**
     * Updates the {@link RGIS} list from the server, while downloading a {@link ProgressMonitorDialog} is displayed
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
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    
                    // Start the task
                    monitor.beginTask(I18N.general_rgdownloaddialog_task, 2);
                    ServerProvider server = ServerProvider.getInstance();
                    try {
                        
                        // Refresh the list
                        server.updateResourceGroupList();
                        monitor.worked(1);
                        ServerProviderDialogWrapper.this.rgisList = server.getAvailableRessourceGroups();
                        monitor.done();
                    } catch (final IOException e) {
                        
                        // Show the error message in an asyncExectuable
                        Display.getDefault().asyncExec(new Thread(new Runnable() {
                            
                            @Override
                            public void run() {
                                IStatus status = new Status(IStatus.ERROR,
                                        "PROGRESS_DIALOG", I18N.general_seedetails, e); //$NON-NLS-1$
                                ErrorDialog.openError(shell, I18N.general_rgdownloaddialog_downloaderror_title,
                                        I18N.general_rgdownloaddialog_downloaderror_text, status);
                            }
                        }));
                        
                    }
                    
                }
            });
        } catch (InvocationTargetException e) {
            IStatus status = new Status(IStatus.ERROR, "PROGRESS_DIALOG", I18N.general_seedetails, e); //$NON-NLS-1$
            ErrorDialog.openError(shell, I18N.general_rgdownloaddialog_downloaderror_title,
                    I18N.general_rgdownloaddialog_downloaderror_text, status);
        } catch (InterruptedException e) {
            IStatus status = new Status(IStatus.ERROR, "PROGRESS_DIALOG", I18N.general_seedetails, e); //$NON-NLS-1$
            ErrorDialog.openError(shell, I18N.general_rgdownloaddialog_downloaderror_title,
                    I18N.general_rgdownloaddialog_downloaderror_text, status);
        }
    }
    
    
    /**
     * Get the list that was downloaded
     * 
     * @return the downlaoded {@link RGIS} list
     */
    public List<RGIS> getRGISList() {
        return this.rgisList;
    }
    
    
    /**
     * Cancels a running job
     */
    public void cancelJob() {
        if (this.job != null) {
            this.job.cancel();
        }
    }
}
