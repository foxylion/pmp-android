package de.unistuttgart.ipvs.pmp.gui.preset;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUIConstants;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.gui.util.RGInstaller;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.missing.MissingApp;
import de.unistuttgart.ipvs.pmp.model.element.preset.IPreset;

public class TabDetails extends Activity {
    
    protected IPreset preset;
    
    protected Handler handler;
    
    private DialogPresetEditCallback callback = new DialogPresetEditCallback() {
        
        @Override
        public void refresh() {
            TabDetails.this.refresh();
        }
        
        
        @Override
        public void openPreset(IPreset preset) {
            // Do nothing
        }
    };
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Get the preset
        String presetIdentifier = super.getIntent().getStringExtra(GUIConstants.PRESET_IDENTIFIER);
        this.preset = ModelProxy.get().getPreset(null, presetIdentifier);
        
        this.handler = new Handler();
        
        // Set view
        setContentView(R.layout.tab_preset_details);
        
        addListener();
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        refresh();
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.preset_tab_details_change_description:
                new DialogPresetEdit(this, this.callback, this.preset).show();
                break;
            
            case R.id.preset_tab_details_remove:
                this.preset.setDeleted(true);
                finish();
                break;
        }
        
        return true;
    }
    
    
    /**
     * Create the menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.preset_menu_details_tab, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    
    protected void refresh() {
        if (getParent() != null && getParent() instanceof ActivityPreset) {
            ((ActivityPreset) getParent()).refresh();
        }
        
        ((TextView) findViewById(R.id.TextView_Description)).setText(this.preset.getDescription());
        ((TextView) findViewById(R.id.TextView_Statistics)).setText(Html.fromHtml("<html><b>Assigned Apps:</b> "
                + this.preset.getAssignedApps().length + "<br/>" + "<b>Assigned Privacy Settings:</b> "
                + this.preset.getGrantedPrivacySettings().length + "<br/><br/>" + "<b>Used Contexts:</b> nyi<br/>"
                + "<b>Active Contexts:</b> nyi<br/><br/>" + "<b>Missing Apps:</b> "
                + this.preset.getMissingApps().length + "<br/>" + "<b>Missing Resource Groups:</b> "
                + RGInstaller.getMissingResourceGroups(this.preset).length + "</html>"));
        
        // TODO Implement the context count.
        
        LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.LinearLayout_Problems);
        Button oneClickInstall = (Button) findViewById(R.id.Button_OneClickInstall);
        Button viewMissingRGs = (Button) findViewById(R.id.Button_ViewMissingRGs);
        Button removeMissingApps = (Button) findViewById(R.id.Button_RemoveMissingApps);
        if (this.preset.getMissingApps().length > 0 || RGInstaller.getMissingResourceGroups(this.preset).length > 0) {
            
            buttonContainer.setVisibility(View.VISIBLE);
            
            if (this.preset.getMissingApps().length > 0) {
                removeMissingApps.setEnabled(true);
            } else {
                removeMissingApps.setEnabled(false);
            }
            
            if (RGInstaller.getMissingResourceGroups(this.preset).length > 0) {
                oneClickInstall.setEnabled(true);
                viewMissingRGs.setEnabled(true);
            } else {
                oneClickInstall.setEnabled(false);
                viewMissingRGs.setEnabled(false);
            }
        } else {
            buttonContainer.setVisibility(View.GONE);
        }
    }
    
    
    private void addListener() {
        Button oneClickInstall = (Button) findViewById(R.id.Button_OneClickInstall);
        oneClickInstall.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String[] missingRGs = RGInstaller.getMissingResourceGroups(TabDetails.this.preset);
                RGInstaller.installResourceGroups(TabDetails.this, TabDetails.this.handler, missingRGs);
            }
        });
        
        Button viewMissingRGs = (Button) findViewById(R.id.Button_ViewMissingRGs);
        viewMissingRGs.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                String[] missingRGs = RGInstaller.getMissingResourceGroups(TabDetails.this.preset);
                Intent intent = GUITools.createFilterAvailableRGsIntent(missingRGs);
                startActivity(intent);
            }
        });
        
        Button removeMissingApps = (Button) findViewById(R.id.Button_RemoveMissingApps);
        removeMissingApps.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                for (MissingApp app : TabDetails.this.preset.getMissingApps()) {
                    // TODO Implement the remove of missing apps from the preset.
                    // preset.removeApp((IApp) app);
                }
            }
        });
        
    }
}
