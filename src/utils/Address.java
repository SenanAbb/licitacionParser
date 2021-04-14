package utils;

public class Address {
	private int addressFormatCode;
	private String cityName, postalZone, addressLine;
	private Country country;
	
	public void print() {
		System.out.print("*** ADDRESS ***\n" +
				"---> Address Format Code: " + addressFormatCode + "\n" +
				"---> City Name: " + cityName + "\n" +
				"---> Postal Zone: " + postalZone + "\n" +
				"---> AddressLine: " + addressLine + "\n");
				country.print();
	}
	
	/******************/
	/** CONTRUCTORES **/
	/******************/
	
	
	public Address(){}


	/***********************/
	/** GETTERS Y SETTERS **/
	/***********************/
	
	
	public int getAddressFormatCode() {
		return addressFormatCode;
	}


	public void setAddressFormatCode(int addressFormatCode) {
		this.addressFormatCode = addressFormatCode;
	}


	public String getCityName() {
		return cityName;
	}


	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	public String getPostalZone() {
		return postalZone;
	}


	public void setPostalZone(String postalZone) {
		this.postalZone = postalZone;
	}


	public String getAddressLine() {
		return addressLine;
	}


	public void setAddressLine(String addressLine) {
		this.addressLine = addressLine;
	}


	public Country getCountry() {
		return country;
	}


	public void setCountry(Country country) {
		this.country = country;
	}
}
