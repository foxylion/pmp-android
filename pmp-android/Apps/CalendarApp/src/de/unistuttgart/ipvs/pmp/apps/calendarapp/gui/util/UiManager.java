/*
 * Copyright 2011 pmp-android development team
 * Project: CalendarApp
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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemConnector;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector.FileSystemListActionType;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.ImportActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

public class UiManager {
    
    private static UiManager instance = null;
    
    private Dialog waitingDialog;
    
    private ImportActivity ia;
    
    
    private UiManager() {
    }
    
    
    public static UiManager getInstance() {
        if (instance == null) {
            instance = new UiManager();
        }
        return instance;
    }
    
    
    /**
     * Shows a {@link ProgressDialog} while registering the app
     */
    public void showWaitingDialog() {
        new Handler().post(new Runnable() {
            
            @Override
            public void run() {
                UiManager.this.waitingDialog = ProgressDialog.show(Model.getInstance().getContext(), Model
                        .getInstance().getContext().getString(R.string.wait), Model.getInstance().getContext()
                        .getString(R.string.registration, true));
            }
        });
    }
    
    
    /**
     * Disposes the dialog that is shown while registering. This is called when the app is registered succesful and the
     * PMPService calls {@link CalendarApp#onRegistrationSuccess()} or {@link CalendarApp#onRegistrationFailed(String)
     * ()}
     */
    public void dismissWaitingDialog() {
        this.waitingDialog.dismiss();
    }
    
    
    /**
     * Shows a dialog when the user wants to do sth. that is not allowed in this service feature
     */
    public void showServiceFeatureInsufficientDialog(final String[] requested) {
        final CalendarAppActivity context = Model.getInstance().getContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.insufficent_sf))
                .setMessage(context.getString(R.string.insufficent_sf_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                }).setNegativeButton(context.getString(R.string.change_sf), new DialogInterface.OnClickListener() {
                    
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        App app = ((App) Model.getInstance().getContext().getApplication());
                        app.requestServiceFeatures(requested);
                    }
                });
        
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    /**
     * Show a dialog, if the appointment list is empty and the user want to export the list
     * 
     * @param context
     *            the context
     */
    public void showAppointmentsListEmptyDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.empty_list_title))
                .setMessage(context.getString(R.string.empty_list_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    /**
     * Show a dialog, if the entered file name is invalid
     * 
     * @param context
     *            the context
     */
    public void showInvalidFileNameDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.invalid_file_name_title))
                .setMessage(context.getString(R.string.invalid_file_name_message))
                .setPositiveButton(context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    
                    // Close the dialog
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        new FileSystemConnector().listStoredFiles(FileSystemListActionType.EXPORT);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    
    
    /**
     * Stores the {@link ImportActivity}
     * 
     * @param ia
     *            {@link ImportActivity}
     */
    public void setImportActivity(ImportActivity ia) {
        this.ia = ia;
    }
    
    
    /**
     * Returns the {@link ImportActivity}
     * 
     * @return
     */
    public ImportActivity getImportActivity() {
        if (ia != null) {
            return ia;
        } else {
            return new ImportActivity();
        }
    }
}
