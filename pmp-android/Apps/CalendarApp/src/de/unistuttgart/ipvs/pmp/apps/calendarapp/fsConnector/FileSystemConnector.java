package de.unistuttgart.ipvs.pmp.apps.calendarapp.fsConnector;

import java.util.List;

import android.os.RemoteException;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.CalendarApp;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Todo;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.IFileAccess;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.Resources;
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
     * Instance of this file system connector (Singleton)
     */
    private static FileSystemConnector instance = null;
    
    /**
     * Identifier of the resource group
     */
    private final String rgIdentifier = "de.unistuttgart.ipvs.pmp.resourcegroups.filesystem";
    
    /**
     * Identifier of the resource used for storing the exported data.
     */
    private final String rIdentifier = Resources.EXTERNAL_DOWNLOAD;
    
    
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
     * This method provides the export for dates to the file system of pmp (resource group)
     * 
     * @param dates
     *            to export
     */
    public void exportDates(List<Todo> dates) {
        
        // Create the export string
        StringBuilder exportStringBuilder = new StringBuilder();
        final String exportString;
        
        // Create export string
        exportStringBuilder.append("BEGIN:VCALENDAR\n");
        exportStringBuilder.append("VERSION:2.0\n");
        exportStringBuilder.append("PRODID:CALENDAR_APP_EXAMPLE_FOR_PMP\n");
        for (Todo date : dates) {
            // Dummy date object
            java.util.Date dateObjectDummy = new java.util.Date();
            dateObjectDummy.setYear(2011);
            dateObjectDummy.setMonth(11);
            dateObjectDummy.setDate(3);
            
            exportStringBuilder.append("BEGIN:VTODO\n");
            exportStringBuilder.append("SUMMARY:" + date.getDescrpition() + "\n");
            exportStringBuilder.append("DTSTAMP:" + dateObjectDummy.getYear() + dateObjectDummy.getMonth()
                    + dateObjectDummy.getDay() + "T" + dateObjectDummy.getHours() + dateObjectDummy.getMinutes()
                    + dateObjectDummy.getSeconds() + "Z\n");
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
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(rgIdentifier));
                        // Write the file
                        ifa.write("de.unistuttgart.ipvs.pmp.apps.calendarapp", exportString, false);
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
     * This method provides the import for dates from the file system of pmp (resource group)
     */
    public void importDates() {
        
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
                        IFileAccess ifa = IFileAccess.Stub.asInterface(rgCon.getAppService().getResource(rgIdentifier));
                        // Write the file
                        importString = ifa.read("de.unistuttgart.ipvs.pmp.apps.calendarapp");
                        
                        // Check, if the import string is null
                        if (importString == null) {
                            Log.e("Importing failed!");
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
