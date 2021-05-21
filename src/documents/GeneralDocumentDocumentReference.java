package documents;

import org.w3c.dom.Element;

/**
 * @params
 * 		id: String[1]
 *		attachment: Attachment[1]
 */
public class GeneralDocumentDocumentReference {
	private String id;
	private Attachment attachment;
	
	public void readAttributes(Element gddr, int POS_UNICO_ELEMENTO){
		this.id = null;
		
		Element id = (Element) gddr.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		try{
			this.id = id.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: GeneralDocument -> GeneralDocumentDoumentReference -> ID no existe\n");
		}
	}
	
	public void readAttachment(Element gddr, int POS_UNICO_ELEMENTO){
		this.attachment = null;
		
		Element att = (Element) gddr.getElementsByTagName("cac:Attachment").item(POS_UNICO_ELEMENTO);
		try{
			this.attachment = new Attachment();
			this.attachment.readExternalReference(att, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: GeneralDocument -> GeneralDocumentDoumentReference -> ATTACHMENT no existe\n");
		}
	}
	
	public void print(){
		System.out.print("*** GENERAL DOCUMENT DOCUMENT REFERENCE ***\n" +
						 "---> ID: " + id + "\n");
		attachment.print();
	}

	public String getId() {
		return id;
	}

	public Attachment getAttachment() {
		return attachment;
	}
}
