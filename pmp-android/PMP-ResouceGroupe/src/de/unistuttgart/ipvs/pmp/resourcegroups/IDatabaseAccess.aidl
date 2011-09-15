package de.unistuttgart.ipvs.pmp.resourcegroups;

interface IDatabaseAccess {
    
    boolean insert();
    boolean update();
    boolean delete();
    boolean create();
}