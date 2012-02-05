package de.unistuttgart.ipvs.pmp.model.context.time;

import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import android.content.Context;
import android.graphics.drawable.Drawable;

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
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public Drawable getIcon() {
        // TODO Auto-generated method stub
        return null;
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
