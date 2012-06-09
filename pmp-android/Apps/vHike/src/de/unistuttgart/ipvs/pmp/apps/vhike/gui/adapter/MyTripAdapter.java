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
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.FriendlyDateFormatter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactTrip;

public class MyTripAdapter extends ArrayAdapter<CompactTrip> {
    
    private Context context;
    private ArrayList<CompactTrip> trips;
    
    
    public MyTripAdapter(Context context, ArrayList<CompactTrip> trips) {
        super(context, R.layout.list_item_my_trip, trips);
        this.context = context;
        this.trips = trips;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View itemView = convertView;
        
        if (itemView == null) {
            // Inflate layout
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_my_trip, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.destination = (TextView) itemView.findViewById(R.id.myTrip_destination);
            viewHolder.date = (TextView) itemView.findViewById(R.id.myTrip_date);
            viewHolder.no1 = (TextView) itemView.findViewById(R.id.myTrip_passengers);
            viewHolder.no2 = (TextView) itemView.findViewById(R.id.myTrip_requests);
            viewHolder.no3 = (TextView) itemView.findViewById(R.id.myTrip_messages);
            itemView.setTag(viewHolder);
        }
        
        ViewHolder holder = (ViewHolder) itemView.getTag();
        
        // set views
        holder.destination.setText(this.trips.get(position).destination);
        
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(this.trips.get(position).startTime);
        FriendlyDateFormatter df = new FriendlyDateFormatter(getContext());
        holder.date.setText(df.format(c));
        
        int i = 0;
        i = this.trips.get(position).numberOfPassengers;
        holder.no1.setText(String.valueOf(i));
        if (i > 0) {
            holder.no1.setBackgroundResource(R.drawable.bg_round_blue);
        } else {
            holder.no1.setBackgroundResource(R.drawable.bg_round_gray);
        }
        
        i = this.trips.get(position).numberOfOffers;
        holder.no2.setText(String.valueOf(i));
        if (i > 0) {
            holder.no2.setBackgroundResource(R.drawable.bg_round_green);
        } else {
            holder.no2.setBackgroundResource(R.drawable.bg_round_gray);
        }
        
        i = this.trips.get(position).numberOfNewMessages;
        holder.no3.setText(String.valueOf(i));
        if (i > 0) {
            holder.no3.setBackgroundResource(R.drawable.bg_round_red);
        } else {
            holder.no3.setBackgroundResource(R.drawable.bg_round_gray);
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
