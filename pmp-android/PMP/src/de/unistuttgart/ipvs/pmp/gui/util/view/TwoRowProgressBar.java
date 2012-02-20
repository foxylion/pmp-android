package de.unistuttgart.ipvs.pmp.gui.util.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.view.BasicTitleView;

public class TwoRowProgressBar extends Dialog {
    
    private BasicTitleView btvTitle;
    private LinearLayout pbTasksContainer;
    private ProgressBar pbTasks;
    private ProgressBar pbProgress;
    
    
    public TwoRowProgressBar(Context context) {
        super(context);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.dialog_two_row_progressbar);
        
        this.btvTitle = (BasicTitleView) findViewById(R.id.Title);
        this.pbTasksContainer = (LinearLayout) findViewById(R.id.LinearLayout_Progress_Tasks);
        this.pbTasks = (ProgressBar) findViewById(R.id.ProgressBar_Tasks);
        this.pbProgress = (ProgressBar) findViewById(R.id.ProgressBar_Progress);
    }
    
    
    @Override
    public void setTitle(CharSequence title) {
        this.btvTitle.setTitle(title.toString());
    }
    
    
    public void setTaskProgress(int current, int max) {
        if (max <= 1) {
            this.pbTasksContainer.setVisibility(View.GONE);
        } else {
            this.pbTasksContainer.setVisibility(View.VISIBLE);
        }
        
        this.pbTasks.setMax(max);
        this.pbTasks.setProgress(current);
    }
    
    
    public void setProgress(int current, int max) {
        this.pbProgress.setMax(max);
        this.pbProgress.setProgress(current);
    }
    
}