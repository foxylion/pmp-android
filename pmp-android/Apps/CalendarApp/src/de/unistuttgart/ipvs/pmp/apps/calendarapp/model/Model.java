package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.activities.CalendarAppActivity;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

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
     * The context of the app
     */
    private CalendarAppActivity appContext;
    
    /**
     * Array adapter of the list to refresh it
     */
    private ArrayAdapter<Appointment> arrayAdapter;
    
    /**
     * The newDate button of the app to dis- and enable it
     */
    private Button newAppointmentButton;
    
    
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
        appointmentList.remove(toDelete);
        this.arrayAdapter.notifyDataSetChanged();
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
     * Returns the service level of the app
     * 
     * @return the service level
     */
    public int getServiceLevel() {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        return app_preferences.getInt("servicelevel", 0);
    }
    
    
    /**
     * Sets the service level and stores it at the preferences of the app
     * 
     * @param serviceLevel
     *            the service level to set
     */
    public void setServiceLevel(int serviceLevel) {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        SharedPreferences.Editor editor = app_preferences.edit();
        editor.putInt("servicelevel", serviceLevel);
        if (!editor.commit()) {
            Log.e("Error while commiting preferences");
        }
    }
    
    
    /**
     * Checks if the table was created yet
     * 
     * @return true if the table exists
     */
    public Boolean isTableCreated() {
        SharedPreferences app_preferences = PreferenceManager.getDefaultSharedPreferences(this.appContext);
        return app_preferences.getBoolean("tablecreated", false);
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
     * Returns the new ppointment button of the app
     * 
     * @return the new appointment button
     */
    public Button getNewAppointmentButton() {
        return this.newAppointmentButton;
    }
    
    
    /**
     * Sets the new date button
     * 
     * @param newDateButton
     *            button of the app
     */
    public void setNewAppointmentButton(Button newDateButton) {
        this.newAppointmentButton = newDateButton;
    }
}
