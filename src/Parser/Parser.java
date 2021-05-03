package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
	private static int ids;
	
	private String NIF = "";
	private File URL;
	
	private String updated;
	private String selfLink, nextLink;
	private int entryCont = 0;
	private ArrayList<Entry> entries = new ArrayList<Entry>();

	public void readAllEntries(){
		try {
    		//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
			NodeList entriesNodes = document.getElementsByTagName("entry");
			
			if (entriesNodes.getLength() > 0){
				entryCont += entriesNodes.getLength();
				for (int i = 0; i < entriesNodes.getLength(); i++){
					// Cojo el nodo actual
					Node entry = entriesNodes.item(i);
					// Compruebo si es un elemento
					if (entry.getNodeType() == Node.ELEMENT_NODE){
						// Lo transformo a element
						Element e = (Element) entry;
						
						//Compruebo el PartyID para saber si es un Entry válido o no
						String result = "";
						try{
							result = getEntryPartyID(e);
						}catch (NullPointerException ex){
							System.err.println(ex.getMessage());
						}
						if (result.compareTo(NIF) == 0){
							// ----> PARSEO INIT <---- //
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
							
							Entry newEntry = new Entry(id, entryId, entryLink, entrySummary, entryTitle, entryUpdate);
							newEntry.readContractFolderStatus(e, POS_UNICO_ELEMENTO);
							//newEntry.print();
							entries.add(newEntry);	
							
							// ----> PARSEO END <---- //
							
							// ----> CONEXION CON BD INIT <---- //
							
							/**
							 * Vamos a insertar en la BD la entry:
							 * 	1. Debemos crear un registro en tbl_ids para guardar la fecha en que se produjo esta lectura
							 * 	2. Guardamos el ids generado automáticamente para linkearlo en tbl_entrys (FK)
							 * 	3. Almacenamos los datos del entry
							 * 	3.1. Creamos la conexion en la clase ConexionSQL
							 * 	3.2. Le pasamos al entry el objeto sql para que pueda hacer la llamada a la creación de su sentencia, y ejecutarla
							 */
							
							newEntry.writeData(ids);
							
							// ----> CONEXION CON BD END <---- //
						}
					}
				}
			}else{
				throw new NullPointerException();
			}
		} catch (NullPointerException | ParserConfigurationException | SAXException | IOException e) {
			System.err.println("No hay ninguna ENTRY en el documento, o el documento es inválido");
			e.printStackTrace();
		}
	}

	public void start() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		readUpdateDate();
		readLinks();
	}
	
	private void readUpdateDate() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		try {
			//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
			//Buscamos la lista de nodos en la raíz (feed) con la etiqueta "updated"
			NodeList nodos = document.getElementsByTagName("updated");
			
			if (nodos.getLength() > 0){
				//Como sabemos que solo vamos a encontrar uno, lo almacenamos directamente
				updated = nodos.item(POS_UNICO_ELEMENTO).getTextContent();
			}else{
				throw new NullPointerException();
			}
		}catch (NullPointerException e){
			System.err.println("UPDATED del documento no existe");
		}
	}
	
	private void readLinks() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String ID;
		
		try {
			//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
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

	
	/******************/
	/** CONSTRUCTORS **/
	/******************/

	
	public Parser(String URL, String NIF){
		this.URL = new File(URL);
		this.NIF = NIF;
		this.ids = createIds();
	}
	
	public Parser(String NIF){
		this.NIF = NIF;
		this.ids = createIds();
	}
	
	
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
			
			// INICIAMOS LA TRANSACCIÓN
			conn.setAutoCommit(false);
			
			// Parametro 1 del procedimiento almacenado
			sentencia.setInt("modos_id", 2);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("ids", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// COMMIT DE LAS INSTRUCCIONES
			conn.commit(); 
			
			// Se obtiene la salida
			ids = sentencia.getInt("ids");
		} catch (SQLException e) {
			System.out.println("Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("Error haciendo rollback: " + e.getMessage());
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
	
	private Document initDocumentBuilder() throws ParserConfigurationException, SAXException, IOException, FileNotFoundException{
		Document document = null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(URL);
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
}
