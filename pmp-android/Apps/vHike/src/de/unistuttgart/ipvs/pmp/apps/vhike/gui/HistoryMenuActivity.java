package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;

public class HistoryMenuActivity extends Activity {
    
    Button btn_driver;
    Button btn_passenger;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.setContentView(R.layout.activity_history_menu);
        
        this.btn_driver = (Button) findViewById(R.id.Button_Driver_History);
        this.btn_passenger = (Button) findViewById(R.id.Button_Passenger_History);
        
        createTouchListener(getBaseContext());
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        
        Controller ctrl = new Controller(ViewModel.getInstance().getvHikeRG());
        
        if (vHikeService.isServiceFeatureEnabled(Constants.SF_HIDE_CONTACT_INFO)) {
            ctrl.enableAnonymity(Model.getInstance().getSid());
        } else {
            ctrl.disableAnonymity(Model.getInstance().getSid());
        }
        Log.i(this, "");
    }
    
    
    private void createTouchListener(final Context context) {
        this.btn_driver.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "Driver", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(HistoryMenuActivity.this, HistoryActivity.class);
                intent.putExtra("IS_DRIVER", true);
                HistoryMenuActivity.this.startActivity(intent);
            }
        });
        this.btn_passenger.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                Toast.makeText(context, "Passenger", Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(HistoryMenuActivity.this, HistoryActivity.class);
                intent.putExtra("IS_DRIVER", false);
                HistoryMenuActivity.this.startActivity(intent);
            }
        });
    }
    
}
