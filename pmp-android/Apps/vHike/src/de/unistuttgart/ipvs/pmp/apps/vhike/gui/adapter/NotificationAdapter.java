package de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter;

import java.util.List;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.ProfileActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.MapModel;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Handles list elements where drivers or passengers are added/removed through
 * finding/accepting/denying hitchhikers
 * 
 * mWhichHitcher indicates which list is to handle. 0 handles passengers list 1
 * handles drivers list
 * 
 * @author andres
 * 
 */
public class NotificationAdapter extends BaseAdapter {

	private Context context;
	private Controller ctrl;
	private List<Profile> hitchhikers;
	private Profile hitchhiker;
	private int mWhichHitcher;

	public NotificationAdapter(Context context, List<Profile> hitchhikers,
			int whichHitcher) {
		this.context = context;
		this.hitchhikers = hitchhikers;
		ctrl = new Controller();
		mWhichHitcher = whichHitcher;
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
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		hitchhiker = hitchhikers.get(position);

		/* load the layout from the xml file */
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout entryView = (LinearLayout) inflater.inflate(
				R.layout.hitchhiker_list, null);

		Button dismiss = (Button) entryView.findViewById(R.id.dismissBtn);
		RatingBar noti_rb = (RatingBar) entryView
				.findViewById(R.id.notification_ratingbar);
		TextView name = (TextView) entryView.findViewById(R.id.TextView_Name);
		final Button accept_invite = (Button) entryView
				.findViewById(R.id.acceptBtn);

		dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mWhichHitcher == 0) {
					// if (all seats taken) -> remove all
					// else -> JUST ONE
					MapModel.getInstance().getHitchPassengers()
							.remove(position);
					notifyDataSetChanged();
				} else {
					switch (ctrl.handleOffer(Model.getInstance().getSid(), 1,
							false)) {
					case Constants.STATUS_HANDLED:
						Toast.makeText(context, "HANDLED: DENY",
								Toast.LENGTH_SHORT).show();
						// remove driver from list if denied
						MapModel.getInstance().getHitchDrivers()
								.remove(position);
						notifyDataSetChanged();
						break;
					case Constants.STATUS_INVALID_OFFER:
						Toast.makeText(context, "INVALID OFFER",
								Toast.LENGTH_SHORT).show();
						break;
					case Constants.STATUS_INVALID_USER:
						Toast.makeText(context, "INVALID_USER",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}
				accept_invite.setBackgroundResource(R.drawable.bg_check);
				accept_invite.refreshDrawableState();
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

		noti_rb.setRating((float) hitchhiker.getRating_num());

		accept_invite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mWhichHitcher == 0) {
					switch (ctrl.sendOffer(Model.getInstance().getSid(), Model
							.getInstance().getTripId(), Model.getInstance()
							.getQueryId(), hitchhiker.getUsername()
							+ ": Need a ride?")) {
					case Constants.STATUS_SENT:
						Toast.makeText(context, "STATUS_SENT",
								Toast.LENGTH_SHORT).show();

						accept_invite
								.setBackgroundResource(R.drawable.bg_waiting);
						accept_invite.refreshDrawableState();
						notifyDataSetChanged();
						break;
					case Constants.STATUS_INVALID_TRIP:
						Toast.makeText(context, "STATUS_INVALID_TRIP",
								Toast.LENGTH_SHORT).show();
						break;
					case Constants.STATUS_INVALID_QUERY:
						Toast.makeText(context, "INVALID_QUERY",
								Toast.LENGTH_SHORT).show();
						break;
					case Constants.STATUS_ALREADY_SENT:
						Toast.makeText(context, "ALREADY SENT",
								Toast.LENGTH_SHORT);
						break;
					}
				} else {
					int offerID = 1;
					switch (ctrl.handleOffer(Model.getInstance().getSid(),
							offerID, true)) {
					case Constants.STATUS_HANDLED:
						Toast.makeText(context, "HANDLED: ACCEPT",
								Toast.LENGTH_SHORT).show();
						// Entfernen
						notifyDataSetChanged();
						break;
					case Constants.STATUS_INVALID_OFFER:
						Toast.makeText(context, "INVALID_OFFER",
								Toast.LENGTH_SHORT).show();
						break;
					case Constants.STATUS_INVALID_USER:
						Toast.makeText(context, "INVALID_USER",
								Toast.LENGTH_SHORT).show();
						break;
					}
				}

			}
		});

		return entryView;
	}

}
