package procurementProject;

import org.w3c.dom.Element;

import utils.Address;

/**
 * @params
 *		countrySubentity: String [0..1]
 *		countrySubentityCode: String [0..1]
 *		address: Address [0..1]
 */
public class RealizedLocation {
	private String countrySubentity, countrySubentityCode;
	private Address address;
	
	public void readAttributes(Element rl, int POS_UNICO_ELEMENTO) {
		this.countrySubentity = null;
		this.countrySubentityCode = null;
		this.address = null;
		
		Element countrySubentity = (Element) rl.getElementsByTagName("cbc:CountrySubentity").item(POS_UNICO_ELEMENTO);
		if (countrySubentity != null){
			this.countrySubentity = countrySubentity.getTextContent();
		}
		
		Element countrySubentityCode = (Element) rl.getElementsByTagName("cbc:CountrySubentityCode").item(POS_UNICO_ELEMENTO);
		if (countrySubentityCode != null){
			this.countrySubentityCode = countrySubentityCode.getTextContent();
		}
		
		// ADDRESS
		Element address = (Element) rl.getElementsByTagName("cac:Address").item(POS_UNICO_ELEMENTO);
		if (address != null){
			this.address = new Address();
			this.address.readAttributes(address, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("*** REALIZED LOCATION ***\n" +
			"---> Country Subentity: " + countrySubentity + "\n" +
			"---> Country Subentity Code: " + countrySubentityCode + "\n");
		if (address != null){
			address.print();
		}else{
			System.out.print("----> Address: null\n");
		}
		System.out.print("--------------------------------\n");
	}	
}
