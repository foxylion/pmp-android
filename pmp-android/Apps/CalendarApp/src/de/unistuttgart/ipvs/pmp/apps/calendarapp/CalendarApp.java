package de.unistuttgart.ipvs.pmp.apps.calendarapp;

import java.io.IOException;
import java.io.InputStream;

import android.app.Dialog;
import android.os.RemoteException;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.app.App;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ChangeAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.NewAppointmentDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.DialogManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.PMPServiceConnector;

public class CalendarApp extends App {
    
    static {
        Log.setTagSufix("CalendarApp");
    }
    
    /**
     * The flags of functionality for the service levels
     */
    private boolean readFlag = false;
    private boolean writeFlag = false;
    private boolean emailFlag = false;
    private boolean importFlag = false;
    private boolean exportFlag = false;
    
    
    @Override
    protected String getServiceAndroidName() {
        return "de.unistuttgart.ipvs.pmp.apps.calendarapp";
    }
    
    
    @Override
    public void setActiveServiceLevel(int level) {
        Log.v("ServiceLevel set to: " + String.valueOf(level));
        Model.getInstance().setServiceLevel(level);
    }
    
    
    @Override
    protected InputStream getXMLInputStream() {
        try {
            return getAssets().open("AppInformation.xml");
        } catch (IOException e) {
            Log.e("IOException during loading App XML", e);
            return null;
        }
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see de.unistuttgart.ipvs.pmp.app.App#onRegistrationSuccess() Is called
     * when the registration was successful. The method then tries to receive
     * the initial service level from the PMP service.
     */
    @Override
    public void onRegistrationSuccess() {
        Log.d("Registration succeed");
        
        // Connector to get the initial service level
        final PMPServiceConnector connector = new PMPServiceConnector(getApplicationContext(), getSignee());
        connector.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.e("Disconnected");
            }
            
            
            @Override
            public void connected() {
                // Try to get the initial service level
                try {
                    connector.getAppService().getInitialServiceLevel();
                } catch (RemoteException e) {
                    Log.e("RemoteException during getting initial ServiceLevel", e);
                }
                DialogManager.getInstance().dismissWaitingDialog();
                connector.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e("Binding failed during getting initial service level.");
            }
        });
        
        // Connect to the service
        connector.bind();
    }
    
    
    @Override
    public void onRegistrationFailed(String message) {
        Log.d("Registration failed:" + message);
        DialogManager.getInstance().dismissWaitingDialog();
    }
    
    
    /**
     * Changes the functionality of the app according to its set ServiceLevel
     */
    public void changeFunctionalityAccordingToServiceLevel() {
        Log.d("Changing ServiceLevel to level " + String.valueOf(Model.getInstance().getServiceLevel()));
        
        int level = Model.getInstance().getServiceLevel();
        
        switch (level) {
            case 0:
                // Disable all
                readFlag = false;
                writeFlag = false;
                emailFlag = false;
                importFlag = false;
                exportFlag = false;
                break;
            case 1:
                // Read from database
                readFlag = true;
                writeFlag = false;
                emailFlag = false;
                importFlag = false;
                exportFlag = false;
                break;
            case 2:
                // Read and write (and delete) from/to database
                readFlag = true;
                writeFlag = true;
                emailFlag = false;
                importFlag = false;
                exportFlag = false;
                break;
            case 3:
                // Read and write (and delete) from/to database and send email
                readFlag = true;
                writeFlag = true;
                emailFlag = true;
                importFlag = false;
                exportFlag = false;
                break;
            case 4:
                // Read and write (and delete) from/to database and import from file system
                readFlag = true;
                writeFlag = true;
                emailFlag = false;
                importFlag = true;
                exportFlag = false;
                break;
            case 5:
                // Read and write (and delete) from/to database, import from file system and send email
                readFlag = true;
                writeFlag = true;
                emailFlag = true;
                importFlag = true;
                exportFlag = false;
                break;
            case 6:
                // Read and write (and delete) from/to database, import and export from/to file system
                readFlag = true;
                writeFlag = true;
                emailFlag = false;
                importFlag = true;
                exportFlag = true;
                break;
            case 7:
                // Read and write from/to database, import and export from/to file system and send email
                readFlag = true;
                writeFlag = true;
                emailFlag = true;
                importFlag = true;
                exportFlag = true;
                break;
        }
        
        /*
         * 
         *  Use flags to enable/disable gui components
         *  
         */
        
        // Read files
        if (readFlag) {
            SqlConnector.getInstance().loadAppointments();
        } else {
            Model.getInstance().clearLocalList();
        }
        
        // new button
        Model.getInstance().getNewAppointmentButton().setEnabled(writeFlag);
        
        /*
         * Listener for adding a new appointment. Opens a new dialog
         */
        Model.getInstance().getNewAppointmentButton().setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (writeFlag) {
                    Dialog dialog = new NewAppointmentDialog(Model.getInstance().getContext());
                    dialog.setTitle("Create new appointment");
                    dialog.show();
                }
            }
        });
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete a appointment or send it via email
         */
        Model.getInstance().getContext().getListView()
                .setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                    
                    @Override
                    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                        if (writeFlag || emailFlag) {
                            menu.setHeaderTitle(getString(R.string.menu));
                        }
                        if (writeFlag && !emailFlag) {
                            menu.add(0, 0, 0, R.string.delete);
                        } else if (emailFlag && !writeFlag) {
                            menu.add(0, 0, 0, R.string.send);
                        } else if (writeFlag && emailFlag) {
                            menu.add(0, 0, 0, R.string.delete);
                            menu.add(1, 1, 0, R.string.send);
                        }
                    }
                });
        
        /*
         * Listener for clicking one item. Opens a new dialog where the user can
         * change the date.
         */
        Model.getInstance().getContext().getListView().setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (writeFlag) {
                    Dialog changeDateDialog = new ChangeAppointmentDialog(Model.getInstance().getContext(), position);
                    changeDateDialog.show();
                }
            }
        });
        
        /*
         * Listener for long clicking on one item. Opens a context menu where
         * the user can delete a file
         */
        if (Model.getInstance().getImportListView() != null) {
            Model.getInstance().getImportListView().setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
                
                @Override
                public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                    if (importFlag && exportFlag) {
                        menu.setHeaderTitle(getString(R.string.menu));
                        menu.add(0, 0, 0, R.string.delete);
                    }
                }
            });
        }
        
    }
}
