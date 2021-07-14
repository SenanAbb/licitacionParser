package locatedContractingParty;

import org.w3c.dom.Element;

import utils.PartyName;

/*
 *		partyName: PartyName[0..1]
 */
public class ParentLocatedParty {
	private PartyName partyName;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param plp El cac:ParentLocatedParty que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element plp, int POS_UNICO_ELEMENTO){
		this.partyName = null;
		
		Element pn = (Element) plp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if (pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}
	}
}
