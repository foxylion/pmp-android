package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Defines the details page that allows the user to edit the privacy setting's
 * identifier and valid values
 * 
 * @author Patrick Strobel
 */
public class PrivacySettingDetailsPage implements IDetailsPage {
	
	private IManagedForm form;
	private Text identifier;
	private Text values;

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
		//System.out.println("Draw");
		FormToolkit toolkit = form.getToolkit();
		Section section = toolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
		section.setText("Privacy Setting");
		section.setExpanded(true);
		section.setLayoutData(parentLayout);
		
		// Add Textfields
		Composite compo = toolkit.createComposite(section);
		compo.setLayout(new GridLayout(2, false));
		GridData textLayout = new GridData();
		textLayout.horizontalAlignment = GridData.FILL;
		textLayout.grabExcessHorizontalSpace = true;
		toolkit.createLabel(compo, "Identifier");
		identifier = toolkit.createText(compo, "Value");
		identifier.setLayoutData(textLayout);
		toolkit.createLabel(compo, "Valid values");
		values = toolkit.createText(compo, "True/False");
		values.setLayoutData(textLayout);
		section.setClient(compo);
		
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
		RGISPrivacySetting ps = (RGISPrivacySetting)((TreeSelection)selection).getFirstElement();
		
		if (ps != null) {
			identifier.setText(ps.getIdentifier());
			values.setText(ps.getValidValueDescription());
		}		
	}


}
