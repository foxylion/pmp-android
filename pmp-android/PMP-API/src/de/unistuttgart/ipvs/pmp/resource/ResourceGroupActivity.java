package de.unistuttgart.ipvs.pmp.resource;

import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * A basic {@link Activity} for a {@link ResourceGroupApp} in iteration 1.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroupActivity extends Activity {
    
    private AtomicBoolean initialized;
    private ProgressBar pb;
    private TextView tv;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.initialized = new AtomicBoolean(false);
        
        this.pb = new ProgressBar(this);
        this.tv = new TextView(this);
        // FIXME: Localization
        this.tv.setText("Please wait while the resource group registers with PMP.");
        this.tv.setPadding(16, 0, 0, 0);
        
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        ll.setOrientation(LinearLayout.HORIZONTAL);
        ll.setVerticalGravity(Gravity.CENTER);
        
        ll.addView(this.pb);
        ll.addView(this.tv);
        
        this.setContentView(ll);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (this.initialized.get()) {
            finish();
        }
        
        // resolve app to be ResourceGroupApp
        Application app = getApplication();
        if (!(app instanceof ResourceGroupApp)) {
            Log.e("ResourceGroupActivity started without ResourceGroupApp.");
        } else {
            final ResourceGroupApp rga = (ResourceGroupApp) app;
            
            // start a new thread that registers all the resource groups
            Thread t = new Thread(new Runnable() {
                
                @Override
                public void run() {
                    for (ResourceGroup rg : rga.getAllResourceGroups()) {
                        rg.start(getApplicationContext());
                    }
                    ResourceGroupActivity.this.initialized.set(true);
                    ResourceGroupActivity.this.finish();
                }
                
            });
            
            t.start();
        }
    }
}
