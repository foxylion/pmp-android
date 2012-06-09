/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
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

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
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
        pathPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setStrokeCap(Paint.Cap.ROUND);
        pathPaint.setStrokeWidth(4);
        pathPaint.setColor(this.context.getResources().getColor(R.color.emirates_red));
        pathPaint.setAlpha(120);
        
        Point startPoint = new Point();
        projection.toPixels(this.gp1, startPoint);
        
        Point endPoint = new Point();
        projection.toPixels(this.gp2, endPoint);
        
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
