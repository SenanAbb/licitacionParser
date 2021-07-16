package tenderingTerms;

import org.w3c.dom.Element;

import utils.Contact;
import utils.PartyName;
import utils.PostalAddress;

/*
 * 		websiteURI: String[0..1]
 * 		partyName: PartyName[1]
 * 		postalAddress: PostalAddress[1]
 * 		contact: Contact[0..1]
 */
public class AppealReceiverParty {
	private String websiteURI;
	private PartyName partyName;
	private PostalAddress postalAddress;
	private Contact contact;
	
	public void readAttributes(Element arp, int POS_UNICO_ELEMENTO) {
		this.websiteURI = null;
		
		Element webURI = (Element) arp.getElementsByTagName("cbc:WebsiteURI").item(POS_UNICO_ELEMENTO);
		if (webURI != null){
			this.websiteURI = webURI.getTextContent();
		}
	}

	/**
	 * Lee el cac:PartyName del documento
	 * 
	 * @param arp El cac:AppealReceiverParty que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readPartyName(Element arp, int POS_UNICO_ELEMENTO) {
		this.partyName = null;
		
		Element pn = (Element) arp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		try{
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> AppealTerms -> AppealReceiverParty -> PARTY NAME no exsite\n");
		}
	}

	/**
	 * Lee el cac:PostalAddress del documento
	 * 
	 * @param arp El cac:AppealReceiverParty que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readPostalAddress(Element arp, int POS_UNICO_ELEMENTO) {
		this.postalAddress = null;
		
		Element pa = (Element) arp.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		try{
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, true);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> AppealTerms -> AppealReceiverParty -> POSTAL ADDRESS no exsite\n");
		}
	}

	/**
	 * Lee el cac:Contact del documento
	 * 
	 * @param arp El cac:AppealReceiverParty que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readContact(Element arp, int POS_UNICO_ELEMENTO){
		this.contact = null;
		
		Element contact = (Element) arp.getElementsByTagName("cac:Contact").item(POS_UNICO_ELEMENTO);
		if (contact != null){
			this.contact = new Contact();
			this.contact.readAttributes(contact, POS_UNICO_ELEMENTO);
		}
	}
}
