package procurementProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 *		durationMeasure: double [0..1]
 *		durationMeasureUnitCode: String [0..1]
 *		startDate: Date [0..1]
 *		endDate: Date [0..1]
 */
public class PlannedPeriod {
	private double durationMeasure;
	private Date startDate, endDate;
	private String durationMeasureUnitCode;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pPeriod El cac:PlannedPeriod que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element pPeriod, int POS_UNICO_ELEMENTO) {
		Element start = (Element) pPeriod.getElementsByTagName("cbc:StartDate").item(POS_UNICO_ELEMENTO);
		Element end = (Element) pPeriod.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
		Element duration = (Element) pPeriod.getElementsByTagName("cbc:DurationMeasure").item(POS_UNICO_ELEMENTO);
		
		if(start != null && end != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			Date endDate = null;
			try {
				startDate = format.parse(start.getTextContent());
				endDate = format.parse(end.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.startDate = startDate;
			this.endDate = endDate;
		}else if (start != null && duration != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date startDate = null;
			try {
				startDate = format.parse(start.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.startDate = startDate;
			this.durationMeasure = Double.parseDouble(duration.getTextContent());
			String durationMeasureUnitCode = duration.getAttributes().getNamedItem("unitCode").getTextContent();
			this.durationMeasureUnitCode = durationMeasureUnitCode;
		}else if (duration != null){
			this.durationMeasure = Double.parseDouble(duration.getTextContent());
			String durationMeasureUnitCode = duration.getAttributes().getNamedItem("unitCode").getTextContent();
			this.durationMeasureUnitCode = durationMeasureUnitCode;
		}else{
			this.startDate = null;
			this.endDate = null;
			this.durationMeasure = -1;
		}	
	}
	
	public double getDurationMeasure() {
		return durationMeasure;
	}

	public java.sql.Date getStartDate() {
		if (startDate != null){
			return new java.sql.Date(startDate.getTime());	
		}else{
			return null;
		}
	}

	public java.sql.Date getEndDate() {
		if (endDate != null){
			return new java.sql.Date(startDate.getTime());
		}else{
			return null;
		}
	}

	public String getUnitCode() {
		return durationMeasureUnitCode;
	}
}
