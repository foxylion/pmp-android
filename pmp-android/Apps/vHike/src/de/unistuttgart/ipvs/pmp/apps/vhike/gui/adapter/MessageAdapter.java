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
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactMessage;

public class MessageAdapter extends ArrayAdapter<CompactMessage> {
    
    private Context context;
    private ArrayList<CompactMessage> messages;
    
    
    public MessageAdapter(Context context, ArrayList<CompactMessage> messages) {
        super(context, R.layout.list_item_message, messages);
        this.context = context;
        this.messages = messages;
    }
    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View itemView = convertView;
        CompactMessage msg = this.messages.get(position);
        if (itemView == null) {
            // Inflate layout
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.list_item_message, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.text = (TextView) itemView.findViewById(R.id.text);
            viewHolder.offer = itemView.findViewById(R.id.offer);
            viewHolder.rating = (RatingBar) itemView.findViewById(R.id.rating);
            itemView.setTag(viewHolder);
        }
        
        ViewHolder holder = (ViewHolder) itemView.getTag();
        holder.text.setText(msg.sender.name);
        if (!msg.isOffer) {
            holder.offer.setVisibility(View.GONE);
        } else {
            holder.offer.setVisibility(View.VISIBLE);
        }
        holder.rating.setRating(Float.valueOf(Double.toString(msg.sender.rating)));
        return itemView;
    }
    
    private static class ViewHolder {
        
        public TextView text;
        public View offer;
        public RatingBar rating;
        
    }
}
