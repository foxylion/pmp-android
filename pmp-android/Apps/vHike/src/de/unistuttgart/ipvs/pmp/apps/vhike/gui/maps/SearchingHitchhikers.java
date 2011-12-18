package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import de.unistuttgart.ipvs.pmp.R;

/**
 * Experimenting how to display a match (adding overlay to existing overlay)
 * 
 * @author andres
 *
 */
public class SearchingHitchhikers extends Overlay {

	private Context context;
	Paint paint1 = new Paint();
	Paint paint2 = new Paint();

	public SearchingHitchhikers(Context context) {
		this.context = context;
		paint2.setARGB(255, 255, 255, 255);
	}

	public void draw(Canvas canvas, MapView mapView, boolean b) {
		super.draw(canvas, mapView, b);

		int lon = (int) -122.084095;
		int lat = (int) 37.422006;
		GeoPoint gPosition = new GeoPoint(lat, lon);

		// convert point to pixels
		Point screenPts = new Point();
		mapView.getProjection().toPixels(gPosition, screenPts);

		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.icon_ride);
		canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);
		mapView.invalidate();
	}
}
