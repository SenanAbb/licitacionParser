package utils;

import org.w3c.dom.Element;

/**
 * @params
 *		id: String [1]
 *		schemeName: String[1]
 */
public class PartyIdentification {
	private String id, schemeName;

	public void readAttributes(Element pi, int POS_UNICO_ELEMENTO) {
		this.id = null;
		this.schemeName = null;
		
		Element id = (Element) pi.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
			this.schemeName = id.getAttributes().getNamedItem("schemeName").getTextContent();
		}else{
			System.err.print("ERROR FATAL: PartyIdentification -> ID no existe\n");
		}
	}
	
	public void print(){
		System.out.print("----> Party Identification: " + id + " " + "(" + schemeName + ")" + "\n");
	}
	
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public PartyIdentification(){}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getId(){
		return id;
	}
	
	public String getSchemeName(){
		return schemeName;
	}
}
