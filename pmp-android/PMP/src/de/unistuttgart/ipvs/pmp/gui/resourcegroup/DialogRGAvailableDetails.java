package de.unistuttgart.ipvs.pmp.gui.resourcegroup;

import java.io.File;
import java.util.Locale;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;
import de.unistuttgart.ipvs.pmp.model.server.ServerProvider;
import de.unistuttgart.ipvs.pmp.util.xml.rg.RgInformationSet;
import android.app.Dialog;
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
                
                new Thread() {
                    
                    public void run() {
                        /* in background */
                        final File rg = ServerProvider.getInstance().downloadResourceGroup(rgInformation.getIdentifier());
                        
                        Looper.prepare();

                        Toast.makeText(DialogRGAvailableDetails.this.getContext(),
                                "Downloaded the rg to " + rg, Toast.LENGTH_LONG);
                        new Handler().post(new Runnable() {
                            
                            @Override
                            public void run() {
                                
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
