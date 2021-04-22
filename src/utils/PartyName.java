package utils;

import org.w3c.dom.Element;

/**
 * @params
 *		name: String[1]
 */
public class PartyName {
	private String name;
	
	public void readAttributes(Element pn, int POS_UNICO_ELEMENTO){
		this.name = null;
		
		Element name = (Element) pn.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if (name != null){
			this.name = name.getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderResult -> WinningParty -> PartyName -> NAME no existe\n");
		}
	}
	
	public void print(){
		System.out.print("**** PARTY NAME ****\n" +
						 "----> Party Name: " + name + "\n" +
						 "--------------------------------\n");
	}
}
