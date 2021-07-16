package procurementProjectLot;

import org.w3c.dom.Element;

import procurementProject.ProcurementProject;

/*
 * 		ID: int[1]
 * 		procurementProject: ProcurementProject[1]
 */
public class ProcurementProjectLot {
	private String ID;
	private ProcurementProject procurementProject;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param ppl El cac:ProcurementProjectLot que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element ppl, int POS_UNICO_ELEMENTO){
		this.ID = null;
		
		Element ID = (Element) ppl.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		try{
			this.ID = ID.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ProcurementProjectLot -> ID no existe\n");
		}
	}
	
	/**
	 * Lee el cac:ProcurementProjectLot del documento
	 * 
	 * @param ppl El cac:ProcurementProjectLot que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readProcurementProject(Element ppl, int POS_UNICO_ELEMENTO){
		this.procurementProject = null;
		
		Element pp = (Element) ppl.getElementsByTagName("cac:ProcurementProject").item(POS_UNICO_ELEMENTO);
		try{
			this.procurementProject = new ProcurementProject();
			
			this.procurementProject.readAttributes(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readBudgetAmount(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRequiredCommodityClassification(pp, POS_UNICO_ELEMENTO);
			this.procurementProject.readRealizedLocation(pp, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ProcurementProjectLot -> PROCUREMENT PROJECT no existe\n");
		}
	}

	public String getID() {
		return ID;
	}

	public ProcurementProject getProcurementProject() {
		return procurementProject;
	}
}
