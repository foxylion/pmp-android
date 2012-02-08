package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;
import java.util.Timer;

import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.CheckAcceptedOffers;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.OfferObject;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.QueryObject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Handles list elements where drivers or passengers are added/removed through
 * finding/accepting/denying hitchhikers
 * 
 * mWhichHitcher indicates which list is to handle. 0 handles passengers list 1
 * handles drivers list
 * 
 * @author andres
 * 
 */
public class NotificationAdapter extends BaseAdapter {
    
    private Context context;
    private Controller ctrl;
    private List<Profile> hitchhikers;
    private Profile hitchhiker;
    private Profile me;
    private int mWhichHitcher;
    private int queryID;
    private int offerID;
    private int userID;
    private int driverID;
    private MapView mapView;
    
    private CheckAcceptedOffers cao;
    private Timer timer;
    
    
    public NotificationAdapter(Context context, List<Profile> hitchhikers, int whichHitcher, MapView mapView) {
        this.context = context;
        this.hitchhikers = hitchhikers;
        ctrl = new Controller();
        mWhichHitcher = whichHitcher;
        this.mapView = mapView;
    }
    
    
    @Override
    public int getCount() {
        return hitchhikers.size();
    }
    
    
    @Override
    public Object getItem(int position) {
        return hitchhikers.get(position);
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
        hitchhiker = hitchhikers.get(position);
        
        /* load the layout from the xml file */
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout entryView = (LinearLayout) inflater.inflate(R.layout.hitchhiker_list, null);
        
        Button dismiss = (Button) entryView.findViewById(R.id.dismissBtn);
        RatingBar noti_rb = (RatingBar) entryView.findViewById(R.id.notification_ratingbar);
        final TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
        final Button accept_invite = (Button) entryView.findViewById(R.id.acceptBtn);
        
        // determine which id to receive
        if (mWhichHitcher == 0) {
            final List<QueryObject> lqo = Model.getInstance().getQueryHolder();
            Log.i(this, "Position: " + position);
            Log.i(this, "LQO is null");
            if (lqo != null) {
                Log.i(this, "LQO size: " + lqo.size());
                queryID = lqo.get(position).getQueryid();
                userID = lqo.get(position).getUserid();
            }
            
            if (Model.getInstance().isPicked(hitchhiker.getID())) {
                accept_invite.setBackgroundResource(R.drawable.bg_disabled);
                accept_invite.setEnabled(false);
            }
            
        } else {
            List<OfferObject> loo = Model.getInstance().getOfferHolder();
            offerID = loo.get(position).getOffer_id();
            driverID = loo.get(position).getUser_id();
        }
        
        if (Model.getInstance().isInInvitedList(hitchhiker.getID())) {
            
            accept_invite.setBackgroundResource(R.drawable.bg_waiting);
            Log.i(this, "Offer waiting " + hitchhiker.getID());
            accept_invite.invalidate();
            
            if (Model.getInstance().isAccepted(hitchhiker.getID())) {
                accept_invite.setBackgroundColor(R.drawable.bg_check);
                accept_invite.invalidate();
                Log.i(this, "Offer waiting to accepted " + hitchhiker.getID());
            }
        }
        
        dismiss.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if (mWhichHitcher == 0) {
                    MapModel.getInstance().getHitchPassengers().remove(position);
                    MapModel.getInstance().getDriverOverlayList(mapView).remove(position + 1);
                    Model.getInstance().addToBannList(userID);
                    mapView.invalidate();
                    notifyDataSetChanged();
                } else {
                    switch (ctrl.handleOffer(Model.getInstance().getSid(), 1, false)) {
                        case Constants.STATUS_HANDLED:
                            Toast.makeText(context, "HANDLED: DENY", Toast.LENGTH_SHORT).show();
                            // remove driver from list if denied
                            MapModel.getInstance().getHitchDrivers().remove(position);
                            MapModel.getInstance().getPassengerOverlayList(mapView).remove(position + 1);
                            mapView.invalidate();
                            notifyDataSetChanged();
                            break;
                        case Constants.STATUS_INVALID_OFFER:
                            Toast.makeText(context, "INVALID OFFER", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.STATUS_INVALID_USER:
                            Toast.makeText(context, "INVALID_USER", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                
            }
        });
        
        name.setText(hitchhiker.getUsername());
        name.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProfileActivity.class);
                List<QueryObject> lqo = Model.getInstance().getQueryHolder();
                List<OfferObject> loo = Model.getInstance().getOfferHolder();
                
                if (mWhichHitcher == 0) {
                    userID = lqo.get(position).getUserid();
                } else {
                    userID = loo.get(position).getUser_id();
                }
                
                if (mWhichHitcher == 0) {
                    intent.putExtra("PROFILE_ID", userID);
                } else {
                    intent.putExtra("PROFILE_ID", driverID);
                }
                
                intent.putExtra("MY_PROFILE", 1);
                
                context.startActivity(intent);
            }
        });
        
        noti_rb.setRating((float) hitchhiker.getRating_avg());
        me = Model.getInstance().getOwnProfile();
        
        accept_invite.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                
                if (mWhichHitcher == 0) {
                    final List<QueryObject> lqo = Model.getInstance().getQueryHolder();
                    queryID = lqo.get(position).getQueryid();
                    userID = lqo.get(position).getUserid();
                }
                
                if (mWhichHitcher == 0) {
                    switch (ctrl.sendOffer(Model.getInstance().getSid(), Model.getInstance().getTripId(), queryID,
                            me.getUsername() + ": Need a ride?")) {
                        case Constants.STATUS_SENT:
                            
                            accept_invite.setBackgroundResource(R.drawable.bg_waiting);
                            Model.getInstance().addToInvitedUser(userID);
                            
                            // check for offer updates for this button
                            cao = new CheckAcceptedOffers(accept_invite, name, userID);
                            cao.run();
                            timer = new Timer();
                            timer.schedule(cao, 300, 10000);
                            
                            break;
                        case Constants.STATUS_INVALID_TRIP:
                            Toast.makeText(context, "STATUS_INVALID_TRIP", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.STATUS_INVALID_QUERY:
                            Toast.makeText(context, "INVALID_QUERY", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.STATUS_ALREADY_SENT:
                            
                            Toast.makeText(context, "ALREADY SENT", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    
                } else {
                    
                    switch (ctrl.handleOffer(Model.getInstance().getSid(), offerID, true)) {
                        case Constants.STATUS_HANDLED:
                            Toast.makeText(context, "HANDLED: ACCEPT", Toast.LENGTH_SHORT).show();
                            
                            accept_invite.setBackgroundResource(R.drawable.bg_check);
                            accept_invite.refreshDrawableState();
                            accept_invite.invalidate();
                            
                            break;
                        case Constants.STATUS_INVALID_OFFER:
                            Toast.makeText(context, "INVALID_OFFER", Toast.LENGTH_SHORT).show();
                            break;
                        case Constants.STATUS_INVALID_USER:
                            Toast.makeText(context, "INVALID_USER", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
                
                accept_invite.invalidate();
                
            }
        });
        
        return entryView;
    }
    
}
