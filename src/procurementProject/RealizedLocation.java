package procurementProject;

import utils.Address;

public class RealizedLocation {
	private String countrySubentity, countrySubentityCode;
	private Address address;
	// Atributos
	private String countrySubentityCodeName;
	
	public void print(){
		System.out.print("** REALIZED LOCATION **\n" +
			"--> Country Subentity: " + countrySubentity + "\n" +
			"--> Country Subentity Code: " + countrySubentityCode + "\n");
		if (address != null){
			address.print();
		}else{
			System.out.print("--> Address: null\n");
		}
		System.out.print("--------------------------------\n");
	}

	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	

	public RealizedLocation() {}


	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getCountrySubentity() {
		return countrySubentity;
	}
	
	public void setCountrySubentity(String countrySubentity) {
		this.countrySubentity = countrySubentity;
	}

	public String getCountrySubentityCode() {
		return countrySubentityCode;
	}

	public void setCountrySubentityCode(String countrySubentityCode) {
		this.countrySubentityCode = countrySubentityCode;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getCountrySubentityCodeName() {
		return countrySubentityCodeName;
	}

	public void setCountrySubentityCodeName(String countrySubentityCodeName) {
		this.countrySubentityCodeName = countrySubentityCodeName;
	}	
}
