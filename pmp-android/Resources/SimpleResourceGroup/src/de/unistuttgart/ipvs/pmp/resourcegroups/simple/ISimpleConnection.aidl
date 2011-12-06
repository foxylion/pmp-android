package de.unistuttgart.ipvs.pmp.resourcegroups.simple;

import java.util.Map;

/**
 * Base on the {@link android.database.sqlite.SQLiteDatabase} class, this interface provides methods to easily
 * create, modify, delete databases as well as execute SQL commands.
 */
interface ISimpleConnection {

	int getPid();

    int getValue();

}