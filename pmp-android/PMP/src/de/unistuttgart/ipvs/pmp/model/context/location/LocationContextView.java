package de.unistuttgart.ipvs.pmp.model.context.location;

import java.util.ArrayList;
import java.util.List;

import android.R.attr;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;

import de.unistuttgart.ipvs.pmp.R;
import de.unistuttgart.ipvs.pmp.gui.util.GUITools;
import de.unistuttgart.ipvs.pmp.model.context.IContextView;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

/**
 * View component for the {@link LocationContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class LocationContextView extends LinearLayout implements IContextView {
    
    public static final String LATITUDE_EXTRA = "lat";
    public static final String LONGITUDE_EXTRA = "lon";
    
    class ExpandableGeoPointList extends BaseExpandableListAdapter {
        
        private final List<LocationContextGeoPoint> data;
        
        
        public ExpandableGeoPointList() {
            this.data = new ArrayList<LocationContextGeoPoint>();
        }
        
        
        public void update(List<LocationContextGeoPoint> data) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetInvalidated();
        }
        
        
        @Override
        public int getGroupCount() {
            return 1;
        }
        
        
        @Override
        public int getChildrenCount(int groupPosition) {
            return this.data.size();
        }
        
        
        @Override
        public Object getGroup(int groupPosition) {
            return getContext().getString(R.string.contexts_location_coordinates);
        }
        
        
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this.data.get(childPosition);
        }
        
        
        @Override
        public long getGroupId(int groupPosition) {
            return 1;
        }
        
        
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        
        
        @Override
        public boolean hasStableIds() {
            return false;
        }
        
        
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            TextView tv;
            if ((convertView != null) && (convertView instanceof TextView)) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(getContext());
            }
            tv.setTextAppearance(getContext(), attr.textAppearanceLarge);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText(getGroup(groupPosition).toString());
            
            return tv;
        }
        
        
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                ViewGroup parent) {
            TextView tv;
            if ((convertView != null) && (convertView instanceof TextView)) {
                tv = (TextView) convertView;
            } else {
                tv = new TextView(getContext());
            }
            tv.setTextAppearance(getContext(), attr.textAppearance);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setText(getChild(groupPosition, childPosition).toString());
            
            return tv;
        }
        
        
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
        
    }
    
    /**
     * Value currently in the view
     */
    protected LocationContextCondition value;
    
    /**
     * List of the coordinates
     */
    private ExpandableListView points;
    protected ExpandableGeoPointList pointsList;
    private Button changeBtn;
    
    /**
     * Thread reading chances from the {@link LocationContextMapView}.
     */
    protected Thread readerThread = new Thread() {
        
        @Override
        public void run() {
            while (!isInterrupted()) {
                synchronized (LocationContextMapView.GEO_POINTS) {
                    if (LocationContextMapView.DIRTY_FLAG.get()) {
                        // load data back
                        LocationContextView.this.value.getPolygon().clear();
                        for (GeoPoint gp : LocationContextMapView.GEO_POINTS) {
                            LocationContextView.this.value.getPolygon().add(
                                    new LocationContextGeoPoint(gp.getLatitudeE6() / 1E6, gp.getLongitudeE6() / 1E6));
                        }
                        // update view
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            
                            @Override
                            public void run() {
                                LocationContextView.this.pointsList.update(LocationContextView.this.value.getPolygon());
                            }
                        });
                        
                    }
                }
                
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                }
            }
        };
    };
    
    /**
     * Uncertainty
     */
    protected SeekBar uncertaintySeek;
    protected TextView uncertaintyText;
    
    /**
     * Hysteresis
     */
    protected SeekBar hysteresisSeek;
    protected TextView hysteresisText;
    
    
    public LocationContextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup(context);
    }
    
    
    public LocationContextView(Context context) {
        super(context);
        setup(context);
    }
    
    
    private void setup(Context context) {
        setOrientation(LinearLayout.VERTICAL);
        
        List<LocationContextGeoPoint> initialList = new ArrayList<LocationContextGeoPoint>();
        initialList.add(new LocationContextGeoPoint(48.745161, 9.106774));
        
        this.value = new LocationContextCondition(1000.0, 100.0, initialList);
        
        inflate(context, R.layout.contexts_location_view, this);
        
        this.points = (ExpandableListView) findViewById(R.id.coordinatesExpandableList);
        this.pointsList = new ExpandableGeoPointList();
        this.points.setAdapter(this.pointsList);
        List<LocationContextGeoPoint> l = new ArrayList<LocationContextGeoPoint>();
        l.add(new LocationContextGeoPoint(10, -10));
        l.add(new LocationContextGeoPoint(-10, 10));
        this.pointsList.update(l);
        
        this.changeBtn = (Button) findViewById(R.id.changeCoordsBtn);
        
        this.uncertaintySeek = (SeekBar) findViewById(R.id.uncertaintySeekBar);
        this.uncertaintyText = (TextView) findViewById(R.id.uncertaintyTextView);
        
        this.hysteresisSeek = (SeekBar) findViewById(R.id.hysteresisSeekBar);
        this.hysteresisText = (TextView) findViewById(R.id.hysteresisTextView);
        addListeners();
    }
    
    
    private void addListeners() {
        /*
         * start MapActivity
         */
        this.changeBtn.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LocationContextMapView.class);
                intent.putExtra(LATITUDE_EXTRA, LocationContextView.this.value.getPolygonLatitudeArray());
                intent.putExtra(LONGITUDE_EXTRA, LocationContextView.this.value.getPolygonLongitudeArray());
                GUITools.startIntent(intent);
                if (!LocationContextView.this.readerThread.isAlive()) {
                    LocationContextView.this.readerThread.start();
                }
            }
        });
        
        /*
         * hysteresis & uncertainty
         */
        OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {
            
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
            
            
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            
            
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double meters = seekBarValueToMeters(progress);
                String display;
                if (meters < 1000) {
                    display = String.format("%.0f m", meters);
                } else if (meters < 10000) {
                    display = String.format("%.1f km", meters / 1000.0);
                } else {
                    display = String.format("%.0f km", meters / 1000.0);
                }
                
                if (seekBar.equals(LocationContextView.this.uncertaintySeek)) {
                    LocationContextView.this.uncertaintyText.setText(display);
                    LocationContextView.this.hysteresisSeek.setMax(progress);
                    
                } else if (seekBar.equals(LocationContextView.this.hysteresisSeek)) {
                    LocationContextView.this.hysteresisText.setText(display);
                    
                }
                
            }
        };
        this.uncertaintySeek.setOnSeekBarChangeListener(seekBarListener);
        this.hysteresisSeek.setOnSeekBarChangeListener(seekBarListener);
        seekBarListener.onProgressChanged(this.uncertaintySeek, metersToSeekBarValue(this.value.getUncertainty()),
                false);
        seekBarListener.onProgressChanged(this.hysteresisSeek, metersToSeekBarValue(this.value.getHysteresis()), false);
    }
    
    
    @Override
    public View asView() {
        return this;
    }
    
    
    @Override
    public String getViewCondition() {
        this.value.setUncertainty(seekBarValueToMeters(this.uncertaintySeek.getProgress()));
        this.value.setHysteresis(seekBarValueToMeters(this.hysteresisSeek.getProgress()));
        
        // polygon via MapView
        
        return this.value.toString();
    }
    
    
    @Override
    public void setViewCondition(String condition) throws InvalidConditionException {
        this.uncertaintySeek.setProgress(metersToSeekBarValue(this.value.getUncertainty()));
        this.hysteresisSeek.setProgress(metersToSeekBarValue(this.value.getHysteresis()));
        
        // polygon via MapView
        
        this.value = LocationContextCondition.parse(condition);
    }
    
    
    /**
     * Converts the {@link SeekBar} value in a reasonable meter amount.
     * 
     * @param progress
     * @return
     */
    protected double seekBarValueToMeters(int progress) {
        int pow = (progress / 90);
        double mantissa = 10.0 + (progress % 90);
        return mantissa * Math.pow(10.0, pow);
    }
    
    
    protected int metersToSeekBarValue(double meters) {
        int log = (int) Math.round(Math.log10(meters));
        double mantissa = meters / Math.pow(10.0, log);
        int modulo = (int) Math.round(mantissa - 10.0);
        return modulo + 90 * log;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        this.readerThread.interrupt();
    }
}
