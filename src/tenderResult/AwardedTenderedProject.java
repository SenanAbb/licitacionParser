package tenderResult;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * 		legalMonetaryTotalList: LegalMonetaryTotal[] [0..*]
 *		contractFormalizationPeriod: ContractFormalizationPeriod [0..1]
 */
public class AwardedTenderedProject {
	private String procurementProjectLotID;
	private LegalMonetaryTotal[] legalMonetaryTotalList;
	private ContractFormalizationPeriod contractFormalizationPeriod;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param atp El cac:AwardedTenderedProject que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element atp, int POS_UNICO_ELEMENTO){
		procurementProjectLotID = null;
		
		Element pplId = (Element) atp.getElementsByTagName("cbc:ProcurementProjectLotID").item(POS_UNICO_ELEMENTO);
		if(pplId != null){
			procurementProjectLotID = pplId.getTextContent();
		}
	}
	
	/**
	 * Lee el cac:LegalMonetaryTotal del documento
	 * 
	 * @param atp El cac:AwardedTenderedProject que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:ContractFormalizationPeriod del documento
	 * 
	 * @param atp El cac:AwardedTenderedProject que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readContractFormalizationPeriod(Element atp, int POS_UNICO_ELEMENTO) {
		contractFormalizationPeriod = null;
		
		Element cfp = (Element) atp.getElementsByTagName("cac:ContractFormalizationPeriod").item(POS_UNICO_ELEMENTO);
		if (cfp != null){
			contractFormalizationPeriod = new ContractFormalizationPeriod();
			
			contractFormalizationPeriod.readAttributes(cfp, POS_UNICO_ELEMENTO);
		}
	}

	public LegalMonetaryTotal[] getLegalMonetaryTotalList() {
		return legalMonetaryTotalList;
	}

	public String getProcurementProjectLotID() {
		return procurementProjectLotID;
	}
}
