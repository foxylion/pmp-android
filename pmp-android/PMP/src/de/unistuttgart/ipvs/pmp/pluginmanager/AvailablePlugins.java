package de.unistuttgart.ipvs.pmp.pluginmanager;

import java.util.ArrayList;
import java.util.List;

public class AvailablePlugins {
    
    private int revision;
    private List<Plugin> plugins = new ArrayList<Plugin>();
    
    
    public int getRevision() {
        return this.revision;
    }
    
    
    public void setRevision(int revision) {
        this.revision = revision;
    }
    
    
    public List<Plugin> getPlugins() {
        return plugins;
    }
    
    public class Plugin {
        
        private String identifier;
        private String name;
        private String description;
        private int latestRevision;
        private int installedRevision;
        
        
        public String getIdentifier() {
            return identifier;
        }
        
        
        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }
        
        
        public String getName() {
            return name;
        }
        
        
        public void setName(String name) {
            this.name = name;
        }
        
        
        public String getDescription() {
            return description;
        }
        
        
        public void setDescription(String description) {
            this.description = description;
        }
        
        
        public int getLatestRevision() {
            return latestRevision;
        }
        
        
        public void setLatestRevision(int latestRevision) {
            this.latestRevision = latestRevision;
        }
        
        
        public int getInstalledRevision() {
            return installedRevision;
        }
        
        
        public void setInstalledRevision(int installedRevision) {
            this.installedRevision = installedRevision;
        }
        
    }
    
}
