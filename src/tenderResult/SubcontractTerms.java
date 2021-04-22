package tenderResult;

import org.w3c.dom.Element;

/**
 * @params
 *		rate: double [0..1]
 *		amount: double [0..1]
 *		description: String [0..1]
 */
public class SubcontractTerms {
	private double rate, amount;
	private String description;
	
	public void readAttributes(Element sct, int POS_UNICO_ELEMENTO) {
		this.rate = -1;
		this.amount = -1;
		this.description = null;
	
		Element rate = (Element) sct.getElementsByTagName("cbc:Rate").item(POS_UNICO_ELEMENTO);
		if (rate != null){
			this.rate = Double.parseDouble(rate.getTextContent());
		}

		Element amount = (Element) sct.getElementsByTagName("cbc:Amount").item(POS_UNICO_ELEMENTO);
		if (amount != null){
			this.amount = Double.parseDouble(amount.getTextContent());
		}
		
		Element description = (Element) sct.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}

	public void print(){
		System.out.print("*** SUBCONTRACT TERMS ***\n" +
						 "---> Rate: " + rate + "\n" +
						 "---> Amount: " + amount + "\n" +
						 "---> Description: " + description + "\n");
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public SubcontractTerms(){}

}
