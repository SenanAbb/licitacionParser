import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.mysql.cj.jdbc.CallableStatement;

import Conexion.ConexionSQL;
import Parser.Parser;

public class main {
//	private static final int MODO_AUTOMATICO = 1; 
	private static final int MODO_MANUAL = 2;
	private static final int POS_UNICO_ELEMENTO = 0; 
	
	private static int ids, feeds;
	private static boolean primera_lectura;
	private static Timestamp fecha_limite, atom_update;
	private static String nextLink, selfLink;
	
	public static void main(String[] args) throws Exception{
		// MOFICACIONES PARA QUE EL PARSER SOLO SE ENCARGUE DE LEER Y ESCRIBIR LAS ENTRYS DE UN ATOM
		// No se encarga de crear los registros de lectura ni setear los links y hacer comprobaciones
		// para saber hasta cuando/donde leer, es decir, UNICAMENTE va a leer los expedientes. Las comprobaciones
		// se harán desde el main.
		
		// Al terminar una ejecución del Parser hay que apuntarlo a null para eliminar de memoria todos los objetos
		// utilizados y definidos por el Parser. Esto se hace para evitar un OutOfMemoryException, es decir, que el Heap
		// de la MV de Java se quede sin memoria y podamos leer ininterrumpidamente.
		
		// EL RECOLECTOR DE BASURA pasará cada vez completemos una vuelta del bucle. Aunque deba pasar automáticamente, 
		// se hará una llamada de forma manual para asegurar su ejecución.
		
		// Comprobamos si es primera lectura o no
		primera_lectura = esPrimeraLectura();
		
		// Creamos el IDS de lectura
		createIds();
		
		// Seteamos la FECHA LIMITE para la lectura dependiendo de si es o no primera_lectura
		setFechaLimite(primera_lectura);
		
		// Seteamos el ATOM para la lectura (la primera)
		String URL = "https://contrataciondelsectorpublico.gob.es/sindicacion/sindicacion_643/licitacionesPerfilesContratanteCompleto3.atom";
		
		// Iniciamos el Document
		URLConnection conexion;
		URL url = new URL(URL);
        
		conexion = url.openConnection();
        conexion.connect();
     
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder db = dbf.newDocumentBuilder();
        
        Document doc = db.parse(conexion.getInputStream());
		
		// Recogemos el NextLink y el Update del ATOM
		readUpdateDate(doc);
		readLinks(doc);
		
		/// WHILE update > fecha_limite
		// Parseamos el ATOM y escribimos
			// Mientras UPDATE sea mayor a la fecha limite repetimos con nextLink
		while (fecha_limite.before(atom_update)){
			createFeeds(url.toString());
			
			System.out.println("URL: " + url.toString());
			System.out.println("ATOM UPDATE: " + atom_update);
			
			logMemoria();
			
			Parser p = new Parser();
			
			System.out.print("\tLEYENDO ENTRYS (");
			
			p.setFeeds(feeds);
			p.setUpdated(atom_update);
			p.setSelfLink(selfLink);
			
			p.readEntries(primera_lectura, doc);
			
			p = null;
			url = null;
			doc = null;
			
	        url = new URL(nextLink);
	        conexion = url.openConnection();
	        conexion.connect();
	        
	        doc = db.parse(conexion.getInputStream());
	        
	        // Recogemos el updated y el link next
	 		readUpdateDate(doc);
	 		readLinks(doc);
		}
		/// ENDWHILE
	}
	
