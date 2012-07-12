/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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
    
    private boolean isTrip = true; // determine whether the item is a trip or a query 
    
    
    public TripSearchResultAdapter(Context context, int layoutResourceId, ArrayList<TripSearchResult> itemlist) {
        super(context, layoutResourceId, itemlist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.list = itemlist;
        if (layoutResourceId == R.layout.list_item_search_result_hiker)
            isTrip = false;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(this.layoutResourceId, parent, false);
            ViewHolder v = new ViewHolder();
            
            v.rowDeparture = itemView.findViewById(R.id.rowDeparture);
            v.departure = (TextView) itemView.findViewById(R.id.departure);
            v.destination = (TextView) itemView.findViewById(R.id.destination);
            v.date = (TextView) itemView.findViewById(R.id.datetime);
            if (isTrip) {
                v.stopovers = (TextView) itemView.findViewById(R.id.stopovers);
                v.rowStopovers = itemView.findViewById(R.id.rowStops);
            }
            v.user = (TextView) itemView.findViewById(R.id.username);
            v.seat = (TextView) itemView.findViewById(R.id.noOfSeats);
            v.rating = (RatingBar) itemView.findViewById(R.id.rating);
            
            itemView.setTag(v);
        }
        
        ViewHolder v = (ViewHolder) itemView.getTag();
        TripSearchResult o = this.list.get(position);
        
        if (o.departure != null && !o.departure.equals(""))
            v.departure.setText(o.departure);
        else
            v.rowDeparture.setVisibility(View.GONE);
        
        v.destination.setText(o.destination);
        v.date.setText((new FriendlyDateFormatter(this.context)).format(o.date));
        
        if (v.stopovers != null) {
            v.stopovers.setText(o.stopovers);
        } else {
            v.rowDeparture.setVisibility(View.GONE);
        }
        v.user.setText(o.username);
        v.seat.setText(String.valueOf(o.seat));
        v.rating.setRating(o.rating);
        
        return itemView;
    }
    
    private static class ViewHolder {
        
        public View rowDeparture;
        public View rowStopovers;
        public TextView departure;
        public TextView destination;
        public TextView date;
        public TextView stopovers;
        public TextView user;
        public TextView seat;
        public RatingBar rating;
    }
}
