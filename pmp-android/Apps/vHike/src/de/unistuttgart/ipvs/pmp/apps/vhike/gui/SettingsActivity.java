package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import de.unistuttgart.ipvs.pmp.R;

/**
 * Will be removed after fully connected vHike to PMP
 * 
 * @author Andre Nguyen
 * 
 */
public class SettingsActivity extends Activity {
    
    private EditText perimeter;
    private CheckBox unanimous;
    
    private String sPerimeter;
    private boolean bUnanimous;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        this.perimeter = (EditText) findViewById(R.id.et_perimeter);
        this.unanimous = (CheckBox) findViewById(R.id.cb_unanimous);
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        SharedPreferences settings = getSharedPreferences("vHikePrefs", MODE_PRIVATE);
        this.sPerimeter = settings.getString("PERIMETER", "10");
        this.bUnanimous = settings.getBoolean("UNANIMOUS", false);
        
        if (this.sPerimeter.equals("")) {
            this.sPerimeter = "10";
        }
        
        this.perimeter.setText(this.sPerimeter);
        this.unanimous.setChecked(this.bUnanimous);
        
    }
    
    
    @Override
    protected void onStop() {
        super.onStop();
        
        SharedPreferences prefs = getSharedPreferences("vHikePrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putString("PERIMETER", this.perimeter.getText().toString());
        prefsEditor.putBoolean("UNANIMOUS", this.unanimous.isChecked());
        prefsEditor.commit();
    }
    
}
