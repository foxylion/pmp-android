/*
 * Copyright 2011 pmp-android development team
 * Project: CalendarApp
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
package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.util.Comparator;
import java.util.Date;

/**
 * A comparator to compare two {@link Date} objects. Objects will be sorted ascending, oldest dates first.
 * 
 * @author Thorsten Berberich
 * 
 */
public class DateComparator implements Comparator<Appointment> {
    
    @Override
    public int compare(Appointment todo1, Appointment todo2) {
        Date date1 = todo1.getDate();
        Date date2 = todo2.getDate();
        return date1.compareTo(date2);
    }
}
