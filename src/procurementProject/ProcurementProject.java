package procurementProject;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 *		name: String [1]
 *		typeCode: int [0..1]
 *		subTypeCode: int [0..1]
 *		budgetAmount: BudgetAmount [0..1]
 *		plannedPeriod: PlannedPeriod [1]
 *		requiredCommodityClassificationList: RequiredCommodityClassification [0..*]
 *		realizedLocation: RealizedLocation [0..1]
 *		contractExtension: ContractExtension [0..1]
 */
public class ProcurementProject {
	private String name;
	private int typeCode, subTypeCode;
	private BudgetAmount budgetAmount;
	private PlannedPeriod plannedPeriod;
	private RequiredCommodityClassification[] requiredCommodityClassificationList;
	private RealizedLocation realizedLocation;
	private ContractExtension contractExtension;
	
	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readAttributes(Element pp, int POS_UNICO_ELEMENTO){
		this.name = null;
		this.typeCode = 0;
		this.subTypeCode = 0;
		
		Element nameNode = null, typeCodeNode = null, subTypeCodeNode = null;
		
		// Compruebo la existencia del Name, si no existe se queda a null y mandamos mensaje
		nameNode = (Element) pp.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		try{
			this.name = nameNode.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ProcurementProject -> NAME no existe\n");
		}
		
		// Compruebo la existencia del TypeCode, si no existe se queda a null y mandamos mensaje
		typeCodeNode = (Element) pp.getElementsByTagName("cbc:TypeCode").item(POS_UNICO_ELEMENTO);
		if (typeCodeNode != null){
			this.typeCode = Integer.parseInt(typeCodeNode.getTextContent());
		}
				
		// Compruebo la existencia del SubTypeCode, si no existe se queda a null y mandamos mensaje
		subTypeCodeNode = (Element) pp.getElementsByTagName("cbc:SubTypeCode").item(POS_UNICO_ELEMENTO);	
		if (subTypeCodeNode != null){
			this.subTypeCode = Integer.parseInt(subTypeCodeNode.getTextContent());
		}
	}
	
	/**
	 * Lee el BudgetAmount del documento
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readBudgetAmount(Element pp, int POS_UNICO_ELEMENTO){
		this.budgetAmount = null;
		
		Element budgetAmount = (Element) pp.getElementsByTagName("cac:BudgetAmount").item(POS_UNICO_ELEMENTO);
		
		// Si no encuentro el bloque entero, lo pongo a null y termino la ejecuci�n, pues el resto de variables y subclases seguro no van a estar
		if (budgetAmount != null){
			this.budgetAmount = new BudgetAmount();
			
			this.budgetAmount.readAttributes(budgetAmount, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee el RequiredCommodityClassification del documento
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readRequiredCommodityClassification(Element pp, int POS_UNICO_ELEMENTO){
		this.requiredCommodityClassificationList = null;
		
		NodeList rccNodeList = pp.getElementsByTagName("cac:RequiredCommodityClassification");
		
		// Si hay alg�n Required Commodity Classification lo creo, sino lo dejo a null;
		if (rccNodeList.getLength() > 0){
			this.requiredCommodityClassificationList = new RequiredCommodityClassification[rccNodeList.getLength()];
			
			for (int i = 0; i < rccNodeList.getLength(); i++){
				RequiredCommodityClassification rcc = new RequiredCommodityClassification();
				
				Element rccElement = (Element) rccNodeList.item(i);
				rcc.readAttributes(rccElement, POS_UNICO_ELEMENTO);
				
				this.requiredCommodityClassificationList[i] = rcc; 
			}
		}
	}

	/**
	 * Lee el PlannedPeriod del documento
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readPlannedPeriod(Element pp, int POS_UNICO_ELEMENTO){
		this.plannedPeriod = new PlannedPeriod();
		
		Element pPeriod = (Element) pp.getElementsByTagName("cac:PlannedPeriod").item(POS_UNICO_ELEMENTO);
		try{
			plannedPeriod.readAttributes(pPeriod, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: ProcurementProject -> PLANNED PERIOD no existe\n");
		}
	}

	/**
	 * Lee el RealizedLocation del documento
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readRealizedLocation(Element pp, int POS_UNICO_ELEMENTO){
		this.realizedLocation = null;
		
		Element rl = (Element) pp.getElementsByTagName("cac:RealizedLocation").item(POS_UNICO_ELEMENTO);
		
		// -> Si existe este elemento, continuamos, sino creamos el objeto NULL
		// -> Esto se puede hacer porque Realized Location tiene cardinal [0..1]
		if (rl != null){
			this.realizedLocation = new RealizedLocation();
			this.realizedLocation.readAttributes(rl, POS_UNICO_ELEMENTO);
		}
	}
	
	/**
	 * Lee el ContractExtension del documento
	 * 
	 * @param pp El cac:ProcurementProject que contiene la informaci�n
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posici�n del array donde coger un dato
	 */
	public void readContractExtension(Element pp, int POS_UNICO_ELEMENTO){
		this.contractExtension = null;
		
		Element ce = (Element) pp.getElementsByTagName("cac:ContractExtension").item(POS_UNICO_ELEMENTO);
		if (ce != null){
			this.contractExtension = new ContractExtension();
			this.contractExtension.readAttributes(ce, POS_UNICO_ELEMENTO);
		}
	}
	
	public String getName(){
		return name;
	}
	
	public int getTypeCode() {
		return typeCode;
	}
	
	public int getSubTypeCode() {
		return subTypeCode;
	}
	
	public BudgetAmount getBudgetAmount() {
		return budgetAmount;
	}
	
	public PlannedPeriod getPlannedPeriod() {
		return plannedPeriod;
	}

	public RequiredCommodityClassification[] getRequiredCommodityClassificationList() {
		return requiredCommodityClassificationList;
	}

	public RealizedLocation getRealizedLocation() {
		return realizedLocation;
	}
	
	public ContractExtension getContractExtension(){
		return contractExtension;
	}
}
