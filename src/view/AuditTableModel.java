package view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.AuditStats;
import model.Patient;

public class AuditTableModel extends AbstractTableModel{
	
	private static final long serialVersionUID = -8783384299192509623L;
	
	public static final int COUNT_COLUMN = 0;
	public static final int DESCRIPTION_COLUMN = 1;
	

	private static final String[] COLUMNS = {"Count", "Description"};
	
	private static final String[] DESCRIPTIONS = {"# of mammogram cases",
												"# of mammogram cases BI-RADS 0, 4, 5",
												"# of biopsies performed",
												"# of BI-RADS category 4",
												"# of BI-RADS category 5",
												"# of biopsy pathologies that were malignant",
												"# of BI-RADS category 4 that were malignant",
												"# of BI-RADS category 5 that were malignant",
												"# of biopsy pathologies that were benign",
												"# of ductal carcinoma in situ",
												"# of invasive ductal carcinoma or invasive lobular carcinoma",
												"# of invasive ductal or lobular carcinoma w/ axillary sampling performed",
												"# of true positives",
												"# of false positives",
												"% positive predictive value (BI-RADS 4 and 5)",
												"% positive predictive value (BI-RADS 4)",
												"% positive predictive value (BI-RADS 5)",
												"% cancer detection rate (per 1000 exams)",
												"% recall"
	};
	
	private List<Patient> rows;
	private AuditStats auditStats;
	
	public AuditTableModel(){
		rows = new ArrayList<Patient>();
		auditStats = new AuditStats();
	}

	public List<Patient> getRows() {
		return rows;
	}

	public void setRows(List<Patient> rows) {
		this.rows = rows;
	}

	public AuditStats getAuditStats() {
		return auditStats;
	}

	public void setAuditStats(AuditStats auditStats) {
		this.auditStats = auditStats;
	}
	
	public void updateTable(){
		auditStats.calculateStats(rows);
		fireTableDataChanged();
	}

	@Override
	public int getColumnCount() {
		return COLUMNS.length;
	}

	@Override
	public int getRowCount() {
		return DESCRIPTIONS.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object value = null;
		
		int tableSize = auditStats.getCounts().length;
		
		if(columnIndex == COUNT_COLUMN){
			if(rowIndex < tableSize){
				value = auditStats.getCounts()[rowIndex];
			}
			else{
				value = auditStats.getPercents()[rowIndex - tableSize];
			}
		}
		else if (columnIndex == DESCRIPTION_COLUMN){
			value = DESCRIPTIONS[rowIndex];
		}
		
		return value;
	}
	
	@Override
	public String getColumnName(int index){
		return COLUMNS[index];
	}

}
