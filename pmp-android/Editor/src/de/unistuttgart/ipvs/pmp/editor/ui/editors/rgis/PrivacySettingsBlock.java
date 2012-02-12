package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;


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
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Manages the master block of the privacy settings pages
 * 
 * @author Patrick Strobel
 *
 */
public class PrivacySettingsBlock extends MasterDetailsBlock {
	
	private FormPage form;
	
	public PrivacySettingsBlock(FormPage form) {
		this.form = form;
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		
		// Create section
		Section section = toolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		section.setText("Privacy Settings");
		section.setExpanded(true);
		section.marginWidth = 5;
		section.marginHeight = 5;
		
		Composite compo = toolkit.createComposite(section);
		compo.setLayout(new GridLayout(2,false));
		
		// Add tree
		TreeViewer treeViewer = new TreeViewer(compo);
		treeViewer.setContentProvider(new PrivacySettingsContentProvider());
		treeViewer.setLabelProvider(new PrivacySettingsLabelProvider());
		treeViewer.setInput(Model.getInstance().getRgis());
		
		GridData treeLayout = new GridData();
		treeLayout.verticalAlignment = GridData.FILL;
		treeLayout.grabExcessVerticalSpace = true;
		treeLayout.horizontalAlignment = GridData.FILL;
		treeLayout.grabExcessHorizontalSpace = true;
		treeViewer.getControl().setLayoutData(treeLayout);
		

		// Create listener that handles selections of tree-items
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
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

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(RGISPrivacySetting.class, new PrivacySettingDetailsPage());
		detailsPart.registerPage(String.class, new LocalizationDetailsPage());
		
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// TODO Auto-generated method stub
		
	}

}
