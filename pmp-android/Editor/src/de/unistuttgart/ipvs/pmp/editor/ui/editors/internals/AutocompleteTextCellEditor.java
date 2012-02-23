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
