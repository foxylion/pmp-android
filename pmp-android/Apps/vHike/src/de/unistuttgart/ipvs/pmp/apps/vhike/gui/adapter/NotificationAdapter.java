/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
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

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * Handles list elements where drivers or passengers are added/removed through
 * finding/accepting/denying/sending offers to hitchhikers
 * 
 * mWhichHitcher indicates which list is to handle. 0 handles passengers list 1
 * handles drivers list
 * 
 * Also gives user opportunity to open profile of a found hitchhiker
 * 
 * @author Alexander Wassiljew, Andre Nguyen
 * 
 */
public class NotificationAdapter extends BaseAdapter {
    
    private Context context;
    private List<Profile> hitchhikers;
    private Profile hitchhiker;
    private int mWhichHitcher;
    private int userID;
    
    
    public NotificationAdapter(Context context, List<Profile> hitchhikers, int whichHitcher) {
        this.context = context;
        this.hitchhikers = hitchhikers;
        this.mWhichHitcher = whichHitcher;
    }
    
    
    @Override
    public int getCount() {
        return this.hitchhikers.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return this.hitchhikers.get(position);
    }
    
    
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
    
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        this.hitchhiker = this.hitchhikers.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.hitchhiker_list, null);
        
        Button dismiss = (Button) entryView.findViewById(R.id.dismissBtn);
        RatingBar noti_rb = (RatingBar) entryView.findViewById(R.id.notification_ratingbar);
        final TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        final Button accept_invite = (Button) entryView.findViewById(R.id.acceptBtn);
        
        name.setText(this.hitchhiker.getUsername());
        name.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NotificationAdapter.this.context, ProfileActivity.class);
                
                NotificationAdapter.this.userID = NotificationAdapter.this.hitchhiker.getID();
                
                intent.putExtra("PROFILE_ID", NotificationAdapter.this.userID);
                intent.putExtra("MY_PROFILE", 1);
                
                NotificationAdapter.this.context.startActivity(intent);
            }
        });
        
        noti_rb.setRating((float) this.hitchhiker.getRating_avg());
        
        List<ViewObject> lqo = ViewModel.getInstance().getLVO();
        Log.i(this, "Position: " + position);
        final ViewObject actObject = lqo.get(position);
        
        if (this.mWhichHitcher == 0) {
            dismiss.setOnClickListener(new OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    ViewModel.getInstance().addToBanned(actObject.getViewObjectToBann());
                    ViewModel.getInstance().updateView(NotificationAdapter.this.mWhichHitcher);
                }
            });
        } else {
            dismiss.setOnClickListener(actObject.getDenieOfferClickListener());
        }
        
        switch (actObject.getStatus()) {
            case Constants.V_OBJ_SATUS_FOUND:
                accept_invite.setOnClickListener(actObject.getOnClickListener(this.mWhichHitcher));
                break;
            case Constants.V_OBJ_SATUS_INVITED:
                accept_invite.setOnClickListener(actObject.getOnClickListener(this.mWhichHitcher));
                accept_invite.setBackgroundResource(R.drawable.bg_waiting);
                
                break;
            case Constants.V_OBJ_SATUS_AWAIT_ACCEPTION:
                accept_invite.setOnClickListener(actObject.getOnClickListener(this.mWhichHitcher));
                accept_invite.setBackgroundResource(R.drawable.bg_waiting);
                break;
            case Constants.V_OBJ_SATUS_ACCEPTED:
                accept_invite.setOnClickListener(actObject.getOnClickListener(this.mWhichHitcher));
                accept_invite.setBackgroundResource(R.drawable.bg_check);
                break;
            case Constants.V_OBJ_SATUS_PICKED_UP:
                accept_invite.setOnClickListener(actObject.getOnClickListener(this.mWhichHitcher));
                accept_invite.setBackgroundResource(R.drawable.bg_disabled);
                name.setTextColor(Color.BLUE);
                break;
        }
        
        return entryView;
    }
    
}
