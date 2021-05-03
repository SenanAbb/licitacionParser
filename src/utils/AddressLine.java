package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 *		line: String [1]
 */
public class AddressLine {
	private String line;

	public void readAttributes(Element al, int POS_UNICO_ELEMENTO) {
		Element addressLine = (Element) al.getElementsByTagName("cbc:Line").item(POS_UNICO_ELEMENTO);
		if (addressLine != null){
			this.line = addressLine.getTextContent();
		}else{
			System.err.print("ERROR FATAL: Postal Address -> AddressLine -> LINE no existe\n");
		}
	}
	
	public void print(){
		System.out.print("**** ADDRESS LINE ****\n" +
						 "----> Address Line: " + line + "\n");
	}
	
	
	public AddressLine(){}

	public void writeData(int postal_address, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newAddressLine(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("line", this.line);
			sentencia.setInt("postal_address", postal_address);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
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
