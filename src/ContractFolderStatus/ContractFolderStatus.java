package ContractFolderStatus;

import locatedContractingParty.LocatedContractingParty;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import procurementProject.ProcurementProject;
import tenderResult.TenderResult;

public class ContractFolderStatus {
	private String contractFolderID, contractFolderStatusCode;
	private ProcurementProject procurementProject;
	private TenderResult[] tenderResultList;
//	private LegalDocumentReference legalDocumenteReference;
//	private TechnicalDocumentReference technicalDocumentReference;
//	private AdditionalDocumentReference[] additionalDocumentReference;
//	private TenderingProcess tenderingProcess;
//	private TenderingTerms tenderingTerms;
	private LocatedContractingParty locatedContractingParty;
//	private ContractModification contractModification;
//	private ValidNoticeInfo[] validNoticeInfoList;
//	private GeneralDocument[] generalDocumentList;
	
	public void readAttributes(Element cfs, int POS_UNICO_ELEMENTO){
		this.contractFolderID = null;
		this.contractFolderStatusCode = null;
		
		//Inicializamos todas las variables que deberá contener el ContractFolderStatus
		Element contractFolderIDNode = (Element) cfs.getElementsByTagName("cbc:ContractFolderID").item(POS_UNICO_ELEMENTO);
		Element contractFolderStatusCodeNode = (Element) cfs.getElementsByTagName("cbc-place-ext:ContractFolderStatusCode").item(POS_UNICO_ELEMENTO);
		
		// Compruebo la existencia del ContractFolderID, si no existe se queda a null y mandamos mensaje
		if (contractFolderIDNode != null){
			this.contractFolderID = contractFolderIDNode.getTextContent();
		}else{
			System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER ID no existe\n");
		}
		
		// Compruebo la existencia del ContractFolderID, si no existe se queda a null y mandamos mensaje
		if (contractFolderStatusCodeNode != null){
			this.contractFolderStatusCode = contractFolderStatusCodeNode.getTextContent();
		}else{
			System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER STATUS CODE no existe\n");
		}	
	}
	
