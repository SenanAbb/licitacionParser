package tenderingProcess;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import utils.DocumentAvailabilityPeriod;

/**
 * @params
 * 		procedureCode: int[1]
 * 		contractingSystemCode: int[0..1]
 *		urgencyCode: int[0..1]
 *		submissionMethodCode: int[0..1]
 *		maximumLotPresentationQuantity: int[0..1]
 *		maximumTendererAwardedLotsQuantity: int[0..1]
 *		partPresentationCode: int[0..1]
 *		lotsCombinationContractingAuthorityRights: String[0..1]
 *		overThresholdIndicator: boolean[0..1]
 *		auctionTerms: AuctionTerms[0..1]
 *		tenderSubmissionDeadlinePeriod: TenderSubmissionDeadlinePeriod[0..1]
 *		openTenderEventList: OpenTenderEvent[] [0..*]
 *		economicOperatorShortList: EconomicOperatorShortList[0..1]
 *		processJustification: ProcessJustification[0..1]
 *		participationRequestReceptionPeriod: ParticipationRequestReceptionPeriod[0..1]
 *		documentAvailabilityPeriod: DocumentAvailabilityPeriod[0..1]
 */
public class TenderingProcess {
	private int procedureCode, contractingSystemCode, urgencyCode, submissionMethodCode, maximumLotPresentationQuantity,
		maximumTendererAwardedLotsQuantity, partPresentationCode;
	private String lotsCombinationContractingAuthorityRights;
	private boolean overThresholdIndicator;
	private AuctionTerms auctionTerms;
	private TenderSubmissionDeadlinePeriod tenderSubmissionDeadlinePeriod;
	private OpenTenderEvent[] openTenderEventList;
	private EconomicOperatorShortList economicOperatorShortList;
	private ProcessJustification processJustification;
	private ParticipationRequestReceptionPeriod participationRequestReceptionPeriod;
	private DocumentAvailabilityPeriod documentAvailabilityPeriod;
	
