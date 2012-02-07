package de.unistuttgart.ipvs.pmp.editor.ui.wizard.ais;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.editor.util.AndroidManifestAdapter;

public class WizardPageCreateAIS extends WizardPage {
    /**
     * {@link Text} for the project
     */
    private Text assetsFolderText;

    /**
     * The identifier of the project
     */
    private Text identifier;

    /**
     * Constructor for SampleNewWizardPage.
     * 
     * @param pageName
     */
    public WizardPageCreateAIS(ISelection selection) {
	super("App-Information-Set");
	setTitle("App-Information-Set File");
	setDescription("This wizard creates a new App-Information-Set for a PMP compatible Application.");
    }

    /**
     * @see IDialogPage#createControl(Composite)
     */
    @Override
    public void createControl(Composite parent) {
	Composite container = new Composite(parent, SWT.NULL);
	GridLayout layout = new GridLayout();
	container.setLayout(layout);
	layout.numColumns = 3;
	layout.verticalSpacing = 9;

	// Project label
	Label label = new Label(container, SWT.NULL);
	label.setText("&Assets folder:");
	assetsFolderText = new Text(container, SWT.BORDER | SWT.SINGLE);
	GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	assetsFolderText.setLayoutData(gd);
	assetsFolderText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		dialogChanged();
	    }
	});

	// Button to browse the project
	Button button = new Button(container, SWT.PUSH);
	button.setText("Browse...");
	button.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
		handleBrowse();
	    }
	});

	label = new Label(container, SWT.NULL);
	label.setText("File name:");

	Text fileText = new Text(container, SWT.BORDER | SWT.SINGLE);
	fileText.setText("ais.xml");
	fileText.setEnabled(false);
	gd = new GridData(GridData.FILL_HORIZONTAL);
	fileText.setLayoutData(gd);
	fileText.addKeyListener(new KeyListener() {

	    @Override
	    public void keyReleased(KeyEvent e) {
	    }

	    @Override
	    public void keyPressed(KeyEvent e) {
		dialogChanged();
	    }
	});

	new Text(container, SWT.NULL).setVisible(false);

	label = new Label(container, SWT.NULL);
	label.setText("Identifier:");

	identifier = new Text(container, SWT.BORDER | SWT.SINGLE);
	identifier.setEnabled(false);
	identifier.setLayoutData(gd);

	dialogChanged();
	setControl(container);
    }

    /**
     * Uses the standard container selection dialog to choose the value for the
     * project field. Only the project path will be taken and "/assets" will be
     * appended
     */
    private void handleBrowse() {
	ContainerSelectionDialog dialog = new ContainerSelectionDialog(
		getShell(), ResourcesPlugin.getWorkspace().getRoot(), false,
		"Select the project");
	if (dialog.open() == ContainerSelectionDialog.OK) {
	    Object[] result = dialog.getResult();
	    if (result.length == 1) {
		String[] split = ((Path) result[0]).toString().split("/");
		assetsFolderText.setText("/" + split[1] + "/assets");
	    }
	}
    }

    /**
     * Checks the project field if it is correct
     */
    private void dialogChanged() {
	// Split only the project out of the string
	String[] projects = getProjectName().split("/");

	// The project name
	String project = "";
	if (projects.length >= 2) {
	    project = "/" + projects[1];
	}

	IResource container = ResourcesPlugin.getWorkspace().getRoot()
		.findMember(new Path(project));

	// No project entered
	if (getProjectName().length() == 0) {
	    updateStatus("Project must be specified");
	    identifier.setText("");
	    return;
	}

	// Project doesn't exist
	if (container == null
		|| (container.getType() & (IResource.PROJECT | IResource.FOLDER)) == 0) {
	    updateStatus("Project container must exist");
	    identifier.setText("");
	    return;
	}

	// Project isn't writable
	if (!container.isAccessible()) {
	    updateStatus("Project must be writable");
	    identifier.setText("");
	    return;
	}

	// Check if it's an Android-Project
	try {
	    String appIdentifier = new AndroidManifestAdapter()
		    .getAppIdentifier(project + "/", "AndroidManifest.xml");
	    identifier.setText(appIdentifier);
	} catch (ParserConfigurationException e) {
	    updateStatus("Project must be an Android-Project");
	    return;
	} catch (SAXException e) {
	    updateStatus("Project must be an Android-Project");
	    return;
	} catch (IOException e) {
	    updateStatus("Project must be an Android-Project");
	    return;
	}

	// Assets folder is not specified at the path
	if (!getProjectName().endsWith(project + "/assets")) {
	    updateStatus("Assets folder must be specified at the root of your project");
	    return;
	}

	// Everything correct
	updateStatus(null);
    }

    /**
     * Sets the error message and if the page is complete
     * 
     * @param message
     *            <code>null</code> if the page is complete, else the error
     *            message to display
     */
    private void updateStatus(String message) {
	setErrorMessage(message);
	setPageComplete(message == null);
    }

    public String getProjectName() {
	return assetsFolderText.getText();
    }
}