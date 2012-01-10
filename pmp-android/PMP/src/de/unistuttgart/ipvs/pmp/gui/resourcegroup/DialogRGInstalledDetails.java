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
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.element.resourcegroup.IResourceGroup;

/**
 * The {@link DialogRGAvailableDetails} displays informations about an at PMP registered Resourcegroup.
 * 
 * @author Jakob Jarosch
 */
public class DialogRGInstalledDetails extends Dialog {
    
    protected IResourceGroup resourcegroup;
    protected TabRGsInstalled parent;
    
    
    /**
     * Creates a new {@link Dialog} for displaying informations about an installed Resourcegroup.
     * 
     * @param context
     *            Context which is used to display the {@link Dialog}.
     * @param parent
     *            The Tab which contains the list installed Resourcegroups. If value is NULL no list will be updated on
     *            uninstall.
     * @param resourcegroup
     *            The informations about the Resourcegroup.
     */
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
        
        addListener();
    }
    
    
    /**
     * Adds the listener to the Activity layout.
     */
    private void addListener() {
        ((Button) findViewById(R.id.Button_Uninstall)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                new DialogConfirmDelete(DialogRGInstalledDetails.this.getContext(), resourcegroup, DialogRGInstalledDetails.this, parent).show();
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
