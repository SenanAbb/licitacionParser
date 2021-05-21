package ContractFolderStatus;

import locatedContractingParty.LocatedContractingParty;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import contractModification.ContractModification;
import documents.AdditionalDocumentReference;
import documents.GeneralDocument;
import documents.LegalDocumentReference;
import documents.TechnicalDocumentReference;
import documents.ValidNoticeInfo;
import procurementProject.ProcurementProject;
import tenderResult.TenderResult;
import tenderingProcess.TenderingProcess;
import tenderingTerms.TenderingTerms;

/**
 * @params
 * 		contractFolderID: String[1]
 * 		contractFolderStatusCode: String[1]
 * 		locatedContractingParty: LocatedContractingParty[1]
 * 		procurementProject: ProcurementProject[1]
 *		tenderResultList: TenderResult[] [0..*]
 *		tenderingTerms: TenderingTerms[1]
 *		tenderingProcess: TenderingProcess[1]
 *		legalDocumentReference: LegalDocumentReference[0..1]
 *		technicalDocumentReference: TechnicalDocumentReference[0..1]
 *		additionalDocumentReferenceList: AdditionalDocumentReference[] [0..*]
 *		validNoticeInfoList: ValidNoticeInfo[] [0..*]
 *		generalDocumentList: GeneralDocument[] [0..*]
 *		contractModificationList: ContractModification[] [0..*]
 */
public class ContractFolderStatus {
	private String contractFolderID, contractFolderStatusCode;
	private LocatedContractingParty locatedContractingParty;
	private ProcurementProject procurementProject;
	private TenderResult[] tenderResultList;
	private TenderingTerms tenderingTerms;
	private TenderingProcess tenderingProcess;
	private ContractModification[] contractModificationList;
	private LegalDocumentReference legalDocumentReference;
	private TechnicalDocumentReference technicalDocumentReference;
	private AdditionalDocumentReference[] additionalDocumentReferenceList;
	private ValidNoticeInfo[] validNoticeInfoList;
	private GeneralDocument[] generalDocumentList;
	
	public void readAttributes(Element cfs, int POS_UNICO_ELEMENTO){
		this.contractFolderID = null;
		this.contractFolderStatusCode = null;
		
		//Inicializamos todas las variables que deberá contener el ContractFolderStatus
		Element contractFolderIDNode = (Element) cfs.getElementsByTagName("cbc:ContractFolderID").item(POS_UNICO_ELEMENTO);
		Element contractFolderStatusCodeNode = (Element) cfs.getElementsByTagName("cbc-place-ext:ContractFolderStatusCode").item(POS_UNICO_ELEMENTO);
		
		// Si el elemento se ha quedado a null, es decir, no se ha encontrado ese elemento en el ATOM, capturamos la excepcion y mandamos un mensaje por pantalla
		try{
			this.contractFolderID = contractFolderIDNode.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER ID no existe\n");
		}
		
		try{
			this.contractFolderStatusCode = contractFolderStatusCodeNode.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER STATUS CODE no existe\n");
		}	
	}
	
	public void readLocatedContractingParty(Element cfs, int POS_UNICO_ELEMENTO){
		this.locatedContractingParty = null;
		
		Element lcp = (Element) cfs.getElementsByTagName("cac-place-ext:LocatedContractingParty").item(POS_UNICO_ELEMENTO);
		
		try{
			this.locatedContractingParty = new LocatedContractingParty();
			this.locatedContractingParty.readAttributes(lcp, POS_UNICO_ELEMENTO);
			this.locatedContractingParty.readParentLocatedParty(lcp, POS_UNICO_ELEMENTO);
			this.locatedContractingParty.readParty(lcp, POS_UNICO_ELEMENTO);
		}catch(NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> LOCATED CONTRACTING PARTY no existe");
		}
	}
	
