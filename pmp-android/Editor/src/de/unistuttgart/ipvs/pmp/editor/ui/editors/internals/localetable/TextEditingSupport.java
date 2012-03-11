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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 * @author Patrick Strobel
 * 
 */
public abstract class TextEditingSupport extends EditingSupport {
    
    protected TextCellEditor editor;
    
    
    public TextEditingSupport(ColumnViewer viewer) {
        super(viewer);
        this.editor = new TextCellEditor((Composite) viewer.getControl());
    }
    
    
    @Override
    protected CellEditor getCellEditor(Object element) {
        return this.editor;
    }
    
    
    @Override
    protected boolean canEdit(Object element) {
        return true;
    }
    
}
