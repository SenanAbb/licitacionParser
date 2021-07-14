package utils;

import org.w3c.dom.Element;

/*
 *		addressFormatCode: int [0..1]/[1]
 *		cityName: String [0..1]/[1]	
 *		postalZone: String [0..1]/[1]
 *		addressLine: AddressLine [0..1]/[1]
 *		country: Country [0..1]/[1]
 */
public class PostalAddress {
	private int addressFormatCode;
	private AddressLine addressLine;
	private String cityName, postalZone;
	private Country country;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pa El cac:PostalAddress que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 * @param obligatorio Dependiendo de la tabla, los campos serán obligatorios o no, esta variable define esta condición
	 */
	// Obligatorio nos dice si los campos AddressLine, CityName, PostalZone y Country debes ser o no obligatorios
	public void readAttributes(Element pa, int POS_UNICO_ELEMENTO, boolean obligatorio) {
		this.addressFormatCode = 0;
		this.cityName = null;
		this.postalZone = null;
		this.addressLine = null;
		this.country = null;
		
		/* Address Format Code */
		Element afc = (Element) pa.getElementsByTagName("cbc:AddressFormatCode").item(POS_UNICO_ELEMENTO);
		if (afc != null){
			this.addressFormatCode = Integer.parseInt(afc.getTextContent());
		}
		
		if(obligatorio){
			readAttributesObligatorio(pa, POS_UNICO_ELEMENTO);
		}else{
			readAttributesNoObligatorio(pa, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pa El cac:PostalAddress que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	private void readAttributesNoObligatorio(Element pa, int POS_UNICO_ELEMENTO){
		/* City Name */
		Element cn = (Element) pa.getElementsByTagName("cbc:CityName").item(POS_UNICO_ELEMENTO);
		if (cn != null){
			this.cityName = cn.getTextContent();
		}
		
		/* Postal Zone */
		Element pz = (Element) pa.getElementsByTagName("cbc:PostalZone").item(POS_UNICO_ELEMENTO);
		if (pz != null){
			this.postalZone = pz.getTextContent();
		}
		
		/* Address Line */
		Element al = (Element) pa.getElementsByTagName("cac:AddressLine").item(POS_UNICO_ELEMENTO);
		if (al != null){
			this.addressLine = new AddressLine();
			this.addressLine.readAttributes(al, POS_UNICO_ELEMENTO);
		}
		
		/* Country */
		Element country = (Element) pa.getElementsByTagName("cac:Country").item(POS_UNICO_ELEMENTO);
		if (country != null){
			this.country = new Country();
			this.country.readAttributes(country, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pa El cac:PostalAddress que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	private void readAttributesObligatorio(Element pa, int POS_UNICO_ELEMENTO){
		/* City Name */
		Element cn = (Element) pa.getElementsByTagName("cbc:CityName").item(POS_UNICO_ELEMENTO);
		try{
			this.cityName = cn.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Postal Address -> CITY NAME no existe\n");
		}
		
		/* Postal Zone */
		Element pz = (Element) pa.getElementsByTagName("cbc:PostalZone").item(POS_UNICO_ELEMENTO);
		try{
			this.postalZone = pz.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Postal Address -> POSTAL ZONE no existe\n");
		}
		
		/* Address Line */
		Element al = (Element) pa.getElementsByTagName("cac:AddressLine").item(POS_UNICO_ELEMENTO);
		try{
			this.addressLine = new AddressLine();
			this.addressLine.readAttributes(al, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Postal Address -> ADDRESS LINE no existe\n");
		}
		
		/* Country */
		Element country = (Element) pa.getElementsByTagName("cac:Country").item(POS_UNICO_ELEMENTO);
		try{
			this.country = new Country();
			this.country.readAttributes(country, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: Postal Address -> COUNTRY no existe\n");
		}
	}
	
	public AddressLine getAddressLine(){
		return addressLine;
	}

	public String getPostalZone(){
		return postalZone;
	}

	public Country getCountry(){
		return country;
	}
	
	public String getCityName(){
		return cityName;
	}
}
