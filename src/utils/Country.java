package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 * 		idenfiticationCode: String [1]
 * 		name: String [0..1]
 */
public class Country {
	private String identificationCode, name;
	
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
	
	public void print() {
		System.out.print("**** COUNTRY ****\n" +
				"----> Identification Code: " + identificationCode + "\n" +
				"----> Name: " + name + "\n");
	}

	public String getIdentificationCode() {
		return identificationCode;
	}
	
	public String getName() {
		return name;
	}
}
