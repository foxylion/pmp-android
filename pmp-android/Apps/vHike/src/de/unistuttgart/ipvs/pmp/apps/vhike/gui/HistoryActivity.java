package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

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

	
	static final String[] DRIVES = new String[] {
        "Berlin 12.03.2002", "Stuttgart 28.09.2010", "..."};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, DRIVES));
	}

}
