package de.unistuttgart.ipvs.pmp;

public class PMPConditions {

    protected static void throw_IllegalArgumentException(String message) {
	RuntimeException re = new IllegalArgumentException(message);
	Log.e("A precondition check failed", re);
	throw re;
    }
}
