package tenderingTerms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 *		securityConditions: String[0..1]
 *		securityClearanceLimitDate: Date[0..1]
 *		personalSituation: String[0..1]
 *		description: String[0..1]
 *		specificTendererRequirementList: SpecificTendererRequirement[] [0..*]
 *		tecnicalEvaluationCriteriaList: TechnicalEvaluationCriteria[] [0..*]
 *		financialEvaluationCriteriaList: FinancialEvaluationCriteria[] [0..*]
 *		requiredBusinessClassificationScheme: RequiredBusinessClassificationScheme[0..1]
 */
public class TendererQualificationRequest {
	private String securityConditions, personalSituation, description;
	private Date securityClearanceLimitDate;
	private SpecificTendererRequirement[] specificTendererRequirementList;
	private FinancialEvaluationCriteria[] financialEvaluationCriteriaList;
	private TechnicalEvaluationCriteria[] technicalEvaluationCriteriaList;
	private RequiredBusinessClassificationScheme requiredBusinessClassificationScheme;

	public void readAttributes(Element tqr, int POS_UNICO_ELEMENTO) {
		this.securityConditions = null;
		this.personalSituation = null;
		this.description = null;
		this.securityClearanceLimitDate = null;
		
		/* SECURITY CONDITIONS */
		Element sc = (Element) tqr.getElementsByTagName("cbc:SecurityConditions").item(POS_UNICO_ELEMENTO);
		if (sc != null){
			this.securityConditions = sc.getTextContent();
		}
		
		/* PERSONAL SITUATION */
		Element ps = (Element) tqr.getElementsByTagName("cbc:PersonalSituation").item(POS_UNICO_ELEMENTO);
		if (ps != null){
			this.personalSituation = ps.getTextContent();
		}
		
		/* DESCRIPTION */
		Element description = (Element) tqr.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		// -> Si no se ha encontrado un nodo con ese nombre o se ha encontrado y su padre es TendererQualificationRequest
		// --> Hay mas "description" dentro de TendererQualificationRequest, pero solo queremos el que sea hijo de esta clase
		if (description != null && description.getParentNode().getNodeName() == "cac:TendererQualificationRequest"){
			this.description = description.getTextContent();
		}
		
		/* SECURITY CLEARANCE LIMIT DATE */
		Element date = (Element) tqr.getElementsByTagName("cbc:SecurityClearanceLimitDate").item(POS_UNICO_ELEMENTO);
		if (date != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date sclDate = null;
			try {
				sclDate = format.parse(date.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.securityClearanceLimitDate = sclDate;
		}
	}
	
	public void readSpecificTendererRequirement(Element tqr, int POS_UNICO_ELEMENTO){
		this.specificTendererRequirementList = null;
		
		NodeList strNodeList = tqr.getElementsByTagName("cac:SpecificTendererRequirement");
		if (strNodeList.getLength() > 0){
			this.specificTendererRequirementList = new SpecificTendererRequirement[strNodeList.getLength()];
			
			for (int i = 0; i < strNodeList.getLength(); i++){
				SpecificTendererRequirement str = new SpecificTendererRequirement();
				
				Element strElement = (Element) strNodeList.item(i);
				str.readAttributes(strElement, POS_UNICO_ELEMENTO);
				
				this.specificTendererRequirementList[i] = str;
			}
		}
	}
	
	public void readFinancialEvaluationCriteria(Element tqr, int POS_UNICO_ELEMENTO){
		this.financialEvaluationCriteriaList = null;
		
		NodeList fecNodeList = tqr.getElementsByTagName("cac:FinancialEvaluationCriteria");
		if (fecNodeList.getLength() > 0){
			this.financialEvaluationCriteriaList = new FinancialEvaluationCriteria[fecNodeList.getLength()];
			
			for (int i = 0; i < fecNodeList.getLength(); i++){
				FinancialEvaluationCriteria fec = new FinancialEvaluationCriteria();
				
				Element ftrElement = (Element) fecNodeList.item(i);
				fec.readAttributes(ftrElement, POS_UNICO_ELEMENTO);
				
				this.financialEvaluationCriteriaList[i] = fec;
			}
		}
	}
	
	public void readTechnicalEvaluationCriteria(Element tqr, int POS_UNICO_ELEMENTO){
		this.technicalEvaluationCriteriaList = null;
		
		NodeList tecNodeList = tqr.getElementsByTagName("cac:TechnicalEvaluationCriteria");
		if (tecNodeList.getLength() > 0){
			this.technicalEvaluationCriteriaList = new TechnicalEvaluationCriteria[tecNodeList.getLength()];
			
			for (int i = 0; i < tecNodeList.getLength(); i++){
				TechnicalEvaluationCriteria tec = new TechnicalEvaluationCriteria();
				
				Element ftrElement = (Element) tecNodeList.item(i);
				tec.readAttributes(ftrElement, POS_UNICO_ELEMENTO);
				
				this.technicalEvaluationCriteriaList[i] = tec;
			}
		}
	}

	public void readRequiredBusinessClassificationScheme(Element tqr, int POS_UNICO_ELEMENTO){
		this.requiredBusinessClassificationScheme = null;
		
		Element rbcs = (Element) tqr.getElementsByTagName("cac:RequiredBusinessClassificationScheme").item(POS_UNICO_ELEMENTO);
		if(rbcs != null){
			this.requiredBusinessClassificationScheme = new RequiredBusinessClassificationScheme();
			
			this.requiredBusinessClassificationScheme.readAttributes(rbcs, POS_UNICO_ELEMENTO);
			this.requiredBusinessClassificationScheme.readClassificationCategory(rbcs, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		/* ATTRIBUTES */
		System.out.print("*** TENDERER QUALIFICATION REQUEST ***\n" +
						 "--> Security Conditions: " + securityConditions + "\n" +
						 "--> Personal Situation: " + personalSituation + "\n" +
						 "--> Description: " + description + "\n" +
						 "--> Secutiry Clearance Limit Date: " + securityClearanceLimitDate + "\n" +
						 "--------------------------------\n");
		
		/* SPECIFIC TENDERER REQUIREMENT */
		if(specificTendererRequirementList != null){
			for (SpecificTendererRequirement str : specificTendererRequirementList){
				str.print();
			}
		}else{
			System.out.print("**** SPECIFIC TENDERER REQUIREMENT: null ****\n");
		}
		
		/* FINANCIAL EVALUATION CRITERIA */
		if(financialEvaluationCriteriaList != null){
			for (FinancialEvaluationCriteria fec : financialEvaluationCriteriaList){
				fec.print();
			}
		}else{
			System.out.print("**** FINANCIAL EVALUATION CRITERIA: null ****\n");
		}
		
		/* TECHNICAL EVALUATION CRITERIA */
		if(technicalEvaluationCriteriaList != null){
			for (TechnicalEvaluationCriteria tec : technicalEvaluationCriteriaList){
				tec.print();
			}
		}else{
			System.out.print("**** TECHNICAL EVALUATION CRITERIA: null ****\n");
		}
		
		/* REQUIRED BUSINESS CLASSIFICATION SCHEME */
		if(requiredBusinessClassificationScheme != null){
			requiredBusinessClassificationScheme.print();
		}else{
			System.out.print("**** REQUIRED BUSINESS CLASSIFICATION SCHEME: null ****\n");
		}
		
		System.out.print("--------------------------------\n");
	}

}
