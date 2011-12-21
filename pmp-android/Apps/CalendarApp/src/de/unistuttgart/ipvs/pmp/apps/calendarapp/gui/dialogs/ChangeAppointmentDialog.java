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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

public class ChangeAppointmentDialog extends Dialog {
    
    /**
     * Index of the date to edit
     */
    private int dateIndex;
    
    /**
     * The date picker
     */
    private DatePicker dPicker;
    
    /**
     * The TextView with the description
     */
    private TextView desc;
    
    /**
     * The button to confirm the dialog
     */
    private Button confirm;
    
    /**
     * Original date
     */
    private Appointment appointment;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ChangeAppointmentDialog(Context context, Appointment clicked) {
        super(context);
        this.appointment = clicked;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.date_dialog);
        
        this.setTitle(R.string.change_todo_dialog);
        
        Date date = this.appointment.getDate();
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        this.dPicker = (DatePicker) findViewById(R.id.datePicker);
        this.dPicker.init(year, month, day, null);
        
        this.desc = (TextView) findViewById(R.id.description);
        this.desc.setText(this.appointment.getDescrpition());
        
        this.confirm = (Button) findViewById(R.id.ConfirmButton);
        this.confirm.setOnClickListener(new ConfirmListener());
        
        /*
         * Neeeded to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * Listener class only needed for the confirm button
     * 
     * @author Thorsten Berberich
     * 
     */
    protected class ConfirmListener implements android.view.View.OnClickListener {
        
        /**
         * Stores the entered data.
         */
        @Override
        public void onClick(View v) {
            // The chosen month
            int month = ChangeAppointmentDialog.this.dPicker.getMonth();
            int year = ChangeAppointmentDialog.this.dPicker.getYear();
            int day = ChangeAppointmentDialog.this.dPicker.getDayOfMonth();
            
            Calendar cal = new GregorianCalendar(year, month, day);
            
            SqlConnector.getInstance().changeAppointment(ChangeAppointmentDialog.this.appointment.getId(),
                    cal.getTime(), ChangeAppointmentDialog.this.desc.getText().toString());
            dismiss();
        }
        
    }
}
