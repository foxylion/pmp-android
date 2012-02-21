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

import java.util.HashMap;
import java.util.Locale;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import de.unistuttgart.ipvs.pmp.editor.model.Model;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.DescriptionContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.contentprovider.NameContentProvider;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.ais.internals.dialogs.ServiceFeatureDescriptionDialog;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Images;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TooltipTableListener;
import de.unistuttgart.ipvs.pmp.editor.xml.AISValidatorWrapper;
import de.unistuttgart.ipvs.pmp.xmlutil.ais.AISServiceFeature;
import de.unistuttgart.ipvs.pmp.xmlutil.common.ILocalizedString;
import de.unistuttgart.ipvs.pmp.xmlutil.common.LocalizedString;

/**
 * Shows the lists for the names and the descriptions of a service feature
 * 
 * @author Thorsten Berberich
 * 
 */
public class ServiceFeatureNameDetailsPage implements IDetailsPage {

	/**
	 * ID of this page
	 */
	public static final String ID = "ais_service_feature_names";

	/**
	 * Given form
	 */
	private IManagedForm form;

	/**
	 * {@link Shell} of the parent
	 */
	private Shell parentShell;

	/**
	 * {@link TableViewer} for the names table
	 */
	private TableViewer nameTableViewer;

	/**
	 * The columns of the names table
	 */
	private TableColumn nameColumn;
	private TableColumn localeNameColumn;

	/**
	 * {@link TableViewer} for the description table
	 */
	private TableViewer descriptionTableViewer;

	/**
	 * The columns of the names table
	 */
	private TableColumn descColumn;
	private TableColumn localeDescColumn;

	/**
	 * Action of the remove description
	 */
	private Action removeDesc;

	/**
	 * The remove name action
	 */
	private Action removeName;

