package de.unistuttgart.ipvs.pmp.gui2.placeholder;

import android.graphics.Bitmap;


public class App {
    
    private String name;
    private String description;
    private Bitmap icon;

    public App(String name, String description, Bitmap icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Bitmap getIcon() {
        return icon;
    }
}
