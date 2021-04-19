package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params
 *		requirementTypeCode: int[0..1]
 */
public class SpecificTendererRequirement {
	private int requirementTypeCode;
	
	public void readAttributes(Element str, int POS_UNICO_ELEMENTO){
		this.requirementTypeCode = -1;
		
		Element rtc = (Element) str.getElementsByTagName("cbc:RequirementTypeCode").item(POS_UNICO_ELEMENTO);
		if (rtc != null){
			this.requirementTypeCode = Integer.parseInt(rtc.getTextContent());
		}
	}
	
	public void print(){
		System.out.print("**** SPECIFIC TENDERER REQUIREMENT ****\n" +
						 "----> Requirement Type Code: " + requirementTypeCode + "\n" +
						 "--------------------------------\n");
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	public SpecificTendererRequirement(){}
}
