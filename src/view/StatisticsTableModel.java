package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.CategoryStats;
import model.Patient;



public class StatisticsTableModel extends AbstractTableModel{
	
	private static final long serialVersionUID = -5722147852749124699L;
	
	public static final int CATEGORY_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	public static final int SCREENING_COLUMN = 2;
	public static final int DIAGNOSTIC_COLUMN = 3;
	public static final int UNDIFFERENTIATED_COLUMN = 4;

	private static final String[] COLUMNS = {"BI-RADS Category", "Final Assessment Category", "Screening", "Diagnostic", "Undifferentiated"};
	private static final String[] CATEGORY_DESCRIPTIONS = {"Incomplete",
												"Negative",
												"Benign Finding(s)",
												"Probably Benign Finding",
												"Suspicious Abnormality",
												"Highly Suggestive of Malignancy",
												"Known Biopsy-Proven Malignancy"};
	
	private List<Patient> rows;
	
	private CategoryStats stats;
	
	/**
	 * Create a new empty file upload table model
	 */
	public StatisticsTableModel(){
		rows = new ArrayList<Patient>();
		stats = new CategoryStats();
//		stats.calcCatStats(rows);
	}

	public List<Patient> getRows() {
		return rows;
	}

	public void setRows(List<Patient> rows) {
		this.rows = rows;
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public int getRowCount() {
		return 7;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		
		if(columnIndex == CATEGORY_COLUMN)
			value = rowIndex;
		else if(columnIndex == DESCRIPTION_COLUMN)
			value = CATEGORY_DESCRIPTIONS[rowIndex];
		else if(columnIndex == SCREENING_COLUMN)
			value = String.format("%s%%", stats.getScreenPercentage()[rowIndex]);
		else if(columnIndex == DIAGNOSTIC_COLUMN)
			value = String.format("%s%%", stats.getDiagPercentage()[rowIndex]);
		else if(columnIndex == UNDIFFERENTIATED_COLUMN)
			value = String.format("%s%%", 0);;
		
		return value;
	}
	
	@Override
	public String getColumnName(int index){
		return COLUMNS[index];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
	    return super.getColumnClass(columnIndex);
	}
	
	@Override
	public boolean isCellEditable(int row, int col) {
	    return (col == 0); 
	}
	
	/**
	 * Adds the specified row to this model
	 * @param row - The row to add
	 * @return - True
	 */
	public boolean add(Patient row){
		rows.add(row);
		fireTableRowsInserted(rows.size(), rows.size());
		
		return true;
	}
	
	/**
	 * Removes the item at the given index
	 * @param index - The index of the element to remove
	 * @return - The element that was removed
	 */
	public Patient remove(int index){
		Patient row = rows.remove(index);
		fireTableRowsDeleted(index, index);
		
		return row;
	}
	
	/*@Override
	public void setValueAt(Object obj, int rowIndex, int columnIndex){
		if (columnIndex == 0) {
	        rows.get(rowIndex).setDeleted((Boolean)obj);
	        this.fireTableRowsUpdated(rowIndex, rowIndex);
	    }
		fireTableCellUpdated(rowIndex, columnIndex);
	}*/
	
	public void updateTable(){
		stats.calculateStats(rows);
		fireTableDataChanged();
	}

}
