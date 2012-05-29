package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactTrip;

public class MyTripAdapter extends ArrayAdapter<CompactTrip> {
    
    private Context context;
    private ArrayList<CompactTrip> trips;
    
    
    public MyTripAdapter(Context context, ArrayList<CompactTrip> trips) {
        super(context, R.layout.list_item_my_trip, trips);
        this.context = context;
        this.trips = trips;
    }
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View itemView = convertView;
        
        if (itemView == null) {
            // Inflate layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_my_trip, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.destination = (TextView) itemView.findViewById(R.id.myTrip_destination);
            viewHolder.date = (TextView) itemView.findViewById(R.id.myTrip_date);
            viewHolder.no1 = (TextView) (TextView) itemView.findViewById(R.id.myTrip_passengers);
            viewHolder.no2 = (TextView) itemView.findViewById(R.id.myTrip_requests);
            viewHolder.no3 = (TextView) itemView.findViewById(R.id.myTrip_messages);
            itemView.setTag(viewHolder);
        }
        
        ViewHolder holder = (ViewHolder) itemView.getTag();
        
        // set views
        holder.destination.setText(trips.get(position).destination);
        
        Calendar c = GregorianCalendar.getInstance();
        holder.date.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)
                .format(c.getTime()));
        
        int i = 0;
        i = trips.get(position).numberOfPassengers;
        holder.no1.setText(String.valueOf(i));
        if (i > 0) {
            holder.no1.setBackgroundResource(R.drawable.bg_round_blue);
        }
        
        i = trips.get(position).numberOfOffers;
        holder.no2.setText(String.valueOf(i));
        if (i > 0) {
            holder.no2.setBackgroundResource(R.drawable.bg_round_green);
        }
        
        i = trips.get(position).numberOfNewMessages;
        holder.no3.setText(String.valueOf(i));
        if (i > 0) {
            holder.no3.setBackgroundResource(R.drawable.bg_round_red);
        }
        
        return itemView;
    }
    
    private static class ViewHolder {
        
        public TextView destination;
        public TextView date;
        public TextView no1;
        public TextView no2;
        public TextView no3;
    }
}
