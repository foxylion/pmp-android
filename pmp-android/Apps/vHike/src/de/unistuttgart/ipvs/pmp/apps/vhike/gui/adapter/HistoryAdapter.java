package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.tools.HistoryRideObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryAdapter extends BaseAdapter{
	
	Context context;
	List<HistoryRideObject> historyRides;
	public HistoryAdapter(Context context, List<HistoryRideObject> historyRides ) {
		this.context = context;
		this.historyRides = historyRides;
	}
	@Override
	public int getCount() {
		return historyRides.size();
	}

	@Override
	public Object getItem(int position) {
		return historyRides.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final HistoryRideObject hRideObject = historyRides.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout entryView = (LinearLayout) inflater.inflate(
				R.layout.history_layout_list, null);
		
		TextView tv_date = (TextView) entryView.findViewById(R.id.history_date);
		TextView tv_destination = (TextView) entryView.findViewById(R.id.history_destination);
		Log.i("Creation:" + hRideObject.getCreation());
		Log.i("Destination:" + hRideObject.getDestination());
		
		tv_date.setText(hRideObject.getCreation());
		tv_destination.setText(hRideObject.getDestination());
		
		entryView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, hRideObject.getDestination(), Toast.LENGTH_SHORT).show();
			}
		});
		
		return entryView;
	}

}
