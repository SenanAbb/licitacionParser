package procurementProject;

import org.w3c.dom.Element;

/**
 * @params
 * 		descrtiption: String [0..1]
 */
public class OptionValidityPeriod {
	private String description;
	
	public void readAttributes(Element ovp, int POS_UNICO_ELEMENTO) {
		this.description = null;
		
		Element description = (Element) ovp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent().trim();
		}
	}
	
	public void print() {
		System.out.print("**** OPTION VALIDITY PERIOD ****\n" +
				"----> Options Description: " + description + "\n" +
				"--------------------------------\n");
	}
}
