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
    
    
    public View getView(int position, View convertView, ViewGroup parent) {
        
        View itemView = convertView;
        CompactMessage msg = messages.get(position);
        if (itemView == null) {
            // Inflate layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        if (msg.sender.rating == 0) {
            holder.rating.setVisibility(View.GONE);
        } else {
            holder.rating.setVisibility(View.VISIBLE);
            holder.rating.setNumStars(msg.sender.rating);
        }
        return itemView;
    }
    
    private static class ViewHolder {
        
        public TextView text;
        public View offer;
        public RatingBar rating;
        
    }
}
