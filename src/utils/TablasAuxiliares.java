package utils;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.cj.jdbc.CallableStatement;

public class TablasAuxiliares {

	public static void writeTBLParentlocatedpartyPartyname(int parent_located_party, int party_name, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newParentlocatedpartyPartyName(?, ?)}");
			sentencia.setInt("parent_located_party", parent_located_party);
			sentencia.setInt("party_name", party_name);
			sentencia.execute();
			
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeTBLPartyPartyname(int party, int party_name,Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyPartyname(?, ?)}");
			sentencia.setInt("party", party);
			sentencia.setInt("party_name", party_name);
			sentencia.execute();
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeDataTBLPartyPartyidentification(int party, int party_identification, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyPartyidentification(?, ?)}");
			sentencia.setInt("party", party);
			sentencia.setInt("party_identification", party_identification);
			sentencia.execute();
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeDataTBLPartyContact(int party, int contact, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyContact(?, ?)}");
			sentencia.setInt("party", party);
			sentencia.setInt("contact", contact);
			sentencia.execute();
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeDataTBLPartyPostaladdress(int party, int postal_address, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPartyPostaladdress(?, ?)}");
			sentencia.setInt("party", party);
			sentencia.setInt("postal_address", postal_address);
			sentencia.execute();
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

	public static void writeDataTBLPostaladdressCountry(int postal_address, int country, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newPostaladdressCountry(?, ?)}");
			sentencia.setInt("country", country);
			sentencia.setInt("postal_address", postal_address);
			sentencia.execute();
		} catch (SQLException e){
			System.out.println("[CFS] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[CFS] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}

}
