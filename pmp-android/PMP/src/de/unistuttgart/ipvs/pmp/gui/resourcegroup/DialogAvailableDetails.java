package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.util.Locale;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;

/**
 * The {@link DialogAvailableDetails} displays informations about an available Resourcegroup.
 * 
 * @author Jakob Jarosch
 */
public class DialogAvailableDetails extends Dialog {
    
    protected RgInformationSet rgInformation;
    
    private Handler handler;
    
    private TabAvailable tab;
    
    
    /**
     * Creates a new {@link Dialog} for displaying informations about an available Resourcegroup.
     * 
     * @param tab
     *            Tab which is used to display the {@link Dialog}.
     * @param rgInformation
     *            The informations about the Resourcegroup.
     */
    public DialogAvailableDetails(TabAvailable tab, RgInformationSet rgInformation) {
        super(tab);
        
        this.tab = tab;
        
        this.handler = new Handler();
        
        this.rgInformation = rgInformation;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_resourcegroup_available);
        
        BasicTitleView btv = (BasicTitleView) findViewById(R.id.Title);
        String title = rgInformation.getNames().get(Locale.getDefault());
        if (title == null) {
            title = rgInformation.getNames().get(Locale.ENGLISH);
        }
        btv.setTitle(title);
        
        TextView tv = (TextView) findViewById(R.id.TextView_Description);
        String description = rgInformation.getDescriptions().get(Locale.getDefault());
        if (description == null) {
            description = rgInformation.getDescriptions().get(Locale.ENGLISH);
        }
        tv.setText(description);
        
        /* Disable the install button when rg already installed. */
        Button installButton = (Button) findViewById(R.id.Button_Install);
        installButton.setEnabled(ModelProxy.get().getResourceGroup(rgInformation.getIdentifier()) == null);
        
        addListener();
    }
    
    
    /**
     * Adds the listener to the Activity layout.
     */
    private void addListener() {
        ((Button) findViewById(R.id.Button_Install)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(DialogAvailableDetails.this.getContext());
                pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pd.setTitle(DialogAvailableDetails.this.getContext().getString(R.string.rg_processing_installation));
                pd.setCancelable(false);
                pd.setProgress(0);
                pd.setMax(1);
                pd.show();
                
                ServerProvider.getInstance().setCallback(new IServerDownloadCallback() {
                    
                    @Override
                    public void tasks(int position, int length) {
                    }
                    
                    
                    @Override
                    public void download(final int position, final int length) {
                        /* Inform the user */
                        DialogAvailableDetails.this.handler.post(new Runnable() {
                            
                            @Override
                            public void run() {
                                pd.setProgress(position);
                                pd.setMax(length);
                            }
                        });
                    }
                });
                
                new Thread() {
                    
                    @Override
                    public void run() {
                        boolean success = false;
                        String error = null;
                        
                        try {
                            ModelProxy.get().installResourceGroup(
                                    DialogAvailableDetails.this.rgInformation.getIdentifier(), false);
                            success = true;
                        } catch (InvalidXMLException e) {
                            error = e.getMessage();
                        } catch (InvalidPluginException e) {
                            error = e.getMessage();
                        }
                        
                        final String message = (success ? "Installed the Resource successfully."
                                : "Failed to install the Resource:\n" + error);
                        
                        /* Inform the user */
                        DialogAvailableDetails.this.handler.post(new Runnable() {
                            
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(DialogAvailableDetails.this.getContext(), message, Toast.LENGTH_LONG)
                                        .show();
                                
                                tab.updateDownloadList();
                                
                                DialogAvailableDetails.this.dismiss();
                            }
                        });
                    };
                }.start();
                
            }
        });
        
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogAvailableDetails.this.dismiss();
            }
        });
    }
}
