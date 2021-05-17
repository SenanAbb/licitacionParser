package procurementProject;

import org.w3c.dom.Element;

/**
 * @params
 *		optionsDescription: String [0..1]
 *		optionValidityPeriod: OptionValidityPeriod [0..1]
 */
public class ContractExtension {
	private String optionsDescription;
	private OptionValidityPeriod optionValidityPeriod;
	
	public void readAttributes(Element ce, int POS_UNICO_ELEMENTO) {
		this.optionsDescription = null;
		this.optionValidityPeriod = null;
		
		Element optionsDesc = (Element) ce.getElementsByTagName("cbc:OptionsDescription").item(POS_UNICO_ELEMENTO);
		if (optionsDesc != null){
			this.optionsDescription = optionsDesc.getTextContent();
		}
		
		Element ovp = (Element) ce.getElementsByTagName("cac:OptionValidityPeriod").item(POS_UNICO_ELEMENTO);
		if (ovp != null){
			this.optionValidityPeriod = new OptionValidityPeriod();
			this.optionValidityPeriod.readAttributes(ovp, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("*** CONTRACT EXTENSION ***\n" +
						 "---> Options Description: " + optionsDescription + "\n");
		if (optionValidityPeriod != null){
			optionValidityPeriod.print();
		}else{
			System.out.print("**** OPTION VALIDITY PERIOD: null ****\n");
		}
	}

	public String getOptionsDescription() {
		return optionsDescription;
	}

	public OptionValidityPeriod getOptionValidityPeriod() {
		return optionValidityPeriod;
	}
	
}
