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
package de.unistuttgart.ipvs.pmp.app;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import de.unistuttgart.ipvs.pmp.app.xmlparser.AppInformationSet;

/**
 * A PrivacyLevel representation for the {@link IAppService}.
 * 
 * @author Jakob Jarosch
 */
public class AppInformationSetParcelable implements Parcelable {
    
    private AppInformationSet set;
    
    
    /**
     * Creates a new {@link AppInformationSet}.
     * 
     * @param resourceGroup
     *            identifier of the resource group
     * @param privacyLevel
     *            identifier of the privacy level
     * @param value
     *            set value of the privacy level
     */
    public AppInformationSetParcelable(AppInformationSet set) {
        this.set = set;
    }
    
    
    /**
     * @return Returns the {@link AppInformationSet}
     */
    public AppInformationSet getAppInformationSet() {
        return this.set;
    }
    
    
    /**
     * Constructor for regenerating Java object of an parcel from this object. Normally called by
     * {@link Parcelable.Creator#createFromParcel(Parcel)} of the {@link AppInformationSetParcelable#CREATOR} variable.
     * 
     * @param source
     *            Parcel-Source
     */
    private AppInformationSetParcelable(Parcel source) {
        this.set = (AppInformationSet) source.readSerializable();
    }
    
    
    /**
     * {@link AppInformationSet#writeToParcel(Parcel, int)} is called when the App Object is sent through an
     * {@link IBinder}. Therefore all data of the object have to be written into the {@link Parcel}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(this.set);
    }
    
    
    @Override
    public int describeContents() {
        return 0;
    }
    
    /**
     * Required Creator for the {@link Parcelable} regeneration.
     */
    public static final Parcelable.Creator<AppInformationSetParcelable> CREATOR = new Parcelable.Creator<AppInformationSetParcelable>() {
        
        @Override
        public AppInformationSetParcelable createFromParcel(Parcel source) {
            return new AppInformationSetParcelable(source);
        }
        
        
        @Override
        public AppInformationSetParcelable[] newArray(int size) {
            return new AppInformationSetParcelable[size];
        }
    };
}
