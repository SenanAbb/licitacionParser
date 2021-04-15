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
			
			this.cfs.readAttributes(cfs, POS_UNICO_ELEMENTO);
			
			/* Read PROCUREMENT PROJECT */
			this.cfs.readProcurementProject(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderResult(cfs, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("---------------- ENTRY " + id + "----------------\n" + 
						 "Link: " + link + "\n" + 
						 "Summary: " + summary + "\n" + 
						 "Title: " + title + "\n" + 
						 "Updated: " + updated + "\n");
		System.out.print("===============================================================\n");
		cfs.print();
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


