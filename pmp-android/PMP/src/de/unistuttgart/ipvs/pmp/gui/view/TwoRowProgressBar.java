package de.unistuttgart.ipvs.pmp.gui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import de.unistuttgart.ipvs.pmp.R;

public class TwoRowProgressBar extends Dialog {
    
    private LinearLayout pbTasksContainer;
    private ProgressBar pbTasks;
    private ProgressBar pbProgress;
    
    
    public TwoRowProgressBar(Context context) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_two_row_progressbar);
        
        pbTasksContainer = (LinearLayout) findViewById(R.id.LinearLayout_Progress_Tasks);
        pbTasks = (ProgressBar) findViewById(R.id.ProgressBar_Tasks);
        pbProgress = (ProgressBar) findViewById(R.id.ProgressBar_Progress);
    }
    
    
    public void setTaskProgress(int current, int max) {
        if (max <= 1) {
            pbTasksContainer.setVisibility(View.GONE);
        } else {
            pbTasksContainer.setVisibility(View.VISIBLE);
        }
        
        pbTasks.setMax(max);
        pbTasks.setProgress(current);
    }
    
    
    public void setProgress(int current, int max) {
        pbProgress.setMax(max);
        pbProgress.setProgress(current);
    }
    
}
