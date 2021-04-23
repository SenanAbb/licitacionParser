package Parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	
	private String NIF = "";
	private File URL;
	
	private String updated;
	private String selfLink, nextLink;
	private int entryCont;
	private ArrayList<Entry> entries = new ArrayList<Entry>();
	
	/* Read:
	 * 	-> File updated date
	 * 	-> Self and Next links
	 */
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
	
	public void readAllEntries(){
		try {
    		//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
			NodeList entriesNodes = document.getElementsByTagName("entry");
			
			if (entriesNodes.getLength() > 0){
				entryCont = entriesNodes.getLength();
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
							String[] idSplit = null;
							String entryId = null, entryLink = null, entrySummary = null, entryTitle = null, entryUpdate = null;
							
							Element id = (Element) e.getElementsByTagName("id").item(POS_UNICO_ELEMENTO);
							try{
								idSplit = id.getTextContent().split("/");
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
								entryUpdate = update.getTextContent();
							}catch (NullPointerException exUPDATED){
								System.err.print("ERROR FATAL: Entry " + entryId + " -> UPDATED no existe\n");
							}
							
							Entry newEntry = new Entry(entryId, entryLink, entrySummary, entryTitle, entryUpdate);
							
							newEntry.readContractFolderStatus(e, POS_UNICO_ELEMENTO);
							
							// PRINT ALL ENTRY DATE STRUCTURE
							newEntry.print();
							
							entries.add(newEntry);				
						}
					}
				}
			}else{
				throw new NullPointerException();
			}
		} catch (NullPointerException | ParserConfigurationException | SAXException | IOException e) {
			System.err.println("No hay ninguna ENTRY en el documento, o el documento es inválido");
		}
	}

	
	/******************/
	/** CONSTRUCTORS **/
	/******************/

	
	public Parser(String URL, String NIF){
		this.URL = new File(URL);
		this.NIF = NIF;
	}
	
	public Parser(String NIF){
		this.NIF = NIF;
	}
	
	
	/**********************/
	/** AUXILIARY METHODS**/
	/**********************/
	
	
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
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public String getNIF() {
		return NIF;
	}


	public File getURL() {
		return URL;
	}

	public void setURL(File uRL) {
		URL = uRL;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public String getSelfLink() {
		return selfLink;
	}

	public void setSelfLink(String selfLink) {
		this.selfLink = selfLink;
	}

	public String getNextLink() {
		return nextLink;
	}

	public void setNextLink(String nextLink) {
		this.nextLink = nextLink;
	}

	public int getEntryCont() {
		return entryCont;
	}

	public void setEntryCont(int entryCont) {
		this.entryCont = entryCont;
	}

	public ArrayList<Entry> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<Entry> entries) {
		this.entries = entries;
	}
}
