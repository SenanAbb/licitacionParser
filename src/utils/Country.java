package utils;

import org.w3c.dom.Element;

/*
 * 		idenfiticationCode: String [1]
 * 		name: String [0..1]
 */
public class Country {
	private String identificationCode, name;
	
	/**
	 * Lee los atributos (las etiquitas <cbc:...>) del documento correspondiente a las variables de esta clase
	 * 
	 * @param country El <cac:Country> que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element country, int POS_UNICO_ELEMENTO) {
		this.identificationCode = null;
		this.name = null;
		
		Element idCode = (Element) country.getElementsByTagName("cbc:IdentificationCode").item(POS_UNICO_ELEMENTO);
		try{
			this.identificationCode = idCode.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ProcurementProject -> RealizedLocation -> Address -> Country -> IDENTIFICATION CODE no existe\n");
		}
		
		Element countryName = (Element) country.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if (countryName != null){
			this.name = countryName.getTextContent();
		}
	}

	public String getIdentificationCode() {
		return identificationCode;
	}
	
	public String getName() {
		return name;
	}
}
