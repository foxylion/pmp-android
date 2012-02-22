package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.IContentProposalListener2;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Provides an text-field with an integrated autocomplete field
 * 
 * @author Patrick Strobel
 * 
 */
public class AutocompleteTextCellEditor extends TextCellEditor {

    private ContentProposalAdapter adapter;

    // Other possible implementation:
    // http://dev.eclipse.org/viewcvs/viewvc.cgi/org.eclipse.jface.snippets/Eclipse%20JFace%20Snippets/org/eclipse/jface/snippets/viewers/Snippet060TextCellEditorWithContentProposal.java?revision=1.1&view=co

    public AutocompleteTextCellEditor(Composite parent, String[] proposals) {
	this(parent, new SimpleContentProposalProvider(proposals), null, null);
    }

    public AutocompleteTextCellEditor(Composite parent,
	    SimpleContentProposalProvider proposalProvider,
	    KeyStroke keyStroke, char[] autoActivationCharacters) {
	super(parent);

	proposalProvider.setFiltering(true);
	adapter = new ContentProposalAdapter(text, new TextContentAdapter(),
		proposalProvider, keyStroke, autoActivationCharacters);
	// adapter.setPropagateKeys(true);
	adapter.setProposalAcceptanceStyle(ContentProposalAdapter.PROPOSAL_REPLACE);

	// Listen for popup open/close events to be able to handle focus events
	// correctly
	adapter.addContentProposalListener(new IContentProposalListener2() {

	    @Override
	    public void proposalPopupClosed(ContentProposalAdapter adapter) {
		if (!text.isFocusControl()) {
		    focusLost();// fireApplyEditorValue();
		}
	    }

	    @Override
	    public void proposalPopupOpened(ContentProposalAdapter adapter) {
	    }
	});
    }

    @Override
    protected void focusLost() {

	if (!adapter.isProposalPopupOpen()) {
	    // this.fireApplyEditorValue();
	    super.focusLost();
	}

    }

    @Override
    protected boolean dependsOnExternalFocusListener() {
	return false;
    }

}
