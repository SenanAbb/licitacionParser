package tenderingTerms;

import org.w3c.dom.Element;

/*
 *		name: String[0..1]
 *		codeValue: String[0..1]
 *		description: String[0..1]
 */
public class ClassificationCategory {
	private String name, codeValue, description;

	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param cc El cac:ClassificationCategory que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element cc, int POS_UNICO_ELEMENTO) {
		this.name = null;
		this.codeValue = null;
		this.description = null;
		
		/* NAME */
		Element name = (Element) cc.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if(name != null){
			this.name = name.getTextContent();
		}
		
		/* CODE VALUE */
		Element cv = (Element) cc.getElementsByTagName("cbc:CodeValue").item(POS_UNICO_ELEMENTO);
		if(cv != null){
			this.codeValue = cv.getTextContent();
		}
		
		/* DESCRPTION */
		Element description = (Element) cc.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if(description != null){
			this.description = description.getTextContent();
		}
	}
	
	public String getCodeValue() {
		return codeValue;
	}
}
