package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params
 *		evaluationCriteriaTypeCode: String[1]
 *		description: String[0..1]
 *		thresholdQuantity: int[0..1]
 */
public class TechnicalEvaluationCriteria {
	private String evaluationCriteriaTypeCode;
	private double thresholdQuantity;
	private String description;
	
	public void readAttributes(Element ftr, int POS_UNICO_ELEMENTO) {
		this.evaluationCriteriaTypeCode = null;
		this.thresholdQuantity = -1;
		this.description = null;
		
		/* EVALUATION CRITERIA TYPE CODE */
		Element ectc = (Element) ftr.getElementsByTagName("cbc:EvaluationCriteriaTypeCode").item(POS_UNICO_ELEMENTO);
		if (ectc != null){
			this.evaluationCriteriaTypeCode = ectc.getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> TendererQualificationRequest -> TechnicalEvaluationCriteria -> EVALUATION CRITERIA TYPE CODE no existe\n");
		}
		
		/* THRESHOLD QUANTITY */
		Element tq = (Element) ftr.getElementsByTagName("cbc:ThresholdQuantity").item(POS_UNICO_ELEMENTO);
		if (tq != null){
			this.thresholdQuantity = Double.parseDouble(tq.getTextContent());
		}
		
		/* DESCRIPTION */
		Element description = (Element) ftr.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}
	
	public void print(){
		System.out.print("**** TECHNICAL EVALUATION CRITERIA ****\n" +
						 "----> Evaluation Criteria Type Code: " + evaluationCriteriaTypeCode + "\n" +
						 "----> Threshold Quantity: " + thresholdQuantity + "\n" +
						 "----> Description: " + description + "\n" +
						 "--------------------------------\n");
	}

	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	public TechnicalEvaluationCriteria(){}
}
