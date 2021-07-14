package procurementProject;

import org.w3c.dom.Element;

/*
 * 		descrtiption: String [0..1]
 */
public class OptionValidityPeriod {
	private String description;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param ovp El cac:OptionValidityPeriod que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element ovp, int POS_UNICO_ELEMENTO) {
		this.description = null;
		
		Element description = (Element) ovp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent().trim();
		}
	}
	
	public String getDescription() {
		return description;
	}
}
