package de.unistuttgart.ipvs.pmp.gui.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

public class LongRunningTaskDialog extends Dialog {
    
    private Runnable runnable;
    
    
    public LongRunningTaskDialog(Context context, Runnable runnable) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_long_running_task);
        
        setCancelable(false);
        
        this.runnable = runnable;
    }
    
    
    public LongRunningTaskDialog setTitle(String title) {
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(title);
        return this;
    }
    
    
    public LongRunningTaskDialog setMessage(String message) {
        ((TextView) findViewById(R.id.TextView_Description)).setText(message);
        return this;
    }
    
    
    public void start() {
        show();
        new Thread() {
            
            public void run() {
                runnable.run();
                
                taskCompleted();
            };
        }.start();
    }
    
    
    private void taskCompleted() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            
            @Override
            public void run() {
                
                dismiss();
            }
        });
    }
}
