/*
 * Copyright 2012 pmp-android development team
 * Project: vHikeApp
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.Formatter;

import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.os.RemoteException;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.ViewSwitcher;
import de.unistuttgart.ipvs.pmp.apps.vhike.Constants;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.exception.QueryException;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.MessageAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.FriendlyDateFormatter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactMessage;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.CompactUser;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripOverview;

public class TripDetailActivity extends ResourceGroupReadyActivity implements OnClickListener {
    
    private TextView btnOverview;
    private TextView btnAllMessages;
    private ViewSwitcher switcher;
    private ListView listAllMessages;
    private ListView listNewMessages;
    private int tripId;
    private TripOverview tripInfo;
    private TextAppearanceSpan menuActive;
    private SpannableString txtOverview;
    private SpannableString txtAllMessages;
    private View bottomMenu;
    private Button btnSearch;
    private Controller ctrl;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);
        
        this.switcher = (ViewSwitcher) findViewById(R.id.trip_detail_view_switcher);
        
        this.menuActive = new TextAppearanceSpan(this, R.style.ActionBarItemActive);
        this.btnOverview = (TextView) findViewById(R.id.trip_detail_button_overview);
        this.txtOverview = new SpannableString(this.btnOverview.getText());
        this.txtOverview.setSpan(this.menuActive, 0, this.txtOverview.length(), 0);
        this.btnOverview.setText(this.txtOverview, BufferType.SPANNABLE);
        
        this.btnAllMessages = (TextView) findViewById(R.id.trip_detail_button_all_messages);
        this.txtAllMessages = new SpannableString(this.btnAllMessages.getText());
        this.btnAllMessages.setText(this.txtAllMessages, BufferType.SPANNABLE);
        
        this.btnSearch = (Button) findViewById(R.id.btnSearch);
        
        this.btnAllMessages.setOnClickListener(this);
        this.btnOverview.setOnClickListener(this);
        this.btnSearch.setOnClickListener(this);
        
        this.bottomMenu = findViewById(R.id.trip_detail_bottom_menu);
        
        tripId = getIntent().getIntExtra("tripId", 0);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (rgvHike != null) {
            vHikeService.getInstance().updateServiceFeatures();
            if (this.ctrl == null) {
                this.ctrl = new Controller(rgvHike);
            }
            prepareViews();
        } else
            getvHikeRG(this);
    }
    
    
    @Override
    public void onResourceGroupReady(IInterface resourceGroup, int resourceGroupId) {
        super.onResourceGroupReady(resourceGroup, resourceGroupId);
        if (resourceGroupId == Constants.RG_VHIKE_WEBSERVICE) {
            
            if (this.ctrl == null) {
                this.ctrl = new Controller(rgvHike);
            }
            prepareViews();
        }
    }
    
    
    @Override
    public void onClick(View v) {
        if (this.switcher == null) {
            return;
        }
        
        switch (v.getId()) {
        
            case R.id.trip_detail_button_overview:
                if (this.switcher.getDisplayedChild() == 1) {
                    this.switcher.setInAnimation(TripDetailActivity.this, R.anim.in_back);
                    this.switcher.setOutAnimation(TripDetailActivity.this, R.anim.out_back);
                    this.switcher.showPrevious();
                    this.txtOverview.setSpan(this.menuActive, 0, this.txtOverview.length(), 0);
                    this.txtAllMessages.removeSpan(this.menuActive);
                    this.btnAllMessages.setText(this.txtAllMessages, BufferType.SPANNABLE);
                    this.btnOverview.setText(this.txtOverview, BufferType.SPANNABLE);
                    this.bottomMenu.setVisibility(View.VISIBLE);
                }
                break;
            
            case R.id.trip_detail_button_all_messages:
                if (this.switcher.getDisplayedChild() == 0) {
                    
                    initAllMessages();
                    
                    this.switcher.setInAnimation(TripDetailActivity.this, R.anim.in_next);
                    this.switcher.setOutAnimation(TripDetailActivity.this, R.anim.out_next);
                    this.switcher.showNext();
                    this.txtAllMessages.setSpan(this.menuActive, 0, this.txtAllMessages.length(), 0);
                    this.txtOverview.removeSpan(this.menuActive);
                    this.btnAllMessages.setText(this.txtAllMessages, BufferType.SPANNABLE);
                    this.btnOverview.setText(this.txtOverview, BufferType.SPANNABLE);
                    this.bottomMenu.setVisibility(View.GONE);
                }
                break;
            case R.id.btnSearch:
                Intent intent = new Intent(this, TripSearchActivity.class);
                intent.putExtra("tripId", tripId);
                this.startActivity(intent);
                break;
        }
    }
    
    
    private void initAllMessages() {
        if (this.listAllMessages == null) {
            this.listAllMessages = (ListView) findViewById(R.id.trip_detail_all_messages);
        }
        
        this.listAllMessages.setAdapter(new MessageAdapter(this, this.tripInfo.messages));
        
        AutoCompleteTextView a = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        a.setAdapter(new ArrayAdapter<CompactMessage>(this, android.R.layout.simple_dropdown_item_1line,
                this.tripInfo.messages) {
        });
    }
    
    
    private void prepareViews() {
        
        if (this.tripInfo == null) {
            try {
                if (rgvHike == null) {
                    getvHikeRG(this);
                    return;
                }
                if (ctrl == null)
                    ctrl = new Controller(rgvHike);
                tripInfo = ctrl.getTripOverview(Model.getInstance().getSid(), tripId);
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (QueryException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            //            ArrayList<CompactUser> passengers = new ArrayList<CompactUser>(3);
            //            passengers.add(new CompactUser(1, "Passenger1", 0));
            //            passengers.add(new CompactUser(2, "Passenger2", 5));
            //            passengers.add(new CompactUser(3, "Passenger3", 3));
            //            ArrayList<CompactMessage> msg = new ArrayList<CompactMessage>(3);
            //            msg.add(new CompactMessage(0, passengers.get(0), passengers.get(1), true, "Hello"));
            //            msg.add(new CompactMessage(2, passengers.get(1), passengers.get(2), false, "Hello 2"));
            //            msg.add(new CompactMessage(3, passengers.get(2), passengers.get(1), true, "Hello 32"));
            //            this.tripInfo = new TripOverview(10, "Berlin", ";Stuttgart;Frankfurt;Leipzig;Dortmund;Bremen;",
            //                    passengers,
            //                    Calendar.getInstance(), 3, msg);
        }
        
        // TODO Still null?
        
        // Set destination
        TextView txt = (TextView) findViewById(R.id.trip_detail_destination);
        txt.setText(this.tripInfo.destination);
        
        // Set time
        txt = (TextView) findViewById(R.id.trip_detail_time);
        txt.setText((new FriendlyDateFormatter(this)).format(this.tripInfo.startTime));
        
        // Set stop-overs
        TextAppearanceSpan captionSpan = new TextAppearanceSpan(this, R.style.CaptionSpan);
        
        String stopoverLabel = (String) getText(R.string.tripDetails_stopovers);
        SpannableStringBuilder builder = new SpannableStringBuilder(stopoverLabel);
        builder.append(this.tripInfo.stopovers);
        builder.setSpan(captionSpan, 0, stopoverLabel.length(), 0);
        
        txt = (TextView) findViewById(R.id.trip_detail_stop_over);
        txt.setText(builder, BufferType.SPANNABLE);
        
        // Set free seats
        txt = (TextView) findViewById(R.id.trip_detail_free_seats);
        txt.setText(String.valueOf(this.tripInfo.numberOfAvailableSeat));
        
        // TODO hiker or driver?
        String text = (new Formatter())
                .format(getText(R.string.tripDetails_search).toString(), "hitchhikers")
                .toString();
        this.btnSearch.setText(text);
        this.btnSearch.setEnabled(this.tripInfo.numberOfAvailableSeat > 0 ? true : false);
        
        // Set passenger list
        String passengerLabel = (String) getText(R.string.tripDetails_passengers);
        builder = new SpannableStringBuilder(passengerLabel);
        
        int start;
        int end = builder.length();
        builder.setSpan(captionSpan, 0, end, 0);
        
        for (CompactUser p : this.tripInfo.passengers) {
            start = builder.length();
            builder.append(p.name);
            end = builder.length();
            builder.setSpan(new PassengerSpan(p.id, p.name), start, end, 0);
            builder.append(", ");
        }
        if (this.tripInfo.passengers.size() > 0) {
            builder.delete(end, end + 1);
        }
        
        txt = (TextView) findViewById(R.id.trip_detail_passengers);
        txt.setMovementMethod(LinkMovementMethod.getInstance());
        txt.setText(builder, BufferType.SPANNABLE);
        
        // Set requests and new messages
        this.listNewMessages = (ListView) findViewById(R.id.trip_detail_list_new_messages);
        this.listNewMessages.setAdapter(new MessageAdapter(this, this.tripInfo.messages));
        this.listNewMessages.setOnItemClickListener(new OnItemClickListener() {
            
            @Override
            public void onItemClick(AdapterView<?> list, View view, int position, long id) {
                try {
                    Intent intent = new Intent(TripDetailActivity.this, MessageActivity.class);
                    intent.putExtra("tripId", TripDetailActivity.this.tripId);
                    intent.putExtra("userId", list.getAdapter().getItemId(position));
                    intent.putExtra("userName", list.getAdapter().getItem(position).toString());
                    if (((CompactMessage) list.getAdapter().getItem(position)).isOffer)
                        intent.putExtra("isOffer", true);
                    TripDetailActivity.this.startActivity(intent);
                } catch (Exception e) {
                    // do nothing
                }
            }
        });
        this.listNewMessages.setOnItemLongClickListener(null);
    }
    
    private class PassengerSpan extends ClickableSpan {
        
        private int id;
        private String name;
        
        
        public PassengerSpan(int id, String name) {
            super();
            this.id = id;
            this.name = name;
        }
        
        
        @Override
        public void onClick(View widget) {
            widget.getParent().clearChildFocus(widget);
            Intent intent = new Intent(TripDetailActivity.this, MessageActivity.class);
            intent.putExtra("passengerId", this.id);
            intent.putExtra("username", this.name);
            intent.putExtra("tripId", TripDetailActivity.this.tripId);
            startActivity(intent);
        }
    }
}
