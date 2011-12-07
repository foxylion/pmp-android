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
package de.unistuttgart.ipvs.pmp.apps.simpleapp.gui.util;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

public class DialogManager {
    
    private static DialogManager instance = null;
    
    private Dialog waitingDialog;
    
    
    private DialogManager() {
    }
    
    
    public static DialogManager getInstance() {
        if (instance == null) {
            instance = new DialogManager();
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
                //DialogManager.this.waitingDialog = ProgressDialog.show(Model.getInstance().getContext(), Model
                //        .getInstance().getContext().getString(R.string.wait), Model.getInstance().getContext()
                //        .getString(R.string.registration, true));
            }
        });
    }
    
    
    /**
     * Disposes the dialog that is shown while registering. This is called when the app is registered successful and the
     * PMPService calls {@link CalendarApp#onRegistrationSuccess()} or {@link CalendarApp#onRegistrationFailed(String)
     * ()}
     */
    public void dismissWaitingDialog() {
        this.waitingDialog.dismiss();
    }
    
    
    /**
     * Shows a dialog when the user wants to do sth. that is not allowed in this service level
     */
    public void showServiceLevelInsufficientDialog(Context context) {
        
    	/*
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
                    public void onClick(DialogInterface dialog, int id) {
                        
                        // Call Privacy Level Activity with the specified Intent
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName("de.unistuttgart.ipvs.pmp",
                                "de.unistuttgart.ipvs.pmp.gui.activities.ServiceLvlActivity"));
                        intent.putExtra("connection.identifier", "de.unistuttgart.ipvs.pmp.apps.calendarapp");
                        
                        Model.getInstance().getContext().startActivity(intent);
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
        */
    }  
}
