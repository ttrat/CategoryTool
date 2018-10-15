package test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import model.Patient;
import model.RecordReader;
import model.RecordWriter;

public class ReadWriteTest {
	
	private RecordReader reader = new RecordReader();
	private RecordWriter writer = new RecordWriter();
	private List<Patient> patients = new ArrayList<Patient>(0);
	
	public void readTest(){
		try {
			
			reader.readRecord("C:\\Users\\rtromb\\Documents\\record03.xml");
			patients = reader.getPatients();
			
			for(Patient patient : patients){
				System.out.println("name = " + patient.getName() + ", " + "screening = " + patient.getScreening_category() + ", " +  "diagnostic = " + patient.getDiagnostic_category() + ", " + "date = " + patient.getDate());
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*private void writeTest(){
		try{
			
			writer.addPatient(new Patient("Wilma Snoot", 0, 5, Boolean.FALSE, "05/05/2005"), "C:\\Users\\rtromb\\Documents\\record03.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	private void removeTest(){
		try{
			
			writer.removePatient("Wilma Snoot", "C:\\Users\\rtromb\\Documents\\record03.xml");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public static void main(String[] args) {
		ReadWriteTest tester = new ReadWriteTest();
		
//		tester.readTest();
//		tester.writeTest();
		tester.removeTest();
		tester.readTest();
		
		System.exit(0);
	}

}
