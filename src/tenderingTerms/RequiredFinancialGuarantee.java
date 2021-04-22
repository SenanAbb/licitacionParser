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
	
	public void readAttributes(Element rfg, int POS_UNICO_ELEMENTO) {
		this.guaranteeTypeCode = -1;
		this.amountRate = -1;
		this.liabilityAmount = -1;
		
		/* GUARANTEE TYPE CODE */
		Element gtc = (Element) rfg.getElementsByTagName("cbc:GuaranteeTypeCode").item(POS_UNICO_ELEMENTO);
		if (gtc != null){
			this.guaranteeTypeCode = Integer.parseInt(gtc.getTextContent());
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> RequiredFinancialGuarantee -> ID no existe\n");
		}
		
		/* AMOUNT RATE */
		Element ar = (Element) rfg.getElementsByTagName("cbc:AmountRate").item(POS_UNICO_ELEMENTO);
		if (ar != null){
			this.amountRate = Double.parseDouble(ar.getTextContent());
		}

		/* LIABILITY AMOUNT */
		Element lr = (Element) rfg.getElementsByTagName("cbc:LiabilityRate").item(POS_UNICO_ELEMENTO);
		if (lr != null){
			this.liabilityAmount = Double.parseDouble(lr.getTextContent());
		}
	}

	public void print(){
		System.out.print("*** REQUIRED FINANCIAL GUARANTEE ***\n" +
						 "---> Guarantee Type Code: " + guaranteeTypeCode + "\n" +
						 "---> Amount Rate: " + amountRate + "\n" +
						 "---> Liability Amount: " + liabilityAmount + "\n" +
					 	 "--------------------------------\n");
	}
}
