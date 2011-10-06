/*
 * Copyright 2011 pmp-android development team
 * Project: SwitchesResourceGroup
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
package de.unistuttgart.ipvs.pmp.service;

import java.io.Serializable;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

public class SerializableContainer implements Parcelable {
    
    private Serializable serializable;
    
    
    public SerializableContainer(Serializable serializable) {
        this.serializable = serializable;
    }
    
    
    public Serializable getSerializable() {
        return this.serializable;
    }
    
    
    /**
     * Constructor for regenerating Java object of an parcel from this object. Normally called by
     * {@link Parcelable.Creator#createFromParcel(Parcel)} of the {@link SerializableContainer#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private SerializableContainer(Parcel source) {
        this.serializable = source.readSerializable();
    }
    
    
    /**
     * {@link SerializableContainer#writeToParcel(Parcel, int)} is called when the App Object is sent through an
     * {@link IBinder}. Therefore all data of the object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.serializable);
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<SerializableContainer> CREATOR = new Parcelable.Creator<SerializableContainer>() {
        
        @Override
        public SerializableContainer createFromParcel(Parcel source) {
            return new SerializableContainer(source);
        }
        
        
        @Override
        public SerializableContainer[] newArray(int size) {
            return new SerializableContainer[size];
        }
    };
    
}
