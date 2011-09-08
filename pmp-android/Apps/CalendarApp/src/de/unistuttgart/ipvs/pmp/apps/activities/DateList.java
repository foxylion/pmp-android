package de.unistuttgart.ipvs.pmp.apps.activities;

import de.unistuttgart.ipvs.pmp.apps.R;
import de.unistuttgart.ipvs.pmp.apps.model.Date;
import de.unistuttgart.ipvs.pmp.apps.model.Model;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DateList extends ListActivity {
    
    private static ArrayAdapter<Date> ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	
	 ad =new ArrayAdapter<Date>(this, R.layout.list_item,
		Model.getInstance().getDateList());
	
	setListAdapter(ad);

	ListView lv = getListView();
	lv.setTextFilterEnabled(true);
	final Intent intent = new Intent(this, ChangeDateActivity.class);

	lv.setOnItemClickListener(new OnItemClickListener() {
	    public void onItemClick(AdapterView<?> parent, View view,
		    int position, long id) {
		intent.putExtra("id", position);
		startActivityForResult(intent, 0);
	    }
	});

    }
    
    /**
     * Called when the ChangeDataActivity finishes to refresh the list
     */
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
	ad.notifyDataSetChanged();
    }

    
   
}
