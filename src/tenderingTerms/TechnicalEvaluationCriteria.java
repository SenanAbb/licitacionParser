package tenderingTerms;

import org.w3c.dom.Element;

/*
 *		evaluationCriteriaTypeCode: String[1]
 *		description: String[0..1]
 *		thresholdQuantity: int[0..1]
 */
public class TechnicalEvaluationCriteria {
	private String evaluationCriteriaTypeCode;
	private double thresholdQuantity;
	private String description;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param tec El cac:TechnicalEvaluationCriteria que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element tec, int POS_UNICO_ELEMENTO) {
		this.evaluationCriteriaTypeCode = null;
		this.thresholdQuantity = -1;
		this.description = null;
		
		/* EVALUATION CRITERIA TYPE CODE */
		Element ectc = (Element) tec.getElementsByTagName("cbc:EvaluationCriteriaTypeCode").item(POS_UNICO_ELEMENTO);
		try {
			this.evaluationCriteriaTypeCode = ectc.getTextContent();
		} catch (NullPointerException e) {
			System.err.print("ERROR FATAL: TenderingTerms -> TendererQualificationRequest -> TechnicalEvaluationCriteria -> EVALUATION CRITERIA TYPE CODE no existe\n");
		}
		
		/* THRESHOLD QUANTITY */
		Element tq = (Element) tec.getElementsByTagName("cbc:ThresholdQuantity").item(POS_UNICO_ELEMENTO);
		if (tq != null){
			this.thresholdQuantity = Double.parseDouble(tq.getTextContent());
		}
		
		/* DESCRIPTION */
		Element description = (Element) tec.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}

	public String getEvaluationCriteriaTypeCode() {
		return evaluationCriteriaTypeCode;
	}

	public String getDescription() {
		return description;
	}
}
