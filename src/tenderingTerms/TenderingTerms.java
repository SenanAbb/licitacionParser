package tenderingTerms;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.DocumentAvailabilityPeriod;

/**
 * @params
 * 		requiredCurriculaIndicator: bool[0..1]
 * 		variantConstraintIndicator: bool[0..1;
 * 		priceRevisionFormulaDescription: String[0..1]
 * 		fundingProgramCode: String[0..1]
 * 		fundingProgram: String[0..1]
 * 		allowedSubcontractTerms: AllowedSubcontractTerms[0..1]
 * 		procurementLegislationDocumentReference: ProcurementLegislationDocumentReference[0..1]
 * 		requiredFinancialGuaranteeList: RequiredFinancialGuarantee[] [0..3]
 * 		awardingTerms: AwardingTerms[0..1]
 *		tendererQualificationRequest: TendererQualificationRequest[0..1]
 *		documentProviderParty: DocumentProviderParty[0..1]
 *		documentAvailabilityPeriod: DocumentAvailabilityPeriod[0..1]
 *		tenderRecipientParty: TenderRecipientParty[0..1]
 *		additionalInformationParty: AdditionalInformationParty[0..1]
 *		appealTerms: AppealTerms[0..1]
 *		tenderPreparationList: TenderPreparation[] [0..*]
 * 		languageList: Language[] [0..*]
 */
public class TenderingTerms {
	private final static int MAX_REQUIRED_FINANCIAL_GUARANTEE = 3;
	
	private boolean requiredCurriculaIndicator, variantConstraintIndicator;
	private String priceRevisionFormulaDescription, fundingProgramCode, fundingProgram;
	private AllowedSubcontractTerms allowedSubcontractTerms;
	private ProcurementLegislationDocumentReference procurementLegislationDocumentReference;
	private RequiredFinancialGuarantee[] requiredFinancialGuaranteeList;
	private AwardingTerms awardingTerms;
	private TendererQualificationRequest tendererQualificationRequest;
	private DocumentProviderParty documentProviderParty;
	private DocumentAvailabilityPeriod documentAvailabilityPeriod;
	private TenderRecipientParty tenderRecipientParty;
	private AdditionalInformationParty additionalInformationParty;
	private AppealTerms appealTerms;
	private TenderPreparation[] tenderPreparationList;
	private Language[] languageList;
	
	public void readAttributes(Element tt, int POS_UNICO_ELEMENTO) {
		this.priceRevisionFormulaDescription = null;
		this.fundingProgram = null;
		this.fundingProgramCode = null;
		
		/* REQUIRED CURRICULA INDICATOR */
		Element rci = (Element) tt.getElementsByTagName("cbc:RequiredCurriculaIndicator").item(POS_UNICO_ELEMENTO);
		if (rci != null){
			this.requiredCurriculaIndicator = Boolean.parseBoolean(rci.getTextContent());
		}
		
		/* VARIANT CONSTRAINT INDICATOR */
		Element vci = (Element) tt.getElementsByTagName("cbc:VariantConstraintIndicator").item(POS_UNICO_ELEMENTO);
		if (vci != null){
			this.variantConstraintIndicator = Boolean.parseBoolean(vci.getTextContent());
		}
		
		/* PRICE REVISION FORMULA INDICATOR */
		Element prfi = (Element) tt.getElementsByTagName("cbc:PriceRevisionFormulaDescription").item(POS_UNICO_ELEMENTO);
		if (prfi != null){
			this.priceRevisionFormulaDescription = prfi.getTextContent();
		}
		
		/* FUNDING PROGRAM CODE */
		Element fpc = (Element) tt.getElementsByTagName("cbc:FundingProgramCode").item(POS_UNICO_ELEMENTO);
		if (fpc != null){
			this.fundingProgramCode = fpc.getTextContent();
		}
		
		/* FUNDING PROGRAM */
		Element fp = (Element) tt.getElementsByTagName("cbc:FundingProgram").item(POS_UNICO_ELEMENTO);
		if (fp != null){
			this.fundingProgram = fpc.getTextContent();
		}
	}
	
