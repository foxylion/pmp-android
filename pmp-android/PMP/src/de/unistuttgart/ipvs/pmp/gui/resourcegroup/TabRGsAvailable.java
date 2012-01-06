package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * The {@link TabRGsAvailable} contains all available Resourcegroups.
 * 
 * @author Jakob Jarosch
 */
public class TabRGsAvailable extends Activity {
    
    /**
     * ProgressBar displays the progress of updating the list of available Resourcegroups.
     */
    protected ProgressBar updateTaskProgressBar;
    
    /*
     * Linear Layouts to show or hide header informations.
     */
    private LinearLayout updateProgressContainer;
    private LinearLayout updateFailedContainer;
    private LinearLayout lastUpdateContainer;
    
    /**
     * The lastUpdateTextView displays the date when the list was last updated.
     */
    private TextView lastUpdateTextView;
    
    /**
     * List of all registered Apps.
     */
    protected List<RgInformationSet> rgisList;
    
    /**
     * {@link ListView} is the view reference for the Resource Groups list.
     */
    private ListView rgisViewList;
    
    /**
     * {@link AdapterRGsAvailable} for displaying the rgisList.
     */
    protected AdapterRGsAvailable rgisAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_rgs_available);
        
        this.updateTaskProgressBar = (ProgressBar) findViewById(R.id.ProgressBar_TaskState);
        this.lastUpdateTextView = (TextView) findViewById(R.id.TextView_LastUpdate);
        this.updateFailedContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingFailed);
        this.updateProgressContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingList);
        this.lastUpdateContainer = (LinearLayout) findViewById(R.id.LinearLayout_Refresh);
        this.rgisViewList = (ListView) findViewById(R.id.ListView_RGs);
        
        this.rgisViewList.setClickable(true);
        
        addListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        startDownloadList();
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.rg_menu, menu);
        return true;
    }
    
    
    /**
     * Initiates a new update of the available Resourcegroups list. (done asynchronously)
     */
    private void startDownloadList() {
        this.lastUpdateContainer.setVisibility(View.GONE);
        this.updateFailedContainer.setVisibility(View.GONE);
        this.updateProgressContainer.setVisibility(View.VISIBLE);
        this.rgisViewList.setAdapter(null);
        
        new Thread() {
            
            @Override
            public void run() {
                ServerProvider.getInstance().setCallback(new IServerDownloadCallback() {
                    
                    @Override
                    public void tasks(int position, int length) {
                        TabRGsAvailable.this.updateTaskProgressBar.setMax(length);
                        TabRGsAvailable.this.updateTaskProgressBar.setProgress(position);
                    }
                    
                    
                    @Override
                    public void download(int position, int length) {
                    }
                });
                final RgInformationSet[] informationSets = ServerProvider.getInstance().findResourceGroups("");
                
                /* Parse the downloaded list */
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        parseDownloadedList(informationSets);
                    }
                });
            }
            
        }.start();
        
    }
    
    
    /**
     * Parses the downloadedList of informationsets.
     * 
     * @param informationSets
     *            Downloaded informationsets.
     */
    protected void parseDownloadedList(RgInformationSet[] informationSets) {
        this.lastUpdateContainer.setVisibility(View.VISIBLE);
        this.updateProgressContainer.setVisibility(View.GONE);
        
        if (informationSets != null && informationSets.length > 0) {
            this.lastUpdateTextView.setText(getResources().getString(R.string.last_update_at) + ": "
                    + new Date().toString());
            
            this.rgisList = Arrays.asList(informationSets);
            
            this.rgisViewList.setAdapter(new AdapterRGsAvailable(this, this.rgisList));
            
        } else {
            this.updateFailedContainer.setVisibility(View.VISIBLE);
        }
    }
    
    
    /**
     * Adds the listener to the Activity layout.
     */
    private void addListener() {
        this.rgisViewList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
                new DialogRGAvailableDetails(TabRGsAvailable.this, TabRGsAvailable.this.rgisList.get(item)).show();
            }
        });
    }
    
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_rg_refresh:
                startDownloadList();
                break;
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
