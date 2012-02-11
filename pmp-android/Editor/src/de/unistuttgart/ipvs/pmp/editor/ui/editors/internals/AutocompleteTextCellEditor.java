package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Provides an text-field with an integrated autocomplete field
 * @author Patrick Strobel
 *
 */
public class AutocompleteTextCellEditor extends TextCellEditor {
	
	// Other possible implementation: 
	// http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.jface.snippets/Eclipse%20JFace%20Snippets/org/eclipse/jface/snippets/viewers/Snippet060TextCellEditorWithContentProposal.java?revision=1.1&view=co
	
	public AutocompleteTextCellEditor(Composite parent, String[] proposals) {
		super(parent);
		new AutoCompleteField(this.getControl(), new TextContentAdapter(), proposals);
	}

	protected void focusLost() {
		
	}
	
	protected boolean dependsOnExternalFocusListener() {
		 return false;
	}
	
}
