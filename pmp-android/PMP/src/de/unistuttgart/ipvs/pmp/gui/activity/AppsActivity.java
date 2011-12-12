package de.unistuttgart.ipvs.pmp.gui.activity;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.AppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

public class AppsActivity extends Activity {
    
    private List<IApp> appsList;
    private ListView appsViewList;
    private AppsAdapter appsAdapter;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_apps);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        appsViewList = (ListView) findViewById(R.id.ListView_Apps);
        appsViewList.setClickable(true);
        
        updateAppsList();
        
        appsViewList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // recover App identifier
                IApp app = ((IApp) appsAdapter.getItem(position));
                
                // create intent and start AppActivity
                openApp(app);
            }
        });
        
        appsViewList.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
                menu.setHeaderTitle(getString(R.string.app_context_menu));
                menu.add(0, 0, 0, R.string.details_app);
                menu.add(1, 1, 0, R.string.remove_app);
            }
        });
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem menuItem) {
        // The menu information
        AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) menuItem.getMenuInfo();
        IApp app = this.appsList.get(menuInfo.position);
        
        if (menuItem.getItemId() == 0) {
            // open details
            openApp(app);
        } else if (menuItem.getItemId() == 1) {
            // remove app from model
            ModelProxy.get().unregisterApp(app.getIdentifier());
            
            // update the app list (item just removed)
            updateAppsList();
            
            // inform the user
            Toast.makeText(this, getResources().getString(R.string.app_removed), Toast.LENGTH_LONG).show();
        }
        
        return true;
    }
    
    private void updateAppsList() {
        appsList = Arrays.asList(ModelProxy.get().getApps());
        appsAdapter = new AppsAdapter(this, appsList);
        appsViewList.setAdapter(appsAdapter);
    }
    
    
    private void openApp(IApp app) {
        Intent intent = new Intent(AppsActivity.this, AppActivity.class);
        intent.putExtra(GUIConstants.APP_IDENTIFIER, app.getIdentifier());
        AppsActivity.this.startActivity(intent);
    }
}
