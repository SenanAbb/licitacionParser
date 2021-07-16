package documents;

import org.w3c.dom.Element;

/*
 *		id: String[0..1]
 *		attachment: Attachment[1]
 */
public class LegalDocumentReference {
	private String id;
	private Attachment attachment;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param ldr El cac:LegalDocumentReference que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element ldr, int POS_UNICO_ELEMENTO){
		this.id = null;
		
		Element id = (Element) ldr.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
		}
	}
	
	/**
	 * Lee el cac:Attachment del documento
	 * 
	 * @param ldr El cac:LegalDocumentReference que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	public String getId() {
		return id;
	}
	
	public Attachment getAttachment() {
		return attachment;
	}
}
