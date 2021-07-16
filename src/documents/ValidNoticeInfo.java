package documents;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 *		noticeTypeCode: String[1]
 *		additionalPublicationStatus: AdditionalPublicationStatus[0..1]
 */
public class ValidNoticeInfo {
	private String noticeTypeCode;
	private AdditionalPublicationStatus[] additionalPublicationStatusList;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param vni El cac:ValidNoticeInfo que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element vni, int POS_UNICO_ELEMENTO){
		this.noticeTypeCode = null;
		
		Element ntc = (Element) vni.getElementsByTagName("cbc-place-ext:NoticeTypeCode").item(POS_UNICO_ELEMENTO);
		try{
			this.noticeTypeCode = ntc.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ValidNoticeInfo -> NOTICE TYPE CODE no existe\n");
		}
	}
	
	/**
	 * Lee el cac:AdditionalPublicationStatus del documento
	 * 
	 * @param vni El cac:ValidNoticeInfo que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	public String getNoticeTypeCode() {
		return noticeTypeCode;
	}
	
	public AdditionalPublicationStatus[] getAdditionalPublicationStatusList() {
		return additionalPublicationStatusList;
	}
}
