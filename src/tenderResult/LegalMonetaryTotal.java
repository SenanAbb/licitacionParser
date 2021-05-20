package tenderResult;

import org.w3c.dom.Element;

/**
 * @params
 *		taxExclusiveAmount: double [1]
 *		taxExclusiveAmountCurrencyID: String [1]
 *		payableAmount: double [1]
 *		payableAmountCurrencyID: String [1]
 */
public class LegalMonetaryTotal {
	private double taxExclusiveAmount, payableAmount;
	private String taxExclusiveAmountCurrencyID, payableAmountCurrencyID;
	
	public void readAttributes(Element lmt, int POS_UNICO_ELEMENTO) {
		this.taxExclusiveAmount = -1;
		this.payableAmount = -1;
		this.taxExclusiveAmountCurrencyID = null;
		this.payableAmountCurrencyID = null;
		
		Element taxExclusiveAmount = (Element) lmt.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		try {
			this.taxExclusiveAmount = Double.parseDouble(taxExclusiveAmount.getTextContent());
			this.taxExclusiveAmountCurrencyID = taxExclusiveAmount.getAttributes().getNamedItem("currencyID").getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderResult -> AwardedTenderedProject -> LegalMonetaryTotal -> TAX EXCLUSIVE AMOUNT no existe\n");
		}
		
		Element payableAmount = (Element) lmt.getElementsByTagName("cbc:PayableAmount").item(POS_UNICO_ELEMENTO);
		try {
			this.payableAmount = Double.parseDouble(payableAmount.getTextContent());
			this.payableAmountCurrencyID = payableAmount.getAttributes().getNamedItem("currencyID").getTextContent();
		} catch (NullPointerException e) {
			System.err.print("ERROR FATAL: TenderResult -> AwardedTenderedProject -> LegalMonetaryTotal -> PAYABLE AMOUNT no existe\n");
		}
	}
	
	public void print(){
		System.out.print("----> Tax Exclusive Amount: " + taxExclusiveAmount + " " + "(" + taxExclusiveAmountCurrencyID + ")\n");
		System.out.print("----> Tax Exclusive Amount: " + payableAmount + " " + "(" + payableAmountCurrencyID + ")\n");
	}
	
	public double getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public String getCurrencyID() {
		return payableAmountCurrencyID;
	}
}
