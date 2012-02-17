package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.ais;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SelectionDialog;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGIS;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * @author Thorsten Berberich
 * 
 */
public class RequiredPrivacySettingsDialog extends SelectionDialog {

    /**
     * The {@link RGISPrivacySetting}s to display
     */
    private RGIS toDisplay;

    /**
     * The text that is display on the left hand side
     */
    private StyledText text;
    /**
     * The list with the PrivacySettings
     */
    private CheckboxTableViewer listViewer;

    /**
     * Stores the entered value
     */
    private HashMap<String, String> values = new HashMap<String, String>();

    /**
     * Sizing constants
     */
    private final static int SIZING_SELECTION_WIDGET_HEIGHT = 250;
    private final static int SIZING_SELECTION_WIDGET_WIDTH = 300;

    /**
     * @param parentShell
     */
    public RequiredPrivacySettingsDialog(Shell parentShell, RGIS toDisplay) {
	super(parentShell);
	this.toDisplay = toDisplay;
    }

    @Override
    protected void configureShell(Shell shell) {
	super.configureShell(shell);
	shell.setText("Select the required Privacy Settings");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
	// create composite
	Composite composite = (Composite) super.createDialogArea(parent);
	composite.setLayout(new GridLayout(2, false));

	Label psLabel = new Label(composite, SWT.NULL);
	psLabel.setText("Choose the required Privacy Settings:");

	Label descLabel = new Label(composite, SWT.NULL);
	descLabel.setText("Description:");

	// The CheckboxTableViewer for the PrivacySettings
	listViewer = CheckboxTableViewer.newCheckList(composite, SWT.BORDER);

	// Set the content provider and the label provider
	PrivacySettingsDialogContentProvider contentProvider = new PrivacySettingsDialogContentProvider();
	listViewer.setContentProvider(contentProvider);
	listViewer.setLabelProvider(new PrivacySettingsDialogLabelProvider());
	listViewer.setInput(toDisplay);

	GridData data = new GridData(GridData.FILL_BOTH);
	data.heightHint = SIZING_SELECTION_WIDGET_HEIGHT;
	data.widthHint = convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH / 2);
	listViewer.getTable().setLayoutData(data);

	// The text that displays the description of the PS and the required
	// Value
	text = new StyledText(composite, SWT.BORDER | SWT.H_SCROLL
		| SWT.V_SCROLL | SWT.WRAP);
	text.setLayoutData(data);

	data = new GridData(GridData.FILL_HORIZONTAL);
	data.widthHint = SIZING_SELECTION_WIDGET_WIDTH;

	// Composite that holds the value label and text f
	Composite valueComp = new Composite(composite, SWT.BORDER);
	valueComp.setLayout(new GridLayout(2, false));
	valueComp.setLayoutData(data);

	Label valueLabel = new Label(valueComp, SWT.NULL);
	valueLabel.setText("Value:");
	valueLabel.pack();

	Text valueText = new Text(valueComp, SWT.BORDER);
	valueText.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL
		| GridData.HORIZONTAL_ALIGN_FILL));
	valueText.pack();

	applyDialogFont(composite);
	return composite;
    }

    @SuppressWarnings("unchecked")
    protected void okPressed() {

	Object[] children = listViewer.getCheckedElements();

	// Build a list of selected children.
	if (children != null) {
	    @SuppressWarnings("rawtypes")
	    ArrayList list = new ArrayList();
	    for (int i = 0; i < children.length; ++i) {
		Object element = children[i];
		if (listViewer.getChecked(element)) {
		    list.add(element);
		}
	    }
	    setResult(list);
	}
	super.okPressed();
    }
}
