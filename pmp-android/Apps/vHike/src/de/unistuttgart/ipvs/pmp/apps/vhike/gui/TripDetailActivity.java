package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.ViewSwitcher;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripOverview;

public class TripDetailActivity extends Activity implements OnClickListener {
    
    private Button btnOverview;
    private Button btnAllMessages;
    private ViewSwitcher switcher;
    private ListView listAllMessages;
    private int tripId;
    private TripOverview tripInfo;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        
        switcher = (ViewSwitcher) findViewById(R.id.trip_detail_view_switcher);
        btnOverview = (Button) findViewById(R.id.trip_detail_button_overview);
        btnAllMessages = (Button) findViewById(R.id.trip_detail_button_all_messages);
        
        btnAllMessages.setOnClickListener(this);
        btnOverview.setOnClickListener(this);
        
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        //        if (getIntent() != null && getIntent().getExtras() != null)
        //            tripId = getIntent().getExtras().getInt("tripId", 0);
        
        prepareViews();
    }
    
    
    @Override
    public void onClick(View v) {
        if (switcher == null)
            return;
        switch (v.getId()) {
        
            case R.id.trip_detail_button_overview:
                if (switcher.getDisplayedChild() == 1) {
                    switcher.setInAnimation(TripDetailActivity.this, R.anim.in_back);
                    switcher.setOutAnimation(TripDetailActivity.this, R.anim.out_back);
                    switcher.showPrevious();
                }
                break;
            
            case R.id.trip_detail_button_all_messages:
                if (switcher.getDisplayedChild() == 0) {
                    
                    initAllMessages();
                    
                    switcher.setInAnimation(TripDetailActivity.this, R.anim.in_next);
                    switcher.setOutAnimation(TripDetailActivity.this, R.anim.out_next);
                    switcher.showNext();
                }
                break;
        }
    }
    
    
    private void initAllMessages() {
        if (listAllMessages == null) {
            listAllMessages = (ListView) findViewById(R.id.trip_detail_all_messages);
        }
        
        listAllMessages.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, tripInfo.persons));
    }
    
    
    private void prepareViews() {
        
        if (tripInfo == null) {
            // TODO Get data
            ArrayList<String> p = new ArrayList<String>(3);
            p.add("Dang");
            p.add("Andre");
            p.add("Alex");
            tripInfo = new TripOverview(10, "Berlin",
                    "Frankfurt;Blah        ; OK;Hello;Heladssdos dsods; Münschen; Aloe vera", GregorianCalendar
                            .getInstance().getTime(), p);
        }
        
        // Set destination
        TextView txt = (TextView) findViewById(R.id.trip_detail_destination);
        txt.setText(tripInfo.destination);
        
        // Set time
        txt = (TextView) findViewById(R.id.trip_detail_time);
        txt.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(
                tripInfo.startTime));
        
        // Set stop overs
        txt = (TextView) findViewById(R.id.trip_detail_stop_over);
        String stopovertext = (String) getText(R.string.tripDetails_stopovers);
        SpannableStringBuilder builder = new SpannableStringBuilder(stopovertext);
        builder.append(tripInfo.stopovers);
        builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.emirates_red)), 0,
                stopovertext.length(), 0);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, stopovertext.length(), 0);
        
        txt.setText(builder, BufferType.SPANNABLE);
        
        // Set requests and new messages
        ListView l = (ListView) findViewById(R.id.trip_detail_overview_list);
        l.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,
                tripInfo.persons));
    }
}
