package tenderingProcess;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 * 		endDate: Date[0..1]
 *		endTime: Time[0..1]
 *		description: String[0..1]
 */
public class ParticipationRequestReceptionPeriod {
	private Date endDate;
	private Time endTime;
	private String description;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param prrp El cac:ParticipationRequestReceptionPeriod que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element prrp, int POS_UNICO_ELEMENTO) {
		this.endDate = null;
		this.endTime = null;
		this.description = null;
		
		/* END DATE */
		Element ed = (Element) prrp.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
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
		Element et = (Element) prrp.getElementsByTagName("cbc:EndTime").item(POS_UNICO_ELEMENTO);
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
		Element description = (Element) prrp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent().trim();
		}
	}

	public java.sql.Date getEndDate() {
		if (endDate != null){
			return new java.sql.Date(endDate.getTime());
		}else{
			return null;
		}
	}

	public Time getEndTime() {
		return endTime;
	}

	public String getDescription() {
		if (description != null){
			return description.trim().replaceAll("\n", " - ");
		}else{
			return null;
		}
	}
	
}
