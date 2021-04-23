package tenderingProcess;

import org.w3c.dom.Element;

import utils.Address;

/**
 * @params
 *		description: String[1]
 *		address: Address[1]
 */
public class OcurrenceLocation {
	private String description;
	private Address address;
	
	public void readAttributes(Element ol, int POS_UNICO_ELEMENTO) {
		this.description = null;
		
		Element description = (Element) ol.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		try{
			this.description = description.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingProcess -> OpenTenderEvent -> OcurrenceLocation -> DESCRIPTION no existe\n");
		}
	}
	
	public void readAddress(Element ol, int POS_UNICO_ELEMENTO){
		this.address = null;
		
		Element ad = (Element) ol.getElementsByTagName("cac:Address").item(POS_UNICO_ELEMENTO);
		try{
			this.address = new Address();
			this.address.readAttributes(ad, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingProcess -> OpenTenderEvent -> OcurrenceLocation -> ADDRESS no existe\n");
		}
	}
	
	public void print(){
		System.out.print("**** OCURRENCE LOCATION ****\n" +
				 		 "----> Description: " + description + "\n");
						 address.print();
		System.out.print("--------------------------------\n");
	}
}
