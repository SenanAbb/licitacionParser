package utils;

import org.w3c.dom.Element;

/*
 *		id: String [1]
 *		schemeName: String[1]
 */
public class PartyIdentification {
	private String id, schemeName;

	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pi El cac:PartyIdentification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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

	public String getId() {
		return id;
	}

	public String getSchemeName() {
		return schemeName;
	}
}
