package documents;

import org.w3c.dom.Element;
 
/** 
  * @params
  *		externalReference: ExternalReference[1]
  */
public class Attachment {
	private ExternalReference externalReference;

	public void readExternalReference(Element att, int POS_UNICO_ELEMENTO) {
		this.externalReference = null;
		
		Element er = (Element) att.getElementsByTagName("cac:ExternalReference").item(POS_UNICO_ELEMENTO);
		if (er != null){
			this.externalReference = new ExternalReference();
			this.externalReference.readAttributes(er, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: Attachment -> EXTERNAL REFERENCE no existe\n");
		}
	}
	
	public void print(){
		System.out.print("*** ATTACHMENT ***\n");
		externalReference.print();
	}
}
