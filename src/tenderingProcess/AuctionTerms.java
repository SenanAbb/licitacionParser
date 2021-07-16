package tenderingProcess;

import org.w3c.dom.Element;

/*
 *		auctionConstraintIndicator: boolean[0..1]
 */
public class AuctionTerms {
	private boolean auctionConstraintIndicator;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param at El cac:AuctionTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element at, int POS_UNICO_ELEMENTO) {
		Element aci = (Element) at.getElementsByTagName("cbc:AuctionConstraintIndicator").item(POS_UNICO_ELEMENTO);
		
		if (aci != null){
			this.auctionConstraintIndicator = Boolean.parseBoolean(aci.getTextContent());
		}
	}
	
	public boolean getAuctionConstraintIndicator() {
		return auctionConstraintIndicator;
	}
}
