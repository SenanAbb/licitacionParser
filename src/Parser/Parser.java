package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.mysql.cj.jdbc.CallableStatement;

import Conexion.ConexionSQL;
import Entry.Entry;

/**
 * @params
 * 		const POS_UNICO_ELEMENTO: int[1]
 * 		NIF: String[1]
 *		ULR: File[1]
 *		updated: Date[1]
 *		selfLink: String[1]
 *		nextLink: String[1]
 *		entryCont: int[0..1]
 *		entries: Entry[] [0..*]
 */
public class Parser {
	private static final int POS_UNICO_ELEMENTO = 0; 
	private static final int MODO_AUTOMATICO = 1; 
	private static final int MODO_MANUAL = 2; 
	private static int ids;
	
	private String NIF = "";
	private File URL;
	
	private Timestamp updated;
	private String selfLink, nextLink;
	private int entryCont = 0;
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	private ArrayList<String> expedientes = new ArrayList<String>();
	
	/* modo = [EXP, NIF] */
	private String modo_identificacion;
	
	/* Si escribir = true -> escribirá los datos en la BD */
	private boolean escribir = true;

	public void readEntries(boolean primera_lectura) throws SQLException{
		try {
    		//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder(this.URL);
			
			NodeList entriesNodes = document.getElementsByTagName("entry");
			
			if (entriesNodes.getLength() > 0){
				entryCont += entriesNodes.getLength();
				for (int i = 0; i < entriesNodes.getLength(); i++){
					// Cojo el nodo actual
					Node entry = entriesNodes.item(i);
					// Lo transformo a element
					Element e = (Element) entry;
					
					System.out.println("Leyendo entry " + (i+1) + "/" + entriesNodes.getLength());
					
					String result = "";
					if (modo_identificacion == "NIF"){
						result = getEntryPartyID(e);
						if (result.compareTo(NIF) == 0){
							readAttributesAndWrite(e, primera_lectura);
						}
					}else if (modo_identificacion == "EXP"){
						result = getEntryExp(e);
						if (expedientes.contains(result)){
							readAttributesAndWrite(e, primera_lectura);
						}
					}else{
						readAttributesAndWrite(e, primera_lectura);		
					}
				}
			}else{
				throw new NullPointerException();
			}
		} catch (NullPointerException | ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readAttributesAndWrite(Element e, boolean primera_lectura) throws SQLException {
		String[] idSplit = null;
		String id = null, entryId = null, entryLink = null, entrySummary = null, entryTitle = null;
		Timestamp entryUpdate = null;
		
		Element idElement = (Element) e.getElementsByTagName("id").item(POS_UNICO_ELEMENTO);
		try{
			id = idElement.getTextContent();
			idSplit = idElement.getTextContent().split("/");
			entryId = idSplit[idSplit.length-1];
		}catch (NullPointerException exID){
			System.err.print("ERROR FATAL: Entry -> ID no existe\n");
		}
		
		Element link = (Element) e.getElementsByTagName("link").item(POS_UNICO_ELEMENTO);
		try{
			entryLink = link.getAttributes().getNamedItem("href").getTextContent();
		}catch (NullPointerException exLINK){
			System.err.print("ERROR FATAL: Entry " + entryId + " -> LINK no existe\n");
		}
				
		Element summary = (Element) e.getElementsByTagName("summary").item(POS_UNICO_ELEMENTO);		
		try{
			entrySummary = summary.getTextContent();
		}catch (NullPointerException exSUMMARY){
			System.err.print("ERROR FATAL: Entry " + entryId + " -> SUMMARY no existe\n");
		}
		
		Element title = (Element) e.getElementsByTagName("title").item(POS_UNICO_ELEMENTO);
		try{
			entryTitle = title.getTextContent();
		}catch (NullPointerException exTITLE){
			System.err.print("ERROR FATAL: Entry " + entryId + " -> TITLE no existe\n");
		}
						
		Element update = (Element) e.getElementsByTagName("updated").item(POS_UNICO_ELEMENTO);
		try{
			// Quitamos los espacios y los caracteres que no queremos
			String date = update.getTextContent().replace("T", " ");
			date = date.substring(0, date.indexOf("+"));
			
			entryUpdate = Timestamp.valueOf(date);
		}catch (NullPointerException exUPDATED){
			System.err.print("ERROR FATAL: Entry " + entryId + " -> UPDATED no existe\n");
		}
		
		System.out.println(" -> ID: " + entryId);
		
		Entry newEntry = new Entry(id, entryId, entryLink, entrySummary, entryTitle, entryUpdate);
		newEntry.readContractFolderStatus(e, POS_UNICO_ELEMENTO);
		//newEntry.print();
		entries.add(newEntry);	
		
		
		/* ----> CONEXION CON BD <----
		 * Vamos a insertar en la BD la entry:
		 * 	1. Debemos crear un registro en tbl_ids para guardar la fecha en que se produjo esta lectura
		 * 	2. Guardamos el ids generado automáticamente para linkearlo con expedientes (FK)
		 * 	3. Almacenamos los datos del entry
		 * 	3.1. Creamos la conexion en la clase ConexionSQL
		 * 	3.2. Le pasamos al entry el objeto sql para que pueda hacer la llamada a la creación de su sentencia, y ejecutarla
		 */
		
		if (escribir){
			ConexionSQL conn = new ConexionSQL();
			conn.writeExpediente(newEntry, ids, updated, primera_lectura);
		}
	}
	
	public void readUpdateDate() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		try {
			//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder(this.URL);
			
			//Buscamos la lista de nodos en la raíz (feed) con la etiqueta "updated"
			NodeList nodos = document.getElementsByTagName("updated");
			
			if (nodos.getLength() > 0){
				//Como sabemos que solo vamos a encontrar uno, lo almacenamos directamente
				String date= nodos.item(POS_UNICO_ELEMENTO).getTextContent().replace("T", " ");
				date = date.substring(0, date.indexOf("+"));
				
				updated = Timestamp.valueOf(date);
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			System.err.println("UPDATED del documento no existe");
		}
	}
	
	public void readLinks() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String ID;
		
		try {
			//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder(this.URL);
			
			// Recogemos primero el ID, para poder modificarlo despues
			NodeList IDs = document.getElementsByTagName("id");
			if(IDs.getLength() > 0){
				ID = IDs.item(POS_UNICO_ELEMENTO).getTextContent();
				
				// Buscamos la lista de nodos en FEED con la etiqueta link
				NodeList links = document.getElementsByTagName("link");
				
				for (int i = 0; i < links.getLength(); i++){
					// Recogo la información de cada link (href="" rel="")
					String rel = links.item(i).getAttributes().getNamedItem("rel").getTextContent();
					String href = links.item(i).getAttributes().getNamedItem("href").getTextContent();
					
					if (rel.compareTo("self") == 0){
						String link = rebuildLink(ID, href);
						selfLink = link;
					}else if (rel.compareTo("next") == 0){
						String link = rebuildLink(ID, href);
						nextLink = link;
					}
				}
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			e.getStackTrace();
		}
	}
	
	public void init(){
		
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/

	// Filtro por NIF
	public Parser(String URL, String NIF, String modo){
		this.URL = new File(URL);
		this.NIF = NIF;
		this.modo_identificacion = modo;
		this.ids = createIds();
	}
	
	// Filtro por nº de expediente
	public Parser(String URL, ArrayList<String> exp, String modo){
		this.URL = new File(URL);
		this.expedientes = exp;
		this.modo_identificacion = modo;
		this.ids = createIds();
	}
	
	// Filtro por nº de expediente sin URL
	public Parser(ArrayList<String> exp, String modo){
		this.expedientes = exp;
		this.modo_identificacion = modo;
		this.ids = createIds();
	}
	
	// Sin nada (leemos todo)
	public Parser(){
		this.ids = createIds();
	};
	
	
	/**********************/
	/** AUXILIARY METHODS**/
	/**********************/
	
	/* Creación de un IDS -> información sobre esta ejecución del Parser */
	private int createIds(){
		int ids = 0;
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
		
		return ids;
	}
	
	/* Comprobación del identificador (NIF) del entry, para ver si es válido o no */
 	private String getEntryPartyID(Element e){
		String ID = "";
		
		// Busco el ContractFolderStatus -> Sabemos que solo hay uno
		Element cfs = (Element)e.getElementsByTagName("cac-place-ext:ContractFolderStatus").item(POS_UNICO_ELEMENTO);
		if (cfs != null){
			
			// Dentro del ContractFolderStatus, busco el LocatedContractingParty -> Sabemos que solo hay uno
			Element lcp = (Element) cfs.getElementsByTagName("cac-place-ext:LocatedContractingParty").item(POS_UNICO_ELEMENTO);
			if (lcp != null){
				
				// Dentro del LocatedContractingParty, busco el Party -> Sabemos que solo hay uno
				Element party = (Element) lcp.getElementsByTagName("cac:Party").item(POS_UNICO_ELEMENTO);
				if (party != null){
					
					// Dentro del Party, busco PartyIdentification, del cual pueden haber: minimo 1, maximo 3
					NodeList PIDNodeList = party.getElementsByTagName("cac:PartyIdentification");
					if (PIDNodeList.getLength() > 0){
						
						// Recorro la lista, y busco el NIF
						for (int i = 0; i < PIDNodeList.getLength(); i++){
							// Cojo el bloque <cac:PartyIdentification>
							Element partyIdentification = (Element) PIDNodeList.item(i);
							// Busco en sus hijos el ID -> Sabemos que por cada PartyIdentification solo vendra un ID
							Element PID = (Element) partyIdentification.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
							// Miramos si el schemeName es NIF, en caso contrario pasamos al siguiente
							if (PID.getAttributes().getNamedItem("schemeName").getTextContent().compareTo("NIF") == 0){
								ID = PID.getTextContent();
							}
						}
						
					}else{
						throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS -> LOCATED CONTRACTING PARTY -> PARTY -> PARTY IDENTIFICATION no existe, no se puede obtener la identificación del entry");
					}
					
				}else{
					throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS -> LOCATED CONTRACTING PARTY -> PARTY no existe, no se puede obtener la identificación del entry");
				}
				
			}else{
				throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS -> LOCATED CONTRACTING PARTY no existe, no se puede obtener la identificación del entry");
			}
			
		}else{
			throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS no existe, no se puede obtener la identificación del entry");
		}
		
		return ID;
	}
	
 	private String getEntryExp(Element e) {
 		String exp = "";
		
		// Busco el ContractFolderStatus -> Sabemos que solo hay uno
		Element cfs = (Element)e.getElementsByTagName("cac-place-ext:ContractFolderStatus").item(POS_UNICO_ELEMENTO);
		if (cfs != null){
			// Dentro del ContractFolderStatus, busco el ContractFolderID -> Sabemos que solo hay uno
			Element cfid = (Element) cfs.getElementsByTagName("cbc:ContractFolderID").item(POS_UNICO_ELEMENTO);
			if (cfid != null){
				exp = cfid.getTextContent();
			}else{
				throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS -> LOCATED CONTRACTING PARTY no existe, no se puede obtener la identificación del entry");
			}
		}else{
			throw new NullPointerException("ENTRY -> CONTRACT FOLDER STATUS no existe, no se puede obtener la identificación del entry");
		}
	
		return exp;
	}
 	
	/* Reconstrucción del link */
	private String rebuildLink(String ID, String href) {
		String result = "";
		String[] splited = ID.split("/");
		int cont = 0;
		
		splited[splited.length-1] = href;
		while (cont < splited.length-1){
			result += splited[cont] + "/";
			cont++;
		}
		result += splited[splited.length-1];
		
		return result;
	}
	
	private Document initDocumentBuilder(File url) throws ParserConfigurationException, SAXException, IOException, FileNotFoundException{
		Document document = null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(url);
            document.getDocumentElement().normalize();
		}catch (FileNotFoundException e){
			throw e;
		}
		return document;
	}
	
	public void printData(){
		System.out.println("*************************************");
		System.out.println("Link actual: " + selfLink);
		System.out.println("En este archivo hay un total de " + entryCont + " entries, de los cuales " + entries.size() + " son válidos.");
		System.out.println("Fecha de actualización: " + updated);
		System.out.println("*************************************");
		System.out.println("Las entries son: ");
		for (Entry e : entries){
			System.out.println("ID: " + e.getId());
			System.out.println("Fecha de actualización: " + e.getUpdated());
			System.out.println("------------");
		}
		System.out.println("Link siguiente: " + nextLink);
	}

	public void setURL(File file) {
		this.URL = file;
	}

	
	/** TYPE CODES */
	
	public void writeSubtypeCodes() throws ParserConfigurationException, SAXException, TransformerException {
		try {
			// 1 -> Suministros
			// 2 -> Servicios
			// 3 -> Obras
			// 50 -> Patrimonial
			int code = 0, tipo;
			String nombre = null;
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
			
	        /* SUMINISTROS */
	        tipo = 1;
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/GoodsContractCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeSubTypeCode(code, nombre, tipo);
	        }
	        
			/* SERVICIOS */
	        tipo = 2;
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/ServiceContractCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        doc = db.parse(conexion.getInputStream());
	        
	        codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeSubTypeCode(code, nombre, tipo);
	        }
	        
	        /* OBRAS */
	        tipo = 3;
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/WorksContractCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        doc = db.parse(conexion.getInputStream());
	        
	        codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeSubTypeCode(code, nombre, tipo);
	        }
	        
	        /* PATRIMONIAL */
	        tipo = 50;
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.02/PatrimonialContractCode-2.02.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        doc = db.parse(conexion.getInputStream());
	        
	        codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeSubTypeCode(code, nombre, tipo);
	        }
	        
