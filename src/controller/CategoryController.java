package controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import dao.RecordReader;
import dao.RecordWriter;

import model.AuditStats;
import model.Patient;

public class CategoryController {
	
	private List<Patient> patients = new ArrayList<Patient>(0);
	
	private Date startDate;
	private Date endDate;
	
	private RecordReader reader = new RecordReader();
	private RecordWriter writer = new RecordWriter();
	
	private AuditStats audits = new AuditStats();
	
	private String recordsLocation = "";
	
	public AuditStats getAudits() {
		return audits;
	}
	public void setAudits(AuditStats audits) {
		this.audits = audits;
	}
	public List<Patient> getPatients() {
		return patients;
	}
	public void setPatients(List<Patient> patients) {
		this.patients = patients;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getRecordsLocation() {
		return recordsLocation;
	}
	public void setRecordsLocation(String recordsLocation) {
		this.recordsLocation = recordsLocation;
	}
	
	public void loadRecords(){
		try {
			
			reader.readRecord(recordsLocation);
			
			this.setPatients(reader.getPatients());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void save(){
		try{
			
			writer.updateRecords(patients, recordsLocation);
			
		}catch(Exception e){
			System.out.println("Error occured while trying to save the records");
			e.printStackTrace();
		}
		
		loadRecords();
		
		this.setPatients(reader.getPatients());
			
	}
	
	public void actionBrowseFile(){
		JFileChooser chooser = new JFileChooser("");
    	chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    	chooser.setMultiSelectionEnabled(false);
    	chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
//    	chooser.setFileFilter(new FileFilter(){
//			
//			public boolean accept(File f) {
//				return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
//			}
//			
//			public String getDescription() {
//				return "XML_FILES_ONLY";
//			}
//    		
//    	});
	    int returnVal = chooser.showOpenDialog(new JFrame());
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	if(chooser.getSelectedFile().getAbsoluteFile().toString().toLowerCase().endsWith(".xml")){
	    		setRecordsLocation(chooser.getSelectedFile().getAbsolutePath());
	    	}
	    }
	    
	    loadRecords();
	}
	
	public List<Patient> limitByDates(){
		List<Patient> rangeList = new ArrayList<Patient>();
		
		//Accounts for before() exclusion issue
		if(startDate.getTime() > Long.MIN_VALUE){
			startDate.setTime(startDate.getTime()-10000L);
		}
		
		for(Patient patient : patients){
			if(!patient.getDate().before(startDate) && !patient.getDate().after(endDate)){
				if(!patient.getDeleted()){
					rangeList.add(patient);
				}
			}
		}
		
		return rangeList;
	}
	
	public void setDateRange(Date startDate, Date endDate){
		
		if(startDate == null){
			startDate = new Date(Long.MIN_VALUE);
		}
		if(endDate == null){
			endDate = new Date(Long.MAX_VALUE);
		}
		this.setStartDate(startDate);
		this.setEndDate(endDate);
	}
	
}
