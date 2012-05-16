package de.unistuttgart.ipvs.pmp.apps.vhike.bluetooth.gui;

import java.util.Timer;

import android.os.Bundle;
import android.os.Handler;
import android.os.IInterface;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;

/**
 * LoginActivity: the startup activity for vHike and starts the registration on PMP to load
 * resource groups
 * 
 * @author Andre Nguyen, Dang Huynh
 * 
 */
public class BluetoothActivity extends ResourceGroupReadyActivity {
    
    private String username = "";
    private String pw = "";
    
    private EditText etUsername;
    private EditText etPW;
    private CheckBox cbAutologin;
    private ProgressBar pbLogin;
    
    private Controller ctrl;
    private Timer loginTimer;
    
    Handler handler;
    protected boolean isCanceled;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_bluetooth);
        
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) throws SecurityException {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        Log.i(this, "RG ready: " + resourceGroup);
        if (rgvHike != null) {
            this.handler.post(new Runnable() {
                
                @Override
                public void run() {
                    //do sth
                }
            });
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
    }
    
}