	public void readProcurementProject(Element cfs, int POS_UNICO_ELEMENTO){
		this.procurementProject = null;
		
		//Dentro del ContractFolderStatus, buscamos el <cac:ProcurementProject>
		Element pp = (Element) cfs.getElementsByTagName("cac:ProcurementProject").item(POS_UNICO_ELEMENTO);
		
		try{
			if(pp.getParentNode().getNodeName() == "cac-place-ext:ContractFolderStatus"){
				this.procurementProject = new ProcurementProject();
				
				this.procurementProject.readAttributes(pp, POS_UNICO_ELEMENTO);
				this.procurementProject.readBudgetAmount(pp, POS_UNICO_ELEMENTO);
				this.procurementProject.readRequiredCommodityClassification(pp, POS_UNICO_ELEMENTO);
				this.procurementProject.readPlannedPeriod(pp, POS_UNICO_ELEMENTO);
				this.procurementProject.readRealizedLocation(pp, POS_UNICO_ELEMENTO);
				this.procurementProject.readContractExtension(pp, POS_UNICO_ELEMENTO);
	
			// Si existe pero su padre no es Contract Folder Status
			}else{
				throw new Exception();
			}
		}catch(NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> PROCUREMENT PROJECT no existe\n");
		}catch(Exception e){
			System.err.print("ERROR FATAL: " + e.getMessage() + "\n");
			e.printStackTrace();
		}
	}
	
	public void readTenderResult(Element cfs, int POS_UNICO_ELEMENTO){
		this.tenderResultList = null;
		
		NodeList tenderResultNodeList = cfs.getElementsByTagName("cac:TenderResult");
		if (tenderResultNodeList.getLength() > 0){
			this.tenderResultList = new TenderResult[tenderResultNodeList.getLength()];
			
			for (int i = 0; i < tenderResultNodeList.getLength(); i++){
				TenderResult tenderResult = new TenderResult();
				Element tr = (Element) tenderResultNodeList.item(i);
				
				tenderResult.readAttributes(tr, POS_UNICO_ELEMENTO);
				tenderResult.readContractList(tr, POS_UNICO_ELEMENTO);
				tenderResult.readWinningParty(tr, POS_UNICO_ELEMENTO);
				tenderResult.readAwardedTenderedProject(tr, POS_UNICO_ELEMENTO);
				tenderResult.readSubcontractTerms(tr, POS_UNICO_ELEMENTO);
				
				this.tenderResultList[i] = tenderResult;
			}
		}
	}

