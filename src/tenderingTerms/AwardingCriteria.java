package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params
 * 		description: String[1]
 *		weightNumeric: double[0..1]
 */
public class AwardingCriteria {
	private String description;
	private double weightNumeric;

	public void readAttributes(Element ac, int POS_UNICO_ELEMENTO) {
		this.description = null;
		this.weightNumeric = -1;
		
		/* DESCRIPTION */
		Element description = (Element) ac.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> AwardingTerms -> AwardingCriteria -> DESCRIPTION no existe\n");
		}
		
		/* WEIGHT NUMERIC */
		Element wn = (Element) ac.getElementsByTagName("cbc:WeightNumeric").item(POS_UNICO_ELEMENTO);
		if (wn != null){
			this.weightNumeric = Double.parseDouble(wn.getTextContent());
		}
	}

	public void print(){
		System.out.print("**** AWARDING CRITERIA ****\n" +
				 "---> Description: " + description + "\n" +
				 "---> Weight Numeric: " + weightNumeric + "\n" +
				 "--------------------------------\n");
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	public AwardingCriteria(){}
}
