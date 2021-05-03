package locatedContractingParty;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

import utils.PartyName;

/**
 * @params
 *		partyName: PartyName[0..1]
 */
public class ParentLocatedParty {
	private PartyName partyName;
	
	// ID de la BD
	private int parent_located_party;
	
	public void readAttributes(Element plp, int POS_UNICO_ELEMENTO){
		this.partyName = null;
		
		Element pn = (Element) plp.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		if (pn != null){
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("*** PARENT LOCATED PARTY ***\n");
		partyName.print();
		System.out.print("--------------------------------\n");
	}

	public void writeData(int located_contracting_party, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newParentLocatedParty(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("contractingPartyTypeCode", located_contracting_party);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("parent_located_party", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida (parametro nº 4)
			this.parent_located_party = sentencia.getInt("parent_located_party");
			
			// Graban las subclases
			if (this.partyName != null){
				this.partyName.writeDataTBLParentlocatedpartyPartyname(parent_located_party, conn);
			}
		} catch (SQLException e){
			System.out.println("[ParentLocatedParty] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[ParentLocatedParty] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}
}
