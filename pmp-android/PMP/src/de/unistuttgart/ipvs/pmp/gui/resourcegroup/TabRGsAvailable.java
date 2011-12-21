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
    
    private ProgressBar updateTaskProgressBar;
    
    private LinearLayout updateProgressContainer;
    private LinearLayout updateFailedContainer;
    private LinearLayout lastUpdateContainer;
    
    private TextView lastUpdateTextView;
    
    /**
     * List of all registered Apps.
     */
    private List<RgInformationSet> rgisList;
    
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
        
        updateTaskProgressBar = (ProgressBar) findViewById(R.id.ProgressBar_TaskState);
        lastUpdateTextView = (TextView) findViewById(R.id.TextView_LastUpdate);
        updateFailedContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingFailed);
        updateProgressContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingList);
        lastUpdateContainer = (LinearLayout) findViewById(R.id.LinearLayout_Refresh);
        rgisViewList = (ListView) findViewById(R.id.ListView_RGs);
        
        rgisViewList.setClickable(true);
        
        addListeners();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        startDownloadList();
    }
    
    
    private void startDownloadList() {
        lastUpdateContainer.setVisibility(View.GONE);
        updateFailedContainer.setVisibility(View.GONE);
        updateProgressContainer.setVisibility(View.VISIBLE);
        rgisViewList.setAdapter(null);
        
        new Thread() {
            
            public void run() {
                ServerProvider.getInstance().setCallback(new IServerDownloadCallback() {
                    
                    @Override
                    public void tasks(int position, int length) {
                        updateTaskProgressBar.setMax(length);
                        updateTaskProgressBar.setProgress(position);
                    }
                    
                    
                    @Override
                    public void download(int position, int length) {
                    }
                });
                final RgInformationSet[] informationSets = ServerProvider.getInstance().findResourceGroups("");
                
                /* Parse the downloaded list */
                runOnUiThread(new Runnable() {
                    
                    public void run() {
                        parseDownloadedList(informationSets);
                    }
                });
            }
            
        }.start();
        
    }
    
    
    private void parseDownloadedList(RgInformationSet[] informationSets) {
        lastUpdateContainer.setVisibility(View.VISIBLE);
        this.updateProgressContainer.setVisibility(View.GONE);
        
        if (informationSets != null && informationSets.length > 0) {
            lastUpdateTextView
                    .setText(getResources().getString(R.string.rg_last_update) + ": " + new Date().toString());
            
            this.rgisList = Arrays.asList(informationSets);
            
            this.rgisViewList.setAdapter(new AdapterRGsAvailable(this, rgisList));
            
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
        rgisViewList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int item, long arg3) {
                new DialogRGAvailableDetails(TabRGsAvailable.this, rgisList.get(item)).show();
            }
        });
    }
}
