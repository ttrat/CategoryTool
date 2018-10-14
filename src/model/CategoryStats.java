package model;

import java.util.Date;
import java.util.List;




public class CategoryStats {
	
	private int[] screenCount;
	private int[] diagCount;
	
	private double[] screenPercentage;
	private double[] diagPercentage;
	
	public int[] getScreenCount() {
		return screenCount;
	}
	public void setScreenCount(int[] screenCount) {
		this.screenCount = screenCount;
	}

	public int[] getDiagCount() {
		return diagCount;
	}
	public void setDiagCount(int[] diagCount) {
		this.diagCount = diagCount;
	}

	public double[] getScreenPercentage() {
		return screenPercentage;
	}
	public void setScreenPercentage(double[] screenPercentage) {
		this.screenPercentage = screenPercentage;
	}

	public double[] getDiagPercentage() {
		return diagPercentage;
	}
	public void setDiagPercentage(double[] diagPercentage) {
		this.diagPercentage = diagPercentage;
	}

	private void init(){
		
		screenCount = new int[Constants.NUM_CATEGORIES];
		diagCount = new int[Constants.NUM_CATEGORIES];
		
		screenPercentage = new double[Constants.NUM_CATEGORIES];
		diagPercentage = new double[Constants.NUM_CATEGORIES];
		
		
	}
	public void calculateStats(List<Patient> patients){
		
		this.init();
		
		double patientCount = patients.size();
		
		for(Patient patient : patients){
			if(patient.getScreening_category() != -1){
				screenCount[patient.getScreening_category()]++;
			}
			if(patient.getDiagnostic_category() != -1){
				diagCount[patient.getDiagnostic_category()]++;
			}
			
		}
		
		for(int i=0; i < Constants.NUM_CATEGORIES; i++){
			
			screenPercentage[i] = ((double)screenCount[i]/patientCount) * 100;
			diagPercentage[i] = ((double)diagCount[i]/patientCount) * 100;
		}
		
	}
	

}
