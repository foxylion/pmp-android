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
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Appointment;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adds separate sections with a header to the list of appointments
 * 
 * @author Thorsten Berberich
 * 
 */
public class SeparatedListAdapter extends BaseAdapter {
    
    /**
     * Stores the information of the sections
     */
    public TreeMap<Long, AppointmentArrayAdapter> sections = new TreeMap<Long, AppointmentArrayAdapter>();
    
    /**
     * {@link ArrayAdapter} with the headers
     */
    public HeaderAdapter headers;
    
    /**
     * Constant to identify a header
     */
    public final static int TYPE_HEADER_OF_A_SECTION = 0;
    
    /**
     * Context of the app
     */
    public Context context;
    
    
    /**
     * Initializes the headers {@link ArrayAdapter}
     * 
     * @param context
     *            context of the app to load the layout with {@link R}
     */
    public SeparatedListAdapter(Context context) {
        this.context = context;
        headers = new HeaderAdapter(context);
    }
    
    
    /**
     * Adds a new section and a new header, header and sections will be ordered by date
     * 
     * @param section
     *            will be shown in the header
     * @param adapter
     *            {@link AppointmentArrayAdapter} with the {@link Appointment}s to show
     */
    public void addSection(Long section, AppointmentArrayAdapter adapter) {
        this.headers.add(section);
        headers.sort();
        this.sections.put(section, adapter);
    }
    
    
    /**
     * Calculates the position of the header of the current date or if this date is 
     * not found to the date before
     * 
     * @return position of the actual header
     */
    public int getActualAppointmentPosition() {
        
        // Skipped entries
        int skipped = 0;
        
        // Last key that was before today
        long keyBefore = 0;
        Date today = new Date();
        
        for (Long key : sections.keySet()) {
            Date parsedDate = new Date(key);
            if (parsedDate.before(today)) {
                keyBefore = key;
                
                // Added the entries that were in this sections +1 for the header
                skipped = skipped + sections.get(key).getCount() + 1;
            } else {
                /*
                 * Skipped is now at the end of the wanted section
                 * -> subtract the number of entries that are in the wanted section and subtract 1 for the 
                 *    header of this section
                 */
                return skipped - 1 - sections.get(keyBefore).getCount();
            }
        }
        
        // Nothing found
        return 0;
    }
    
    
    /**
     * Removes all empty stuff out of the sections and headers
     */
    public void removeEmptyHeadersAndSections() {
        
        // Stores the things that will be deleted out of the sections
        ArrayList<Long> toDel = new ArrayList<Long>();
        for (Entry<Long, AppointmentArrayAdapter> entry : sections.entrySet()) {
            
            // Delete the headers if the section is empty
            if (entry.getValue().getCount() == 0) {
                headers.remove(entry.getKey());
                
                // Remember the section to delete
                toDel.add(entry.getKey());
            }
        }
        
        // Delete the sections
        for (Long del : toDel) {
            sections.remove(del);
        }
    }
    
    
    public void reset() {
        sections.clear();
        headers.clear();
    }
    
    
    @Override
    public int getCount() {
        int totalSections = 0;
        for (AppointmentArrayAdapter adapter : this.sections.values()) {
            // Counts the sections + one header for every section
            totalSections += adapter.getCount() + 1;
        }
        return totalSections;
    }
    
    
    @Override
    public boolean isEnabled(int position) {
        // Headers are disabled
        return (getItemViewType(position) != TYPE_HEADER_OF_A_SECTION);
    }
    
    
    @Override
    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            AppointmentArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if the position is inside this section  
            if (position == 0)
                return section;
            if (position < size)
                return adapter.getItem(position - 1);
            
            // otherwise jump into the next section  
            position -= size;
        }
        return null;
    }
    
    
    @Override
    public int getViewTypeCount() {
        int total = 1;
        for (AppointmentArrayAdapter adapter : this.sections.values()) {
            total += adapter.getViewTypeCount();
        }
        return total;
    }
    
    
    @Override
    public int getItemViewType(int position) {
        int type = 1;
        for (Object section : this.sections.keySet()) {
            AppointmentArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if position inside this section  
            if (position == 0)
                return TYPE_HEADER_OF_A_SECTION;
            if (position < size)
                return type + adapter.getItemViewType(position - 1);
            
            // otherwise jump into next section  
            position -= size;
            type += adapter.getViewTypeCount();
        }
        return -1;
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int sectionnum = 0;
        if (!headers.isEmpty()) {
            for (Long section : this.sections.keySet()) {
                AppointmentArrayAdapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;
                convertView = null;
                
                // check if position inside this section
                if (position == 0) {
                    return headers.getView(sectionnum, convertView, parent);
                }
                if (position < size)
                    return adapter.getView(position - 1, convertView, parent);
                
                // otherwise jump into next section  
                position -= size;
                sectionnum++;
            }
        }
        return new TextView(Model.getInstance().getContext());
    }
}
