package tenderingTerms;

import org.w3c.dom.Element;

/*
 *		id: String [0..1]
 */
public class Language {
	private String id;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param languageElement El cac:Language que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element languageElement, int POS_UNICO_ELEMENTO) {
		this.id = null;
		
		Element id = (Element) languageElement.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if(id != null){
			this.id = id.getTextContent();
		}
	}

	public String getId() {
		return id;
	}
}
