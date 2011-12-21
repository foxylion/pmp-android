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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter;

import java.util.ArrayList;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Implements the {@link ArrayAdapter} to show the custom entries
 * 
 * @author Thorsten Berberich
 * 
 */
public class AppointmentArrayAdapter extends ArrayAdapter<Appointment> {
    
    /**
     * Appointment list
     */
    private ArrayList<Appointment> items;
    
    /**
     * Context of the app
     */
    private Context context;
    
    
    /**
     * Constructor
     * 
     * @param context
     *            context of the app
     * @param textViewResourceId
     *            ID of the row xml element
     * @param items
     *            appointment items
     */
    public AppointmentArrayAdapter(Context context, int textViewResourceId, ArrayList<Appointment> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.items = items;
    }
    
    
    @Override
    public int getCount() {
        return items.size();
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }
        
        Appointment appointm = items.get(position);
        if (appointm != null) {
            TextView textTop = (TextView) view.findViewById(R.id.toptext);
            TextView textBottom = (TextView) view.findViewById(R.id.bottomtext);
            if (textTop != null) {
                textTop.setText(appointm.getDescrpition());
            }
            if (textBottom != null) {
                textBottom.setText(appointm.getDateString());
            }
        }
        return view;
    }
}
