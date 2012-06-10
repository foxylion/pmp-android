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
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import de.unistuttgart.ipvs.pmp.Log;
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.TripSearchResultAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.utils.ResourceGroupReadyActivity;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripSearchResult;

public class TripSearchActivity extends ResourceGroupReadyActivity implements OnClickListener,
        OnItemClickListener {
    
    private TextView txtTolerance;
    private RatingBar rbRating;
    private SlidingDrawer slider;
    private ListView listResult;
    private TripSearchResultAdapter adapter;
    private ArrayList<TripSearchResult> data;
    private boolean isTripSearch = false;
    private int tripId;
    
    
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
        if (i.hasExtra("tripId")) {
            tripId = i.getIntExtra("tripId", -1);
            txt.setText("Hitchhiker search");
            btnOpenMap.setText("Show live hikers on map");
            //            txt.setText((new Formatter()).format((String) getText(R.string.Trip_search_title), "Trip")
            //                    .toString());
            //            btnOpenMap.setText((new Formatter()).format((String) getText(R.string.Show_live_trips), "trips")
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
        
        SeekBar seeker = (SeekBar) findViewById(R.id.seekBarTolerance);
        seeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
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
        seeker = (SeekBar) findViewById(R.id.seekBarRating);
        seeker.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
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
        if (this.data == null) {
            
            // TODO load real data here
            
            this.data = new ArrayList<TripSearchResult>(10);
            Calendar d = Calendar.getInstance();
            String[] names = "eyepatch1 twittered facebook google yahoo hiker42 netter999 eight nine ten jdskl djwe fjks fjlk"
                    .split(" ");
            for (int i = 0; i < 10; i++) {
                Calendar d1 = Calendar.getInstance();
                d1.setTimeInMillis(d.getTimeInMillis());
                this.data.add(new TripSearchResult(32, "Stuttgart", "Berlin", d1, names[i], 10,
                        (float) (Math.random() * 5f),
                        (int) Math.floor(Math.random() * 10)));
                d1.add(Calendar.HOUR, (int) Math.floor(Math.random() * 24));
                d.setTimeInMillis(d1.getTimeInMillis());
            }
        }
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
