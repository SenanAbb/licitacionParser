package tenderResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

public class ContractFormalizationPeriod {
	private Date startDate, endDate;
	private String description;
	
	public void readAttributes(Element cfp, int POS_UNICO_ELEMENTO) {
		this.startDate = null;
		this.endDate = null;
		this.description = null;
		
		Element startDateElement = (Element) cfp.getElementsByTagName("cbc:StartDate").item(POS_UNICO_ELEMENTO);
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
		
		Element endDateElement = (Element) cfp.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
		if (endDateElement != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date endDate = null;
			try {
				endDate = format.parse(endDateElement.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.endDate = endDate;
		}
		
		Element description = (Element) cfp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public ContractFormalizationPeriod(){}


	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public Date getStartDate() {
		return startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public String getDescription() {
		return description;
	}
}
