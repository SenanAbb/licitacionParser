package documents;

import org.w3c.dom.Element;

/**
 * @params
 *		id: String[0..1]
 *		attachment: Attachment[1]
 */
public class TechnicalDocumentReference {
	private String id;
	private Attachment attachment;
	
	public void readAttributes(Element tdr, int POS_UNICO_ELEMENTO){
		this.id = null;
		
		Element id = (Element) tdr.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
		}
	}
	
	public void readAttachment(Element tdr, int POS_UNICO_ELEMENTO){
		this.attachment = null;
		
		Element att = (Element) tdr.getElementsByTagName("cac:Attachment").item(POS_UNICO_ELEMENTO);
		try{
			this.attachment = new Attachment();
			this.attachment.readExternalReference(att, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> TechnicalDocumentReference -> ATTACHMENT no existe\n");
		}
	}
	
	public void print() {
		System.out.print("** TECHNICAL DOCUMENT REFERENCE **\n" +
						 "--> ID: " + id + "\n" +
						 "--------------------------------\n");
		attachment.print();
		System.out.print("===============================================================\n");
	}
}
