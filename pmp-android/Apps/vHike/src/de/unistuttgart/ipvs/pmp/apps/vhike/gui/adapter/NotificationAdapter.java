package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Profile;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		final Button accept = (Button) entryView.findViewById(R.id.acceptBtn);
		TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);

		// dismiss.setText("Dismiss");
		dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				accept.setBackgroundResource(R.drawable.bg_check);
				accept.refreshDrawableState();
			}
		});

		name.setText(hitchhiker.getUsername());
		name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, ProfileActivity.class);
				context.startActivity(intent);
			}
		});

		// accept.setText("Accept");
//		final Button accept = (Button) entryView.findViewById(R.id.acceptBtn);
		accept.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				accept.setBackgroundColor(R.drawable.waiting);
				accept.setBackgroundResource(R.drawable.bg_waiting);
				accept.refreshDrawableState();
			}
		});

		return entryView;
	}

}
