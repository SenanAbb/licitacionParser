package tenderingProcess;

import org.w3c.dom.Element;

/**
 * @params
 *		auctionConstraintIndicator: boolean[0..1]
 */
public class AuctionTerms {
	private boolean auctionConstraintIndicator;
	
	public void readAttributes(Element tp, int POS_UNICO_ELEMENTO) {
		Element aci = (Element) tp.getElementsByTagName("cbc:AuctionConstraintIndicator").item(POS_UNICO_ELEMENTO);
		
		if (aci != null){
			this.auctionConstraintIndicator = Boolean.parseBoolean(aci.getTextContent());
		}
	}
	
	public void print(){
		System.out.print("*** AUCTION TERMS ***\n" +
						 "---> Auction Constraint Indicator: " + auctionConstraintIndicator + "\n" +
						 "--------------------------------\n");
	}
	
	public boolean getAuctionConstraintIndicator() {
		return auctionConstraintIndicator;
	}
}
