package de.unistuttgart.ipvs.pmp.gui.util.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

public class DialogLongRunningTask extends Dialog {
    
    private Runnable runnable;
    
    
    public DialogLongRunningTask(Context context, Runnable runnable) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_long_running_task);
        
        setCancelable(false);
        
        this.runnable = runnable;
    }
    
    
    public DialogLongRunningTask setTitle(String title) {
        ((BasicTitleView) findViewById(R.id.Title)).setTitle(title);
        return this;
    }
    
    
    public DialogLongRunningTask setMessage(String message) {
        ((TextView) findViewById(R.id.TextView_Description)).setText(message);
        return this;
    }
    
    
    public void start() {
        show();
        new Thread() {
            
            @Override
            public void run() {
                DialogLongRunningTask.this.runnable.run();
                
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
