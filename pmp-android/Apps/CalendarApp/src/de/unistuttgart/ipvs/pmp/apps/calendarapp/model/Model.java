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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.ImportActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources.FileDetails;

public class Model {
    
    /**
     * Instance of this class
     */
    private static Model instance;
    
    /**
     * Holds all stored dates
     */
    private ArrayList<Appointment> appointmentList = new ArrayList<Appointment>();
    
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
    private ArrayAdapter<Appointment> arrayAdapter;
    
    /**
     * Array adapter of the import file list to refresh it
     */
    private ArrayAdapter<FileDetails> importArrayAdapter;
    
    
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
    public void loadAppointments(ArrayList<Appointment> tList) {
        
        this.appointmentList.clear();
        for (Appointment date : tList) {
            this.appointmentList.add(date);
        }
        
        Collections.sort(this.appointmentList, new DateComparator());
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
    public void setArrayAdapter(ArrayAdapter<Appointment> adapter) {
        this.arrayAdapter = adapter;
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
     * Called from {@link SqlConnector#storeNewDate(String, String)}. Adds the appointment to de model.
     * 
     * @param date
     *            date to store
     */
    public void addAppointment(Appointment appointment) {
        this.appointmentList.add(appointment);
        Collections.sort(this.appointmentList, new DateComparator());
        this.arrayAdapter.notifyDataSetChanged();
        
        // Update the visibility of the "no appointments avaiable" textview
        getContext().updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Called when the appointment is deleted out of the model. Called from {@link SqlConnector#deleteDate(int)}
     * 
     * @param id
     *            id of the date to delete
     */
    public void deleteAppointmentByID(int id) {
        Appointment toDelete = null;
        for (Appointment todo : this.appointmentList) {
            if (todo.getId() == id) {
                toDelete = todo;
            }
        }
        this.appointmentList.remove(toDelete);
        this.arrayAdapter.notifyDataSetChanged();
        
        // Update the visibility of the "no appointments avaiable" textview
        getContext().updateNoAvaiableAppointmentsTextView();
    }
    
    
    /**
     * Delete all appointments from the database and model
     */
    public void deleteAllAppointments() {
        // Get all ids
        List<Integer> idList = new ArrayList<Integer>();
        for (Appointment appointment : this.appointmentList) {
            idList.add(appointment.getId());
        }
        
        // Delete all appointments
        for (int id : idList) {
            SqlConnector.getInstance().deleteAppointment(id);
        }
    }
    
    
    /**
     * Changes the appointment. Called from {@link SqlConnector#changeAppointment(int, String, String)}
     * 
     * @param id
     *            unique id of the date
     * @param date
     *            {@link Date}
     * @param description
     *            descrpition of the date
     */
    public void changeAppointment(int id, Date date, String description) {
        for (Appointment appointment : this.appointmentList) {
            if (appointment.getId() == id) {
                appointment.setDate(date);
                appointment.setDescription(description);
                Collections.sort(this.appointmentList, new DateComparator());
            }
        }
        this.arrayAdapter.notifyDataSetChanged();
    }
    
    
    /**
     * Returns the appointment at the given index of the list
     * 
     * @param index
     *            index of the date
     * @return the date
     */
    public Appointment getAppointmentByIndex(int index) {
        return this.appointmentList.get(index);
    }
    
    
    /**
     * Returns the whole list of appointments
     * 
     * @return
     */
    public ArrayList<Appointment> getAppointmentList() {
        return this.appointmentList;
    }
    
    
    /**
     * Returns true if the service feature is set or not
     * 
     * @return true if set, false if not set or not found
     */
    public Boolean getServiceFeature(String featureKey) {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        return app_preferences.getBoolean(featureKey, false);
    }
    
    
    
    /**
     * Sets the status if the table exists or not
     * 
     * @param created
     *            true if the table is created
     */
    public void tableCreated(Boolean created) {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putBoolean("tablecreated", created);
        if (!editor.commit()) {
            Log.e("Error while commiting preferences");
        }
    }
    
    
    /**
     * Clears the local stored list of dates but not the dates stored at the database
     */
    public void clearLocalList() {
        this.appointmentList.clear();
        this.arrayAdapter.notifyDataSetChanged();
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
}
