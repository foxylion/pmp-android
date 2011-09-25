package de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import android.os.RemoteException;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.IFileAccess;
import de.unistuttgart.ipvs.pmp.service.utils.IConnectorCallback;
import de.unistuttgart.ipvs.pmp.service.utils.ResourceGroupServiceConnector;

/**
 * This class is implemented with the singleton pattern and provides the interface to the resource group "file system"
 * of pmp.
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
     * Instance of this file system connector (Singleton)
     */
    private static FileSystemConnector instance = null;
    
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
     * The import string
     */
    private String importString = null;
    
    
    /**
     * Private constructor (Singleton)
     */
    private FileSystemConnector() {
        
    }
    
    
    /**
     * This method returns the one and only instance of the file system connector (Singleton)
     */
    public static FileSystemConnector getInstance() {
        if (instance == null) {
            instance = new FileSystemConnector();
        }
        return instance;
    }
    
    
    /**
     * This method provides the export for appointments to the file system of pmp (resource group)
     * 
     * @param appointments
     *            to export
     * @param fileName
     *            name of the file
     */
    public void exportAppointments(List<Appointment> appointments, final String fileName) {
        
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
            exportStringBuilder.append("SUMMARY:" + appointment.getDescrpition() + "\n");
            
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
        
        // The resource group connection
        final ResourceGroupServiceConnector rgCon = new ResourceGroupServiceConnector(Model.getInstance().getContext()
                .getApplicationContext(),
                ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getSignee(), rgIdentifier);
        
        // Add a callback handler
        rgCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.d(rgIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d(rgIdentifier + " connected");
                
                if (rgCon.getAppService() == null) {
                    Log.e(rgIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(
                                resourceIdentifier));
                        // Create the folder
                        ifa.makeDirs(FOLDER_NAME);
                        // Write the file
                        boolean success = ifa.write(FOLDER_NAME + "/" + fileName, exportString, false);
                        
                        // if exporting worked successfully, add the file to the model list
                        if (success) {
                            FileSystemConnector.getInstance().listStoredFiles();
                            Toast.makeText(Model.getInstance().getContext(), "Export success", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Log.e("Exporting failed");
                        }
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                }
                
                rgCon.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(rgIdentifier + " binding failed");
            }
        });
        rgCon.bind();
    }
    
    
    /**
     * This method provides the import for appointments from the file system of pmp (resource group)
     * 
     * @param fileName
     *            the name of the file
     */
    public void importAppointments(final String fileName) {
        
        // clear the import string
        importString = null;
        
        // The resource group connection
        final ResourceGroupServiceConnector rgCon = new ResourceGroupServiceConnector(Model.getInstance().getContext()
                .getApplicationContext(),
                ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getSignee(), rgIdentifier);
        
        // Add a callback handler
        rgCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.d(rgIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d(rgIdentifier + " connected");
                
                if (rgCon.getAppService() == null) {
                    Log.e(rgIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(
                                resourceIdentifier));
                        // Read the file
                        importString = ifa.read(FOLDER_NAME + "/" + fileName);
                        
                        // Check, if the import string is null
                        if (importString == null) {
                            Log.e("Importing failed!");
                        } else {
                            
//                            // Delete all appointments
//                            for (Appointment appointment : Model.getInstance().getAppointmentList()) {
//                                SqlConnector.getInstance().deleteAppointment(appointment.getId());
//                            }
                            
                            Log.e(importString);
                            
                            String[] importArray = importString.split("\n");
                            
                            // Check meta data
                            boolean rowOne = importArray[0].equals("BEGIN:VCALENDAR");
                            boolean rowTwo = importArray[1].equals("VERSION:2.0");
                            boolean rowThree = importArray[2].equals("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP");
                            boolean rowLast = importArray[importArray.length - 1].equals("END:VCALENDAR");
                            if (!(rowOne && rowTwo && rowThree && rowLast)) {
                                Log.e("Import meta data is invalid");
                            }
                            
                            // Check and get the appointments
                            boolean success = true;
                            String description = null;
                            String dateString = null;
                            for (int dataRow = 0; dataRow < importArray.length - 4; dataRow++) {
                                String importRow = importArray[dataRow + 3];
                                
                                switch (dataRow % 4) {
                                    case 0:
                                        if (!importRow.equals("BEGIN:VTODO")) {
                                            success = false;
                                        }
                                        break;
                                    case 1:
                                        if (!importRow.startsWith("SUMMARY:")) {
                                            success = false;
                                        } else {
                                            description = importRow.substring(8);
                                        }
                                        break;
                                    case 2:
                                        if (!importRow.startsWith("DTSTAMP:")) {
                                            success = false;
                                        } else {
                                            
                                            dateString = importRow.substring(8);
                                            // Check and parse the date
                                            if (!dateString
                                                    .matches("\\d\\d\\d\\d[0-1]\\d\\d\\dT[0-2]\\d[0-5]\\d[0-5]\\dZ")) {
                                                success = false;
                                                Log.e("Date does not match the regular expression pattern!");
                                            }
                                            
                                            Date date = new Date(Integer.valueOf(dateString.substring(0, 4)), Integer
                                                    .valueOf(dateString.substring(4, 6)) - 1, Integer
                                                    .valueOf(dateString.substring(6, 8)), Integer.valueOf(dateString
                                                    .substring(9, 11)), Integer.valueOf(dateString.substring(11, 13)),
                                                    Integer.valueOf(dateString.substring(13, 15)));
                                            
                                            // Store the appointment
                                            SqlConnector.getInstance().storeNewAppointment(date, description);
                                            Log.d("Imported appointment: " + description);
                                            Log.d("Imported appointment: " + date);
                                        }
                                        break;
                                    case 3:
                                        if (!importRow.equals("END:VTODO")) {
                                            success = false;
                                        }
                                        break;
                                }
                                
                            }
                            
                            // If something went wrong, log the error
                            if (!success) {
                                Log.e("Import data invalid; imported as far as posible");
                            } else {
                                Log.d("Import succeed");
                            }
                            
                        }
                        
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                }
                
                rgCon.unbind();
                
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(rgIdentifier + " binding failed");
            }
        });
        rgCon.bind();
    }
    
    
    /**
     * This method provides the stored files
     * 
     * @return list of all stored files
     */
    public void listStoredFiles() {
        // The resource group connection
        final ResourceGroupServiceConnector rgCon = new ResourceGroupServiceConnector(Model.getInstance().getContext()
                .getApplicationContext(),
                ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getSignee(), rgIdentifier);
        
        // Add a callback handler
        rgCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.d(rgIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d(rgIdentifier + " connected");
                
                if (rgCon.getAppService() == null) {
                    Log.e(rgIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(
                                resourceIdentifier));
                        // list the files and add it to the model (and clear the model)
                        Model.getInstance().clearFileList();
                        for (FileDetails file : ifa.list(FOLDER_NAME)) {
                            Model.getInstance().addFileToList(file);
                        }
                        
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                }
                
                rgCon.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(rgIdentifier + " binding failed");
            }
        });
        rgCon.bind();
    }
    
    
    /**
     * Delete a file
     * 
     * @param file
     *            fileDetails of the file
     */
    public void deleteFile(final FileDetails file) {
        // The resource group connection
        final ResourceGroupServiceConnector rgCon = new ResourceGroupServiceConnector(Model.getInstance().getContext()
                .getApplicationContext(),
                ((CalendarApp) Model.getInstance().getContext().getApplicationContext()).getSignee(), rgIdentifier);
        
        // Add a callback handler
        rgCon.addCallbackHandler(new IConnectorCallback() {
            
            @Override
            public void disconnected() {
                Log.d(rgIdentifier + " disconnected");
            }
            
            
            @Override
            public void connected() {
                Log.d(rgIdentifier + " connected");
                
                if (rgCon.getAppService() == null) {
                    Log.e(rgIdentifier + " appService is null");
                } else {
                    // Get resource
                    try {
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(
                                resourceIdentifier));
                        // delete a file
                        boolean success = ifa.delete(FOLDER_NAME + "/" + file.getName());
                        if (success) {
                            Model.getInstance().removeFileFromList(file);
                            FileSystemConnector.getInstance().listStoredFiles();
                        }
                    } catch (RemoteException e) {
                        Log.e("Remote Exception", e);
                    }
                }
                
                rgCon.unbind();
            }
            
            
            @Override
            public void bindingFailed() {
                Log.e(rgIdentifier + " binding failed");
            }
        });
        rgCon.bind();
    }
    
}
