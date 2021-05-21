package contractModification;

import org.w3c.dom.Element;

/**
 * @params
 *		taxExclusiveAmount: double[0..1]
 *		currencyID: String[0..1]
 */
public class FinalLegalMonetaryTotal {
	private double taxExclusiveAmount;
	private String currencyID;
	
	public void readAttributes(Element flmt, int POS_UNICO_ELEMENTO){
		this.taxExclusiveAmount = -1;
		this.currencyID = null;
		
		Element tea = (Element) flmt.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		if (tea != null){
			this.taxExclusiveAmount = Double.parseDouble(tea.getTextContent());
			this.currencyID = tea.getAttributes().getNamedItem("currencyID").getTextContent();
		}
	}
	
	public void print() {
		System.out.print("*** FINAL LEGAL MONETARY TOTAL ***\n" + 
						 "---> Tax Exclusive Amount: " + taxExclusiveAmount + " " + currencyID + "\n" +
						 "--------------------------------\n");
	}

	public double getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}

	public String getCurrencyID() {
		return currencyID;
	}
}
