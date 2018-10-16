package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.EventObject;
import java.util.Map;

import javax.swing.AbstractCellEditor;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;


public class PatientTable extends JTable implements TableCellRenderer{

	private static final long serialVersionUID = 9100680929990317995L;
	
	private static final int ROW_HEIGHT = 20;
	
	public PatientTable(PatientTableModel model){
		super(model);

		if(model == null)
			throw new IllegalArgumentException("model == null");
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setFillsViewportHeight(true);
		setRowHeight(ROW_HEIGHT);
		setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		setRowSelectionAllowed(false);
		setColumnSelectionAllowed(false);
		setCellSelectionEnabled(false);
		getTableHeader().setReorderingAllowed(false);
		setAutoCreateRowSorter(true); //TODO: is this causing sorter updated problem?
		
		setColumnRenderer(getColumnModel(), this);
		
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		TableCellRenderer r = null;

		//convert the view indices into the model
		row = row > -1 ? convertRowIndexToModel(row) : row;
		column = convertColumnIndexToModel(column);

		if(row > -1){
			r = getDefaultRenderer(value.getClass());
		}
		else{
			r = getTableHeader().getDefaultRenderer();
		}

		return r.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}
	
	
	/**
	 * Sets the column renderer for the specified model
	 * @param model - The column model
	 * @param renderer - The renderer to use
	 */
	private void setColumnRenderer(TableColumnModel model, TableCellRenderer renderer){
		int columnCount = model.getColumnCount();
		
		DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
		leftRenderer.setHorizontalAlignment( JLabel.LEFT );
		
		ClassLoader cl = this.getClass().getClassLoader();

		for(int i = 0; i < columnCount; i++){
			TableColumn column = model.getColumn(i);
			column.setCellRenderer(renderer);
			column.setHeaderRenderer(renderer);
			
			if(i == 0){
				column.setMaxWidth(55);
				column.setMinWidth(25);
				column.setHeaderRenderer(iconHeaderRenderer);
				column.setHeaderValue(new TextAndIcon("", new ImageIcon(cl.getResource("images/icon_delete_sm_02.png"))));
			}
			else if(i==1 || i==9 || i==10){
				column.setCellEditor(new NameCellEditor());
			}
			else if(i==2 || i==3 || i==4 || i==5){
				column.setCellRenderer(leftRenderer);
				column.setMinWidth(125);
				column.setMaxWidth(150);
				
				if(i==5){
					column.setCellEditor(new BiopsyCellEditor());
				}
				else{
					column.setCellEditor(new CategoryCellEditor());
				}
			}
			else if(i == 6){
				column.setCellEditor(new DateCellEditor());
			}
			else if(i == 7 || i == 8){
				column.setMaxWidth(0);
				column.setMinWidth(0);
				column.setPreferredWidth(0);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
	    {
	        Component c = super.prepareRenderer(renderer, row, column);
	        
	        if(getValueAt(row, 0) == Boolean.TRUE){
		        Map attributes = c.getFont().getAttributes();
		        attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
	
		        c.setForeground(Color.RED);
		        c.setFont(new Font(attributes));
	        }
	        else if(getValueAt(row, 7) == Boolean.TRUE){
	        	c.setForeground(Color.GREEN);
	        }
	        else if(getValueAt(row, 8) == Boolean.TRUE){
	        	c.setForeground(Color.ORANGE);
	        }
	        else{
	        	c.setForeground(Color.BLACK);
	        	c.setBackground(Color.WHITE);
	        }

	        return c;
	    }
	
	// This customized renderer can render objects of the type TextandIcon
	TableCellRenderer iconHeaderRenderer = new DefaultTableCellRenderer() {
	    /**
		 * 
		 */
		private static final long serialVersionUID = -159322684683608467L;

		public Component getTableCellRendererComponent(JTable table, Object value,
	            boolean isSelected, boolean hasFocus, int row, int column) {
	        // Inherit the colors and font from the header component
	        if (table != null) {
	            JTableHeader header = table.getTableHeader();
	            if (header != null) {
	                setForeground(header.getForeground());
	                setBackground(header.getBackground());
	                setFont(header.getFont());
	            }
	        }

	        if (value instanceof TextAndIcon) {
	            setIcon(((TextAndIcon)value).icon);
	            setText(((TextAndIcon)value).text);
	        } else {
	            setText((value == null) ? "" : value.toString());
	            setIcon(null);
	        }
	        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
	        setHorizontalAlignment(JLabel.CENTER);
	        return this;
	    }
	};
	
	class TextAndIcon {
	    TextAndIcon(String text, Icon icon) {
	        this.text = text;
	        this.icon = icon;
	    }
	    String text;
	    Icon icon;
	}
	
	public class NameCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 4534441505175661086L;

		Object oldVal;
		
		int clickCountToStart = 2;
		
		JComponent component = new JTextField();
		
		int rowIndex;
		
		JTable table;

	    public Component getTableCellEditorComponent(JTable table, Object value,
	        boolean isSelected, int rowIndex, int vColIndex) {
	    	
	    	oldVal = ((PatientTableModel) table.getModel()).getTrueValueAt(rowIndex, vColIndex);
	    	
	    	this.rowIndex = rowIndex;
	    	this.table = table;
	    	
			((JTextField)component).setText(value.toString());
			
	    	return component;
	    }

	    public Object getCellEditorValue() {
	    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to change this value?", "Edit value?", JOptionPane.YES_NO_OPTION);
	    	
    		if (reply == JOptionPane.YES_OPTION){
    			confirmEditState(table, rowIndex);
    			return ((JTextField)component).getText();
    		}
    		else{
    			return oldVal;
    		}
	    }
	    
	    public boolean isCellEditable(EventObject anEvent) {  
	        if (anEvent instanceof MouseEvent) {   
	        	return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;  
	        }  
	        return true;  
	    }  
	}
	
	public class CategoryCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 7115319103225729114L;

		Object oldVal;
		
		int clickCountToStart = 2;
		
		Integer [] categories = {-1, 0, 1, 2, 3, 4, 5, 6};
		
		JComponent component = new JComboBox<Integer>(categories);
		
		int rowIndex;
		
		JTable table;

	    @SuppressWarnings("unchecked")
		public Component getTableCellEditorComponent(JTable table, Object value,
	        boolean isSelected, int rowIndex, int vColIndex) {
	    	
	    	oldVal = ((PatientTableModel) table.getModel()).getTrueValueAt(rowIndex, vColIndex);
	    	
	    	this.rowIndex = rowIndex;
	    	this.table = table;
	    	
			((JComboBox<Integer>)component).setSelectedItem(value);
			
	    	return component;
	    }

	    @SuppressWarnings("unchecked")
		public Object getCellEditorValue() {
	    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to change this value?", "Edit value?", JOptionPane.YES_NO_OPTION);
	    	
    		if (reply == JOptionPane.YES_OPTION){
    			confirmEditState(table, rowIndex);
    			return ((JComboBox<Integer>)component).getSelectedItem();
    		}
    		else{
    			return oldVal;
    		}
	    }
	    
	    public boolean isCellEditable(EventObject anEvent) {  
	        if (anEvent instanceof MouseEvent) {   
	        	return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;  
	        }  
	        return true;  
	    }  
	}
	
	public class BiopsyCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 7115319103225729114L;

		Object oldVal;
		
		int clickCountToStart = 2;
		
		Integer [] categories = {-1, 0, 1, 2, 3, 4, 5, 6, 7, 8};
		
		JComponent component = new JComboBox<Integer>(categories);
		
		int rowIndex;
		
		JTable table;

	    @SuppressWarnings("unchecked")
		public Component getTableCellEditorComponent(JTable table, Object value,
	        boolean isSelected, int rowIndex, int vColIndex) {
	    	
	    	oldVal = ((PatientTableModel) table.getModel()).getTrueValueAt(rowIndex, vColIndex);
	    	
	    	this.rowIndex = rowIndex;
	    	this.table = table;
	    	
			((JComboBox<Integer>)component).setSelectedItem(value);
			
	    	return component;
	    }

	    @SuppressWarnings("unchecked")
		public Object getCellEditorValue() {
	    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to change this value?", "Edit value?", JOptionPane.YES_NO_OPTION);
	    	
    		if (reply == JOptionPane.YES_OPTION){
    			confirmEditState(table, rowIndex);
    			return ((JComboBox<Integer>)component).getSelectedItem();
    		}
    		else{
    			return oldVal;
    		}
	    }
	    
	    public boolean isCellEditable(EventObject anEvent) {  
	        if (anEvent instanceof MouseEvent) {   
	        	return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;  
	        }  
	        return true;  
	    }  
	}
	
