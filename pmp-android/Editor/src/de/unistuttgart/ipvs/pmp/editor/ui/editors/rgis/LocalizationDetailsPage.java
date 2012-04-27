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
package de.unistuttgart.ipvs.pmp.editor.ui.editors.rgis;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.ILocaleTableAction;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localetable.LocaleTable;
import de.unistuttgart.ipvs.pmp.editor.util.I18N;
import de.unistuttgart.ipvs.pmp.editor.xml.RGISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.rgis.RGISPrivacySetting;

/**
 * Defines the details page of the privacy setting that allows the user to set
 * the privacy setting's localization
 * 
 * @author Patrick Strobel
 */
public class LocalizationDetailsPage implements IDetailsPage {
    
    private final PrivacySettingsBlock block;
    private IManagedForm form;
    private RGISPrivacySetting privacySetting;
    private LocaleTable localeTable;
    private final LocaleTable.Type type;
    
    
    public LocalizationDetailsPage(PrivacySettingsBlock block, LocaleTable.Type type) {
        this.block = block;
        this.type = type;
    }
    
    
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
        
        // Build view
        FormToolkit toolkit = this.form.getToolkit();
        Section section = toolkit.createSection(parent, ExpandableComposite.TITLE_BAR | Section.DESCRIPTION);
        section.setText(I18N.general_localization);
        section.setDescription(I18N.editor_rgis_ps_localization_description);
        section.setExpanded(true);
        section.setLayoutData(parentLayout);
        
        // Build localization table
        ILocaleTableAction dirtyAction = new ILocaleTableAction() {
            
            @Override
            public void doSetDirty(boolean dirty) {
                LocalizationDetailsPage.this.block.getModel().setDirty(true);
                
            }
            
            
            @Override
            public void doValidate() {
                RGISValidatorWrapper validator = RGISValidatorWrapper.getInstance();
                validator.validateRGIS(LocalizationDetailsPage.this.block.getModel().getRgis(), true);
                LocalizationDetailsPage.this.block.refresh();
            }
            
        };
        this.localeTable = new LocaleTable(section, this.type, dirtyAction, toolkit);
        // localizationTable = new InformationTable(section,
        // new StoredInformation(), toolkit);
        section.setClient(this.localeTable.getComposite());
        
    }
    
    
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean isDirty() {
        return false;
    }
    
    
    @Override
    public void commit(boolean onSave) {
        this.block.refresh();
    }
    
    
    @Override
    public boolean setFormInput(Object input) {
        return false;
    }
    
    
    @Override
    public void setFocus() {
        // TODO Auto-generated method stub
        
    }
    
    
    @Override
    public boolean isStale() {
        return false;
    }
    
    
    @Override
    public void refresh() {
        
    }
    
    
    @Override
    public void selectionChanged(IFormPart part, ISelection selection) {
        // Get parent element (PS-Object)
        TreePath[] path = ((TreeSelection) selection).getPaths();
        
        // This happens if the user deletes the default name/desc
        if (path.length < 1) {
            return;
        }
        this.privacySetting = (RGISPrivacySetting) path[0].getFirstSegment();
        
        this.localeTable.setData(this.privacySetting);
        this.localeTable.refresh();
    }
    
}
