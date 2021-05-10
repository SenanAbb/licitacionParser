package procurementProject;

import org.w3c.dom.Element;

/**
 * @author senan
 *		itemClassificationCode: int [0..1]
 */
public class RequiredCommodityClassification {
	private int itemClassificationCode;
	
	public void readAttributes(Element rccElement, int POS_UNICO_ELEMENTO) {
		Element itemCode = (Element) rccElement.getElementsByTagName("cbc:ItemClassificationCode").item(POS_UNICO_ELEMENTO);
		if (itemCode != null){	
			this.itemClassificationCode = Integer.parseInt(itemCode.getTextContent());
		}		
	}
	
	public void print(){
		System.out.print("*** REQUIRED COMMODITY CLASSIFICATION ***\n");
		System.out.print("--->Item Clas. Code: " + itemClassificationCode + "\n");
		System.out.print("--------------------------------\n");
	}
	
	public int getItemClassificationCode() {
		return itemClassificationCode;
	}
}
