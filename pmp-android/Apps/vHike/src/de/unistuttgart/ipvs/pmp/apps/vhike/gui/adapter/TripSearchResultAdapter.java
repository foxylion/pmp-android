package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.FriendlyDateFormatter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripSearchResult;

public class TripSearchResultAdapter extends ArrayAdapter<TripSearchResult> {
    
    private int layoutResourceId;
    private Context context;
    private ArrayList<TripSearchResult> list;
    
    
    public TripSearchResultAdapter(Context context, int layoutResourceId, ArrayList<TripSearchResult> itemlist) {
        super(context, layoutResourceId, itemlist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        list = itemlist;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(layoutResourceId, parent, false);
            ViewHolder v = new ViewHolder();
            
            v.departure = (TextView) itemView.findViewById(R.id.departure);
            v.destination = (TextView) itemView.findViewById(R.id.destination);
            v.date = (TextView) itemView.findViewById(R.id.datetime);
            v.stopovers = (TextView) itemView.findViewById(R.id.stopovers);
            v.user = (TextView) itemView.findViewById(R.id.username);
            v.seat = (TextView) itemView.findViewById(R.id.noOfSeats);
            v.rating = (RatingBar) itemView.findViewById(R.id.rating);
            
            itemView.setTag(v);
        }
        
        ViewHolder v = (ViewHolder) itemView.getTag();
        TripSearchResult o = list.get(position);
        
        v.departure.setText(o.departure);
        v.destination.setText(o.destination);
        v.date.setText((new FriendlyDateFormatter(context)).format(o.date));
        if (v.stopovers != null)
            v.stopovers.setText(o.stopovers);
        v.user.setText(o.username);
        v.seat.setText(String.valueOf(o.seat));
        v.rating.setRating(o.rating);
        
        return itemView;
    }
    
    private static class ViewHolder {
        
        public TextView departure;
        public TextView destination;
        public TextView date;
        public TextView stopovers;
        public TextView user;
        public TextView seat;
        public RatingBar rating;
    }
}