	public void readAttributes(Element tp, int POS_UNICO_ELEMENTO){
		this.procedureCode = -1;
		this.contractingSystemCode = -1;
		this.urgencyCode = -1;
		this.submissionMethodCode = -1;
		this.maximumLotPresentationQuantity = -1;
		this.maximumTendererAwardedLotsQuantity = -1;
		this.partPresentationCode = -1;
		this.lotsCombinationContractingAuthorityRights = null;
		
		/* PROCEDURE CODE */
		Element pc = (Element) tp.getElementsByTagName("cbc:ProcedureCode").item(POS_UNICO_ELEMENTO);
		try{
			this.procedureCode = Integer.parseInt(pc.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractingFolderStatus -> TenderingProcess -> PROCEDURE CODE no existe\n");
		}
		
		/* CONTRACTING SYSTEM CODE */
		Element csc = (Element) tp.getElementsByTagName("cbc:ContractingSystemCode").item(POS_UNICO_ELEMENTO);
		try{
			this.contractingSystemCode = Integer.parseInt(csc.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractingFolderStatus -> TenderingProcess -> CONTRACTING SYSTEM CODE no existe\n");
		}
		
		/* URGENCY CODE */
		Element uc = (Element) tp.getElementsByTagName("cbc:UrgencyCode").item(POS_UNICO_ELEMENTO);
		if (uc != null){
			this.urgencyCode = Integer.parseInt(uc.getTextContent());
		}
		
		/* SUBMISSION METHOD CODE */
		Element smc = (Element) tp.getElementsByTagName("cbc:SubmissionMethodCode").item(POS_UNICO_ELEMENTO);
		if (smc != null){
			this.submissionMethodCode = Integer.parseInt(smc.getTextContent());
		}
		
		/* MAXIMUM LOT PRESENTATION QUANTITY */
		Element mlpq = (Element) tp.getElementsByTagName("cbc:MaximumLotPresentationQuantity").item(POS_UNICO_ELEMENTO);
		if (mlpq != null){
			this.maximumLotPresentationQuantity = Integer.parseInt(mlpq.getTextContent());
		}
		
		/* MAXIMUM TENDERER AWARDED LOTS QUANTITY */
		Element mtalq = (Element) tp.getElementsByTagName("cbc:MaximumTendererAwardedLotsQuantity").item(POS_UNICO_ELEMENTO);
		if (mtalq != null){
			this.maximumTendererAwardedLotsQuantity = Integer.parseInt(mtalq.getTextContent());
		}
		
		/* PART PRESENTATION CODE */
		Element ppc = (Element) tp.getElementsByTagName("cbc:PartPresentationCode").item(POS_UNICO_ELEMENTO);
		if (ppc != null){
			this.partPresentationCode = Integer.parseInt(ppc.getTextContent());
		}
		
		/* LOTS COMBINATION CONTRACTING AUTHORITY RIGHTS */
		Element lccar = (Element) tp.getElementsByTagName("cbc:LotsCombinationContractingAuthorityRights").item(POS_UNICO_ELEMENTO);
		if (lccar != null){
			this.lotsCombinationContractingAuthorityRights = lccar.getTextContent();
		}
		
		/* OVER THRESHOLD INDICATOR */
		Element oti = (Element) tp.getElementsByTagName("cbc:OverThresholdIndicator").item(POS_UNICO_ELEMENTO);
		if (oti != null){
			this.overThresholdIndicator = Boolean.parseBoolean(oti.getTextContent());
		}
	}

	public void readAuctionTerms(Element tp, int POS_UNICO_ELEMENTO) {
		this.auctionTerms = null;
		
		Element at = (Element) tp.getElementsByTagName("cac:AuctionTerms").item(POS_UNICO_ELEMENTO);
		if (at != null){
			this.auctionTerms = new AuctionTerms();
			this.auctionTerms.readAttributes(tp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readTenderSubmissionDeadlinePeriod(Element tp, int POS_UNICO_ELEMENTO){
		this.tenderSubmissionDeadlinePeriod = null;
		
		Element tsdp = (Element) tp.getElementsByTagName("cac:TenderSubmissionDeadlinePeriod").item(POS_UNICO_ELEMENTO);
		if (tsdp != null){
			this.tenderSubmissionDeadlinePeriod = new TenderSubmissionDeadlinePeriod();
			this.tenderSubmissionDeadlinePeriod.readAttributes(tsdp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readOpenTenderEvent(Element tp, int POS_UNICO_ELEMENTO){
		this.openTenderEventList = null;
		
		NodeList oteNodeList = tp.getElementsByTagName("cac:OpenTenderEvent");
		if (oteNodeList.getLength() > 0){
			this.openTenderEventList = new OpenTenderEvent[oteNodeList.getLength()];
			
			for (int i = 0; i < oteNodeList.getLength(); i++){
				OpenTenderEvent ote = new OpenTenderEvent();
				
				Element oteElement = (Element) oteNodeList.item(i);
				ote.readAttributes(oteElement, POS_UNICO_ELEMENTO);
				ote.readOcurrenceLocation(oteElement, POS_UNICO_ELEMENTO);
				
				this.openTenderEventList[i] = ote;
			}
		}
	}
	
	public void readEconomicOperatorShortList(Element tp, int POS_UNICO_ELEMENTO){
		this.economicOperatorShortList = null;
		
		Element eosl = (Element) tp.getElementsByTagName("cac:EconomicOperatorShortList").item(POS_UNICO_ELEMENTO);
		if (eosl != null){
			this.economicOperatorShortList = new EconomicOperatorShortList();
			this.economicOperatorShortList.readAttributes(eosl, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readProcessJustification(Element tp, int POS_UNICO_ELEMENTO){
		this.processJustification = null;
		
		Element pj = (Element) tp.getElementsByTagName("cac:ProcessJustification").item(POS_UNICO_ELEMENTO);
		if (pj != null){
			this.processJustification = new ProcessJustification();
			this.processJustification.readAttributes(pj, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readParticipationRequestReceptionPeriod(Element tp, int POS_UNICO_ELEMENTO){
		this.participationRequestReceptionPeriod = null;
		
		Element prrp = (Element) tp.getElementsByTagName("cac:ParticipationRequestReceptionPeriod").item(POS_UNICO_ELEMENTO);
		if (prrp != null){
			this.participationRequestReceptionPeriod = new ParticipationRequestReceptionPeriod();
			this.participationRequestReceptionPeriod.readAttributes(prrp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readDocumentAvailabilityPeriod(Element tp, int POS_UNICO_ELEMENTO){
		this.documentAvailabilityPeriod = null;
		
		Element dap = (Element) tp.getElementsByTagName("cac:DocumentAvailabilityPeriod").item(POS_UNICO_ELEMENTO);
		if (dap != null){
			this.documentAvailabilityPeriod = new DocumentAvailabilityPeriod();
			this.documentAvailabilityPeriod.readAttributes(dap, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		/* ATTRIBUTES */
		System.out.print("** TENDERING PROCESS **\n" +
				"--> Procedure Code: " + procedureCode + "\n" +
				"--> Contracting System Code: " + contractingSystemCode + "\n" +
				"--> Urgency Code: " + urgencyCode + "\n" +
				"--> Submission Method Code: " + submissionMethodCode + "\n" +
				"--> Maximum Lot Presentation Quantity: " + maximumLotPresentationQuantity + "\n" +
				"--> Maximum Tenderer Aw. Lots Quantity: " + maximumTendererAwardedLotsQuantity + "\n" +
				"--> Part Presentation Code: " + partPresentationCode + "\n" +
				"--> Lots Combination Contracting Auth. Rights: " + lotsCombinationContractingAuthorityRights + "\n" +
				"--> Over Threshold Indicator: " + overThresholdIndicator + "\n" +
				"--------------------------------\n");
		
		/* AUCTION TERMS */
		if (auctionTerms != null){
			auctionTerms.print();
		}else{
			System.out.print("*** AUCTION TERMS: null ***\n" +
							 "--------------------------------\n");
		}
		
		/* TENDER SUBMISSION DEADLINE PERIOD */
		if (tenderSubmissionDeadlinePeriod != null){
			tenderSubmissionDeadlinePeriod.print();
		}else{
			System.out.print("*** TENDER SUBMISSION DEADLINE PERIOD: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* OPEN TENDER EVENT */
		if (openTenderEventList != null){
			for (OpenTenderEvent o : openTenderEventList){
				o.print();
			}
		}else{
			System.out.print("*** OPEN TENDER EVENT: null ***\n" +
							 "--------------------------------\n");
		}
		
		/* ECONOMIC OPERATOR SHORT LIST */
		if (economicOperatorShortList != null){
			economicOperatorShortList.print();
		}else{
			System.out.print("*** ECONOMIC OPERATOR SHORT LIST: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* PROCESS JUSTIFICATION */
		if (processJustification != null){
			processJustification.print();
		}else{
			System.out.print("*** PROCESS JUSTIFICATION: null ***\n" +
			 		 		 "--------------------------------\n");
		}	
		
		/* PARTICIPATION REQUEST RECEPTION PERIOD */
		if (participationRequestReceptionPeriod != null){
			participationRequestReceptionPeriod.print();
		}else{
			System.out.print("*** PARTICIPATION REQUEST RECEPTION PERIOD: null ***\n" +
					 		 "--------------------------------\n");
		}
		
		/* DOCUMENT AVAILABILITY PERIOD */
		if(documentAvailabilityPeriod != null){
			documentAvailabilityPeriod.print();
		}else{
			System.out.print("*** DOCUMENT AVAILABILITY PERIOD: null ***\n" +
	 		 		 		 "--------------------------------\n");
		}
		
		System.out.print("===============================================================\n");
	}

	public int getProcedureCode() {
		return procedureCode;
	}
	
	public int getContractingSystemTypeCode() {
		return contractingSystemCode;
	}
	
	public int getUrgencyCode() {
		return urgencyCode;
	}
	
	public int getSubmissionMethodCode() {
		return submissionMethodCode;
	}
	
	public AuctionTerms getAuctionTerms() {
		return auctionTerms;
	}

	public DocumentAvailabilityPeriod getDocumentAvailabilityPeriod(){
		return documentAvailabilityPeriod;
	}
	
	public TenderSubmissionDeadlinePeriod getTenderSubmissionDeadlinePeriod(){
		return tenderSubmissionDeadlinePeriod;
	}
	
	public ParticipationRequestReceptionPeriod getParticipationRequestReceptionPeriod(){
		return participationRequestReceptionPeriod;
	}
}
