package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;

public class MyTripAdapter extends ArrayAdapter<String> {
    
    private Context context;
    private String[] values;
    
    
    public MyTripAdapter(Context context, String[] values) {
        super(context, R.layout.layout_my_trip_list_item, values);
        this.context = context;
        this.values = values;
    }
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.layout_my_trip_list_item, parent, false);
        TextView departure = (TextView) itemView.findViewById(R.id.myTrip_destination);
        departure.setText(values[position]);
        TextView date = (TextView) itemView.findViewById(R.id.myTrip_date);
        TextView time = (TextView) itemView.findViewById(R.id.myTrip_time);
        Calendar c = GregorianCalendar.getInstance();
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        date.setText(df.format(c.getTime()));
        time.setText(SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(c.getTime()));
        return itemView;
    }
    
}
