package documents;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 *		publicationMediaName: String[0..1]
 *		additionalPublicationDocumentReferenceList: AdditionalPublicationDocumentReference[] [0..*]
 *		additionalPublicationRequestList: AdditionalPublicationRequest[] [0..*]
 */
public class AdditionalPublicationStatus {
	private String publicationMediaName;
	private AdditionalPublicationDocumentReference[] additionalPublicationDocumentReferenceList;
	private AdditionalPublicationRequest[] additionalPublicationRequestList;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param aps El cac:AdditionalPublicationStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element aps, int POS_UNICO_ELEMENTO){
		this.publicationMediaName = null;
		
		Element pmn = (Element) aps.getElementsByTagName("cbc-place-ext:PublicationMediaName").item(POS_UNICO_ELEMENTO);
		if (pmn != null){
			this.publicationMediaName = pmn.getTextContent();
		}
	}
	
	/**
	 * Lee el cac:AdditionalPublicationDocumentReference del documento
	 * 
	 * @param aps El cac:AdditionalPublicationStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAdditionalPublicationDocumentReference(Element aps, int POS_UNICO_ELEMENTO){
		this.additionalPublicationDocumentReferenceList = null;
		
		NodeList apdrNodeList = aps.getElementsByTagName("cac-place-ext:AdditionalPublicationDocumentReference");
		if (apdrNodeList.getLength() > 0){
			this.additionalPublicationDocumentReferenceList = new AdditionalPublicationDocumentReference[apdrNodeList.getLength()];
			
			for (int i = 0; i < apdrNodeList.getLength(); i++){
				AdditionalPublicationDocumentReference apdr = new AdditionalPublicationDocumentReference();
				
				Element apdrElement = (Element) apdrNodeList.item(i);
				apdr.readAttributes(apdrElement, POS_UNICO_ELEMENTO);
				apdr.readAttachment(apdrElement, POS_UNICO_ELEMENTO);
				
				this.additionalPublicationDocumentReferenceList[i] = apdr;
			}
		}
	}
	
	/**
	 * Lee el cac:AdditionalPublicationRequest del documento
	 * 
	 * @param aps El cac:AdditionalPublicationStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAdditionalPublicationRequest(Element aps, int POS_UNICO_ELEMENTO){
		this.additionalPublicationRequestList = null;
		
		NodeList aprNodeList = aps.getElementsByTagName("cac-place-ext:AdditionalPublicationRequest");
		if (aprNodeList.getLength() > 0){
			this.additionalPublicationRequestList = new AdditionalPublicationRequest[aprNodeList.getLength()];
			
			for (int i = 0; i < aprNodeList.getLength(); i++){
				AdditionalPublicationRequest apr = new AdditionalPublicationRequest();
				
				Element apdrElement = (Element) aprNodeList.item(i);
				apr.readAttributes(apdrElement, POS_UNICO_ELEMENTO);
				
				this.additionalPublicationRequestList[i] = apr;
			}
		}
	}
	
	public String getPublicationMediaName() {
		return publicationMediaName;
	}
	
	public AdditionalPublicationDocumentReference[] getAdditionalPublicationDocumentReferenceList() {
		return additionalPublicationDocumentReferenceList;
	}
	
	public AdditionalPublicationRequest[] getAdditionalPublicationRequestList() {
		return additionalPublicationRequestList;
	}
}
