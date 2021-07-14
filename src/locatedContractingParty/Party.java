package locatedContractingParty;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.Contact;
import utils.PartyIdentification;
import utils.PartyName;
import utils.PostalAddress;

/*
 * 		websiteURI: String[0..1]
 * 		buyerProfileURIID: String[0..1]
 * 		partyName: PartyName[1]
 * 		partyIdentificationList: PartyIdentification[] [1..3]
 * 		contact: Contact[0..1]
 * 		postalAddress: PostalAddress[0..1]
 */
public class Party {
	private String websiteURI;
	private PartyName partyName;
	private PartyIdentification[] partyIdentificationList;
	private Contact contact;
	private PostalAddress postalAddress;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param p El cac:Party que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element p, int POS_UNICO_ELEMENTO){
		this.websiteURI = null;
		this.partyName = null;
		this.partyIdentificationList = null;
		this.contact = null;
		this.postalAddress = null;
		
		/* Website URI */
		Element wsURI = (Element) p.getElementsByTagName("cbc:WebsiteURI").item(POS_UNICO_ELEMENTO);
		if (wsURI != null){
			this.websiteURI = wsURI.getTextContent();
		}
		
		/* Buyer Profile URI ID */
		Element bpUID = (Element) p.getElementsByTagName("cbc:BuyerProfileURIID").item(POS_UNICO_ELEMENTO);
		if (bpUID != null){
			bpUID.getTextContent();
		}
		
		/* Party Name */
		Element pn = (Element) p.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		try{
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: LocatedContractingParty -> Party -> PARTY NAME no existe\n");
		}
		
		/* Party Identification */
		NodeList piNodeList = p.getElementsByTagName("cac:PartyIdentification");
		if(piNodeList.getLength() > 0){
			this.partyIdentificationList = new PartyIdentification[piNodeList.getLength()];
			for (int i = 0; i < piNodeList.getLength(); i++){
				PartyIdentification partyIdentification = new PartyIdentification();
				
				Element pi = (Element) piNodeList.item(i);
				partyIdentification.readAttributes(pi, POS_UNICO_ELEMENTO);
				
				this.partyIdentificationList[i] = partyIdentification;
			}
		}else{
			System.err.print("ERROR FATAL: LocatedContractingParty -> Party -> PARTY IDENTIFICATION no existe\n");
		}
		
		/* Contact  */
		Element c = (Element) p.getElementsByTagName("cac:Contact").item(POS_UNICO_ELEMENTO);
		if (c != null){
			this.contact = new Contact();
			this.contact.readAttributes(c, POS_UNICO_ELEMENTO);
		}
		
		/* Postal Address */
		Element pa = (Element) p.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		if (pa != null){
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, false);
		}
	}

	public PartyIdentification[] getPartyIdentificationList() {
		return partyIdentificationList;
	}
	
	public PartyName getPartyName(){
		return partyName;
	}
	
	public String getWebsiteURI(){
		return websiteURI;
	}
	
	public PostalAddress getPostalAddress(){
		return postalAddress;
	}
	
	public Contact getContact(){
		return contact;
	}
}
