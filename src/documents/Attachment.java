package documents;

import org.w3c.dom.Element;
 
/*
  *		externalReference: ExternalReference[1]
  */
public class Attachment {
	private ExternalReference externalReference;

	/**
	 * Lee el cac:ExternalReference del documento
	 * 
	 * @param att El cac:Attachment que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readExternalReference(Element att, int POS_UNICO_ELEMENTO) {
		this.externalReference = null;
		
		Element er = (Element) att.getElementsByTagName("cac:ExternalReference").item(POS_UNICO_ELEMENTO);
		try{
			this.externalReference = new ExternalReference();
			this.externalReference.readAttributes(er, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Attachment -> EXTERNAL REFERENCE no existe\n");
		}
	}
	
	public ExternalReference getExternalReference() {
		return externalReference;
	}
}
