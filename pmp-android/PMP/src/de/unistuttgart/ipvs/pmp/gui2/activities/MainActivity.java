package de.unistuttgart.ipvs.pmp.gui2.activities;

import de.unistuttgart.ipvs.pmp.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

/**
 * The {@link MainActivity} is the startup activity for PMP. It is also available in the App-Drawer.
 * 
 * @author Jakob Jarosch
 */
public class MainActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.pmp_main);
        
        registerListener();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }
    
    
    @Override
    protected void onPause() {
        super.onPause();
    }
    
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    
    
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    
    
    /**
     * Registers all the listeners to the {@link Button}s.
     */
    private void registerListener() {
        Button buttonApps = (Button) findViewById(R.id.Button_Apps);
        Button buttonRgs = (Button) findViewById(R.id.Button_RGs);
        Button buttonPresets = (Button) findViewById(R.id.Button_Presets);
        Button buttonSettings = (Button) findViewById(R.id.Button_Settings);
        
        /* The Apps-Button OnClickListener */
        buttonApps.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Tapped on Apps-Button", Toast.LENGTH_SHORT).show();
            }
        });
        
        /* The RGs-Button OnClickListener */
        buttonRgs.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Tapped on RGs-Button", Toast.LENGTH_SHORT).show();
            }
        });
        
        /* The Presets-Button OnClickListener */
        buttonPresets.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Tapped on Presets-Button", Toast.LENGTH_SHORT).show();
            }
        });
        
        /* The Settings-Button OnClickListener */
        buttonSettings.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Tapped on Settings-Button", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
