package de.unistuttgart.ipvs.pmp.apps.calendarapp.gui.dialogs;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.R;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Todo;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.model.Model;
import de.unistuttgart.ipvs.pmp.apps.calendarapp.sqlConnector.SqlConnector;

public class ChangeTodoDialog extends Dialog {
    
    /**
     * Index of the date to edit
     */
    private int dateIndex;
    
    /**
     * The date picker
     */
    private DatePicker dPicker;
    
    /**
     * The TextView with the description
     */
    private TextView desc;
    
    /**
     * The button to confirm the dialog
     */
    private Button confirm;
    
    /**
     * Original date
     */
    private Todo todo;
    
    
    /**
     * Necessary constructor
     * 
     * @param context
     *            the context
     */
    public ChangeTodoDialog(Context context, int id) {
        super(context);
        this.dateIndex = id;
    }
    
    
    /**
     * Called when the dialog is first created. Gets all elements of the gui
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.changedate);
        
        this.setTitle("Change date");
        
        this.todo = Model.getInstance().getTodoByIndex(this.dateIndex);
        Date date = todo.getDate();
        
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        
        this.dPicker = (DatePicker) findViewById(R.id.datePickerChange);
        this.dPicker.init(year, month, day, null);
        
        this.desc = (TextView) findViewById(R.id.descriptionChangeDate);
        this.desc.setText(this.todo.getDescrpition());
        
        this.confirm = (Button) findViewById(R.id.ConfirmButtonChange);
        this.confirm.setOnClickListener(new ConfirmListener());
        
        /*
         * Neeeded to fill the width of the screen
         */
        getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    
    /**
     * Listener class only needed for the confirm button
     * 
     * @author Thorsten Berberich
     * 
     */
    protected class ConfirmListener implements android.view.View.OnClickListener {
        
        /**
         * Stores the entered data.
         */
        @Override
        public void onClick(View v) {
            // The chosen month
            int month = ChangeTodoDialog.this.dPicker.getMonth();
            int year = ChangeTodoDialog.this.dPicker.getYear();
            int day = ChangeTodoDialog.this.dPicker.getDayOfMonth();
            
            Calendar cal = new GregorianCalendar(year, month, day);
            
            SqlConnector.getInstance().changeDate(ChangeTodoDialog.this.todo.getId(), cal.getTime(),
                    ChangeTodoDialog.this.desc.getText().toString());
            dismiss();
        }
        
    }
}
