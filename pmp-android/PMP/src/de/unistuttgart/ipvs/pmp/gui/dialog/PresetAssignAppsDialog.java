package de.unistuttgart.ipvs.pmp.gui.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.adapter.PresetAssignAppsAdapter;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.tab.PresetAppsTab;
import de.unistuttgart.ipvs.pmp.model.element.app.IApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

/**
 * Dialog for assigning Apps to the Preset
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
     * The PresetAppsTab
     */
    private PresetAppsTab activity;
    
    /**
     * The Preset
     */
    protected IPreset preset;
    
    /**
     * The instance of the adapter
     */
    protected PresetAssignAppsAdapter appsAdapter;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
<<<<<<< HEAD
     * @param preset
     *            the Preset
=======
     * @param preset2
>>>>>>> fb2f691cca6dd2f18b1df88e99d43976a6c2b38d
     */
    public PresetAssignAppsDialog(Context context, IPreset preset) {
        super(context);
        this.preset = preset;
        this.activity = (PresetAppsTab) context;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_preset_assign_apps);
        
        this.confirm = (Button) findViewById(R.id.presets_dialog_confirm);
        this.cancel = (Button) findViewById(R.id.presets_dialog_cancel);
        
        this.confirm.setOnClickListener(new ConfirmListener(this.activity));
        this.cancel.setOnClickListener(new CancelListener());
        
        // Apps
        List<IApp> apps = calcDisplayApps();
        
        ListView appsList = (ListView) findViewById(R.id.listview_assigned_apps);
        appsList.setClickable(true);
        
        this.appsAdapter = new PresetAssignAppsAdapter(this.activity, apps);
        appsList.setAdapter(this.appsAdapter);
        
    }
    
    
    /**
     * Calc Apps to display = All registered Apps without assigned Apps
     * 
     * @return Apps to display
     */
    public List<IApp> calcDisplayApps() {
        List<IApp> allAppsList = Arrays.asList(ModelProxy.get().getApps());
        List<IApp> allAssignedAppsList = Arrays.asList(this.preset.getAssignedApps());
        List<IApp> displayList = new ArrayList<IApp>();
        
        Loop: for (IApp app : allAppsList) {
            for (IApp assignedApp : allAssignedAppsList) {
                if (app.equals(assignedApp)) {
                    continue Loop;
                }
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
        
        /**
         * The PresetAppsTabActivity
         */
        private PresetAppsTab activity;
        
        
        public ConfirmListener(PresetAppsTab activity) {
            this.activity = activity;
        }
        
        
        @Override
        public void onClick(View v) {
            
            // Store
            for (IApp app : PresetAssignAppsDialog.this.appsAdapter.getCheckBoxMap().keySet()) {
                if (PresetAssignAppsDialog.this.appsAdapter.getCheckBoxMap().get(app)) {
                    PresetAssignAppsDialog.this.preset.assignApp(app);
                }
            }
            this.activity.updateList();
            
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
