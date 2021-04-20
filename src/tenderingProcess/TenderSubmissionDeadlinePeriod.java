package tenderingProcess;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/**
 * @params
 *		endDate: Date[0..1]
 *		endTime: Time[0..1]
 *		description: String[0..1]
 */
public class TenderSubmissionDeadlinePeriod {
	private Date endDate;
	private Time endTime;
	private String description;
	
	public void readAttributes(Element tsdp, int POS_UNICO_ELEMENTO) {
		this.endDate = null;
		this.endTime = null;
		this.description = null;
		
		/* END DATE */
		Element ed = (Element) tsdp.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
		if(ed != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = null;
			try {
				endDate = format.parse(ed.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.endDate = endDate;
		}
		
		/* END TIME */
		Element et = (Element) tsdp.getElementsByTagName("cbc:EndTime").item(POS_UNICO_ELEMENTO);
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
			this.endTime = endTime;
		}
		
		/* DESCRIPTION */
		Element description = (Element) tsdp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent().trim();
		}
	}

	public void print(){
		System.out.print("*** TENDER SUBMISSION DEADLINE PERIOD ***\n" +
						 "---> End Date: " + endDate + "\n" +
						 "---> End Time: " + endTime + "\n" +
						 "---> Description: " + description + "\n" +
						 "--------------------------------\n");
	}
}
