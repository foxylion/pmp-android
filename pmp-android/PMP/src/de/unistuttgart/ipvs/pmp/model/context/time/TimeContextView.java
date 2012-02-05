package de.unistuttgart.ipvs.pmp.model.context.time;

import android.content.Context;
import android.view.View;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

public class TimeContextView extends View implements IContextView {
    
    public TimeContextView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    
    
    @Override
    public View asView() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public String getViewCondition() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public void setViewCondition(String condition) throws InvalidConditionException {
        // TODO Auto-generated method stub
        
    }
    
}
