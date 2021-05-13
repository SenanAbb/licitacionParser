package locatedContractingParty;

import org.w3c.dom.Element;

/**
 * @params
 * 		contractingPartyTypeCode: int [1]
 * 		parentLocatedParty: ParentLocatedParty [0..1]
 * 		party: Party [1]
 */
public class LocatedContractingParty {
	private int contractingPartyTypeCode;
	private ParentLocatedParty parentLocatedParty;
	private Party party;
	
	// ID de la base de datos
	int located_contracting_party;
	
	public void readAttributes(Element lcp, int POS_UNICO_ELEMENTO){
		this.contractingPartyTypeCode = -1;
		
		Element contractingPartyTypeCode = (Element) lcp.getElementsByTagName("cbc:ContractingPartyTypeCode").item(POS_UNICO_ELEMENTO);
		try{
			this.contractingPartyTypeCode = Integer.parseInt(contractingPartyTypeCode.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> LocatedContractingParty -> CONTRACTING PARTY TYPE CODE no existe\n");
		}
	}
	
	public void readParentLocatedParty(Element lcp, int POS_UNICO_ELEMENTO){
		this.parentLocatedParty = null;
		
		Element parentLocatedParty = (Element) lcp.getElementsByTagName("cac:ParentLocatedParty").item(POS_UNICO_ELEMENTO);
		if (parentLocatedParty != null){
			this.parentLocatedParty = new ParentLocatedParty();
			this.parentLocatedParty.readAttributes(parentLocatedParty, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readParty(Element lcp, int POS_UNICO_ELEMENTO){
		this.party = null;
		
		Element party = (Element) lcp.getElementsByTagName("cac:Party").item(POS_UNICO_ELEMENTO);
		try{	
			this.party = new Party();
			this.party.readAttributes(party, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> LocatedContractingParty -> PARTY no existe\n");
		}
	}
	
	public void print(){
		System.out.print("** LOCATED CONTRACTING PARTY **\n" +
						 "--> Contracting Party Type Code: " + contractingPartyTypeCode + "\n"+
						 "--------------------------------\n");
		
		if (parentLocatedParty != null){
			parentLocatedParty.print();
		}else{
			System.out.print("*** PARENT LOCATED PARTY: null ***\n" + 
					"--------------------------------\n");
		}
		
		if (party != null){
			party.print();
		}else{
			System.out.print("*** PARTY: null ***\n" + 
					"--------------------------------\n");
		}
		System.out.print("===============================================================\n");
	}

	public Party getParty(){
		return party;
	}
	
	public int getContractingPartyTypeCode() {
		return contractingPartyTypeCode;
	}
}
