package de.unistuttgart.ipvs.pmp.gui.tab;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.RGsAvailableAdapter;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

public class RGsAvailableTab extends Activity {
    
    private ProgressBar updateTaskProgressBar;
    private ProgressBar updateDownloadProgressBar;
    
    private LinearLayout updateProgressContainer;
    private LinearLayout updateFailedContainer;
    
    /**
     * List of all registered Apps.
     */
    private List<RgInformationSet> rgisList;
    
    /**
     * {@link ListView} is the view reference for the Resource Groups list.
     */
    private ListView rgisViewList;
    
    /**
     * {@link RGsAvailableAdapter} for displaying the rgisList.
     */
    protected RGsAvailableAdapter rgisAdapter;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.tab_rgs_available);
        
        updateDownloadProgressBar = (ProgressBar) findViewById(R.id.ProgressBar_DownloadState);
        updateTaskProgressBar = (ProgressBar) findViewById(R.id.ProgressBar_TaskState);
        updateFailedContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingFailed);
        updateProgressContainer = (LinearLayout) findViewById(R.id.LinearLayout_UpdatingList);
        
        addListeners();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        startDownloadList();
    }
    
    
    private void startDownloadList() {
        updateFailedContainer.setVisibility(View.GONE);
        updateProgressContainer.setVisibility(View.VISIBLE);
        
        new Thread() {
            
            public void run() {
                ServerProvider.getInstance().setCallback(new IServerDownloadCallback() {
                    
                    @Override
                    public void tasks(int position, int length) {
                        Looper.prepare();
                        updateTaskProgressBar.setMax(length);
                        updateTaskProgressBar.setProgress(position);
                        Looper.loop();
                    }
                    
                    
                    @Override
                    public void download(int position, int length) {
                        Looper.prepare();
                        updateDownloadProgressBar.setMax(length);
                        updateDownloadProgressBar.setProgress(position);
                        Looper.loop();
                    }
                });
                RgInformationSet[] informationSets = ServerProvider.getInstance().findResourceGroups("");
                
                parseDownloadedList(informationSets);
            }
            
        }.start();
        
    }
    
    
    private void parseDownloadedList(RgInformationSet[] informationSets) {
        Looper.prepare();
        
        rgisList = Arrays.asList(informationSets);
        
        if (informationSets != null && informationSets.length > 0) {
            this.updateProgressContainer.setVisibility(View.GONE);
            
            this.rgisViewList = (ListView) findViewById(R.id.ListView_RGs);
            this.rgisViewList.setClickable(true);
            
            this.rgisViewList.setAdapter(new RGsAvailableAdapter(this, rgisList));
        } else {
            this.updateProgressContainer.setVisibility(View.GONE);
            this.updateFailedContainer.setVisibility(View.VISIBLE);
        }
        
        Looper.loop();
    }
    
    
    private void addListeners() {
        ((Button) findViewById(R.id.Button_RefreshList)).setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                startDownloadList();
            }
        });
    }
}
