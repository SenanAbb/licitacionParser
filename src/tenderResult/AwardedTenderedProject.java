package tenderResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 * 		legalMonetaryTotalList: LegalMonetaryTotal[] [0..*]
 *		contractFormalizationPeriod: ContractFormalizationPeriod [0..1]
 */
public class AwardedTenderedProject {
	//private int procurementProjectLotID;
	private LegalMonetaryTotal[] legalMonetaryTotalList;
	private ContractFormalizationPeriod contractFormalizationPeriod;
	
	public void readLegalMoneratyTotalList(Element atp, int POS_UNICO_ELEMENTO) {
		legalMonetaryTotalList = null;
		
		NodeList lmtNodeList = atp.getElementsByTagName("cac:LegalMonetaryTotal");
		if (lmtNodeList.getLength() > 0){
			legalMonetaryTotalList = new LegalMonetaryTotal[lmtNodeList.getLength()];
			
			for (int i = 0; i < lmtNodeList.getLength(); i++){
				LegalMonetaryTotal lmt = new LegalMonetaryTotal();
				lmt.readAttributes((Element) lmtNodeList.item(i), POS_UNICO_ELEMENTO);
				this.legalMonetaryTotalList[i] = lmt;
			}
		}
	}
	
	public void readContractFormalizationPeriod(Element atp, int POS_UNICO_ELEMENTO) {
		contractFormalizationPeriod = null;
		
		Element cfp = (Element) atp.getElementsByTagName("cac:ContractFormalizationPeriod").item(POS_UNICO_ELEMENTO);
		if (cfp != null){
			contractFormalizationPeriod = new ContractFormalizationPeriod();
			
			contractFormalizationPeriod.readAttributes(cfp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("*** AWARDED TENDERED PROJECT ***\n");
//				"--> Procurement Project Lot ID: " + procurementProjectLotID + "\n" +
		
		if (legalMonetaryTotalList != null){
			System.out.print("**** LEGAL MONETARY TOTAL ****\n");
			for (LegalMonetaryTotal l : legalMonetaryTotalList){
				l.print();
			}
		}else{
			System.out.print("**** LEGAL MONETARY TOTAL: null ****\n");
		}
		
		if (contractFormalizationPeriod != null){
			contractFormalizationPeriod.print();
		}else{
			System.out.print("**** CONTRACT FORMALIZATION PERIOD: null ****\n");
		}
		System.out.print("--------------------------------\n");
	}
}
