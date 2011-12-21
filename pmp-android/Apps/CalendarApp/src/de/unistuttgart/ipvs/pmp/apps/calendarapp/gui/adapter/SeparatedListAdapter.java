package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.adapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SeparatedListAdapter extends BaseAdapter {
    
    public final Map<String, AppointmentArrayAdapter> sections = new LinkedHashMap<String, AppointmentArrayAdapter>();
    public final ArrayAdapter<String> headers;
    public final static int TYPE_SECTION_HEADER = 0;
    
    
    public SeparatedListAdapter(Context context) {
        headers = new ArrayAdapter<String>(context, R.layout.list_header);
    }
    
    
    public void addSection(String section, AppointmentArrayAdapter adapter) {
        this.headers.add(section);
        // TODO Sortieren -> Header der richtigen Section zuweisenF
        headers.sort(new Comparator<String>() {

            @Override
            public int compare(String lhs, String rhs) {
                long d1 = Date.parse(lhs);
                long d2 = Date.parse(rhs);
                return (int) (d1-d2);
            }
        });
        this.sections.put(section, adapter);
    }
    
    public void removeEmptyHeadersAndSections(){
        for (Entry<String, AppointmentArrayAdapter> entry: sections.entrySet()){
            if (entry.getValue().getCount() == 0){
                headers.remove(entry.getKey());
                sections.remove(entry.getKey());
            }
        }
    }
    
    
    @Override
    public int getCount() {
        // total together all sections, plus one for each section header  
        int total = 0;
        for (AppointmentArrayAdapter adapter : this.sections.values())
            total += adapter.getCount() + 1;
        return total;
    }
    
    
    @Override
    public boolean isEnabled(int position) {
        return (getItemViewType(position) != TYPE_SECTION_HEADER);
    }
    
    
    @Override
    public Object getItem(int position) {
        for (Object section : this.sections.keySet()) {
            AppointmentArrayAdapter adapter = sections.get(section);
            int size = adapter.getCount() + 1;
            
            // check if position inside this section  
            if (position == 0)
                return section;
            if (position < size)
                return adapter.getItem(position - 1);
            
            // otherwise jump into next section  
            position -= size;
        }
        return null;
    }
    
    
    @Override
    public int getViewTypeCount() {
        // assume that headers count as one, then total all sections  
        int total = 1;
        for (AppointmentArrayAdapter adapter : this.sections.values())
            total += adapter.getViewTypeCount();
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
                return TYPE_SECTION_HEADER;
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
                if (position == 0){
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
}
