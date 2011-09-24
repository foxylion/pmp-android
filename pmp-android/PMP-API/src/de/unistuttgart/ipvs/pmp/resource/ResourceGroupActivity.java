package de.unistuttgart.ipvs.pmp.resource;

import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;

/**
 * A basic {@link Activity} for a {@link ResourceGroupApp} in iteration 1.
 * 
 * @author Tobias Kuhn
 * 
 */
public class ResourceGroupActivity extends Activity {
    
    private ResourceGroup[] rgs;
    
    private ProgressDialog progress;
    
    private TextView titles;
    private TextView state;
    private Button installBtn;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        if (!(getApplication() instanceof ResourceGroupApp)) {
            Log.e("ResourceGroupActivity started without ResourceGroupApp.");
            return;
        } else {
            this.rgs = ((ResourceGroupApp) getApplication()).getAllResourceGroups();
        }
        
        this.progress = new ProgressDialog(this);
        
        this.titles = new TextView(this);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.rgs.length; i++) {
            sb.append(this.rgs[i].getName(Locale.getDefault().getLanguage()));
            if (i != this.rgs.length - 1) {
                sb.append(System.getProperty("line.separator"));
            }
        }
        this.titles.setText(sb.toString());
        this.titles.setPadding(16, 16, 16, 16);
        
        this.installBtn = new Button(this);
        this.installBtn.setText("Install");
        this.installBtn.setEnabled(false);
        this.installBtn.setPadding(16, 16, 16, 16);
        this.installBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                install();
            }
        });
        
        this.state = new TextView(this);
        // FIXME: Localization
        this.state.setText("");
        this.state.setPadding(16, 16, 16, 16);
        
        LinearLayout topDown = new LinearLayout(this);
        topDown.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        topDown.setOrientation(LinearLayout.VERTICAL);
        topDown.setHorizontalGravity(Gravity.CENTER);
        
        topDown.addView(this.titles);
        topDown.addView(this.installBtn);
        topDown.addView(this.state);
        
        this.setContentView(topDown);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        this.progress.setMessage("Searching for PMP...");
        this.progress.show();
        
        // start a new thread that checks whether all ResourceGroups are registered.
        Thread t = new Thread(new Runnable() {
            
            @Override
            public void run() {
                boolean allRegistered = true;
                for (ResourceGroup rg : ResourceGroupActivity.this.rgs) {
                    if (!rg.isRegistered(getApplicationContext())) {
                        allRegistered = false;
                        break;
                    }
                }
                foundRegistrationState(allRegistered);
            }
        });
        
        t.start();
    }
    
    
    /**
     * Called when the initializer thread has found PMP and checked whether the RGs are registered or not.
     * 
     * @param registrationState
     *            true, if and only if all resource groups are registered
     */
    private void foundRegistrationState(final boolean registrationState) {
        runOnUiThread(new Runnable() {
            
            @Override
            public void run() {
                ResourceGroupActivity.this.progress.hide();
                ResourceGroupActivity.this.installBtn.setEnabled(!registrationState);
                if (registrationState) {
                    ResourceGroupActivity.this.installBtn.setText("Already installed");
                    ResourceGroupActivity.this.state
                            .setText("All resource groups stored in this app are already registered with PMP.");
                } else {
                    ResourceGroupActivity.this.state
                            .setText("Click \"Install\" to register all the missing resource groups with PMP.");
                }
            }
        });
    }
    
    
    /**
     * Called when the user clicks the "Install" button.
     */
    private void install() {
        this.progress.setMessage("Registering with PMP...");
        this.progress.show();
        
        // start a new thread that registers all resource groups
        Thread t = new Thread(new Runnable() {
            
            @Override
            public void run() {
                for (ResourceGroup rg : ResourceGroupActivity.this.rgs) {
                    rg.register(getApplicationContext());
                }
                runOnUiThread(new Runnable() {
                    
                    @Override
                    public void run() {
                        ResourceGroupActivity.this.installBtn.setEnabled(false);
                        ResourceGroupActivity.this.state
                                .setText("All resource groups have been registered with PMP. You may go back.");
                        ResourceGroupActivity.this.progress.hide();
                    }
                });
            }
        });
        
        t.start();
    }
    
}
