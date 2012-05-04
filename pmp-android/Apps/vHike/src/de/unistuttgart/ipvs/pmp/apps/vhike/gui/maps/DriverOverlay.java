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

/**
 * OverlayItem for drivers which holds the driver logo and the perimeter
 * 
 * @author Andre Nguyen
 * 
 */
@SuppressWarnings("rawtypes")
public class DriverOverlay extends ItemizedOverlay {
    
    private Context mContext;
    private GeoPoint mGps;
    private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();
    private String name;
    
    
    /**
     * Set drawable icon, context for onTap method, and drivers position (gps)
     * to draw the perimeter
     * 
     * @param defaultMarker
     * @param context
     * @param gps
     */
    public DriverOverlay(Drawable defaultMarker, Context context, GeoPoint gps, String name) {
        super(boundCenterBottom(defaultMarker));
        this.mContext = context;
        this.mGps = gps;
        this.name = name;
    }
    
    
    public DriverOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }
    
    
    public void addOverlay(OverlayItem overlay) {
        this.mOverlays.add(overlay);
        populate();
    }
    
    
    // Removes overlay item i
    public void removeOverlay(int i) {
        this.mOverlays.remove(i);
        populate();
    }
    
    
    /**
     * if drawable is tapped, a dialog will pop up containing short information
     * about the driver
     */
    @Override
    protected boolean onTap(int i) {
        OverlayItem item = this.mOverlays.get(i);
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.mContext);
        dialog.setTitle(item.getTitle());
        dialog.setMessage(item.getSnippet());
        dialog.show();
        return true;
    }
    
    
    /**
     * custom draw implementation to draw the perimeter
     */
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        super.draw(canvas, mapView, shadow);
        Projection projection = mapView.getProjection();
        
        Point point = new Point();
        Paint paint = new Paint();
        paint.setColor(this.mContext.getResources().getColor(R.color.lime_green));
        paint.setAntiAlias(true);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        projection.toPixels(this.mGps, point);
        
        canvas.drawText(this.name, point.x - 15, point.y - 55, paint);
        
        return true;
    }
    
    
    /**
     * create item for List of overlays
     */
    @Override
    protected OverlayItem createItem(int i) {
        return this.mOverlays.get(i);
    }
    
    
    /**
     * size of overlay list
     */
    @Override
    public int size() {
        return this.mOverlays.size();
    }
    
}