	public void readProcurementProject(Element cfs, int POS_UNICO_ELEMENTO){
		this.procurementProject = null;
		
		//Dentro del ContractFolderStatus, buscamos el <cac:ProcurementProject>
		Element pp = (Element) cfs.getElementsByTagName("cac:ProcurementProject").item(POS_UNICO_ELEMENTO);
		
		//Si su padre no es ContractFolderStatus -> No existe
		if (pp == null || pp.getParentNode().getNodeName() != "cac-place-ext:ContractFolderStatus"){
			System.err.print("ERROR FATAL: ContractFolderStatus -> PROCUREMENT PROJECT no existe\n");
		}else{
			this.procurementProject = new ProcurementProject();
			
			this.procurementProject.readAttributes(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readBudgetAmount(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRequiredCommodityClassification(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readPlannedPeriod(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRealizedLocation(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readContractExtension(pp, POS_UNICO_ELEMENTO);
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
		}else{
			System.err.print("ERROR FATAL: ContractFolderStatus -> TENDER RESULT no existe");
		}
	}
	
	public void readLocatedContractingParty(Element cfs, int POS_UNICO_ELEMENTO) {
		this.locatedContractingParty = null;
		
		Element lcp = (Element) cfs.getElementsByTagName("cac-place-ext:LocatedContractingParty").item(POS_UNICO_ELEMENTO);
		if (lcp != null){
			this.locatedContractingParty = new LocatedContractingParty();
			this.locatedContractingParty.readAttributes(lcp, POS_UNICO_ELEMENTO);
			this.locatedContractingParty.readParentLocatedParty(lcp, POS_UNICO_ELEMENTO);
			this.locatedContractingParty.readParty(lcp, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: ContractFolderStatus -> LOCATED CONTRACTING PARTY no existe");
		}
	}
	
	public void print(){
		System.out.print("* CONTRACT FOLDER STATUS *\n" + 
				 		 "-> Contract Folder ID: " + contractFolderID + "\n" + 
						 "-> Contract Folder Status Code: " + contractFolderStatusCode + "\n" +
						 "--------------------------------\n");
		System.out.print("===============================================================\n");
		this.locatedContractingParty.print();
		this.procurementProject.print();
		for (TenderResult td : tenderResultList){
			td.print();
		}
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public ContractFolderStatus(){}
	
	public ContractFolderStatus(String contractFolderID,
			String contractFolderStatusCode,
			ProcurementProject procurementProject)
//			TenderResult[] tenderResultList, 
//			TenderingProcess tenderingProcess,
//			TenderingTerms tenderingTerms,
//			LocatedContractingParty locatedContractingParty,
//			ContractModification contractModification) 
	{
		this.contractFolderID = contractFolderID;
		this.contractFolderStatusCode = contractFolderStatusCode;
		this.procurementProject = procurementProject;
//		this.tenderResultList = tenderResultList;
//		this.tenderingProcess = tenderingProcess;
//		this.tenderingTerms = tenderingTerms;
//		this.locatedContractingParty = locatedContractingParty;
//		this.contractModification = contractModification;
	}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/


	public String getContractFolderID() {
		return contractFolderID;
	}


	public void setContractFolderID(String contractFolderID) {
		this.contractFolderID = contractFolderID;
	}


	public String getContractFolderStatusCode() {
		return contractFolderStatusCode;
	}


	public void setContractFolderStatusCode(String contractFolderStatusCode) {
		this.contractFolderStatusCode = contractFolderStatusCode;
	}


	public ProcurementProject getProcurementProject() {
		return procurementProject;
	}


	public void setProcurementProject(ProcurementProject procurementProject) {
		this.procurementProject = procurementProject;
	}


	public TenderResult[] getTenderResultList() {
		return tenderResultList;
	}


	public void setTenderResultList(TenderResult[] tenderResultList) {
		this.tenderResultList = tenderResultList;
	}

//	public LegalDocumentReference getLegalDocumenteReference() {
//		return legalDocumenteReference;
//	}
//
//
//	public void setLegalDocumenteReference(
//			LegalDocumentReference legalDocumenteReference) {
//		this.legalDocumenteReference = legalDocumenteReference;
//	}
//
//
//	public TechnicalDocumentReference getTechnicalDocumentReference() {
//		return technicalDocumentReference;
//	}
//
//
//	public void setTechnicalDocumentReference(
//			TechnicalDocumentReference technicalDocumentReference) {
//		this.technicalDocumentReference = technicalDocumentReference;
//	}
//
//
//	public AdditionalDocumentReference[] getAdditionalDocumentReference() {
//		return additionalDocumentReference;
//	}
//
//
//	public void setAdditionalDocumentReference(
//			AdditionalDocumentReference[] additionalDocumentReference) {
//		this.additionalDocumentReference = additionalDocumentReference;
//	}
//
//
//	public TenderingProcess getTenderingProcess() {
//		return tenderingProcess;
//	}
//
//
//	public void setTenderingProcess(TenderingProcess tenderingProcess) {
//		this.tenderingProcess = tenderingProcess;
//	}
//
//
//	public TenderingTerms getTenderingTerms() {
//		return tenderingTerms;
//	}
//
//
//	public void setTenderingTerms(TenderingTerms tenderingTerms) {
//		this.tenderingTerms = tenderingTerms;
//	}
//
//
//	public LocatedContractingParty getLocatedContractingParty() {
//		return locatedContractingParty;
//	}
//
//
//	public void setLocatedContractingParty(
//			LocatedContractingParty locatedContractingParty) {
//		this.locatedContractingParty = locatedContractingParty;
//	}
//
//
//	public ContractModification getContractModification() {
//		return contractModification;
//	}
//
//
//	public void setContractModification(ContractModification contractModification) {
//		this.contractModification = contractModification;
//	}
//
//
//	public ValidNoticeInfo[] getValidNoticeInfoList() {
//		return validNoticeInfoList;
//	}
//
//
//	public void setValidNoticeInfoList(ValidNoticeInfo[] validNoticeInfoList) {
//		this.validNoticeInfoList = validNoticeInfoList;
//	}
//
//
//	public GeneralDocument[] getGeneralDocumentList() {
//		return generalDocumentList;
//	}
//
//
//	public void setGeneralDocumentList(GeneralDocument[] generalDocumentList) {
//		this.generalDocumentList = generalDocumentList;
//	}
}
