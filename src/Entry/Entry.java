package Entry;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

import Conexion.ConexionSQL;
import ContractFolderStatus.ContractFolderStatus;

/**
 * @params
 *		id: String[1]
 *		link: String[1]
 *		summary: String[1]
 *		title: String[1]
 *		updated: Date[1]
 *		cfs: ContractFolderStatus[1]
 */
public class Entry {
	private String id, id_num, link, summary, title;
	private Timestamp updated;
	private ContractFolderStatus cfs;
	private int entrys;
	
	public void readContractFolderStatus(Element entry, int POS_UNICO_ELEMENTO){
		this.cfs = null;
		
		//Dentro del ENTRY, buscamos el <cac-place-ext:ContractFolderStatus>
		Element cfs = (Element) entry.getElementsByTagName("cac-place-ext:ContractFolderStatus").item(POS_UNICO_ELEMENTO);
		
		if (cfs == null){
			System.err.print("ERROR FATAL: CONTRACT FOLDER STATUS no existe\n");
			System.exit(0);
		}else{
			this.cfs = new ContractFolderStatus();
			
			this.cfs.readAttributes(cfs, POS_UNICO_ELEMENTO);
			
			this.cfs.readProcurementProject(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderResult(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readLocatedContractingParty(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderingTerms(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderingProcess(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readContractModification(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readLegalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTechnicalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readAdditionalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readValidNoticeInfo(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readGeneralDocument(cfs, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("---------------- ENTRY " + id_num + "----------------\n" + 
						 "Link: " + link + "\n" + 
						 "Summary: " + summary + "\n" + 
						 "Title: " + title + "\n" + 
						 "Updated: " + updated + "\n");
		System.out.print("===============================================================\n");
		cfs.print();
	}
	
	public void writeData(int ids) {
		ConexionSQL sql = new ConexionSQL();
		Connection conn = sql.conectarMySQL();
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newEntry(?, ?, ?, ?, ?, ?, ?)}");
			
			// INICIAMOS LA TRANSACCIÓN
			conn.setAutoCommit(false);
			
			// Parametros del procedimiento almacenado
			sentencia.setString("id", this.id);
			sentencia.setString("link", this.link);
			sentencia.setString("summary", this.summary);
			sentencia.setString("title", this.title);
			sentencia.setTimestamp("updated", this.updated);
			sentencia.setInt("ids", ids);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("entrys", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida (parametro nº 7)
			this.entrys = sentencia.getInt("entrys");
			
			// Mandamos a ContractFolderStatus a grabar datos
			this.cfs.writeData(entrys, conn);
			
			// COMMIT DE LAS INSTRUCCIONES
			conn.commit(); 
		
		} catch (SQLException e){
			System.out.println("[ENTRY] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[ENTRY] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public Entry(String id, String id_num, String link, String summary, String title, Timestamp updated) {
		this.id = id;
		this.id_num = id_num;
		this.link = link;
		this.summary = summary;
		this.title = title;
		this.updated = updated;
	}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/


	public String getId() {
		return id_num;
	}

	public Timestamp getUpdated() {
		return updated;
	}

	
}


