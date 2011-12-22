package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps;

import java.util.List;

import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapModel {

	private static MapModel instance;
	private List<Overlay> mapOverlays;

	public static MapModel getInstance() {
		if (instance == null) {
			instance = new MapModel();
		}
		return instance;
	}

	public List<Overlay> getOverlayList(MapView mapView) {
		if (mapOverlays == null) {
			mapOverlays = mapView.getOverlays();
		}
		return mapOverlays;
	}

}
