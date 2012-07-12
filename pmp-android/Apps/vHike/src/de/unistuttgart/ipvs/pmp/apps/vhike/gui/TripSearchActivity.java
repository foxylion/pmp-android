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

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.Controller;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.TripSearchResultAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.Model;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripSearchResult;

public class TripSearchActivity extends ResourceGroupReadyActivity implements OnClickListener,
        OnItemClickListener {
    
    private TextView txtTolerance;
    private RatingBar rbRating;
    private SlidingDrawer slider;
    private ListView listResult;
    private EditText etDestination;
    private EditText etDeparture;
    private EditText etUsername;
    private DatePicker datepicker;
    private SeekBar seekbarTolerance;
    private SeekBar seekbarRating;
    
    private TripSearchResultAdapter adapter;
    private ArrayList<TripSearchResult> data;
    private boolean isTripSearch = false;
    private int tripId;
    private String destination;
    private String departure;
    private String username;
    private int seatNo;
    private Calendar date;
    private int tolerance = 0;
    private float minRating = 0f;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        
        this.slider = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        this.txtTolerance = (TextView) findViewById(R.id.txtTolerance);
        this.rbRating = (RatingBar) findViewById(R.id.ratingBar);
        this.listResult = (ListView) findViewById(R.id.listResult);
        
        Intent i = getIntent();
        TextView txt = (TextView) findViewById(R.id.title);
        Button btnOpenMap = (Button) findViewById(R.id.btnOpenMap);
        destination = i.hasExtra("destination") ? i.getStringExtra("destination") : "";
        seatNo = i.getIntExtra("seat", 0);
        
        if (i.hasExtra("dateInMilli")) {
            date = Calendar.getInstance();
            date.setTimeInMillis(i.getLongExtra("dateInMilli", System.currentTimeMillis()));
        }
        
        // Prepare search filter
        etDestination = (EditText) slider.findViewById(R.id.editTextDestination);
        etDestination.setText(destination);
        etDeparture = (EditText) slider.findViewById(R.id.editTextDeparture);
        etUsername = (EditText) slider.findViewById(R.id.editTextUsername);
        datepicker = (DatePicker) slider.findViewById(R.id.datePicker);
        if (date != null)
            datepicker.updateDate(
                    date.get(Calendar.YEAR), date.get(Calendar.MONDAY), date.get(Calendar.DAY_OF_MONTH));
        
        if (i.hasExtra("tripId")) {
            tripId = i.getIntExtra("tripId", -1);
            txt.setText("Hitchhiker search");
            btnOpenMap.setText("Show live hikers on map");
            //            txt.setText(String.format((String) getText(R.string.Trip_search_title), "Trip")
            //                    .toString());
            //            btnOpenMap.setText(String.format((String) getText(R.string.Show_live_trips), "trips")
            //                    .toString());
        } else {
            isTripSearch = true;
            txt.setText("Trip search");
            btnOpenMap.setText("Show live drivers on map");
            //            txt.setText((new Formatter()).format((String) getText(R.string.Trip_search_title), "Ride")
            //                    .toString());
            //            btnOpenMap.setText((new Formatter()).format((String) getText(R.string.Show_live_trips), "rides")
            //                    .toString());
            this.slider.animateOpen();
        }
        
        seekbarTolerance = (SeekBar) findViewById(R.id.seekBarTolerance);
        seekbarTolerance.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                
            }
            
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TripSearchActivity.this.txtTolerance.setText(String.valueOf(progress));
            }
        });
        seekbarRating = (SeekBar) findViewById(R.id.seekBarRating);
        seekbarRating.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TripSearchActivity.this.rbRating.setRating(progress / 2f);
                
            }
        });
        
        ((Button) findViewById(R.id.btnFilter)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnOpenMap)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnSearch)).setOnClickListener(this);
        listResult.setOnItemClickListener(this);
    }
    
    
    private void loadData() {
        if (getvHikeRG(this) == null)
            return;
        
        // Load data from server
        try {
            Controller ctrl = new Controller(rgvHike);
            
            destination = etDestination.getText().toString().trim();
            departure = etDeparture.getText().toString().trim();
            username = etUsername.getText().toString().trim().replaceAll(" ", "");
            tolerance = seekbarTolerance.getProgress();
            minRating = (float) seekbarRating.getProgress() / (float) seekbarRating.getMax() * 5f;
            date.clear();
            date.set(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth());
            Calendar date1 = Calendar.getInstance();
            Calendar date2 = Calendar.getInstance();
            date1.setTimeInMillis(date.getTimeInMillis() - tolerance * 86400000);
            date2.setTimeInMillis(date.getTimeInMillis() + tolerance * 86400000 + 86399000); // @ 23:59:59
            
            this.data = ctrl.searchTrip(Model.getInstance().getSid(), departure, destination, seatNo,
                    date1, date2, minRating, username, isTripSearch);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        
        if (data == null)
            data = new ArrayList<TripSearchResult>(0);
        else if (data.size() == 0)
            Toast.makeText(this, isTripSearch ? "No trip found" : "No hiker found", Toast.LENGTH_LONG).show();
        this.adapter = new TripSearchResultAdapter(
                this,
                isTripSearch ? R.layout.list_item_search_result_trip : R.layout.list_item_search_result_hiker,
                this.data);
        this.listResult.setAdapter(this.adapter);
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (rgvHike == null)
            getvHikeRG(this);
        else {
            vHikeService.getInstance().updateServiceFeatures();
            
            loadData();
        }
    }
    
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOpenMap:
                break;
            case R.id.btnFilter:
                this.slider.animateClose();
                break;
            case R.id.btnSearch:
                this.slider.animateClose();
                loadData();
                break;
        }
    }
    
    
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(this, String.format("clicked %d - %d", position, id));
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }
    
}
