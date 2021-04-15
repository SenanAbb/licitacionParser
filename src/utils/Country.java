package utils;

import org.w3c.dom.Element;

public class Country {
	private String identificationCode, name;
	// Atributos
	private String identificationCodeLanguageID, identificationCodeName;

	public void readAttributes(Element country, int POS_UNICO_ELEMENTO) {
		this.identificationCode = null;
		this.name = null;
		this.identificationCodeLanguageID = null;
		this.identificationCodeName = null;
		
		Element idCode = (Element) country.getElementsByTagName("cbc:IdentificationCode").item(POS_UNICO_ELEMENTO);
		if (idCode != null){
			this.identificationCode = idCode.getTextContent();
			
			Element countryName = (Element) country.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
			if (countryName != null){
				this.name = countryName.getTextContent();
			}	
		}else{
			System.err.print("ERROR FATAL: ProcurementProject -> RealizedLocation -> Address -> Country -> IDENTIFICATION CODE no existe\n");
		}
	}
	
	public void print() {
		System.out.print("**** COUNTRY ****\n" +
				"----> Identification Code: " + identificationCode + "\n" +
				"----> Name: " + name + "\n");
	}
	
	
	/******************/
	/** CONTRUCTORES **/
	/******************/
	
	
	public Country() {}

	
	/***********************/
	/** GETTERS Y SETTERS **/
	/***********************/
	
	
	public String getIdentificationCode() {
		return identificationCode;
	}

	public void setIdentificationCode(String identificationCode) {
		this.identificationCode = identificationCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdentificationCodeLanguageID() {
		return identificationCodeLanguageID;
	}

	public void setIdentificationCodeLanguageID(String identificationCodeLanguageID) {
		this.identificationCodeLanguageID = identificationCodeLanguageID;
	}

	public String getIdentificationCodeName() {
		return identificationCodeName;
	}

	public void setIdentificationCodeName(String identificationCodeName) {
		this.identificationCodeName = identificationCodeName;
	}
}
