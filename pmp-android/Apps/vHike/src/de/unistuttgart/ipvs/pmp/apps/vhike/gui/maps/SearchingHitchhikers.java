package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import de.unistuttgart.ipvs.pmp.R;

/**
 * Experimenting how to display a match (adding overlay to existing overlay)
 * 
 * @author andres
 * 
 */
public class SearchingHitchhikers extends Overlay {

	private Context context;
	private GeoPoint gPosition;
	private int whichHitchhiker;
	private Bitmap bmp;

	public SearchingHitchhikers(Context context, GeoPoint gPosition,
			int whichHitchhiker) {
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

		/**
		 * draw radius circle
		 */
		Paint myCircle = new Paint();
		myCircle.setColor(Color.BLUE);
		myCircle.setAntiAlias(true);
		myCircle.setStyle(Style.STROKE);

		// add marker
		bmp = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.passenger_logo);

		canvas.drawBitmap(bmp, screenPts.x, screenPts.y, null);
		canvas.drawCircle(screenPts.x, screenPts.y, 100, myCircle);
		mapView.invalidate();
		return true;
	}

}
