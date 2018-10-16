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

	public void calculateStats(List<Patient> patients) {
        this.numCases = patients.size();
        this.clearCounters();
        for (Patient patient : patients) {
            Boolean birads0 = patient.getDiagnostic_category() == 0 && patient.getUltrasound() == -1 || patient.getUltrasound() == 0;
            Boolean birads4 = patient.getDiagnostic_category() == 4 && patient.getUltrasound() == -1 || patient.getUltrasound() == 4;
            Boolean birads5 = patient.getDiagnostic_category() == 5 && patient.getUltrasound() == -1 || patient.getUltrasound() == 5;
            Boolean malignant = patient.getBiopsy_result() > -1 && patient.getBiopsy_result() < 4;
            Boolean recall = patient.getDiagnostic_category() != -1 || patient.getUltrasound() != -1;
            if ((birads4.booleanValue() || birads5.booleanValue()) && malignant.booleanValue()) {
                ++this.numTruePositive;
            }
            if ((birads4.booleanValue() || birads5.booleanValue()) && !malignant.booleanValue()) {
                ++this.numFalsePositive;
            }
            if (birads0.booleanValue() || birads4.booleanValue() || birads5.booleanValue()) {
                ++this.numCases045;
            }
            if (birads4.booleanValue()) {
                ++this.numCatFour;
            }
            if (birads5.booleanValue()) {
                ++this.numCatFive;
            }
            if (patient.getBiopsy_result() != -1) {
                ++this.numBiopsies;
                if (malignant.booleanValue()) {
                    ++this.numBiopsiesMalignant;
                    if (birads4.booleanValue()) {
                        ++this.numCatFourMalignant;
                    }
                    if (birads5.booleanValue()) {
                        ++this.numCatFiveMalignant;
                    }
                } else if (!malignant.booleanValue()) {
                    ++this.numBiopsiesBenign;
                }
                if (patient.getBiopsy_result() == 0) {
                    ++this.numDuctalCarcinoma;
                }
            } else if (!birads4.booleanValue()) {
                birads5.booleanValue();
            }
            if (!recall.booleanValue()) continue;
            ++this.numRecalled;
        }
        this.recallRate = (double)this.numRecalled / (double)this.numCases * 100.0;
        this.posPredFour = this.numCatFour > 0 ? (double)this.numCatFourMalignant / (double)this.numCatFour * 100.0 : 0.0;
        this.posPredFive = this.numCatFive > 0 ? (double)this.numCatFiveMalignant / (double)this.numCatFive * 100.0 : 0.0;
        this.posPredFourFive = this.numCatFour + this.numCatFive > 0 ? (double)(this.numCatFourMalignant + this.numCatFiveMalignant) / (double)(this.numCatFour + this.numCatFive) * 100.0 : 0.0;
        this.cancerDetectionRate = this.numCases > 0 ? (double)this.numBiopsiesMalignant / (double)this.numCases * 100.0 : 0.0;
        this.setCounts(new int[]{this.numCases, this.numCases045, this.numBiopsies, this.numCatFour, this.numCatFive, this.numBiopsiesMalignant, this.numCatFourMalignant, this.numCatFiveMalignant, this.numBiopsiesBenign, this.numDuctalCarcinoma, this.numInvDuctalLobular, this.numInvDuctalLobularAxillary, this.numTruePositive, this.numFalsePositive});
        this.setPercents(new double[]{this.posPredFourFive, this.posPredFour, this.posPredFive, this.cancerDetectionRate, this.recallRate});
    }
	
	private void clearCounters(){
		
		numCases045 = 0;
		numCatFour = 0;
		numCatFive = 0;
		numBiopsies = 0;
		numBiopsiesMalignant = 0;
		numCatFourMalignant = 0;
		numCatFiveMalignant = 0;
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
