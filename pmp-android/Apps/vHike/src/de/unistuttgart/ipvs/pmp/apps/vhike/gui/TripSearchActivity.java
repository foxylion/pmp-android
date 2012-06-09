package de.unistuttgart.ipvs.pmp.apps.vhike.gui;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
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
import de.unistuttgart.ipvs.pmp.apps.vhike.R;
import de.unistuttgart.ipvs.pmp.apps.vhike.ctrl.vHikeService;
import de.unistuttgart.ipvs.pmp.apps.vhike.gui.adapter.TripSearchResultAdapter;
import de.unistuttgart.ipvs.pmp.apps.vhike.model.TripSearchResult;

public class TripSearchActivity extends Activity implements OnClickListener, OnItemClickListener {
    
    private TextView txtTolerance;
    private RatingBar rbRating;
    private SlidingDrawer slider;
    private ListView listResult;
    private TripSearchResultAdapter adapter;
    private ArrayList<TripSearchResult> data;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_search);
        
        this.slider = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        this.txtTolerance = (TextView) findViewById(R.id.txtTolerance);
        this.rbRating = (RatingBar) findViewById(R.id.ratingBar);
        this.listResult = (ListView) findViewById(R.id.listResult);
        
        loadData();
        this.adapter = new TripSearchResultAdapter(this, R.layout.list_item_search_result_trip, this.data);
        this.listResult.setAdapter(this.adapter);
        
        this.listResult.setOnItemClickListener(this);
        
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
        this.slider.animateOpen();
    }
    
    
    private void loadData() {
        if (this.data == null) {
            this.data = new ArrayList<TripSearchResult>(10);
            Calendar d = Calendar.getInstance();
            for (int i = 0; i < 10; i++) {
                Calendar d1 = Calendar.getInstance();
                d1.setTimeInMillis(d.getTimeInMillis());
                this.data.add(new TripSearchResult(32, "Stuttgart", "Berlin", d1, "Ben", 10, (float) (Math
                        .random() * 5f),
                        (int) Math.floor(Math.random() * 10)));
                d1.add(Calendar.HOUR, (int) Math.floor(Math.random() * 24));
                d.setTimeInMillis(d1.getTimeInMillis());
            }
        }
    }
    
    
    @Override
    protected void onResume() {
        super.onResume();
        
        vHikeService.getInstance().updateServiceFeatures();
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
        System.out.println(String.format("clicked %d - %d", position, id));
    }
}
