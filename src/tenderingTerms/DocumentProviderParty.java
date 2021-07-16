package tenderingTerms;

import org.w3c.dom.Element;

import utils.PartyName;
import utils.PostalAddress;

/*
 * 		websiteURI: String[0..1]
 * 		partyName: PartyName[1]
 * 		postalAddress: PostalAddress[1]
 */
public class DocumentProviderParty {
	private String websiteURI;
	private PartyName partyName;
	private PostalAddress postalAddress;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param dpp El cac:DocumentProviderParty que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element dpp, int POS_UNICO_ELEMENTO) {
		this.websiteURI = null;
		
		Element webURI = (Element) dpp.getElementsByTagName("cbc:WebsiteURI").item(POS_UNICO_ELEMENTO);
		if (webURI != null){
			this.websiteURI = webURI.getTextContent();
		}
	}
	
	/**
	 * Lee el cac:PartyName del documento
	 * 
	 * @param dpp El cac:DocumentProviderParty que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readPartyName(Element dpp, int POS_UNICO_ELEMENTO) {
		this.partyName = null;
		
		Element pn = (Element) dpp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		try{
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> DocumentProviderParty -> PARTY NAME no exsite\n");
		}
	}

	/**
	 * Lee el cac:PostalAddress del documento
	 * 
	 * @param dpp El cac:DocumentProviderParty que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readPostalAddress(Element dpp, int POS_UNICO_ELEMENTO) {
		this.postalAddress = null;
		
		Element pa = (Element) dpp.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		try {
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, true);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> DocumentProviderParty -> POSTAL ADDRESS no exsite\n");
		}
	}
}
