package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter;

import java.util.ArrayList;
import java.util.Comparator;
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
    public final TreeMap<String, AppointmentArrayAdapter> sections = new TreeMap<String, AppointmentArrayAdapter>(
            new StringComparator());
    
    /**
     * {@link ArrayAdapter} with the headers
     */
    public final ArrayAdapter<String> headers;
    
    public final static int TYPE_HEADER_OF_A_SECTION = 0;
    
    
    /**
     * Initializes the headers {@link ArrayAdapter}
     * 
     * @param context
     *            context of the app to load the layout with {@link R}
     */
    public SeparatedListAdapter(Context context) {
        headers = new ArrayAdapter<String>(context, R.layout.list_header);
    }
    
    
    /**
     * Adds a new section and a new header, header and sections will be ordered by date
     * 
     * @param section
     *            will be shown in the header
     * @param adapter
     *            {@link AppointmentArrayAdapter} with the {@link Appointment}s to show
     */
    public void addSection(String section, AppointmentArrayAdapter adapter) {
        this.headers.add(section);
        headers.sort(new StringComparator());
        this.sections.put(section, adapter);
    }
    
    
    /**
     * Removes all empty stuff out of the sections and headers
     */
    public void removeEmptyHeadersAndSections() {
        
        // Stores the things that will be deleted out of the sections
        ArrayList<String> toDel = new ArrayList<String>();
        for (Entry<String, AppointmentArrayAdapter> entry : sections.entrySet()) {
            
            // Delete the headers if the section is empty
            if (entry.getValue().getCount() == 0) {
                headers.remove(entry.getKey());
                
                // Remember the section to delete
                toDel.add(entry.getKey());
            }
        }
        
        // Delete the sections
        for (String del : toDel) {
            sections.remove(del);
        }
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
            for (String section : this.sections.keySet()) {
                AppointmentArrayAdapter adapter = sections.get(section);
                int size = adapter.getCount() + 1;
                convertView = null;
                // check if position inside this section
                if (position == 0) {
                    System.out.println(sectionnum);
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
    
    /**
     * Comparator to sort the headers and sections
     * 
     * @author Thorsten Berberich
     * 
     */
    private class StringComparator implements Comparator<String> {
        
        @Override
        public int compare(String lhs, String rhs) {
            long d1 = Date.parse(lhs);
            long d2 = Date.parse(rhs);
            return (int) (d1 - d2);
        }
        
    }
}
