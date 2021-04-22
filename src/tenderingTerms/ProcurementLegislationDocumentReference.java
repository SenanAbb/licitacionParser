package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params 	
 *		id: String[0..1]
 */
public class ProcurementLegislationDocumentReference {
	private String id;
	
	public void readAttributes(Element pldr, int POS_UNICO_ELEMENTO) {
		this.id = null;
		
		Element id = (Element) pldr.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
		}
	}
	
	public void print(){
		System.out.print("*** PROCUREMENT LEGISLATION DOCUMENT REFERENCE ***\n" +
				 "---> ID: " + id + "\n" +
				 "--------------------------------\n");
	}
}
