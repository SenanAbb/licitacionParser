package Parser;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import Conexion.ConexionSQL;
import Entry.Entry;

/**
 * @params
 * 		const POS_UNICO_ELEMENTO: int[1]
 * 		NIF: String[1]
 *		ULR: File[1]
 *		updated: Date[1]
 *		nextLink: String[1]
 *		entryCont: int[0..1]
 *		entries: Entry[] [0..*]
 */
public class Parser {
	private static final int POS_UNICO_ELEMENTO = 0; 
 
	private int feeds;
	
	private Entry entry;
	private Timestamp updated;
	private String selfLink;
	
	/* Si escribir = true -> escribirá los datos en la BD */
	private boolean escribir = true;
	
	public void readEntries(boolean primera_lectura, Document doc) throws Exception{
		Document document = doc;
		
		// Entries
		NodeList entriesNodes = document.getElementsByTagName("entry");
		System.out.println(entriesNodes.getLength() + ")");
		
		if (entriesNodes.getLength() > 0){
			entriesNodes.getLength();
			for (int i = 0; i < entriesNodes.getLength(); i++){
				// Cojo el nodo actual
				Node entry = entriesNodes.item(i);
				// Lo transformo a element
				Element e = (Element) entry;
				
				//System.out.println("\t\tLeyendo entry " + (i+1) + "/" + entriesNodes.getLength());
				
				readAttributesAndWrite(e, primera_lectura);		
			}
		}else{
			throw new NullPointerException();
		}
		
		entriesNodes = null;
	}
	
	private void readAttributesAndWrite(Element e, boolean primera_lectura) throws Exception{
		try{
			String[] idSplit = null;
			String id = null, entryId = null, entryLink = null, entrySummary = null, entryTitle = null;
			Timestamp entryUpdate = null;
			
			// ID
			Element idElement = (Element) e.getElementsByTagName("id").item(POS_UNICO_ELEMENTO);
			id = idElement.getTextContent();
			idSplit = idElement.getTextContent().split("/");
			entryId = idSplit[idSplit.length-1];
		
			// LINK
			Element link = (Element) e.getElementsByTagName("link").item(POS_UNICO_ELEMENTO);
			entryLink = link.getAttributes().getNamedItem("href").getTextContent();
				
			// SUMMARY
			Element summary = (Element) e.getElementsByTagName("summary").item(POS_UNICO_ELEMENTO);		
			entrySummary = summary.getTextContent();
			
			// TITLE
			Element title = (Element) e.getElementsByTagName("title").item(POS_UNICO_ELEMENTO);
			entryTitle = title.getTextContent();
					
			// UPDATE
			Element update = (Element) e.getElementsByTagName("updated").item(POS_UNICO_ELEMENTO);
			// Quitamos los espacios y los caracteres que no queremos
			String date = update.getTextContent().replace("T", " ");
			date = date.substring(0, date.indexOf("+"));
			entryUpdate = Timestamp.valueOf(date);
		
			//System.out.println(" -> ID: " + entryId);
		
			Entry newEntry = new Entry(id, entryId, entryLink, entrySummary, entryTitle, entryUpdate);
			newEntry.readContractFolderStatus(e, POS_UNICO_ELEMENTO);
			//newEntry.print();
			this.entry = newEntry;
		
			if (escribir){
				ConexionSQL conn = new ConexionSQL();
				conn.writeExpediente(newEntry, feeds, primera_lectura);
				
				// ESCRIBIMOS LOS DATOS EN EL LOG DESPUES DE ESCRIBIR EN LA BD
				conn.escribirLog(updated, selfLink, entryUpdate, entryId, newEntry.getContractFolderStatus().getContractFolderStatusCode());
			}
			
			newEntry = null;
		}catch(Exception ex){
			ConexionSQL conn = new ConexionSQL();
			conn.escribirLogError(selfLink, this.entry.getId(), ex);
		}
	}
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	// Sin nada (leemos todo)
	public Parser(){}
	
	
	/**********************/
	/** AUXILIARY METHODS**/
	/**********************/
	
	public void setFeeds(int feeds) {
		this.feeds = feeds;
	}

	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}
	
	/** TYPE CODES  */
	
	public void writeSubtypeCodes() throws Exception {
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
	public void writeCPV() throws Exception {
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
	public void writeCountryIdentificationCode() throws Exception {
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
	public void writeCountrySubentityCode() throws Exception {
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
	public void writeProcedureCode() throws Exception{
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
	public void writeContractingSystemTypeCode() throws Exception{
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
	public void writeUrgencyCode() throws Exception{
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
	public void writeSubmissionMethodCode() throws Exception{
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
	public void writeLanguage() throws Exception{
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
	public void writeProcurementLegislation() throws Exception{
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
	public void writeContractingPartyTypeCode() throws Exception {
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
	public void writeModosId() throws Exception {
		ConexionSQL con = new ConexionSQL();
		int modosid = 1;
		String descripcion = "Automático";
		
		// Escribimos en la BD
		con.writeModosId(modosid, descripcion);
		
		modosid = 2;
		descripcion = "Manual";
		con.writeModosId(modosid, descripcion);
	}
	public void writeContractFolderStatusCode() throws Exception{
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
	public void writeTypeCode() throws Exception{
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
	public void writeTipoPliego() throws Exception{
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
	public void writeTipoPlazo() throws Exception{
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
	public void writeFundingProgramCode() throws Exception{
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
	public void writeGuaranteeTypeCode() throws Exception{
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
	public void writeRequiredBusinessProfileCode() throws Exception{
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
	public void writeDeclarationTypeCode() throws Exception{
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
	public void writeTechnicalCapabilityTypeCode() throws Exception{
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
	public void writeFinancialCapabilityTypeCode() throws Exception{
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
	public void writeTipoEvaluacion() throws Exception{
		ConexionSQL con = new ConexionSQL();
		int id = 1;
		String tipo = "Técnica";
		con.writeTipoEvaluacion(id, tipo);
		
		id = 2;
		tipo = "Económica-Financiera";
		con.writeTipoEvaluacion(id, tipo);
	}
	public void writeTenderResultCode() throws Exception{
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
	public void writeReasonCode() throws Exception{
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
	public void writeNoticeTypeCode() throws Exception{
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
	public void writeDocumentTypeCode() throws Exception{
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
