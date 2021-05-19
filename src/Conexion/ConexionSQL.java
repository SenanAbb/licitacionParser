package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import procurementProject.RequiredCommodityClassification;
import tenderResult.Contract;
import tenderResult.TenderResult;
import tenderingTerms.AwardingCriteria;
import tenderingTerms.ClassificationCategory;
import tenderingTerms.FinancialEvaluationCriteria;
import tenderingTerms.Language;
import tenderingTerms.RequiredFinancialGuarantee;
import tenderingTerms.SpecificTendererRequirement;
import tenderingTerms.TechnicalEvaluationCriteria;
import utils.PartyIdentification;
import Entry.Entry;

import com.mysql.cj.jdbc.CallableStatement;

import documents.AdditionalDocumentReference;

public class ConexionSQL {
	private static final int PLIEGO_ADMINISTRATIVO = 1;
	private static final int PLIEGO_TECNICO = 2;
	private static final int PLIEGO_ADICIONAL = 3;
	
	private static final String UBICACION_ORGANICA = "DIR3";
	
	private static final int PLAZO_PLIEGOS = 1;
	private static final int PLAZO_OFERTA = 2;
	private static final int PLAZO_SOLICITUDES = 3;
	
	private static final int EVALUACION_TECNICA = 1;
	private static final int EVALUACION_ECONOMICA_FINANCIERA = 2;
	
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
	
	public void writeContractingPartyTypeCode(String code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newContractingPartyTypeCode(?, ?)}");
			
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

