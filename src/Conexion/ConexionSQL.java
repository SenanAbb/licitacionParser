package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import procurementProject.RequiredCommodityClassification;
import procurementProjectLot.ProcurementProjectLot;
import tenderResult.Contract;
import tenderResult.LegalMonetaryTotal;
import tenderResult.SubcontractTerms;
import tenderResult.TenderResult;
import tenderResult.WinningParty;
import tenderingTerms.AwardingCriteria;
import tenderingTerms.ClassificationCategory;
import tenderingTerms.FinancialEvaluationCriteria;
import tenderingTerms.Language;
import tenderingTerms.RequiredFinancialGuarantee;
import tenderingTerms.SpecificTendererRequirement;
import tenderingTerms.TechnicalEvaluationCriteria;
import utils.PartyIdentification;
import Entry.Entry;
import Parser.Parser;

import com.mysql.cj.jdbc.CallableStatement;

import contractModification.ContractModification;
import documents.AdditionalDocumentReference;
import documents.AdditionalPublicationDocumentReference;
import documents.AdditionalPublicationRequest;
import documents.AdditionalPublicationStatus;
import documents.GeneralDocument;
import documents.ValidNoticeInfo;

public class ConexionSQL extends Parser{
	private static final int PLIEGO_ADMINISTRATIVO = 1;
	private static final int PLIEGO_TECNICO = 2;
	private static final int PLIEGO_ADICIONAL = 3;
	
	private static final int PLAZO_PLIEGOS = 1;
	private static final int PLAZO_OFERTA = 2;
	private static final int PLAZO_SOLICITUDES = 3;
	
	private static final int EVALUACION_TECNICA = 1;
	private static final int EVALUACION_ECONOMICA_FINANCIERA = 2;
	
	private int feeds_expedientes;
	
    private String driver = "com.mysql.jdbc.Driver"; // Librería de MySQL
    private String database = "licitacion"; // Nombre de la base de datos  
    private String hostname = "localhost"; // Host   
    private String port = "3306"; // Puerto
    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";    
    private String username = "root"; // Nombre de usuario    
    private String password = "root"; // Clave de usuario
    
    public Connection conectarMySQL() throws Exception{
    	Connection conn = null;
    	
    	try{
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url, username, password);
    	} catch (ClassNotFoundException | SQLException e){
    		throw e;
    	}
    	
