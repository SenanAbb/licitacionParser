package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params
 * 		description: String[0..1]
 * 		amount: boolean[0..1]
 *		rate: double[0..1]
 *		subcontractTermsCode: int[0..1]
 *		minimumRate: double[0..1]
 *		maximumRate: double[0..1]
 */
public class AllowedSubcontractTerms {
	private String description;
	private boolean amount;
	private double rate, minimumRate, maximumRate;
	private int subcontractTerms;
	
	public void readAttributes(Element ast, int POS_UNICO_ELEMENTO) {
		this.description = null;
		this.rate = -1;
		this.subcontractTerms = -1;
		this.minimumRate = -1;
		this.maximumRate = -1;
		
		/* DESCRIPTION */
		Element description = (Element) ast.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
		
		/* AMOUNT */
		Element amount = (Element) ast.getElementsByTagName("cbc:Amount").item(POS_UNICO_ELEMENTO);
		if (amount != null){
			this.amount = Boolean.parseBoolean(amount.getTextContent());
		}
		
		/* RATE */
		Element rate = (Element) ast.getElementsByTagName("cbc:Rate").item(POS_UNICO_ELEMENTO);
		if (rate != null){
			this.rate = Double.parseDouble(rate.getTextContent());
		}
		
		/* SUBCONTRACT TERMS */
		Element sTerms = (Element) ast.getElementsByTagName("cbc:SubcontractTerms").item(POS_UNICO_ELEMENTO);
		if (sTerms != null){
			this.subcontractTerms = Integer.parseInt(sTerms.getTextContent());
		}
		
		/* MINIMUM RATE */
		Element minRate = (Element) ast.getElementsByTagName("cbc:MinimumRate").item(POS_UNICO_ELEMENTO);
		if (minRate != null){
			this.minimumRate = Double.parseDouble(minRate.getTextContent());
		}
		
		/* MINIMUM RATE */
		Element maxRate = (Element) ast.getElementsByTagName("cbc:MaximumRate").item(POS_UNICO_ELEMENTO);
		if (maxRate != null){
			this.maximumRate = Double.parseDouble(maxRate.getTextContent());
		}
	}
	
	public void print(){
		System.out.print("*** ALLOWED SUBCONTRACT TERMS ***\n" +
						 "---> Description: " + description + "\n" +
						 "---> Amount: " + amount + "\n" +
						 "---> Subcontract Terms: " + subcontractTerms + "\n" +
						 "---> Rate: " + rate + "\n" +
						 "---> Minimum Rate: " + minimumRate + "\n" +
						 "---> Maximum Rate: " + maximumRate + "\n" +
						 "--------------------------------\n");
	}
}
