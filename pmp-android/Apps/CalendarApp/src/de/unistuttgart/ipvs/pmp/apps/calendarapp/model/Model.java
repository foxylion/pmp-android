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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.widget.ArrayAdapter;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.ImportActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter.AppointmentArrayAdapter;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter.SeparatedListAdapter;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;

public class Model {
    
    /**
     * Instance of this class
     */
    private static Model instance;
    
    /**
     * Stores for every existing day a list of {@link Appointment}s
     */
    private HashMap<String, ArrayList<Appointment>> dayAppointments = new HashMap<String, ArrayList<Appointment>>();
    
    /**
     * {@link HashMap} for storing the adapters of one day
     */
    private HashMap<String, AppointmentArrayAdapter> adapters = new HashMap<String, AppointmentArrayAdapter>();
    
    /**
     * Holds all files for importing
     */
    private List<FileDetails> fileList = new ArrayList<FileDetails>();
    
    /**
     * The context of the app
     */
    private CalendarAppActivity appContext;
    
    /**
     * The context of the import activity
     */
    private ImportActivity importContext;
    
    /**
     * Array adapter of the list to refresh it
     */
    private SeparatedListAdapter arrayAdapter;
    
    /**
     * Array adapter of the import file list to refresh it
     */
    private ArrayAdapter<FileDetails> importArrayAdapter;
    
