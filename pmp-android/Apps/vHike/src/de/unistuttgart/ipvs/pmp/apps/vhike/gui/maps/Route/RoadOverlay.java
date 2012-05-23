package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.ViewModel;

/**
 * Overlay for a route from a driver to passenger
 * 
 * @author Andre Nguyen
 * 
 */
public class RoadOverlay extends com.google.android.maps.Overlay {
    
    Road mRoad;
    ArrayList<GeoPoint> mPoints;
    
    
    public RoadOverlay(Road road, MapView mv, boolean firstDraw) {
        mRoad = road;
        if (road.mRoute.length > 0) {
            mPoints = new ArrayList<GeoPoint>();
            for (int i = 0; i < road.mRoute.length; i++) {
                mPoints.add(new GeoPoint((int) (road.mRoute[i][1] * 1000000), (int) (road.mRoute[i][0] * 1000000)));
            }
            int moveToLat = (mPoints.get(0).getLatitudeE6() + (mPoints.get(mPoints.size() - 1).getLatitudeE6() - mPoints
                    .get(0).getLatitudeE6()) / 2);
            int moveToLong = (mPoints.get(0).getLongitudeE6() + (mPoints.get(mPoints.size() - 1).getLongitudeE6() - mPoints
                    .get(0).getLongitudeE6()) / 2);
            GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);
            
            Log.i(this, "RoadDesc" + mRoad.mDescription);
            Log.i(this, "RoadName" + mRoad.mName);
            String distance = parseDistance(mRoad.mDescription);
            String time = parseTime(mRoad.mDescription);
            String from = parseFrom(mRoad.mName);
            String to = parseTo(mRoad.mName);
            ViewModel.getInstance().setEtInfoText(from, to, distance, time);
            int zoom = 0;
            try {
                zoom = zoomLevel(Double.valueOf(distance));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                zoom = 15;
            }
            
            MapController mapController = mv.getController();
            mapController.animateTo(moveTo);
            
            mapController.setZoom(zoom);
            
        }
    }
    
    
    /**
     * Inputs roadDescrition and parses the distance
     * 
     * @param roadDescription
     * @return distance
     */
    private String parseDistance(String roadDescription) {
        String[] temp;
        String dist = "";
        temp = roadDescription.split("k");
        Log.i(this, "temp: " + temp[0]);
        temp = temp[0].split("\\:");
        
        dist = temp[1];
        
        // convert to km
        return dist;
    }
    
    
    private String parseTime(String roadDescription) {
        String[] temp;
        temp = roadDescription.split("\\(");
        temp = temp[1].split("\\)");
        Log.i(this, "Time: " + temp[0]);
        return temp[0];
    }
    
    
    private String parseFrom(String roadName) {
        String[] temp;
        temp = roadName.split(" to ");
        Log.i(this, "From: " + temp[0]);
        return temp[0];
    }
    
    
    private String parseTo(String roadName) {
        String[] temp;
        temp = roadName.split(" to ");
        Log.i(this, "To: " + temp[1]);
        return temp[1];
    }
    
    
    /**
     * Calculates a fitting zoomLevel according to a routes distance
     * 
     * @param distance
     * @return zoomLevel
     */
    public static byte zoomLevel(double distance) {
        byte zoom = 1;
        double E = 40075;
        Log.i("Astrology", "result: " + (Math.log(E / distance) / Math.log(2) + 1));
        zoom = (byte) Math.round(Math.log(E / distance) / Math.log(2) + 1);
        // to avoid exeptions
        if (zoom > 21)
            zoom = 21;
        if (zoom < 1)
            zoom = 1;
        
        return zoom;
    }
    
    
    @Override
    public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
        super.draw(canvas, mv, shadow);
        drawPath(mv, canvas);
        return true;
    }
    
    
    public void drawPath(MapView mv, Canvas canvas) {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        for (int i = 0; i < mPoints.size(); i++) {
            Point point = new Point();
            mv.getProjection().toPixels(mPoints.get(i), point);
            x2 = point.x;
            y2 = point.y;
            if (i > 0) {
                canvas.drawLine(x1, y1, x2, y2, paint);
            }
            x1 = x2;
            y1 = y2;
        }
    }
}
