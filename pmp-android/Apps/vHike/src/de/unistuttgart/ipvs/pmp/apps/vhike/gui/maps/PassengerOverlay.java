package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.vhikeDialogs;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.resourcegroups.vHikeWS.aidl.IvHikeWebservice;

/**
 * Overlay for passengers, handling the drawable icon, tap actions
 * 
 * @author Andre Nguyen
 * 
 */
@SuppressWarnings("rawtypes")
public class PassengerOverlay extends ItemizedOverlay {
    
    private Context mContext;
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private MapView mapView;
    private IvHikeWebservice ivhs;
    
    /**
     * set passenger icon through drawable and context for onTap method
     * 
     * @param defaultMarker
     * @param context
     */
    public PassengerOverlay(Drawable defaultMarker, Context context, MapView mapView, IvHikeWebservice ivhs) {
        super(boundCenterBottom(defaultMarker));
        this.mContext = context;
        this.mapView = mapView;
        this.ivhs = ivhs;
    }
    
    
    public void addOverlay(OverlayItem overlay) {
        this.mOverlays.add(overlay);
        populate();
    }
    
    
    /**
     * Opens a dialog containing short information about the passenger
     */
    @Override
    protected boolean onTap(int i) {
        OverlayItem item = this.mOverlays.get(i);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this.mContext);
//        dialog.setTitle(item.getTitle());
//        dialog.setMessage(item.getSnippet());
//        dialog.show();
        int id = Integer.valueOf(item.getTitle());
        Controller ctrl = new Controller(ivhs);
        Profile user = ctrl.getProfile(Model.getInstance().getSid(), id);
//        int lat = user.
        vhikeDialogs.getInstance().getContactDialog(mContext, mapView, item.getSnippet()).show();
        return true;
    }
    
    
    @Override
    protected OverlayItem createItem(int i) {
        return this.mOverlays.get(i);
    }
    
    
    @Override
    public int size() {
        return this.mOverlays.size();
    }
    
}
