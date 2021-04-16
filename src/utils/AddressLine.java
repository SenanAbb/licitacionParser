package utils;

import org.w3c.dom.Element;

public class AddressLine {
	private String line;

	public void readAttributes(Element al, int POS_UNICO_ELEMENTO) {
		Element addressLine = (Element) al.getElementsByTagName("cbc:Line").item(POS_UNICO_ELEMENTO);
		if (addressLine != null){
			this.line = addressLine.getTextContent();
		}else{
			System.err.print("ERROR FATAL: Postal Address -> AddressLine -> LINE no existe\n");
		}
	}
	
	public void print(){
		System.out.print("**** ADDRESS LINE ****\n" +
						 "----> Address Line: " + line + "\n");
	}
	
	
	public AddressLine(){}
}
