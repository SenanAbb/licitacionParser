package Parser;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
	public void start(){
		readUpdateDate();
		readLinks();
	}
	
	private void readUpdateDate(){
		try {        	
			//Iniciamos el DocumentBuilderFactory;
            Document document = initDocumentBuilder();
            
            //Buscamos la lista de nodos en la raíz (feed) con la etiqueta "updated"
            NodeList nodos = document.getElementsByTagName("updated");
            
            //Como sabemos que solo vamos a encontrar uno, lo almacenamos directamente
            updated = nodos.item(0).getTextContent();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
	
	private void readLinks(){
		String ID;
		
		try {
			//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
			// Recogemos primero el ID, para poder modificarlo despues
			NodeList IDs = document.getElementsByTagName("id");
			ID = IDs.item(0).getTextContent();
			
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
		}catch (Exception e){
			e.getStackTrace();
		}
	}

	public void readAllEntries(){
		try {
    		//Iniciamos el DocumentBuilderFactory;
			Document document = initDocumentBuilder();
			
			NodeList entriesNodes = document.getElementsByTagName("entry");
			entryCont = entriesNodes.getLength();
			
			for (int i = 0; i < entriesNodes.getLength(); i++){
				// Cojo el nodo actual
				Node entry = entriesNodes.item(i);
				// Compruebo si es un elemento
				if (entry.getNodeType() == Node.ELEMENT_NODE){
					// Lo transformo a element
					Element e = (Element) entry;
					
					//Compruebo el PartyID para saber si es un Entry válido o no
					String result = getEntryPartyID(e);
					if (result.compareTo(NIF) == 0){
						String[] idSplit = null;
						String entryId = null, entryLink = null, entrySummary = null, entryTitle = null, entryUpdate = null;
						
						Element id = (Element) e.getElementsByTagName("id").item(POS_UNICO_ELEMENTO);
						if (id != null){
							idSplit = id.getTextContent().split("/");
							entryId = idSplit[idSplit.length-1];
						}else{
							System.err.print("ERROR FATAL: Entry -> ID no existe\n");
						}
						
						Element link = (Element) e.getElementsByTagName("link").item(POS_UNICO_ELEMENTO);
						if (link != null){
							entryLink = link.getAttributes().getNamedItem("href").getTextContent();
						}else{
							System.err.print("ERROR FATAL: Entry " + entryId + " -> LINK no existe\n");
						}
								
						Element summary = (Element) e.getElementsByTagName("summary").item(POS_UNICO_ELEMENTO);		
						if (summary != null){
							entrySummary = summary.getTextContent();
						}else{
							System.err.print("ERROR FATAL: Entry " + entryId + " -> SUMMARY no existe\n");
						}
						
						Element title = (Element) e.getElementsByTagName("title").item(POS_UNICO_ELEMENTO);
						if (title != null){
							entryTitle = title.getTextContent();
						}else{
							System.err.print("ERROR FATAL: Entry " + entryId + " -> TITLE no existe\n");
						}
										
						Element update = (Element) e.getElementsByTagName("updated").item(POS_UNICO_ELEMENTO);
						if (update != null){
							entryUpdate = update.getTextContent();
						}else{
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
		} catch (Exception e) {
			e.printStackTrace();
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
		
		NodeList entryChilds = e.getChildNodes();
		
		for (int i = 0; i < entryChilds.getLength(); i++){
			Node contractFolderStatus = entryChilds.item(i);
			
			// Busco la clase ContractFolderStatus, y compuebo que sea un nodo
			if (contractFolderStatus.getNodeName().compareTo("cac-place-ext:ContractFolderStatus") == 0 && contractFolderStatus.getNodeType() == Node.ELEMENT_NODE){
				// Cuando la haya encontrado, voy a buscar en sus hijos
				Element CFSElement = (Element) contractFolderStatus;
				NodeList CFSChilds = CFSElement.getChildNodes();
				
				// Busco la clase LocatedContractingParty
				for (int j = 0; j < CFSChilds.getLength(); j++){
					Node locatedContractingParty = CFSChilds.item(j);
					
					// Busco la clase LocatedContractingParty y compruebo que sea un nodo
					if (locatedContractingParty.getNodeName().compareTo("cac-place-ext:LocatedContractingParty") == 0 && locatedContractingParty.getNodeType() == Node.ELEMENT_NODE){
						Element LCPElement = (Element) locatedContractingParty;
						NodeList LCPChilds = LCPElement.getChildNodes();
						
						// Busco la clase Party
						for (int k = 0; k < LCPChilds.getLength(); k++){
							Node party = LCPChilds.item(k);
							
							// Busco la clase Party y compruebo que sea un nodo
							if (party.getNodeName().compareTo("cac:Party") == 0 && party.getNodeType() == Node.ELEMENT_NODE){
								Element PartyElement = (Element) party;
								NodeList partyChilds = PartyElement.getChildNodes();
								
								for (int x = 0; x < partyChilds.getLength(); x++){
									Node partyIdentification = partyChilds.item(x);
									
									// Busco la clase PartyIdentification y compruebo que sea un nodo
									if (partyIdentification.getNodeName().compareTo("cac:PartyIdentification") == 0 && partyIdentification.getNodeType() == Node.ELEMENT_NODE){
										Element PartyID = (Element) partyIdentification;
										NodeList PIDs = PartyID.getElementsByTagName("cbc:ID");
										
										for (int y = 0; y < PIDs.getLength(); y++){
											String type = PIDs.item(0).getAttributes().getNamedItem("schemeName").getTextContent();
											if (type.compareTo("NIF") == 0){
												ID = PIDs.item(y).getTextContent();
											}
										}
									}
								}
								break;
							}
						}
						break;
					}
				}
				break;
			}
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
	
	private Document initDocumentBuilder(){
		Document document = null;
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            document = documentBuilder.parse(URL);
            document.getDocumentElement().normalize();
		}catch (Exception e){
			e.printStackTrace();
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

	public void setNIF(String nIF) {
		NIF = nIF;
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
