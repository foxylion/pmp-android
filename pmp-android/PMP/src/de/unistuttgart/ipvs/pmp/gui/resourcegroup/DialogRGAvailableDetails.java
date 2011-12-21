package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.io.File;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.model.ModelProxy;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidPluginException;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidXMLException;
import de.unistuttgart.ipvs.pmp.model.server.IServerDownloadCallback;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DialogRGAvailableDetails extends Dialog {
    
    private RgInformationSet rgInformation;
    
    
    public DialogRGAvailableDetails(Context context, RgInformationSet rgInformation) {
        super(context);
        
        this.rgInformation = rgInformation;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_resourcegroup_available);
        
        BasicTitleView btv = (BasicTitleView) findViewById(R.id.Title);
        btv.setTitle(rgInformation.getNames().get(Locale.ENGLISH));
        
        TextView tv = (TextView) findViewById(R.id.TextView_Description);
        tv.setText(rgInformation.getDescriptions().get(Locale.ENGLISH));
        
        addListeners();
    }
    
    
    private void addListeners() {
        ((Button) findViewById(R.id.Button_Install)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(DialogRGAvailableDetails.this.getContext());
                pd.setTitle("Processing installation request...");
                pd.setCancelable(false);
                
                pd.show();
                
                new Thread() {
                    
                    public void run() {
                        boolean success = false;
                        String error = null;
                        
                        try {
                            ModelProxy.get().installResourceGroup(rgInformation.getIdentifier());
                            success = true;
                        } catch (InvalidXMLException e) {
                            error = e.getMessage();
                        } catch (InvalidPluginException e) {
                            error = e.getMessage();
                        }
                        
                        final String message = (success ? "Installed the Resource successfully."
                                : "Failed to install the Resource:\n" + error);
                        
                        /* Inform the user */
                        Looper.prepare();
                        new Handler().post(new Runnable() {
                            
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toast.makeText(DialogRGAvailableDetails.this.getContext(), message, Toast.LENGTH_LONG)
                                        .show();
                                
                                DialogRGAvailableDetails.this.dismiss();
                            }
                        });
                        Looper.loop();
                    };
                }.start();
                
            }
        });
        
        ((Button) findViewById(R.id.Button_Close)).setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                DialogRGAvailableDetails.this.dismiss();
            }
        });
    }
}
