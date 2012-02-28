package de.unistuttgart.ipvs.pmp.apps.infoapp.panels;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Translation extends Fragment {
	public static Location newInstance(String title) {
		Location f = new Location();
		Bundle bundle = new Bundle();
		bundle.putString("title", title);
		f.setArguments(bundle);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		TextView textView = new TextView(getActivity());
		textView.setText(getArguments().getString("title"));
		return textView;
	}
}
