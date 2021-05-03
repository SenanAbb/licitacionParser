package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 *		addressFormatCode: int [0..1]/[1]
 *		cityName: String [0..1]/[1]	
 *		postalZone: String [0..1]/[1]
 *		addressLine: AddressLine [0..1]/[1]
 *		country: Country [0..1]/[1]
 */
public class PostalAddress {
	private static final int NULL_FORMAT = 0;
	
	private int addressFormatCode;
	private AddressLine addressLine;
	private String cityName, postalZone;
	private Country country;
	
	//ID
	private int postal_address;
	
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
	
	public void print() {
		System.out.print("**** POSTAL ADDRESS ****\n" +
				"----> Address Format Code: " + addressFormatCode + "\n" +
				"----> City Name: " + cityName + "\n" +
				"----> Postal Zone: " + postalZone + "\n");
				addressLine.print();
				country.print();
		System.out.print("--------------------------------\n");
	}

	public void writeDataTBLPartyPostalAddress(int party, Connection conn) {
		writeData(conn);
		TablasAuxiliares.writeDataTBLPartyPostaladdress(party, postal_address, conn);
		
		// Subclases
		if (addressLine != null){
			addressLine.writeData(postal_address, conn);
		}
		
		if (country != null){
			country.writeData(postal_address, conn);
		}
	}

	private void writeData(Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPostaladdress(?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			if(addressFormatCode == -1){
				sentencia.setInt("addressformatcode", NULL_FORMAT);
			}else{
				sentencia.setInt("addressformatcode", this.addressFormatCode);
			}
			sentencia.setString("cityname", this.cityName);
			sentencia.setString("postalzone", this.postalZone);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("postal_address", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			this.postal_address = sentencia.getInt("postal_address");	
		} catch (SQLException e){
			System.out.println("[Party] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[Party] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}
}
