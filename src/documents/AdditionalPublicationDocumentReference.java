package documents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/**
 * @params
 *		issueDate: Date[0..1]
 *		documentTypeCode: int[0..1]
 *		attachment: Attachment[0..1]
 */
public class AdditionalPublicationDocumentReference {
	private Date issueDate;
	private String documentTypeCode;
	private Attachment attachment;
	
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
	
	public void readAttachment(Element apdr, int POS_UNICO_ELEMENTO){
		this.attachment = null;
		
		Element att = (Element) apdr.getElementsByTagName("cac:Attachment").item(POS_UNICO_ELEMENTO);
		if (att != null){
			this.attachment = new Attachment();
			this.attachment.readExternalReference(att, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("**** ADDITIONAL PUBLICATION DOCUMENT REFERENCE ****\n" +
				 "----> Issue Date: " + issueDate + "\n" +
				 "----> Document Type Code: " + documentTypeCode + "\n");
		if (attachment != null){
			attachment.print();
		}else{
			System.out.print("**** ATTACHMENT: null ****\n");
		}
		System.out.print("--------------------------------\n");
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
