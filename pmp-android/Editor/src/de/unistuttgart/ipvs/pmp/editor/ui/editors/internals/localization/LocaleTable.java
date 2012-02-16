package de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.localization;

import java.util.Locale;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.Information;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.LocaleEditingSupport;
import de.unistuttgart.ipvs.pmp.editor.ui.editors.internals.TextEditingSupport;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.AbstractLocale;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.BasicIS;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Description;
import de.unistuttgart.ipvs.pmp.xmlutil.common.informationset.Name;

public class LocaleTable {
	
	private final BasicIS data;
	private final Composite parent;
	private final Composite outerCompo;
	private final Type type;
	private final TableViewer tableViewer;
	private boolean dirty = false;
	public enum Type {NAME, LOCALE};
	
	public LocaleTable(Composite parent, BasicIS data, Type type, FormToolkit toolkit) {
		this.parent = parent;
		this.data = data;
		this.type = type;
		
		// Create grid for storing the table and buttons
		outerCompo = toolkit.createComposite(parent);
		outerCompo.setLayout(new GridLayout(2, false));
		
		// Set layout so that the table uses the whole width
		GridData tableLayout = new GridData();
		tableLayout.horizontalAlignment = GridData.FILL;
		tableLayout.grabExcessHorizontalSpace = true;

		// Workaround for SWT-Bug
		// (https://bugs.eclipse.org/bugs/show_bug.cgi?id=215997)
		tableLayout.widthHint = 1;
		
		Composite tableCompo = new Composite(outerCompo, SWT.NONE);
		tableCompo.setLayoutData(tableLayout);
		TableColumnLayout columnLayout = new TableColumnLayout();
		tableCompo.setLayout(columnLayout);
		
		tableViewer = new TableViewer(tableCompo, SWT.BORDER | SWT.FULL_SELECTION);
		Table table = tableViewer.getTable();
		table.setLayoutData(tableLayout);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		buildColumns(columnLayout);
		
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setInput(data);
		
	}
	
	private void buildColumns(TableColumnLayout columnLayout) {
		
		ColumnLabelProvider localeLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((AbstractLocale) element).getLocale().getLanguage();
			}
		};
		
		EditingSupport localeEditing = new LocaleEditingSupport(tableViewer) {
			
			@Override
			protected Object getValue(Object element) {
				return ((AbstractLocale)element).getLocale();
			}
			
			@Override
			protected void setValue(Object element, Object value) {
				AbstractLocale al = ((AbstractLocale)element);
				String input = ((String)value).toLowerCase();
				
				// Mark as dirty, when the locale has been edited
				if (!al.getLocale().getLanguage().equals(input)) {
					Locale locale = new Locale(input);
					((AbstractLocale)element).setLocale(locale);
					dirty = true;
				}
			}
		};
		
		buildColumn("Locale", 100, localeLabel, localeEditing, columnLayout);
		
		ColumnLabelProvider valueLabel = new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (type == Type.NAME) {
					return ((Name)element).getName();
				} else {
					return ((Description)element).getDescription();
				}
			}
		};
		
		EditingSupport valueEditing = new TextEditingSupport(tableViewer) {

			@Override
			protected Object getValue(Object element) {
				if (type == Type.NAME) {
					return ((Name)element).getName();
				} else {
					return ((Description)element).getDescription();
				}
			}

			@Override
			protected void setValue(Object element, Object value) {
				String input = (String)value;
				
				if (type == Type.NAME) {
					Name name = (Name)element;
					if (!name.getName().equals(input)) {
						name.setName(input);
						dirty = true;
					}
				} else {
					Description desc = (Description)element;
					if (!desc.equals(desc)) {
						desc.setDescription(input);
						dirty = true;
					}
				}
			}
			
		};
		
		String valueTitle;
		if (type == Type.NAME) {
			valueTitle = "Name";
		} else {
			valueTitle = "Description";
		}
		
		buildColumn(valueTitle, 0, valueLabel, valueEditing, columnLayout);
	}
	
	/**
	 * Build a column using the given data
	 * @param text	Title of the column
	 * @param width	Width of the column in pixel. 
	 * 				If set to 0 or a negative value, the column will use all available width
	 * @param labelProvider
	 * @param editingSupport
	 * @param columnLayout
	 */
	private void buildColumn(String text, int width, CellLabelProvider labelProvider, EditingSupport editingSupport, TableColumnLayout columnLayout) {
		TableViewerColumn column = new TableViewerColumn(tableViewer, SWT.BORDER);
		TableColumn control = column.getColumn();
		if (width <= 0) {
			columnLayout.setColumnData(control,	new ColumnWeightData(1,true));			
		} else {
			columnLayout.setColumnData(control,	new ColumnPixelData(25,true));
		}
		control.setText(text);
		column.setLabelProvider(labelProvider);
		column.setEditingSupport(editingSupport);
		
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public Composite getComposite() {
		return outerCompo;
	}
	
	/**
	 * Refreshes the table so that current values from the model will be shown
	 */
	public void refresh() {
		tableViewer.refresh();
		parent.getParent().layout();
	}

}
