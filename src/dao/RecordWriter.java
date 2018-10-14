package dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import model.Patient;

import org.jdom2.*;
import org.jdom2.input.*;
import org.jdom2.output.*;

public class RecordWriter {
	
	private Document document = new Document();
	private SAXBuilder builder = new SAXBuilder();
	
	private long max;
	private boolean update = false;
	
	private final String PATIENT_ELEMENT_NAME = "patient";
	
	
	/**
	 * constants to generate record backups for default record
	 */
		private static final boolean RUN_BACKUP = false;
		private static final String DEFAULT_RECORD_PATH = System.getProperty("user.home")+File.separator+"default_record.xml";
		private static final long BACKUP_DELAY = 0;
	/**
	 * 
	 */
	
	/*
	public RecordWriter(){
		try{
			if(check_backup_delay(DEFAULT_RECORD_PATH, BACKUP_DELAY)){
				run_backup(RUN_BACKUP, DEFAULT_RECORD_PATH, document);
			}
		}catch(Exception e){
			System.out.println("Error creating RecordWriter instance. " + e.getMessage());
		}
	}
	*/
	
	public void updateRecords(List<Patient> patients, String recordPath) throws IOException, JDOMException{
		File input = new File(recordPath);
		
		if(!input.exists()){
        	input.createNewFile();
        	String myxml = "<records></records>";
        	document = builder.build(new StringReader(myxml));
        }
		
		else{
			document = builder.build(input);
		}
		
		max = findMax();
		if(update){
			update();
		}
		
		for(Patient patient : patients){
			
			if(patient.getAdded() == Boolean.TRUE && patient.getDeleted() == Boolean.FALSE){
				this.addPatient(patient, recordPath);
			}
			else if(patient.getAdded() == Boolean.FALSE && patient.getDeleted() == Boolean.TRUE){
//				this.removePatient(patient.getName());
				this.removePatient(patient.getId());
			}
			
			if(patient.getEdited() == Boolean.TRUE){
				this.editPatient(patient);
			}
			
		}
		
		writeToFile(document, recordPath);
	}
	
	public void addPatient(Patient patient, String recordPath) throws IOException, JDOMException {
        File input = new File(recordPath);
        
        if(!input.exists()){
        	input.createNewFile();
        	String myxml = "<records></records>";
        	document = builder.build(new StringReader(myxml));
        }
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        
        max += 1;
        
        Element element = new Element(PATIENT_ELEMENT_NAME);
        element.setAttribute("id", Long.toString(max));
        element.setAttribute("name", patient.getName());
        element.setAttribute("screening", Integer.toString(patient.getScreening_category()));
        element.setAttribute("diagnostic", Integer.toString(patient.getDiagnostic_category()));
        element.setAttribute("ultrasound", Integer.toString(patient.getUltrasound()));
        element.setAttribute("biopsy", Integer.toString(patient.getBiopsy_result()));
        element.setAttribute("date", formatter.format(patient.getDate()));
        
        document.getRootElement().addContent(element);
    }
	
	public void removePatient(String name) throws JDOMException, IOException{
    	
    	List<Element> elements = document.getRootElement().getChildren(PATIENT_ELEMENT_NAME);
    	
    	 for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
         {
	         Element element = iter.next();
	         if(element.getAttribute("name").getValue().equals(name)){
	        	 iter.remove();
	        	 document.getRootElement().removeContent(element);
	         }
         }
	}
	
	public void removePatient(long id) throws JDOMException, IOException{
    	
    	List<Element> elements = document.getRootElement().getChildren(PATIENT_ELEMENT_NAME);
    	
    	 for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
         {
	         Element element = iter.next();
	         if(element.getAttribute("id").getValue().equals(Long.toString(id))){
	        	 iter.remove();
	        	 document.getRootElement().removeContent(element);
	         }
         }
	}
	
	public void editPatient(Patient patient){
		List<Element> elements = document.getRootElement().getChildren(PATIENT_ELEMENT_NAME);
		
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	
   	 	for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
        {
	         Element element = iter.next();
	         
	         if(element.getAttribute("id").getValue().equals(Long.toString(patient.getId()))){
	        	 element.getAttribute("name").setValue(String.valueOf(patient.getName()));
	        	 element.getAttribute("screening").setValue(String.valueOf(patient.getScreening_category()));
	        	 element.getAttribute("diagnostic").setValue(String.valueOf(patient.getDiagnostic_category()));
	        	 element.getAttribute("ultrasound").setValue(String.valueOf(patient.getUltrasound()));
	        	 element.getAttribute("biopsy").setValue(String.valueOf(patient.getBiopsy_result()));
	        	 element.getAttribute("date").setValue(String.valueOf(formatter.format(patient.getDate())));
	         }
        }
	}
	
	private static void writeToFile(Document document, String filePath) throws IOException{
		OutputStream out = null;
        
        try{
        	out = new FileOutputStream(filePath);
        	
        	XMLOutputter serializer = new XMLOutputter();
        	serializer.setFormat(Format.getPrettyFormat());
        	serializer.output(document, out);
             
        }catch(Exception e){
        	e.printStackTrace();
        }
        finally{
        	out.close();
        }
	}
	
	private long findMax(){
		long max = 0;
		long id;
		
		List<Element> elements = document.getRootElement().getChildren(PATIENT_ELEMENT_NAME);
		
    	
   	 	for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
        {
	         Element element = iter.next();
	         
	         if(element.getAttribute("id") != null){
	        	 id = Long.parseLong(element.getAttribute("id").getValue());
	         }
	         else{
	        	 update = true;
	        	 id = -1L;
	         }
	         
	         if(id > max){
	        	 max = id;
	         }
        }
   	 	
   	 	return max;
	}
	
	private void update(){
		List<Element> elements = document.getRootElement().getChildren(PATIENT_ELEMENT_NAME);
		
    	
   	 	for ( Iterator<Element> iter = elements.iterator(); iter.hasNext(); )
        {
	         Element element = iter.next();
	         
	         max += 1;
	         
	         if(element.getAttribute("id") == null){
	        	 element.setAttribute("id", Long.toString(max));
	         }
        }
	}
	
	/*private static void run_backup(boolean backup, String recordPath, Document document){
		File backupFile = new File(recordPath);
		int i = 0;
		
		while(backupFile.exists()){
			
			backupFile = new File(String.format("%s.bak_%d", recordPath, i));
			
			i++;
		}
		
		File recordFile = new File(recordPath);
		recordFile.renameTo(backupFile);
        
	}
	
	private static boolean check_backup_delay(String recordPath, long backupDelay) throws JDOMException, IOException{
		boolean runBackup = false;
		
		File input = new File(recordPath);
		
		if(input.exists()){
			Document document = new Document();
			SAXBuilder builder = new SAXBuilder();			
			document = builder.build(input);
			
			Element backupElement = document.getRootElement().getChild("backup");
			long date = Long.valueOf(backupElement.getAttribute("date").toString());
			
			if((System.currentTimeMillis() - date) > backupDelay){
				runBackup = true;
			}
			
        }
		
		return runBackup;
	}*/
	
}