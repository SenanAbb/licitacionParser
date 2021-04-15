package procurementProject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import utils.Address;
import utils.Country;

public class ProcurementProject {
	private String name;
	private int typeCode, subTypeCode;
	private BudgetAmount budgetAmount;
	private PlannedPeriod plannedPeriod;
	private RequiredCommodityClassification[] requiredCommodityClassificationList;
	private RealizedLocation realizedLocation;
	private ContractExtension contractExtension;
	
	public void readAttributes(Element pp, int POS_UNICO_ELEMENTO){
		//Inicializamos todas las variables que deberá contener el ProcurementProject
		this.name = null;
		this.typeCode = 0;
		this.subTypeCode = 0;
		
		Element nameNode = null, typeCodeNode = null, subTypeCodeNode = null;
		
		/* 
			-> Si los de cardinal 1 faltan, lanzamos un mensaje de error
			-> Si los de cardinal [0..1] o [0..*] faltan, se pone a null dentro de la clase padre
		*/
		
		// Compruebo la existencia del Name, si no existe se queda a null y mandamos mensaje
		nameNode = (Element) pp.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if (nameNode != null) {
			this.name = nameNode.getTextContent();
		}else{
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
	
	public void readBudgetAmount(Element pp, int POS_UNICO_ELEMENTO){
		this.budgetAmount = null;
		
		Element budgetAmount = (Element) pp.getElementsByTagName("cac:BudgetAmount").item(POS_UNICO_ELEMENTO);
		
		// Si no encuentro el bloque entero, lo pongo a null y termino la ejecución, pues el resto de variables y subclases seguro no van a estar
		if (budgetAmount != null){
			this.budgetAmount = new BudgetAmount();
			
			this.budgetAmount.readAttributes(budgetAmount, POS_UNICO_ELEMENTO);
		}
	}
	
	public void readRequiredCommodityClassification(Element pp, int POS_UNICO_ELEMENTO){
		this.requiredCommodityClassificationList = null;
		
		NodeList rccNodeList = pp.getElementsByTagName("cac:RequiredCommodityClassification");
		
		// Si hay algún Required Commodity Classification lo creo, sino lo dejo a null;
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

	public void readPlannedPeriod(Element pp, int POS_UNICO_ELEMENTO){
		this.plannedPeriod = new PlannedPeriod();
		
		Element pPeriod = (Element) pp.getElementsByTagName("cac:PlannedPeriod").item(POS_UNICO_ELEMENTO);
		if (pPeriod != null){
			plannedPeriod.readAttributes(pPeriod, POS_UNICO_ELEMENTO);
		}else{
			System.err.print("ERROR FATAL: ProcurementProject -> PLANNED PERIOD no existe\n");
		}
	}

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
	
	public void readContractExtension(Element pp, int POS_UNICO_ELEMENTO){
		this.contractExtension = null;
		
		Element ce = (Element) pp.getElementsByTagName("cac:ContractExtension").item(POS_UNICO_ELEMENTO);
		if (ce != null){
			this.contractExtension = new ContractExtension();
			this.contractExtension.readAttributes(ce, POS_UNICO_ELEMENTO);
		}
	}
	
	public void print(){
		System.out.print("** PROCUREMENT PROJECT **\n" +
				"--> Name: " + name + "\n" +
				"--> TypeCode: " + typeCode + "\n" +
				"--> SubTypeCode: " + subTypeCode + "\n" +
				"--------------------------------\n");
		
		if (budgetAmount != null){
			budgetAmount.print();
		}else{
			System.out.print("*** BUDGET AMOUNT: null ***\n" +
							 "--------------------------------\n");
		}
		
		if (plannedPeriod.getStartDate() != null ||
				plannedPeriod.getEndDate() != null || 
				plannedPeriod.getDurationMeasure() != 0.0){
			plannedPeriod.print();
		}else{
			System.out.print("*** PLANNED PERIOD: no existen medidores ***\n" +
							 "--------------------------------\n");
		}
		
		if (requiredCommodityClassificationList != null){
			for (RequiredCommodityClassification rcc : requiredCommodityClassificationList){
				rcc.print();
			}
		}else{
			System.out.print("*** REQUIRED COMMODITY CLASSIFICATION: null ***\n" +
							 "--------------------------------\n");
		}
		
		if (realizedLocation != null){
			realizedLocation.print();
		}else{
			System.out.print("*** REALIZED LOCATION: null ***\n" +
							 "--------------------------------\n");
		}
		
		if (contractExtension != null){
			contractExtension.print();
		}else{
			System.out.print("*** CONTRACT EXTENSION: null ***\n" +
							 "--------------------------------\n");
		}
		System.out.print("===============================================================\n");
	}
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public ProcurementProject(){}
	
	public ProcurementProject(String name, PlannedPeriod plannedPeriod) {
		this.name = name;
		this.plannedPeriod = plannedPeriod;
	}

	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/

	
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public int getTypeCode() {
		return typeCode;
	}


	public void setTypeCode(int typeCode) {
		this.typeCode = typeCode;
	}


	public int getSubTypeCode() {
		return subTypeCode;
	}


	public void setSubTypeCode(int subTypeCode) {
		this.subTypeCode = subTypeCode;
	}


	public BudgetAmount getBudgetAmount() {
		return budgetAmount;
	}


	public void setBudgetAmount(BudgetAmount budgetAmount) {
		this.budgetAmount = budgetAmount;
	}


	public PlannedPeriod getPlannedPeriod() {
		return plannedPeriod;
	}


	public void setPlannedPeriod(PlannedPeriod plannedPeriod) {
		this.plannedPeriod = plannedPeriod;
	}


	public RequiredCommodityClassification[] getRequiredCommodityClassificationList() {
		return requiredCommodityClassificationList;
	}


	public void setRequiredCommodityClassificationList(
			RequiredCommodityClassification[] requiredCommodityClassificationList) {
		this.requiredCommodityClassificationList = requiredCommodityClassificationList;
	}


	public RealizedLocation getRealizedLocation() {
		return realizedLocation;
	}


	public void setRealizedLocation(RealizedLocation realizedLocation) {
		this.realizedLocation = realizedLocation;
	}


	public ContractExtension getContractExtension() {
		return contractExtension;
	}


	public void setContractExtension(ContractExtension contractExtension) {
		this.contractExtension = contractExtension;
	}
}
