package documents;

import org.w3c.dom.Element;

/*
 *		generalDocumentDocumentReference: GeneralDocumentDocumentReference[0..1]
 */
public class GeneralDocument {
	private GeneralDocumentDocumentReference generalDocumentDocumentReference;
	
	/**
	 * Lee el cac:GeneralDocumentDocumentReference del documento
	 * 
	 * @param gd El cac:GeneralDocument que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readGeneralDocumentDocumentReference(Element gd, int POS_UNICO_ELEMENTO){
		this.generalDocumentDocumentReference = null;
		
		Element gddr = (Element) gd.getElementsByTagName("cac-place-ext:GeneralDocumentDocumentReference").item(POS_UNICO_ELEMENTO);
		if (gddr != null){
			this.generalDocumentDocumentReference = new GeneralDocumentDocumentReference();
			this.generalDocumentDocumentReference.readAttributes(gddr, POS_UNICO_ELEMENTO);
			this.generalDocumentDocumentReference.readAttachment(gddr, POS_UNICO_ELEMENTO);
		}
	}

	public GeneralDocumentDocumentReference getGeneralDocumentDocumentReference() {
		return generalDocumentDocumentReference;
	}
}