    	return conn;
    }
	
	/* TABLAS GENERALES */
	
	public void writeExpediente(Entry entry, int feeds, boolean primera_lectura) throws Exception {
		if (primera_lectura){
			writeData_firstTime(entry, feeds);
		}else{
			writeData_notFirstTime(entry, feeds);
		}
	}
	
	private void writeData_firstTime(Entry entry, int feeds) throws Exception {
		boolean existe = searchExpediente(Integer.parseInt(entry.getId()));
		boolean todo;
		if(existe){
			todo = false;
			writeNewFeedsExpediente(entry, feeds, todo);
		}else{
			todo = true;
			writeNewExpediente(entry);
			writeNewFeedsExpediente(entry, feeds, todo);
		}
	}
	
	private void writeData_notFirstTime(Entry entry, int feeds) throws Exception {
		boolean existe = searchExpediente(Integer.parseInt(entry.getId()));
		boolean todo;
		if(existe){
			todo = false;
			
			// Comprobamos si el estado del entry actual es mayor o igual que el último registrado
			Connection conn = conectarMySQL();
			PreparedStatement sentencia = conn.prepareStatement("SELECT feeds_expedientes, orden FROM tbl_feeds_expedientes INNER JOIN tbl_contract_folder_status_code" +
					" ON tbl_feeds_expedientes.estado = tbl_contract_folder_status_code.code WHERE expediente = ? ORDER BY updated DESC LIMIT 1");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			ResultSet rs = sentencia.executeQuery();
			while(rs.next()){
				int orden_ultimo = rs.getInt("orden");
				int orden_entry = -1;
				int feeds_exp_ultimo = rs.getInt("feeds_expedientes");
				rs.close();
				
				sentencia = conn.prepareStatement("SELECT orden FROM tbl_contract_folder_status_code WHERE code = ?");
				sentencia.setString(1, entry.getContractFolderStatus().getContractFolderStatusCode());
				
				rs = sentencia.executeQuery();
				if (rs.next()){
					orden_entry = rs.getInt("orden");	
				}else{
					throw new SQLException();
				}
				
				/* TRES POSIBILIDADES
				 * 1 - Orden MAYOR: Las tablas que no existan las creo y linkeo al nuevo ids_expedientes
				 * 2 - Orden IGUAL: Es una RECTIFICACIÓN. Busco la tabla, junto con el valor que se modifica. Una vez encontrado:
				 * 	2.1 - Creo un nuevo registro de la tabla linkeado al ids_expediente nuevo
				 * 3 - Orden MENOR: ERROR
				 */
				
				if (orden_entry >= orden_ultimo){ // HAY QUE QUITAR EL =
					writeNewFeedsExpediente(entry, feeds, todo);
					checkTables(entry, feeds_exp_ultimo);
				}else if (orden_entry == orden_ultimo){ // PARA RECTIFICACION
					// Para la primera version solo vamos a almacenar el IDS de la rectificación
					writeNewFeedsExpediente(entry, feeds, todo);
				}
			}
		}else{
			todo = true;
			writeNewExpediente(entry);
			writeNewFeedsExpediente(entry, feeds, todo);
		}
	}

	private void writeNewExpediente(Entry entry) throws Exception{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			conn.setAutoCommit(false);
			
			sentencia = (CallableStatement) conn.prepareCall("{call newExpediente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
			
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
			sentencia.setInt("num_lotes", 0);
			
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
			if (conn != null) conn.rollback();
			throw e;
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

	private void writeNewFeedsExpediente(Entry entry, int feeds, boolean todo) throws Exception{
		Connection conn = conectarMySQL();
		CallableStatement sentencia = null;
		int entidad_adjudicadora = writeEntidadAdjudicadora(entry, conn);
		
		try {
			if (todo){
				sentencia = (CallableStatement) conn.prepareCall("{call newFeeds_Expediente(?, ?, ?, ?, ?, ?, ?)}");
				
				sentencia.setInt("feeds", feeds);
				sentencia.setInt("expediente", Integer.parseInt(entry.getId()));
				sentencia.setString("summary", entry.getSummary());
				sentencia.setTimestamp("updated", entry.getUpdated());
				sentencia.setString("estado", entry.getContractFolderStatus().getContractFolderStatusCode());
				sentencia.setInt("entidad_adjudicadora", entidad_adjudicadora);
				
				sentencia.execute();
				
				feeds_expedientes = sentencia.getInt("feeds_expediente");
				
				// HAY QUE CONTROLAR QUE DESPUES DE CREAR EL IDS_EXPEDIENTE, SI FALLA ALGO, MARCAR ESE IDS COMO ERROR
				
				// INICIAMOS LA TRANSACCION
				// En caso de haber algun fallo con la base de datos, esta entry se descartará
				conn.setAutoCommit(false);
				
				writeLugarDeEjecucion(entry, conn);
				writeLotes(entry, conn);
				writeProcesoDeLicitacion(entry, conn);
				writePlazoDeObtencion(entry, conn);
				writeExtensionDeContrato(entry, conn);
				writeCondicionesDeLicitacion(entry, conn);
				writeGarantias(entry, conn);
				writeRequisitosDeParticipacion(entry, conn);
				writeCriterioDeEvaluacion(entry, conn);
				writeSubcontratacionPermitida(entry, conn);
				writeCriterioDeAdjudicacion(entry, conn);
				writeResultadoDelProcedimiento(entry, conn);
				writeJustificacionDelProceso(entry, conn);
				writeModificacionesDeContrato(entry, conn);
				writePublicacionesOficiales(entry, conn);
				writeOtrosDocumentos(entry, conn);
				
				// FINALIZAMOS LA TRANSACCION
				conn.commit();
			}else{
				sentencia = (CallableStatement) conn.prepareCall("{call newFeeds_Expediente(?, ?, ?, ?, ?, ?, ?)}");
				
				sentencia.setInt("feeds", feeds);
				sentencia.setInt("expediente", Integer.parseInt(entry.getId()));
				sentencia.setString("summary", entry.getSummary());
				sentencia.setTimestamp("updated", entry.getUpdated());
				sentencia.setString("estado", entry.getContractFolderStatus().getContractFolderStatusCode());
				sentencia.setInt("entidad_adjudicadora", entidad_adjudicadora);
				
				sentencia.execute();
				
				feeds_expedientes = sentencia.getInt("feeds_expediente");
				
				writeLotes(entry, conn);
				writeResultadoDelProcedimiento(entry, conn);
			}
		} catch (SQLException e){
			if (conn != null) conn.rollback();
			throw e;
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

	private void checkTables(Entry entry, int ids_exp_ultimo) throws Exception{
		Connection conn = conectarMySQL();
		PreparedStatement sentencia = null;
		ResultSet rs = null;
		int aux = 0;
		
		conn.setAutoCommit(false);
		
		try {
			// LUGAR DE EJECUCION
			sentencia = conn.prepareStatement("SELECT * FROM tbl_lugar_de_ejecucion INNER JOIN tbl_feeds_expedientes ON "
					+ "tbl_lugar_de_ejecucion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente= ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			if (!rs.next()){
				writeLugarDeEjecucion(entry, conn);
			}
			rs.close();
			sentencia.close();
			
			// LOTES
			// Si tengo lotes, hago comprobaciones
			ProcurementProjectLot[] ppl = entry.getContractFolderStatus().getProcurementProjectLotList();
			if (ppl != null){
				// Miro si tengo más lotes que los almacenados en la BD
				int num_lotes = 0;
				
				sentencia = conn.prepareStatement("SELECT num_lotes FROM tbl_expedientes WHERE expedientes = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				
				rs = sentencia.executeQuery();
				if (rs.next()){
					num_lotes = rs.getInt("num_lotes");
				}
				
				if (num_lotes < ppl.length){
					// Actualizamos el parámetro num_lotes en tbl_expediente
					sentencia= conn.prepareStatement("UPDATE tbl_expedientes SET num_lotes = ?");
					sentencia.setInt(1, ppl.length);
					sentencia.executeUpdate();
					
					// Busco los que no tengo en la BD y los inserto
					for (ProcurementProjectLot p : ppl){
						sentencia = conn.prepareStatement("SELECT * FROM tbl_lotes WHERE numero_de_lote = ? AND expedientes = ?");
						sentencia.setString(1, p.getID());
						sentencia.setInt(2, Integer.parseInt(entry.getId()));
						
						rs = sentencia.executeQuery();
						if (!rs.next()){
							writeLotes_Unico(p, Integer.parseInt(entry.getId()), conn);
						}
					}
				}
			}	
			
			// PROCESO DE LICITACION
			sentencia = conn.prepareStatement("SELECT * FROM tbl_proceso_de_licitacion INNER JOIN tbl_feeds_expedientes ON "
					+ "tbl_proceso_de_licitacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			if (!rs.next()){
				writeProcesoDeLicitacion(entry, conn);
			}
			rs.close();
			sentencia.close();
			
			// PLAZOS DE OBTENCIÓN
			// Pliegos
			sentencia = conn.prepareStatement("SELECT * FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ? "
					+ "AND tbl_plazo_de_obtencion.tipo_plazo = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			sentencia.setInt(2, PLAZO_PLIEGOS);
			
			rs = sentencia.executeQuery();
			// Si no está en la base de datos Y LO TENGO EN EL ENTRY
			if (!rs.next() && entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod() != null){
				// Busco todos los plazos del expediente
				sentencia = conn.prepareStatement("SELECT id_plazo_de_obtencion, tipo_plazo FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes" + 
						" ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes" + 
						" WHERE tbl_plazo_de_obtencion.feeds_expedientes = ?");
				sentencia.setInt(1, ids_exp_ultimo);
				
				// Actualizo el ids_expediente al actual
				rs = sentencia.executeQuery();
				while (rs.next()){
					sentencia = conn.prepareStatement("UPDATE tbl_plazo_de_obtencion SET feeds_expedientes = ?");
					sentencia.setInt(1, feeds_expedientes);
					sentencia.executeUpdate();
				}
				writePlazoDeObtencionPliegos(entry, conn);
				aux = ids_exp_ultimo;
				ids_exp_ultimo = feeds_expedientes;
			}
			rs.close();
			sentencia.close();
			
			/// Oferta
			sentencia = conn.prepareStatement("SELECT * FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ? "
					+ "AND tbl_plazo_de_obtencion.tipo_plazo = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			sentencia.setInt(2, PLAZO_OFERTA);
			
			rs = sentencia.executeQuery();
			if (!rs.next() && entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod() != null){
				// Busco todos los plazos del expediente
				sentencia = conn.prepareStatement("SELECT id_plazo_de_obtencion, tipo_plazo FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes" + 
						" ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes" + 
						" WHERE tbl_plazo_de_obtencion.feeds_expedientes = ?");
				sentencia.setInt(1, ids_exp_ultimo);
				
				// Actualizo el ids_expediente al actual
				rs = sentencia.executeQuery();
				while (rs.next()){
					sentencia = conn.prepareStatement("UPDATE tbl_plazo_de_obtencion SET feeds_expedientes = ?");
					sentencia.setInt(1, feeds_expedientes);
					sentencia.executeUpdate();
				}
				writePlazoDeObtencionOferta(entry, conn);
				aux = ids_exp_ultimo;
				ids_exp_ultimo = feeds_expedientes;
			}
			rs.close();
			sentencia.close();
			
			/// Solicitudes
			sentencia = conn.prepareStatement("SELECT * FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ? "
					+ "AND tbl_plazo_de_obtencion.tipo_plazo = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			sentencia.setInt(2, PLAZO_SOLICITUDES);
			
			rs = sentencia.executeQuery();
			if (!rs.next() && entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod() != null){
				// Busco todos los plazos del expediente
				sentencia = conn.prepareStatement("SELECT id_plazo_de_obtencion, tipo_plazo FROM tbl_plazo_de_obtencion INNER JOIN tbl_feeds_expedientes" + 
						" ON tbl_plazo_de_obtencion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes" + 
						" WHERE tbl_plazo_de_obtencion.feeds_expedientes = ?");
				sentencia.setInt(1, ids_exp_ultimo);
				
				// Actualizo el ids_expediente al actual
				rs = sentencia.executeQuery();
				while (rs.next()){
					sentencia = conn.prepareStatement("UPDATE tbl_plazo_de_obtencion SET feeds_expedientes = ?");
					sentencia.setInt(1, feeds_expedientes);
					sentencia.executeUpdate();
				}
				writePlazoDeObtencionSolicitudes(entry, conn);
				aux = ids_exp_ultimo;
				ids_exp_ultimo = feeds_expedientes;
			}
			rs.close();
			sentencia.close();
			
			ids_exp_ultimo = aux;
			
			// EXTENSION DE CONTRATO
			sentencia = conn.prepareStatement("SELECT * FROM tbl_extension_de_contrato INNER JOIN tbl_feeds_expedientes ON "
					+ "tbl_extension_de_contrato.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			if (!rs.next()){
				writeExtensionDeContrato(entry, conn);
			}
			rs.close();
			sentencia.close();
			
			// CONDICIONES DE LICITACION
			sentencia = conn.prepareStatement("SELECT * FROM tbl_condiciones_de_licitacion INNER JOIN tbl_feeds_expedientes ON "
					+ "tbl_condiciones_de_licitacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			if (rs.next()){
				// Si existe y hay algun dato distinto
				boolean distinto = false;
				
				if (entry.getContractFolderStatus().getTenderingTerms().getRequiredCurriculaIndicator() != rs.getBoolean("cv")){
					distinto = true;
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getVariantConstraintIndicator() != rs.getBoolean("admision_de_variantes")){
					distinto = true;
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getPriceRevisionFormulaDescription() != null){
					if (entry.getContractFolderStatus().getTenderingTerms().getPriceRevisionFormulaDescription() != rs.getString("revision_de_precios")){
						distinto = true;
					}
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgramCode() != null){
					if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgramCode() != rs.getString("programa_de_financiacion")){
						distinto = true;
					}
				}
				
				if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgram() != null){
					if (entry.getContractFolderStatus().getTenderingTerms().getFundingProgram() != rs.getString("descripcion_programas_financiacion")){
						distinto = true;
					}
				}
				
				if (distinto){
					writeCondicionesDeLicitacion(entry, conn);	
				}
			}else if (!rs.next()){
				// Si no existe
				writeCondicionesDeLicitacion(entry, conn);
			}
			
			rs.close();
			sentencia.close();
			
			// GARANTÍA
			RequiredFinancialGuarantee[] rfg = entry.getContractFolderStatus().getTenderingTerms().getRequiredFinancialGuaranteeList();
			
			sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_garantia INNER JOIN tbl_feeds_expedientes ON "
					+ "tbl_garantia.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			rs.next();
			int tam = rs.getInt(1);
			int rfg_tam = 0;
			if (rfg != null){
				rfg_tam = rfg.length;
			}
			
			if (rfg_tam > tam){
				sentencia = conn.prepareStatement("SELECT * FROM tbl_garantia INNER JOIN tbl_feeds_expedientes ON "
						+ "tbl_garantia.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				
				rs = sentencia.executeQuery();
				
				// Si no hay ninguna garantia, añado todos
				if (!rs.next()){
					writeGarantias(entry, conn);
				}else{
					boolean encontrado = false;
					for (int i = 0; i < rfg_tam; i++){
						rs = sentencia.executeQuery();
						while (rs.next() && !encontrado){
							if (rs.getInt("guarantee_type_code") == rfg[i].getGuaranteeTypeCode() &&
									(rs.getDouble("importe") == rfg[i].getLiabilityAmount() ||
									rs.getDouble("porcentaje") == rfg[i].getAmountRate())){
								encontrado = true;
							}
							if (!encontrado){
								writeGarantias_Unico(rfg[i], conn);
							}
						}
						rs.close();
						encontrado = false;
					}
				}
			}
			
			// 	REQUISITOS DE PARTICIPACION
			sentencia = conn.prepareStatement("SELECT * FROM tbl_requisitos_de_participacion INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_requisitos_de_participacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			
			if (!rs.next()){
				writeRequisitosDeParticipacion(entry, conn);
			}
			
			rs.close();
			sentencia.close();
			
			// CRITERIOS DE EVALUACION
			if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest() != null){
				TechnicalEvaluationCriteria[] tec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getTechnicalEvaluationCriteriaList();
				FinancialEvaluationCriteria[] fec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getFinancialEvaluationCriteriaList();
				
				sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_criterio_de_evaluacion INNER JOIN tbl_feeds_expedientes ON tbl_criterio_de_evaluacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				 
				rs = sentencia.executeQuery();
				rs.next();
				
				tam = rs.getInt(1);
				int fec_tam = 0;
				int tec_tam = 0;
				
				if (tec != null) tec_tam = tec.length;
				if (fec != null) fec_tam = fec.length;
				
				if ((tec_tam + fec_tam) > tam){
					// Busco el distinto
					sentencia = conn.prepareStatement("SELECT * FROM tbl_criterio_de_evaluacion INNER JOIN tbl_feeds_expedientes ON tbl_criterio_de_evaluacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
					sentencia.setInt(1, Integer.parseInt(entry.getId()));
					 
					rs = sentencia.executeQuery();
					
					// Si no hay ningun criterio
					if (!rs.next()){
						if (tec != null){
							for (int i = 0; i < tec.length; i++){
								writeCriterioDeEvaluacionTecnica(tec[i], conn);
							}
						}
						if (fec != null){
							for (int i = 0; i < fec.length; i++){
								writeCriterioDeEvaluacionEconomica(fec[i], conn);
							}
						}
					}else{
						// Si hay algún criterio
						// 1. Para cada criterio de la entry, comprueba si está en la BD
						// 1.1 Si no está, lo añado
						boolean encontrado = false;
						rs = sentencia.executeQuery();
						
						if (tec != null){
							for (int i = 0; i < tec.length; i++){
								rs = sentencia.executeQuery();
								while (rs.next() && !encontrado){
									if (rs.getString("descripcion").compareTo(tec[i].getDescription()) == 0 && rs.getString("tipo_technical").compareTo(tec[i].getEvaluationCriteriaTypeCode()) == 0){
										encontrado = true;
									}
								}
								if (!encontrado){
									writeCriterioDeEvaluacionTecnica(tec[i], conn);
								}
								rs.close();
								encontrado = false;
							}
						}
						
						if (fec != null){
							for (int i = 0; i < fec.length; i++){
								rs = sentencia.executeQuery();
								while (rs.next() && !encontrado){
									if (rs.getString("descripcion").compareTo(fec[i].getDescription()) == 0 && rs.getString("tipo_financial").compareTo(fec[i].getEvaluationCriteriaTypeCode()) == 0){
										encontrado = true;
									}
								}
								if (!encontrado){
									writeCriterioDeEvaluacionEconomica(fec[i], conn);
								}
								rs.close();
								encontrado = false;
							}
						}
					}
				}
			}
			
			// SUBCONTRATACION PERMITIDA
			sentencia = conn.prepareStatement("SELECT * FROM tbl_subcontratacion_permitida INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_subcontratacion_permitida.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			
			if (!rs.next()){
				writeSubcontratacionPermitida(entry, conn);
			}
			
			// CRITERIOS DE ADJUDICACION
			if (entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms() != null){
				AwardingCriteria[] ac = entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms().getAwardingCriteriaList();
				
				sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_criterio_de_adjudicacion INNER JOIN tbl_feeds_expedientes ON tbl_criterio_de_adjudicacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				 
				rs = sentencia.executeQuery();
				
				rs.next();
				tam = rs.getInt(1);
				int ac_tam = 0;
				if (ac != null){
					ac_tam = ac.length;
				}
				
				if (ac_tam > tam){
					sentencia = conn.prepareStatement("SELECT * FROM tbl_criterio_de_adjudicacion INNER JOIN tbl_feeds_expedientes ON tbl_criterio_de_adjudicacion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
					sentencia.setInt(1, Integer.parseInt(entry.getId()));
					
					rs = sentencia.executeQuery();
					
					// Si no hay ningun criterio, añado todos
					if (!rs.next()){
						writeCriterioDeAdjudicacion(entry, conn);
					}else{
						boolean encontrado = false;
						for (int i = 0; i < ac_tam; i++){
							rs = sentencia.executeQuery();
							while (rs.next() && !encontrado){
								if (rs.getString("descripcion").compareTo(ac[i].getDescription()) == 0 &&
										rs.getDouble("ponderacion") == ac[i].getWeightNumeric()){
									encontrado = true;
								}
							}
							if (!encontrado){
								writeCriterioDeAdjudicacion_Unico(ac[i], conn);
							}
							rs.close();
							encontrado = false;
						}
					}
				}
			}
			
			// RESULTADO DEL PROCEDIMIENTO
			TenderResult[] tr = entry.getContractFolderStatus().getTenderResultList();
			
			if (tr != null){
				// Miro si estoy con lotes o no
				sentencia = conn.prepareStatement("SELECT num_lotes FROM tbl_expedientes WHERE expedientes = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				rs = sentencia.executeQuery();
				rs.next();

				boolean lotes = rs.getInt("num_lotes") > 0;
				
				for (TenderResult t : tr){
					boolean encontrado = false;
					
					PreparedStatement sentencia1 = conn.prepareStatement("SELECT * FROM tbl_resultado_del_procedimiento INNER JOIN tbl_feeds_expedientes ON tbl_resultado_del_procedimiento.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes WHERE tbl_feeds_expedientes.expediente = ?");
					sentencia1.setInt(1, Integer.parseInt(entry.getId()));
					
					rs = sentencia1.executeQuery();
					
					while (rs.next() && !encontrado){
						if (rs.getInt("result_code") == t.getResultCode() &&
								rs.getString("motivacion").compareTo(t.getDescription()) == 0 &&
								rs.getDouble("precio_oferta_mas_baja") == t.getLowerTenderAmount() &&
								rs.getDouble("precio_oferta_mas_alta") == t.getHigherTenderAmount()){
							encontrado = true;
						}
					}
					
					if (!encontrado && lotes){
						writeResultadoDelProcedimiento_Unico(t, conn, t.getAwardedTenderedProject().getProcurementProjectLotID());
					}else if (!encontrado && !lotes){
						writeResultadoDelProcedimiento_Unico(t, conn, null);
					}
					sentencia1.close();
					rs.close();
				}
			}
			
			// INFORMACION SOBRE EL CONTRATO, ADJUDICATARIO, IMPORTES DE LA ADJUDICACION y CONDICIONES DE SUBCONTRATACION VIENEN CON EL RESULTADO
			
			// JUSTIFICACION DEL PROCESO
			sentencia = conn.prepareStatement("SELECT * FROM tbl_justificacion_del_proceso INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_justificacion_del_proceso.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			
			if (!rs.next()){
				writeJustificacionDelProceso(entry, conn);
			}
			
			// MODIFICACIONES DE CONTRATO
			ContractModification[] cm = entry.getContractFolderStatus().getContractModificationList();
			
			sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_modificaciones_de_contrato INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_modificaciones_de_contrato.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			rs.next();
			tam = rs.getInt(1);
			int cm_tam = 0;
			if (cm != null){
				cm_tam = cm.length;
			}
			
			if (cm_tam > tam){
				sentencia = conn.prepareStatement("SELECT * FROM tbl_modificaciones_de_contrato INNER JOIN tbl_feeds_expedientes "
						+ "ON tbl_modificaciones_de_contrato.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
						+ "WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				
				rs = sentencia.executeQuery();
				
				// Si no hay ninguna modificacion
				if (!rs.next()){
					writeModificacionesDeContrato(entry, conn);
				}else{
					boolean encontrado = false;
					for (int i = 0; i < cm_tam; i++){
						rs = sentencia.executeQuery();
						while (rs.next() && !encontrado){
							if (rs.getString("numero_de_contrato").compareTo(cm[i].getContractID()) == 0 &&
									rs.getInt("numero_de_modificacion") == cm[i].getID() &&
									rs.getDouble("importe_modificacion") == cm[i].getContractModificationLegalMonetaryTotal().getTaxExclusiveAmount()){
								encontrado = true;
							}
						}
						if (!encontrado){
							writeModificacionesDeContrato_Unico(cm[i], conn);
						}
						rs.close();
						encontrado = false;
					}
				}
			}
			
			// PUBLICACIONES OFICIALES
			ValidNoticeInfo[] vni = entry.getContractFolderStatus().getValidNoticeInfoList();
			
			sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_publicaciones_oficiales INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_publicaciones_oficiales.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			rs.next();
			tam = rs.getInt(1);
			int vni_tam = 0;
			if (vni != null){
				vni_tam = vni.length;
			}
			
			if (vni_tam > tam){
				sentencia = conn.prepareStatement("SELECT * FROM tbl_publicaciones_oficiales INNER JOIN tbl_feeds_expedientes "
						+ "ON tbl_publicaciones_oficiales.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
						+ "WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				
				rs = sentencia.executeQuery();
				
				// Si no hay ninguna
				if (!rs.next()){
					writePublicacionesOficiales(entry, conn);
				}else{
					boolean encontrado = false;
					for (int i = 0; i < vni_tam; i++){
						sentencia = conn.prepareStatement("SELECT tipo_de_anuncio, medio_de_publicacion, fecha FROM tbl_publicaciones_oficiales INNER JOIN tbl_additional_publication_status "
								+ "ON tbl_publicaciones_oficiales.publicaciones_oficiales = tbl_additional_publication_status.publicaciones_oficiales " 
								+ "INNER JOIN tbl_fecha_envio "
								+ "ON tbl_additional_publication_status.additional_publication_status = tbl_fecha_envio.additional_publication_status");
						
						rs = sentencia.executeQuery();
						encontrado = false;
						
						while (rs.next() && !encontrado){
							if (rs.getString("tipo_de_anuncio").compareTo(vni[i].getNoticeTypeCode()) == 0){
								for (AdditionalPublicationStatus aps : vni[i].getAdditionalPublicationStatusList()){
									if (rs.getString("medio_de_publicacion").compareTo(aps.getPublicationMediaName()) == 0){
										for (AdditionalPublicationDocumentReference apdr : aps.getAdditionalPublicationDocumentReferenceList()){
											if (apdr.getIssueDate() != null){
												SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
												String fechaString = format.format(apdr.getIssueDate());
												Date fecha = null;
												try {
													fecha = format.parse(fechaString);
												} catch (ParseException e) {e.printStackTrace();}
												if (fecha.compareTo(rs.getDate("fecha")) == 0){
													encontrado = true;
													break;
												}
											}
										}
									}
								}	
							}
						}
						if (!encontrado){
							writePublicacionesOficiales_Unico(vni[i], conn);
						}
					}
					rs.close();
					encontrado = false;
				}
			}
			
			// OTROS DOCUMENTOS
			GeneralDocument[] gd = entry.getContractFolderStatus().getGeneralDocumentList();
			
			sentencia = conn.prepareStatement("SELECT COUNT(*) FROM tbl_otros_documentos INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_otros_documentos.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE tbl_feeds_expedientes.expediente = ?");
			sentencia.setInt(1, Integer.parseInt(entry.getId()));
			
			rs = sentencia.executeQuery();
			rs.next();
			tam = rs.getInt(1);
			int gd_tam = 0;
			if (gd != null){
				gd_tam = gd.length;
			}
			
			if (gd_tam > tam){
				sentencia = conn.prepareStatement("SELECT * FROM tbl_otros_documentos INNER JOIN tbl_feeds_expedientes "
						+ "ON tbl_otros_documentos.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
						+ "WHERE tbl_feeds_expedientes.expediente = ?");
				sentencia.setInt(1, Integer.parseInt(entry.getId()));
				
				rs = sentencia.executeQuery();
				
				// Si no hay ninguna modificacion
				if (!rs.next()){
					writeOtrosDocumentos(entry, conn);
				}else{
					boolean encontrado = false;
					for (int i = 0; i < gd_tam; i++){
						rs = sentencia.executeQuery();
						while (rs.next() && !encontrado){
							if (rs.getString("ID").compareTo(gd[i].getGeneralDocumentDocumentReference().getId()) == 0){
									encontrado = true;
							}
						}
						if (!encontrado){
							writeOtrosDocumentos_Unico(gd[i], conn);
							encontrado = false;
						}
						rs.close();
						encontrado = false;
					}
				}
			}
			
			conn.commit();
			
		} catch (SQLException e){
			if (conn != null) conn.rollback();
			throw e;
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
	
	private void writeLugarDeEjecucion(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation() != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newLugarDeEjecucion(?, ?, ?, ?, ?, ?, ?)}");
				
				// Parametros
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				sentencia.setString("subentidad_territorial", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getCountrySubentityCode());
				
				if (entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getAddress() != null){
					sentencia.setString("subentidad_nacional", entry.getContractFolderStatus().getProcurementProject().getRealizedLocation().getCountrySubentity());
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
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeLotes(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		PreparedStatement sentencia_busqueda = null;
		
		try {
			ProcurementProjectLot[] ppl = entry.getContractFolderStatus().getProcurementProjectLotList();
			
			if (ppl != null){
				int lote = 0, num_lotes;
				
				// Actualizamos el parámetro num_lotes en tbl_expediente
				sentencia_busqueda = entry_conn.prepareStatement("SELECT num_lotes FROM tbl_expedientes WHERE expedientes = ?");
				sentencia_busqueda.setInt(1, Integer.parseInt(entry.getId()));
				ResultSet rs = sentencia_busqueda.executeQuery();
				rs.next();
				
				num_lotes = rs.getInt("num_lotes");
				
				sentencia_busqueda.close();
				
				for (int i = 0; i < ppl.length; i++){
					// Compruebo si ese lote lo tengo en la BD
					sentencia_busqueda = entry_conn.prepareStatement("SELECT lotes FROM tbl_lotes WHERE numero_de_lote = ? AND expedientes = ?");
					sentencia_busqueda.setString(1, ppl[i].getID());
					sentencia_busqueda.setInt(2, Integer.parseInt(entry.getId()));
					
					rs = sentencia_busqueda.executeQuery();
					
					// Si no lo tengo, lo añado y sumo 1 al numero de lotes
					if (!rs.next()){
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newLote(?, ?, ?, ?, ?, ?, ?)}");
						
						sentencia.setInt("expedientes", Integer.parseInt(entry.getId()));
						
						sentencia.setString("numero_de_lote", ppl[i].getID());
						sentencia.setString("objeto", ppl[i].getProcurementProject().getName());
						sentencia.setDouble("importe_sin_impuestos", ppl[i].getProcurementProject().getBudgetAmount().getTotalAmount());
						sentencia.setDouble("importe_con_impuestos", ppl[i].getProcurementProject().getBudgetAmount().getTaxExclusiveAmount());
						
						sentencia_busqueda.close();
						
						// Para el lugar de ejecución -> el del expediente que lo contiene
						sentencia_busqueda = entry_conn.prepareStatement("SELECT lugar_de_ejecucion FROM tbl_lugar_de_ejecucion INNER JOIN tbl_feeds_expedientes "
								+ "ON tbl_lugar_de_ejecucion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
								+ "WHERE expediente = ?");
						sentencia_busqueda.setInt(1, Integer.parseInt(entry.getId()));
						
						rs = sentencia_busqueda.executeQuery();
						if (rs.next()){
							sentencia.setInt("lugar_de_ejecucion", rs.getInt("lugar_de_ejecucion"));
						}
						
						sentencia.execute();
						
						lote = sentencia.getInt("lote");
						
						sentencia.close();
						
						if (ppl[i].getProcurementProject().getRequiredCommodityClassificationList() != null){
							// CPV
							for (RequiredCommodityClassification r : ppl[i].getProcurementProject().getRequiredCommodityClassificationList()){
								sentencia = (CallableStatement) entry_conn.prepareCall("{call newLote_CPV(?, ?)}");
								
								// Parametros
								sentencia.setInt("code", r.getItemClassificationCode());
								sentencia.setInt("lotes", lote);
								
								// Ejecutamos
								sentencia.execute();
								sentencia.close();
							}
						}
						
						num_lotes++;
					}
				}
				
				sentencia_busqueda = entry_conn.prepareStatement("UPDATE tbl_expedientes SET num_lotes = ?");
				sentencia_busqueda.setInt(1, num_lotes);
				sentencia_busqueda.executeUpdate();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (sentencia_busqueda != null) sentencia_busqueda.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeLotes_Unico(ProcurementProjectLot p, int id, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		PreparedStatement sentencia_busqueda = null;
		
		try {
			int lote = 0;
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newLote(?, ?, ?, ?, ?, ?, ?)}");
			
			sentencia.setInt("expedientes", id);
			
			sentencia.setString("numero_de_lote", p.getID());
			sentencia.setString("objeto", p.getProcurementProject().getName());
			sentencia.setDouble("importe_sin_impuestos", p.getProcurementProject().getBudgetAmount().getTotalAmount());
			sentencia.setDouble("importe_con_impuestos", p.getProcurementProject().getBudgetAmount().getTaxExclusiveAmount());
			
			// Para el lugar de ejecución primero miramos si existe, si no existe lo creamos
			sentencia_busqueda = entry_conn.prepareStatement("SELECT lugar_de_ejecucion FROM tbl_lugar_de_ejecucion INNER JOIN tbl_feeds_expedientes "
					+ "ON tbl_lugar_de_ejecucion.feeds_expedientes = tbl_feeds_expedientes.feeds_expedientes "
					+ "WHERE expediente = ?");
			sentencia_busqueda.setInt(1, id);
			
			ResultSet rs = sentencia_busqueda.executeQuery();
			if (rs.next()){
				sentencia.setInt("lugar_de_ejecucion", rs.getInt("lugar_de_ejecucion"));
			}
			
			sentencia.execute();
			
			lote = sentencia.getInt("lote");
			
			sentencia.close();
			
			// CPV
			for (RequiredCommodityClassification r : p.getProcurementProject().getRequiredCommodityClassificationList()){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newLote_CPV(?, ?)}");
				
				// Parametros
				sentencia.setInt("code", r.getItemClassificationCode());
				sentencia.setInt("lotes", lote);
				
				// Ejecutamos
				sentencia.execute();
				sentencia.close();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (sentencia_busqueda != null) sentencia_busqueda.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeProcesoDeLicitacion(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newProcesoDeLicitacion(?, ?, ?, ?, ?, ?, ?, ?)}");
			
			// Parametros
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
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
			if (languageList != null){
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
				throw e;
			}
		}
	}
	private int writeEntidadAdjudicadora(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		PreparedStatement sentencia_busqueda = null;
		
		int entidad_adjudicadora = -1;
		
		try {
			String query = "SELECT entidad_adjudicadora FROM tbl_entidad_adjudicadora WHERE ";
			for (PartyIdentification pi : entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPartyIdentificationList()){
				query += pi.getSchemeName() + " = '" + pi.getId() + "' OR ";
			}
			query = query.substring(0, query.length()-3); // Eliminamos el ultimo OR
			
			sentencia_busqueda = entry_conn.prepareStatement(query);
			
			ResultSet rs = sentencia_busqueda.executeQuery();
			
			if(rs.next()){
				entidad_adjudicadora = rs.getInt("entidad_adjudicadora");
			}else{
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newEntidadAdjudicadora(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				
				// ubicacion_organica
				boolean encontrado = false;
				PartyIdentification[] pi = entry.getContractFolderStatus().getLocatedContractingParty().getParty().getPartyIdentificationList();
				
				sentencia.setNull("NIF", java.sql.Types.NULL);
				sentencia.setNull("DIR3", java.sql.Types.NULL);
				sentencia.setNull("ID_PLATAFORMA", java.sql.Types.NULL);
				
				for (int i = 0; i < pi.length; i++){
					// Parámetro DIR3
					if (pi[i].getSchemeName().compareTo("DIR3") == 0){
						encontrado = true;
						sentencia.setString("ubicacion_organica", pi[i].getId());
						sentencia.setString("DIR3", pi[i].getId());
					// Parámetro NIF
					}else if (pi[i].getSchemeName().compareTo("NIF") == 0){
						sentencia.setString("NIF", pi[i].getId());
					// Parámetro ID_PLATAFORMA
					}else if (pi[i].getSchemeName().compareTo("ID_PLATAFORMA") == 0){
						sentencia.setString("ID_PLATAFORMA", pi[i].getId());
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
				
				entidad_adjudicadora = sentencia.getInt("entidad_adjudicadora");
			}
			return entidad_adjudicadora;
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (sentencia_busqueda != null) sentencia_busqueda.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePlazoDeObtencion(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			// Plazo de obteción de PLIEGOS
			if (entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod() != null){
				writePlazoDeObtencionPliegos(entry, entry_conn);	
			}
			
			// Plazo de obteción de OFERTA
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod() != null){
				writePlazoDeObtencionOferta(entry, entry_conn);
			}
			
			// Plazo de obteción de SOLICITUDES
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod() != null){
				writePlazoDeObtencionSolicitudes(entry, entry_conn);
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePlazoDeObtencionPliegos(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newPlazoDeObtencion(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setInt("tipo_plazo", PLAZO_PLIEGOS);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndDate() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndTime() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getDocumentAvailabilityPeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			sentencia.setString("observaciones", null);
			
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePlazoDeObtencionOferta(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newPlazoDeObtencion(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setInt("tipo_plazo", PLAZO_OFERTA);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndDate() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndTime() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getDescription() != null){
				String cadena = entry.getContractFolderStatus().getTenderingProcess().getTenderSubmissionDeadlinePeriod().getDescription();
				sentencia.setString("observaciones", cadena);
			}else{
				sentencia.setString("observaciones", null);
			}
			
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePlazoDeObtencionSolicitudes(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newPlazoDeObtencion(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setInt("tipo_plazo", PLAZO_SOLICITUDES);
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndDate() != null){
				sentencia.setDate("fecha", entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndDate());
			}else{
				sentencia.setDate("fecha", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndTime() != null){
				sentencia.setTime("hora", entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getEndTime());
			}else{
				sentencia.setTime("hora", null);
			}
			
			if (entry.getContractFolderStatus().getTenderingProcess().getParticipationRequestReceptionPeriod().getDescription() != null){
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
				throw e;
			}
		}
	}
	private void writeExtensionDeContrato(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getProcurementProject().getContractExtension() != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newExtensionDeContrato(?, ?, ?)}");
			
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
			
				sentencia.setString("opciones", entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionsDescription());
				if (entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionValidityPeriod() != null){
					sentencia.setString("periodo_de_validez", entry.getContractFolderStatus().getProcurementProject().getContractExtension().getOptionValidityPeriod().getDescription().trim()); 		
				}else{
					sentencia.setString("periodo_de_validez", null);
				}
				sentencia.execute();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeCondicionesDeLicitacion(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCondicionesDeLicitacion(?, ?, ?, ?, ?, ?)}");
			
			// Plazo de obteción de PLIEGOS
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
		
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
				throw e;
			}
		}
	}
	private void writeGarantias(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			RequiredFinancialGuarantee[] rfg = entry.getContractFolderStatus().getTenderingTerms().getRequiredFinancialGuaranteeList();
			
			if (rfg != null){
				for (int i = 0; i < rfg.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newGarantia(?, ?, ?, ?, ?)}");
					
					sentencia.setInt("feeds_expedientes", feeds_expedientes);
					
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
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}	
	}
	private void writeGarantias_Unico(RequiredFinancialGuarantee rfg, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newGarantia(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			
			// Primero insertamos la garantía, y luego la relación con ids
			sentencia.setInt("guarantee_type_code", rfg.getGuaranteeTypeCode());
			
			if (rfg.getLiabilityAmount() > 0){
				sentencia.setDouble("importe", rfg.getLiabilityAmount());
				sentencia.setString("moneda", rfg.getLiabilityAmountCurrencyID());
			}else{
				sentencia.setNull("importe", java.sql.Types.NULL);
				sentencia.setString("moneda", null);
			}
			
			if (rfg.getAmountRate() > 0){
				sentencia.setDouble("porcentaje", rfg.getAmountRate());
			}else{
				sentencia.setNull("porcentaje", java.sql.Types.NULL);
			}
			
			sentencia.execute();
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}	
	}
	private void writeRequisitosDeParticipacion(Entry entry, Connection entry_conn) throws SQLException{
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
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				
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
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeCriterioDeEvaluacion(Entry entry, Connection entry_conn) throws SQLException{
		if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest() != null){
			if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getTechnicalEvaluationCriteriaList() != null){
				// CRITERIO TECNICO
				TechnicalEvaluationCriteria[] tec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getTechnicalEvaluationCriteriaList();
				if (tec != null){
					for (int i = 0; i < tec.length; i++){
						writeCriterioDeEvaluacionTecnica(tec[i], entry_conn);
					}
				}
			}
			
			if (entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getFinancialEvaluationCriteriaList() != null){	
				// CRITERIO ECONOMICO-FINANCIERO
				FinancialEvaluationCriteria[] fec = entry.getContractFolderStatus().getTenderingTerms().getTendererQualificationRequest().getFinancialEvaluationCriteriaList();
				if (fec != null){
					for (int i = 0; i < fec.length; i++){
						writeCriterioDeEvaluacionEconomica(fec[i], entry_conn);
					}
				}
			}	
		}
	}
	private void writeCriterioDeEvaluacionTecnica(TechnicalEvaluationCriteria tec, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			// CRITERIO TECNICO
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeEvaluacion(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setString("descripcion", tec.getDescription());
			sentencia.setInt("tipo_evaluacion", EVALUACION_TECNICA);
			sentencia.setString("tipo_technical", tec.getEvaluationCriteriaTypeCode());
			sentencia.setNull("tipo_financial", java.sql.Types.NULL);
			
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeCriterioDeEvaluacionEconomica(FinancialEvaluationCriteria fec, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			// CRITERIO TECNICO
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeEvaluacion(?, ?, ?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setString("descripcion", fec.getDescription());
			sentencia.setInt("tipo_evaluacion", EVALUACION_ECONOMICA_FINANCIERA);
			sentencia.setNull("tipo_technical", java.sql.Types.NULL);
			sentencia.setString("tipo_financial", fec.getEvaluationCriteriaTypeCode());
			
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeSubcontratacionPermitida(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getTenderingTerms().getAllowedSubcontractTerms() != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newSubcontratacionPermitida(?, ?, ?)}");
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				
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
				throw e;
			}
		}
	}
	private void writeCriterioDeAdjudicacion(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			if (entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms() != null){
				AwardingCriteria[] ac = entry.getContractFolderStatus().getTenderingTerms().getAwardingTerms().getAwardingCriteriaList();
				
				if (ac != null){
					for (int i = 0; i < ac.length; i++){
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeAdjudicacion(?, ?, ?)}");
						
						sentencia.setInt("feeds_expedientes", feeds_expedientes);
						sentencia.setString("descripcion", ac[i].getDescription());
						
						if (ac[i].getWeightNumeric() > 0){
							sentencia.setDouble("ponderacion", ac[i].getWeightNumeric());
						}else{
							sentencia.setNull("ponderacion", java.sql.Types.NULL);
						}
						
						sentencia.execute();
					}
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeCriterioDeAdjudicacion_Unico(AwardingCriteria ac, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCriterioDeAdjudicacion(?, ?, ?)}");
			
			sentencia.setInt("feeds_expedientes", feeds_expedientes);
			sentencia.setString("descripcion", ac.getDescription());
			
			if (ac.getWeightNumeric() > 0){
				sentencia.setDouble("ponderacion", ac.getWeightNumeric());
			}else{
				sentencia.setNull("ponderacion", java.sql.Types.NULL);
			}
			
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeResultadoDelProcedimiento(Entry entry, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		PreparedStatement sentencia_busqueda = null;
		
		try {
			TenderResult[] tr = entry.getContractFolderStatus().getTenderResultList();
			if (tr != null){
				// Miramos si estamos en un expediente con lotes o no
				sentencia_busqueda = entry_conn.prepareStatement("SELECT num_lotes FROM tbl_expedientes WHERE expedientes = ?");
				sentencia_busqueda.setInt(1, Integer.parseInt(entry.getId()));
				
				ResultSet rs = sentencia_busqueda.executeQuery();
				rs.next();
				
				if (rs.getInt("num_lotes") > 0){
					for (int i = 0; i < tr.length; i++){
						int adjudicatario = -1;
						
						// Adjudicatario
						WinningParty wp = tr[i].getWinningParty();
						if (wp != null){
							adjudicatario = writeAdjudicatario(wp, tr[i].getSMEAwardedIndicator(), entry_conn);
						}
						
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newResultadoDelProcedimiento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
						
						sentencia.setInt("feeds_expedientes", feeds_expedientes);
						sentencia.setInt("result_code", tr[i].getResultCode());
						
						if (tr[i].getDescription() != null){
							sentencia.setString("motivacion", tr[i].getDescription());
						}else{
							sentencia.setString("motivacion", null);
						}
						
						if (adjudicatario != -1){
							sentencia.setInt("adjudicatario", adjudicatario);
						}else{
							sentencia.setNull("adjudicatario", java.sql.Types.NULL);
						}
						
						sentencia.setDate("fecha_acuerdo", tr[i].getAwardDate());
						
						sentencia.setDouble("ofertas_recibidas", tr[i].getReceivedTenderQuantity());
						
						if(tr[i].getLowerTenderAmount() >= 0){
							sentencia.setDouble("precio_oferta_mas_baja", tr[i].getLowerTenderAmount());
						}else{
							sentencia.setNull("precio_oferta_mas_baja", java.sql.Types.NULL);
						}
						
						if(tr[i].getHigherTenderAmount() >= 0){
							sentencia.setDouble("precio_oferta_mas_alta", tr[i].getHigherTenderAmount());
						}else{
							sentencia.setNull("precio_oferta_mas_alta", java.sql.Types.NULL);
						}
						
						sentencia.setBoolean("excluidos", tr[i].getAbnormallyLowTenderIndicator());
						sentencia.setString("numero_de_lote", tr[i].getAwardedTenderedProject().getProcurementProjectLotID());
						
						sentencia.execute();
						
						int resultado = sentencia.getInt("resultado_del_procedimiento");
						
						sentencia.close();
						
						// Información sobre el contrato
						Contract[] c = tr[i].getContractList();
						if (c != null){
							writeInformacionDelContrato(resultado, tr[i].getStartDate(), c, entry_conn);
						}
						
						// Importe de adjudicación
						if (tr[i].getAwardedTenderedProject() != null && tr[i].getAwardedTenderedProject().getLegalMonetaryTotalList() != null){
							LegalMonetaryTotal[] lmt = tr[i].getAwardedTenderedProject().getLegalMonetaryTotalList();
							for(int j = 0; j < lmt.length; j++){
								writeImporteDeAdjudicacion(resultado, lmt[j].getPayableAmount(), lmt[j].getTaxExclusiveAmount(), lmt[j].getCurrencyID(), entry_conn);
							}
						}
						
						// Condiciones de subcontratación
						if (tr[i].getSubcontractTerms() != null){
							writeCondicionesDeSubcontratacion(resultado, tr[i].getSubcontractTerms(), entry_conn);
						}
					}
				}else{
					for (int i = 0; i < tr.length; i++){
						int adjudicatario = -1;
						
						// Adjudicatario
						WinningParty wp = tr[i].getWinningParty();
						if (wp != null){
							adjudicatario = writeAdjudicatario(wp, tr[i].getSMEAwardedIndicator(), entry_conn);
						}
						
						sentencia = (CallableStatement) entry_conn.prepareCall("{call newResultadoDelProcedimiento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
						
						sentencia.setInt("feeds_expedientes", feeds_expedientes);
						sentencia.setInt("result_code", tr[i].getResultCode());
						
						if (tr[i].getDescription() != null){
							sentencia.setString("motivacion", tr[i].getDescription());
						}else{
							sentencia.setString("motivacion", null);
						}
						
						if (adjudicatario != -1){
							sentencia.setInt("adjudicatario", adjudicatario);
						}else{
							sentencia.setNull("adjudicatario", java.sql.Types.NULL);
						}
						
						sentencia.setDate("fecha_acuerdo", tr[i].getAwardDate());
						
						sentencia.setDouble("ofertas_recibidas", tr[i].getReceivedTenderQuantity());
						
						if(tr[i].getLowerTenderAmount() >= 0){
							sentencia.setDouble("precio_oferta_mas_baja", tr[i].getLowerTenderAmount());
						}else{
							sentencia.setNull("precio_oferta_mas_baja", java.sql.Types.NULL);
						}
						
						if(tr[i].getHigherTenderAmount() >= 0){
							sentencia.setDouble("precio_oferta_mas_alta", tr[i].getHigherTenderAmount());
						}else{
							sentencia.setNull("precio_oferta_mas_alta", java.sql.Types.NULL);
						}
						
						sentencia.setBoolean("excluidos", tr[i].getAbnormallyLowTenderIndicator());
						sentencia.setNull("numero_de_lote", java.sql.Types.NULL);
						
						sentencia.execute();
						
						int resultado = sentencia.getInt("resultado_del_procedimiento");
						
						sentencia.close();
						
						// Información sobre el contrato
						Contract[] c = tr[i].getContractList();
						if (c != null){
							writeInformacionDelContrato(resultado, tr[i].getStartDate(), c, entry_conn);
						}
						
						// Importe de adjudicación
						if (tr[i].getAwardedTenderedProject() != null && tr[i].getAwardedTenderedProject().getLegalMonetaryTotalList() != null){
							LegalMonetaryTotal[] lmt = tr[i].getAwardedTenderedProject().getLegalMonetaryTotalList();
							for(int j = 0; j < lmt.length; j++){
								writeImporteDeAdjudicacion(resultado, lmt[j].getPayableAmount(), lmt[j].getTaxExclusiveAmount(), lmt[j].getCurrencyID(), entry_conn);
							}
						}
						
						// Condiciones de subcontratación
						if (tr[i].getSubcontractTerms() != null){
							writeCondicionesDeSubcontratacion(resultado, tr[i].getSubcontractTerms(), entry_conn);
						}
					}
				}
			}
			
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeResultadoDelProcedimiento_Unico(TenderResult tr, Connection entry_conn, String loteId) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			if (tr != null){
				int adjudicatario = -1;
				
				// Adjudicatario
				WinningParty wp = tr.getWinningParty();
				if (wp != null){
					adjudicatario = writeAdjudicatario(wp, tr.getSMEAwardedIndicator(), entry_conn);
				}
				
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newResultadoDelProcedimiento(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				sentencia.setInt("result_code", tr.getResultCode());
				
				if (tr.getDescription() != null){
					sentencia.setString("motivacion", tr.getDescription());
				}else{
					sentencia.setString("motivacion", null);
				}
				
				if (adjudicatario != -1){
					sentencia.setInt("adjudicatario", adjudicatario);
				}else{
					sentencia.setNull("adjudicatario", java.sql.Types.NULL);
				}
				
				sentencia.setDate("fecha_acuerdo", tr.getAwardDate());
				
				if (tr.getReceivedTenderQuantity() >= 0){
					sentencia.setDouble("ofertas_recibidas", tr.getReceivedTenderQuantity());
				}else{
					sentencia.setNull("ofertas_recibidas", java.sql.Types.NULL);
				}
				
				if(tr.getLowerTenderAmount() >= 0){
					sentencia.setDouble("precio_oferta_mas_baja", tr.getLowerTenderAmount());
				}else{
					sentencia.setNull("precio_oferta_mas_baja", java.sql.Types.NULL);
				}
				
				if(tr.getHigherTenderAmount() >= 0){
					sentencia.setDouble("precio_oferta_mas_alta", tr.getHigherTenderAmount());
				}else{
					sentencia.setNull("precio_oferta_mas_alta", java.sql.Types.NULL);
				}
				
				sentencia.setBoolean("excluidos", tr.getAbnormallyLowTenderIndicator());
				
				if (loteId == null){
					sentencia.setNull("numero_de_lote", java.sql.Types.NULL);
				}else{
					sentencia.setString("numero_de_lote", loteId);
				}
				
				sentencia.execute();
				
				int resultado = sentencia.getInt("resultado_del_procedimiento");
				
				sentencia.close();
				
				// Información sobre el contrato
				Contract[] c = tr.getContractList();
				if (c != null){
					writeInformacionDelContrato(resultado, tr.getStartDate(), c, entry_conn);
				}
				
				// Importe de adjudicación
				if (tr.getAwardedTenderedProject() != null && tr.getAwardedTenderedProject().getLegalMonetaryTotalList() != null){
					LegalMonetaryTotal[] lmt = tr.getAwardedTenderedProject().getLegalMonetaryTotalList();
					for(int j = 0; j < lmt.length; j++){
						writeImporteDeAdjudicacion(resultado, lmt[j].getPayableAmount(), lmt[j].getTaxExclusiveAmount(), lmt[j].getCurrencyID(), entry_conn);
					}
				}
				
				// Condiciones de subcontratación
				if (tr.getSubcontractTerms() != null){
					writeCondicionesDeSubcontratacion(resultado, tr.getSubcontractTerms(), entry_conn);
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeInformacionDelContrato(int resultado, java.sql.Date startDate, Contract[] c, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			for (int i = 0; i < c.length; i++){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newContrato(?, ?, ?, ?)}");
				
				sentencia.setInt("resultado_del_procedimiento", resultado);
				
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
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private int writeAdjudicatario(WinningParty wp, boolean pyme, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		PreparedStatement sentencia_busqueda = null;
		int adjudicatario = -1;
		
		// Adjudicatorio
		try {
			// Buscamos el adjudicatario, si existe se añade la referencia, si no existe se crea y se referencia
			String schemeName = wp.getPartyIdentificationList()[0].getSchemeName();
			String id = wp.getPartyIdentificationList()[0].getId();
			
			sentencia_busqueda = entry_conn.prepareStatement("SELECT adjudicatario FROM tbl_adjudicatario WHERE " + schemeName + " = '" + id + "'");
			ResultSet rs = sentencia_busqueda.executeQuery();
			
			if(rs.next()){
				adjudicatario = rs.getInt("adjudicatario");
			}else{
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newAdjudicatario(?, ?, ?, ?, ?, ?)}");
				
				sentencia.setString("nombre", wp.getPartyName().getName());
				sentencia.setBoolean("pyme", pyme);
				
				if (schemeName.compareTo("NIF") == 0){
					sentencia.setString("NIF", id);
					sentencia.setNull("UTE", java.sql.Types.NULL);
					sentencia.setNull("OTROS", java.sql.Types.NULL);
				}else if (schemeName.compareTo("UTE") == 0){
					sentencia.setString("UTE", id);
					sentencia.setNull("NIF", java.sql.Types.NULL);
					sentencia.setNull("OTROS", java.sql.Types.NULL);
				}else if (schemeName.compareTo("OTROS") == 0){
					sentencia.setString("OTROS", id);
					sentencia.setNull("UTE", java.sql.Types.NULL);
					sentencia.setNull("NIF", java.sql.Types.NULL);
				}
				
				sentencia.execute();
				
				adjudicatario = sentencia.getInt("adjudicatario");
			}
			
			return adjudicatario;
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
				if (sentencia_busqueda != null) sentencia_busqueda.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeImporteDeAdjudicacion(int resultado, double con_imp, double sin_imp, String currencyID, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newImportesDeAdjudicacion(?, ?, ?, ?)}");
			sentencia.setInt("resultado_del_procedimiento", resultado);
			sentencia.setDouble("total_sin_impuestos", sin_imp);
			sentencia.setDouble("total_con_impuestos", con_imp);
			sentencia.setString("currencyID", currencyID);
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeCondicionesDeSubcontratacion(int resultado, SubcontractTerms s, Connection entry_conn) throws SQLException {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) entry_conn.prepareCall("{call newCondicionesDeSubcontratacion(?, ?, ?)}");
			sentencia.setInt("resultado_del_procedimiento", resultado);
			if (s.getDescription() != null){
				sentencia.setString("descripcion", s.getDescription());
			}else{
				sentencia.setString("descripcion", null);
			}
			if (s.getRate() >= 0){
				sentencia.setDouble("porcentaje", s.getRate());
			}else{
				sentencia.setNull("porcentaje", java.sql.Types.NULL);
			}
			sentencia.execute();
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeJustificacionDelProceso(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
	
		try {
			if (entry.getContractFolderStatus().getTenderingProcess().getProcessJustification() != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newJustificacionDelProceso(?, ?, ?)}");
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				
				if (entry.getContractFolderStatus().getTenderingProcess().getProcessJustification().getReasonCode() != null){
					sentencia.setString("codigo_de_motivo", entry.getContractFolderStatus().getTenderingProcess().getProcessJustification().getReasonCode());
				}else{
					sentencia.setString("codigo_de_motivo", null);
				}
				
				if (entry.getContractFolderStatus().getTenderingProcess().getProcessJustification().getDescription() != null){
					sentencia.setString("descripcion", entry.getContractFolderStatus().getTenderingProcess().getProcessJustification().getDescription());
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
				throw e;
			}
		}
	}
	private void writeModificacionesDeContrato(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			ContractModification[] cm = entry.getContractFolderStatus().getContractModificationList();
			if (cm != null){
				for (int i = 0; i < cm.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newModificacionesDeContrato(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
					
					sentencia.setInt("feeds_expedientes", feeds_expedientes);
					sentencia.setString("numero_de_contrato", cm[i].getContractID());
					sentencia.setInt("numero_de_modificacion", cm[i].getID());
					
					if (cm[i].getContractModificationLegalMonetaryTotal() != null){
						sentencia.setDouble("importe_modificacion", cm[i].getContractModificationLegalMonetaryTotal().getTaxExclusiveAmount());
						sentencia.setString("currencyID", cm[i].getContractModificationLegalMonetaryTotal().getCurrencyID());
					}else{
						sentencia.setNull("importe_modificacion", java.sql.Types.NULL);
					}
					
					if (cm[i].getFinalLegalMonetaryTotal() != null){
						sentencia.setDouble("importe_contrato", cm[i].getFinalLegalMonetaryTotal().getTaxExclusiveAmount());
						sentencia.setString("currencyID", cm[i].getFinalLegalMonetaryTotal().getCurrencyID());
					}else{
						sentencia.setNull("importe_contrato", java.sql.Types.NULL);
						sentencia.setString("currencyID", null);
					}
					
					if (cm[i].getContractModificationDurationMeasure() > 0){
						sentencia.setInt("plazo_modificacion", cm[i].getContractModificationDurationMeasure());
						sentencia.setString("plazo_modificacion_unit_code", cm[i].getContractModificationDurationMeasureUnitCode());
					}else{
						sentencia.setNull("plazo_modificacion", java.sql.Types.NULL);
						sentencia.setString("plazo_modificacion_unit_code", null);
					}
					
					if (cm[i].getFinalDurationMeasure() > 0){
						sentencia.setInt("plazo_total", cm[i].getFinalDurationMeasure());
						sentencia.setString("plazo_total_unit_code", cm[i].getFinalDurationMeasureUnitCode());
					}else{
						sentencia.setNull("plazo_total", java.sql.Types.NULL);
						sentencia.setString("plazo_total_unit_code", null);
					}
					
					if (cm[i].getNote() != null){
						sentencia.setString("observaciones", cm[i].getNote());
					}else{
						sentencia.setString("observaciones", null);
					}
					
					sentencia.execute();
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writeModificacionesDeContrato_Unico(ContractModification cm, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (cm != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newModificacionesDeContrato(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				sentencia.setString("numero_de_contrato", cm.getContractID());
				sentencia.setInt("numero_de_modificacion", cm.getID());
				
				if (cm.getContractModificationLegalMonetaryTotal() != null){
					sentencia.setDouble("importe_modificacion", cm.getContractModificationLegalMonetaryTotal().getTaxExclusiveAmount());
					sentencia.setString("currencyID", cm.getContractModificationLegalMonetaryTotal().getCurrencyID());
				}else{
					sentencia.setNull("importe_modificacion", java.sql.Types.NULL);
				}
				
				if (cm.getFinalLegalMonetaryTotal() != null){
					sentencia.setDouble("importe_contrato", cm.getFinalLegalMonetaryTotal().getTaxExclusiveAmount());
					sentencia.setString("currencyID", cm.getFinalLegalMonetaryTotal().getCurrencyID());
				}else{
					sentencia.setNull("importe_contrato", java.sql.Types.NULL);
					sentencia.setString("currencyID", null);
				}
				
				if (cm.getContractModificationDurationMeasure() > 0){
					sentencia.setInt("plazo_modificacion", cm.getContractModificationDurationMeasure());
					sentencia.setString("plazo_modificacion_unit_code", cm.getContractModificationDurationMeasureUnitCode());
				}else{
					sentencia.setNull("plazo_modificacion", java.sql.Types.NULL);
					sentencia.setString("plazo_modificacion_unit_code", null);
				}
				
				if (cm.getFinalDurationMeasure() > 0){
					sentencia.setInt("plazo_total", cm.getFinalDurationMeasure());
					sentencia.setString("plazo_total_unit_code", cm.getFinalDurationMeasureUnitCode());
				}else{
					sentencia.setNull("plazo_total", java.sql.Types.NULL);
					sentencia.setString("plazo_total_unit_code", null);
				}
				
				if (cm.getNote() != null){
					sentencia.setString("observaciones", cm.getNote());
				}else{
					sentencia.setString("observaciones", null);
				}
				
				sentencia.execute();
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePublicacionesOficiales(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			ValidNoticeInfo[] vni = entry.getContractFolderStatus().getValidNoticeInfoList();
			if (vni != null){
				for (int i = 0; i < vni.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newPublicacionesOficiales(?, ?, ?)}");
					sentencia.setInt("feeds_expedientes", feeds_expedientes);
					sentencia.setString("tipo_de_anuncio", vni[i].getNoticeTypeCode());
					
					sentencia.execute();
					int publicacion = sentencia.getInt("publicacion");
					writeAdditionalPublicationStatus(publicacion, vni[i], entry_conn);
				}
			}
		} finally {
			// Cerramos las conexiones
			try {
				if (sentencia != null) sentencia.close();
			} catch (SQLException e) {
				throw e;
			}
		}
	}
	private void writePublicacionesOficiales_Unico(ValidNoticeInfo vni, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (vni != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newPublicacionesOficiales(?, ?, ?)}");
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				sentencia.setString("tipo_de_anuncio", vni.getNoticeTypeCode());
				
				sentencia.execute();
				int publicacion = sentencia.getInt("publicacion");
				writeAdditionalPublicationStatus(publicacion, vni, entry_conn);
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
	private void writeAdditionalPublicationStatus(int publicaciones_oficiales, ValidNoticeInfo v, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			AdditionalPublicationStatus[] aps = v.getAdditionalPublicationStatusList();
			if (aps != null){
				for (int i = 0; i < aps.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newAdditionalPublicationStatus(?, ?, ?)}");
					sentencia.setInt("publicaciones_oficiales", publicaciones_oficiales);
					sentencia.setString("medio_de_publicacion", aps[i].getPublicationMediaName());
					
					sentencia.execute();
					int publication_status = sentencia.getInt("publication_status");
					sentencia.close();
					
					// Fecha envío y Acta Adjunta
					AdditionalPublicationDocumentReference[] apdr = aps[i].getAdditionalPublicationDocumentReferenceList();
					if (apdr != null){
						for (int j = 0; j < apdr.length; j++){
							if (apdr[j].getIssueDate() != null){
								sentencia = (CallableStatement) entry_conn.prepareCall("{call newFechaEnvio(?, ?)}");
								sentencia.setDate("fecha", apdr[j].getIssueDate());
								sentencia.setInt("additional_publication_status", publication_status);
								sentencia.execute();
								sentencia.close();
							}else if (apdr[j].getAttachment() != null){
								sentencia = (CallableStatement) entry_conn.prepareCall("{call newActaAdjunta(?, ?, ?, ?)}");
								sentencia.setInt("additional_publication_status", publication_status);
								sentencia.setString("document_type_code", apdr[j].getDocumentTypeCode());
								if (apdr[j].getAttachment().getExternalReference().getURI() != null){
									sentencia.setString("URI", apdr[j].getAttachment().getExternalReference().getURI());
								}else{
									sentencia.setString("URI", null);
								}
								if (apdr[j].getAttachment().getExternalReference().getFileName() != null){
									sentencia.setString("file_name", apdr[j].getAttachment().getExternalReference().getFileName());
								}else{
									sentencia.setString("file_name", null);
								}
							}
						}
					}
					
					// Fecha publicación
					AdditionalPublicationRequest[] apr = aps[i].getAdditionalPublicationRequestList();
					if (apr != null){
						for (int j = 0; j < apr.length; j++){
							sentencia = (CallableStatement) entry_conn.prepareCall("{call newFechaPublicacion(?, ?)}");
							sentencia.setDate("fecha", apr[j].getSendDate());
							sentencia.setInt("additional_publication_status", publication_status);
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
	private void writeOtrosDocumentos(Entry entry, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			GeneralDocument[] gd = entry.getContractFolderStatus().getGeneralDocumentList();
			if (gd != null){
				for (int i = 0; i < gd.length; i++){
					sentencia = (CallableStatement) entry_conn.prepareCall("{call newOtrosDocumentos(?, ?, ?, ?)}");
					
					sentencia.setInt("feeds_expedientes", feeds_expedientes);
					sentencia.setString("ID", gd[i].getGeneralDocumentDocumentReference().getId());
					
					if (gd[i].getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getURI() != null){
						sentencia.setString("URI", gd[i].getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getURI());
					}else{
						sentencia.setString("URI", null);
					}
					
					if (gd[i].getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getFileName() != null){
						sentencia.setString("file_name", gd[i].getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getFileName());
					}else{
						sentencia.setString("file_name", null);
					}
					
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
	private void writeOtrosDocumentos_Unico(GeneralDocument gd, Connection entry_conn) throws SQLException{
		CallableStatement sentencia = null;
		
		try {
			if (gd != null){
				sentencia = (CallableStatement) entry_conn.prepareCall("{call newOtrosDocumentos(?, ?, ?, ?)}");
				
				sentencia.setInt("feeds_expedientes", feeds_expedientes);
				sentencia.setString("ID", gd.getGeneralDocumentDocumentReference().getId());
				
				if (gd.getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getURI() != null){
					sentencia.setString("URI", gd.getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getURI());
				}else{
					sentencia.setString("URI", null);
				}
				
				if (gd.getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getFileName() != null){
					sentencia.setString("file_name", gd.getGeneralDocumentDocumentReference().getAttachment().getExternalReference().getFileName());
				}else{
					sentencia.setString("file_name", null);
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
	/* AUXILIARES */
	
	private boolean searchExpediente(int id) throws Exception {
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
	
	public Timestamp getLastUpdateDate() throws Exception{
		PreparedStatement sentencia = null;
		Connection conn = conectarMySQL();
		Timestamp fecha = null;
		
		try {
			sentencia = conn.prepareStatement("SELECT DISTINCT updated FROM tbl_feeds ORDER BY updated DESC LIMIT 1");
			ResultSet rs = sentencia.executeQuery();
			rs.next();
			fecha = rs.getTimestamp(1);
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
		
		return fecha;
	}
	
	/** TYPE CODES */
    
	public void writeSubTypeCode(int code, String nombre, int tipo) throws Exception {
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
	public void writeCPVCode(int code, String nombre) throws Exception {
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
	public void writeCountryIdentificationCode(String code, String nombre) throws Exception{
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
	public void writeCountrySubentityCode(String code, String nombre) throws Exception{
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
	public void writeProcedureCode(int code, String nombre) throws Exception{
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
	public void writeContractingSystemTypeCode(int code, String nombre) throws Exception{
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
	public void writeUrgencyCode(int code, String nombre) throws Exception{
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
	public void writeSubmissionMethodCode(int code, String nombre) throws Exception{
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
	public void writeLanguage(String code, String nombre) throws Exception{
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
	public void writeProcurementLegislation(String code, String nombre) throws Exception{
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
	public void writeContractingPartyTypeCode(String code, String nombre) throws Exception {
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
	public void writeModosId(int code, String descripcion) throws Exception{
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
	public void writeContractFolderStatusCode(String code, String nombre, int orden) throws Exception {
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newContractFolderStatusCode(?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("code", code);
			sentencia.setString("nombre", nombre);
			sentencia.setInt("orden", orden);
			
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
	public void writeTypèCode(int code, String nombre) throws Exception {
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
	public void writeTipoPliego(int id, String tipo) throws Exception{
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
	public void writeTipoPlazo(int id, String tipo) throws Exception{
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
	public void writeFundingProgramCode(String code, String nombre) throws Exception{
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
	public void writeGuarateeTypeCode(int code, String nombre) throws Exception{
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
	public void writeRequiredBusinessProfileCode(String code, String nombre) throws Exception{
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
	public void writeDeclarationTypeCode(int code, String nombre) throws Exception{
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
	public void writeTechnicalCapabilityTypeCode(String code, String nombre) throws Exception {
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
	public void writeFinancialCapabilityTypeCode(String code, String nombre) throws Exception {
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
	public void writeTipoEvaluacion(int tipo, String descripcion) throws Exception{
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
	public void writeTenderResultCode(int code, String nombre) throws Exception {
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
	public void writeReasonCode(String code, String nombre) throws Exception{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newReasonCode(?, ?)}");
			
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
	public void writeNoticeTypeCode(String code, String nombre) throws Exception{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newNoticeTypeCode(?, ?)}");
			
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
	public void writeDocumentTypeCode(String code, String nombre) throws Exception{
		Connection conn = conectarMySQL();
		
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newDocumentTypeCode(?, ?)}");
			
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
}