	private static void createFeeds(String URL) throws Exception{
		ConexionSQL sql = new ConexionSQL();
		Connection conn = sql.conectarMySQL();
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) conn.prepareCall("{call newFeeds(?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setInt("ids", ids);
			sentencia.setString("URL", URL);
			sentencia.setTimestamp("updated", atom_update);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("feeds", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			feeds = sentencia.getInt("feeds");
		} catch (SQLException e) {
			System.out.println("Error para rollback: " + e.getMessage());
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
	
	private static void garbageCollector(){
		Runtime garbage = Runtime.getRuntime();
        System.out.println("Memoria libre antes de limpieza: " + garbage.freeMemory());
 
        garbage.gc();
 
        System.out.println("Memoria libre tras la limpieza: " + garbage.freeMemory());
	}
	
	private static void logMemoria(){
		int dataSize = 1024*1024;
		
		System.out.println("\t[MÁXIMA]: " + Runtime.getRuntime().maxMemory()/dataSize + "MB");
		System.out.println("\t[TOTAL]: " + Runtime.getRuntime().totalMemory()/dataSize + "MB");
		System.out.println("\t[LIBRE]: " + Runtime.getRuntime().freeMemory()/dataSize + "MB");
		System.out.println("\t[USADA]: " + (Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/dataSize + "MB");
	}
	
	private static void readLinks(Document document){
		try {
			// Recogemos primero el ID, para poder modificarlo despues
			NodeList IDs = document.getElementsByTagName("id");
			if(IDs.getLength() > 0){
				// Buscamos la lista de nodos en FEED con la etiqueta link
				NodeList links = document.getElementsByTagName("link");
				
				for (int i = 0; i < links.getLength(); i++){
					// Recogo la información de cada link (href="" rel="")
					String rel = links.item(i).getAttributes().getNamedItem("rel").getTextContent();
					String href = links.item(i).getAttributes().getNamedItem("href").getTextContent();
					
					if (rel.compareTo("next") == 0){
						//String link = rebuildLink(ID, href);
						nextLink = href;
					}else if (rel.compareTo("self") == 0){
						selfLink = href;
					}
				}
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			e.getStackTrace();
		}
	}
	
	private static void readUpdateDate(Document document){
		try {
			//Buscamos la lista de nodos en la raíz (feed) con la etiqueta "updated"
			NodeList nodos = document.getElementsByTagName("updated");
			
			if (nodos.getLength() > 0){
				
				//Como sabemos que solo vamos a encontrar uno, lo almacenamos directamente
				String date= nodos.item(POS_UNICO_ELEMENTO).getTextContent().replace("T", " ");
				date = date.substring(0, date.indexOf("+"));
				
				atom_update = Timestamp.valueOf(date);
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			System.err.println("UPDATED del documento no existe");
		}
	}
	
	private static void setFechaLimite(boolean primera_lectura) throws Exception{
		if (!primera_lectura){
			ConexionSQL conn = new ConexionSQL();
			fecha_limite = conn.getLastUpdateDate();
		}else{
			String fecha = "2020-01-01 00:00:00.000";
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.s");
			Date date = dateFormat.parse(fecha);
			long time = date.getTime();
			
			fecha_limite = new Timestamp(time);
		}
	}
	
	private static boolean esPrimeraLectura() throws Exception{
		boolean primera_lectura = false;
		
		ConexionSQL sql = new ConexionSQL();
		Connection conn = sql.conectarMySQL();
		
		PreparedStatement sentencia = conn.prepareStatement("SELECT * FROM tbl_ids");
		ResultSet rs = sentencia.executeQuery();
		
		if (!rs.next()) primera_lectura = true;
		
		return primera_lectura;
	}
	
	private static void createIds() throws Exception{
		ConexionSQL sql = new ConexionSQL();
		Connection conn = sql.conectarMySQL();
		CallableStatement sentencia = null;
		
		try{
			sentencia = (CallableStatement) conn.prepareCall("{call newIds(?, ?)}");
			
			// Parametro 1 del procedimiento almacenado
			sentencia.setInt("modos_id", MODO_MANUAL);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("ids", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			ids = sentencia.getInt("ids");
		} catch (SQLException e) {
			System.out.println("Error para rollback: " + e.getMessage());
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
	
	private static void rellenar(Parser p) throws Exception{
		// Rellenar las tablas TypeCode
		System.out.println("ModosID"); p.writeModosId();
		System.out.println("TypeCode"); p.writeTypeCode();
		System.out.println("SubtypeCode"); p.writeSubtypeCodes();
		System.out.println("CPV"); p.writeCPV();
		System.out.println("CountryID"); p.writeCountryIdentificationCode();
		System.out.println("CountrySubID"); p.writeCountrySubentityCode();
		System.out.println("ProcedureCode"); p.writeProcedureCode();
		System.out.println("ContractingSystem"); p.writeContractingSystemTypeCode();
		System.out.println("Urgency"); p.writeUrgencyCode();
		System.out.println("Submission"); p.writeSubmissionMethodCode();
		System.out.println("Language"); p.writeLanguage();
		System.out.println("Procurement"); p.writeProcurementLegislation();
		System.out.println("ContractingParty"); p.writeContractingPartyTypeCode();
		System.out.println("ContractFolderStatus"); p.writeContractFolderStatusCode();
		System.out.println("TipoPliego"); p.writeTipoPliego();
		System.out.println("TipoPlazo"); p.writeTipoPlazo();
		System.out.println("FundingProgram"); p.writeFundingProgramCode();
		System.out.println("Guarantee"); p.writeGuaranteeTypeCode();
		System.out.println("RequiredBusiness"); p.writeRequiredBusinessProfileCode();
		System.out.println("Declaration"); p.writeDeclarationTypeCode();
		System.out.println("Technical"); p.writeTechnicalCapabilityTypeCode();
		System.out.println("Financial"); p.writeFinancialCapabilityTypeCode();
		System.out.println("TipoEvaluacion"); p.writeTipoEvaluacion();
		System.out.println("TenderResult"); p.writeTenderResultCode();
		System.out.println("ReasonCode"); p.writeReasonCode();
		System.out.println("NoticeTypeCode"); p.writeNoticeTypeCode();
		System.out.println("DocumentTypeCode"); p.writeDocumentTypeCode();
	}
	private static void leerDirectorio(boolean primera_lectura) throws Exception {
		ArrayList<String> exp = new ArrayList<String>();
		//exp.add("PDT.-3.9/19");
		exp.add("PDT.-3.8/19");
		
		/* PARSER DE DIRECTORIO */
		String path = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Licitaciones 20-21/PRUEBAS_LOTES2/";
		String[] files = null;
		
		File f = new File(path);
		File[] listado = f.listFiles();
		
		Parser p = new Parser();
		
		p.createIds();
		p.createFeeds(path);
		try {
			p.setFechaLimite(primera_lectura);
		} catch (ParseException e) {e.printStackTrace();}
		
		if (listado == null || listado.length == 0) {
		    System.out.println("No hay elementos dentro de la carpeta actual");
		}else {
		    for (int i=0; i< listado.length; i++) {
		    	String sub_path = path.concat(listado[i].getName());
		    	
		    	if (!listado[i].isDirectory()){
		    		System.out.println("==============");
                	System.out.println(sub_path);
                	System.out.println("==============");
                	File file = new File(sub_path);
                	p.setURL(file);
                	p.readEntries(primera_lectura, null);           
		    	}else{
		    		System.out.println("----> " + listado[i].getName() + " <-----");
		    		files = getFiles(sub_path);
		    		if ( files != null ) {
		                int size = files.length;
		                for ( int j = 0; j < size; j ++ ) {
		                	System.out.println("==============");
		                	System.out.println("ARCHIVO Nº " + (j+1) + " DE " + size);
		                	System.out.println(files[j]);
		                	System.out.println("==============");
		                	File file = new File(files[j]);
		                	p.setURL(file);
		                	p.readEntries(primera_lectura, null);
		                }
		            }
		    	}
		    }
		}
	}
	private static void leerArchivo(boolean primera_lectura) throws Exception{
		/** PARSER DE UN SOLO ARCHIVO */
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/LicitacionParser/Licitaciones 20-21/PRUEBAS/2.atom";
		//String URL = "Licitaciones 20-21/2020_12/licitacionesPerfilesContratanteCompleto3_20201230_181906_1";
		ArrayList<String> exp = new ArrayList<String>();
		exp.add("PDT.-3.9/19");
		exp.add("PDT.-3.8/19");
		
		Parser p = new Parser();
		p.setURL(new File(URL));
		p.createIds();
		p.readEntries(primera_lectura, null);
	}
	private static String[] getFiles(String path) {
		String[] arr_res = null;

        File f = new File(path);
        if ( f.isDirectory( )) {
            List<String> res   = new ArrayList<>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for ( int i = 0; i < size; i ++ ) {
                if ( arr_content[ i ].isFile( ))
                res.add( arr_content[ i ].toString( ));
            }

            arr_res = res.toArray(new String[0]);
        } else {
        	System.err.println( "¡ Path NO válido !" );
        }
        return arr_res;
	}
}