package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
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
    private Button anonymous_btn;
    private Button observation;
    
    private EditText et_lastname;
    private EditText et_firstname;
    private EditText et_tel;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        this.handler = new Handler();
        this.anonymous_btn = (Button) findViewById(R.id.btn_anonymous);
        this.observation = (Button) findViewById(R.id.btn_observation);
        this.et_lastname = (EditText) findViewById(R.id.et_lastname);
        this.et_firstname = (EditText) findViewById(R.id.et_firstname);
        this.et_tel = (EditText) findViewById(R.id.et_mobile);
        if (getvHikeRG(this) != null) {
            setUpProfile();
        }
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        
        vHikeService.getInstance().updateServiceFeatures();
        
        this.ctrl = new Controller(rgvHike);
        
        if (vHikeService.isServiceFeatureEnabled(Constants.SF_HIDE_CONTACT_INFO)) {
            this.ctrl.enableAnonymity(Model.getInstance().getSid());
            boolean anonymous = this.ctrl.isProfileAnonymous(Model.getInstance().getSid(), Model
                    .getInstance()
                    .getOwnProfile().getID());
            if (anonymous) {
                Log.i(this, "Anonymous: " + anonymous);
                this.anonymous_btn.setBackgroundResource(R.drawable.btn_anonymous);
            }
        } else {
            this.ctrl.disableAnonymity(Model.getInstance().getSid());
            this.anonymous_btn.setBackgroundResource(R.drawable.btn_anonymous_disabled);
        }
        Log.i(this, "");
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    ProfileActivity.this.ctrl = new Controller(rgvHike);
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
                this.ctrl = new Controller(rgvHike);
                boolean anonymous = this.ctrl.isProfileAnonymous(Model.getInstance().getSid(), Model
                        .getInstance()
                        .getOwnProfile().getID());
                if (anonymous) {
                    Log.i(this, "Anonymous: " + anonymous);
                    this.anonymous_btn.setBackgroundResource(R.drawable.btn_anonymous);
                }
                
                boolean observationEnabled = this.ctrl.isObservationEnabled(Model.getInstance()
                        .getOwnProfile()
                        .getID());
                Log.i(this, "ObservationEnabled: " + observationEnabled);
                if (observationEnabled) {
                    this.observation.setBackgroundResource(R.drawable.btn_observation);
                } else {
                    this.observation.setBackgroundResource(R.drawable.btn_observation_disabled);
                }
                this.anonymous_btn.setVisibility(View.VISIBLE);
                this.anonymous_btn.setClickable(false);
                this.observation.setVisibility(View.VISIBLE);
            } else {
                this.ctrl = new Controller(rgvHike);
                this.profile = this.ctrl.getProfile(Model.getInstance().getSid(), profileID);
                this.et_lastname.setClickable(false);
                this.et_lastname.setFocusable(false);
                this.et_lastname.setCursorVisible(false);
                this.et_firstname.setClickable(false);
                this.et_firstname.setFocusable(false);
                this.et_firstname.setCursorVisible(false);
                this.et_tel.setClickable(false);
                this.et_tel.setFocusable(false);
                this.et_tel.setCursorVisible(false);
                this.anonymous_btn.setVisibility(View.GONE);
                this.observation.setVisibility(View.GONE);
            }
            
            this.observation.setOnClickListener(new View.OnClickListener() {
                
                @Override
                public void onClick(View v) {
                    boolean observationEnabled = ProfileActivity.this.ctrl.isObservationEnabled(Model
                            .getInstance()
                            .getOwnProfile().getID());
                    Log.i(this, "ObservationEnabled: " + observationEnabled);
                    if (observationEnabled) {
                        ProfileActivity.this.ctrl.disableObservation(Model.getInstance().getSid(), Model
                                .getInstance()
                                .getOwnProfile()
                                .getID());
                        ProfileActivity.this.observation
                                .setBackgroundResource(R.drawable.btn_observation_disabled);
                    } else {
                        ProfileActivity.this.observation.setBackgroundResource(R.drawable.btn_observation);
                        int obsNr = ProfileActivity.this.ctrl.enableObservation(Model.getInstance().getSid(),
                                Model.getInstance()
                                        .getOwnProfile().getID());
                        final AlertDialog alertDialog = new AlertDialog.Builder(ProfileActivity.this)
                                .create();
                        alertDialog.setTitle("Enabled Observation");
                        alertDialog.setMessage("Observation No.: " + obsNr);
                        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                            
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                alertDialog.cancel();
                            }
                        });
                        alertDialog.show();
                    }
                    
                }
            });
            
            TextView tv_username = (TextView) findViewById(R.id.tv_username);
            tv_username.setText(this.profile.getUsername());
            
            this.et_firstname.setText(this.profile.getFirstname());
            
            this.et_lastname.setText(this.profile.getLastname());
            
            EditText et_email = (EditText) findViewById(R.id.et_email);
            et_email.setText(this.profile.getEmail());
            
            this.et_tel.setText(this.profile.getTel());
            
            this.rb = (RatingBar) findViewById(R.id.ratingbar_profile);
            this.rb.setRating((float) this.profile.getRating_avg());
            
            if (ratingModus == 1) {
                this.rb.setIsIndicator(false);
                this.rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                    
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        vhikeDialogs
                                .getInstance()
                                .getRateProfileConfirmation(rgvHike, ProfileActivity.this, profileID,
                                        (int) rating,
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
            tv_rating.setText(Float.toString((float) this.profile.getRating_avg()));
            
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
            case (R.id.mi_save): {
                // send to changes to server
                String lastname = this.et_lastname.getText().toString();
                String firstname = this.et_firstname.getText().toString();
                String tel = this.et_tel.getText().toString();
                Log.i(this, "Clicked Save Changes.");
                this.ctrl.editProfile(Model.getInstance().getSid(), lastname, firstname, tel);
                break;
            }
            
        }
        return true;
    }
    
}
