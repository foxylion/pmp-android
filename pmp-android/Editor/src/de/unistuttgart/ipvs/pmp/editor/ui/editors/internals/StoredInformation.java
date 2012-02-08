package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;

public class StoredInformation {
	
	IObservableList information =  new WritableList();
	
	public StoredInformation() {
		information.add(new Information("de","Dateisystem","blbal"));
		information.add(new Information("en","Filesysten","blabla"));
	}
	
	protected IObservableList getObservableList() {
		return this.information;
	}
 
}
