package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.ViewSwitcher;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactMessage;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactUser;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripOverview;

public class TripDetailActivity extends Activity implements OnClickListener {
    
    private TextView btnOverview;
    private TextView btnAllMessages;
    private ViewSwitcher switcher;
    private ListView listAllMessages;
    private ListView listNewMessages;
    private long tripId;
    private TripOverview tripInfo;
    private TextAppearanceSpan menuActive;
    private SpannableString txtOverview;
    private SpannableString txtAllMessages;
    private View bottomMenu;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        
        switcher = (ViewSwitcher) findViewById(R.id.trip_detail_view_switcher);
        
        menuActive = new TextAppearanceSpan(this, R.style.ActionBarItemActive);
        btnOverview = (TextView) findViewById(R.id.trip_detail_button_overview);
        txtOverview = new SpannableString(btnOverview.getText());
        txtOverview.setSpan(menuActive, 0, txtOverview.length(), 0);
        btnOverview.setText(txtOverview, BufferType.SPANNABLE);
        
        btnAllMessages = (TextView) findViewById(R.id.trip_detail_button_all_messages);
        txtAllMessages = new SpannableString(btnAllMessages.getText());
        btnAllMessages.setText(txtAllMessages, BufferType.SPANNABLE);
        
        btnAllMessages.setOnClickListener(this);
        btnOverview.setOnClickListener(this);
        
        bottomMenu = findViewById(R.id.trip_detail_bottom_menu);
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
                    txtOverview.setSpan(menuActive, 0, txtOverview.length(), 0);
                    txtAllMessages.removeSpan(menuActive);
                    btnAllMessages.setText(txtAllMessages, BufferType.SPANNABLE);
                    btnOverview.setText(txtOverview, BufferType.SPANNABLE);
                    bottomMenu.setVisibility(View.VISIBLE);
                }
                break;
            
            case R.id.trip_detail_button_all_messages:
                if (switcher.getDisplayedChild() == 0) {
                    
                    initAllMessages();
                    
                    switcher.setInAnimation(TripDetailActivity.this, R.anim.in_next);
                    switcher.setOutAnimation(TripDetailActivity.this, R.anim.out_next);
                    switcher.showNext();
                    txtAllMessages.setSpan(menuActive, 0, txtAllMessages.length(), 0);
                    txtOverview.removeSpan(menuActive);
                    btnAllMessages.setText(txtAllMessages, BufferType.SPANNABLE);
                    btnOverview.setText(txtOverview, BufferType.SPANNABLE);
                    bottomMenu.setVisibility(View.GONE);
                }
                break;
        }
    }
    
    
    private void initAllMessages() {
        if (listAllMessages == null) {
            listAllMessages = (ListView) findViewById(R.id.trip_detail_all_messages);
        }
        
        listAllMessages.setAdapter(new ArrayAdapter<CompactMessage>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, tripInfo.messages));
        
        AutoCompleteTextView a = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        a.setAdapter(new ArrayAdapter<CompactMessage>(this, android.R.layout.simple_dropdown_item_1line,
                tripInfo.messages) {
        });
    }
    
    
    private void prepareViews() {
        
        if (tripInfo == null) {
            // TODO Get actual data
            ArrayList<CompactUser> passengers = new ArrayList<CompactUser>(3);
            passengers.add(new CompactUser(1, "Passenger1"));
            passengers.add(new CompactUser(2, "Passenger2"));
            passengers.add(new CompactUser(3, "Passenger3"));
            ArrayList<CompactMessage> msg = new ArrayList<CompactMessage>(3);
            msg.add(new CompactMessage(0, passengers.get(0), passengers.get(1), true, "Hello"));
            msg.add(new CompactMessage(2, passengers.get(1), passengers.get(2), false, "Hello"));
            msg.add(new CompactMessage(3, passengers.get(2), passengers.get(1), true, "Hello"));
            tripInfo = new TripOverview(10, "Berlin", ";Stuttgart;Frankfurt;Leipzig;Dortmund;Bremen;", passengers,
                    GregorianCalendar.getInstance().getTime(), msg);
        }
        
        // Set destination
        TextView txt = (TextView) findViewById(R.id.trip_detail_destination);
        txt.setText(tripInfo.destination);
        
        // Set time
        txt = (TextView) findViewById(R.id.trip_detail_time);
        txt.setText(SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT).format(
                tripInfo.startTime));
        
        // Set stop-overs
        TextAppearanceSpan captionSpan = new TextAppearanceSpan(this, R.style.CaptionSpan);
        
        String stopoverLabel = (String) getText(R.string.tripDetails_stopovers);
        SpannableStringBuilder builder = new SpannableStringBuilder(stopoverLabel);
        builder.append(tripInfo.stopovers);
        builder.setSpan(captionSpan, 0, stopoverLabel.length(), 0);
        
        txt = (TextView) findViewById(R.id.trip_detail_stop_over);
        txt.setText(builder, BufferType.SPANNABLE);
        
        // Set passenger list
        String passengerLabel = (String) getText(R.string.tripDetails_passengers);
        builder = new SpannableStringBuilder(passengerLabel);
        
        int start;
        int end = builder.length();
        builder.setSpan(captionSpan, 0, end, 0);
        
        for (CompactUser p : tripInfo.passengers) {
            start = builder.length();
            builder.append(p.name);
            end = builder.length();
            builder.setSpan(new PassengerSpan(p.id), start, end, 0);
            builder.append(", ");
        }
        if (tripInfo.passengers.size() > 0)
            builder.delete(end, end + 1);
        
        txt = (TextView) findViewById(R.id.trip_detail_passengers);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(builder, BufferType.SPANNABLE);
        
        // Set requests and new messages
        listNewMessages = (ListView) findViewById(R.id.trip_detail_list_new_messages);
        listNewMessages.setAdapter(new ArrayAdapter<CompactMessage>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, tripInfo.messages));
        listNewMessages.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> list, View item, int pos, long arg3) {
                try {
                    Intent intent = new Intent(TripDetailActivity.this, MessageActivity.class);
                    intent.putExtra("tripId", TripDetailActivity.this.tripId);
                    intent.putExtra("userId", list.getAdapter().getItemId(pos));
                    intent.putExtra("userName", list.getAdapter().getItem(pos).toString());
                    TripDetailActivity.this.startActivity(intent);
                } catch (Exception e) {
                    // do nothing
                }
            }
        });
        listNewMessages.setOnItemLongClickListener(null);
        registerForContextMenu(listNewMessages);
    }
    
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        return super.onContextItemSelected(item);
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.message_contextmenu, menu);
    }
    
    private class PassengerSpan extends ClickableSpan {
        
        private int id;
        
        
        public PassengerSpan(int id) {
            super();
            this.id = id;
        }
        
        
        @Override
        public void onClick(View widget) {
            widget.getParent().clearChildFocus(widget);
            Intent intent = new Intent(TripDetailActivity.this, MyTripActivity.class);
            intent.putExtra("passengerId", id);
            intent.putExtra("tripId", TripDetailActivity.this.tripId);
            TripDetailActivity.this.startActivity(intent);
            // TODO PassengerAction!
        }
        
    }
}
