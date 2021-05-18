package tenderingTerms;

import org.w3c.dom.Element;

/** 
 * @params 
 * 		guaranteeTypeCode: int[1]
 *		amountRate: double[0..1]
 *		liabilityAmount: double[0..1]
 */
public class RequiredFinancialGuarantee {
	private int guaranteeTypeCode;
	private double amountRate, liabilityAmount;
	private String liabilityAmountCurrencyID;
	
	public void readAttributes(Element rfg, int POS_UNICO_ELEMENTO) {
		this.guaranteeTypeCode = -1;
		this.amountRate = -1;
		this.liabilityAmount = -1;
		this.liabilityAmountCurrencyID = null;
		
		/* GUARANTEE TYPE CODE */
		Element gtc = (Element) rfg.getElementsByTagName("cbc:GuaranteeTypeCode").item(POS_UNICO_ELEMENTO);
		try {
			this.guaranteeTypeCode = Integer.parseInt(gtc.getTextContent());
		} catch (NullPointerException e) {
			System.err.print("ERROR FATAL: TenderingTerms -> RequiredFinancialGuarantee -> ID no existe\n");
		}
		
		/* AMOUNT RATE */
		Element ar = (Element) rfg.getElementsByTagName("cbc:AmountRate").item(POS_UNICO_ELEMENTO);
		if (ar != null){
			this.amountRate = Double.parseDouble(ar.getTextContent());
		}

		/* LIABILITY AMOUNT */
		Element lr = (Element) rfg.getElementsByTagName("cbc:LiabilityAmount").item(POS_UNICO_ELEMENTO);
		if (lr != null){
			this.liabilityAmount = Double.parseDouble(lr.getTextContent());
			this.liabilityAmountCurrencyID = lr.getAttributes().getNamedItem("currencyID").getTextContent();
		}	
	}

	public void print(){
		System.out.print("*** REQUIRED FINANCIAL GUARANTEE ***\n" +
						 "---> Guarantee Type Code: " + guaranteeTypeCode + "\n" +
						 "---> Amount Rate: " + amountRate + "\n" +
						 "---> Liability Amount: " + liabilityAmount + "\n" +
					 	 "--------------------------------\n");
	}

	public int getGuaranteeTypeCode() {
		return guaranteeTypeCode;
	}

	public double getAmountRate() {
		return amountRate;
	}

	public double getLiabilityAmount() {
		return liabilityAmount;
	}

	public String getLiabilityAmountCurrencyID() {
		return liabilityAmountCurrencyID;
	}
}
