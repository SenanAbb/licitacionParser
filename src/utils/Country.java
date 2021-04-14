package utils;

public class Country {
	private String identificationCode, name;
	// Atributos
	private String identificationCodeLanguageID, identificationCodeName;

	public void print() {
		System.out.print("**** COUNTRY ****\n" +
				"----> Identification Code: " + identificationCode + "\n" +
				"----> Name: " + name + "\n");
	}
	
	
	/******************/
	/** CONTRUCTORES **/
	/******************/
	
	
	public Country(String identificationCode) {
		super();
		this.identificationCode = identificationCode;
	}

	
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
