package de.unistuttgart.ipvs.pmp.model.context.time;

import android.content.Context;
import android.graphics.drawable.Drawable;
import de.unistuttgart.ipvs.pmp.PMPApplication;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;

public class TimeContext implements IContext {
    
    private IContextView view = null;
    
    private long lastState;
    
    
    public TimeContext() {
        this.lastState = System.currentTimeMillis();
    }
    
    
    @Override
    public String getIdentifier() {
        return "TimeContext";
    }
    
    
    @Override
    public String getName() {
        return PMPApplication.getContext().getString(R.string.contexts_time_name);
    }
    
    
    @Override
    public String getDescription() {
        return PMPApplication.getContext().getString(R.string.contexts_time_desc);
    }
    
    
    @Override
    public Drawable getIcon() {
        return PMPApplication.getContext().getResources().getDrawable(R.drawable.contexts_time_icon);
    }
    
    
    @Override
    public IContextView getView(Context context) {
        if (this.view == null) {
            this.view = new TimeContextView(context);
        }
        return this.view;
    }
    
    
    @Override
    public long update() {
        this.lastState = System.currentTimeMillis();
        return 0L;
    }
    
    
    @Override
    public boolean getLastState(String condition) {
        TimeContextCondition tcc = TimeContextCondition.parse(condition);
        return tcc.satisfiedIn(this.lastState);
    }
}