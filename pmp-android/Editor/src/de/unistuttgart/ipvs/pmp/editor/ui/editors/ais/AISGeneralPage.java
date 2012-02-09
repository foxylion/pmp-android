/**
 * 
 */
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.util.Locale;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.InformationTable;

/**
 * @author Thorsten
 * 
 */
public class AISGeneralPage extends FormPage {

    public static final String ID = "rgis_general";

    /**
     * @param editor
     * @param id
     * @param title
     */
    public AISGeneralPage(FormEditor editor) {
	super(editor, ID, "General");
    }

    @Override
    protected void createFormContent(IManagedForm managedForm) {
	ScrolledForm form = managedForm.getForm();
	FormToolkit toolkit = managedForm.getToolkit();
	form.setText("Define general information");

	form.getBody().setLayout(new GridLayout(1, false));
	addPropertiesSection(form.getBody(), toolkit);
	addLocalizationSection(form.getBody(), toolkit);
    }

    private void addPropertiesSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSectionWithDescription(parent, "Preferences",
		toolkit, "Defines general informations about the ais.xml");

	// Create elements stored inside this section
	Composite client = toolkit.createComposite(section, SWT.WRAP);

	client.setLayout(new GridLayout(2, false));

	GridData textLayout = new GridData();
	textLayout.horizontalAlignment = GridData.FILL;
	textLayout.grabExcessHorizontalSpace = true;

	toolkit.createLabel(client, "Identifier");
	Text identifier = toolkit.createText(client, null);
	identifier.setLayoutData(textLayout);

	toolkit.createLabel(client, "Revision");
	Text revision = toolkit.createText(client, null);
	revision.setLayoutData(textLayout);

	section.setClient(client);
    }

    private void addLocalizationSection(Composite parent, FormToolkit toolkit) {
	// Set the section's parameters
	Section section = createSectionWithDescription(parent, "Localization",
		toolkit, "Defines the localized informations about the ais.xml");

	// Create elements stored inside this section
	final Composite client = toolkit.createComposite(section, SWT.None);

	client.setLayout(new GridLayout(1, true));

	final GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	// Workaround for SWT-Bug needed
	// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
	layoutData.widthHint = 1;

//	// Create the table
//	final Table table = new Table(client, SWT.SINGLE | SWT.HIDE_SELECTION
//		| SWT.BORDER);
//
//	table.setHeaderVisible(true);
//	table.setLinesVisible(true);
//
//	final TableColumn locale = new TableColumn(table, SWT.LEFT);
//	locale.setText("Locale");
//	locale.pack();
//
//	final TableColumn name = new TableColumn(table, SWT.LEFT);
//	name.setText("Name");
//	name.pack();
//
//	final TableColumn desc = new TableColumn(table, SWT.LEFT);
//	desc.setText("Description");
//	desc.pack();
//
//	TableItem tableItem = new TableItem(table, SWT.NONE);
//	TableEditor editor = new TableEditor(table);
//
//	Text textField = new Text(client, SWT.NONE);
//	new AutoCompleteField(textField, new TextContentAdapter(),
//		Locale.getISOCountries());
//	
//	Button checkButton = new Button(table, SWT.PUSH);
//	checkButton.pack();
//
//	editor.minimumWidth = checkButton.getSize().x;
//	editor.horizontalAlignment = SWT.CENTER;
//	editor.setEditor(checkButton, tableItem, 0);
//	
	final Table table = new Table(client, SWT.SINGLE | SWT.HIDE_SELECTION
	| SWT.BORDER);
	table.setHeaderVisible(true);
	table.setLinesVisible(true);

	final TableColumn locale = new TableColumn(table, SWT.LEFT);
	locale.setText("Locale");
	locale.pack();

	final TableColumn name = new TableColumn(table, SWT.LEFT);
	name.setText("Name");
	name.pack();

	final TableColumn desc = new TableColumn(table, SWT.LEFT);
	desc.setText("Description");
	desc.pack();
        
        TableItem item = new TableItem(table, SWT.NONE);
                
        
        final TableEditor editor = new TableEditor(table);
        //The editor must have the same size as the cell and must
        //not be any smaller than 50 pixels.
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        
        
        Text newEditor = new Text(table, SWT.NONE);
        editor.setEditor(newEditor, item, 0);
	new AutoCompleteField(newEditor, new TextContentAdapter(),
	Locale.getISOCountries());
	table.setLayoutData(layoutData);
	section.setClient(client);
    }

    /**
     * Creates a default section which spans over the whole editor
     * 
     * @param parent
     * @param title
     * @param toolkit
     * @return
     */
    private Section createSectionWithDescription(Composite parent,
	    String title, FormToolkit toolkit, String desc) {
	Section section = toolkit.createSection(parent, Section.TWISTIE
		| Section.TITLE_BAR | Section.DESCRIPTION);
	section.setDescription(desc);
	section.setText(title);
	section.setExpanded(true);

	GridData layoutData = new GridData();
	layoutData.horizontalAlignment = GridData.FILL;
	layoutData.grabExcessHorizontalSpace = true;

	section.setLayoutData(layoutData);

	return section;
    }

}
