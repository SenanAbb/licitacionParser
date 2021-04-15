package utils;

import org.w3c.dom.Element;

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
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public PartyName(){}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getName(){
		return name;
	}
}
