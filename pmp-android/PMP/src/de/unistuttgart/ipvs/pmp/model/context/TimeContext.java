package de.unistuttgart.ipvs.pmp.model.context;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class TimeContext implements IContext {
    
    private IContextView view = null;
    
    
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
        // TODO Auto-generated method stub
        return 0;
    }
    
    
    @Override
    public boolean getLastState(String condition) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