	public class DateCellEditor extends AbstractCellEditor implements TableCellEditor {
		
		private static final long serialVersionUID = 6471647526288463493L;

		Object oldVal;
		
		int rowIndex;
		
		JTable table;
		
		int clickCountToStart = 2;
		
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		
		JComponent component = new JDatePickerImpl(datePanel);

	    public Component getTableCellEditorComponent(JTable table, Object value,
	        boolean isSelected, int rowIndex, int vColIndex) {
	    	
	    	oldVal = value;
	    	
	    	this.rowIndex = rowIndex;
	    	this.table = table;
	    	
	    	return component;
	    }

	    public Object getCellEditorValue() {
	    	int reply = JOptionPane.showConfirmDialog(null, "Are you sure you want to change this value?", "Edit value?", JOptionPane.YES_NO_OPTION);
	    	
    		if (reply == JOptionPane.YES_OPTION){
    			confirmEditState(table, rowIndex);
    			return ((JDatePickerImpl)component).getModel().getValue();
    		}
    		else{
    			return oldVal;
    		}
	    }
	    
	    public boolean isCellEditable(EventObject anEvent) {  
	        if (anEvent instanceof MouseEvent) {   
	        	return ((MouseEvent)anEvent).getClickCount() >= clickCountToStart;  
	        }  
	        return true;  
	    }  
	}
	
	private static void confirmEditState(JTable table, int rowIndex){
		int modelRow = table.convertRowIndexToModel(rowIndex);
		
		((PatientTableModel) table.getModel()).getRowAt(modelRow).setEdited(Boolean.TRUE);
	}
}
