package tenderingProcess;

import java.util.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.w3c.dom.Element;

/**
 * @params
 * 		typeCode: int[1]
 * 		identificationID: String[0..1]
 * 		description: String[0..1]
 * 		ocurrenceDate: Date[0..1]
 *		ocurrenceTime: Time[0..1]
 *		ocurrenceLocation: OcurrenceLocation[1]
 */
public class OpenTenderEvent {
	private int typeCode;
	private String identificationID, description;
	private Date ocurrenceDate;
	private Time ocurrenceTime;
	private OcurrenceLocation ocurrenceLocation;
	
	public void readAttributes(Element ote, int POS_UNICO_ELEMENTO){
		this.typeCode = -1;
		this.identificationID = null;
		this.description = null;
		this.ocurrenceDate = null;
		this.ocurrenceTime = null;
		
		/* TYPE CODE */
		Element tc = (Element) ote.getElementsByTagName("cbc:TypeCode").item(POS_UNICO_ELEMENTO);
		if (tc != null){
			this.typeCode = Integer.parseInt(tc.getTextContent());
		}else{
			System.err.print("ERROR FATAL: TenderingProcess -> OpenTenderEvent -> TYPE CODE no existe\n");
		}
		
		/* IDENTIFICATION ID */
		Element iid = (Element) ote.getElementsByTagName("cbc:IdentificationID").item(POS_UNICO_ELEMENTO);
		if (iid != null){
			this.identificationID = iid.getTextContent();
		}
		
		/* DESCRIPTION */
		Element description = (Element) ote.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
		
		/* END DATE */
		Element ed = (Element) ote.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
		if(ed != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = null;
			try {
				endDate = format.parse(ed.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.ocurrenceDate = endDate;
		}
		
		/* END TIME */
		Element et = (Element) ote.getElementsByTagName("cbc:EndTime").item(POS_UNICO_ELEMENTO);
		if (et != null){
			SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
			Date date = null;
			Time endTime = null;
			try {
				date = (Date) format.parse(et.getTextContent());
				endTime = new Time(date.getTime());
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			this.ocurrenceTime = endTime;
		}
	}

	public void readOcurrenceLocation(Element ote, int POS_UNICO_ELEMENTO) {
		this.ocurrenceLocation = null;
		
		Element ol = (Element) ote.getElementsByTagName("cac:OcurrenceLocation").item(POS_UNICO_ELEMENTO);
		if (ol != null){
			this.ocurrenceLocation = new OcurrenceLocation();
			this.ocurrenceLocation.readAttributes(ol, POS_UNICO_ELEMENTO);
			this.ocurrenceLocation.readAddress(ol, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: TenderingProcess -> OpenTenderEvent -> OCURRENCE LOCATION no existe\n");
		}
	}
	
	public void print(){
		System.out.print("*** TENDER SUBMISSION DEADLINE PERIOD ***\n" +
						 "---> Type Code: " + typeCode + "\n" +
						 "---> Identification ID: " + identificationID + "\n" +
						 "---> Description: " + description + "\n" +
						 "---> Ocurrence Date: " + ocurrenceDate + "\n" +
						 "---> Ocurrence Time: " + ocurrenceTime + "\n");
						 this.ocurrenceLocation.print();
		System.out.print("--------------------------------\n");
	}
}
