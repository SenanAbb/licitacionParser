package procurementProject;

import java.util.Date;

public class PlannedPeriod {
	private double durationMeasure;
	private Date startDate, endDate;
	// Atributos
	private String durationMeasureUnitCode;
	
	public void print(){
		System.out.print("** PLANNED PERIOD **\n" +
				"--> Duration Measure: " + durationMeasure + " " + durationMeasureUnitCode + "\n" +
				"--> Start Date: " + startDate + "\n" +
				"--> End Date: " + endDate + "\n" +
				"--------------------------------\n");
	}
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public PlannedPeriod(){}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getDurationMeasureUnitCode(){
		return durationMeasureUnitCode;
	}
	
	public void setUnitCode(String durationMeasureUnitCode){
		this.durationMeasureUnitCode = durationMeasureUnitCode;
	}

	public double getDurationMeasure() {
		return durationMeasure;
	}


	public void setDurationMeasure(double durationMeasure) {
		this.durationMeasure = durationMeasure;
	}


	public Date getStartDate() {
		return startDate;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}


	public Date getEndDate() {
		return endDate;
	}


	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
