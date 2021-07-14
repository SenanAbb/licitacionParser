package procurementProject;

import org.w3c.dom.Element;

import utils.Address;

/*
 *		countrySubentity: String [0..1]
 *		countrySubentityCode: String [0..1]
 *		address: Address [0..1]
 */
public class RealizedLocation {
	private String countrySubentity, countrySubentityCode;
	private Address address;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param rl El cac:RealizedLocation que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
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
	
	public Address getAddress(){
		return address;
	}
	
	public String getCountrySubentityCode() {
		return countrySubentityCode;
	}

	public String getCountrySubentity() {
		return countrySubentity;
	}
}
