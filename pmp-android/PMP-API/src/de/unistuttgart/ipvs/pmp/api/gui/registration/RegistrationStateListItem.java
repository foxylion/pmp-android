package de.unistuttgart.ipvs.pmp.api.gui.registration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.R;

public class RegistrationStateListItem extends LinearLayout {
    
    private TextView itemNumberTv;
    private TextView itemNameTv;
    private ProgressBar itemProgessPb;
    private ImageView itemStateIv;
    private String itemName;
    private int itemNumber;
    
    public enum State {
        NONE,
        PROCESSING,
        SUCCESS,
        FAIL,
        NEW,
        SKIPPED
    }
    
    
    public RegistrationStateListItem(Context context, int itemNumber, String itemName) {
        super(context);
        
        this.itemName = itemName;
        this.itemNumber = itemNumber;
        
        /* create the layout */
        LayoutInflater layoutInflater = (LayoutInflater) this.getContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        
        View view = layoutInflater.inflate(R.layout.pmp_api_registration_list_state_item, null);
        addView(view);
        
        setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
        
        this.itemNumberTv = (TextView) findViewById(R.id.TextView_ItemNumber);
        this.itemNameTv = (TextView) findViewById(R.id.TextView_ItemName);
        this.itemProgessPb = (ProgressBar) findViewById(R.id.ProgressBar_ItemProcessing);
        this.itemStateIv = (ImageView) findViewById(R.id.ImageView_ItemState);
        
        this.itemNumberTv.setText("" + this.itemNumber);
        this.itemNameTv.setText(this.itemName);
    }
    
    
    public void setState(State state) {
        switch (state) {
            case NONE:
                this.itemProgessPb.setVisibility(View.GONE);
                this.itemStateIv.setVisibility(View.GONE);
                break;
            
            case PROCESSING:
                this.itemProgessPb.setVisibility(View.VISIBLE);
                this.itemStateIv.setVisibility(View.GONE);
                break;
            
            case SUCCESS:
                this.itemProgessPb.setVisibility(View.GONE);
                this.itemStateIv.setVisibility(View.VISIBLE);
                this.itemStateIv.setImageResource(R.drawable.icon_success);
                break;
            
            case FAIL:
                this.itemProgessPb.setVisibility(View.GONE);
                this.itemStateIv.setVisibility(View.VISIBLE);
                this.itemStateIv.setImageResource(R.drawable.icon_delete);
                break;
            
            case NEW:
                this.itemProgessPb.setVisibility(View.GONE);
                this.itemStateIv.setVisibility(View.VISIBLE);
                this.itemStateIv.setImageResource(R.drawable.icon_arrow_left);
                break;
            
            case SKIPPED:
                this.itemProgessPb.setVisibility(View.GONE);
                this.itemStateIv.setVisibility(View.VISIBLE);
                this.itemStateIv.setImageResource(R.drawable.icon_semi_success);
                break;
        
        }
    }
}
