package procurementProject;

public class RequiredCommodityClassification {
	private int itemClassificationCode;
	
	public void print(){
		System.out.print("** REQUIRED COMMODITY CLASSIFICATION **\n");
		System.out.print("-->Item Clas. Code: " + itemClassificationCode + "\n");
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
