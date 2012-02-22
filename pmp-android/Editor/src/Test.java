/*
 * Copyright 2012 pmp-android development team
 * Project: Editor
 * Project-Site: http://code.google.com/p/pmp-android/
 *
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import java.io.IOException;

import de.unistuttgart.ipvs.pmp.editor.util.ServerProvider;

public class Test {

	/**
	 * This is used to test the internal components without the need to run
	 * another eclipse-instance
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		System.out.println("Running...");
		ServerProvider server = new ServerProvider();
		server.getAvailableRessourceGroups();

	}

}
