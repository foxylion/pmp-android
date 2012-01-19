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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.api.PMP;
import de.unistuttgart.ipvs.pmp.api.PMPResourceIdentifier;
import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestResourceHandler;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.ImportActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs.ExportDialog;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.util.UiManager;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Severity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.IFileAccess;

/**
 * This class is implemented with the singleton pattern and provides the interface to the resource group "file system"
 * of PMP.
 * 
 * @author Marcus Vetter
 * 
 */
public class FileSystemConnector {
    
    /**
     * Foldername
     */
    private static final String FOLDER_NAME = "calendarData";
    
    /**
     * Identifier of the resource group
     */
    private static final String rgIdentifier = "de.unistuttgart.ipvs.pmp.resourcegroups.filesystem";
    
    /**
     * Identifier of the resource used for storing the exported data. This is needed since the file system resource has
     * no resource having the same name as its resource-group
     */
    private static final String resourceIdentifier = "ext_download";
    
    /**
     * Identifier to get the resource
     */
    private PMPResourceIdentifier pmpIdentifier = PMPResourceIdentifier.make(rgIdentifier, resourceIdentifier);
    
    /**
     * The import string
     */
    private String importString = null;
    
    /**
     * This method provides the export for appointments to the file system of pmp (resource group)
     * 
     * @param appointments
     *            to export
     * @param fileName
     *            name of the file
     */
    public void exportAppointments(List<Appointment> appointments, final String fileName) {
        
        // Check, if the filename is valid
        if (!fileName.matches("[[a-zA-Z0-9]|.|_|\\-| ]*")) {
            UiManager.getInstance().showInvalidFileNameDialog(Model.getInstance().getContext());
            Log.d("Invalid file name, regex: [[a-zA-Z0-9]|.|_|\\-| ]*");
            return;
        }
        
        // Create the export string
        StringBuilder exportStringBuilder = new StringBuilder();
        final String exportString;
        
        // Create export string
        exportStringBuilder.append("BEGIN:VCALENDAR\n");
        exportStringBuilder.append("VERSION:2.0\n");
        exportStringBuilder.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP\n");
        for (Appointment appointment : appointments) {
            
            // Date of the appointment
            Date date = appointment.getDate();
            
            // Build the export string
            exportStringBuilder.append("BEGIN:VTODO\n");
            exportStringBuilder.append("SUMMARY:" + appointment.getName() + "\n");
            exportStringBuilder.append("DESCRIPTION:" + appointment.getDescrpition() + "\n");
            
            // Add the severity
            exportStringBuilder.append("PRIORITY:");
            switch (appointment.getSeverity()) {
                case HIGH:
                    exportStringBuilder.append("1\n");
                    break;
                case MIDDLE:
                    exportStringBuilder.append("5\n");
                    break;
                case LOW:
                    exportStringBuilder.append("6\n");
                    break;
            }
            
            // Format the date and time
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
            SimpleDateFormat formatterTime = new SimpleDateFormat("HHmmss", Locale.getDefault());
            String dateString = formatterDate.format(cal.getTime());
            String timeString = formatterTime.format(cal.getTime());
            
            exportStringBuilder.append("DTSTAMP:" + dateString + "T" + timeString + "Z\n");
            exportStringBuilder.append("END:VTODO\n");
        }
        exportStringBuilder.append("END:VCALENDAR");
        exportString = exportStringBuilder.toString();
        
        PMP.get().getResource(pmpIdentifier, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                Looper.prepare();
                if (binder != null) {
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(binder);
                        
                        if (checkForSdCard(ifa)) {
                            // Write the file
                            boolean success = ifa.write(FOLDER_NAME + "/" + fileName, exportString, false);
                            
                            // if exporting worked successfully, add the file to the model list
                            if (success) {
                                listStoredFiles(FileSystemListActionType.NONE);
                                Toast.makeText(Model.getInstance().getContext(), R.string.export_toast_succeed,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Exporting failed");
                                Toast.makeText(Model.getInstance().getContext(), R.string.export_toast_failed,
                                        Toast.LENGTH_SHORT);
                            }
                        }
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        Looper.loop();
                    }
                } else {
                    Log.e("Could not connect to filesystem ressource");
                }
            }
        });
    }
    
    
    /**
     * This method provides the import for appointments from the file system of pmp (resource group)
     * 
     * @param fileName
     *            the name of the file
     */
    public void importAppointments(final String fileName) {
        // clear the import string
        this.importString = null;
        
        PMP.get().getResource(pmpIdentifier, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                if (binder != null) {
                    try {
                        Looper.prepare();
                        // The file access interface
                        IFileAccess ifa = IFileAccess.Stub.asInterface(binder);
                        //              List of appointments to add
                        ArrayList<Appointment> importAppointmentList = new ArrayList<Appointment>();
                        
                        // Read the file
                        FileSystemConnector.this.importString = ifa.read(FOLDER_NAME + "/" + fileName);
                        
                        // Check, if the import string is null
                        if (FileSystemConnector.this.importString == null) {
                            Log.e("Importing failed!");
                        } else {
                            
                            // The import string (split by newlines)
                            String[] importArray = FileSystemConnector.this.importString.split("\n");
                            
                            // Flag, if the import succeed
                            boolean success = true;
                            
                            // Check meta data
                            if (importArray.length > 3) {
                                boolean rowOne = importArray[0].equals("BEGIN:VCALENDAR");
                                boolean rowTwo = importArray[1].equals("VERSION:2.0");
                                boolean rowThree = importArray[2].equals("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
                                boolean rowLast = importArray[importArray.length - 1].equals("END:VCALENDAR");
                                if (!(rowOne && rowTwo && rowThree && rowLast)) {
                                    Log.e("Import meta data is invalid");
                                    success = false;
                                }
                            } else {
                                success = false;
                            }
                            
                            // Check and get the appointments
                            String name = null;
                            String description = null;
                            String dateString = null;
                            Severity severity = Severity.MIDDLE;
                            for (int dataRow = 0; dataRow < importArray.length - 4; dataRow++) {
                                String importRow = importArray[dataRow + 3];
                                
                                switch (dataRow % 6) {
                                    case 0:
                                        if (!importRow.equals("BEGIN:VTODO")) {
                                            success = false;
                                        }
                                        break;
                                    case 1:
                                        if (!importRow.startsWith("SUMMARY:")) {
                                            success = false;
                                        } else {
                                            ;
                                            name = importRow.substring(8);
                                        }
                                        break;
                                    case 2:
                                        if (!importRow.startsWith("DESCRIPTION:")) {
                                            success = false;
                                        } else {
                                            description = importRow.substring(12);
                                        }
                                        break;
                                    case 3:
                                        if (!importRow.startsWith("PRIORITY:")) {
                                            success = false;
                                        } else {
                                            int sev = 5;
                                            try {
                                                sev = Integer.valueOf(importRow.substring(9));
                                            } catch (NumberFormatException e) {
                                                Log.e("Could not parse severity", e);
                                            }
                                            
                                            switch (sev) {
                                                case 1:
                                                    severity = Severity.HIGH;
                                                    break;
                                                case 5:
                                                    severity = Severity.MIDDLE;
                                                    break;
                                                case 6:
                                                    severity = Severity.LOW;
                                                    break;
                                                default:
                                                    success = false;
                                                    break;
                                            }
                                        }
                                        break;
                                    case 4:
                                        if (!importRow.startsWith("DTSTAMP:")) {
                                            success = false;
                                        } else {
                                            
                                            dateString = importRow.substring(8);
                                            // Check and parse the date
                                            if (!dateString
                                                    .matches("\\d\\d\\d\\d[0-1]\\d\\d\\dT[0-2]\\d[0-5]\\d[0-5]\\dZ")) {
                                                success = false;
                                                Log.e("Date does not match the regular expression pattern!");
                                            } else {
                                                SimpleDateFormat formatterDate = new SimpleDateFormat("yyyyMMdd",
                                                        Locale.getDefault());
                                                SimpleDateFormat formatterTime = new SimpleDateFormat("HHmmss", Locale
                                                        .getDefault());
                                                
                                                formatterDate.setLenient(false);
                                                formatterTime.setLenient(false);
                                                try {
                                                    //Check the date if its an existing date
                                                    formatterDate.parse(dateString.substring(0, 8));
                                                    
                                                    // Check the time
                                                    formatterTime.parse(dateString.substring(9, 15));
                                                    
                                                    GregorianCalendar cal = new GregorianCalendar(Integer
                                                            .valueOf(dateString.substring(0, 4)), Integer
                                                            .valueOf(dateString.substring(4, 6)) - 1, Integer
                                                            .valueOf(dateString.substring(6, 8)), Integer
                                                            .valueOf(dateString.substring(9, 11)), Integer
                                                            .valueOf(dateString.substring(11, 13)), Integer
                                                            .valueOf(dateString.substring(13, 15)));
                                                    // Add the appointment to the list for importing
                                                    importAppointmentList.add(new Appointment(-1, name, description,
                                                            cal.getTime(), severity));
                                                } catch (ParseException e) {
                                                    success = false;
                                                }
                                                
                                            }
                                        }
                                        break;
                                    case 5:
                                        if (!importRow.equals("END:VTODO")) {
                                            success = false;
                                        }
                                        break;
                                }
                                
                            }
                            
                            // If something went wrong, log the error
                            if (!success) {
                                Log.e("Import data invalid; imported as far as posible");
                                Toast.makeText(Model.getInstance().getImportContext(),
                                        R.string.import_data_invalid_toast, Toast.LENGTH_SHORT).show();
                                UiManager.getInstance().getImportActivity().finish();
                            } else {
                                
                                SqlConnector sqlCon = new SqlConnector();
                                sqlCon.storeAppointmentListInEmptyList(importAppointmentList);
                                
                                Log.d("Import succeed");
                                Toast.makeText(Model.getInstance().getContext(), R.string.import_succeed_toast,
                                        Toast.LENGTH_SHORT).show();
                            }
                            
                        }
                        
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    } finally {
                        Looper.loop();
                    }
                } else {
                    Log.e("Could not connect to filesystem ressource");
                }
            }
        });
    }
    
    
    /**
     * This method provides the stored files
     * 
     * @param type
     *            Type of invoked action after listing the files successfully.
     */
    public void listStoredFiles(final FileSystemListActionType type) {
        
        PMP.get().getResource(pmpIdentifier, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                if (binder != null) {
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(binder);
                        
                        // list the files and add it to the model (and clear the model)
                        Model.getInstance().clearFileList();
                        List<FileDetails> fileList = new ArrayList<FileDetails>();
                        
                        // Flag, if the next action should be invoked or not
                        boolean invokeNextAction = false;
                        
                        try {
                            fileList = ifa.list(FOLDER_NAME);
                            for (FileDetails file : fileList) {
                                Model.getInstance().addFileToList(file);
                            }
                            invokeNextAction = true;
                        } catch (Exception e) {
                            boolean makeDir = ifa.makeDirs(FOLDER_NAME);
                            if (makeDir) {
                                Log.d("Created folder " + FOLDER_NAME);
                                invokeNextAction = true;
                            } else {
                                Toast.makeText(Model.getInstance().getContext(), R.string.sd_card_missing,
                                        Toast.LENGTH_LONG).show();
                                Log.d("If you want to use the import/export functionality, you have to insert a SD-Card!");
                            }
                        }
                        
                        if (invokeNextAction) {
                            switch (type) {
                                case EXPORT:
                                    // Check, if list of appointments is empty
                                    List<Appointment> appointments = Model.getInstance().getAppointmentList();
                                    if (appointments == null || appointments.size() == 0) {
                                        Log.d("Can not export appointment. There are no appointments available!");
                                        UiManager.getInstance().showAppointmentsListEmptyDialog(
                                                Model.getInstance().getContext());
                                    } else {
                                        // Open dialog for entering a file name
                                        Dialog exportDialog = new ExportDialog(Model.getInstance().getContext());
                                        exportDialog.show();
                                    }
                                    break;
                                case IMPORT:
                                    // Open activity with file list
                                    Intent intent = new Intent(Model.getInstance().getContext(), ImportActivity.class);
                                    if (Model.getInstance().getContext() != null) {
                                        Model.getInstance().getContext().startActivity(intent);
                                    }
                                    break;
                                case NONE:
                                    break;
                            }
                        }
                        
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                }
            }
        });
        
    }
    
    
    /**
     * Delete a file
     * 
     * @param file
     *            fileDetails of the file
     */
    public void deleteFile(final FileDetails file) {
        
        PMP.get().getResource(pmpIdentifier, new PMPRequestResourceHandler() {
            
            @Override
            public void onReceiveResource(PMPResourceIdentifier resource, IBinder binder) {
                if (binder != null) {
                    Log.d(rgIdentifier + " connected");
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(binder);
                        
                        // delete a file
                        boolean success = ifa.delete(FOLDER_NAME + "/" + file.getName());
                        if (success) {
                            Model.getInstance().removeFileFromList(file);
                            listStoredFiles(FileSystemListActionType.NONE);
                            Toast.makeText(Model.getInstance().getImportContext(), R.string.delete_file_toast,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                    
                } else {
                    Log.e("Could not connect to filesystem ressource");
                }
            }
        });
    }
    
    
    public Boolean checkForSdCard(IFileAccess ifa) {
        try {
            if (ifa.makeDirs(FOLDER_NAME)) {
                Log.d("Created folder " + FOLDER_NAME);
                return true;
            } else {
                Toast.makeText(Model.getInstance().getContext(), R.string.sd_card_missing, Toast.LENGTH_LONG).show();
                Log.d("If you want to use the import/export functionality, you have to insert a SD-Card!");
                return false;
            }
        } catch (RemoteException e) {
            Log.e("Remote Exception", e);
        } catch (NotFoundException e) {
            Log.e("Not found Exception", e);
        }
        return false;
    }
    
}
