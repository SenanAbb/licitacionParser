package contractFolderStatus;

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
import procurementProjectLot.ProcurementProjectLot;
import tenderResult.TenderResult;
import tenderingProcess.TenderingProcess;
import tenderingTerms.TenderingTerms;

/*
 * 		contractFolderID: String[1]
 * 		contractFolderStatusCode: String[1]
 * 		locatedContractingParty: LocatedContractingParty[1]
 * 		procurementProject: ProcurementProject[1]
 * 		procurementProjectLotList: ProcurementProjectLot[0..*]
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
	private ProcurementProjectLot[] procurementProjectLotList;
	private TenderResult[] tenderResultList;
	private TenderingTerms tenderingTerms;
	private TenderingProcess tenderingProcess;
	private ContractModification[] contractModificationList;
	private LegalDocumentReference legalDocumentReference;
	private TechnicalDocumentReference technicalDocumentReference;
	private AdditionalDocumentReference[] additionalDocumentReferenceList;
	private ValidNoticeInfo[] validNoticeInfoList;
	private GeneralDocument[] generalDocumentList;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:LocatedContractingParty del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:ProcurementProject del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:ProcurementProjectLot del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readProcurementProjectLot(Element cfs, int POS_UNICO_ELEMENTO){
		this.procurementProjectLotList = null;
		
		NodeList procurementProjectLotList = cfs.getElementsByTagName("cac:ProcurementProjectLot");
		if (procurementProjectLotList.getLength() > 0){
			this.procurementProjectLotList = new ProcurementProjectLot[procurementProjectLotList.getLength()];
			
			for (int i = 0; i < procurementProjectLotList.getLength(); i++){
				ProcurementProjectLot procurementProjectLot = new ProcurementProjectLot();
				Element ppl = (Element) procurementProjectLotList.item(i);
				
				procurementProjectLot.readAttributes(ppl, POS_UNICO_ELEMENTO);
				procurementProjectLot.readProcurementProject(ppl, POS_UNICO_ELEMENTO);
				
				this.procurementProjectLotList[i] = procurementProjectLot;
			}
		}
	}
	
	/**
	 * Lee el cac:TenderResult del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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

	/**
	 * Lee el cac:TenderingTerms del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:TenderingProcess del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:ContractModification del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:LegalDocumentReference del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readLegalDocumentReference(Element cfs, int POS_UNICO_ELEMENTO){
		this.legalDocumentReference = null;
		
		Element ldr = (Element) cfs.getElementsByTagName("cac:LegalDocumentReference").item(POS_UNICO_ELEMENTO);
		if (ldr != null){
			this.legalDocumentReference = new LegalDocumentReference();
			this.legalDocumentReference.readAttributes(ldr, POS_UNICO_ELEMENTO);
			this.legalDocumentReference.readAttachment(ldr, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee el cac:TechnicalDocumentReference del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readTechnicalDocumentReference(Element cfs, int POS_UNICO_ELEMENTO){
		this.technicalDocumentReference = null;
		
		Element tdr = (Element) cfs.getElementsByTagName("cac:TechnicalDocumentReference").item(POS_UNICO_ELEMENTO);
		if (tdr != null){
			this.technicalDocumentReference = new TechnicalDocumentReference();
			this.technicalDocumentReference.readAttributes(tdr, POS_UNICO_ELEMENTO);
			this.technicalDocumentReference.readAttachment(tdr, POS_UNICO_ELEMENTO);
		}
	}

	/**
	 * Lee el cac:AdditionalDocumentReference del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	/**
	 * Lee el cac:ValidNoticeInfo del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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

	/**
	 * Lee el cac:GeneralDocument del documento
	 * 
	 * @param cfs El cac-place-ext:ContractFolderStatus que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	public ProcurementProjectLot[] getProcurementProjectLotList() {
		return procurementProjectLotList;
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
