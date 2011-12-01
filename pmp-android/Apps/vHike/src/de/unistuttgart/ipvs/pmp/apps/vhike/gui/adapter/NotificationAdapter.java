package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NotificationAdapter extends BaseAdapter {

	private Context context;
	private List<Profile> hitchhikers;

	public NotificationAdapter(Context context, List<Profile> hitchhikers) {
		this.context = context;
		this.hitchhikers = hitchhikers;
	}

	@Override
	public int getCount() {
		return hitchhikers.size();
	}

	@Override
	public Object getItem(int position) {
		return hitchhikers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Profile hitchhiker = hitchhikers.get(position);

		/* load the layout from the xml file */
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout entryView = (LinearLayout) inflater.inflate(
				R.layout.hitchhiker_list, null);

		Button dismiss = (Button) entryView.findViewById(R.id.dismissBtn);
		// dismiss.setText("Dismiss");
		dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Dismiss", Toast.LENGTH_LONG);
			}
		});

		TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
	//	name.setText(hitchhiker.getUser_name());
		name.setClickable(true);

		Button accept = (Button) entryView.findViewById(R.id.acceptBtn);
		// accept.setText("Accept");
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Accept", Toast.LENGTH_SHORT);
			}
		});

		return entryView;
	}

}