	public void writeModosId(int code, String descripcion){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newModosId(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("modosId", code);
			sentencia.setString("description", descripcion);
			
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
	
	public void writeContractFolderStatusCode(String code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newContractFolderStatusCode(?, ?)}");
			
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
	
	public void writeTypèCode(int code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTypeCode(?, ?)}");
			
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
	
	public void writeTipoPliego(int id, String tipo){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTipoPliego(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("id", id);
			sentencia.setString("tipo", tipo);
			
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
	
	public void writeTipoPlazo(int id, String tipo){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTipoPlazo(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("tipo_plazo", id);
			sentencia.setString("descripcion", tipo);
			
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
	
	public void writeFundingProgramCode(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newFundingProgramCode(?, ?)}");
			
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
	
	public void writeGuarateeTypeCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newGuaranteeTypeCode(?, ?)}");
			
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
	
	public void writeRequiredBusinessProfileCode(String code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newRequiredBusinessProfileCode(?, ?)}");
			
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
	
	public void writeDeclarationTypeCode(int code, String nombre){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newDeclarationTypeCode(?, ?)}");
			
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
	
	public void writeTechnicalCapabilityTypeCode(String code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTechnicalCapabilityTypeCode(?, ?)}");
			
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
	
	public void writeFinancialCapabilityTypeCode(String code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newFinancialCapabilityTypeCode(?, ?)}");
			
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
	
	public void writeTipoEvaluacion(int tipo, String descripcion){
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTipoEvaluacion(?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("tipo_evaluacion", tipo);
			sentencia.setString("descripcion", descripcion);
			
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
	
	public void writeTenderResultCode(int code, String nombre) {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newTenderResultCode(?, ?)}");
			
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
	
	/* TABLAS GENERALES */
	
	public void writeExpediente(Entry entry, int ids) throws SQLException {
		boolean existe = searchExpediente(Integer.parseInt(entry.getId()));
		if(existe){
			writeNewIdsExpediente(entry, ids);
		}else{
			writeNewExpediente(entry);
			writeNewIdsExpediente(entry, ids);
		}
	}

	private void writeNewExpediente(Entry entry) throws SQLException{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			conn.setAutoCommit(false);
			
			sentencia = (CallableStatement) conn.prepareCall("{call newExpediente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
			sentencia.setString("numero_expediente", entry.getContractFolderStatus().getContractFolderID());
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
			
			conn.commit();
		} catch (SQLException e){
			System.out.println(entry.getId() + " " + entry.getContractFolderStatus().getProcurementProject().getTypeCode()
					+ " " + entry.getContractFolderStatus().getProcurementProject().getSubTypeCode());
			e.printStackTrace();
			if (conn != null) conn.rollback();
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

	private void writeNewIdsExpediente(Entry entry, int ids) throws SQLException{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newExpediente_Ids(?, ?, ?, ?, ?, ?)}");
			
			sentencia.setInt("ids", ids);
			sentencia.setInt("expediente", Integer.parseInt(entry.getId()));
			sentencia.setString("summary", entry.getSummary());
			sentencia.setTimestamp("updated", entry.getUpdated());
			sentencia.setString("estado", entry.getContractFolderStatus().getContractFolderStatusCode());
			
			sentencia.execute();
			
			// HAY QUE CONTROLAR QUE DESPUES DE CREAR EL IDS_EXPEDIENTE, SI FALLA ALGO, MARCAR ESE IDS COMO ERROR
			
			// INICIAMOS LA TRANSACCION
			// En caso de haber algun fallo con la base de datos, esta entry se descartará
			conn.setAutoCommit(false);
			
			int ids_expediente = sentencia.getInt("ids_expediente");
			
			writeLugarDeEjecucion(ids_expediente, entry, conn);
			writeProcesoDeLicitacion(ids_expediente, entry, conn);
			writeEntidadAdjudicadora(ids_expediente, entry, conn);
			writePlazoDeObtencion(ids_expediente, entry, conn);
			writeExtensionDeContrato(ids_expediente, entry, conn);
			writeCondicionesDeLicitacion(ids_expediente, entry, conn);
			writeGarantias(ids_expediente, entry, conn);
			writeRequisitosDeParticipacion(ids_expediente, entry, conn);
			writeCriterioDeEvaluacion(ids_expediente, entry, conn);
			writeSubcontratacionPermitida(ids_expediente, entry, conn);
			writeCriterioDeAdjudicacion(ids_expediente, entry, conn);
			writeResultadoDelProcedimiento(ids_expediente, entry, conn);
			
			// FINALIZAMOS LA TRANSACCION
			conn.commit();
		} catch (SQLException e){
			e.printStackTrace();
			if (conn != null) conn.rollback();
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

	private void writeLugarDeEjecucion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newLugarDeEjecucion(?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("ids_expedientes", ids);
			sentencia.setString("subentidad_territorial", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getCountrySubentityCode());
			
			if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress() != null){
				sentencia.setString("subentidad_nacional", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCountry().getIdentificationCode());
				sentencia.setString("pais", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCountry().getName());
				
				if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getAddressLine() != null){
					sentencia.setString("calle", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getAddressLine().getLine());
				}else{
					sentencia.setString("calle", null);
				}
				
				sentencia.setString("codigo_postal", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getPostalZone());
				sentencia.setString("poblacion", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress().getCityName());
			}else{
				sentencia.setString("subentidad_nacional", null);	
				sentencia.setString("pais", null);
				sentencia.setString("calle", null);
				sentencia.setString("codigo_postal", null);
				sentencia.setString("poblacion", null);
			}
			
			// Ejecucion
			sentencia.execute();
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeProcesoDeLicitacion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newProcesoDeLicitacion(?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("ids_expedientes", ids);
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
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newProcesoDeLicitacion_Idioma(?, ?)}");
					
					// Parametros
					sentencia.setInt("proceso_de_licitacion", procesoId);
					sentencia.setString("idioma", languageList[i].getId());
					
					// Ejecucion
					sentencia.execute();
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeEntidadAdjudicadora(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newEntidadAdjudicadora(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("ids_expedientes", ids);
			
			// ubicacion_organica
			boolean encontrado = false;
			PartyIdentification[] pi = entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPartyIdentificationList();
			
			for (int i = 0; i < pi.length; i++){
				if (pi[i].getSchemeName().compareTo(UBICACION_ORGANICA) == 0){
					encontrado = true;
					sentencia.setString("ubicacion_organica", pi[i].getId());
				// Parámetro NIF
				}else if (pi[i].getSchemeName().compareTo("NIF") == 0){
					sentencia.setString("NIF", pi[i].getId());
				}
			}
			
			if (!encontrado){
				sentencia.setString("ubicacion_organica", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPartyName().getName());
			}
			
			// nombre
			sentencia.setString("nombre", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPartyName().getName());
			
			// tipo_administracion
			sentencia.setInt("tipo_administracion", entry.getContractFolderStatus().getLocatedContractingParty().getContractingPartyTypeCode());
			
			// sitio_web
			sentencia.setString("sitio_web", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getWebsiteURI());
			
			// calle
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress() != null){
				if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getAddressLine() != null){
					sentencia.setString("calle", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getAddressLine().getLine());
				}else{
					sentencia.setString("calle", null);
				}
			}else{
				sentencia.setString("calle", null);
			}
			
			// codigo_postal
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress() != null){
				sentencia.setString("codigo_postal", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getPostalZone());
			}else{
				sentencia.setString("codigo_postal", null);
			}
			
			// poblacion
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress() != null){
				sentencia.setString("poblacion", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getCityName());
			}else{
				sentencia.setString("poblacion", null);
			}
			
			// pais
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress() != null){
				if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getCountry() != null){
					sentencia.setString("pais", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPostalAddress().getCountry().getIdentificationCode());
				}else{
					sentencia.setString("pais", null);
				}
			}else{
				sentencia.setString("pais", null);
			}

			// nombre_contacto
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact() != null){
				sentencia.setString("nombre_contacto", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact().getName());
			}else{
				sentencia.setString("nombre_contacto", null);
			}
			
			// telefono
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact() != null){
				sentencia.setString("telefono", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact().getTelephone());
			}else{
				sentencia.setString("telefono", null);
			}
			
			// fax
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact() != null){
				sentencia.setString("fax", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact().getTelefax());
			}else{
				sentencia.setString("fax", null);
			}
			
			// correo_electronico
			if (entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact() != null){
				sentencia.setString("correo_electronico", entry.getContractFolderStatus().getLocatedContractingParty().getParty().getContact().getElectronicMail());
			}else{
				sentencia.setString("correo_electronico", null);
			}
			
			// Ejecucion
			sentencia.execute();
			
			int entidad_adjudicadora = sentencia.getInt("entidad_adjudicadora");
			
			// ID's
			for (int i = 0; i < pi.length; i++){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newId(?, ?, ?)}");
				
				// Parametros
				sentencia.setInt("entidad_adjudicadora", entidad_adjudicadora);
				sentencia.setString("tipo_id", pi[i].getSchemeName());
				sentencia.setString("valor", pi[i].getId());
				
				// Ejecucion
				sentencia.execute();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writePlazoDeObtencion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newPlazoDeObtencion(?, ?, ?, ?, ?)}");
			
