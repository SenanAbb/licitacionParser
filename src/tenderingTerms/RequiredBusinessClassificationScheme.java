package tenderingTerms;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 * 		ID: String[0..1]
 * 		name: String[0..1]
 * 		agencyName: String[0..1]
 * 		versionID: String[0..1]
 * 		URI: String[0..1]
 * 		schemeURI: String[0..1]
 * 		languageID: String[0..1]
 *		classificationCategoryList: ClassificationCategory[] [0..*]
 */
public class RequiredBusinessClassificationScheme {
	private String ID, name, agencyName, versionID, URI, schemeURI, languageID;
	private ClassificationCategory[] classificationCategoryList;
	
	public void readAttributes(Element rbcs, int POS_UNICO_ELEMENTO) {
		this.ID = null;
		this.name = null;
		this.agencyName = null;
		this.versionID = null;
		this.URI = null;
		this.schemeURI = null;
		this.languageID = null;
		
		/* ID */
		Element id = (Element) rbcs.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.ID = id.getTextContent();
		}
		
		/* NAME */
		Element name = (Element) rbcs.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if (name != null){
			this.name = name.getTextContent();
		}
		
		/* AGENCY NAME */
		Element an = (Element) rbcs.getElementsByTagName("cbc:AgencyName").item(POS_UNICO_ELEMENTO);
		if (an != null){
			this.agencyName = an.getTextContent();
		}
		
		/* VERSION ID */
		Element vid = (Element) rbcs.getElementsByTagName("cbc:VersionID").item(POS_UNICO_ELEMENTO);
		if (vid != null){
			this.versionID = vid.getTextContent();
		}
		
		/* URI */
		Element uri = (Element) rbcs.getElementsByTagName("cbc:URI").item(POS_UNICO_ELEMENTO);
		if (uri != null){
			this.URI = uri.getTextContent();
		}
		
		/* SCHEME URI */
		Element sURI = (Element) rbcs.getElementsByTagName("cbc:SchemeURI").item(POS_UNICO_ELEMENTO);
		if (sURI != null){
			this.schemeURI = sURI.getTextContent();
		}
		
		/* LANGUAGE ID */
		Element lid = (Element) rbcs.getElementsByTagName("cbc:LanguageID").item(POS_UNICO_ELEMENTO);
		if (lid != null){
			this.languageID = lid.getTextContent();
		}
	}

	public void readClassificationCategory(Element rbcs, int POS_UNICO_ELEMENTO) {
		this.classificationCategoryList = null;
		
		NodeList ccNodeList = rbcs.getElementsByTagName("cac:ClassificationCategory");
		if(ccNodeList.getLength() > 0){
			this.classificationCategoryList = new ClassificationCategory[ccNodeList.getLength()];
			
			for (int i = 0; i < ccNodeList.getLength(); i++){
				ClassificationCategory cc = new ClassificationCategory();
				
				Element ccElement = (Element) ccNodeList.item(i);
				cc.readAttributes(ccElement, POS_UNICO_ELEMENTO);
				
				this.classificationCategoryList[i] = cc;
			}
		}
	}
	
	public void print(){
		System.out.print("**** REQUIRED BUSINESS CLASSIFICATION SCHEME ****\n" +
						 "---->ID: " + ID + "\n" +
						 "---->Name: " + name + "\n" +
						 "---->Agency Name: " + agencyName + "\n" +
						 "---->Version ID: " + versionID + "\n" +
						 "---->URI: " + URI + "\n" +
						 "---->Scheme URI: " + schemeURI + "\n" +
						 "---->Language ID: " + languageID + "\n");
		
		if(classificationCategoryList != null){
			for(ClassificationCategory c : classificationCategoryList){
				c.print();
			}
		}else{
			System.out.print("***** CLASSIFICATION CATEGORY: null *****\n");
		}
	}
}
