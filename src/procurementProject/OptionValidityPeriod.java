package procurementProject;

public class OptionValidityPeriod {
	private String description;
	
	public void print() {
		System.out.print("*** OPTION VALIDITY PERIOD ***\n" +
				"--> Options Description: " + description + "\n" +
				"--------------------------------\n");
	}
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public OptionValidityPeriod(){}

	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
