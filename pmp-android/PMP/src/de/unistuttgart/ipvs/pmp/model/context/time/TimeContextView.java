package de.unistuttgart.ipvs.pmp.model.context.time;

import java.util.TimeZone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
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
    
    private static TimeZone UTC = TimeZone.getTimeZone("GMT");
    
    /**
     * Value currently in the view
     */
    private TimeContextCondition value;
    
    /**
     * {@link TimePicker}s
     */
    protected TimePicker beginPicker, endPicker;
    
    /**
     * {@link CheckBox} for whole day
     */
    private CheckBox dayBox;
    
    /**
     * {@link RadioButton}s for UTC vs. fixed with phone
     */
    private RadioButton timeWithPhone, timeWithLocation;
    
    
    public TimeContextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }
    
    
    public TimeContextView(Context context) {
        super(context);
        setup(context);
    }
    
    
    private void setup(Context context) {
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
        this.dayBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TimeContextView.this.beginPicker.setEnabled(!isChecked);
                TimeContextView.this.endPicker.setEnabled(!isChecked);
                
                if (isChecked) {
                    TimeContextView.this.beginPicker.setCurrentHour(0);
                    TimeContextView.this.beginPicker.setCurrentMinute(0);
                    //TimeContextView.this.beginPicker.setCurrentSecond(0);
                    
                    TimeContextView.this.endPicker.setCurrentHour(23);
                    TimeContextView.this.endPicker.setCurrentMinute(59);
                    //TimeContextView.this.endPicker.setCurrentSecond(59);
                }
            }
        });
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public String getViewCondition() {
        this.value.setUTC(this.timeWithLocation.isChecked());
        
        this.value.getBegin().setHour(this.beginPicker.getCurrentHour());
        this.value.getBegin().setMinute(this.beginPicker.getCurrentMinute());
        this.value.getBegin().setSecond(0);
        
        this.value.getEnd().setHour(this.endPicker.getCurrentHour());
        this.value.getEnd().setMinute(this.endPicker.getCurrentMinute());
        this.value.getEnd().setSecond(0);
        
        if (this.value.isUTC()) {
            // convert to UTC
            this.value.getBegin().convertTimeZone(TimeZone.getDefault(), UTC);
            this.value.getEnd().convertTimeZone(TimeZone.getDefault(), UTC);
        }
        
        return this.value.toString();
    }
    
    
    @Override
    public void setViewCondition(String condition) throws InvalidConditionException {
        this.value = TimeContextCondition.parse(condition);
        
        TimeContextConditionTime begin = this.value.getBegin();
        TimeContextConditionTime end = this.value.getEnd();
        if (this.value.isUTC()) {
            begin.convertTimeZone(UTC, TimeZone.getDefault());
            end.convertTimeZone(UTC, TimeZone.getDefault());
        }
        // set field values
        this.beginPicker.setCurrentHour(begin.getHour());
        this.beginPicker.setCurrentMinute(begin.getMinute());
        //this.beginPicker.setCurrentSecond(begin.getSecond());
        
        this.endPicker.setCurrentHour(end.getHour());
        this.endPicker.setCurrentMinute(end.getMinute());
        //this.endPicker.setCurrentSecond(end.getSecond());
        
        this.dayBox.setChecked(this.value.representsWholeDay());
        
        this.timeWithPhone.setChecked(!this.value.isUTC());
        this.timeWithLocation.setChecked(this.value.isUTC());
    }
}
