package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.PickUp;

/**
 * Overlay for passengers, handling the drawable icon, tap actions and drawing
 * the perimeter
 * 
 * @author andres
 * 
 */
@SuppressWarnings("rawtypes")
public class PassengerOverlay extends ItemizedOverlay {
    
    private Context mContext;
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private int userID;
    
    
    /**
     * set passenger icon through drawable and context for onTap method
     * 
     * @param defaultMarker
     * @param context
     */
    public PassengerOverlay(Drawable defaultMarker, Context context, int userID) {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
        this.userID = userID;
    }
    
    
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
    
    
    /**
     * Opens a dialog containing short information about the passenger
     */
    @Override
    protected boolean onTap(int i) {
        OverlayItem item = mOverlays.get(i);
        //        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        //        dialog.setTitle(item.getTitle());
        //        dialog.setMessage(item.getSnippet());
        //        dialog.show();
        PickUp pickUpDialog = new PickUp(mContext, userID);
        pickUpDialog.setTitle(item.getTitle());
        pickUpDialog.show();
        return true;
    }
    
    
    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }
    
    
    @Override
    public int size() {
        return mOverlays.size();
    }
    
}
