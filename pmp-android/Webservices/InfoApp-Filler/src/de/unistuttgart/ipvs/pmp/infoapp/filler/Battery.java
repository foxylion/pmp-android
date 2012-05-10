/*
 * Copyright 2012 pmp-android development team
 * Project: InfoApp-Filler
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
package de.unistuttgart.ipvs.pmp.infoapp.filler;

import java.io.IOException;
import java.util.List;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.eventmanager.BatteryEventManager;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Adapter;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.BatteryEvent.Status;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.events.Event;

public class Battery extends Filler {
    
    public Battery(Service service, long fromMillis, long toMillis) {
        super(service, fromMillis, toMillis);
    }
    
    
    @Override
    protected int maxSpreading() {
        return 240;
    }
    
    private BatteryEvent.Adapter lastPlugged = Adapter.NOT_PLUGGED;
    private BatteryEvent.Status lastStatus = Status.NOT_CHARGING;
    private float lastTemp = 21f;
    private int lastLevel = 50;
    private int lastVoltage = 3600;
    
    
    @Override
    public Event generateEvent(long time) {
        
        // Set level and voltage depending on last charging status
        if (this.lastStatus == Status.CHARGING) {
            this.lastLevel = (int) (this.lastLevel + Math.random() * 25);
            this.lastVoltage = (int) (this.lastLevel + Math.random() * 10);
            if (this.lastLevel > 100) {
                this.lastLevel = 100;
            }
            if (this.lastVoltage > 3650) {
                this.lastLevel = 3650;
            }
        } else if (this.lastStatus == Status.DISCHARGING) {
            this.lastLevel = (int) (this.lastLevel - Math.random() * 20);
            this.lastVoltage = (int) (this.lastLevel + Math.random() * 5);
            if (this.lastLevel < 0) {
                this.lastLevel = 0;
            }
            if (this.lastVoltage < 3400) {
                this.lastLevel = 33400;
            }
        }
        
        // Set adapter status randomly
        int pluggedInt = (int) (Math.random() * 4);
        switch (pluggedInt) {
            case 0:
                this.lastPlugged = Adapter.AC;
                break;
            case 1:
            case 2:
                this.lastPlugged = Adapter.NOT_PLUGGED;
                break;
            case 3:
                this.lastPlugged = Adapter.USB;
                break;
        }
        
        // Set charging status depending on adapter status
        if (this.lastPlugged == Adapter.NOT_PLUGGED) {
            if (Math.random() < 0.2) {
                this.lastStatus = Status.UNKNOWN;
            } else {
                this.lastStatus = Status.DISCHARGING;
            }
        } else {
            if (this.lastLevel >= 95) {
                this.lastStatus = Status.FULL;
            } else if (Math.random() < 0.2) {
                this.lastStatus = Status.NOT_CHARGING;
            } else {
                this.lastStatus = Status.CHARGING;
            }
        }
        
        // "Remove" battery if adapter is plugged and charging status is unknown
        boolean present = true;
        if (this.lastPlugged != Adapter.NOT_PLUGGED && this.lastStatus == Status.UNKNOWN) {
            if (Math.random() < 0.8) {
                present = false;
            }
        }
        
        this.lastTemp = this.lastTemp + ((float) (Math.random() - 0.5)) * 4;
        if (this.lastTemp > 100) {
            this.lastTemp = 100;
        }
        if (this.lastTemp < -100) {
            this.lastTemp = -100;
        }
        
        BatteryEvent event = new BatteryEvent(++this.id, time, this.lastLevel, this.lastVoltage, this.lastPlugged,
                present, this.lastStatus, this.lastTemp);
        return event;
    }
    
    
    @Override
    public void uploadEvents(List<Event> events) {
        BatteryEventManager bem = new BatteryEventManager(this.service);
        
        try {
            bem.commitEvents(events);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
}
