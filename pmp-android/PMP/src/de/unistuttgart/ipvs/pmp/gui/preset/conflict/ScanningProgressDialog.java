package de.unistuttgart.ipvs.pmp.gui.preset.conflict;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.conflicts.ConflictModel;
import de.unistuttgart.ipvs.pmp.model.conflicts.IProcessingCallback;

public class ScanningProgressDialog extends Dialog {
    
    private ActivityConflictList activity;
    
    
    public ScanningProgressDialog(ActivityConflictList activity) {
        super(activity);
        
        this.activity = activity;
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_conflicts_scanning);
        
        setCancelable(false);
    }
    
    
    public void start() {
        show();
        
        ConflictModel.getInstance().calculateConflicts(new IProcessingCallback() {
            
            @Override
            public void stepMessage(String message) {
                setMessage(message);
            }
            
            
            @Override
            public void progressUpdate(int completed, int fullCount) {
                setProgress(completed, fullCount);
                
            }
            
            
            @Override
            public void finished() {
                updateActivity();
                dismiss();
            }
            
        });
    }
    
    
    private void setMessage(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            
            @Override
            public void run() {
                ((TextView) findViewById(R.id.TextView_Description)).setText(message);
            }
        });
    }
    
    
    private void setProgress(final int completed, final int fullCount) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            
            @Override
            public void run() {
                ProgressBar progress = (ProgressBar) findViewById(R.id.ProgressBar);
                progress.setProgress(completed);
                progress.setMax(fullCount);
            }
        });
    }
    
    
    private void updateActivity() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            
            @Override
            public void run() {
                activity.refresh();
            }
        });
    }
}