	/**
	 * The displayed ServiceFeature
	 */
	AISServiceFeature displayed;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.IFormPart#initialize(org.eclipse.ui.forms.IManagedForm
	 * )
	 */
	@Override
	public void initialize(IManagedForm arg0) {
		this.form = arg0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createContents(Composite parent) {
		this.parentShell = parent.getShell();

		// Set parent's layout
		GridData parentLayout = new GridData();
		parentLayout.verticalAlignment = GridData.FILL;
		parentLayout.grabExcessVerticalSpace = true;
		parentLayout.horizontalAlignment = GridData.FILL;
		parentLayout.grabExcessHorizontalSpace = true;
		parent.setLayout(new GridLayout(1, false));

		// Attributes section
		FormToolkit toolkit = this.form.getToolkit();

		// The name section
		Section nameSection = toolkit.createSection(parent,
				ExpandableComposite.CLIENT_INDENT
						| ExpandableComposite.TITLE_BAR);
		nameSection.setText("Names");
		nameSection.setLayout(new GridLayout(1, false));
		nameSection.setExpanded(true);
		nameSection.setLayoutData(parentLayout);
		createNameSectionAttributeToolbar(nameSection);

		// The description section
		Section descriptionSection = toolkit.createSection(parent,
				ExpandableComposite.CLIENT_INDENT
						| ExpandableComposite.TITLE_BAR);
		descriptionSection.setText("Descriptions");
		descriptionSection.setLayout(new GridLayout(1, false));
		descriptionSection.setExpanded(true);
		descriptionSection.setLayoutData(parentLayout);
		createDescriptionSectionAttributeToolbar(descriptionSection);

		// Composite that is display in the description section
		Composite descriptionComposite = toolkit
				.createComposite(descriptionSection);
		descriptionComposite.setLayout(new GridLayout(1, false));
		descriptionComposite.setLayoutData(parentLayout);
		createDescriptionTable(descriptionComposite, toolkit);

		// Composite that is display in the name section
		Composite nameComposite = toolkit.createComposite(nameSection);
		nameComposite.setLayout(new GridLayout(1, false));
		nameComposite.setLayoutData(parentLayout);
		createNameTable(nameComposite, toolkit);

		// Set the composites
		descriptionSection.setClient(descriptionComposite);
		nameSection.setClient(nameComposite);
	}

	private TableViewer createNameTable(Composite parent, FormToolkit toolkit) {
		// Use grid layout so that the table uses the whole screen width
		final GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.verticalAlignment = GridData.FILL;
		layoutData.grabExcessVerticalSpace = true;

		// Workaround for SWT-Bug needed
		// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		layoutData.widthHint = 1;

		this.nameTableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);
		this.nameTableViewer.setContentProvider(new NameContentProvider());

		// Disable the default tool tips
		this.nameTableViewer.getTable().setToolTipText("");

		TooltipTableListener tooltipListener = new TooltipTableListener(
				this.nameTableViewer, this.parentShell);

		this.nameTableViewer.getTable().addListener(SWT.Dispose,
				tooltipListener);
		this.nameTableViewer.getTable().addListener(SWT.KeyDown,
				tooltipListener);
		this.nameTableViewer.getTable().addListener(SWT.MouseMove,
				tooltipListener);
		this.nameTableViewer.getTable().addListener(SWT.MouseHover,
				tooltipListener);

		this.nameTableViewer.getTable().addSelectionListener(
				new SelectionListener() {

					@Override
					public void widgetSelected(SelectionEvent arg0) {
						if (ServiceFeatureNameDetailsPage.this.nameTableViewer
								.getTable().getSelectionCount() > 0) {
							ServiceFeatureNameDetailsPage.this.removeName
									.setEnabled(true);
						} else {
							ServiceFeatureNameDetailsPage.this.removeName
									.setEnabled(false);
						}
					}

					@Override
					public void widgetDefaultSelected(SelectionEvent arg0) {
					}
				});

		// The locale column with the LabelProvider
		TableViewerColumn localeColumn = new TableViewerColumn(
				this.nameTableViewer, SWT.NULL);
		localeColumn.getColumn().setText("Locale");
		localeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((LocalizedString) element).getLocale().toString();
			}

			@Override
			public Image getImage(Object element) {
				ILocalizedString item = (LocalizedString) element;
				if (!item.getIssues().isEmpty()) {
					return Images.ERROR16;
				}
				return null;
			}
		});

		this.localeNameColumn = localeColumn.getColumn();

		// The description column with the LabelProvider
		TableViewerColumn nameColumn = new TableViewerColumn(
				this.nameTableViewer, SWT.NULL);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((LocalizedString) element).getString();
			}
		});

		this.nameColumn = nameColumn.getColumn();

		// Define the table's view
		Table descriptionTable = this.nameTableViewer.getTable();
		descriptionTable.setLayoutData(layoutData);
		descriptionTable.setHeaderVisible(true);
		descriptionTable.setLinesVisible(true);

		// Add the double click listener
		this.nameTableViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent arg0) {
				if (ServiceFeatureNameDetailsPage.this.nameTableViewer
						.getTable().getSelectionCount() == 1) {
					LocalizedString oldName = (LocalizedString) ServiceFeatureNameDetailsPage.this.nameTableViewer
							.getTable().getSelection()[0].getData();

					// Show the dialog
					HashMap<String, String> values = new HashMap<String, String>();
					ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
							ServiceFeatureNameDetailsPage.this.parentShell,
							oldName.getLocale().toString(),
							oldName.getString(), values, "Name", "Change");
					if (dialog.open() == Window.OK) {
						String newName = values.get("Name");
						String newLocale = values.get("locale");

						// Sth. has changed
						if (!(newName.equals(oldName.getString()) && newLocale
								.equals(oldName.getLocale().toString()))) {
							ServiceFeatureNameDetailsPage.this.nameTableViewer
									.getTable().setRedraw(false);

							Locale locale = new Locale(newLocale);

							oldName.setLocale(locale);
							oldName.setString(newName);

							Model.getInstance().setAISDirty(true);
							AISValidatorWrapper
									.getInstance()
									.validateServiceFeature(
											ServiceFeatureNameDetailsPage.this.displayed,
											true);
							ServiceFeatureMasterBlock.refreshTree();
							ServiceFeatureNameDetailsPage.this.nameTableViewer
									.refresh();

							// Redraw the table
							ServiceFeatureNameDetailsPage.this.nameColumn
									.pack();
							ServiceFeatureNameDetailsPage.this.localeNameColumn
									.pack();

							ServiceFeatureNameDetailsPage.this.nameTableViewer
									.getTable().redraw();
							ServiceFeatureNameDetailsPage.this.nameTableViewer
									.getTable().setRedraw(true);
						}
					}
				}

			}
		});

		return this.nameTableViewer;
	}

	public TableViewer createDescriptionTable(Composite parent,
			FormToolkit toolkit) {
		// Use grid layout so that the table uses the whole screen width
		final GridData layoutData = new GridData();
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.verticalAlignment = GridData.FILL;
		layoutData.grabExcessVerticalSpace = true;

		// Workaround for SWT-Bug needed
		// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		layoutData.widthHint = 1;

		this.descriptionTableViewer = new TableViewer(parent, SWT.BORDER
				| SWT.FULL_SELECTION | SWT.MULTI);

		TooltipTableListener descTooltipListener = new TooltipTableListener(
				this.descriptionTableViewer, this.parentShell);

		// Disable the default tool tips
		this.descriptionTableViewer.getTable().setToolTipText("");

		this.descriptionTableViewer.getTable().addListener(SWT.Dispose,
				descTooltipListener);
		this.descriptionTableViewer.getTable().addListener(SWT.KeyDown,
				descTooltipListener);
		this.descriptionTableViewer.getTable().addListener(SWT.MouseMove,
				descTooltipListener);
		this.descriptionTableViewer.getTable().addListener(SWT.MouseHover,
				descTooltipListener);

		this.descriptionTableViewer
				.setContentProvider(new DescriptionContentProvider());

		// The locale column with the LabelProvider
		TableViewerColumn localeColumn = new TableViewerColumn(
				this.descriptionTableViewer, SWT.NULL);
		localeColumn.getColumn().setText("Locale");
		localeColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((LocalizedString) element).getLocale().toString();
			}
		});

		this.localeDescColumn = localeColumn.getColumn();

		// The name column with the LabelProvider
		TableViewerColumn descColumn = new TableViewerColumn(
				this.descriptionTableViewer, SWT.NULL);
		descColumn.getColumn().setText("Description");
		descColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((LocalizedString) element).getString();
			}
		});

		this.descColumn = descColumn.getColumn();

		// Define the table's view
		Table descriptionTable = this.descriptionTableViewer.getTable();
		descriptionTable.setLayoutData(layoutData);
		descriptionTable.setHeaderVisible(true);
		descriptionTable.setLinesVisible(true);

		descriptionTable.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if (ServiceFeatureNameDetailsPage.this.descriptionTableViewer
						.getTable().getSelectionCount() > 0) {
					ServiceFeatureNameDetailsPage.this.removeDesc
							.setEnabled(true);
				} else {
					ServiceFeatureNameDetailsPage.this.removeDesc
							.setEnabled(false);
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});

		// Add the double click listener
		this.descriptionTableViewer
				.addDoubleClickListener(new IDoubleClickListener() {

					@Override
					public void doubleClick(DoubleClickEvent arg0) {
						if (ServiceFeatureNameDetailsPage.this.descriptionTableViewer
								.getTable().getSelectionCount() == 1) {
							LocalizedString oldDesc = (LocalizedString) ServiceFeatureNameDetailsPage.this.descriptionTableViewer
									.getTable().getSelection()[0].getData();

							// Show the dialog
							HashMap<String, String> values = new HashMap<String, String>();
							ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
									ServiceFeatureNameDetailsPage.this.parentShell,
									oldDesc.getLocale().toString(), oldDesc
											.getString(), values,
									"Description", "Change");
							if (dialog.open() == Window.OK) {
								String newDesc = values.get("Description");
								String newLocale = values.get("locale");

								// Sth. has changed
								if (!(newDesc.equals(oldDesc.getString()) && newLocale
										.equals(oldDesc.getLocale().toString()))) {
									ServiceFeatureNameDetailsPage.this.descriptionTableViewer
											.getTable().setRedraw(false);

									Locale locale = new Locale(newLocale);

									oldDesc.setLocale(locale);
									oldDesc.setString(newDesc);

									Model.getInstance().setAISDirty(true);
									AISValidatorWrapper
											.getInstance()
											.validateServiceFeature(
													ServiceFeatureNameDetailsPage.this.displayed,
													true);
									ServiceFeatureMasterBlock.refreshTree();

									ServiceFeatureNameDetailsPage.this.descriptionTableViewer
											.refresh();

									// Redraw the table
									ServiceFeatureNameDetailsPage.this.descColumn
											.pack();
									ServiceFeatureNameDetailsPage.this.localeDescColumn
											.pack();

									ServiceFeatureNameDetailsPage.this.descriptionTableViewer
											.getTable().setRedraw(true);
									ServiceFeatureNameDetailsPage.this.descriptionTableViewer
											.getTable().redraw();
								}
							}
						}
					}
				});

		return this.descriptionTableViewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.forms.IPartSelectionListener#selectionChanged(org.eclipse
	 * .ui.forms.IFormPart, org.eclipse.jface.viewers.ISelection)
	 */
	@Override
	public void selectionChanged(IFormPart arg0, ISelection selection) {
		// Disable redrawing of the table
		this.nameTableViewer.getTable().setRedraw(false);
		this.descriptionTableViewer.getTable().setRedraw(false);

		// Get the selected service feature and set the name
		TreePath[] path = ((TreeSelection) selection).getPaths();
		this.displayed = (AISServiceFeature) path[0].getFirstSegment();
		this.nameTableViewer.setInput(this.displayed);
		this.localeNameColumn.pack();
		this.nameColumn.pack();

		/*
		 * Disable the remove buttons because if the selection was changed no
		 * line is selected
		 */
		this.removeName.setEnabled(false);
		this.removeDesc.setEnabled(false);

		// Set the description
		this.descriptionTableViewer.setInput(this.displayed);
		this.descColumn.pack();
		this.localeDescColumn.pack();

		// Enable redrawing
		this.nameTableViewer.getTable().setRedraw(true);
		this.descriptionTableViewer.getTable().setRedraw(true);
	}

	/**
	 * Adds the toolbar with the remove and add buttons to the name section
	 * 
	 * @param section
	 *            {@link Section} to set the toolbar
	 */
	private void createNameSectionAttributeToolbar(Section section) {
		// Create the toolbar
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(section);

		final Cursor handCursor = new Cursor(Display.getCurrent(),
				SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		toolbar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if ((handCursor != null) && (handCursor.isDisposed() == false)) {
					handCursor.dispose();
				}
			}
		});

		// Picture can be added also to the actions
		Action add = new Action("Add") {

			@Override
			public void run() {
				HashMap<String, String> values = new HashMap<String, String>();
				ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
						ServiceFeatureNameDetailsPage.this.parentShell, null,
						null, values, "Name", "Add");
				if (dialog.open() == Window.OK) {
					ServiceFeatureNameDetailsPage.this.nameTableViewer
							.getTable().setRedraw(false);

					// Get the values
					String stringLocale = values.get("locale");
					String stringName = values.get("Name");

					// Create the things for the model
					Locale locale = new Locale(stringLocale);

					LocalizedString name = new LocalizedString();
					name.setLocale(locale);
					name.setString(stringName);

					ServiceFeatureNameDetailsPage.this.displayed.addName(name);

					Model.getInstance().setAISDirty(true);
					AISValidatorWrapper.getInstance().validateServiceFeature(
							ServiceFeatureNameDetailsPage.this.displayed, true);
					ServiceFeatureMasterBlock.refreshTree();

					ServiceFeatureNameDetailsPage.this.nameTableViewer
							.refresh();

					ServiceFeatureNameDetailsPage.this.nameColumn.pack();
					ServiceFeatureNameDetailsPage.this.localeNameColumn.pack();

					ServiceFeatureNameDetailsPage.this.nameTableViewer
							.getTable().redraw();
					ServiceFeatureNameDetailsPage.this.nameTableViewer
							.getTable().setRedraw(true);
				}
			}
		};
		add.setToolTipText("Add a new name for the Service Feature");

		// The remove action
		this.removeName = new Action("Remove") {

			@Override
			public void run() {
				ServiceFeatureNameDetailsPage.this.nameTableViewer.getTable()
						.setRedraw(false);
				TableItem[] selection = ServiceFeatureNameDetailsPage.this.nameTableViewer
						.getTable().getSelection();

				// Delete it out of the model
				for (TableItem item : selection) {
					ServiceFeatureNameDetailsPage.this.displayed
							.removeName((LocalizedString) item.getData());
				}
				ServiceFeatureNameDetailsPage.this.nameColumn.pack();
				ServiceFeatureNameDetailsPage.this.localeNameColumn.pack();
				AISValidatorWrapper.getInstance().validateServiceFeature(
						ServiceFeatureNameDetailsPage.this.displayed, true);
				ServiceFeatureMasterBlock.refreshTree();

				ServiceFeatureNameDetailsPage.this.nameTableViewer.getTable()
						.setRedraw(true);
				ServiceFeatureNameDetailsPage.this.nameTableViewer.refresh();
				Model.getInstance().setAISDirty(true);
			}
		};
		this.removeName.setToolTipText("Remove the selected names");
		this.removeName.setEnabled(false);

		// Add the actions to the toolbar
		toolBarManager.add(add);
		toolBarManager.add(this.removeName);

		toolBarManager.update(true);
		section.setTextClient(toolbar);
	}

	/**
	 * Adds the toolbar with the remove and add buttons to the description
	 * section
	 * 
	 * @param section
	 *            {@link Section} to set the toolbar
	 */
	private void createDescriptionSectionAttributeToolbar(Section section) {
		// Create the toolbar
		ToolBarManager toolBarManager = new ToolBarManager(SWT.FLAT);
		ToolBar toolbar = toolBarManager.createControl(section);

		final Cursor handCursor = new Cursor(Display.getCurrent(),
				SWT.CURSOR_HAND);
		toolbar.setCursor(handCursor);
		toolbar.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				if ((handCursor != null) && (handCursor.isDisposed() == false)) {
					handCursor.dispose();
				}
			}
		});

		// Picture can be added also to the actions
		Action add = new Action("Add") {

			@Override
			public void run() {
				HashMap<String, String> values = new HashMap<String, String>();
				ServiceFeatureDescriptionDialog dialog = new ServiceFeatureDescriptionDialog(
						ServiceFeatureNameDetailsPage.this.parentShell, null,
						null, values, "Description", "Add");
				if (dialog.open() == Window.OK) {
					ServiceFeatureNameDetailsPage.this.descriptionTableViewer
							.getTable().setRedraw(false);

					// Get the values
					String stringLocale = values.get("locale");
					String stringDesc = values.get("Description");

					// Create the things for the model
					Locale locale = new Locale(stringLocale);

					LocalizedString desc = new LocalizedString();
					desc.setLocale(locale);
					desc.setString(stringDesc);

					ServiceFeatureNameDetailsPage.this.displayed
							.addDescription(desc);

					Model.getInstance().setAISDirty(true);
					AISValidatorWrapper.getInstance().validateServiceFeature(
							ServiceFeatureNameDetailsPage.this.displayed, true);
					ServiceFeatureMasterBlock.refreshTree();

					ServiceFeatureNameDetailsPage.this.descriptionTableViewer
							.refresh();

					ServiceFeatureNameDetailsPage.this.descColumn.pack();
					ServiceFeatureNameDetailsPage.this.localeDescColumn.pack();

					ServiceFeatureNameDetailsPage.this.descriptionTableViewer
							.getTable().redraw();
					ServiceFeatureNameDetailsPage.this.descriptionTableViewer
							.getTable().setRedraw(true);
				}
			}
		};
		add.setToolTipText("Add a new description for the Service Feature");

		// The remove action
		this.removeDesc = new Action("Remove") {

			@Override
			public void run() {
				ServiceFeatureNameDetailsPage.this.descriptionTableViewer
						.getTable().setRedraw(false);
				TableItem[] selections = ServiceFeatureNameDetailsPage.this.descriptionTableViewer
						.getTable().getSelection();

				// Delete it out of the model
				for (TableItem item : selections) {
					ServiceFeatureNameDetailsPage.this.displayed
							.removeDescription((LocalizedString) item.getData());
				}

				ServiceFeatureNameDetailsPage.this.descColumn.pack();
				ServiceFeatureNameDetailsPage.this.localeDescColumn.pack();

				ServiceFeatureNameDetailsPage.this.descriptionTableViewer
						.getTable().setRedraw(true);
				AISValidatorWrapper.getInstance().validateServiceFeature(
						ServiceFeatureNameDetailsPage.this.displayed, true);
				ServiceFeatureMasterBlock.refreshTree();

				ServiceFeatureNameDetailsPage.this.descriptionTableViewer
						.refresh();
				Model.getInstance().setAISDirty(true);
			}
		};
		this.removeDesc.setToolTipText("Remove the selected descriptions");
		this.removeDesc.setEnabled(false);

		// Add the actions to the toolbar
		toolBarManager.add(add);
		toolBarManager.add(this.removeDesc);

		toolBarManager.update(true);
		section.setTextClient(toolbar);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#commit(boolean)
	 */
	@Override
	public void commit(boolean arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return Model.getInstance().isAisDirty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#isStale()
	 */
	@Override
	public boolean isStale() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#refresh()
	 */
	@Override
	public void refresh() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IFormPart#setFormInput(java.lang.Object)
	 */
	@Override
	public boolean setFormInput(Object arg0) {
		return false;
	}
}
