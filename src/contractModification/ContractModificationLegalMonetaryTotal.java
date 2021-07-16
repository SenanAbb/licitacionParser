package contractModification;

import org.w3c.dom.Element;

/*
 *		taxExclusiveAmount: double[0..1]
 *		currencyID: String[0..1]
 */
public class ContractModificationLegalMonetaryTotal {
	private double taxExclusiveAmount;
	private String currencyID;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param cmlmt El cac:ContractModificationLegalMonetaryTotal que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element cmlmt, int POS_UNICO_ELEMENTO){
		this.taxExclusiveAmount = -1;
		this.currencyID = null;
		
		Element tea = (Element) cmlmt.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		if (tea != null){
			this.taxExclusiveAmount = Double.parseDouble(tea.getTextContent());
			this.currencyID = tea.getAttributes().getNamedItem("currencyID").getTextContent();
		}
	}

	public double getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}

	public String getCurrencyID() {
		return currencyID;
	}
}
