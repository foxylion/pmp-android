package de.unistuttgart.ipvs.pmp.api;

public class PMPResourceIdentifier {
	private String rg;
	private String res;

	private PMPResourceIdentifier(String rg, String res) {
		throw new UnsupportedOperationException();
	}

	public static PMPResourceIdentifier make(String rg, String res) {
		throw new UnsupportedOperationException();
	}
}