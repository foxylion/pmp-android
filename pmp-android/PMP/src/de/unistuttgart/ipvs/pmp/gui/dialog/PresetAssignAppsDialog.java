package de.unistuttgart.ipvs.pmp.gui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetAssignAppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.tab.PresetAppsTab;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Dialog for adding a Preset
 * 
 * @author Marcus Vetter
 */
public class PresetAssignAppsDialog extends Dialog {
    
    /**
     * The button to confirm the dialog
     */
    private Button confirm;
    
    /**
     * The button to cancel the dialog
     */
    private Button cancel;
    
    /**
     * The PresetAppsTabActivity
     */
    private PresetAppsTab activity;
    
    /**
     * The Preset
     */
    private IPreset preset;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public PresetAssignAppsDialog(Context context) {
        super(context);
    }
    
    
    /**
     * Set the activity
     * 
     * @param presetAppsTab
     *            PresetsActivity
     */
    public void setActivity(PresetAppsTab presetAppsTab) {
        this.activity = presetAppsTab;
    }
    
    
    /**
     * Set the preset
     * 
     * @param preset
     *            Preset to set
     */
    public void setPreset(IPreset preset) {
        this.preset = preset;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.dialog_preset_assign_apps);
        
        this.setTitle("Assign Apps");
        
        this.confirm = (Button) findViewById(R.id.presets_dialog_confirm);
        this.cancel = (Button) findViewById(R.id.presets_dialog_cancel);
        
        this.confirm.setOnClickListener(new ConfirmListener());
        this.cancel.setOnClickListener(new CancelListener());
        
        // Apps
        List<IApp> apps = calcDisplayApps();
        
        ListView appsList = (ListView) findViewById(R.id.listview_assigned_apps);
        appsList.setClickable(true);
        
        PresetAssignAppsAdapter appsAdapter = new PresetAssignAppsAdapter(activity, apps);
        appsList.setAdapter(appsAdapter);
        
        appsList.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                
            }
        });
        
    }
    
    
    /**
     * Calc Apps to display = All registered Apps without assigned Apps
     * 
     * @return Apps to display
     */
    private List<IApp> calcDisplayApps() {
        List<IApp> allAppsList = Arrays.asList(ModelProxy.get().getApps());
        List<IApp> allAssignedAppsList = Arrays.asList(preset.getAssignedApps());
        List<IApp> displayList = new ArrayList<IApp>();
        
        Loop: for (IApp app : allAppsList) {
            for (IApp assignedApp : allAssignedAppsList) {
                if (app.equals(assignedApp))
                    continue Loop;
            }
            displayList.add(app);
            
        }
        
        return displayList;
    }
    
    /**
     * Listener class needed for the confirm button
     * 
     */
    private class ConfirmListener implements android.view.View.OnClickListener {
        
        @Override
        public void onClick(View v) {
            
            // Store
            // TODO: Store
            
            // Dismiss
            dismiss();
            
        }
        
    }
    
    /**
     * Listener class needed for the cancel button
     * 
     */
    private class CancelListener implements android.view.View.OnClickListener {
        
        @Override
        public void onClick(View v) {
            // Dismiss
            dismiss();
        }
        
    }
    
}
