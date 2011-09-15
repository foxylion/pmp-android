package de.unistuttgart.ipvs.pmp.resourcegroups.database;

interface IDatabaseAccessTemp {
    
    String select();
    boolean insert();
    boolean update();
    boolean delete();
    boolean create();
}
