package documents;

import org.w3c.dom.Element;

/**
 * @params
 *		generalDocumentDocumentReference: GeneralDocumentDocumentReference[0..1]
 */
public class GeneralDocument {
	private GeneralDocumentDocumentReference generalDocumentDocumentReference;
	
	public void readGeneralDocumentDocumentReference(Element gd, int POS_UNICO_ELEMENTO){
		this.generalDocumentDocumentReference = null;
		
		Element gddr = (Element) gd.getElementsByTagName("cac-place-ext:GeneralDocumentDocumentReference").item(POS_UNICO_ELEMENTO);
		if (gddr != null){
			this.generalDocumentDocumentReference = new GeneralDocumentDocumentReference();
			this.generalDocumentDocumentReference.readAttributes(gddr, POS_UNICO_ELEMENTO);
			this.generalDocumentDocumentReference.readAttachment(gddr, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		if (generalDocumentDocumentReference != null){
			generalDocumentDocumentReference.print();
			System.out.print("===============================================================\n");
		}else{
			System.out.print("*** GENERAL DOCUMENT DOCUMENT REFERENCE: null ***\n");
		}
	}
}