	        con.writeSubTypeCode(0, "nulo", 0);
	     } catch (IOException e) {
	        e.printStackTrace();
	     }
	}

	public void writeCPV() throws ParserConfigurationException, SAXException, TransformerException {
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.04/CPV2008-2.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			String[] nombres = valueList.item(j).getTextContent().replaceAll("\n", "").split(" ");
	        			
	        			for (int k = 0; k < nombres.length; k++){
	        				if (!nombres[k].isEmpty() && nombres[k].compareTo("\n")!=0){
	        					nombre += nombres[k] + " ";
	        				}
	        			}
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeCPVCode(code, nombre.trim());
	        	nombre = "";
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeCountryIdentificationCode() throws ParserConfigurationException, SAXException, TransformerException {
		try {
			String code = "", nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/CountryIdentificationCode-2.08.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeCountryIdentificationCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeCountrySubentityCode() throws ParserConfigurationException, SAXException, TransformerException {
		try {
			String code = "", nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/NUTS-2021.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeCountrySubentityCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeProcedureCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.07/SyndicationTenderingProcessCode-2.07.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeProcedureCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void writeContractingSystemTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/ContractingSystemTypeCode-2.08.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeContractingSystemTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void writeUrgencyCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/DiligenceTypeCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeUrgencyCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeSubmissionMethodCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0; 
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/TenderDeliveryCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeSubmissionMethodCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeLanguage() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "", nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/LanguagePresentationCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeLanguage(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeProcurementLegislation() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "", nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/ProcurementLegislationDocumentReferenceID-2.08.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeProcurementLegislation(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void writeContractingPartyTypeCode() throws ParserConfigurationException, SAXException, TransformerException {
		try {
			String code = "", nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/ContractingAuthorityCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeContractingPartyTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeModosId() {
		ConexionSQL con = new ConexionSQL();
		int modosid = 1;
		String descripcion = "Automático";
		
		// Escribimos en la BD
		con.writeModosId(modosid, descripcion);
		
		modosid = 2;
		descripcion = "Manual";
		con.writeModosId(modosid, descripcion);
	}
	
	public void writeContractFolderStatusCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "", nombre = "";
			int orden = 1;
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.03/ContractFolderStatusCode-2.03.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	if (code.compareTo("ANUL") == 0){
		        	con.writeContractFolderStatusCode(code, nombre, 0);
	        	}else{
	        		con.writeContractFolderStatusCode(code, nombre, orden);
	        		orden++;
	        	}
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
		
	public void writeTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/ContractCode-2.08.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeTypèCode(code, nombre);
	        }
	        con.writeTypèCode(0, "nulo");
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeTipoPliego(){
		ConexionSQL con = new ConexionSQL();
		int id = 1;
		String tipo = "Administrativo";
		con.writeTipoPliego(id, tipo);
		
		id = 2;
		tipo = "Técnico";
		con.writeTipoPliego(id, tipo);
		
		id = 3;
		tipo = "Adicional";
		con.writeTipoPliego(id, tipo);
	}
	
	public void writeTipoPlazo(){
		ConexionSQL con = new ConexionSQL();
		int id = 1;
		String tipo = "Pliegos";
		con.writeTipoPlazo(id, tipo);
		
		id = 2;
		tipo = "Oferta";
		con.writeTipoPlazo(id, tipo);
		
		id = 3;
		tipo = "Solicitudes";
		con.writeTipoPlazo(id, tipo);
	}
	
	public void writeFundingProgramCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.02/FundingProgramCode-2.02.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeFundingProgramCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeGuaranteeTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/1.04/GuaranteeTypeCode-1.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        			if (nombre.compareTo("Special") == 0){
	        				nombre = "Especial";
	        			}
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeGuarateeTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void writeRequiredBusinessProfileCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.05/RequiredBusinessProfileCode-2.05.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeRequiredBusinessProfileCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeDeclarationTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.08/DeclarationTypeCode-2.08.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeDeclarationTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeTechnicalCapabilityTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.0/TechnicalCapabilityTypeCode-2.0.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeTechnicalCapabilityTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeFinancialCapabilityTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.0/FinancialCapabilityTypeCode-2.0.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeFinancialCapabilityTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeTipoEvaluacion(){
		ConexionSQL con = new ConexionSQL();
		int id = 1;
		String tipo = "Técnica";
		con.writeTipoEvaluacion(id, tipo);
		
		id = 2;
		tipo = "Económica-Financiera";
		con.writeTipoEvaluacion(id, tipo);
	}
	
	public void writeTenderResultCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			int code = 0;
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.02/TenderResultCode-2.02.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = Integer.parseInt(valueList.item(j).getTextContent().trim());
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeTenderResultCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeReasonCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.0/ProcessJustificationReasonCode-2.0.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeReasonCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public void writeNoticeTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.04/TenderingNoticeTypeCode-2.04.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeNoticeTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void writeDocumentTypeCode() throws ParserConfigurationException, SAXException, TransformerException{
		try {
			String code = "";
			String nombre = "";
			ConexionSQL con = new ConexionSQL();
			URL url;
			URLConnection conexion;
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance(); 
	        DocumentBuilder db = dbf.newDocumentBuilder(); 
	        
	        // Se abre la conexión
	        url = new URL("https://contrataciondelestado.es/codice/cl/2.07/TenderingDocumentTypeCode-2.07.gc");
	        conexion = url.openConnection();
	        conexion.connect();
	     
	        Document doc = db.parse(conexion.getInputStream());
	        
	        Element codeList = (Element) doc.getElementsByTagName("gc:CodeList").item(POS_UNICO_ELEMENTO);
	        Element simpleCodeList = (Element) codeList.getElementsByTagName("SimpleCodeList").item(POS_UNICO_ELEMENTO);
	        NodeList rowsList = simpleCodeList.getElementsByTagName("Row");
	        
	        for (int i = 0; i < rowsList.getLength(); i++){
	        	Element row = (Element) rowsList.item(i);
	        	NodeList valueList = row.getElementsByTagName("Value");
	        	for (int j = 0; j < valueList.getLength(); j++){
	        		if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("code") == 0){
	        			code = valueList.item(j).getTextContent().trim();
	        		}else if (valueList.item(j).getAttributes().getNamedItem("ColumnRef").getTextContent().compareTo("nombre") == 0){
	        			nombre = valueList.item(j).getTextContent().trim();
	        		}
	        	}
	        	// Escribimos en la BD
	        	con.writeDocumentTypeCode(code, nombre);
	        }
		}catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
