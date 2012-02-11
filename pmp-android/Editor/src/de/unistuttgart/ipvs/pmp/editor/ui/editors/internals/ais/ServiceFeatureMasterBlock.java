package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;

/**
 * @author Thorsten
 * 
 */
public class ServiceFeatureMasterBlock extends MasterDetailsBlock {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.
     * ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
	FormToolkit toolkit = managedForm.getToolkit();
	Section section = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR);
	section.setText("Privacy Settings");
	section.setExpanded(true);

	Composite compo = toolkit.createComposite(section);
	compo.setLayout(new GridLayout(2, false));

	// Add tree
	TreeViewer treeViewer = new TreeViewer(compo);
	treeViewer.setContentProvider(new ServiceFeatureTreeProvider());
	treeViewer.setLabelProvider(new ServiceFeatureTreeLabelProvider());
	treeViewer.setInput(Model.getInstance().getAis());
	GridData treeLayout = new GridData();
	treeLayout.verticalAlignment = GridData.FILL;
	treeLayout.grabExcessVerticalSpace = true;
	treeLayout.horizontalAlignment = GridData.FILL;
	treeLayout.grabExcessHorizontalSpace = true;
	treeViewer.getControl().setLayoutData(treeLayout);

	final SectionPart spart = new SectionPart(section);
	managedForm.addPart(spart);
	sashForm.setOrientation(SWT.HORIZONTAL);
	managedForm.reflow(true);

	treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

	    @Override
	    public void selectionChanged(SelectionChangedEvent event) {
		managedForm.fireSelectionChanged(spart, event.getSelection());

	    }
	});

	// Add buttons
	Composite buttonCompo = toolkit.createComposite(compo);
	GridData buttonLayout = new GridData();
	buttonLayout.verticalAlignment = SWT.BEGINNING;
	buttonCompo.setLayout(new FillLayout(SWT.VERTICAL));
	buttonCompo.setLayoutData(buttonLayout);
	toolkit.createButton(buttonCompo, "Add", SWT.PUSH);
	toolkit.createButton(buttonCompo, "Remove", SWT.PUSH);

	section.setClient(compo);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse
     * .ui.forms.IManagedForm)
     */
    @Override
    protected void createToolBarActions(IManagedForm arg0) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.
     * forms.DetailsPart)
     */
    @Override
    protected void registerPages(DetailsPart arg0) {
	// TODO Auto-generated method stub

    }

}
