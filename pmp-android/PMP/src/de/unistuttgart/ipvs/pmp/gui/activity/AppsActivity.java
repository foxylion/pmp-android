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
import de.unistuttgart.ipvs.pmp.gui.adapter.AppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.placeholder.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppsActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_apps);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        /* Temporary bad stuff, to Test the Activity */
        
        IApp[] apps = ModelProxy.get().getApps();
        
        ListView appsList = (ListView) findViewById(R.id.ListView_Apps);
        appsList.setClickable(true);
        
        AppsAdapter appsAdapter = new AppsAdapter(this, Arrays.asList(apps));
        appsList.setAdapter(appsAdapter);
        
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Toast.makeText(AppsActivity.this, "Tapped on item " + arg2, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AppsActivity.this, AppActivity.class);
                AppsActivity.this.startActivity(i);
            }
        });
    }
}
