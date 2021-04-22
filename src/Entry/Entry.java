package Entry;

import org.w3c.dom.Element;

import ContractFolderStatus.ContractFolderStatus;

/**
 * @params
 *		id: String[1]
 *		link: String[1]
 *		summary: String[1]
 *		title: String[1]
 *		updated: Date[1]
 *		cfs: ContractFolderStatus[1]
 */
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
			
			this.cfs.readProcurementProject(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderResult(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readLocatedContractingParty(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderingTerms(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTenderingProcess(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readContractModification(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readLegalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readTechnicalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readAdditionalDocumentReference(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readValidNoticeInfo(cfs, POS_UNICO_ELEMENTO);
			this.cfs.readGeneralDocument(cfs, POS_UNICO_ELEMENTO);
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

	public String getUpdated() {
		return updated;
	}
}


