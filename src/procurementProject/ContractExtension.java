package procurementProject;

import org.w3c.dom.Element;

/*
 *		optionsDescription: String [0..1]
 *		optionValidityPeriod: OptionValidityPeriod [0..1]
 */
public class ContractExtension {
	private String optionsDescription;
	private OptionValidityPeriod optionValidityPeriod;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param ce El cac:ContractExtension que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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

	public String getOptionsDescription() {
		return optionsDescription;
	}

	public OptionValidityPeriod getOptionValidityPeriod() {
		return optionValidityPeriod;
	}
	
}
