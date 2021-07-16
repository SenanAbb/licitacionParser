package tenderingProcess;

import org.w3c.dom.Element;

/*
 *		reasonCode: int[0..1]
 *		description: String[0..1]
 */
public class ProcessJustification {
	private String reasonCode;
	private String description;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pj El cac:ProcessJustification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element pj, int POS_UNICO_ELEMENTO) {
		this.reasonCode = null;
		this.description = null;
		
		Element rc = (Element) pj.getElementsByTagName("cbc:ReasonCode").item(POS_UNICO_ELEMENTO);
		if (rc != null){
			this.reasonCode = rc.getTextContent();
		}
		
		Element desc = (Element) pj.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (desc != null){
			this.description = desc.getTextContent();
		}
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public String getDescription() {
		return description;
	}
}
