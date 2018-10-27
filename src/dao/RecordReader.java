package dao;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import model.Patient;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RecordReader {
	
	private List<Patient> patients = new ArrayList<Patient>();
	
	private SAXParser saxParser;
	
	private RecordHandler recordHandler;
	
	public RecordReader(){
		try{
			
			SAXParserFactory factory = SAXParserFactory.newInstance();
			saxParser = factory.newSAXParser();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void readRecord(String recordPath) throws SAXException, IOException {
		
		this.recordHandler = new RecordHandler();
		
//		File file = new File(recordPath);
//		if(!file.exists())
//		{
//			file.createNewFile();
//		}
		
		saxParser.parse(recordPath, recordHandler);
		
		this.patients = recordHandler.getPatients();
	}
	
	
	public List<Patient> getPatients(){
		if(patients != null)
			return this.patients;
		else
			throw new NullPointerException("Patient records are null. Call readRecord() on valid record path to populate.");
		
	}
	
	
	private class RecordHandler extends DefaultHandler{
		private List<Patient> patients = new ArrayList<Patient>();
		
		public List<Patient> getPatients() {
			return patients;
		}
		
		public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
			
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			
			try{
				if (qName.equalsIgnoreCase("PATIENT")){
					
					String screen_cat = attributes.getValue("screening") == null ? "-1" : attributes.getValue("screening");
					String diag_cat = attributes.getValue("diagnostic") == null ? "-1" : attributes.getValue("diagnostic");
					String ultrasound = attributes.getValue("ultrasound") == null ? "-1" : attributes.getValue("ultrasound");
					String biopsy = attributes.getValue("biopsy") == null ? "-1" : attributes.getValue("biopsy");
					String provider = attributes.getValue("provider") == null ? "" : attributes.getValue("provider");
					String comments = attributes.getValue("comments") == null ? "" : attributes.getValue("comments");
					
					Patient patient = new Patient(attributes.getValue("name"),
												Integer.parseInt(screen_cat),
												Integer.parseInt(diag_cat),
												Integer.parseInt(ultrasound),
												Integer.parseInt(biopsy),
												formatter.parse(attributes.getValue("date")),
												provider,
												comments);
					if(attributes.getValue("id") != null){
						patient.setId(Long.parseLong(attributes.getValue("id")));
					}
					
					patients.add(patient);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
	/*************
	 * TEST METHOD
	 *************/
	public static void main(String[] args){
		
		RecordReader reader = new RecordReader();
		try {
			reader.readRecord("C:/NDAR/NewTest/NDARSubmissionPackage-1352305184751.xml");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(reader.getPatients());
		for(Patient patient : reader.getPatients()){
			System.out.println("Patient Name: " + patient.getName());
			System.out.println("Patient Screening Category: " + patient.getScreening_category());
			System.out.println("Patient Diagnostic Category: " + patient.getDiagnostic_category());
			System.out.println("Patient Date: " + patient.getDate());
		}
	}
	

}
