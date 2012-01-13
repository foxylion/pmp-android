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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Creates the view for the header of a section
 * 
 * @author Thorsten Berberich
 * 
 */
public class HeaderAdapter extends BaseAdapter {
    
    /**
     * Context of the app
     */
    private Context context;
    
    /**
     * Stores the {@link Date#getTime()} for every header of a section
     */
    ArrayList<Long> headers = new ArrayList<Long>();
    
    
    /**
     * Creates a new header
     * 
     * @param context
     *            context of the app
     */
    public HeaderAdapter(Context context) {
        this.context = context;
    }
    
    
    @Override
    public int getCount() {
        return headers.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return headers.get(position);
    }
    
    
    @Override
    public long getItemId(int arg0) {
        return 0;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_header, null);
        }
        
        Long dateTime = headers.get(position);
        
        if (dateTime != null) {
            TextView header = (TextView) view.findViewById(R.id.list_header_title);
            Date date = new Date(dateTime);
            Calendar cal = new GregorianCalendar();
            cal.setTime(date);
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.FULL);
            header.setText(dateFormat.format(cal.getTime()));
        }
        return view;
    }
    
    
    /**
     * Adds a section
     * 
     * @param section
     *            {@link Date#getTime()}
     */
    public void add(Long section) {
        if (!headers.contains(section))
            headers.add(section);
    }
    
    
    /**
     * Sorts the headers
     */
    public void sort() {
        Collections.sort(headers);
    }
    
    
    /**
     * Removes the given header
     * 
     * @param key
     *            {@link Date#getTime()} of the header
     *            key of the header
     */
    public void remove(Long key) {
        headers.remove(key);
    }
    
    
    /**
     * Clears everything
     */
    public void clear() {
        headers.clear();
    }
    
}
