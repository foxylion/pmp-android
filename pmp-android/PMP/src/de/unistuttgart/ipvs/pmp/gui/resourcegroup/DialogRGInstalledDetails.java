package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

public class DialogRGInstalledDetails extends Dialog {
    
    protected IResourceGroup resourcegroup;
    protected TabRGsInstalled parent;
    
    
    public DialogRGInstalledDetails(Context context, TabRGsInstalled parent, IResourceGroup resourcegroup) {
        super(context);
        
        this.resourcegroup = resourcegroup;
        this.parent = parent;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_resourcegroup_installed);
        
        BasicTitleView btv = (BasicTitleView) findViewById(R.id.Title);
        btv.setIcon(resourcegroup.getIcon());
        btv.setTitle(resourcegroup.getName());
        
        TextView tv = (TextView) findViewById(R.id.TextView_Description);
        tv.setText(resourcegroup.getDescription());
        
        addListeners();
    }
    
    
    private void addListeners() {
        ((Button) findViewById(R.id.Button_Uninstall)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ModelProxy.get().uninstallResourceGroup(DialogRGInstalledDetails.this.resourcegroup.getIdentifier());
                DialogRGInstalledDetails.this.dismiss();
                
                if (DialogRGInstalledDetails.this.parent != null) {
                    DialogRGInstalledDetails.this.parent.refreshList();
                }
            }
        });
        
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogRGInstalledDetails.this.dismiss();
            }
        });
    }
    
}
