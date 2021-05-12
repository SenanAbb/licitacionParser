package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import procurementProject.RequiredCommodityClassification;
import tenderingTerms.Language;
import Entry.Entry;

import com.mysql.cj.jdbc.CallableStatement;

import documents.AdditionalDocumentReference;

public class ConexionSQL {
	private static final int PLIEGO_ADMINISTRATIVO = 1;
	private static final int PLIEGO_TECNICO = 2;
	private static final int PLIEGO_ADICIONAL = 3;
	
    private String driver = "com.mysql.jdbc.Driver"; // Librería de MySQL
    private String database = "licitacion"; // Nombre de la base de datos  
    private String hostname = "localhost"; // Host   
    private String port = "3306"; // Puerto
    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";    
    private String username = "root"; // Nombre de usuario    
    private String password = "root"; // Clave de usuario
    
    public Connection conectarMySQL(){
    	Connection conn = null;
    	
    	try{
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url, username, password);
    	} catch (ClassNotFoundException | SQLException e){
    		e.printStackTrace();
    	}
    	
    	return conn;
    }

    /* TYPE CODES */
    
	public void writeSubTypeCode(int code, String nombre, int tipo) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newSubtypeCode(?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre.trim());
			sentencia.setInt("type_code", tipo);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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

	public void writeCPVCode(int code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newCPV(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre.replaceAll("\n", ""));
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeCountryIdentificationCode(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newCountryIdentificationCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeCountrySubentityCode(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newCountrySubentityCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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

	public void writeProcedureCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newProcedureCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeContractingSystemTypeCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newContractingSystemTypeCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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

	public void writeUrgencyCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newUrgencyCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeSubmissionMethodCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newSubmissionMethodCode(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeLanguage(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newLanguage(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	public void writeProcurementLegislation(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newProcurementLegislation(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("code", code);
			sentencia.setString("nombre", nombre);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
		} catch (SQLException e){
			e.printStackTrace();
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
	
	/* TABLAS GENERALES */
	
	public void writeExpediente(Entry entry, int ids) {
		boolean existe = searchExpediente(Integer.parseInt(entry.getId()));
		if(existe){
			writeNewIdsExpediente(entry, ids);
		}else{
			writeNewExpediente(entry);
			writeNewIdsExpediente(entry, ids);
		}
	}

	private void writeNewExpediente(Entry entry){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newExpediente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
			sentencia.setString("title", entry.getTitle());
			sentencia.setString("link", entry.getLink());
			sentencia.setString("objeto_contrato", entry.getContractFolderStatus().getProcurementProject().getName());
			sentencia.setDouble("valor_estimado", entry.getContractFolderStatus().getProcurementProject().getBudgetAmount().getEstimatedOverallContractAmount());
			sentencia.setDouble("presupuesto_sin_impuestos", entry.getContractFolderStatus().getProcurementProject().getBudgetAmount().getTaxExclusiveAmount());
			sentencia.setDouble("presupuesto_con_impuestos", entry.getContractFolderStatus().getProcurementProject().getBudgetAmount().getTotalAmount());
			
			java.sql.Date start_date = entry.getContractFolderStatus().getProcurementProject().getPlannedPeriod().getStartDate();
			java.sql.Date end_date = entry.getContractFolderStatus().getProcurementProject().getPlannedPeriod().getEndDate();
			double duration = entry.getContractFolderStatus().getProcurementProject().getPlannedPeriod().getDurationMeasure();
			String unit_code = entry.getContractFolderStatus().getProcurementProject().getPlannedPeriod().getUnitCode();
			
			if (start_date != null && end_date != null){
				sentencia.setDate("start_date", start_date);
				sentencia.setDate("end_date", end_date);
				sentencia.setDouble("duracion", duration);
				sentencia.setString("unitcode", null);
			} else if (start_date != null && duration != -1){
				sentencia.setDate("start_date", start_date);
				sentencia.setDate("end_date", null);
				sentencia.setDouble("duracion", duration);
				sentencia.setString("unitcode", unit_code);
			} else {
				sentencia.setDate("start_date", null);
				sentencia.setDate("end_date", null);
				sentencia.setDouble("duracion", duration);
				sentencia.setString("unitcode", unit_code);
			}
			
			sentencia.setString("unitcode", entry.getContractFolderStatus().getProcurementProject().getPlannedPeriod().getUnitCode());
			sentencia.setInt("typecode", entry.getContractFolderStatus().getProcurementProject().getTypeCode());
			sentencia.setInt("subtypecode", entry.getContractFolderStatus().getProcurementProject().getSubTypeCode());
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// WRITE CPV
			if (entry.getContractFolderStatus().getProcurementProject().getRequiredCommodityClassificationList() != null){
				for (RequiredCommodityClassification r : entry.getContractFolderStatus().getProcurementProject().getRequiredCommodityClassificationList()){
					sentencia = (CallableStatement) conn.prepareCall("{call newExpediente_CPV(?, ?)}");
					
					// Parametros
					sentencia.setInt("code", r.getItemClassificationCode());
					sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
					
					// Ejecutamos
					sentencia.execute();
					sentencia.close();
				}
			}
			
			// WRITE PLIEGOS
			// Administrativas (LegalDocumentReference)
			if (entry.getContractFolderStatus().getLegalDocumentReference() != null){
				sentencia = (CallableStatement) conn.prepareCall("{call newPliego(?, ?, ?, ?, ?, ?)}");
				
				sentencia.setString("id", entry.getContractFolderStatus().getLegalDocumentReference().getId());
				sentencia.setString("uri", entry.getContractFolderStatus().getLegalDocumentReference().getAttachment().getExternalReference().getURI());
				sentencia.setString("document_hash", entry.getContractFolderStatus().getLegalDocumentReference().getAttachment().getExternalReference().getDocumentHash());
				sentencia.setString("file_name", entry.getContractFolderStatus().getLegalDocumentReference().getAttachment().getExternalReference().getFileName());
				sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
				sentencia.setInt("tipo_pliego", PLIEGO_ADMINISTRATIVO);
				
				sentencia.execute();
			}
			
			
			// Técnicas (TechnicalDocumentReference)
			if (entry.getContractFolderStatus().getTechnicalDocumentReference() != null){
				sentencia = (CallableStatement) conn.prepareCall("{call newPliego(?, ?, ?, ?, ?, ?)}");
				
				sentencia.setString("id", entry.getContractFolderStatus().getTechnicalDocumentReference().getId());
				sentencia.setString("uri", entry.getContractFolderStatus().getTechnicalDocumentReference().getAttachment().getExternalReference().getURI());
				sentencia.setString("document_hash", entry.getContractFolderStatus().getTechnicalDocumentReference().getAttachment().getExternalReference().getDocumentHash());
				sentencia.setString("file_name", entry.getContractFolderStatus().getTechnicalDocumentReference().getAttachment().getExternalReference().getFileName());
				sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
				sentencia.setInt("tipo_pliego", PLIEGO_TECNICO);
				
				sentencia.execute();
			}
			
			// Adicionales (AditionalDocumentReference)
			if (entry.getContractFolderStatus().getAdditionalDocumentReferenceList() != null){
				for (AdditionalDocumentReference a : entry.getContractFolderStatus().getAdditionalDocumentReferenceList()){
					sentencia = (CallableStatement) conn.prepareCall("{call newPliego(?, ?, ?, ?, ?, ?)}");
					
					sentencia.setString("id", a.getId());
					sentencia.setString("uri", a.getAttachment().getExternalReference().getURI());
					sentencia.setString("document_hash", a.getAttachment().getExternalReference().getDocumentHash());
					sentencia.setString("file_name", a.getAttachment().getExternalReference().getFileName());
					sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
					sentencia.setInt("tipo_pliego", PLIEGO_ADICIONAL);
					
					sentencia.execute();
				}
			}
		} catch (SQLException e){
			System.out.println(entry.getId() + " " + entry.getContractFolderStatus().getProcurementProject().getTypeCode()
					+ " " + entry.getContractFolderStatus().getProcurementProject().getSubTypeCode());
			e.printStackTrace();
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

	private void writeNewIdsExpediente(Entry entry, int ids){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newExpediente_Ids(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("ids", ids);
			sentencia.setInt("expediente", Integer.parseInt(entry.getId()));
			sentencia.setString("summary", entry.getSummary());
			sentencia.setTimestamp("updated", entry.getUpdated());
			sentencia.setString("estado", entry.getContractFolderStatus().getContractFolderStatusCode());
			
			sentencia.execute();
		} catch (SQLException e){
			e.printStackTrace();
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

	public void writeLugarDeEjecucion(Entry entry){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newLugarDeEjecucion(?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
			sentencia.setString("country_subentity_code", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getCountrySubentityCode());
			
			if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress() != null){
				sentencia.setString("country_identification_code", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCountry().getIdentificationCode());
				sentencia.setString("pais", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCountry().getName());
				
				if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getAddressLine() != null){
					sentencia.setString("calle", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getAddressLine().getLine());
				}else{
					sentencia.setString("calle", null);
				}
				
				sentencia.setString("codigo_postal", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getPostalZone());
				sentencia.setString("poblacion", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCityName());
			}else{
				sentencia.setString("country_identification_code", null);	
				sentencia.setString("pais", null);
				sentencia.setString("calle", null);
				sentencia.setString("codigo_postal", null);
				sentencia.setString("poblacion", null);
			}
			
			
			// Ejecucion
			sentencia.execute();
			
		} catch (SQLException | NullPointerException e){
			System.out.println(entry.getId() + " ");
			e.printStackTrace();
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
	
	public void writeProcesoDeLicitacion(Entry entry){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newProcesoDeLicitacion(?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
			sentencia.setInt("procedure_code", entry.getContractFolderStatus().getTenderingProcess().getProcedureCode());
			
			if (entry.getContractFolderStatus().getTenderingProcess().getContractingSystemTypeCode() > 0){
				sentencia.setInt("contracting_system_type_code", entry.getContractFolderStatus().getTenderingProcess().getContractingSystemTypeCode());
			}else{
				sentencia.setNull("contracting_system_type_code", java.sql.Types.NULL);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getUrgencyCode() > 0){
				sentencia.setInt("urgency_code", entry.getContractFolderStatus().getTenderingProcess().getUrgencyCode());	
			}else{
				sentencia.setNull("urgency_code", java.sql.Types.NULL);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getSubmissionMethodCode() > 0){
				sentencia.setInt("submission_method_code", entry.getContractFolderStatus().getTenderingProcess().getSubmissionMethodCode());	
			}else{
				sentencia.setNull("submission_method_code", java.sql.Types.NULL);
			}
			
			if (entry.getContractFolderStatus().getTenderingTerms().getProcurementLegislationDocumenteReference() != null){
				sentencia.setString("procurement_legislation", entry.getContractFolderStatus().getTenderingTerms().getProcurementLegislationDocumenteReference().getId());
			}else{
				sentencia.setString("procurement_legislation", "N/A");
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getAuctionTerms() != null){
				sentencia.setBoolean("subasta_electronica", entry.getContractFolderStatus().getTenderingProcess().getAuctionTerms().getAuctionConstraintIndicator());
			}else{
				sentencia.setNull("subasta_electronica", java.sql.Types.NULL);
			}
			
			// Ejecucion
			sentencia.execute();
			
			int procesoId = sentencia.getInt("proceso_de_licitacion");
			
			// LANGUAGES
			Language[] languageList = entry.getContractFolderStatus().getTenderingTerms().getLanguageList();
			if (languageList.length > 0){
				for (int i = 0; i < languageList.length; i++){
					sentencia = (CallableStatement) conn.prepareCall("{call newProcesoDeLicitacion_Idioma(?, ?)}");
					
					// Parametros
					sentencia.setInt("proceso_de_licitacion", procesoId);
					sentencia.setString("idioma", languageList[i].getId());
					
					// Ejecucion
					sentencia.execute();
				}
			}
		} catch (SQLException | NullPointerException e){
			System.out.println(entry.getId() + " ");
			e.printStackTrace();
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
	
	/* AUXILIARES */
	
	private boolean searchExpediente(int id) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		boolean existe = false;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call searchExpediente(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("id", id);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("existe", java.sql.Types.BOOLEAN);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida (parametro nº 4)
			existe = sentencia.getBoolean("existe");
			
		} catch (SQLException e){
			e.printStackTrace();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return existe;
	}
}
