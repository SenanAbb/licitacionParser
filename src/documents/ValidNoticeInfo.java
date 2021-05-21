package documents;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 *		noticeTypeCode: String[1]
 *		additionalPublicationStatus: AdditionalPublicationStatus[0..1]
 */
public class ValidNoticeInfo {
	private String noticeTypeCode;
	private AdditionalPublicationStatus[] additionalPublicationStatusList;
	
	public void readAttributes(Element vni, int POS_UNICO_ELEMENTO){
		this.noticeTypeCode = null;
		
		Element ntc = (Element) vni.getElementsByTagName("cbc-place-ext:NoticeTypeCode").item(POS_UNICO_ELEMENTO);
		try{
			this.noticeTypeCode = ntc.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ValidNoticeInfo -> NOTICE TYPE CODE no existe\n");
		}
	}
	
	public void readAdditionalPublicationStatus(Element vni, int POS_UNICO_ELEMENTO){
		this.additionalPublicationStatusList = null;
		
		NodeList apsList = vni.getElementsByTagName("cac-place-ext:AdditionalPublicationStatus");
		if (apsList.getLength() > 0){
			this.additionalPublicationStatusList = new AdditionalPublicationStatus[apsList.getLength()];
			
			for (int i = 0; i < apsList.getLength(); i++){
				AdditionalPublicationStatus aps = new AdditionalPublicationStatus();
				
				Element apsElement = (Element) apsList.item(i);
				aps.readAttributes(apsElement, POS_UNICO_ELEMENTO);
				aps.readAdditionalPublicationDocumentReference(apsElement, POS_UNICO_ELEMENTO);
				aps.readAdditionalPublicationRequest(apsElement, POS_UNICO_ELEMENTO);

				this.additionalPublicationStatusList[i] = aps;
			}
		}
	}
	
	public void print(){
		System.out.print("** VALID NOTICE INFO **\n" +
						 "---> Notice Type Code: " + noticeTypeCode + "\n" +
						 "--------------------------------\n");
		//additionalPublicationStatus.print();
		System.out.print("===============================================================\n");
	}

	
	public String getNoticeTypeCode() {
		return noticeTypeCode;
	}

	
	public AdditionalPublicationStatus[] getAdditionalPublicationStatusList() {
		return additionalPublicationStatusList;
	}
}
