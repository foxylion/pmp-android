package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.dialog.ContactDialog;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;
import de.unistuttgart.ipvs.pmp.resourcegroups.contact.aidl.IContact;
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
    private IContact iContact;
    private String name;
    private GeoPoint mGps;
    
    private ContactDialog contactDialog;
    private int itsMe;
    
    
    /**
     * set passenger icon through drawable and context for onTap method
     * 
     * @param defaultMarker
     * @param context
     */
    public PassengerOverlay(Drawable defaultMarker, Context context, MapView mapView, IvHikeWebservice ivhs,
            IContact iContact, String name, GeoPoint gps, ContactDialog contactDialog, int itsMe) {
        super(boundCenterBottom(defaultMarker));
        this.mContext = context;
        this.mapView = mapView;
        this.ivhs = ivhs;
        this.iContact = iContact;
        this.name = name;
        this.mGps = gps;
        
        this.contactDialog = contactDialog;
        this.itsMe = itsMe;
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
        
        //if 0 passenger, if 1 user
        if (itsMe == 0) {
            int id = Integer.valueOf(item.getTitle());
            Controller ctrl = new Controller(ivhs);
            Profile user = ctrl.getProfile(Model.getInstance().getSid(), id);
            //        int lat = user. (get lat lng
            
            contactDialog.setToGPS(mGps);
            contactDialog.show();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(this.mContext);
            dialog.setTitle(item.getTitle());
            dialog.setMessage(item.getSnippet());
            dialog.show();
        }
        //        vhikeDialogs.getInstance().getContactDialog(mContext, mapView, item.getSnippet(), iContact).show();
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
    
    
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);
        Projection projection = mapView.getProjection();
        
        Point point = new Point();
        Paint paint = new Paint();
        paint.setColor(mContext.getResources().getColor(R.color.orange));
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        projection.toPixels(mGps, point);
        
        canvas.drawText(name, point.x - 20, point.y - 55, paint);
        
        return true;
    }
    
}
