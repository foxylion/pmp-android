package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.DBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.db.IDBConnector;
import de.unistuttgart.ipvs.pmp.resourcegroups.energy.resource.resultset.ResultSetCurrentValues;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class EnergyActivity extends Activity {
    
    private ToggleButton onOff;
    private Button update;
    
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        this.onOff = (ToggleButton) findViewById(R.id.serviceButton);
        this.onOff.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                if (EnergyActivity.this.onOff.isChecked()) {
                    startService(new Intent(EnergyActivity.this.getApplicationContext(), EnergyService.class));
                } else {
                    stopService(new Intent(EnergyActivity.this.getApplicationContext(), EnergyService.class));
                }
            }
        });
        
        this.update = (Button) findViewById(R.id.updateButton);
        this.update.setOnClickListener(new OnClickListener() {
            
            public void onClick(View v) {
                updateValues();
            }
            
        });
        
        updateValues();
    }
    
    
    private void updateValues() {
        // Get the ResultSet
        IDBConnector db = DBConnector.getInstance(this);
        ResultSetCurrentValues rs = db.getCurrentValues();
        
        // Current values
        TextView tvLevel = (TextView) findViewById(R.id.textCVLevel);
        TextView tvHealth = (TextView) findViewById(R.id.textCVHealth);
        TextView tvStatus = (TextView) findViewById(R.id.textCVStatus);
        TextView tvTemperature = (TextView) findViewById(R.id.textCVTemperature);
        
        tvLevel.setText(rs.getLevel());
        tvHealth.setText(rs.getHealth());
        StringBuilder statusText = new StringBuilder(rs.getStatus());
        if (rs.getPlugged() != "") {
            statusText.append(" / " + rs.getPlugged());
        }
        statusText.append(" (since " + rs.getStatusTime() + ")");
        tvStatus.setText(statusText.toString());
        tvTemperature.setText(rs.getTemperature());
        
    }
}
