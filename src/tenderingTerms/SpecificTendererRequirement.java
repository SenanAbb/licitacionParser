package tenderingTerms;

import org.w3c.dom.Element;

/*
 *		requirementTypeCode: int[0..1]
 */
public class SpecificTendererRequirement {
	private int requirementTypeCode;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param str El cac:SpecificTendererRequirement que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element str, int POS_UNICO_ELEMENTO){
		this.requirementTypeCode = -1;
		
		Element rtc = (Element) str.getElementsByTagName("cbc:RequirementTypeCode").item(POS_UNICO_ELEMENTO);
		if (rtc != null){
			this.requirementTypeCode = Integer.parseInt(rtc.getTextContent());
		}
	}

	public int getRequirementTypeCode() {
		return requirementTypeCode;
	}
}
