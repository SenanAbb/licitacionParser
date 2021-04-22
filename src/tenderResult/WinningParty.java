package tenderResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.PartyIdentification;
import utils.PartyName;

/**
 * @author senan
 *		partyIdentificationList: PartyIdentification[] [1..3]
 *		partyName: PartyName [1]
 */
public class WinningParty {
	private PartyIdentification[] partyIdentificationList;
	private PartyName partyName;
	
	public void readPartyIdentificationList(Element wp, int POS_UNICO_ELEMENTO){
		this.partyIdentificationList = null;
		
		NodeList piNodeList = wp.getElementsByTagName("cac:PartyIdentification");
		if (piNodeList.getLength() > 0){
			this.partyIdentificationList = new PartyIdentification[piNodeList.getLength()];
			
			for (int i = 0; i < piNodeList.getLength(); i++){
				PartyIdentification p = new PartyIdentification();
				
				p.readAttributes((Element) piNodeList.item(i), POS_UNICO_ELEMENTO);
				
				this.partyIdentificationList[i] = p;
			}
		}else{
			System.err.print("ERROR FATAL: TenderResult -> WinningParty -> PARTY IDENTIFICATION no existe\n");
		}
		
	}
	
	public void readPartyName(Element wp, int POS_UNICO_ELEMENTO){
		this.partyName = null;
		
		Element pn = (Element) wp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if (pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: TenderResult -> WinningParty -> PARTY NAME no existe\n");
		}
	}
	
	public void print(){
		System.out.print("*** WINNING PARTY ***\n");
		for (PartyIdentification p : partyIdentificationList){
			p.print();
		}
		partyName.print();
	}
}
