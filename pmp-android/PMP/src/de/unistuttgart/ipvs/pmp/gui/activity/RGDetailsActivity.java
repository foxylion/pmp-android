package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;

public class RGDetailsActivity extends Activity {
    
    private Bundle extras;
    public ProgressDialog pd;
    
    private OnClickListener installListener = new OnClickListener() {
        
        @Override
        public void onClick(View v) {
            //            InstallPluginTask task = new InstallPluginTask(RGDetailsActivity.this);
            //            task.execute(extras.getString("identifier"));
        }
    };
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.extras = getIntent().getExtras();
        int isStub = this.extras.getInt("is_stub");
        
        if (isStub == 0) {
            createFullView(this.extras.getString("identifier"));
            Log.e("Full View");
        } else if (isStub == 1) {
            createStubView(this.extras.getInt("position"));
            Log.e("Stub View");
        }
    }
    
    
    private void createStubView(int position) {
        setContentView(R.layout.activity_rgs_details_stub);
        //        AvailablePlugins.Plugin plugin = PluginManager.getInstance().getAvailablePlugins().getPlugins().get(position);
        
        findViewById(R.id.Name);
        
        findViewById(R.id.Description);
        
        Button button = (Button) findViewById(R.id.Button);
        button.setOnClickListener(this.installListener);
    }
    
    
    private void createFullView(String string) {
        setContentView(R.layout.activity_rgs_details);
        // lol, that was already commented out
        /*
        TextView name = (TextView) findViewById(R.id.TextView_Name);
        name.setText(resourceGroup.getName());
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(resourceGroup.getDescription());
        
        TextView status = (TextView) findViewById(R.id.TextView_Status);
        String text;
        if (plugin.getInstalledRevision() == 0) {
            status.setTextColor(Color.GRAY);
            text = context.getString(R.string.rgs_notinstalled);
        } else {
            if (plugin.getInstalledRevision() == plugin.getLatestRevision()) {
                status.setTextColor(Color.GREEN);
                text = context.getString(R.string.rgs_uptodate);
                text += " (r" + String.valueOf(plugin.getInstalledRevision()) + ")";
            } else {
                status.setTextColor(Color.YELLOW);
                text = context.getString(R.string.rgs_outdated);
                text += " (r" + String.valueOf(plugin.getInstalledRevision());
                text += " to r" + String.valueOf(plugin.getLatestRevision()) + ")";
            }
        }
        status.setText(text);
        */
    }
}
