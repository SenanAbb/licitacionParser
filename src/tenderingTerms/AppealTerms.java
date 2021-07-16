package tenderingTerms;

import org.w3c.dom.Element;

/*
 * 		appealInformationParty: AppealInformationParty[0..1]
 * 		appealReceiverParty: AppealReceiverParty[0..1]
 * 		presentationPeriod: PresentationPeriod[0..1]
 *		mediationParty: MediationParty[0..1]
 */
public class AppealTerms {
	private AppealInformationParty appealInformationParty;
	private AppealReceiverParty appealReceiverParty;
	private PresentationPeriod presentationPeriod;
	private MediationParty mediationParty;

	/**
	 * Lee el cac:AppealInformationParty del documento
	 * 
	 * @param at El cac:AppealTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAppealInformationParty(Element at, int POS_UNICO_ELEMENTO) {
		this.appealInformationParty = null;
		
		Element aip = (Element) at.getElementsByTagName("cac:AppealInformationParty").item(POS_UNICO_ELEMENTO);
		if (aip != null){
			this.appealInformationParty = new AppealInformationParty();
			
			this.appealInformationParty.readAttributes(aip, POS_UNICO_ELEMENTO);
			this.appealInformationParty.readPartyName(aip, POS_UNICO_ELEMENTO);
			this.appealInformationParty.readPostalAddress(aip, POS_UNICO_ELEMENTO);
			this.appealInformationParty.readContact(aip, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee el cac:AppealReceiverParty del documento
	 * 
	 * @param at El cac:AppealTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAppealReceiverParty(Element at, int POS_UNICO_ELEMENTO) {
		this.appealReceiverParty= null;
		
		Element arp = (Element) at.getElementsByTagName("cac:AppealReceiverParty").item(POS_UNICO_ELEMENTO);
		if (arp != null){
			this.appealReceiverParty = new AppealReceiverParty();
			
			this.appealReceiverParty.readAttributes(arp, POS_UNICO_ELEMENTO);
			this.appealReceiverParty.readPartyName(arp, POS_UNICO_ELEMENTO);
			this.appealReceiverParty.readPostalAddress(arp, POS_UNICO_ELEMENTO);
			this.appealReceiverParty.readContact(arp, POS_UNICO_ELEMENTO);
		}
	}

	/**
	 * Lee el cac:PresentationPeriod del documento
	 * 
	 * @param at El cac:AppealTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readPresentationPeriod(Element at, int POS_UNICO_ELEMENTO) {
		this.presentationPeriod = null;
		
		Element pp = (Element) at.getElementsByTagName("cac:PresentationPeriod").item(POS_UNICO_ELEMENTO);
		if (pp != null){
			this.presentationPeriod = new PresentationPeriod();
			this.presentationPeriod.readAttributes(pp, POS_UNICO_ELEMENTO);
		}
	}

	/**
	 * Lee el cac:MediationParty del documento
	 * 
	 * @param at El cac:AppealTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readMediationParty(Element at, int POS_UNICO_ELEMENTO) {
		this.mediationParty= null;
		
		Element mp = (Element) at.getElementsByTagName("cac:MediationParty").item(POS_UNICO_ELEMENTO);
		if (mp != null){
			this.mediationParty = new MediationParty();
			
			this.mediationParty.readAttributes(mp, POS_UNICO_ELEMENTO);
			this.mediationParty.readPartyName(mp, POS_UNICO_ELEMENTO);
			this.mediationParty.readPostalAddress(mp, POS_UNICO_ELEMENTO);
			this.mediationParty.readContact(mp, POS_UNICO_ELEMENTO);
		}
	}
}
