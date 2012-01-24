package de.unistuttgart.ipvs.pmp.api.gui.registration;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;

/**
 * The Elements class provides all elements defined in the xml.
 * 
 * @author Jakob Jarosch
 */
class RegistrationElements {
    
    private IRegistrationUI parentUi;
    
    public TextView tvFailureMissingActivity;
    public TextView tvFailureMissingPMP;
    public TextView tvFailureError;
    public TextView tvFailureErrorMessage;
    public TextView tvOpenApp;
    public TextView tvSelectInitialSF;
    
    public Button buttonClose;
    public Button buttonOpenApp;
    public Button buttonSelectInitialSF;
    
    private List<RegistrationStateListItem> registrationStateList = new ArrayList<RegistrationStateListItem>();
    
    
    public RegistrationElements(IRegistrationUI activity) {
        this.parentUi = activity;
        
        this.tvFailureMissingActivity = (TextView) parentUi.findViewById(R.id.TextView_Failure_MissingActivity);
        this.tvFailureMissingPMP = (TextView) parentUi.findViewById(R.id.TextView_Failure_MissingPMP);
        this.tvFailureError = (TextView) parentUi.findViewById(R.id.TextView_Failure_Error);
        this.tvFailureErrorMessage = (TextView) parentUi.findViewById(R.id.TextView_Failure_ErrorMessage);
        this.tvOpenApp = (TextView) parentUi.findViewById(R.id.TextView_OpenApp);
        this.tvSelectInitialSF = (TextView) parentUi.findViewById(R.id.TextView_OpenSFList);
        
        this.buttonClose = (Button) activity.findViewById(R.id.Button_Close);
        this.buttonOpenApp = (Button) activity.findViewById(R.id.Button_OpenApp);
        this.buttonSelectInitialSF = (Button) activity.findViewById(R.id.Button_OpenSFList);
        
        fillRegistrationStateList();
        
        addListener();
    }
    
    
    /**
     * Updates the state of an item from the list.
     * 
     * @param item
     *            Number of the item, begins with 1.
     * @param state
     *            New state of the item.
     */
    public void setState(int item, RegistrationStateListItem.State state) {
        registrationStateList.get(item - 1).setState(state);
    }
    
    
    private void fillRegistrationStateList() {
        registrationStateList.add(new RegistrationStateListItem(parentUi.getContext(), 1, "Preparing & detecting PMP"));
        registrationStateList.add(new RegistrationStateListItem(parentUi.getContext(), 2, "Checking registration"));
        registrationStateList.add(new RegistrationStateListItem(parentUi.getContext(), 3, "Registration at PMP"));
        registrationStateList.add(new RegistrationStateListItem(parentUi.getContext(), 4,
                "Set initial Service Features"));
        registrationStateList.add(new RegistrationStateListItem(parentUi.getContext(), 5, "Open the App"));
        
        ((LinearLayout) parentUi.findViewById(R.id.LinearLayout_States)).removeAllViews();
        
        for (RegistrationStateListItem item : registrationStateList) {
            ((LinearLayout) parentUi.findViewById(R.id.LinearLayout_States)).addView(item);
        }
    }
    
    
    private void addListener() {
        this.buttonClose.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                parentUi.close();
            }
        });
        
        this.buttonSelectInitialSF.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                parentUi.invokeEvent(RegistrationEventTypes.SF_SCREEN_OPENED);
            }
        });
        
        this.buttonOpenApp.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                parentUi.invokeEvent(RegistrationEventTypes.OPEN_APP);
            }
        });
    }
}
