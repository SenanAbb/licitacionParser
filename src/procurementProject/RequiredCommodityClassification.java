package procurementProject;

import org.w3c.dom.Element;

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
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public RequiredCommodityClassification(){}

	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public int getItemClassificationCode() {
		return itemClassificationCode;
	}
	
	public void setItemClassificationCode(int itemClassificationCode) {
		this.itemClassificationCode = itemClassificationCode;
	}


	
}
