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
        super(context, R.layout.layout_my_trip_list_item, trips);
        this.context = context;
        this.trips = trips;
    }
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
        // Inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.layout_my_trip_list_item, parent, false);
        
        // set views
        TextView departure = (TextView) itemView.findViewById(R.id.myTrip_destination);
        departure.setText(trips.get(position).destination);
        
        TextView date = (TextView) itemView.findViewById(R.id.myTrip_date);
        Calendar c = GregorianCalendar.getInstance();
        date.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(
                c.getTime()));
        
        int i = 0;
        
        TextView passenger = (TextView) itemView.findViewById(R.id.myTrip_passengers);
        i = trips.get(position).numberOfPassengers;
        passenger.setText(String.valueOf(i));
        if (i > 0) {
            passenger.setBackgroundResource(R.drawable.bg_round_blue);
        }
        
        TextView request = (TextView) itemView.findViewById(R.id.myTrip_requests);
        i = trips.get(position).numberOfOffers;
        request.setText(String.valueOf(i));
        if (i > 0) {
            request.setBackgroundResource(R.drawable.bg_round_green);
        }
        
        TextView message = (TextView) itemView.findViewById(R.id.myTrip_messages);
        i = trips.get(position).numberOfNewMessages;
        message.setText(String.valueOf(i));
        if (i > 0) {
            message.setBackgroundResource(R.drawable.bg_round_red);
        }
        
        return itemView;
    }
}
