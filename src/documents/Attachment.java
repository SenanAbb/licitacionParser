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
		try{
			this.externalReference = new ExternalReference();
			this.externalReference.readAttributes(er, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Attachment -> EXTERNAL REFERENCE no existe\n");
		}
	}
	
	public void print(){
		System.out.print("*** ATTACHMENT ***\n");
		externalReference.print();
	}
}