			// Plazo de obteción de PLIEGOS
			sentencia.setInt("ids_expedientes", ids);
			sentencia.setInt("tipo_plazo", PLAZO_PLIEGOS);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			sentencia.setString("observaciones", null);
			
			sentencia.execute();
			
			// Plazo de obteción de OFERTA
			sentencia.setInt("ids_expedientes", ids);
			sentencia.setInt("tipo_plazo", PLAZO_OFERTA);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod() != null
					&& entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getDescription() != null){
				String cadena = entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getDescription();
				sentencia.setString("observaciones", cadena);
			}
			
			sentencia.execute();
			
			// Plazo de obteción de SOLICITUDES
			sentencia.setInt("ids_expedientes", ids);
			sentencia.setInt("tipo_plazo", PLAZO_SOLICITUDES);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod() != null
					&& entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getDescription() != null){
				sentencia.setString("observaciones", entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getDescription());
			}else{
				sentencia.setString("observaciones", null);
			}
			
			sentencia.execute();
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeExtensionDeContrato(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newExtensionDeContrato(?, ?, ?)}");
			
			// Plazo de obteción de PLIEGOS
			sentencia.setInt("ids_expedientes", ids);
			
			if (entry.getContractFolderStatus().getProcurementProject().getContractExtension() != null){
				sentencia.setString("opciones", entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionsDescription());
				if (entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionValidityPeriod() != null){
					sentencia.setString("periodo_de_validez", entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionValidityPeriod().getDescription().trim()); 		
				}else{
					sentencia.setString("periodo_de_validez", null);
				}
			}else{
				sentencia.setDate("opciones", null);
				sentencia.setString("periodo_de_validez", null);
			}
			
			sentencia.execute();
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeCondicionesDeLicitacion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCondicionesDeLicitacion(?, ?, ?, ?, ?, ?)}");
			
			// Plazo de obteción de PLIEGOS
			sentencia.setInt("ids_expedientes", ids);
		
			sentencia.setBoolean("cv", entry.getContractFolderStatus().getTenderingTerms().getRequiredCurriculaIndicator());
			sentencia.setBoolean("admision_de_variantes", entry.getContractFolderStatus().getTenderingTerms().getVariantConstraintIndicator());
			
			if (entry.getContractFolderStatus().getTenderingTerms().getPriceRevisionFormulaDescription() != null){
				sentencia.setString("revision_de_precios", entry.getContractFolderStatus().getTenderingTerms().getPriceRevisionFormulaDescription());
			}else{
				sentencia.setString("revision_de_precios", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgramCode() != null){
				sentencia.setString("programa_de_financiacion", entry.getContractFolderStatus().getTenderingTerms().getFundingProgramCode());
			}else{
				sentencia.setString("programa_de_financiacion", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgram() != null){
				sentencia.setString("descripcion_programas_financiacion", entry.getContractFolderStatus().getTenderingTerms().getFundingProgram());
			}else{
				sentencia.setString("descripcion_programas_financiacion", null);
			}
			
			sentencia.execute();
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeGarantias(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			RequiredFinancialGuarantee[] rfg = entry.getContractFolderStatus().getTenderingTerms().getRequiredFinancialGuaranteeList();
			
			if (rfg != null){
				for (int i = 0; i < rfg.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newGarantia(?, ?, ?, ?, ?)}");
					
					// Primero insertamos la garantía, y luego la relación con ids
					sentencia.setInt("guarantee_type_code", rfg[i].getGuaranteeTypeCode());
					
					if (rfg[i].getLiabilityAmount() > 0){
						sentencia.setDouble("importe", rfg[i].getLiabilityAmount());
						sentencia.setString("moneda", rfg[i].getLiabilityAmountCurrencyID());
					}else{
						sentencia.setNull("importe", java.sql.Types.NULL);
						sentencia.setString("moneda", null);
					}
					
					if (rfg[i].getAmountRate() > 0){
						sentencia.setDouble("porcentaje", rfg[i].getAmountRate());
					}else{
						sentencia.setNull("porcentaje", java.sql.Types.NULL);
					}
					
					sentencia.execute();
					
					int garantia = sentencia.getInt("garantia");
					
					sentencia.close();
					
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newIds_Garantia(?, ?)}");
					sentencia.setInt("ids", ids);
					sentencia.setInt("garantia", garantia);
					
					sentencia.execute();
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void writeRequisitosDeParticipacion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newRequisitosDeParticipacion(?, ?, ?, ?)}");
			
			// Primero insertamos el requisito, y luego las listas
			if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest() != null){
				if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getPersonalSituation() != null){
					sentencia.setString("titulo_habilitante", entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getPersonalSituation());
				}else{
					sentencia.setString("titulo_habilitante", null);	
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getDescription() != null){
					sentencia.setString("solvencia_requerida", entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getDescription());
				}else{
					sentencia.setString("solvencia_requerida", null);	
				}
				
				sentencia.setInt("ids_expedientes", ids);
				
				sentencia.execute();
			
				int requisitos = sentencia.getInt("requisitos");
			
				sentencia.close();
				
				if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getRequiredBusinessClassificationScheme() != null){
					ClassificationCategory[] ccList = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getRequiredBusinessClassificationScheme().getClassificationCategoryList();
					if (ccList != null){
						for (int i = 0; i < ccList.length; i++){
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newClasificacionEmpresarial(?, ?)}");
							
							sentencia.setString("required_business_profile_code", ccList[i].getCodeValue());
							sentencia.setInt("requisitos_de_participacion", requisitos);
							
							sentencia.execute();
						}
					}
				}
				
				SpecificTendererRequirement[] strList = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getSpecificTendererRequirementList();
				if (strList != null){
					for (int i = 0; i < strList.length; i++){
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newCondicionesDeAdmision(?, ?)}");
						
						sentencia.setInt("declaration_type_code", strList[i].getRequirementTypeCode());
						sentencia.setInt("requisitos_de_participacion", requisitos);
						
						sentencia.execute();
					}
				}
			}else{
				sentencia.setString("titulo_habilitante", null);	
				sentencia.setString("solvencia_requerida", null);	
				sentencia.setInt("ids_expedientes", ids);
			}
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeCriterioDeEvaluacion(int ids, Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest() != null){
				// CRITERIO TECNICO
				if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getTechnicalEvaluationCriteriaList() != null){
					TechnicalEvaluationCriteria[] tec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getTechnicalEvaluationCriteriaList();
					if (tec != null){
						for (int i = 0; i < tec.length; i++){
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeEvaluacion(?, ?, ?, ?, ?)}");
							
							sentencia.setString("descripcion", tec[i].getDescription());
							sentencia.setInt("tipo_evaluacion", EVALUACION_TECNICA);
							sentencia.setString("tipo_technical", tec[i].getEvaluationCriteriaTypeCode());
							sentencia.setNull("tipo_financial", java.sql.Types.NULL);
							
							sentencia.execute();
							
							int criterio = sentencia.getInt("criterio");
							
							sentencia.close();
							
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newIds_CriterioDeEvaluacion(?, ?)}");
							sentencia.setInt("ids_expedientes", ids);
							sentencia.setInt("criterio_de_evaluacion", criterio);
							
							sentencia.execute();
						}
					}
					
					// CRITERIO ECONOMICO-FINANCIERO
					FinancialEvaluationCriteria[] fec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getFinancialEvaluationCriteriaList();
					if (fec != null){
						for (int i = 0; i < fec.length; i++){
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeEvaluacion(?, ?, ?, ?, ?)}");
							
							sentencia.setString("descripcion", fec[i].getDescription());
							sentencia.setInt("tipo_evaluacion", EVALUACION_ECONOMICA_FINANCIERA);
							sentencia.setNull("tipo_technical", java.sql.Types.NULL);
							sentencia.setString("tipo_financial", fec[i].getEvaluationCriteriaTypeCode());
							
							sentencia.execute();
							
							int criterio = sentencia.getInt("criterio");
							
							sentencia.close();
							
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newIds_CriterioDeEvaluacion(?, ?)}");
							sentencia.setInt("ids_expedientes", ids);
							sentencia.setInt("criterio_de_evaluacion", criterio);
							
							sentencia.execute();
						}
					}
				}	
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeSubcontratacionPermitida(int ids_expediente, Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms() != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newSubcontratacionPermitida(?, ?, ?)}");
				
				sentencia.setInt("ids_expedientes", ids_expediente);
				
				if (entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms().getRate() > 0){
					sentencia.setDouble("porcentaje", entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms().getRate());
				}else{
					sentencia.setNull("porcentaje", java.sql.Types.NULL);
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms().getDescription() != null){
					sentencia.setString("descripcion", entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms().getDescription());
				}else{
					sentencia.setString("descripcion", null);
				}
				
				sentencia.execute();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeCriterioDeAdjudicacion(int ids_expediente, Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms() != null){
				AwardingCriteria[] ac = entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms().getAwardingCriteriaList();
				
				if (ac != null){
					for (int i = 0; i < ac.length; i++){
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeAdjudicacion(?, ?, ?)}");
						
						sentencia.setString("descripcion", ac[i].getDescription());
						
						if (ac[i].getWeightNumeric() > 0){
							sentencia.setDouble("ponderacion", ac[i].getWeightNumeric());
						}else{
							sentencia.setNull("ponderacion", java.sql.Types.NULL);
						}
						
						sentencia.execute();
						
						int criterio = sentencia.getInt("criterio");
						
						sentencia.close();
						
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newIds_CriterioDeAdjudicacion(?, ?)}");
						sentencia.setInt("ids_expedientes", ids_expediente);
						sentencia.setInt("criterio_de_adjudicacion", criterio);
						
						sentencia.execute();
					}
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeResultadoDelProcedimiento(int ids_expediente, Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			TenderResult[] tr = entry.getContractFolderStatus().getTenderResultList();
			if (tr != null){
				for (int i = 0; i < tr.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newResultadoDelProcedimiento(?, ?, ?, ?, ?, ?, ?, ?)}");
					
					sentencia.setInt("resultado", tr[i].getResultCode());
					
					if (tr[i].getDescription() != null){
						sentencia.setString("motivacion", tr[i].getDescription());
					}else{
						sentencia.setString("motivacion", null);
					}
					
					sentencia.setDate("fecha_acuerdo", tr[i].getAwardDate());
					
					sentencia.setDouble("ofertas_recibidas", tr[i].getReceivedTenderQuantity());
					
					if(tr[i].getLowerTenderAmount() > 0){
						sentencia.setDouble("precio_oferta_mas_baja", tr[i].getLowerTenderAmount());
					}else{
						sentencia.setNull("precio_oferta_mas_baja", java.sql.Types.NULL);
					}
					
					if(tr[i].getHigherTenderAmount() > 0){
						sentencia.setDouble("precio_oferta_mas_alta", tr[i].getHigherTenderAmount());
					}else{
						sentencia.setNull("precio_oferta_mas_alta", java.sql.Types.NULL);
					}
					
					sentencia.setBoolean("excluidos", tr[i].getAbnormallyLowTenderIndicator());
					
					sentencia.execute();
					
					int resultado = sentencia.getInt("resultado_del_procedimiento");
					
					sentencia.close();
					
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newIds_Resultado(?, ?)}");
					sentencia.setInt("ids_expedientes", ids_expediente);
					sentencia.setInt("resultado_del_procedimiento", resultado);
					
					sentencia.execute();
					
					// Información sobre el contrato
					Contract[] c = tr[i].getContractList();
					if (c != null){
						writeInformacionDelContrato(resultado, tr[i].getStartDate(), c, entry_conn);
					}
				}
			}
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void writeInformacionDelContrato(int resultado, java.sql.Date startDate, Contract[] c, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			for (int i = 0; i < c.length; i++){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newContrato(?, ?, ?, ?)}");
				
				if (c[i].getId() != null){
					sentencia.setString("numero_contrato", c[i].getId());
				}else{
					sentencia.setString("numero_contrato", null);
				}
				
				if (c[i].getIssueDate() != null){
					sentencia.setDate("fecha_formalizacion", c[i].getIssueDate());
				}else{
					sentencia.setNull("fecha_formalizacion", java.sql.Types.NULL);
				}
				
				if (startDate != null){
					sentencia.setDate("fecha_entrada_vigor", startDate);
				}else{
					sentencia.setNull("fecha_entrada_vigor", java.sql.Types.NULL);
				}
				
				sentencia.execute();
				
				int contrato = sentencia.getInt("contrato");
				
				sentencia.close();
				
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newResultado_Contrato(?, ?)}");
				sentencia.setInt("resultado", resultado);
				sentencia.setInt("contrato", contrato);
				sentencia.execute();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
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
			
			// Se obtiene la salida
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
