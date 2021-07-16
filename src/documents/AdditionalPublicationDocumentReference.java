package documents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 *		issueDate: Date[0..1]
 *		documentTypeCode: int[0..1]
 *		attachment: Attachment[0..1]
 */
public class AdditionalPublicationDocumentReference {
	private Date issueDate;
	private String documentTypeCode;
	private Attachment attachment;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param apdr El cac:AdditionalPublicationDocumentReference que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element apdr, int POS_UNICO_ELEMENTO){
		this.issueDate = null;
		this.documentTypeCode = null;
		
		Element id = (Element) apdr.getElementsByTagName("cbc:IssueDate").item(POS_UNICO_ELEMENTO);
		if (id != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date issueDate = null;
			try {
				issueDate = format.parse(id.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.issueDate = issueDate;
		}
		
		Element dtc = (Element) apdr.getElementsByTagName("cbc:DocumentTypeCode").item(POS_UNICO_ELEMENTO);
		if (dtc != null){
			this.documentTypeCode = dtc.getTextContent();
		}
	}
	
	/**
	 * Lee el cac:Attachment del documento
	 * 
	 * @param apdr El cac:AdditionalPublicationDocumentReference que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttachment(Element apdr, int POS_UNICO_ELEMENTO){
		this.attachment = null;
		
		Element att = (Element) apdr.getElementsByTagName("cac:Attachment").item(POS_UNICO_ELEMENTO);
		if (att != null){
			this.attachment = new Attachment();
			this.attachment.readExternalReference(att, POS_UNICO_ELEMENTO);
		}
	}

	public String getDocumentTypeCode() {
		return documentTypeCode;
	}

	public Attachment getAttachment() {
		return attachment;
	}

	public java.sql.Date getIssueDate() {
		if (issueDate != null){
			return new java.sql.Date(issueDate.getTime());	
		}else{
			return null;
		}
	}
}