    /**
     * Highest id of the {@link Appointment}s
     */
    private int id = 0;
    
    
    /**
     * Private constructor because of singleton
     */
    private Model() {
    }
    
    
    /**
     * Returns the stored instance of the class or creates a new one if there is none
     * 
     * @return instance of this class
     */
    public static Model getInstance() {
        if (instance == null) {
            instance = new Model();
        }
        
        return instance;
    }
    
    
    /**
     * Called from {@link SqlConnector#loadAppointments()}
     */
    public void loadAppointments(ArrayList<Appointment> appList) {
        
        dayAppointments.clear();
        for (Appointment app : appList) {
            addAppointment(app);
        }
        
        this.arrayAdapter.notifyDataSetChanged();
        
        // Update the visibility of the "no appointments avaiable" textview
        getContext().updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Sets the array adapter that the model can refresh the list when something is changed
     * 
     * @param adapter
     *            the ArrayAdapter of the list with dates
     */
    public void setArrayAdapter(SeparatedListAdapter adapter) {
        this.arrayAdapter = adapter;
    }
    
    
    /**
     * Sets the array adapter that the model can refresh the list when something is changed
     * 
     * @param adapter
     *            the ArrayAdapter of the list with dates
     */
    public SeparatedListAdapter getArrayAdapter() {
        return this.arrayAdapter;
    }
    
    
    /**
     * Sets the context of the app
     * 
     * @param context
     *            context of the app
     */
    public void setContext(CalendarAppActivity context) {
        this.appContext = context;
    }
    
    
    /**
     * Returns the context of the app
     * 
     * @return app context
     */
    public CalendarAppActivity getContext() {
        return this.appContext;
    }
    
    
    /**
     * Called from {@link SqlConnector#storeNewDate(String, String)}. Adds the appointment to the {@link Appointment}
     * list of the day or creates the list if it doesn't exist
     * 
     * @param Appointment
     *            appointment to store
     */
    public void addAppointment(Appointment appointment) {
        if (appointment.getDescrpition().equals("") && appointment.getName().equals("")) {
            Toast.makeText(this.appContext, R.string.appointment_not_added, Toast.LENGTH_SHORT).show();
            return;
        }
        
        String key = creatKey(appointment.getDate());
        if (dayAppointments.containsKey(key)) {
            dayAppointments.get(key).add(appointment);
        } else {
            ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
            appointmentList.add(appointment);
            dayAppointments.put(key, appointmentList);
            AppointmentArrayAdapter adapter = new AppointmentArrayAdapter(appContext, R.layout.list_item,
                    appointmentList);
            adapters.put(key, adapter);
            arrayAdapter.addSection(appointment.getDateString(), adapter);
        }
        
        this.arrayAdapter.notifyDataSetChanged();
        
        // Update the visibility of the "no appointments available" textview
        getContext().updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Changes the appointment. Called from {@link SqlConnector#changeAppointment(int, String, String)}
     * 
     * @param id
     *            unique id of the date
     * @param date
     *            {@link Date}
     * @param description
     *            Description of the date
     */
    public void changeAppointment(int id, Date date, Date oldDate, String name, String description, Severity severity) {
        String key = creatKey(oldDate);
        
        Appointment toDel = null;
        
        if (dayAppointments.containsKey(key)) {
            ArrayList<Appointment> appList = dayAppointments.get(key);
            for (Appointment appointment : appList) {
                
                // The date remains the same
                if (appointment.getId() == id && oldDate.equals(date)) {
                    appointment.setDate(date);
                    appointment.setName(name);
                    appointment.setDescription(description);
                    appointment.setSeverity(severity);
                    adapters.get(key).notifyDataSetChanged();
                    break;
                    
                    // The date changes
                } else if (appointment.getId() == id) {
                    // Store appointment to delete later
                    toDel = appointment;
                    break;
                }
            }
            
            // Delete appointment if necessary if the date has changed
            if (toDel != null) {
                deleteAppointment(toDel);
                
                // Add new appointment
                addAppointment(new Appointment(id, name, description, date, severity));
            }
            
            this.arrayAdapter.notifyDataSetChanged();
        } else {
            Log.e("List of this day not found");
        }
    }
    
    
    /**
     * Returns the whole list of appointments
     * 
     * @return all appointments at all days
     */
    public ArrayList<Appointment> getAppointmentList() {
        ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
        for (Entry<String, ArrayList<Appointment>> entry : dayAppointments.entrySet()) {
            appointmentList.addAll(entry.getValue());
        }
        return appointmentList;
    }
    
    
    public Boolean isModelEmpty() {
        return dayAppointments.isEmpty();
    }
    
    
    /**
     * Clears the local stored list of dates but not the dates stored at the database
     */
    public void clearLocalList() {
        dayAppointments.clear();
        arrayAdapter.reset();
        adapters.clear();
        
        arrayAdapter.notifyDataSetChanged();
        appContext.updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Clears the local stored list of dates but not the dates stored at the database
     */
    public void clearLocalListWithoutTextViewUpdate() {
        this.dayAppointments.clear();
        adapters.clear();
        arrayAdapter.removeEmptyHeadersAndSections();
    }
    
    
    /**
     * Get the file list for importing
     * 
     * @return file list for importing
     */
    public List<FileDetails> getFileList() {
        return this.fileList;
    }
    
    
    /**
     * Get a file for a given file name
     * 
     * @param fileName
     *            given file name
     * @return file for file name, null if file name does not exist
     */
    public FileDetails getFileForName(String fileName) {
        for (FileDetails file : this.fileList) {
            if (file.getName().equals(fileName)) {
                return file;
            }
        }
        // Update the visibility of the "no files avaiable" textview
        if (getImportContext() != null) {
            getImportContext().updateNoAvaiableFilesTextView();
        }
        return null;
    }
    
    
    /**
     * Remove a file from the list for importing
     * 
     * @param file
     *            to remove
     */
    public void removeFileFromList(FileDetails file) {
        this.fileList.remove(file);
        if (this.importArrayAdapter != null) {
            this.importArrayAdapter.notifyDataSetChanged();
        }
        
        // Update the visibility of the "no files avaiable" textview
        if (getImportContext() != null) {
            getImportContext().updateNoAvaiableFilesTextView();
        }
    }
    
    
    /**
     * Add a file to the list for importing
     * 
     * @param file
     *            to add
     */
    public void addFileToList(FileDetails file) {
        this.fileList.add(file);
        if (this.importArrayAdapter != null) {
            this.importArrayAdapter.notifyDataSetChanged();
        }
        
        // Update the visibility of the "no files avaiable" textview
        if (getImportContext() != null) {
            getImportContext().updateNoAvaiableFilesTextView();
        }
    }
    
    
    /**
     * Clear the file list of the model
     */
    public void clearFileList() {
        this.fileList.clear();
        if (this.importArrayAdapter != null) {
            this.importArrayAdapter.notifyDataSetChanged();
        }
        
        // Update the visibility of the "no files avaiable" textview
        if (getImportContext() != null) {
            getImportContext().updateNoAvaiableFilesTextView();
        }
    }
    
    
    /**
     * Check, if a file name already exists
     * 
     * @param filenameToCheck
     *            filename to check
     * @return flag
     */
    public boolean isFileNameExisting(String filenameToCheck) {
        for (FileDetails file : this.fileList) {
            if (file.getName().toLowerCase().equals(filenameToCheck.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Get the array adapter for importing files
     * 
     * @return array adapter
     */
    public ArrayAdapter<FileDetails> getImportArrayAdapter() {
        return this.importArrayAdapter;
    }
    
    
    /**
     * Set the array adapter for importing files
     * 
     * @param importArrayAdapter
     *            array adapter for importing files
     */
    public void setImportArrayAdapter(ArrayAdapter<FileDetails> importArrayAdapter) {
        this.importArrayAdapter = importArrayAdapter;
    }
    
    
    /**
     * Get the context of the import activity
     * 
     * @return context of the import activity
     */
    public ImportActivity getImportContext() {
        return this.importContext;
    }
    
    
    /**
     * Set the context of the import activity
     * 
     * @param importContext
     *            context of the import activity
     */
    public void setImportContext(ImportActivity importContext) {
        this.importContext = importContext;
    }
    
    
    /**
     * Creates a key for getting an arraylist of {@link Appointment}s.
     * 
     * @return string representation
     */
    private String creatKey(Date date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
        return dateFormat.format(cal.getTime());
    }
    
    
    /**
     * Deletes a appointment out of the day list
     * 
     * @param appointment
     *            appointment to delete
     */
    public void deleteAppointment(Appointment appointment) {
        ArrayList<String> toDelete = new ArrayList<String>();
        
        // Search the correct list of this day
        for (Entry<String, ArrayList<Appointment>> dayList : dayAppointments.entrySet()) {
            if (dayList.getValue().contains(appointment)) {
                
                // Delete the entry out of this day list
                dayList.getValue().remove(appointment);
                
                /*
                 * If this daylist is empty then remove the header and the adapter,
                 * remember what to delete out of the list of days
                 */
                if (dayList.getValue().isEmpty()) {
                    arrayAdapter.removeEmptyHeadersAndSections();
                    adapters.remove(dayList.getKey());
                    toDelete.add(dayList.getKey());
                }
            }
        }
        
        // Delete the day lists out of the whole list
        for (String del : toDelete) {
            dayAppointments.remove(del);
        }
        arrayAdapter.notifyDataSetChanged();
        appContext.updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Delete all appointments from the database and model
     */
    public void deleteAllAppointments() {
        setHighestId(0);
        dayAppointments.clear();
        arrayAdapter.reset();
        arrayAdapter.notifyDataSetChanged();
        adapters.clear();
        appContext.updateNoAvaiableAppointmentsTextView();
        SqlConnector connector = new SqlConnector();
        connector.deleteAllApointments();
    }
    
    
    /**
     * Sets the new highest id
     * 
     * @param id
     *            highest id
     */
    public void setHighestId(int id) {
        this.id = id;
    }
    
    
    /**
     * Returns the current highest id
     * 
     * @return current highest id
     */
    public int getHighestId() {
        return id;
    }
    
    
    /**
     * Returns a new highest id
     * 
     * @return currentHighestId+1
     */
    public int getNewHighestId() {
        return id++;
    }
}
