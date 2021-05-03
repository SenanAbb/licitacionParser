package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 *		id: String [1]
 *		schemeName: String[1]
 */
public class PartyIdentification {
	private String id, schemeName;
	
	//ID
	private int party_identification;

	public void readAttributes(Element pi, int POS_UNICO_ELEMENTO) {
		this.id = null;
		this.schemeName = null;
		
		Element id = (Element) pi.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
			this.schemeName = id.getAttributes().getNamedItem("schemeName").getTextContent();
		}else{
			System.err.print("ERROR FATAL: PartyIdentification -> ID no existe\n");
		}
	}
	
	public void print(){
		System.out.print("----> Party Identification: " + id + " " + "(" + schemeName + ")" + "\n");
	}

	public void writeDataTBLPartyPartyidentification(int party, Connection conn) {
		writeData(conn);
		TablasAuxiliares.writeDataTBLPartyPartyidentification(party, party_identification, conn);
	}

	private void writeData(Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyIdentification(?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("id", this.id);
			sentencia.setString("schemeName", this.schemeName);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("party_identification", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida (parametro nº 4)
			this.party_identification = sentencia.getInt("party_identification");
			
		} catch (SQLException e){
			System.out.println("[PartyIdentification] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[PartyIdentification] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}
}
