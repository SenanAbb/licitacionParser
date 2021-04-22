package utils;

import org.w3c.dom.Element;

/**
 * @params
 * 		addressFormatCode: int [0..1]
 * 		cityName: String [0..1]
 * 		postalZone: String [0..1]
 * 		addressLine: String [0..1]
 * 		country: Country [0..1]
 */
public class Address {
	private int addressFormatCode;
	private String cityName, postalZone, addressLine;
	private Country country;
	
	public void readAttributes(Element address, int POS_UNICO_ELEMENTO) {
		this.addressFormatCode = -1;
		this.cityName = null;
		this.postalZone = null;
		this.addressLine = null;
		this.country = null;
		
		// ADDRESS FORMATCODE
		Element afc = (Element) address.getElementsByTagName("cbc:AddressFormatCode").item(POS_UNICO_ELEMENTO);
		if (afc != null){
			this.addressFormatCode = Integer.parseInt(afc.getTextContent());
		}
		
		// CITY NAME
		Element cn = (Element) address.getElementsByTagName("cbc:CityName").item(POS_UNICO_ELEMENTO);
		if (cn != null){
			this.cityName = cn.getTextContent();
		}
		
		// POSTAL ZONE
		Element pz = (Element) address.getElementsByTagName("cbc:PostalZone").item(POS_UNICO_ELEMENTO);
		if (pz != null){
			this.postalZone = pz.getTextContent();
		}
		
		// ADDRESS LINE
		Element al = (Element) address.getElementsByTagName("cbc:AdressLine").item(POS_UNICO_ELEMENTO);
		if (al != null){
			this.addressLine = al.getTextContent();
		}
		
		// COUNTRY
		Element country = (Element) address.getElementsByTagName("cac:Country").item(POS_UNICO_ELEMENTO);
		if (country != null){
			this.country = new Country();
			this.country.readAttributes(country, POS_UNICO_ELEMENTO);
		}
		
	}
	
	public void print() {
		System.out.print("**** ADDRESS ****\n" +
				"----> Address Format Code: " + addressFormatCode + "\n" +
				"----> City Name: " + cityName + "\n" +
				"----> Postal Zone: " + postalZone + "\n" +
				"----> AddressLine: " + addressLine + "\n");
				country.print();
	}
}
