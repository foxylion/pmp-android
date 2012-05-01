package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;

import de.unistuttgart.ipvs.pmp.apps.vhike.R;

public class RouteOverlay extends Overlay {
    
    private Context context;
    private GeoPoint gp1;
    private GeoPoint gp2;
    
    
    public RouteOverlay(Context context, GeoPoint gp1, GeoPoint gp2) { // GeoPoint is a int. (6E)
        this.context = context;
        this.gp1 = gp1;
        this.gp2 = gp2;
    }
    
    
    @Override
    public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
        Projection projection = mapView.getProjection();
        
        Paint pathPaint = new Paint();
        pathPaint.setAntiAlias(true);
        pathPaint.setStyle(Style.STROKE);
        pathPaint.setStrokeWidth(4f);
        pathPaint.setColor(context.getResources().getColor(R.color.emirates_red));
        pathPaint.setAlpha(120);
        
        Point startPoint = new Point();
        projection.toPixels(gp1, startPoint);
        
        Point endPoint = new Point();
        projection.toPixels(gp2, endPoint);
        
        Path path = new Path();
        path.moveTo(startPoint.x, startPoint.y);
        path.lineTo(endPoint.x, endPoint.y);
        
//        canvas.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y, pathPaint);
        canvas.drawPath(path, pathPaint);
        
        return super.draw(canvas, mapView, shadow, when);
    }
    
    
    @Override
    public void draw(Canvas canvas, MapView mapView, boolean shadow) {
        super.draw(canvas, mapView, shadow);
    }
    
}
