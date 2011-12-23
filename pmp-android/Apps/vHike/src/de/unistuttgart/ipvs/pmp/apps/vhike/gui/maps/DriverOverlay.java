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
 * @author andres
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
	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
			long when) {
		super.draw(canvas, mapView, shadow);
		// convert point to pixels
		Point screenPts = new Point();
		mapView.getProjection().toPixels(mGps, screenPts);

		Paint myCircle = new Paint();
		myCircle.setColor(Color.BLUE);
		myCircle.setAntiAlias(true);
		myCircle.setStyle(Style.STROKE);

		canvas.drawCircle(screenPts.x, screenPts.y, 100, myCircle);

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
