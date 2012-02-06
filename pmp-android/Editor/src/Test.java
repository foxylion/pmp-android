import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;


public class Test {

	/**
	 * This is used to test the internal components without the need to run another
	 * eclipse-instance
	 */
	public static void main(String[] args) {
		System.out.println("Running...");
		ServerProvider server = new ServerProvider();
		server.updateRessourceGroupList();
		
	}

}
