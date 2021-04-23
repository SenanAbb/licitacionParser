package documents;

import org.w3c.dom.Element;

/**
 * @params
 *		id: String[0..1]
 *		attachment: Attachment[1]
 */
public class LegalDocumentReference {
	private String id;
	private Attachment attachment;
	
	public void readAttributes(Element ldr, int POS_UNICO_ELEMENTO){
		this.id = null;
		
		Element id = (Element) ldr.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
		}
	}
	
	public void readAttachment(Element ldr, int POS_UNICO_ELEMENTO){
		this.attachment = null;
		
		Element att = (Element) ldr.getElementsByTagName("cac:Attachment").item(POS_UNICO_ELEMENTO);
		try{
			this.attachment = new Attachment();
			this.attachment.readExternalReference(att, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> LegalDocumentReference -> ATTACHMENT no existe\n");
		}
	}
	
	public void print() {
		System.out.print("** LEGAL DOCUMENT REFERENCE **\n" +
						 "--> ID: " + id + "\n" +
						 "--------------------------------\n");
		attachment.print();
		System.out.print("===============================================================\n");
	}
}
