package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import de.unistuttgart.ipvs.pmp.R;

/**
 * Sets a marker on current location through a corresponding user (driver or passenger)
 * @author Andre
 *
 */
public class MapOverlay extends Overlay {

	private Context context;
	private GeoPoint gPosition;
	private int whichHitchhiker;
	private Bitmap bmp;

	public MapOverlay(Context context, GeoPoint gPosition, int whichHitchhiker) {
		this.context = context;
		this.gPosition = gPosition;
		this.whichHitchhiker = whichHitchhiker;
	}

	public void setPointToDraw(GeoPoint point) {
		gPosition = point;
	}

	public GeoPoint getgPosition() {
		return gPosition;
	}

	/**
	 * eigene Draw-Implementierung
	 */
	@Override
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		super.draw(canvas, mapView, shadow);

		// convert point to pixels
		Point screenPts = new Point();
		mapView.getProjection().toPixels(gPosition, screenPts);

		// add marker
		if (whichHitchhiker == 1) {
			bmp = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.icon_ride);
		} else {
			bmp = BitmapFactory.decodeResource(context.getResources(),
					R.drawable.passenger_logo);
		}
		
		canvas.drawBitmap(bmp, screenPts.x, screenPts.y - 24, null);
		return true;
	}
}
