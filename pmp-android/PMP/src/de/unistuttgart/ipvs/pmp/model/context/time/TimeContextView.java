package de.unistuttgart.ipvs.pmp.model.context.time;

import java.util.TimeZone;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TimePicker;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

/**
 * View component for the {@link TimeContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextView extends LinearLayout implements IContextView {
    
    /**
     * Value currently in the view
     */
    private TimeContextCondition value;
    
    /**
     * {@link TimePicker}s
     */
    private TimePicker beginPicker, endPicker;
    
    /**
     * {@link CheckBox} for whole day
     */
    private CheckBox dayBox;
    
    /**
     * {@link RadioButton}s for UTC vs. fixed with phone
     */
    private RadioButton timeWithPhone, timeWithLocation;
    
    
    public TimeContextView(Context context) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);
        
        inflate(context, R.layout.contexts_time_view, this);
        
        // set fields
        this.beginPicker = (TimePicker) findViewById(R.id.beginPicker);
        this.endPicker = (TimePicker) findViewById(R.id.endPicker);
        this.dayBox = (CheckBox) findViewById(R.id.dayBox);
        this.timeWithPhone = (RadioButton) findViewById(R.id.kindWithPhone);
        this.timeWithLocation = (RadioButton) findViewById(R.id.kindWithLocation);
        
        addListeners();
    }
    
    
    private void addListeners() {
        // TODO actions in Listneers        
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public String getViewCondition() {
        this.value.setUTC(this.timeWithLocation.isChecked());
        
        int timeZoneDeltaMin = 0;
        int timeZoneDeltaHrs = 0;
        if (this.value.isUTC()) {
            timeZoneDeltaMin = TimeZone.getDefault().getRawOffset() / 60000;
            timeZoneDeltaHrs = timeZoneDeltaMin / 60;
            timeZoneDeltaMin %= 60;
        }
        
        this.value.getBegin().setHour(this.beginPicker.getCurrentHour() - timeZoneDeltaHrs);
        this.value.getBegin().setMinute(this.beginPicker.getCurrentMinute() - timeZoneDeltaMin);
        this.value.getBegin().setSecond(0);
        
        this.value.getEnd().setHour(this.endPicker.getCurrentHour() - timeZoneDeltaHrs);
        this.value.getEnd().setMinute(this.endPicker.getCurrentMinute() - timeZoneDeltaMin);
        this.value.getEnd().setSecond(0);
        
        return this.value.toString();
    }
    
    
    @Override
    public void setViewCondition(String condition) throws InvalidConditionException {
        this.value = TimeContextCondition.parse(condition);
        
        int timeZoneDeltaMin = 0;
        int timeZoneDeltaHrs = 0;
        if (this.value.isUTC()) {
            timeZoneDeltaMin = TimeZone.getDefault().getRawOffset() / 60000;
            timeZoneDeltaHrs = timeZoneDeltaMin / 60;
            timeZoneDeltaMin %= 60;
        }
        // set field values
        this.beginPicker.setCurrentHour(timeZoneDeltaHrs + this.value.getBegin().getHour());
        this.beginPicker.setCurrentMinute(timeZoneDeltaMin + this.value.getBegin().getMinute());
        //this.beginPicker.setCurrentSecond(timeZoneDelta + this.value.getBegin().getSecond());
        
        this.endPicker.setCurrentHour(timeZoneDeltaHrs + this.value.getEnd().getHour());
        this.endPicker.setCurrentMinute(timeZoneDeltaMin + this.value.getEnd().getMinute());
        //this.endPicker.setCurrentSecond(this.value.getEnd().getSecond());
        
        this.dayBox.setChecked(this.value.representsWholeDay());
        
        this.timeWithPhone.setChecked(!this.value.isUTC());
        this.timeWithLocation.setChecked(this.value.isUTC());
    }
}
