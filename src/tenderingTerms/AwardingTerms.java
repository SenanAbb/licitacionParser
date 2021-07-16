package tenderingTerms;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * 		awardingCriteriaList: AwardingCriteria[] [0..*]
 */
public class AwardingTerms {
	private AwardingCriteria[] awardingCriteriaList;
	
	/**
	 * Lee el cac:AwardingCriteria del documento
	 * 
	 * @param at El cac:AwardingTerms que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAwardingCriteria(Element at, int POS_UNICO_ELEMENTO){
		this.awardingCriteriaList = null;
		
		NodeList acNodeList = at.getElementsByTagName("cac:AwardingCriteria");
		if(acNodeList.getLength() > 0){
			this.awardingCriteriaList = new AwardingCriteria[acNodeList.getLength()];
			
			for (int i = 0; i < acNodeList.getLength(); i++){
				AwardingCriteria ac = new AwardingCriteria();
				
				Element acElement = (Element) acNodeList.item(i);
				ac.readAttributes(acElement, POS_UNICO_ELEMENTO);
				
				this.awardingCriteriaList[i] = ac;
			}
		}		
	}

	public AwardingCriteria[] getAwardingCriteriaList() {
		return awardingCriteriaList;
	}
}
