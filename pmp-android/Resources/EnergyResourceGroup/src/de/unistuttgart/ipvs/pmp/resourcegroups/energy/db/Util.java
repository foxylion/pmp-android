/*
 * Copyright 2012 pmp-android development team
 * Project: EnergyResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy.db;

/**
 * An utility class for converting milliseconds into a string representation of d, h, m, s
 * 
 * @author Marcus Vetter
 * 
 */
public class Util {
    
    /**
     * Convert a time in milliseconds to a string in days, hours, minutes, seconds
     * 
     * @param ms
     *            the time in milliseconds
     * @return a pretty string
     */
    public static String convertMillisecondsToString(long ms) {
        long givenSecs = ms / 1000;
        long days = givenSecs / 60 / 60 / 24;
        long daysInSec = days * 60 * 60 * 24;
        long hours = ((givenSecs - daysInSec) / 60 / 60);
        long hoursInSec = hours * 60 * 60;
        long mins = ((givenSecs - daysInSec - hoursInSec) / 60);
        long minsInSec = mins * 60;
        long secs = givenSecs - daysInSec - hoursInSec - minsInSec;
        
        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(String.valueOf(days) + "d ");
        }
        if (hours > 0) {
            sb.append(String.valueOf(hours) + "h ");
        }
        if (mins > 0) {
            sb.append(String.valueOf(mins) + "m ");
        }
        sb.append(String.valueOf(secs) + "s");
        
        return sb.toString();
    }
    
}
