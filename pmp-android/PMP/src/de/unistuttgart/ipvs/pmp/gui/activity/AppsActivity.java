package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.AppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppsActivity extends Activity {
    
    private ListView appsList;
    private AppsAdapter appsAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_apps);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        IApp[] apps = ModelProxy.get().getApps();
        
        appsList = (ListView) findViewById(R.id.ListView_Apps);
        appsList.setClickable(true);
        
        appsAdapter = new AppsAdapter(this, Arrays.asList(apps));
        appsList.setAdapter(appsAdapter);
        
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // recover App identifier
                String appIdentifier = ((IApp) appsAdapter.getItem(position)).getIdentifier();
                
                // create intent and start AppActivity
                Intent intent = new Intent(AppsActivity.this, AppActivity.class);
                intent.putExtra(GUIConstants.APP_IDENTIFIER, appIdentifier);
                AppsActivity.this.startActivity(intent);
            }
        });
    }
}
