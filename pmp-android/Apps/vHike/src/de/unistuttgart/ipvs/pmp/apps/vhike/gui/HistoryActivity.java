package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import android.app.ListActivity;
import android.os.Bundle;

//import android.widget.ArrayAdapter;

//import de.unistuttgart.ipvs.pmp.R;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.content.Context;
//
//import java.util.List;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import de.unistuttgart.ipvs.pmp.gui.activity.AppsActivity;
//import de.unistuttgart.ipvs.pmp.model.element.app.IApp;

/**
 * This Activity gives user the List of all driven journeys
 * 
 * @author Anton Makarov
 * 
 */

public class HistoryActivity extends ListActivity {

	private HistoryActivityAdapter mAdapter;

	private ArrayList<AWName> mData;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initiateData();

		mAdapter = new HistoryActivityAdapter(this, mData);
		this.setListAdapter(mAdapter);

	}

	private void initiateData() {
		mData = new ArrayList<AWName>();

		AWName name = new AWName("Berlin", "12.03.2002");
		mData.add(name);

		name = new AWName("Stuttgart", "28.09.2010");
		mData.add(name);

		name = new AWName("München", "23.11.2011");
		mData.add(name);

		name = new AWName("Vaihingen", "01.12.2011");
		mData.add(name);
	}
}
