package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public class PrivacySettingDetailPage implements IDetailsPage {
	
	private IManagedForm form;

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;		
	}

	@Override
	public void createContents(Composite parent) {
		// Set parent's layout
		GridData parentLayout = new GridData();
		parentLayout.verticalAlignment = GridData.FILL;
		parentLayout.grabExcessVerticalSpace = true;
		parentLayout.horizontalAlignment = GridData.FILL;
		parentLayout.grabExcessHorizontalSpace = true;
		parent.setLayout(new GridLayout());
		//parent.setLayoutData(parentLayout);
		
		// Build view
		System.out.println("Draw");
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		section.setText("Privacy Setting");
		section.setExpanded(true);
		section.setLayoutData(parentLayout);
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void commit(boolean onSave) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean setFormInput(Object input) {
		System.out.println("Set Input");
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isStale() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		// TODO Auto-generated method stub
		System.out.println("Update details!");
		
	}


}
