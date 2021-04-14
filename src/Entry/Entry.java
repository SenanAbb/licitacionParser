package Entry;

import org.w3c.dom.Element;

import ContractFolderStatus.ContractFolderStatus;

public class Entry {
	private String id, link, summary, title, updated;
	private ContractFolderStatus cfs;
	
	public void readContractFolderStatus(Element entry, int POS_UNICO_ELEMENTO){
		this.cfs = null;
		
		//Dentro del ENTRY, buscamos el <cac-place-ext:ContractFolderStatus>
		Element cfs = (Element) entry.getElementsByTagName("cac-place-ext:ContractFolderStatus").item(POS_UNICO_ELEMENTO);
		
		if (cfs == null){
			System.err.print("ERROR FATAL: CONTRACT FOLDER STATUS no existe\n");
			System.exit(0);
		}else{
			this.cfs = new ContractFolderStatus();
			String contractFolderID = null, contractFolderStatusCode = null;
			
			//Inicializamos todas las variables que deberá contener el ContractFolderStatus
			Element contractFolderIDNode = (Element) cfs.getElementsByTagName("cbc:ContractFolderID").item(POS_UNICO_ELEMENTO);
			Element contractFolderStatusCodeNode = (Element) cfs.getElementsByTagName("cbc-place-ext:ContractFolderStatusCode").item(POS_UNICO_ELEMENTO);
			
			// Compruebo la existencia del ContractFolderID, si no existe se queda a null y mandamos mensaje
			if (contractFolderIDNode != null){
				contractFolderID = contractFolderIDNode.getTextContent();
				this.cfs.setContractFolderID(contractFolderID);
			}else{
				System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER ID no existe\n");
			}
			
			// Compruebo la existencia del ContractFolderID, si no existe se queda a null y mandamos mensaje
			if (contractFolderStatusCodeNode != null){
				contractFolderStatusCode = contractFolderStatusCodeNode.getTextContent();
				this.cfs.setContractFolderStatusCode(contractFolderStatusCode);
			}else{
				System.err.print("ERROR FATAL: ContractFolderStatus -> CONTRACT FOLDER STATUS CODE no existe\n");
			}	
			
			this.cfs.readProcurementProject(cfs, POS_UNICO_ELEMENTO);
			if (this.cfs.getProcurementProject() != null){
				this.cfs.getProcurementProject().print();
			}
		}
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public Entry(String id, String link, String summary, String title, String updated) {
		this.id = id;
		this.link = link;
		this.summary = summary;
		this.title = title;
		this.updated = updated;
	}
	
	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUpdated() {
		return updated;
	}

	public void setUpdated(String updated) {
		this.updated = updated;
	}

	public ContractFolderStatus getCfs() {
		return cfs;
	}

	public void setCfs(ContractFolderStatus cfs) {
		this.cfs = cfs;
	}
}


