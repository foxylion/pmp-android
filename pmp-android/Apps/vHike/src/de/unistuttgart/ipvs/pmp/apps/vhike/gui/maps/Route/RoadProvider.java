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
package de.unistuttgart.ipvs.pmp.apps.vhike.gui.maps.Route;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.google.android.maps.GeoPoint;

/**
 * 
 * Connects to GooleMaps and calculates a route from A to B and converts to fit
 * Google Maps API
 * 
 * @author Andre Nguyen
 * 
 */
public class RoadProvider {
    
    public static Road getRoute(InputStream is) {
        KMLHandler handler = new KMLHandler();
        try {
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(is, handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return handler.mRoad;
    }
    
    
    public static String getUrl(double fromLat, double fromLon, double toLat,
            double toLon) {// connect to map web service
        StringBuffer urlString = new StringBuffer();
        urlString.append("http://maps.google.com/maps?f=d&hl=en");
        urlString.append("&saddr=");// from
        urlString.append(Double.toString(fromLat));
        urlString.append(",");
        urlString.append(Double.toString(fromLon));
        urlString.append("&daddr=");// to
        urlString.append(Double.toString(toLat));
        urlString.append(",");
        urlString.append(Double.toString(toLon));
        urlString.append("&ie=UTF8&0&om=0&output=kml");
        return urlString.toString();
    }
    
    
    public static String getUrl1(GeoPoint from, GeoPoint to) {
        StringBuffer urlString = new StringBuffer();
        urlString.append("http://maps.google.com/maps?f=d&hl=en");
        urlString.append("&saddr=");// from
        urlString.append(from.getLatitudeE6());
        urlString.append(",");
        urlString.append(from.getLongitudeE6());
        urlString.append("&daddr=");// to
        urlString.append(to.getLatitudeE6());
        urlString.append(",");
        urlString.append(to.getLongitudeE6());
        urlString.append("&ie=UTF8&0&om=0&output=kml");
        return urlString.toString();
    }
    
}

class KMLHandler extends DefaultHandler {
    
    Road mRoad;
    boolean isPlacemark;
    boolean isRoute;
    boolean isItemIcon;
    @SuppressWarnings("rawtypes")
    private Stack mCurrentElement = new Stack();
    private String mString;
    
    
    public KMLHandler() {
        this.mRoad = new Road();
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public void startElement(String uri, String localName, String name,
            Attributes attributes) throws SAXException {
        this.mCurrentElement.push(localName);
        if (localName.equalsIgnoreCase("Placemark")) {
            this.isPlacemark = true;
            this.mRoad.mPoints = addPoint(this.mRoad.mPoints);
        } else if (localName.equalsIgnoreCase("ItemIcon")) {
            if (this.isPlacemark) {
                this.isItemIcon = true;
            }
        }
        this.mString = new String();
    }
    
    
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        String chars = new String(ch, start, length).trim();
        this.mString = this.mString.concat(chars);
    }
    
    
    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        if (this.mString.length() > 0) {
            if (localName.equalsIgnoreCase("name")) {
                if (this.isPlacemark) {
                    this.isRoute = this.mString.equalsIgnoreCase("Route");
                    if (!this.isRoute) {
                        this.mRoad.mPoints[this.mRoad.mPoints.length - 1].mName = this.mString;
                    }
                } else {
                    this.mRoad.mName = this.mString;
                }
            } else if (localName.equalsIgnoreCase("color") && !this.isPlacemark) {
                this.mRoad.mColor = Integer.parseInt(this.mString, 16);
            } else if (localName.equalsIgnoreCase("width") && !this.isPlacemark) {
                this.mRoad.mWidth = Integer.parseInt(this.mString);
            } else if (localName.equalsIgnoreCase("description")) {
                if (this.isPlacemark) {
                    String description = cleanup(this.mString);
                    if (!this.isRoute) {
                        this.mRoad.mPoints[this.mRoad.mPoints.length - 1].mDescription = description;
                    } else {
                        this.mRoad.mDescription = description;
                    }
                }
            } else if (localName.equalsIgnoreCase("href")) {
                if (this.isItemIcon) {
                    this.mRoad.mPoints[this.mRoad.mPoints.length - 1].mIconUrl = this.mString;
                }
            } else if (localName.equalsIgnoreCase("coordinates")) {
                if (this.isPlacemark) {
                    if (!this.isRoute) {
                        String[] xyParsed = split(this.mString, ",");
                        double lon = Double.parseDouble(xyParsed[0]);
                        double lat = Double.parseDouble(xyParsed[1]);
                        this.mRoad.mPoints[this.mRoad.mPoints.length - 1].mLatitude = lat;
                        this.mRoad.mPoints[this.mRoad.mPoints.length - 1].mLongitude = lon;
                    } else {
                        String[] coodrinatesParsed = split(this.mString, " ");
                        int lenNew = coodrinatesParsed.length;
                        int lenOld = this.mRoad.mRoute.length;
                        double[][] temp = new double[lenOld + lenNew][2];
                        for (int i = 0; i < lenOld; i++) {
                            temp[i] = this.mRoad.mRoute[i];
                        }
                        for (int i = 0; i < lenNew; i++) {
                            String[] xyParsed = split(coodrinatesParsed[i], ",");
                            for (int j = 0; j < 2 && j < xyParsed.length; j++) {
                                temp[lenOld + i][j] = Double
                                        .parseDouble(xyParsed[j]);
                            }
                        }
                        this.mRoad.mRoute = temp;
                    }
                }
            }
        }
        this.mCurrentElement.pop();
        if (localName.equalsIgnoreCase("Placemark")) {
            this.isPlacemark = false;
            if (this.isRoute) {
                this.isRoute = false;
            }
        } else if (localName.equalsIgnoreCase("ItemIcon")) {
            if (this.isItemIcon) {
                this.isItemIcon = false;
            }
        }
    }
    
    
    private String cleanup(String value) {
        String remove = "<br/>";
        int index = value.indexOf(remove);
        if (index != -1) {
            value = value.substring(0, index);
        }
        remove = "&#160;";
        index = value.indexOf(remove);
        int len = remove.length();
        while (index != -1) {
            value = value.substring(0, index).concat(
                    value.substring(index + len, value.length()));
            index = value.indexOf(remove);
        }
        return value;
    }
    
    
    public RPoint[] addPoint(RPoint[] points) {
        RPoint[] result = new RPoint[points.length + 1];
        for (int i = 0; i < points.length; i++) {
            result[i] = points[i];
        }
        result[points.length] = new RPoint();
        return result;
    }
    
    
    private static String[] split(String strString, String strDelimiter) {
        String[] strArray;
        int iOccurrences = 0;
        int iIndexOfInnerString = 0;
        int iIndexOfDelimiter = 0;
        int iCounter = 0;
        if (strString == null) {
            throw new IllegalArgumentException("Input string cannot be null.");
        }
        if (strDelimiter.length() <= 0 || strDelimiter == null) {
            throw new IllegalArgumentException(
                    "Delimeter cannot be null or empty.");
        }
        if (strString.startsWith(strDelimiter)) {
            strString = strString.substring(strDelimiter.length());
        }
        if (!strString.endsWith(strDelimiter)) {
            strString += strDelimiter;
        }
        while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
                iIndexOfInnerString)) != -1) {
            iOccurrences += 1;
            iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
        }
        strArray = new String[iOccurrences];
        iIndexOfInnerString = 0;
        iIndexOfDelimiter = 0;
        while ((iIndexOfDelimiter = strString.indexOf(strDelimiter,
                iIndexOfInnerString)) != -1) {
            strArray[iCounter] = strString.substring(iIndexOfInnerString,
                    iIndexOfDelimiter);
            iIndexOfInnerString = iIndexOfDelimiter + strDelimiter.length();
            iCounter += 1;
        }
        
        return strArray;
    }
}