	public void readAllowedSubcontractTerms(Element tt, int POS_UNICO_ELEMENTO){
		this.allowedSubcontractTerms = null;
		
		Element ast = (Element) tt.getElementsByTagName("cac:AllowedSubcontractTerms").item(POS_UNICO_ELEMENTO);
		if(ast != null){
			this.allowedSubcontractTerms = new AllowedSubcontractTerms();
			this.allowedSubcontractTerms.readAttributes(ast, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readProcurementLegislationDocumentReference(Element tt, int POS_UNICO_ELEMENTO){
		this.procurementLegislationDocumentReference = null;
		
		Element pldr = (Element) tt.getElementsByTagName("cac:ProcurementLegislationDocumentReference").item(POS_UNICO_ELEMENTO);
		if (pldr != null){
			this.procurementLegislationDocumentReference = new ProcurementLegislationDocumentReference();
			this.procurementLegislationDocumentReference.readAttributes(pldr, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readRequiredFinancialGuarantee(Element tt, int POS_UNICO_ELEMENTO){
		this.requiredFinancialGuaranteeList = null;
		
		NodeList rfgNodeList = tt.getElementsByTagName("cac:RequiredFinancialGuarantee");
		if(rfgNodeList.getLength() > 0 && rfgNodeList.getLength() <= MAX_REQUIRED_FINANCIAL_GUARANTEE){
			this.requiredFinancialGuaranteeList = new RequiredFinancialGuarantee[rfgNodeList.getLength()];
			
			for (int i = 0; i < rfgNodeList.getLength(); i++){
				RequiredFinancialGuarantee rfg = new RequiredFinancialGuarantee();
				
				Element rfgElement = (Element) rfgNodeList.item(i);
				rfg.readAttributes(rfgElement, POS_UNICO_ELEMENTO);
				
				this.requiredFinancialGuaranteeList[i] = rfg;
			}
		}
	}
	
	public void readAwardingTerms(Element tt, int POS_UNICO_ELEMENTO){
		this.awardingTerms = null;
		
		Element at = (Element) tt.getElementsByTagName("cac:AwardingTerms").item(POS_UNICO_ELEMENTO);
		if (at != null){
			this.awardingTerms = new AwardingTerms();
			this.awardingTerms.readAwardingCriteria(at, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readTendererQualificationRequest(Element tt, int POS_UNICO_ELEMENTO){
		this.tendererQualificationRequest = null;
		
		Element tqr = (Element) tt.getElementsByTagName("cac:TendererQualificationRequest").item(POS_UNICO_ELEMENTO);
		if (tqr != null){
			this.tendererQualificationRequest = new TendererQualificationRequest();
			
			this.tendererQualificationRequest.readAttributes(tqr, POS_UNICO_ELEMENTO);
			this.tendererQualificationRequest.readSpecificTendererRequirement(tqr, POS_UNICO_ELEMENTO);
			this.tendererQualificationRequest.readFinancialEvaluationCriteria(tqr, POS_UNICO_ELEMENTO);
			this.tendererQualificationRequest.readTechnicalEvaluationCriteria(tqr, POS_UNICO_ELEMENTO);
			this.tendererQualificationRequest.readRequiredBusinessClassificationScheme(tqr, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readDocumentProviderParty(Element tt, int POS_UNICO_ELEMENTO){
		this.documentProviderParty = null;
		
		Element dpp = (Element) tt.getElementsByTagName("cac:DocumentProviderParty").item(POS_UNICO_ELEMENTO);
		if(dpp != null){
			this.documentProviderParty = new DocumentProviderParty();
			
			this.documentProviderParty.readAttributes(dpp, POS_UNICO_ELEMENTO);
			this.documentProviderParty.readPartyName(dpp, POS_UNICO_ELEMENTO);
			this.documentProviderParty.readPostalAddress(dpp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readDocumentAvailabilityPeriod(Element tt, int POS_UNICO_ELEMENTO){
		this.documentAvailabilityPeriod = null;
		
		Element dap = (Element) tt.getElementsByTagName("cac:DocumentAvailabilityPeriod").item(POS_UNICO_ELEMENTO);
		if (dap != null){
			this.documentAvailabilityPeriod = new DocumentAvailabilityPeriod();
			this.documentAvailabilityPeriod.readAttributes(dap, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readTenderRecipientParty(Element tt, int POS_UNICO_ELEMENTO){
		this.tenderRecipientParty = null;
		
		Element trp = (Element) tt.getElementsByTagName("cac:TenderRecipientParty").item(POS_UNICO_ELEMENTO);
		if(trp != null){
			this.tenderRecipientParty = new TenderRecipientParty();
			
			this.tenderRecipientParty.readAttributes(trp, POS_UNICO_ELEMENTO);
			this.tenderRecipientParty.readPartyName(trp, POS_UNICO_ELEMENTO);
			this.tenderRecipientParty.readPostalAddress(trp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readAdditionalInformationParty(Element tt, int POS_UNICO_ELEMENTO){
		this.additionalInformationParty = null;
		
		Element aip = (Element) tt.getElementsByTagName("cac:AdditionalInformationParty").item(POS_UNICO_ELEMENTO);
		if (aip != null){
			this.additionalInformationParty = new AdditionalInformationParty();
			
			this.additionalInformationParty.readAttributes(aip, POS_UNICO_ELEMENTO);
			this.additionalInformationParty.readPartyName(aip, POS_UNICO_ELEMENTO);
			this.additionalInformationParty.readPostalAddress(aip, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readAppealTerms(Element tt, int POS_UNICO_ELEMENTO){
		this.appealTerms = null;
		
		Element at = (Element) tt.getElementsByTagName("cac:AppealTerms").item(POS_UNICO_ELEMENTO);
		if (at != null){
			this.appealTerms = new AppealTerms();
			
			this.appealTerms.readAppealReceiverParty(at, POS_UNICO_ELEMENTO);
			this.appealTerms.readAppealInformationParty(at, POS_UNICO_ELEMENTO);
			this.appealTerms.readPresentationPeriod(at, POS_UNICO_ELEMENTO);
			this.appealTerms.readMediationParty(at, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readTenderPreparation(Element tt, int POS_UNICO_ELEMENTO){
		this.tenderPreparationList = null;
		
		NodeList tplNodeList = tt.getElementsByTagName("cac:TenderPreparation");
		if (tplNodeList.getLength() > 0){
			this.tenderPreparationList = new TenderPreparation[tplNodeList.getLength()];
			
			for(int i = 0; i < tplNodeList.getLength(); i++){
				TenderPreparation tpl = new TenderPreparation();
				
				Element tplElement = (Element) tplNodeList.item(i);
				tpl.readAttributes(tplElement, POS_UNICO_ELEMENTO);
				
				this.tenderPreparationList[i] = tpl;
			}
		}
	}
	
	public void readLanguage(Element tt, int POS_UNICO_ELEMENTO){
		this.languageList = null;
		
		NodeList languageNodeList = tt.getElementsByTagName("cac:Language");
		if(languageNodeList.getLength() > 0){
			this.languageList = new Language[languageNodeList.getLength()];
			
			for (int i = 0; i < languageNodeList.getLength(); i++){
				Language l = new Language();
				
				Element languageElement = (Element) languageNodeList.item(i);
				l.readAttributes(languageElement, POS_UNICO_ELEMENTO);
				
				this.languageList[i] = l;
			}
		}
	}
	
	public void print(){
		/* ATTRIBUTES */
		System.out.print("** TENDERING TERMS **\n" +
				"--> Required Curricula Indicator: " + requiredCurriculaIndicator + "\n" +
				"--> Variant Curricula Indicator: " + variantConstraintIndicator + "\n" +
				"--> Price Revision Formula Indicator: " + priceRevisionFormulaDescription + "\n" +
				"--------------------------------\n");
		
		/* ALLOWED SUBCONTRACTS TERMS */
		if(allowedSubcontractTerms != null){
			allowedSubcontractTerms.print();
		}else{
			System.out.print("*** ALLOWED SUBCONTRACT TERMS: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* PROCUREMENT LEG. DOC. REFERENCE */
		if(procurementLegislationDocumentReference != null){
			procurementLegislationDocumentReference.print();
		}else{
			System.out.print("*** PROCUREMENT LEGISL. DOC. REFERENCE: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* REQUIRED FINANCIAL GUARANTEE */
		if (requiredFinancialGuaranteeList != null){
			for (RequiredFinancialGuarantee rfg : requiredFinancialGuaranteeList){
				rfg.print();
			}
		}else{
			System.out.print("*** REQUIRED FINANCIAL GUARANTEE: null ***\n" +
							 "--------------------------------\n");
		}
		
		/* AWARDING TERMS */
		if(awardingTerms != null){
			awardingTerms.print();
		}else{
			System.out.print("*** AWARDING TERMS: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* TENDERER QUALIFICATION REQUEST */
		if(tendererQualificationRequest != null){
			tendererQualificationRequest.print();
		}else{
			System.out.print("*** TENDERER QUALIFICATION REQUEST: null ***\n" +
			 		 		 "--------------------------------\n");
		}
		
		/* DOCUMENT PROVIDER PARTY */
		if(documentProviderParty != null){
			documentProviderParty.print();
		}else{
			System.out.print("*** DOCUMENT PROVIDER PARTY: null ***\n" +
	 		 		 		 "--------------------------------\n");
		}	
		
		/* DOCUMENT AVAILABILITY PERIOD */
		if(documentAvailabilityPeriod != null){
			documentAvailabilityPeriod.print();
		}else{
			System.out.print("*** DOCUMENT AVAILABILITY PERIOD: null ***\n" +
	 		 		 		 "--------------------------------\n");
		}
		
		/* TENDER RECIPIENT PARTY */
		if(tenderRecipientParty != null){
			tenderRecipientParty.print();
		}else{
			System.out.print("*** TENDER RECIPIENT PARTY: null ***\n" +
	 		 		 		 "--------------------------------\n");
		}	
		
		/* ADDITIONAL INFORMATION PARTY */
		if(additionalInformationParty != null){
			additionalInformationParty.print();
		}else{
			System.out.print("*** ADDITIONAL INFORMATION PARTY: null ***\n" +
	 		 		 		 "--------------------------------\n");
		}
		
		/* APPEAL TERMS */
		if(appealTerms != null){
			appealTerms.print();
		}else{
			System.out.print("*** APPEAL TERMS: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* TENDER PREPARATION */
		if(tenderPreparationList != null){
			for (TenderPreparation tp : tenderPreparationList){
				tp.print();
			}
		}else{
			System.out.print("*** TENDER PREPARATION: null ***\n" +
							 "--------------------------------\n");
		}
		
		/* LANGUAGE */
		if(languageList != null){
			for (Language l : languageList){
				l.print();
			}
		}else{
			System.out.print("*** LANGUAGE: null ***\n" +
							 "--------------------------------\n");
		}
		System.out.print("===============================================================\n");
	}

	public ProcurementLegislationDocumentReference getProcurementLegislationDocumenteReference() {
		return procurementLegislationDocumentReference;
	}
	
	public Language[] getLanguageList() {
		return languageList;
	}

	
	public boolean getRequiredCurriculaIndicator() {
		return requiredCurriculaIndicator;
	}

	
	public boolean getVariantConstraintIndicator() {
		return variantConstraintIndicator;
	}

	
	public String getPriceRevisionFormulaDescription() {
		return priceRevisionFormulaDescription;
	}

	
	public String getFundingProgramCode() {
		return fundingProgramCode;
	}

	
	public String getFundingProgram() {
		return fundingProgram;
	}

	
}
