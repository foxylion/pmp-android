package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Map.Entry;
import java.util.TreeMap;

import de.unistuttgart.ipvs.pmp.Log;
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
    public TreeMap<String, AppointmentArrayAdapter> sections = new TreeMap<String, AppointmentArrayAdapter>(
            new StringComparator());
    
    /**
     * {@link ArrayAdapter} with the headers
     */
    public ArrayAdapter<String> headers;
    
    public final static int TYPE_HEADER_OF_A_SECTION = 0;
    
    public Context context;
    
    
    /**
     * Initializes the headers {@link ArrayAdapter}
     * 
     * @param context
     *            context of the app to load the layout with {@link R}
     */
    public SeparatedListAdapter(Context context) {
        this.context = context;
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
     * Calculates the position of the header of the current date
     * 
     * @return position of the actual header
     */
    public int getActualAppointmentPosition() {
        
        // Skipped entries
        int skipped = 0;
        
        // Key to count the headers
        String actualKey = "";
        
        for (String key : sections.keySet()) {
            long parsed = Date.parse(key);
            Date parsedDate = new Date(parsed);
            Date today = new Date();
            
            // Year is to small, add all entries to skipped
            if (parsedDate.getYear() < today.getYear()) {
                
                // If the key is not known, add one because of the header
                if (!key.equals(actualKey)) {
                    skipped++;
                    actualKey = key;
                }
                skipped = skipped + sections.get(key).getCount();
                continue;
                
                // Year is to equal, but month is too small, add all entries to skipped
            } else if (parsedDate.getYear() == today.getYear() && parsedDate.getMonth() < today.getMonth()) {
                
                // If the key is not known, add one because of the header
                if (!key.equals(actualKey)) {
                    skipped++;
                    actualKey = key;
                }
                skipped = skipped + sections.get(key).getCount();
                continue;
                
                // Year and month are equal, but day is too small, add all entries to skipped
            } else if (parsedDate.getYear() == today.getYear() && parsedDate.getMonth() == today.getMonth()
                    && parsedDate.getDay() < today.getDay()) {
                
                // If the key is not known, add one because of the header
                if (!key.equals(actualKey)) {
                    skipped++;
                    actualKey = key;
                }
                skipped = skipped + sections.get(key).getCount();
                continue;
            } else {
                
                // Entry with a date found that was later
                return skipped;
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
    
    
    public void reset() {
        sections = new TreeMap<String, AppointmentArrayAdapter>(new StringComparator());
        headers = new ArrayAdapter<String>(context, R.layout.list_header);
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
            try {
                long d1 = Date.parse(lhs);
                long d2 = Date.parse(rhs);
                Date d11 = new Date(d1);
                Date d12 = new Date(d2);
                return d11.compareTo(d12);
            } catch (IllegalArgumentException e) {
                Log.e("Could not parse date", e);
            }
            return 0;
        }
        
    }
}