	public void readTenderingTerms(Element cfs, int POS_UNICO_ELEMENTO){
		this.tenderingTerms = null;
		
		Element tt = (Element) cfs.getElementsByTagName("cac:TenderingTerms").item(POS_UNICO_ELEMENTO);
		try{
			this.tenderingTerms = new TenderingTerms();
			
			this.tenderingTerms.readAttributes(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readAllowedSubcontractTerms(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readProcurementLegislationDocumentReference(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readRequiredFinancialGuarantee(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readAwardingTerms(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readTendererQualificationRequest(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readDocumentProviderParty(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readDocumentAvailabilityPeriod(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readTenderRecipientParty(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readAdditionalInformationParty(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readAppealTerms(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readTenderPreparation(tt, POS_UNICO_ELEMENTO);
			this.tenderingTerms.readLanguage(tt, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> TENDERING TERMS no existe\n");
		}
	}
	
	public void readTenderingProcess(Element cfs, int POS_UNICO_ELEMENTO){
		this.tenderingProcess = null;
		
		Element tp = (Element) cfs.getElementsByTagName("cac:TenderingProcess").item(POS_UNICO_ELEMENTO);
		
		try{
			this.tenderingProcess = new TenderingProcess();
			
			this.tenderingProcess.readAttributes(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readAuctionTerms(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readTenderSubmissionDeadlinePeriod(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readOpenTenderEvent(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readEconomicOperatorShortList(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readProcessJustification(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readParticipationRequestReceptionPeriod(tp, POS_UNICO_ELEMENTO);
			this.tenderingProcess.readDocumentAvailabilityPeriod(tp, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> TENDERING PROCESS no existe\n");
		}
	}
	
	public void readContractModification(Element cfs, int POS_UNICO_ELEMENTO){
		this.contractModificationList = null;
		
		NodeList cmNodeList = cfs.getElementsByTagName("cac:ContractModification");
		if(cmNodeList.getLength() > 0){
			this.contractModificationList = new ContractModification[cmNodeList.getLength()];
			
			for (int i = 0; i < cmNodeList.getLength(); i++){
				ContractModification cm = new ContractModification();
				
				Element cmElement = (Element) cmNodeList.item(i);
				cm.readAttributes(cmElement, POS_UNICO_ELEMENTO);
				cm.readContractModificationLegalMonetaryTotal(cmElement, POS_UNICO_ELEMENTO);
				cm.readFinalLegalMonetaryTotal(cmElement, POS_UNICO_ELEMENTO);
				
				this.contractModificationList[i] = cm;
			}
		}
	}
	
	public void readLegalDocumentReference(Element cfs, int POS_UNICO_ELEMENTO){
		this.legalDocumentReference = null;
		
		Element ldr = (Element) cfs.getElementsByTagName("cac:LegalDocumentReference").item(POS_UNICO_ELEMENTO);
		if (ldr != null){
			this.legalDocumentReference = new LegalDocumentReference();
			this.legalDocumentReference.readAttributes(ldr, POS_UNICO_ELEMENTO);
			this.legalDocumentReference.readAttachment(ldr, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readTechnicalDocumentReference(Element cfs, int POS_UNICO_ELEMENTO){
		this.technicalDocumentReference = null;
		
		Element tdr = (Element) cfs.getElementsByTagName("cac:TechnicalDocumentReference").item(POS_UNICO_ELEMENTO);
		if (tdr != null){
			this.technicalDocumentReference = new TechnicalDocumentReference();
			this.technicalDocumentReference.readAttributes(tdr, POS_UNICO_ELEMENTO);
			this.technicalDocumentReference.readAttachment(tdr, POS_UNICO_ELEMENTO);
		}
	}

	public void readAdditionalDocumentReference(Element cfs, int POS_UNICO_ELEMENTO){
		this.additionalDocumentReferenceList = null;
		
		NodeList adrNodeList = cfs.getElementsByTagName("cac:AdditionalDocumentReference");
		if (adrNodeList.getLength() > 0){
			this.additionalDocumentReferenceList = new AdditionalDocumentReference[adrNodeList.getLength()];
			
			for (int i = 0; i < adrNodeList.getLength(); i++){
				AdditionalDocumentReference adr = new AdditionalDocumentReference();
				
				Element adrElement = (Element) adrNodeList.item(i);
				adr.readAttributes(adrElement, POS_UNICO_ELEMENTO);
				adr.readAttachment(adrElement, POS_UNICO_ELEMENTO);
				
				this.additionalDocumentReferenceList[i] = adr;
			}
		}
	}
	
	public void readValidNoticeInfo(Element cfs, int POS_UNICO_ELEMENTO){
		this.validNoticeInfoList = null;
		
		NodeList vniNodeList = cfs.getElementsByTagName("cac-place-ext:ValidNoticeInfo");
		if(vniNodeList.getLength() > 0){
			this.validNoticeInfoList = new ValidNoticeInfo[vniNodeList.getLength()];
			
			for (int i = 0; i < vniNodeList.getLength(); i++){
				ValidNoticeInfo vni = new ValidNoticeInfo();
				
				Element vniElement = (Element) vniNodeList.item(i);
				vni.readAttributes(vniElement, POS_UNICO_ELEMENTO);
				vni.readAdditionalPublicationStatus(vniElement, POS_UNICO_ELEMENTO);
				
				this.validNoticeInfoList[i] = vni;
			}
		}
	}

	public void readGeneralDocument(Element cfs, int POS_UNICO_ELEMENTO){
		this.generalDocumentList = null;
		
		NodeList gdNodeList = cfs.getElementsByTagName("cac-place-ext:GeneralDocument");
		if(gdNodeList.getLength() > 0){
			this.generalDocumentList = new GeneralDocument[gdNodeList.getLength()];
			
			for (int i = 0; i < gdNodeList.getLength(); i++){
				GeneralDocument gd = new GeneralDocument();
				
				Element gdElement = (Element) gdNodeList.item(i);
				gd.readGeneralDocumentDocumentReference(gdElement, POS_UNICO_ELEMENTO);
				
				this.generalDocumentList[i] = gd;
			}
		}
	}
	
	public void print(){
		System.out.print("* CONTRACT FOLDER STATUS *\n" + 
				 		 "-> Contract Folder ID: " + contractFolderID + "\n" + 
						 "-> Contract Folder Status Code: " + contractFolderStatusCode + "\n" +
						 "--------------------------------\n");
		System.out.print("===============================================================\n");
		this.locatedContractingParty.print();
		
		/* PROCUREMENT PROJECT */
		if(this.procurementProject != null){
			this.procurementProject.print();
		}else{
			System.out.print("** PROCUREMENT PROJECT: null **\n");
		}
		
		/* TENDER RESULT */
		if (tenderResultList != null){
			for (TenderResult td : tenderResultList){
				td.print();
			}
		}else{
			System.out.print("** TENDER RESULT: null **\n");
		}
		
		/* TENDERING TERMS */
		if(this.tenderingTerms != null){
			this.tenderingTerms.print();
		}else{
			System.out.print("** TENDERING TERMS: null **\n");
		}
		
		/* TENDERING PROCESS */
		if(this.tenderingProcess != null){
			this.tenderingProcess.print();
		}else{
			System.out.print("** TENDERING PROCESS: null **\n");
		}
		 
		/* CONTRACT MODIFICATION */
		if (this.contractModificationList != null){
			for (ContractModification c : contractModificationList){
				c.print();
			}
		}else{
			System.out.print("** CONTRACT MODIFICATION: null **\n");
		}
		
		/* LEGAL DOCUMENT */
		if (this.legalDocumentReference != null){
			legalDocumentReference.print();
		}else{
			System.out.print("** LEGAL DOCUMENT REFERENCE: null **\n");
		}
		
		/* TECHNICAL DOCUMENT */
		if (this.technicalDocumentReference != null){
			technicalDocumentReference.print();
		}else{
			System.out.print("** TECHNICAL DOCUMENT REFERENCE: null **\n");
		}
		
		/* ADDITIONAL DOCUMENT */
		if (this.additionalDocumentReferenceList != null){
			for (AdditionalDocumentReference a : additionalDocumentReferenceList){
				a.print();
			}
		}else{
			System.out.print("** ADDITIONAL DOCUMENT REFERENCE: null **\n");
		}
	
		/* VALID NOTICE INFO */
		if (this.validNoticeInfoList != null){
			for (ValidNoticeInfo v : validNoticeInfoList){
				v.print();
			}
		}else{
			System.out.print("** VALID NOTICE INFO: null **\n");
		}
		
		/* GENERAL DOCUMENT */
		if (this.generalDocumentList != null){
			for (GeneralDocument g : generalDocumentList){
				g.print();
			}
		}else{
			System.out.print("** GENERAL DOCUMENT: null **\n");
		}
	}

	public String getContractFolderID(){
		return contractFolderID;
	}
	
	public LegalDocumentReference getLegalDocumentReference() {
		return legalDocumentReference;
	}
	
	public TechnicalDocumentReference getTechnicalDocumentReference() {
		return technicalDocumentReference;
	}
	
	public AdditionalDocumentReference[] getAdditionalDocumentReferenceList() {
		return additionalDocumentReferenceList;
	}

	public ProcurementProject getProcurementProject() {
		return procurementProject;
	}
	
	public String getContractFolderStatusCode() {
		return contractFolderStatusCode;
	}

	public TenderingProcess getTenderingProcess() {
		return tenderingProcess;
	}
	
	public TenderingTerms getTenderingTerms(){
		return tenderingTerms;
	}

	public LocatedContractingParty getLocatedContractingParty() {
		return locatedContractingParty;
	}
	
	public TenderResult[] getTenderResultList() {
		return tenderResultList;
	}

	public ContractModification[] getContractModificationList() {
		return contractModificationList;
	}

	
	public ValidNoticeInfo[] getValidNoticeInfoList() {
		return validNoticeInfoList;
	}

	public GeneralDocument[] getGeneralDocumentList() {
		return generalDocumentList;
	}
}
