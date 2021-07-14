package utils;

import org.w3c.dom.Element;

/*
 *		name: String[1]
 */
public class PartyName {
	private String name;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pn El cac:PartName que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element pn, int POS_UNICO_ELEMENTO){
		this.name = null;
		Element name = (Element) pn.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		
		try{
			this.name = name.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: PartyName -> NAME no existe\n");
		}
	}
	
	public String getName(){
		return name;
	}
}
