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
package de.unistuttgart.ipvs.pmp.infoapp.webservice.properties;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import de.unistuttgart.ipvs.pmp.infoapp.webservice.Service;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InternalDatabaseException;
import de.unistuttgart.ipvs.pmp.infoapp.webservice.exceptions.InvalidParameterException;

/**
 * Stores information about a device and allows to update or insert a new device information set
 * 
 * @author Patrick Strobel
 */
public class DeviceProperties extends Properties {
    
    public static class DeviceOem {
        
        private String manufacturer;
        private String model;
        private String ui;
        
        
        public DeviceOem(String manufacturer, String model, String ui) {
            this.manufacturer = manufacturer;
            this.model = model;
            this.ui = ui;
        }
        
        
        public String getManufacturer() {
            return this.manufacturer;
        }
        
        
        public String getModel() {
            return this.model;
        }
        
        
        public String getUi() {
            return this.ui;
        }
    }
    
    public static class Display {
        
        private short x;
        private short y;
        
        
        public Display(short x, short y) {
            this.x = x;
            this.y = y;
        }
        
        
        public short getResolutionX() {
            return this.x;
        }
        
        
        public short getResolutionY() {
            return this.y;
        }
    }
    
    public static class Memory {
        
        private short total;
        private short free;
        
        
        /**
         * 
         * @param total
         *            Total size of the memory in MB
         * @param free
         *            Free space in the memory in MB
         */
        public Memory(short total, short free) {
            this.total = total;
            this.free = free;
        }
        
        
        public short getTotal() {
            return this.total;
        }
        
        
        public short getFree() {
            return this.free;
        }
    }
    
    private DeviceOem deviceOem;
    private byte api;
    private String kernel;
    private Display display;
    private short cpu;
    private Memory memoryInternal;
    private Memory memoryExternal;
    private float cameraRes;
    private String[] sensors;
    private float runtime;
    
    
    /**
     * Creates a new device property-set
     * 
     * @param service
     *            Helper class used for communication with the webservice
     * @param deviceOem
     *            General information about the manufacturer and model
     * @param api
     *            Android API-level
     * @param kernel
     *            Linux kernel version
     * @param display
     *            Display resolution
     * @param cpu
     *            CPU frequency in MHz
     * @param memoryInt
     *            Information about the size of the internal memory
     * @param memoryExt
     *            Information about the size of the external memory
     * @param sensors
     *            Sensors supported by the device
     * @param runtime
     *            Device's runtime
     */
    public DeviceProperties(Service service, DeviceOem deviceOem, byte api, String kernel, Display display, short cpu,
            Memory memoryInt, Memory memoryExt, float cameraRes, String[] sensors, float runtime) {
        super(service);
        this.deviceOem = deviceOem;
        this.api = api;
        this.kernel = kernel;
        this.display = display;
        this.cpu = cpu;
        this.memoryInternal = memoryInt;
        this.memoryExternal = memoryExt;
        this.cameraRes = cameraRes;
        this.sensors = sensors;
        this.runtime = runtime;
    }
    
    
    /**
     * Gets general information about the device
     * 
     * @return General information
     */
    public DeviceOem getDeviceOem() {
        return this.deviceOem;
    }
    
    
    /**
     * Gets the Android API level used by the device
     * 
     * @return API level
     */
    public byte api() {
        return this.api;
    }
    
    
    /**
     * Gets the Linux kernel version
     * 
     * @return Kernel version
     */
    public String getKernel() {
        return this.kernel;
    }
    
    
    /**
     * Gets the display's resolution
     * 
     * @return Display resolution
     */
    public Display getDisplayResolution() {
        return this.display;
    }
    
    
    /**
     * Gets the CPU's frequency
     * 
     * @return Frequency in MHz
     */
    public short getCpuFrequency() {
        return this.cpu;
    }
    
    
    /**
     * Gets information about the internal memory
     * 
     * @return Internal memory information
     */
    public Memory getInternalMemory() {
        return this.memoryInternal;
    }
    
    
    /**
     * Gets information about the external memory
     * 
     * @return External memory information
     */
    public Memory getExternalMemory() {
        return this.memoryExternal;
    }
    
    
    /**
     * Gets the internal camera's resolution
     * 
     * @return Camera resolution
     */
    public float getCameraResolution() {
        return this.cameraRes;
    }
    
    
    /**
     * Gets the sensor's supported by the device
     * 
     * @return Supported sensors
     */
    public String[] getSensors() {
        return this.sensors;
    }
    
    
    /**
     * Gets the device's runtime
     * 
     * @return Device runtime
     */
    public float getRuntime() {
        return this.runtime;
    }
    
    
    @Override
    public void commit() throws InternalDatabaseException, InvalidParameterException, IOException {
        try {
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            params.add(new BasicNameValuePair("manufacturer", this.deviceOem.manufacturer));
            params.add(new BasicNameValuePair("apiLevel", Byte.toString(this.api)));
            params.add(new BasicNameValuePair("kernel", this.kernel));
            params.add(new BasicNameValuePair("model", this.deviceOem.model));
            params.add(new BasicNameValuePair("ui", this.deviceOem.ui));
            params.add(new BasicNameValuePair("displayResX", Short.toString(this.display.x)));
            params.add(new BasicNameValuePair("displayResY", Short.toString(this.display.y)));
            params.add(new BasicNameValuePair("cpuFrequency", Short.toString(this.cpu)));
            params.add(new BasicNameValuePair("memoryInternal", Short.toString(this.memoryInternal.total)));
            params.add(new BasicNameValuePair("memoryInternalFree", Short.toString(this.memoryInternal.free)));
            params.add(new BasicNameValuePair("memoryExternal", Short.toString(this.memoryExternal.total)));
            params.add(new BasicNameValuePair("memoryExternalFree", Short.toString(this.memoryExternal.free)));
            params.add(new BasicNameValuePair("cameraResolution", Float.toString(this.cameraRes)));
            
            StringBuilder sensorsSb = new StringBuilder();
            boolean first = true;
            for (String sensor : this.sensors) {
                if (first) {
                    first = false;
                } else {
                    sensorsSb.append(",");
                }
                sensorsSb.append(sensor);
            }
            params.add(new BasicNameValuePair("sensors", sensorsSb.toString()));
            
            params.add(new BasicNameValuePair("runtime", Float.toString(this.runtime)));
            
            super.service.requestPostService("update_device.php", params);
        } catch (JSONException e) {
            throw new IOException("Server returned no valid JSON object: " + e);
        }
    }
}
