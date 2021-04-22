package locatedContractingParty;

import org.w3c.dom.Element;

import utils.PartyName;

/**
 * @params
 *		partyName: PartyName[0..1]
 */
public class ParentLocatedParty {
	private PartyName partyName;
	
	public void readAttributes(Element plp, int POS_UNICO_ELEMENTO){
		this.partyName = null;
		
		Element pn = (Element) plp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if (pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("*** PARENT LOCATED PARTY ***\n");
		partyName.print();
		System.out.print("--------------------------------\n");
	}
}
