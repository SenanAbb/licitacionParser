package documents;

import org.w3c.dom.Element;

/**
 * @params
 *		noticeTypeCode: String[1]
 *		additionalPublicationStatus: AdditionalPublicationStatus[0..1]
 */
public class ValidNoticeInfo {
	private String noticeTypeCode;
	private AdditionalPublicationStatus additionalPublicationStatus;
	
	public void readAttributes(Element vni, int POS_UNICO_ELEMENTO){
		this.noticeTypeCode = null;
		
		Element ntc = (Element) vni.getElementsByTagName("cbc-place-ext:NoticeTypeCode").item(POS_UNICO_ELEMENTO);
		if (ntc != null){
			this.noticeTypeCode = ntc.getTextContent();
		}else{
			System.err.print("ERROR FATAL: ValidNoticeInfo -> NOTICE TYPE CODE no existe\n");
		}
	}
	
	public void readAdditionalPublicationStatus(Element vni, int POS_UNICO_ELEMENTO){
		this.additionalPublicationStatus = null;
		
		Element aps = (Element) vni.getElementsByTagName("cac-place-ext:AdditionalPublicationStatus").item(POS_UNICO_ELEMENTO);
		if (aps != null){
			this.additionalPublicationStatus = new AdditionalPublicationStatus();
			this.additionalPublicationStatus.readAttributes(aps, POS_UNICO_ELEMENTO);
			this.additionalPublicationStatus.readAdditionalPublicationDocumentReference(aps, POS_UNICO_ELEMENTO);
			this.additionalPublicationStatus.readAdditionalPublicationRequest(aps, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("** VALID NOTICE INFO **\n" +
						 "---> Notice Type Code: " + noticeTypeCode + "\n" +
						 "--------------------------------\n");
		additionalPublicationStatus.print();
		System.out.print("===============================================================\n");
	}
}
