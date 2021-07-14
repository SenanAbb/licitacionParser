package procurementProject;

import org.w3c.dom.Element;

/*
 *		itemClassificationCode: int [0..1]
 */
public class RequiredCommodityClassification {
	private int itemClassificationCode;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param rccElement El cac:RequiredCommodityClassification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element rccElement, int POS_UNICO_ELEMENTO) {
		Element itemCode = (Element) rccElement.getElementsByTagName("cbc:ItemClassificationCode").item(POS_UNICO_ELEMENTO);
		if (itemCode != null){	
			this.itemClassificationCode = Integer.parseInt(itemCode.getTextContent());
		}		
	}
	
	public int getItemClassificationCode() {
		return itemClassificationCode;
	}
}
