package model;

import java.util.HashMap;
import java.util.Map;


public class Constants {
	
	public static final String RELEASE_VERSION = "0.8.2";
	
	public static final int FRAME_WIDTH = 1500;
	public static final int FRAME_HEIGHT = 700;
	
	public static final int NUM_CATEGORIES = 7;
	
	public static Map<Integer, String> IMAGING_CATEGORIES = new HashMap<Integer, String>();
	static {
		IMAGING_CATEGORIES.put(-1,"N/A");
		IMAGING_CATEGORIES.put(0,"0: Incomplete");
		IMAGING_CATEGORIES.put(1,"1: Negative");
		IMAGING_CATEGORIES.put(2,"2: Benign Finding(s)");
		IMAGING_CATEGORIES.put(3,"3: Probably Benign Finding");
		IMAGING_CATEGORIES.put(4,"4: Suspicious Abnormality");
		IMAGING_CATEGORIES.put(5,"5: Highly Suggestive of Malignancy");
		IMAGING_CATEGORIES.put(6,"6: Known Biopsy-Proven Malignancy");
	}
	
	public static Map<Integer, String> ULTRASOUND_CATEGORIES = new HashMap<Integer, String>();
	static {
		ULTRASOUND_CATEGORIES.put(-1,"N/A");
		ULTRASOUND_CATEGORIES.put(0,"0: Incomplete");
		ULTRASOUND_CATEGORIES.put(1,"1: Negative");
		ULTRASOUND_CATEGORIES.put(2,"2: Benign Finding(s)");
		ULTRASOUND_CATEGORIES.put(3,"3: Probably Benign Finding");
		ULTRASOUND_CATEGORIES.put(4,"4: Suspicious Abnormality");
		ULTRASOUND_CATEGORIES.put(5,"5: Highly Suspicious of Malignancy");
		ULTRASOUND_CATEGORIES.put(6,"6: Known Malignancy");
	}
	
	public static Map<Integer, String> BIOPSY_RESULTS = new HashMap<Integer, String>();
	static {
		BIOPSY_RESULTS.put(-1,"N/A");
		BIOPSY_RESULTS.put(0,"0: DCIS");
		BIOPSY_RESULTS.put(1,"1: LCIS");
		BIOPSY_RESULTS.put(2,"2: Invasive Ductal Carcinoma");
		BIOPSY_RESULTS.put(3,"3: Inflammatory Breast Cancer");
		BIOPSY_RESULTS.put(4,"4: Benign");
		BIOPSY_RESULTS.put(5,"5: Fibroadenoma");
		BIOPSY_RESULTS.put(6,"6: Phyllodes Tumor");
		BIOPSY_RESULTS.put(7,"7: Atypical Hyperplasia");
		BIOPSY_RESULTS.put(8,"8: Microcystic Disease");
	}
	
	public static final String[] AUDIT_DESCRIPTIONS = {
		"# of mammogram cases",
		"# of mammogram cases BI-RADS 0, 4, 5",
		"# of biopsies performed",
		"# of BI-RADS category 4",
		"# of BI-RADS category 5",
		"# of biopsy pathologies that were malignant",
		"# of BI-RADS category 4 that were malignant",
		"# of BI-RADS category 5 that were malignant",
		"# of biopsy pathologies that were benign",
		"# of cases from BI-RADS 4 and 5 that were lost",
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
	

}
