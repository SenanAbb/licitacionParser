package ContractFolderStatus;

import org.w3c.dom.Element;

import procurementProject.ProcurementProject;

public class ContractFolderStatus {
	private String contractFolderID, contractFolderStatusCode;
	private ProcurementProject procurementProject;
//	private TenderResult[] tenderResultList;
//	private LegalDocumentReference legalDocumenteReference;
//	private TechnicalDocumentReference technicalDocumentReference;
//	private AdditionalDocumentReference[] additionalDocumentReference;
//	private TenderingProcess tenderingProcess;
//	private TenderingTerms tenderingTerms;
//	private LocatedContractingParty locatedContractingParty;
//	private ContractModification contractModification;
//	private ValidNoticeInfo[] validNoticeInfoList;
//	private GeneralDocument[] generalDocumentList;
	
	public void readProcurementProject(Element cfs, int POS_UNICO_ELEMENTO){
		this.procurementProject = null;
		
		//Dentro del ContractFolderStatus, buscamos el <cac:ProcurementProject>
		Element pp = (Element) cfs.getElementsByTagName("cac:ProcurementProject").item(POS_UNICO_ELEMENTO);
		
		//Si su padre con es ContractFolderStatus -> No existe
		if (pp != null && pp.getParentNode().getNodeName() != "cac-place-ext:ContractFolderStatus"){
			System.err.print("ERROR FATAL: ContractFolderStatus -> PROCUREMENT PROJECT no existe\n");
		}else{
			this.procurementProject = new ProcurementProject();
			
			//Inicializamos todas las variables que deberá contener el ProcurementProject
			String name = null;
			int typeCode = 0, subTypeCode = 0;
			
			Element nameNode = null, typeCodeNode = null, subTypeCodeNode = null;
			
			/* 
				-> Si los de cardinal 1 faltan, lanzamos un mensaje de error
				-> Si los de cardinal [0..1] o [0..*] faltan, se pone a null dentro de la clase padre, pero no se detiene la ejecución
			*/
			
			// Compruebo la existencia del Name, si no existe se queda a null y mandamos mensaje
			nameNode = (Element) pp.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
			if (nameNode != null) {
				name = nameNode.getTextContent();
				this.procurementProject.setName(name);
			}else{
				System.err.print("ERROR FATAL: ProcurementProject -> NAME no existe\n");
			}
			
			// Compruebo la existencia del TypeCode, si no existe se queda a null y mandamos mensaje
			typeCodeNode = (Element) pp.getElementsByTagName("cbc:TypeCode").item(POS_UNICO_ELEMENTO);
			if (typeCodeNode != null){
				typeCode = Integer.parseInt(typeCodeNode.getTextContent());
				this.procurementProject.setTypeCode(typeCode);
			}
					
			// Compruebo la existencia del SubTypeCode, si no existe se queda a null y mandamos mensaje
			subTypeCodeNode = (Element) pp.getElementsByTagName("cbc:SubTypeCode").item(POS_UNICO_ELEMENTO);	
			if (subTypeCodeNode != null){
				subTypeCode = Integer.parseInt(subTypeCodeNode.getTextContent());
				this.procurementProject.setSubTypeCode(subTypeCode);
			}
			
			this.procurementProject.readBudgetAmount(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRequiredCommodityClassification(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readPlannedPeriod(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRealizedLocation(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readContractExtension(pp, POS_UNICO_ELEMENTO);
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


//	public TenderResult[] getTenderResultList() {
//		return tenderResultList;
//	}
//
//
//	public void setTenderResultList(TenderResult[] tenderResultList) {
//		this.tenderResultList = tenderResultList;
//	}
//
//
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
