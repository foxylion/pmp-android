package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;

import de.unistuttgart.ipvs.pmp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryActivityAdapter extends BaseAdapter {

	private ArrayList<AWName> mData = new ArrayList<AWName>();

	private final LayoutInflater mLayoutInflater;

	public HistoryActivityAdapter(Context pContext, ArrayList<AWName> pData) {
		mData = pData;

		mLayoutInflater = (LayoutInflater) pContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int pPosition) {
		return mData.get(pPosition);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int pPosition, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.activity_history,
					null);
		}

		((TextView) convertView.findViewById(R.id.row_city))
				.setText(((AWName) getItem(pPosition)).getCity());
		((TextView) convertView.findViewById(R.id.row_date))
				.setText(((AWName) getItem(pPosition)).getDate());

		return convertView;
	}

}
