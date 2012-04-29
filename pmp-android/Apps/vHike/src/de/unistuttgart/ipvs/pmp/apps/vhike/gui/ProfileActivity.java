package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

/**
 * Displays the profile of the current user, some other vHike-User to simply display users personal information or to
 * rate a user after a ride through the history activity
 * 
 * @author Andre Nguyen
 * 
 */
public class ProfileActivity extends ResourceGroupReadyActivity {
    
    private Profile profile;
    private RatingBar rb;
    private Handler handler;
    private Controller ctrl;
    static final String[] RECENT_RIDES = new String[] { "01.01.2011, Stuttgart", "02.01.2011, Berlin",
            "03.01.2011, Vaihingen", "..." };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_profile);
        handler = new Handler();
        if (getvHikeRG(this) != null)
            setUpProfile();
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            handler.post(new Runnable() {
                
                @Override
                public void run() {
                    setUpProfile();
                }
            });
        }
    }
    
    
    private void setUpProfile() {
        
        if (rgvHike != null) {
            /**
             * MY_PROFILE: gives info about opening ones own profile or from someone else
             * PROFILE_ID: the profile id from some vHike-User
             * RATING_MODUS: if rating modus is set the ratingbar is editable
             * TRIP_ID: TRIP_ID which is needed to rate a user from a past ride
             * 
             */
            int whoIsIt = getIntent().getExtras().getInt("MY_PROFILE");
            final int profileID = getIntent().getExtras().getInt("PROFILE_ID");
            int ratingModus = getIntent().getExtras().getInt("RATING_MODUS");
            final int tripID = getIntent().getExtras().getInt("TRIP_ID");
            
            if (whoIsIt == 0) {
                this.profile = Model.getInstance().getOwnProfile();
            } else {
                ctrl = new Controller(rgvHike);
                this.profile = ctrl.getProfile(Model.getInstance().getSid(), profileID);
            }
            
            final Button anonymous = (Button) findViewById(R.id.btn_anonymous);
            anonymous.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // if (anonymous = active)
                    anonymous.setBackgroundResource(R.drawable.btn_anonymous);
                }
            });
            
            final Button observation = (Button) findViewById(R.id.btn_observation);
            observation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   
                    // if observation = active
                    observation.setBackgroundResource(R.drawable.btn_observation);
                }
            });
            
            TextView tv_username = (TextView) findViewById(R.id.tv_username);
            tv_username.setText(this.profile.getUsername());
            
            EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
            et_firstname.setText(this.profile.getFirstname());
            
            EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
            et_lastname.setText(this.profile.getLastname());
            
            EditText et_email = (EditText) findViewById(R.id.et_email);
            et_email.setText(this.profile.getEmail());
            
            EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
            et_mobile.setText(this.profile.getTel());
            
            this.rb = (RatingBar) findViewById(R.id.ratingbar_profile);
            this.rb.setRating((float) this.profile.getRating_avg());
            
            if (ratingModus == 1) {
                this.rb.setIsIndicator(false);
                this.rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                    
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        vhikeDialogs
                                .getInstance()
                                .getRateProfileConfirmation(rgvHike, ProfileActivity.this, profileID, (int) rating,
                                        tripID).show();
                    }
                });
            } else {
                this.rb.setIsIndicator(true);
                this.rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        // do nothing
                    }
                });
            }
            
            TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
            tv_rating.setText(Float.toString((float) profile.getRating_avg()));
            
        }
        
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_menu, menu);
        return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.mi_save):
                // send to changes to server
                break;
        }
        return true;
    }
    
}
