package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;
import de.unistuttgart.ipvs.pmp.util.Restarter;

public class DialogConfirmDelete extends Dialog {
    
    protected IResourceGroup resourceGroup;
    protected Dialog installedDetails;
    protected TabInstalled installedTab;
    
    
    public DialogConfirmDelete(Context context, IResourceGroup resourceGroup, Dialog installedDetails,
            TabInstalled installedTab) {
        super(context);
        
        this.resourceGroup = resourceGroup;
        this.installedDetails = installedDetails;
        this.installedTab = installedTab;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_rgs_confirm_delete);
        
        TextView tvDescription = (TextView) findViewById(R.id.TextView_Description);
        tvDescription.setText(getContext().getString(R.string.rg_confirm_description, resourceGroup.getName()));
        
        addListener();
    }
    
    
    private void addListener() {
        Button confirmButton = (Button) findViewById(R.id.Button_Confirm);
        confirmButton.setOnClickListener(new Button.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                ModelProxy.get().uninstallResourceGroup(DialogConfirmDelete.this.resourceGroup.getIdentifier());
                
                Toast.makeText(DialogConfirmDelete.this.getContext(),
                        DialogConfirmDelete.this.getContext().getString(R.string.rg_removed_success), Toast.LENGTH_LONG)
                        .show();
                
                /* Here we use a bad code style because android has a bug. see ticket #485 in redmine */
                new Thread() {
                    
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                        }
                        Restarter.killAppAndRestartActivity(DialogConfirmDelete.this.installedTab.getParent());
                    };
                }.start();
                
                DialogConfirmDelete.this.dismiss();
                DialogConfirmDelete.this.installedDetails.dismiss();
                DialogConfirmDelete.this.installedTab.refreshList();
            }
        });
        
        Button cancelButton = (Button) findViewById(R.id.Button_Cancel);
        cancelButton.setOnClickListener(new Button.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogConfirmDelete.this.dismiss();
            }
        });
    }
    
}
