package procurementProject;

public class ContractExtension {
	private String optionsDescription;
	private OptionValidityPeriod optionValidityPeriod;
	
	public void print(){
		System.out.print("** CONTRACT EXTENSION **\n" +
				"--> Options Description: " + optionsDescription + "\n");
		if (optionValidityPeriod != null){
			optionValidityPeriod.print();
		}else{
			System.out.print("*** OPTION VALIDITY PERIOD: null ***\n");
		}
	}
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public ContractExtension() {}


	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getOptionsDescription() {
		return optionsDescription;
	}
	
	public void setOptionsDescription(String optionsDescription) {
		this.optionsDescription = optionsDescription;
	}

	public OptionValidityPeriod getOptionValidityPeriod() {
		return optionValidityPeriod;
	}

	public void setOptionValidityPeriod(OptionValidityPeriod optionValidityPeriod) {
		this.optionValidityPeriod = optionValidityPeriod;
	}
}
