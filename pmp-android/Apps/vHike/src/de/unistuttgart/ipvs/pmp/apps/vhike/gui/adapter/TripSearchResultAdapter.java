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
    
    
    public TripSearchResultAdapter(Context context, int layoutResourceId, ArrayList<TripSearchResult> itemlist) {
        super(context, layoutResourceId, itemlist);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.list = itemlist;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        
        if (itemView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(this.layoutResourceId, parent, false);
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
        TripSearchResult o = this.list.get(position);
        
        v.departure.setText(o.departure);
        v.destination.setText(o.destination);
        v.date.setText((new FriendlyDateFormatter(this.context)).format(o.date));
        if (v.stopovers != null) {
            v.stopovers.setText(o.stopovers);
        }
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
