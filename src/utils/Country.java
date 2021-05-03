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

	//ID
	private int country;
	
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

	public void writeData(int postal_address, Connection conn) {
		writeData(conn);
		TablasAuxiliares.writeDataTBLPostaladdressCountry(postal_address, country, conn);
	}

	private void writeData(Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newCountry(?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("identification_code", this.identificationCode);
			sentencia.setString("name", this.name);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("country", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			this.country = sentencia.getInt("country");	
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
