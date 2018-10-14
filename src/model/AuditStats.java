package model;

import java.util.List;

public class AuditStats {
	
	private int numCases = 0;
	private int numCases045 = 0;
	private int numBiopsies = 0;
	private int numCatFour = 0;
	private int numCatFive = 0;
	private int numBiopsiesMalignant = 0;
	private int numCatFourMalignant = 0;
	private int numCatFiveMalignant = 0;
	private int numBiopsiesBenign = 0;
	private int numLost45 = 0;
	private int numDuctalCarcinoma = 0;
	private int numInvDuctalLobular = 0;
	private int numInvDuctalLobularAxillary = 0;
	private int numTruePositive = 0;
	private int numFalsePositive = 0;
	private int numRecalled = 0;
	private double posPredFourFive = 0;
	private double posPredFour = 0;
	private double posPredFive = 0;
	private double cancerDetectionRate = 0;
	private double recallRate = 0;
	
	private int[] counts;
	private double[] percents;
	

	public int[] getCounts() {
		return counts;
	}

	public void setCounts(int[] counts) {
		this.counts = counts;
	}


	public double[] getPercents() {
		return percents;
	}

	public void setPercents(double[] percents) {
		this.percents = percents;
	}

	public void calculateStats(List<Patient> patients){
		
		Boolean birads0;
		Boolean birads4;
		Boolean birads5;
		Boolean malignant;
//		Boolean benign;
//		Boolean falsePositive;
//		Boolean truePositive;
		Boolean recall; //if has diagnostic or ultrasound (not N/A)
		//what is positive predictive value?
//		Boolean lost; //cat 4 or 5 but no biopsy?
		
		numCases = patients.size();
		
		clearCounters();
		
		for(Patient patient : patients){
			
			////New Calculations////
			/**
			 * Basic Assumptions
			 */
			birads0 = patient.getDiagnostic_category() == 0;
			birads4 = patient.getDiagnostic_category() == 4;
			birads5 = patient.getDiagnostic_category() == 5;
			
			malignant = (patient.getBiopsy_result() > -1) && (patient.getBiopsy_result() < 4);
			
			recall = patient.getDiagnostic_category() != -1 || patient.getUltrasound() != -1;
			
//			lost = (birads4 || birads5) && patient.getBiopsy_result() == -1;
			/**
			 * 
			 */
			
			
			if((birads4 || birads5) && malignant){
				numTruePositive++;
			}
			if((birads4 || birads5) && !malignant){
				numFalsePositive++;
			}
			
			if(birads0 || birads4 || birads5){
				numCases045++;
			}
			if(birads4){
				numCatFour++;
			}
			if(birads5){
				numCatFive++;
			}
			
			if(patient.getBiopsy_result() != -1){
				numBiopsies++;
				
				if(malignant){
					numBiopsiesMalignant++;
					
					if(birads4){
						numCatFourMalignant++;
					}
					if(birads5){
						numCatFiveMalignant++;
					}
				}
				else if(!malignant){
					numBiopsiesBenign++;
				}
				
				if(patient.getBiopsy_result() == 0){
					numDuctalCarcinoma++;
				}
				
				//TODO: ductal stats?
//				if(?){
//					numInvDuctalLobular
//					numInvDuctalLobularAxillary
//				}
			}
			
			else{
				if(birads4 || birads5){
					numLost45++;
				}
			}
			
			if(recall){
				numRecalled++;
			}
			
			//////////////////////////
			
		}
			
		////new////
		recallRate = ((double)numRecalled / numCases) * 100;
		posPredFour = numCatFour > 0 ? ((double)numCatFourMalignant / (double)numCatFour) * 100 : 0;
		posPredFive = numCatFive > 0 ? ((double)numCatFiveMalignant / (double)numCatFive) * 100 : 0;
		posPredFourFive = numCatFour + numCatFive > 0 ? ((double)(numCatFourMalignant + numCatFiveMalignant) / (double)(numCatFour + numCatFive)) * 100 : 0;
		///////////
		
		setCounts(new int[] {numCases, numCases045, numBiopsies, numCatFour, numCatFive, numBiopsiesMalignant, numCatFourMalignant, numCatFiveMalignant, numBiopsiesBenign, numLost45, numDuctalCarcinoma, numInvDuctalLobular, numInvDuctalLobularAxillary, numTruePositive, numFalsePositive});
		setPercents(new double[] {posPredFourFive, posPredFour, posPredFive, cancerDetectionRate, recallRate});
	}
	
	private void clearCounters(){
		
		numCases045 = 0;
		numCatFour = 0;
		numCatFive = 0;
		numBiopsies = 0;
		numBiopsiesMalignant = 0;
		numCatFourMalignant = 0;
		numCatFiveMalignant = 0;
		numLost45 = 0;
		numBiopsiesBenign = 0;
		numDuctalCarcinoma = 0;
		numInvDuctalLobular = 0;
		numInvDuctalLobularAxillary = 0;
		numTruePositive = 0;
		numFalsePositive = 0;
		
		numRecalled = 0;
		
		recallRate = 0.0;
	}

}
