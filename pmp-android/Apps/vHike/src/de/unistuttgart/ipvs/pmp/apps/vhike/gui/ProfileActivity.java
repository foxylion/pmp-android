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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

/**
 * Displays the profile of the current user, some other vHike-User to simply display users personal information or to
 * rate a user after a ride through the history activity
 * 
 * @author Andre Nguyen
 * 
 */
public class ProfileActivity extends Activity {
    
    private Profile profile;
    private RatingBar rb;
    
    static final String[] RECENT_RIDES = new String[] { "01.01.2011, Stuttgart", "02.01.2011, Berlin",
            "03.01.2011, Vaihingen", "..." };
    
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        setUpProfile();
    }
    
    
    private void setUpProfile() {
        
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
            profile = Model.getInstance().getOwnProfile();
        } else {
            Controller ctrl = new Controller();
            profile = ctrl.getProfile(Model.getInstance().getSid(), profileID);
        }
        
        TextView tv_username = (TextView) findViewById(R.id.tv_username);
        tv_username.setText(profile.getUsername());
        
        EditText et_firstname = (EditText) findViewById(R.id.et_firstname);
        et_firstname.setText(profile.getFirstname());
        
        EditText et_lastname = (EditText) findViewById(R.id.et_lastname);
        et_lastname.setText(profile.getLastname());
        
        EditText et_email = (EditText) findViewById(R.id.et_email);
        et_email.setText(profile.getEmail());
        
        EditText et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_mobile.setText(profile.getTel());
        
        rb = (RatingBar) findViewById(R.id.ratingbar_profile);
        rb.setRating((float) profile.getRating_avg());
        
        if (ratingModus == 1) {
            rb.setIsIndicator(false);
            rb.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    vhikeDialogs.getInstance()
                            .getRateProfileConfirmation(ProfileActivity.this, profileID, (int) rating, tripID).show();
                }
            });
        } else {
            rb.setIsIndicator(true);
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    // do nothing
                }
            });
        }
        
        TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating.setText(String.valueOf(profile.getRating_avg()));
        
        EditText et_desc = (EditText) findViewById(R.id.et_description_profile);
        et_desc.setText(profile.getDescription());
        // // car = "";
        // et_car.setText(car);
        
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
