package de.unistuttgart.ipvs.pmp.model.context.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The parsed condition for a {@link LocationContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class LocationContextCondition {
    
    private static Map<String, LocationContextCondition> cache = new HashMap<String, LocationContextCondition>();
    
    private static Pattern CONDITION_PATTERN = Pattern.compile("([0-9\\.]+);([0-9\\.]+);([0-9\\.]~[0-9\\.]--)+");
    
    
    /**
     * Parses a {@link LocationContextCondition} from a string.
     * 
     * @param condition
     * @return
     */
    public static LocationContextCondition parse(String condition) {
        LocationContextCondition result = cache.get(condition);
        
        if (result == null) {
            Matcher match = CONDITION_PATTERN.matcher(condition);
            if (!match.matches()) {
                throw new IllegalArgumentException("LocationContextCondition was not formatted properly: " + condition);
            }
            
            List<LocationContextGeoPoint> poly = new ArrayList<LocationContextGeoPoint>();
            for (int group = 3; group < match.groupCount(); group++) {
                String[] coords = match.group(group).split("--")[0].split("~");
                poly.add(new LocationContextGeoPoint(Double.valueOf(coords[0]), Double.valueOf(coords[1])));
            }
            
            result = new LocationContextCondition(Double.parseDouble(match.group(1)),
                    Double.parseDouble(match.group(2)), poly);
            cache.put(condition, result);
        }
        
        return result;
    }
    
    /**
     * The polygon that defines the area to select. The space which is selected is inside the order of the points. CW
     * mean the outside, CCW the inside.
     */
    private List<LocationContextGeoPoint> polygon;
    
    /**
     * How far you can possibly be outside of the polygon (in meters)
     */
    private double uncertainty;
    
    /**
     * State-bound hysteresis, i.e. if you're inside you need to go this far outside to toggle deactive, if you're
     * outside you need to go this far in to toggle active.
     * (in meters)
     */
    private double hysteresis;
    
    
    public LocationContextCondition(double uncertainty, double hysteresis, List<LocationContextGeoPoint> polygon) {
        this.uncertainty = uncertainty;
        this.hysteresis = hysteresis;
        this.polygon = polygon;
    }
    
    
    @Override
    public String toString() {
        StringBuffer pointList = new StringBuffer();
        for (LocationContextGeoPoint lcgp : this.polygon) {
            pointList.append(lcgp.getLatitude());
            pointList.append("~");
            pointList.append(lcgp.getLongitude());
            pointList.append("--");
        }
        
        return String.format("%f;%f;%s", this.uncertainty, this.hysteresis, pointList.toString());
    }
    
    
    /**
     * Checks whether the condition is satisfied in the state
     * 
     * @param state
     * @return
     */
    public boolean satisfiedIn(LocationContextState state) {
        // TODO hysteresis
        
        // we do this first because the point-in-polygon test might suffer problems
        //if the single point is too close to the polygon
        if (geoCircleIntersectsPolygon(state, (state.getAccuracy() + this.uncertainty) / 1000f)) {
            return true;
        } else if (pointInPolygon(state)) {
            return true;
        }
        
        return false;
    }
    
    
    /**
     * Tests whether a circle around p with diameter d kilometers does intersect the polygon.
     * 
     * @param p
     * @param d
     * @return
     */
    private boolean geoCircleIntersectsPolygon(LocationContextGeoPoint p, double d) {
        // for each line in the polygon
        for (int i = 0; i < this.polygon.size() - 1; i++) {
            double lat = this.polygon.get(i).getLatitude();
            double lon = this.polygon.get(i).getLongitude();
            double latD = this.polygon.get(i + 1).getLatitude() - lat;
            double lonD = this.polygon.get(i + 1).getLongitude() - lon;
            
            // TODO...
            
        }
        return false;
    }
    
    
    /**
     * Tests point in polygon by using the even-odd crossing algorithm. May suffer problems if the point is too near to
     * the polygon.
     * 
     * @param p
     * @return
     */
    private boolean pointInPolygon(LocationContextGeoPoint p) {
        /*
         * imagine a ray cast from p in direction (1,1).
         */
        
        int intersections = 0;
        
        // for each line in the polygon
        for (int i = 0; i < this.polygon.size() - 1; i++) {
            double lat = this.polygon.get(i).getLatitude();
            double lon = this.polygon.get(i).getLongitude();
            double latD = this.polygon.get(i + 1).getLatitude() - lat;
            double lonD = this.polygon.get(i + 1).getLongitude() - lon;
            
            // find the segment parameter, i.e. whether the intersection
            // is on the selected part of the line segment, i.e. seg in [0,1]
            double seg = (p.getLatitude() + lon - p.getLongitude() - lat) / (latD + lonD);
            if ((seg < 0) || (seg > 1)) {
                continue;
            }
            
            // find whether it intersects the ray or the line behind zero, i.e. t < 0
            double t = p.getLatitude() + seg * latD - lat;
            if (t > 0) {
                intersections++;
            }
            
        }
        return intersections % 2 == 1;
    }
    
    
    /*
     * Getters / Setters for view
     */
    
    public double getUncertainty() {
        return this.uncertainty;
    }
    
    
    public void setUncertainty(double uncertainty) {
        this.uncertainty = uncertainty;
    }
    
    
    public double getHysteresis() {
        return this.hysteresis;
    }
    
    
    public void setHysteresis(double hysteresis) {
        this.hysteresis = hysteresis;
    }
    
    
    public List<LocationContextGeoPoint> getPolygon() {
        return this.polygon;
    }
    
}
