package tenderingTerms;

import org.w3c.dom.Element;

import utils.PartyName;
import utils.PostalAddress;

/**
 * @params
 * 		websiteURI: String[0..1]
 * 		partyName: PartyName[1]
 * 		postalAddress: PostalAddress[1]
 */
public class TenderRecipientParty {
	private String websiteURI;
	private PartyName partyName;
	private PostalAddress postalAddress;
	
	public void readAttributes(Element trp, int POS_UNICO_ELEMENTO) {
		this.websiteURI = null;
		
		Element webURI = (Element) trp.getElementsByTagName("cbc:WebsiteURI").item(POS_UNICO_ELEMENTO);
		if (webURI != null){
			this.websiteURI = webURI.getTextContent();
		}
	}

	public void readPartyName(Element trp, int POS_UNICO_ELEMENTO) {
		this.partyName = null;
		
		Element pn = (Element) trp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if(pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> TenderRecipientParty -> PARTY NAME no exsite\n");
		}
	}
	
	public void readPostalAddress(Element trp, int POS_UNICO_ELEMENTO) {
		this.postalAddress = null;
		
		Element pa = (Element) trp.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		if(pa != null){
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, true);
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> TenderRecipientParty -> POSTAL ADDRESS no exsite\n");
		}
	}
	
	public void print(){
		/* ATTRIBUTES */
		System.out.print("*** TENDER RECIPIENT PARTY ***\n" +
						 "---> WebsiteURI: " + websiteURI + "\n");
		
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
	}
}
