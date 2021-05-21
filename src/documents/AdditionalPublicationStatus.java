package documents;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 *		publicationMediaName: String[0..1]
 *		additionalPublicationDocumentReferenceList: AdditionalPublicationDocumentReference[] [0..*]
 *		additionalPublicationRequestList: AdditionalPublicationRequest[] [0..*]
 */
public class AdditionalPublicationStatus {
	private String publicationMediaName;
	private AdditionalPublicationDocumentReference[] additionalPublicationDocumentReferenceList;
	private AdditionalPublicationRequest[] additionalPublicationRequestList;
	
	public void readAttributes(Element aps, int POS_UNICO_ELEMENTO){
		this.publicationMediaName = null;
		
		Element pmn = (Element) aps.getElementsByTagName("cbc-place-ext:PublicationMediaName").item(POS_UNICO_ELEMENTO);
		if (pmn != null){
			this.publicationMediaName = pmn.getTextContent();
		}
	}
	
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
	
	public void print(){
		System.out.print("*** ADDITIONAL PUBLICATION STATUS ***\n" +
						 "---> Publication Media Name: " + publicationMediaName + "\n" +
						 "--------------------------------\n");
		
		if (additionalPublicationDocumentReferenceList != null){
			for (AdditionalPublicationDocumentReference a : additionalPublicationDocumentReferenceList){
				a.print();
			}
		}else{
			System.out.println("**** ADDITIONAL PUBLICATION DOCUMENT REFERENCE: null ****");
		}
		
		if (additionalPublicationRequestList != null){
			for (AdditionalPublicationRequest a : additionalPublicationRequestList){
				a.print();
			}
		}else{
			System.out.println("**** ADDITIONAL PUBLICATION REQUEST: null ****");
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
