package de.unistuttgart.ipvs.pmp.jpmpps;

import java.io.File;

/**
 * Main Class.
 * 
 * @author Jakob Jarosch
 */
public class Main {

	/**
	 * Starts a new instance of the JPMPPS.
	 * 
	 * @param args
	 *            Requires one argument, the path to the folder with all
	 *            ResourceGroups.
	 */
	public static void main(String[] args) {
		// Check argument length
		if (args.length != 1) {
			System.out
					.println("[E] Sorry, needs one argument with path to resource groups. [-> Exit]");
			System.exit(1);
		}

		// Check if folder exists
		if (!new File(args[0]).isDirectory()) {
			System.out
					.println("[E] Path needs to reference a valid directory. [-> Exit]");
			System.exit(2);
		}

		// Start the server
		JPMPPS.get(new File(args[0])).initialize();
		JPMPPS.get().startServer();
	}
}
