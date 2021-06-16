package tenderResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * @params
 * 		resultCode: int [1]
 * 		description: String[0..1]
 * 		awardDate: String[1]
 * 		receivedTenderQuantity: int[1]
 * 		lowerTenderAmount: double[0..1]
 *		higherTenderAmount: double[0..1]
 *		abnormallyLowTenderIndicator: bool[0..1]
 *		startDate: Date[0..1]
 *		contractList: ContractList[] [0..*]
 *		awardedTenderedProject: AwardedTenderedProject[0..1]
 *		winningParty: WinningParty[0..1]
 *		SMEAwardedIndicator: bool[0..1]
 *		subcontractTerms: SubcontractTerms[0..1]
 */
public class TenderResult {
	private int resultCode;
	private String description;
	private int receivedTenderQuantity;
	private double lowerTenderAmount, higherTenderAmount;
	private boolean abnormallyLowTenderIndicator, SMEAwardedIndicator;
	private Date startDate, awardDate;
	private Contract[] contractList;
	private AwardedTenderedProject awardedTenderedProject;
	private WinningParty winningParty;
	private SubcontractTerms subcontractTerms;
	
	public void readAttributes(Element tr, int POS_UNICO_ELEMENTO){
		this.resultCode = -1;
		this.description = null;
		this.receivedTenderQuantity = -1;
		this.lowerTenderAmount = -1;
		this.higherTenderAmount = -1;
		this.startDate = null;
		this.awardDate = null;
		
		/* RESULT CODE */
		Element resultCode = (Element) tr.getElementsByTagName("cbc:ResultCode").item(POS_UNICO_ELEMENTO);
		try{
			this.resultCode = Integer.parseInt(resultCode.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderResult -> RESULT CODE no existe\n");
		}
		
		/* DESCRIPTION */
		Element description = (Element) tr.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent(); 
		}
		
		/* AWARD DATE */
		Element awDate = (Element) tr.getElementsByTagName("cbc:AwardDate").item(POS_UNICO_ELEMENTO);
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date awardDate = null;
			try {
				awardDate = format.parse(awDate.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.awardDate = awardDate;
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderResult -> AWARD DATE no existe\n");
		}
		
		/* RECEIVED TENDER QUANTITY */
		Element rtq = (Element) tr.getElementsByTagName("cbc:ReceivedTenderQuantity").item(POS_UNICO_ELEMENTO);
		try {
			this.receivedTenderQuantity = Integer.parseInt(rtq.getTextContent());
		} catch (NullPointerException e) {
			System.err.print("ERROR FATAL: TenderResult -> RECEIVED TENDER QUANTITY no existe\n");
		}
		
		/* LOWER TENDER AMOUNT */
		Element lta = (Element) tr.getElementsByTagName("cbc:LowerTenderAmount").item(POS_UNICO_ELEMENTO);
		if(lta != null){
			this.lowerTenderAmount = Double.parseDouble(lta.getTextContent());
		}
		
		/* HIGHER TENDER AMOUNT */
		Element hta = (Element) tr.getElementsByTagName("cbc:HigherTenderAmount").item(POS_UNICO_ELEMENTO);
		if(hta != null){
			this.higherTenderAmount = Double.parseDouble(hta.getTextContent());
		}
		
		/* SME AWARDED INDICATOR */
		Element sme = (Element) tr.getElementsByTagName("cbc:SMEAwardedIndicator").item(POS_UNICO_ELEMENTO);
		if(sme != null){
			this.SMEAwardedIndicator = Boolean.parseBoolean(sme.getTextContent());
		}
		
		/* ABNORMALLY LOW TENDER INDICATOR */
		Element alti = (Element) tr.getElementsByTagName("cbc:AbnormallyLowTendersIndicator").item(POS_UNICO_ELEMENTO);
		if(alti != null){
			this.abnormallyLowTenderIndicator = Boolean.parseBoolean(alti.getTextContent());
		}
		
		/* START DATE */
		Element startDateElement = (Element) tr.getElementsByTagName("cbc:StartDate").item(POS_UNICO_ELEMENTO);
		if (startDateElement != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			try {
				startDate = format.parse(startDateElement.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.startDate = startDate;
		}
	}

	public void readContractList(Element tr, int POS_UNICO_ELEMENTO){
		this.contractList = null;
		NodeList contractNodeList = tr.getElementsByTagName("cac:Contract");
		
		if(contractNodeList.getLength() > 0){
			this.contractList = new Contract[contractNodeList.getLength()];
			
			for (int i = 0; i < contractNodeList.getLength(); i++){
				Contract c = new Contract();
				
				Element contract = (Element) contractNodeList.item(i);
				c.readAttributes(contract, POS_UNICO_ELEMENTO);
				
				this.contractList[i] = c;
			}
		}
	}
	
	public void readWinningParty(Element tr, int POS_UNICO_ELEMENTO) {
		this.winningParty = null;
		
		Element wp = (Element) tr.getElementsByTagName("cac:WinningParty").item(POS_UNICO_ELEMENTO);
		if(wp != null){
			this.winningParty = new WinningParty();
			this.winningParty.readPartyName(wp, POS_UNICO_ELEMENTO);
			this.winningParty.readPartyIdentificationList(wp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readAwardedTenderedProject(Element tr, int POS_UNICO_ELEMENTO) {
		this.awardedTenderedProject = null;
		
		Element atp = (Element) tr.getElementsByTagName("cac:AwardedTenderedProject").item(POS_UNICO_ELEMENTO);
		if (atp != null){
			this.awardedTenderedProject = new AwardedTenderedProject();
			this.awardedTenderedProject.readAttributes(atp, POS_UNICO_ELEMENTO);
			this.awardedTenderedProject.readLegalMoneratyTotalList(atp, POS_UNICO_ELEMENTO);
			this.awardedTenderedProject.readContractFormalizationPeriod(atp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readSubcontractTerms(Element tr, int POS_UNICO_ELEMENTO) {
		this.subcontractTerms = null;
		
		Element sct = (Element) tr.getElementsByTagName("cac:SubcontractTerms").item(POS_UNICO_ELEMENTO);
		if (sct != null){
			this.subcontractTerms = new SubcontractTerms();
			this.subcontractTerms.readAttributes(sct, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("** TENDER RESULT **\n" +
				"--> Result Code: " + resultCode + "\n" +
				"--> Description: " + description + "\n" +
				"--> Award Date: " + awardDate + "\n" +
				"--> Start Date: " + startDate + "\n" +
				"--> Received Tender Quantity: " + receivedTenderQuantity + "\n" +
				"--> Lower Tender Amount: " + lowerTenderAmount + "\n" +
				"--> Higher Tender Amount: " + higherTenderAmount + "\n" +
				"--> SME Awarded Indicator: " + SMEAwardedIndicator + "\n" +
				"--> Abnormally Low Tenders Indicator: " + abnormallyLowTenderIndicator + "\n" +
				"--------------------------------\n");
		if (contractList != null){
			for (Contract c : contractList){
				c.print();
			}
		}else{
			System.out.print("*** CONTRACT LIST: null ***\n" + 
					"--------------------------------\n");
		}
		
		if (winningParty != null){
			winningParty.print();
		}else{
			System.out.print("*** WINNING PARTY: null ***\n" + 
					"--------------------------------\n");
		}
		
		if (awardedTenderedProject != null){
			awardedTenderedProject.print();
		}else{
			System.out.print("*** AWARDED TENDERED PROJECT: null ***\n" + 
					"--------------------------------\n");
		}
		
		if (subcontractTerms != null){
			subcontractTerms.print();
		}else{
			System.out.print("*** SUBCONTRACT TERMS: null ***\n" + 
					"--------------------------------\n");
		}
		System.out.print("===============================================================\n");
	}

	public int getResultCode() {
		return resultCode;
	}

	public String getDescription() {
		return description;
	}

	public int getReceivedTenderQuantity() {
		return receivedTenderQuantity;
	}

	public double getLowerTenderAmount() {
		return lowerTenderAmount;
	}

	public double getHigherTenderAmount() {
		return higherTenderAmount;
	}

	public boolean getAbnormallyLowTenderIndicator() {
		return abnormallyLowTenderIndicator;
	}

	public java.sql.Date getAwardDate() {
		if (awardDate != null){
			return new java.sql.Date(awardDate.getTime());	
		}else{
			return null;
		}
	}

	public Contract[] getContractList() {
		return contractList;
	}
	
	public java.sql.Date getStartDate() {
		if (startDate != null){
			return new java.sql.Date(startDate.getTime());	
		}else{
			return null;
		}
	}

	public WinningParty getWinningParty() {
		return winningParty;
	}

	public boolean getSMEAwardedIndicator() {
		return SMEAwardedIndicator;
	}

	public AwardedTenderedProject getAwardedTenderedProject() {
		return awardedTenderedProject;
	}

	public SubcontractTerms getSubcontractTerms() {
		return subcontractTerms;
	}
}
