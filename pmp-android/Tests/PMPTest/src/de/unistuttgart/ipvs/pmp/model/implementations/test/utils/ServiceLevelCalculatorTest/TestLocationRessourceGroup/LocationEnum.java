package de.unistuttgart.ipvs.pmp.model.implementations.test.utils.ServiceLevelCalculatorTest.TestLocationRessourceGroup;

public enum LocationEnum {

	STUTTGART(0),
	BADEN_WUERTTEMBERG(1),
	DEUTSCHLAND(2);
	
	private int id;
	
	private LocationEnum(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
}
