package de.unistuttgart.ipvs.pmp.gui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class RGDetailsActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgs_details);
        
        String identifier = this.getIntent().getStringExtra("identifier");
        //String identifier = "org.oracle.db";
        
        IResourceGroup resourceGroup = ModelProxy.get().getResourceGroup(identifier);

        ImageView icon = (ImageView) findViewById(R.id.ImageView_Icon);
        icon.setImageDrawable(resourceGroup.getIcon());
        
        TextView name = (TextView) findViewById(R.id.TextView_Name);
        name.setText(resourceGroup.getName());
        
        TextView description = (TextView) findViewById(R.id.TextView_Description);
        description.setText(resourceGroup.getDescription());
        
        TextView status = (TextView) findViewById(R.id.TextView_Status);
        String text;
        if (/* resourceGroup.isInstalled() == */true) {
            status.setTextColor(Color.GREEN);
            String version = String.valueOf(resourceGroup.getVersion());
            text = "Already installed (V." + version + ")";
        } else {
            status.setTextColor(Color.YELLOW);
            text = "Available for install";
        }
        status.setText(text);
      
    }
}
