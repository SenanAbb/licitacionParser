package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 *		name: String[1]
 */
public class PartyName {
	private String name;
	
	// ID de la BD
	private int party_name;
	
	public void readAttributes(Element pn, int POS_UNICO_ELEMENTO){
		this.name = null;
		Element name = (Element) pn.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		
		try{
			this.name = name.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: PartyName -> NAME no existe\n");
		}
	}
	
	public void print(){
		System.out.print("**** PARTY NAME ****\n" +
						 "----> Party Name: " + name + "\n" +
						 "--------------------------------\n");
	}

	public void writeDataTBLParentlocatedpartyPartyname(int parent_located_party, Connection conn){
		writeData(conn);
		TablasAuxiliares.writeTBLParentlocatedpartyPartyname(parent_located_party, party_name, conn);
	}
	
	public void writeDataTBLPartyPartyname(int party, Connection conn) {
		writeData(conn);
		TablasAuxiliares.writeTBLPartyPartyname(party, party_name, conn);
	}
	
	private void writeData(Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyName(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("name", this.name);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("party_name", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida (parametro nº 4)
			this.party_name = sentencia.getInt("party_name");
			
		} catch (SQLException e){
			System.out.println("[PartyName] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[PartyName] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	
}
