/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-CommunicationLib
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
package de.unistuttgart.ipvs.pmp.infoapp.graphs;

/**
 * This builder accepts different information about the charts/graphs and uses this information to create the URL to the
 * available chart-scripts
 * 
 * @author Patrick Strobel
 */
public class UrlBuilder {
    
    public static String DEFAULT_URL = "https://infoapp.no-ip.org/graphs";
    private String url;
    
    private int day = 0;
    private int month = 0;
    private int year = 0;
    
    public enum Scales {
        DAY,
        WEEK,
        MONTH,
        YEAR
    };
    
    private Scales scale = Scales.DAY;
    
    private boolean showAnnotations = true;
    
    public enum Views {
        DYNAMIC,
        STATIC
    }
    
    private Views view = Views.DYNAMIC;
    
    private String deviceId;
    
    
    /**
     * Creates a new builder-instance used to create URLs to the available charts
     * 
     * @param url
     *            URL to where all scripts are located.
     * @param deviceId
     *            16-bit (32 characters) HEX-value used the uniquely identify the Android device
     */
    public UrlBuilder(String url, String deviceId) {
        this.url = url;
        this.deviceId = deviceId;
    }
    
    
    /**
     * Sets the day in month for which the charts should be displayed.
     * This need only to be set if a day other than the current day should be used.
     * If not set or set to "0" the current day will be used.<br>
     * <b>Note: </b>If the year ({@link setYear()}) or month ({@link setMonth()}) is not set or set to 0, the script
     * will always use the current day
     * 
     * @param day
     *            A value between 0 and 31
     */
    public void setDay(int day) {
        if (day >= 1 && day <= 31) {
            this.day = day;
        }
    }
    
    
    /**
     * Sets the month for which the charts should be displayed.
     * This need only to be set if a month other than the current month should be used.
     * If not set or set to "0" the current month will be used.<br>
     * <b>Note: </b>If the year ({@link setYear()}) is not set or set to 0, the script will
     * always use the current month
     * 
     * @param month
     *            A value between 0 and 12
     */
    public void setMonth(int month) {
        if (month >= 1 && month <= 12) {
            this.month = month;
        }
    }
    
    
    /**
     * Sets the year for which the charts should be displayed.
     * This need only to be set if a year other than the current year should be used.
     * If not set or set to "0" the current year will be used
     * 
     * @param year
     *            A non-negative year
     */
    public void setYear(int year) {
        if (year >= 0) {
            this.year = year;
        }
    }
    
    
    /**
     * Sets the scale that should be used for the charts displayed by the script.
     * If not set {@code Scales.DAY} will be used
     * 
     * @param scale
     *            Scale used for the charts
     */
    public void setScale(Scales scale) {
        this.scale = scale;
    }
    
    
    /**
     * If set to true, annotations will be shown in the dynamic charts.
     * If not set, annotations will be visible
     * 
     * @param show
     *            True, when annotations should be shown
     */
    public void setShowAnnotations(boolean show) {
        this.showAnnotations = show;
    }
    
    
    /**
     * Set the view-mode that will be used by the scripts.
     * If set to {@code Views.DYNAMIC}), interactive charts will be used. These charts require enabled Java-Script and a
     * SVG capable browser. As SVG-capabilities are only available in Android's default browser since version 3.0, this
     * should be set to {@code Views.static} when running on a device having an older Android version installed
     * 
     * @param view
     *            View mode used to render the charts
     */
    public void setView(Views view) {
        this.view = view;
    }
    
    
    /**
     * Generates a string that contains all parameters that have been set to a non-default value and appends it to the
     * base URL
     * 
     * @param scriptName
     *            The script name that will inserted between the base URL and the parameters
     * @return String that can be used to be directly attached to the URL
     */
    private String getParameterizedUrl(String scriptName) {
        StringBuilder url = new StringBuilder(this.url);
        url.append("/");
        url.append(scriptName);
        url.append("?");
        
        // Date
        if (this.year > 0) {
            url.append("year=");
            url.append(this.year);
            
            if (this.month > 0) {
                url.append("&month=");
                url.append(this.month);
                
                if (this.day > 0) {
                    url.append("&day=");
                    url.append(this.day);
                }
            }
        }
        
        // Scale
        if (this.scale != Scales.DAY) {
            url.append("&scale=");
            switch (this.scale) {
                case WEEK:
                    url.append("week");
                    break;
                case MONTH:
                    url.append("month");
                    break;
                case YEAR:
                    url.append("year");
                    break;
            }
        }
        
        //Remaining parameters
        if (!this.showAnnotations) {
            url.append("&annotations=hide");
        }
        
        if (this.view != Views.DYNAMIC) {
            url.append("&view=static");
        }
        
        url.append("&device=");
        url.append(this.deviceId);
        
        return url.toString();
    }
    
    
    /**
     * Gets the URL to the device specific battery graphs
     * 
     * @return The URL
     */
    public String getBatteryGraphUrl() {
        return getParameterizedUrl("battery.php");
    }
    
    
    /**
     * Gets the URL to the device specific cellular connection graphs
     * 
     * @return The URL
     */
    public String getCellularConnectionGraphUrl() {
        return getParameterizedUrl("connectioncellular.php");
    }
    
    
    /**
     * Gets the URL to the device specific near-field connection graphs
     * 
     * @return The URL
     */
    public String getConnectionGraphUrl() {
        return getParameterizedUrl("connection.php");
    }
    
    
    /**
     * Gets the URL to the device specific activity and standby graphs
     * 
     * @return The URL
     */
    public String getStandbyGraphUrl() {
        return getParameterizedUrl("connection.php");
    }
    
    
    /**
     * Gets the URL to the statistical battery graphs
     * 
     * @return The URL
     */
    public String getBatteryPropGraphUrl() {
        return getParameterizedUrl("battery_properties.php");
    }
    
    
    /**
     * Gets the URL to the statistical connection graphs
     * 
     * @return The URL
     */
    public String getConnctionPropGraphUrl() {
        return getParameterizedUrl("connection_properties.php");
    }
    
    
    /**
     * Gets the URL to the statistical hardware graphs
     * 
     * @return The URL
     */
    public String getHardwarePropGraphUrl() {
        return getParameterizedUrl("hardware_properties.php");
    }
    
    
    /**
     * Gets the URL to the statistical profile graphs
     * 
     * @return The URL
     */
    public String getProfilePropGraphUrl() {
        return getParameterizedUrl("profile_properties.php");
    }
    
    
    /**
     * Gets the URL to the statistical software graphs
     * 
     * @return The URL
     */
    public String getSoftwarePropGraphUrl() {
        return getParameterizedUrl("software_properties.php");
    }
}
