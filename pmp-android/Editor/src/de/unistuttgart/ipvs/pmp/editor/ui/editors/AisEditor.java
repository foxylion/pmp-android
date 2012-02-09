package de.unistuttgart.ipvs.pmp.editor.ui.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.AISGeneralPage;

public class AisEditor extends FormEditor {


    /* (non-Javadoc)
     * @see org.eclipse.ui.forms.editor.FormEditor#addPages()
     */
    @Override
    protected void addPages() {
	try {
	    addPage(new AISGeneralPage(this));
	} catch (PartInitException e) {
	    e.printStackTrace();
	}
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void doSave(IProgressMonitor arg0) {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#doSaveAs()
     */
    @Override
    public void doSaveAs() {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
     */
    @Override
    public boolean isSaveAsAllowed() {
	// TODO Auto-generated method stub
	return false;
    }
}