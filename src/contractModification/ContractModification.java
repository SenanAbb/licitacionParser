package contractModification;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 * 		contractID: String[1]
 * 		ID: String[1]
 *		date: Date[0..1]
 *		note: String[0..1]
 *		contractModificationDurationMeasure: int[0..1]
 *		contractModificationDurationMeasureUnitCode: String[0..1]
 *		finalDurationMeasure: int[0..1]
 *		finalDurationMeasureUnitCode: String[0..1]
 *		contractModificationLegalMonetaryTotal: ContractModificationLegalMonetaryTotal[0..1]
 *		finalLegalMonetaryTotal: FinalLegalMonetaryTotal[0..1]
 */
public class ContractModification {
	private String contractID, note;
	private Date date;
	private int contractModificationDurationMeasure, finalDurationMeasure, ID;
	private String contractModificationDurationMeasureUnitCode, finalDurationMeasureUnitCode;
	private ContractModificationLegalMonetaryTotal contractModificationLegalMonetaryTotal;
	private FinalLegalMonetaryTotal finalLegalMonetaryTotal;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param cm El cac:ContractModification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element cm, int POS_UNICO_ELEMENTO) {
		this.contractID = null;
		this.ID = -1;
		this.note = null;
		this.date = null;
		this.contractModificationDurationMeasure = -1;
		this.finalDurationMeasure = -1;
		this.contractModificationDurationMeasureUnitCode = null;
		this.finalDurationMeasureUnitCode = null;
		
		/* CONTRACT ID */
		Element cid = (Element) cm.getElementsByTagName("cbc:ContractID").item(POS_UNICO_ELEMENTO);
		try{
			this.contractID = cid.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> ContractModification -> CONTRACT ID no existe\n");
		}
		
		/* ID */
		Element id = (Element) cm.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		try{
			this.ID = Integer.parseInt(id.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ContractFolderStatus -> ContractModification -> ID no existe\n");
		}
		
		/* NOTE */
		Element note = (Element) cm.getElementsByTagName("cbc:Note").item(POS_UNICO_ELEMENTO);
		if (note != null){
			this.note = note.getTextContent();
		}
		
		/* DATE */
		Element d = (Element) cm.getElementsByTagName("cbc:Date").item(POS_UNICO_ELEMENTO);
		if(d != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = null;
			try {
				date = format.parse(d.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.date = date;
		}
		
		/* CONTRACT MODIFICATION DURATION MEASURE && UNIT CODE*/
		Element cmdm = (Element) cm.getElementsByTagName("cbc:ContractModificationDurationMeasure").item(POS_UNICO_ELEMENTO);
		if (cmdm != null){
			this.contractModificationDurationMeasure = Integer.parseInt(cmdm.getTextContent().trim());
			this.contractModificationDurationMeasureUnitCode = cmdm.getAttributes().getNamedItem("unitCode").getTextContent();
		}
		
		/* FINAL DURATION MEASURE && UNIT CODE*/
		Element fdm = (Element) cm.getElementsByTagName("cbc:FinalDurationMeasure").item(POS_UNICO_ELEMENTO);
		if (fdm != null){
			this.finalDurationMeasure = Integer.parseInt(fdm.getTextContent().trim());
			this.finalDurationMeasureUnitCode = fdm.getAttributes().getNamedItem("unitCode").getTextContent();
		}
	}

	/**
	 * Lee el cac:ContractModificationLegalMonetaryTotal del documento
	 * 
	 * @param cm El cac:ContractModification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readContractModificationLegalMonetaryTotal(Element cm, int POS_UNICO_ELEMENTO){
		this.contractModificationLegalMonetaryTotal = null;
		
		Element cmlmt = (Element) cm.getElementsByTagName("cac:ContractModificationLegalMonetaryTotal").item(POS_UNICO_ELEMENTO);
		if (cmlmt != null){
			this.contractModificationLegalMonetaryTotal = new ContractModificationLegalMonetaryTotal();
			this.contractModificationLegalMonetaryTotal.readAttributes(cmlmt, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee el cac:FinalLegalMonetaryTotal del documento
	 * 
	 * @param cm El cac:ContractModification que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readFinalLegalMonetaryTotal(Element cm, int POS_UNICO_ELEMENTO){
		this.finalLegalMonetaryTotal = null;
		
		Element flmt = (Element) cm.getElementsByTagName("cac:FinalLegalMonetaryTotal").item(POS_UNICO_ELEMENTO);
		if (flmt != null){
			this.finalLegalMonetaryTotal = new FinalLegalMonetaryTotal();
			this.finalLegalMonetaryTotal.readAttributes(flmt, POS_UNICO_ELEMENTO);
		}
	}
	
	public String getContractID() {
		return contractID;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getNote() {
		return note;
	}
	
	public int getContractModificationDurationMeasure() {
		return contractModificationDurationMeasure;
	}
	
	public int getFinalDurationMeasure() {
		return finalDurationMeasure;
	}
	
	public String getContractModificationDurationMeasureUnitCode() {
		return contractModificationDurationMeasureUnitCode;
	}
	
	public String getFinalDurationMeasureUnitCode() {
		return finalDurationMeasureUnitCode;
	}
	
	public ContractModificationLegalMonetaryTotal getContractModificationLegalMonetaryTotal() {
		return contractModificationLegalMonetaryTotal;
	}
	
	public FinalLegalMonetaryTotal getFinalLegalMonetaryTotal() {
		return finalLegalMonetaryTotal;
	}
}
