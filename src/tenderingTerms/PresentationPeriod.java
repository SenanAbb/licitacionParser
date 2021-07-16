package tenderingTerms;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 *		endDate: Date[0..1]
 *		endTime: Time[0..1]
 */
public class PresentationPeriod {
	private Date endDate;
	private Time endTime;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pp El cac:PresentationPeriod que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element pp, int POS_UNICO_ELEMENTO) {
		this.endDate = null;
		this.endTime = null;
		
		/* END DATE */
		Element ed = (Element) pp.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
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
		Element et = (Element) pp.getElementsByTagName("cbc:EndTime").item(POS_UNICO_ELEMENTO);
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
	}
}
