package tenderingTerms;

import org.w3c.dom.Element;

import utils.Contact;
import utils.PartyName;
import utils.PostalAddress;

/**
 * @params
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

	public void readPartyName(Element arp, int POS_UNICO_ELEMENTO) {
		this.partyName = null;
		
		Element pn = (Element) arp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if(pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> AppealTerms -> AppealReceiverParty -> PARTY NAME no exsite\n");
		}
	}

	public void readPostalAddress(Element arp, int POS_UNICO_ELEMENTO) {
		this.postalAddress = null;
		
		Element pa = (Element) arp.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		if(pa != null){
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, true);
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> AppealTerms -> AppealReceiverParty -> POSTAL ADDRESS no exsite\n");
		}
		
	}

	public void readContact(Element arp, int POS_UNICO_ELEMENTO){
		this.contact = null;
		
		Element contact = (Element) arp.getElementsByTagName("cac:Contact").item(POS_UNICO_ELEMENTO);
		if (contact != null){
			this.contact = new Contact();
			this.contact.readAttributes(contact, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		/* ATTRIBUTES */
		System.out.print("**** APPEAL RECEIVER PARTY ****\n" +
						 "----> WebsiteURI: " + websiteURI + "\n");
		
		/* PARTY NAME */
		if(partyName != null){
			partyName.print();
		}else{
			System.out.println("**** PARTY NAME: null ****\n" +
							"--------------------------------\n");
		}
		
		/* POSTAL ADDRESS*/
		if(postalAddress != null){
			postalAddress.print();
		}else{
			System.out.println("**** POSTAL ADDRESS: null ****\n" +
							"--------------------------------\n");
		}
		
		/* CONTACT */
		if(contact != null){
			contact.print();
		}else{
			System.out.println("**** CONTACT: null ****\n" +
							"--------------------------------\n");
		}
	}
}
