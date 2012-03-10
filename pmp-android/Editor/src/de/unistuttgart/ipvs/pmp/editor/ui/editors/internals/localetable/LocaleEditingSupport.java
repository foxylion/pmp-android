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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable;

import java.util.Locale;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Provides a cell editor that enables the user to put in a locale. It provides
 * a autocomplete-text field the shows all available locales while typing.
 * 
 * @author Patrick Strobel
 */
public abstract class LocaleEditingSupport extends EditingSupport {

    // private ComboBoxViewerCellEditor editor;
    private TextCellEditor editor;

    public LocaleEditingSupport(TableViewer viewer) {
	super(viewer);
	editor = new AutocompleteTextCellEditor(viewer.getTable(),
		Locale.getISOLanguages());
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
	return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
	return true;
    }

}
