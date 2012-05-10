/*
 * Copyright 2012 pmp-android development team
 * Project: ConnectionResourceGroup
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
package de.unistuttgart.ipvs.pmp.resourcegroups.energy;

/**
 * @author Marcus Vetter
 */
interface IEnergy {

	/*
	* Current values
	*/
	
	/**
	* Get the current level
	*/
	String getCurrentLevel();
	
	/**
	* Get the current health
	*/
	String getCurrentHealth();
	
	/**
	* Get the current status
	*/
	String getCurrentStatus();
	
	/**
	* Get the current plugged
	*/
	String getCurrentPlugged();
	
	/**
	* Get the current charging time
	*/
	String getCurrentStatusTime();

	/**
	* Get the current temperature
	*/
	String getCurrentTemperature();
	
	/*
	* Values since last boot
	*/
	
	/**
	* Get the date of the last device boot
	*/
	String getLastBootDate();
	
	/**
	* Get the uptime since last boot
	*/
	String getLastBootUptime();
	
	/**
	* Get the uptime with battery since last boot
	*/
	String getLastBootUptimeBattery();
	
	/**
	* Get the duration of charging since last boot
	*/
	String getLastBootDurationOfCharging();
	
	/**
	* Get the ratio (charging:battery) since last boot
	*/
	String getLastBootRatio();
	
	/**
	* Get the temperature peak since last boot
	*/
	String getLastBootTemperaturePeak();
	
	/**
	* Get the temperature average since last boot
	*/
	String getLastBootTemperatureAverage();
	
	/**
	* Get the count of charging since last boot
	*/
	String getLastBootCountOfCharging();
	
	/** 
	* Get the time of active device (screen on) since last boot
	*/
	String getLastBootScreenOn();
	
	/*
	* Total values
	*/
	
	/**
	* Get the date of the first device boot since measurement
	*/
	String getTotalBootDate();
	
	/**
	* Get the total uptime
	*/
	String getTotalUptime();
	
	/**
	* Get the total uptime with battery
	*/
	String getTotalUptimeBattery();
	
	/**
	* Get the total duration of charging
	*/
	String getTotalDurationOfCharging();
	
	/**
	* Get the total ratio (charging:battery)
	*/
	String getTotalRatio();
	
	/**
	* Get the total temperature peak
	*/
	String getTotalTemperaturePeak();
	
	/**
	* Get the total temperature average
	*/
	String getTotalTemperatureAverage();
	
	/**
	* Get the total count of charging
	*/
	String getTotalCountOfCharging();
	
	/** 
	* Get the time of active device (screen on) in total
	*/
	String getTotalScreenOn();
	
	/**
	* Upload the data
	* @return URL of the evaluation
	*/
	String uploadData();

}