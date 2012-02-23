package de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class ProfileDialog extends Dialog {
    
    private Profile profile;
    private int profileID;
    private Controller ctrl;
    
    
    @SuppressWarnings("static-access")
    public ProfileDialog(Context context, int profileID) {
        super(context);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_profile);
        this.profileID = profileID;
        ctrl = new Controller();
        
        setUpProfile();
    }
    
    
    private void setUpProfile() {
        
        /**
         * PROFILE_ID: the profile id from some vHike-User
         * TRIP_ID: TRIP_ID which is needed to rate a user from a past ride
         * 
         */
        
        profile = ctrl.getProfile(Model.getInstance().getSid(), profileID);
        
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
        
        RatingBar rb = (RatingBar) findViewById(R.id.ratingbar_profile);
        rb.setRating((float) profile.getRating_avg());
        
        TextView tv_rating = (TextView) findViewById(R.id.tv_rating);
        tv_rating.setText(String.valueOf(profile.getRating_avg()));
        
        EditText et_desc = (EditText) findViewById(R.id.et_description_profile);
        et_desc.setText(profile.getDescription());
        // // car = "";
        // et_car.setText(car);
        
    }
    
}
