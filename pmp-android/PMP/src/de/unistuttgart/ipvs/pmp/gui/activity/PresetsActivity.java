package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetsAdapter;
import de.unistuttgart.ipvs.pmp.gui.mockup.MockupModel;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * 
 * @author Marcus Vetter
 * 
 */
public class PresetsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_presets);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        /* Temporary bad stuff, to Test the Activity */
        
        IPreset[] presets = MockupModel.instance.getPresets();
        
        ListView presetsList = (ListView) findViewById(R.id.ListView_Presets);
        presetsList.setClickable(true);
        
        PresetsAdapter presetsAdapter = new PresetsAdapter(this, Arrays.asList(presets));
        presetsList.setAdapter(presetsAdapter);
        
        presetsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(PresetsActivity.this, "Tapped on item " + arg2, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(PresetsActivity.this, PresetActivity.class);
                PresetsActivity.this.startActivity(i);
            }
        });
    }
}
