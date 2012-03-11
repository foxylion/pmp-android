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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.ais;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AndroidApplicationException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.AppIdentifierNotFoundException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.NoMainActivityException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPActivityAlreadyExistsException;
import de.unistuttgart.ipvs.pmp.editor.exceptions.androidmanifestparser.PMPServiceAlreadyExists;
import de.unistuttgart.ipvs.pmp.editor.model.AisModel;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.AisEditor;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.ILocaleTableAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.LocaleTable.Type;
import de.unistuttgart.ipvs.pmp.editor.util.AndroidManifestAdapter;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.IAIS;

/**
 * Creates the "General" page for the {@link AisEditor}
 * 
 * @author Thorsten Berberich
 * 
 */
public class AISGeneralPage extends FormPage implements SelectionListener {
    
    /**
     * ID of this page
     */
    private static final String ID = "ais_general";
    
    /**
     * Path to the project that is opened
     */
    private String PROJECT_PATH;
    
    /**
     * The model of this editor instance
     */
    private final AisModel model;
    
    /**
     * Android manifest file name
     */
    private static String MANIFTEST = "AndroidManifest.xml";
    
    
    /**
     * Creates the general page
     * 
     * @param editor
     *            {@link FormEditor}
     * @param path
     *            the project path
     * @param model
     *            the {@link AisModel} of this instance
     */
    public AISGeneralPage(FormEditor editor, String path, AisModel model) {
        super(editor, ID, "General");
        this.model = model;
        this.PROJECT_PATH = path;
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
    
    
    /**
     * Adds the properties section to the page
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     */
    private void addPropertiesSection(Composite parent, FormToolkit toolkit) {
        // Set the section's parameters
        Section section = createSectionWithDescription(parent, "AndroidManifest.xml functions", toolkit,
                "Add helpful parts to the AndroidManifest.xml of the project");
        
        // Create elements stored inside this section
        Composite client = toolkit.createComposite(section, SWT.WRAP);
        
        client.setLayout(new GridLayout(2, false));
        
        Button pmpReg = toolkit.createButton(client, "Add PMP Registration activity...", SWT.PUSH);
        pmpReg.setImage(Images.IMG_OBJ_ADD);
        
        Button service = toolkit.createButton(client, "Add PMP service...", SWT.PUSH);
        service.setImage(Images.IMG_OBJ_ADD);
        pmpReg.addSelectionListener(this);
        service.addSelectionListener(this);
        
        section.setClient(client);
    }
    
    
    /**
     * Adds the localization section to the page with the table
     * 
     * @param parent
     *            parent {@link Composite}
     * @param toolkit
     *            {@link FormToolkit}
     */
    private void addLocalizationSection(Composite parent, FormToolkit toolkit) {
        // Set the section's parameters
        Section section = createSection(parent, "Localization", toolkit);
        
        // Create elements stored inside this section
        Composite client = toolkit.createComposite(section);
        
        client.setLayout(new GridLayout(2, false));
        
        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.verticalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        layoutData.grabExcessVerticalSpace = true;
        
        client.setLayoutData(layoutData);
        section.setLayoutData(layoutData);
        
        IAIS ais = this.model.getAis();
        ILocaleTableAction dirtyAction = new ILocaleTableAction() {
            
            @Override
            public void doSetDirty(boolean dirty) {
                AISGeneralPage.this.model.setDirty(true);
            }
            
            
            @Override
            public void doValidate() {
                AISValidatorWrapper.getInstance().validateAIS(AISGeneralPage.this.model.getAis(), true);
            }
        };
        LocaleTable nameTable = new LocaleTable(client, ais, Type.NAME, dirtyAction, toolkit);
        nameTable.getComposite().setLayoutData(layoutData);
        
        LocaleTable descTable = new LocaleTable(client, ais, Type.DESCRIPTION, dirtyAction, toolkit);
        descTable.getComposite().setLayoutData(layoutData);
        
        section.setClient(client);
    }
    
    
    /**
     * Creates a default section which spans over the whole editor
     * 
     * @param parent
     *            parent {@link Composite}
     * @param title
     *            the title
     * @param toolkit
     *            {@link FormToolkit}
     * @param desc
     *            description of the Section
     * @return the created Section
     */
    private Section createSectionWithDescription(Composite parent, String title, FormToolkit toolkit, String desc) {
        Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR
                | Section.DESCRIPTION);
        section.setDescription(desc);
        section.setText(title);
        section.setExpanded(true);
        
        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        
        section.setLayoutData(layoutData);
        
        return section;
    }
    
    
    /**
     * Creates a default section which spans over the whole editor
     * 
     * @param parent
     *            parent Composite
     * @param title
     *            title of the section
     * @param toolkit
     *            {@link FormToolkit}
     * @return the creatde Section
     */
    private Section createSection(Composite parent, String title, FormToolkit toolkit) {
        Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR);
        section.setText(title);
        section.setExpanded(true);
        
        GridData layoutData = new GridData();
        layoutData.horizontalAlignment = GridData.FILL;
        layoutData.grabExcessHorizontalSpace = true;
        
        section.setLayoutData(layoutData);
        
        return section;
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
     * .swt.events.SelectionEvent)
     */
    @Override
    public void widgetDefaultSelected(SelectionEvent arg0) {
        // TODO Auto-generated method stub
        
    }
    
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt
     * .events.SelectionEvent)
     */
    @Override
    public void widgetSelected(SelectionEvent event) {
        if (event.getSource() instanceof Button) {
            AndroidManifestAdapter adapter = new AndroidManifestAdapter();
            Button clicked = (Button) event.getSource();
            
            /*
             * Add the activity or the service to the AndroidManifestxml and /
             * show the error warnings
             */
            if (clicked.getText().equals("Add PMP Registration activity...")) {
                try {
                    adapter.addPMPActivityToManifest(this.PROJECT_PATH, MANIFTEST);
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Success",
                            "The PMP Registration activity was successfully added to the AndroidManifest.xml.");
                } catch (FileNotFoundException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "AndroidManifest.xml was not found.", status);
                } catch (ParserConfigurationException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Parser configuration exception.", status);
                } catch (SAXException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the activity.", status);
                } catch (IOException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the activity.", status);
                } catch (TransformerFactoryConfigurationError e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the activity.", status);
                } catch (TransformerException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the activity.", status);
                } catch (PMPActivityAlreadyExistsException e) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information",
                            "The PMP activity is already declared.");
                } catch (NoMainActivityException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error", "No main activity declared.",
                            status);
                } catch (AppIdentifierNotFoundException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "The <application> node was not found.", status);
                }
            } else {
                try {
                    adapter.addPMPServiceToManifest(this.PROJECT_PATH, MANIFTEST);
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Success",
                            "The PMP Service was successfully added to the AndroidManifest.xml.");
                    
                } catch (FileNotFoundException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "AndroidManifest.xml was not found.", status);
                } catch (DOMException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (ParserConfigurationException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (SAXException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (IOException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (AndroidApplicationException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "More than one <application> node found.", status);
                } catch (TransformerFactoryConfigurationError e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (TransformerException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "Could not add the PMP service.", status);
                } catch (PMPServiceAlreadyExists e) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Information",
                            "The PMP service is already declared.");
                } catch (AppIdentifierNotFoundException e) {
                    IStatus status = new Status(IStatus.ERROR, ID, "See details", e);
                    ErrorDialog.openError(Display.getDefault().getActiveShell(), "Error",
                            "The <application> node was not found.", status);
                }
            }
        }
    }
}
