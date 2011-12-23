package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

public class TabRGsAvailable extends Activity {
    
    protected ProgressBar updateTaskProgressBar;
    
    private LinearLayout updateProgressContainer;
    private LinearLayout updateFailedContainer;
    private LinearLayout lastUpdateContainer;
    
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
        
        addListeners();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        startDownloadList();
    }
    
    
    protected void startDownloadList() {
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
    
    
    protected void parseDownloadedList(RgInformationSet[] informationSets) {
        this.lastUpdateContainer.setVisibility(View.VISIBLE);
        this.updateProgressContainer.setVisibility(View.GONE);
        
        if (informationSets != null && informationSets.length > 0) {
            this.lastUpdateTextView.setText(getResources().getString(R.string.rg_last_update) + ": "
                    + new Date().toString());
            
            this.rgisList = Arrays.asList(informationSets);
            
            this.rgisViewList.setAdapter(new AdapterRGsAvailable(this, this.rgisList));
            
        } else {
            this.updateFailedContainer.setVisibility(View.VISIBLE);
        }
    }
    
    
    private void addListeners() {
        ((Button) findViewById(R.id.Button_Refresh)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startDownloadList();
            }
        });
        
        /* Add the listener for the Items in the list */
        this.rgisViewList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
                new DialogRGAvailableDetails(TabRGsAvailable.this, TabRGsAvailable.this.rgisList.get(item)).show();
            }
        });
    }
}
