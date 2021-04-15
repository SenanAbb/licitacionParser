package tenderResult;

import org.w3c.dom.Element;

public class LegalMonetaryTotal {
	private double taxExclusiveAmount, payableAmount;
	private String taxExclusiveAmountCurrencyID, payableAmountCurrencyID;
	
	public void readAttributes(Element lmt, int POS_UNICO_ELEMENTO) {
		this.taxExclusiveAmount = -1;
		this.payableAmount = -1;
		this.taxExclusiveAmountCurrencyID = null;
		this.payableAmountCurrencyID = null;
		
		Element taxExclusiveAmount = (Element) lmt.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		if (taxExclusiveAmount != null){
			this.taxExclusiveAmount = Double.parseDouble(taxExclusiveAmount.getTextContent());
			this.taxExclusiveAmountCurrencyID = taxExclusiveAmount.getAttributes().getNamedItem("currencyID").getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderResult -> AwardedTenderedProject -> LegalMonetaryTotal -> TAX EXCLUSIVE AMOUNT no existe\n");
		}
		
		Element payableAmount = (Element) lmt.getElementsByTagName("cbc:PayableAmount").item(POS_UNICO_ELEMENTO);
		if (payableAmount != null){
			this.payableAmount = Double.parseDouble(payableAmount.getTextContent());
			this.payableAmountCurrencyID = payableAmount.getAttributes().getNamedItem("currencyID").getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderResult -> AwardedTenderedProject -> LegalMonetaryTotal -> PAYABLE AMOUNT no existe\n");
		}
	}

	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public LegalMonetaryTotal(){}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public double getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}

	public double getPayableAmount() {
		return payableAmount;
	}

	public String getTaxExclusiveAmountCurrencyID() {
		return taxExclusiveAmountCurrencyID;
	}

	public String getPayableAmountCurrencyID() {
		return payableAmountCurrencyID;
	}
}
