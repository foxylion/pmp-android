/*
 * Copyright 2012 pmp-android development team
 * Project: vHike
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

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
    
    
    /**
     * Set drawable icon, context for onTap method, and drivers position (gps)
     * to draw the perimeter
     * 
     * @param defaultMarker
     * @param context
     * @param gps
     */
    public DriverOverlay(Drawable defaultMarker, Context context, GeoPoint gps) {
        super(boundCenterBottom(defaultMarker));
        mContext = context;
        mGps = gps;
    }
    
    
    public DriverOverlay(Drawable defaultMarker) {
        super(boundCenterBottom(defaultMarker));
    }
    
    
    public void addOverlay(OverlayItem overlay) {
        mOverlays.add(overlay);
        populate();
    }
    
    
    // Removes overlay item i
    public void removeOverlay(int i) {
        mOverlays.remove(i);
        populate();
    }
    
    
    /**
     * if drawable is tapped, a dialog will pop up containing short information
     * about the driver
     */
    @Override
    protected boolean onTap(int i) {
        OverlayItem item = mOverlays.get(i);
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
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
        // convert point to pixels
        Point screenPts = new Point();
        mapView.getProjection().toPixels(mGps, screenPts);
        
        Paint myCircle = new Paint();
        myCircle.setColor(Color.BLUE);
        myCircle.setAntiAlias(true);
        myCircle.setStyle(Style.FILL);
        myCircle.setAlpha(30);
        
        canvas.drawCircle(screenPts.x, screenPts.y - 15, 100, myCircle);
        
        return true;
    }
    
    
    /**
     * create item for List of overlays
     */
    @Override
    protected OverlayItem createItem(int i) {
        return mOverlays.get(i);
    }
    
    
    /**
     * size of overlay list
     */
    @Override
    public int size() {
        return mOverlays.size();
    }
    
}
