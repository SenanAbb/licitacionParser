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
	
	public void readBudgetAmount(Element pp, int POS_UNICO_ELEMENTO){
		Element budgetAmount = (Element) pp.getElementsByTagName("cac:BudgetAmount").item(POS_UNICO_ELEMENTO);
		
		// Si no encuentro el bloque entero, lo pongo a null y termino la ejecución, pues el resto de variables y subclases seguro no van a estar
		if (budgetAmount != null){
			this.budgetAmount = new BudgetAmount();
			
			// Inicializamos todas las variables que tendrá la subclase BudgetAmount
			Node estimatedOverallContractAmountNode = null, totalAmountNode = null, taxExclusiveAmountNode = null;
			double estimatedOverallContractAmount = -1.0,
					totalAmount = -1.0,
					taxExclusiveAmount = -1.0;
			String estimatedOverallContractAmountCurrencyID = null, 
					totalAmountCurrencyID = null,
					taxExclusiveAmountCurrencyID = null;
			
			/* Comprobamos que existen las variables OBLIGATORIAS (estimated... y totalAmount)
				-> Si existen: Creamos el objeto
				-> Si no existen: Lanzamos error
			*/
			estimatedOverallContractAmountNode = budgetAmount.getElementsByTagName("cbc:EstimatedOverallContractAmount").item(POS_UNICO_ELEMENTO);
			if (estimatedOverallContractAmountNode != null){
				estimatedOverallContractAmount = Double.parseDouble(estimatedOverallContractAmountNode.getTextContent());
				estimatedOverallContractAmountCurrencyID = estimatedOverallContractAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
				
				this.budgetAmount.setEstimatedOverallContractAmount(estimatedOverallContractAmount);
				this.budgetAmount.setEstimatedOverallContractAmountCurrencyID(estimatedOverallContractAmountCurrencyID);
			}else{
				System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> ESTIMATED OVERALL CONTRACT AMOUNT no existe");
				this.budgetAmount.setEstimatedOverallContractAmount(estimatedOverallContractAmount);
				this.budgetAmount.setEstimatedOverallContractAmountCurrencyID(estimatedOverallContractAmountCurrencyID);
			}
			
			totalAmountNode = budgetAmount.getElementsByTagName("cbc:TotalAmount").item(POS_UNICO_ELEMENTO);
			if (totalAmountNode != null){
				totalAmount = Double.parseDouble(totalAmountNode.getTextContent());
				totalAmountCurrencyID = totalAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
				
				this.budgetAmount.setTotalAmount(totalAmount);
				this.budgetAmount.setTotalAmountCurrencyID(totalAmountCurrencyID);
			}else{
				System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> TOTAL AMOUNT no existe");
				this.budgetAmount.setTotalAmount(totalAmount);
				this.budgetAmount.setTotalAmountCurrencyID(totalAmountCurrencyID);
			}
			
			taxExclusiveAmountNode = budgetAmount.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
			if (taxExclusiveAmountNode != null){
				taxExclusiveAmount = Double.parseDouble(taxExclusiveAmountNode.getTextContent());
				taxExclusiveAmountCurrencyID = taxExclusiveAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
				
				this.budgetAmount.setTaxExclusiveAmount(taxExclusiveAmount);
				this.budgetAmount.setTaxExclusiveAmountCurrencyID(taxExclusiveAmountCurrencyID);
			}else{
				this.budgetAmount.setTaxExclusiveAmount(taxExclusiveAmount);
				this.budgetAmount.setTaxExclusiveAmountCurrencyID(taxExclusiveAmountCurrencyID);
			}
		}else{
			this.budgetAmount = null;
		}
	}
	
	public void readRequiredCommodityClassification(Element pp, int POS_UNICO_ELEMENTO){
		this.requiredCommodityClassificationList = null;
		
		NodeList rccNodeList = pp.getElementsByTagName("cac:RequiredCommodityClassification");
		
		// Si hay algún Required Commodity Classification lo creo, sino lo dejo a null;
		if (rccNodeList.getLength() > 0){
			this.requiredCommodityClassificationList = new RequiredCommodityClassification[rccNodeList.getLength()];
			
			for (int i = 0; i < rccNodeList.getLength(); i++){
				Element rcc = (Element) rccNodeList.item(i);
				Node itemCode = rcc.getElementsByTagName("cbc:ItemClassificationCode").item(POS_UNICO_ELEMENTO);
				if (itemCode != null){
					this.requiredCommodityClassificationList[i] = new RequiredCommodityClassification();
					this.requiredCommodityClassificationList[i].setItemClassificationCode(Integer.parseInt(itemCode.getTextContent()));
				}
			}
		}
	}

	public void readPlannedPeriod(Element pp, int POS_UNICO_ELEMENTO){
		PlannedPeriod plannedPeriod = new PlannedPeriod();
		
		Element pPeriod = (Element) pp.getElementsByTagName("cac:PlannedPeriod").item(POS_UNICO_ELEMENTO);
		if (pPeriod != null){
			Element start = (Element) pPeriod.getElementsByTagName("cbc:StartDate").item(POS_UNICO_ELEMENTO);
			Element end = (Element) pPeriod.getElementsByTagName("cbc:EndDate").item(POS_UNICO_ELEMENTO);
			Element duration = (Element) pPeriod.getElementsByTagName("cbc:DurationMeasure").item(POS_UNICO_ELEMENTO);
			
			if(start != null && end != null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = format.parse(start.getTextContent());
					endDate = format.parse(end.getTextContent());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				plannedPeriod.setStartDate(startDate);
				plannedPeriod.setEndDate(endDate);
				
				this.plannedPeriod = plannedPeriod;
			}else if (start != null && duration != null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				Date startDate = null;
				try {
					startDate = format.parse(start.getTextContent());
				} catch (ParseException e) {
					e.printStackTrace();
				}
				plannedPeriod.setStartDate(startDate);
				plannedPeriod.setDurationMeasure(Double.parseDouble(duration.getTextContent()));
				String durationMeasureUnitCode = duration.getAttributes().getNamedItem("unitCode").getTextContent();
				plannedPeriod.setUnitCode(durationMeasureUnitCode);
				
				this.plannedPeriod = plannedPeriod;
			}else if (duration != null){
				plannedPeriod.setDurationMeasure(Double.parseDouble(duration.getTextContent()));
				String durationMeasureUnitCode = duration.getAttributes().getNamedItem("unitCode").getTextContent();
				plannedPeriod.setUnitCode(durationMeasureUnitCode);

				this.plannedPeriod = plannedPeriod;
			}else{
				plannedPeriod.setStartDate(null);
				plannedPeriod.setEndDate(null);
				plannedPeriod.setDurationMeasure(-1);

				this.plannedPeriod = plannedPeriod;
			}
		}else{
			this.plannedPeriod = plannedPeriod;
			System.err.print("ERROR FATAL: ProcurementProject -> PLANNED PERIOD no existe\n");
		}
	}

	public void readRealizedLocation(Element pp, int POS_UNICO_ELEMENTO){
		this.realizedLocation = null;
		
		Element rl = (Element) pp.getElementsByTagName("cac:RealizedLocation").item(POS_UNICO_ELEMENTO);
		// -> Si existe este elemento, continuamos, sino creamos el objeto NULL
		// -> Esto se puede hacer porque Realized Location tiene cardinal [0..1]
		
		if (rl != null){
			realizedLocation = new RealizedLocation();
			
			Element countrySubentity = (Element) rl.getElementsByTagName("cbc:CountrySubentity").item(POS_UNICO_ELEMENTO);
			if (countrySubentity != null){
				this.realizedLocation.setCountrySubentity(countrySubentity.getTextContent());
			}
			
			Element countrySubentityCode = (Element) rl.getElementsByTagName("cbc:CountrySubentityCode").item(POS_UNICO_ELEMENTO);
			if (countrySubentityCode != null){
				this.realizedLocation.setCountrySubentityCode(countrySubentityCode.getTextContent());
			}
			
			// ADDRESS
			Address ad = null;
			Element address = (Element) rl.getElementsByTagName("cac:Address").item(POS_UNICO_ELEMENTO);
			if (address != null){
				ad = new Address();
				
				// ADDRESS FORMATCODE
				Element afc = (Element) address.getElementsByTagName("cbc:AddressFormatCode").item(POS_UNICO_ELEMENTO);
				if (afc != null){
					ad.setCityName(afc.getTextContent());
				}
				
				// CITY NAME
				Element cn = (Element) address.getElementsByTagName("cbc:CityName").item(POS_UNICO_ELEMENTO);
				if (cn != null){
					ad.setCityName(cn.getTextContent());
				}
				
				// POSTAL ZONE
				Element pz = (Element) address.getElementsByTagName("cbc:PostalZone").item(POS_UNICO_ELEMENTO);
				if (pz != null){
					ad.setCityName(pz.getTextContent());
				}
				
				// ADDRESS LINE
				Element al = (Element) address.getElementsByTagName("cbc:AdressLine").item(POS_UNICO_ELEMENTO);
				if (al != null){
					ad.setCityName(al.getTextContent());
				}
				
				// COUNTRY
				Element country = (Element) address.getElementsByTagName("cac:Country").item(POS_UNICO_ELEMENTO);
				if (country != null){
					Element idCode = (Element) country.getElementsByTagName("cbc:IdentificationCode").item(POS_UNICO_ELEMENTO);
					if (idCode == null){
						System.err.print("ERROR FATAL: ProcurementProject -> RealizedLocation -> Address -> Country -> IDENTIFICATION CODE no existe\n");
					}
					String identificationCode = idCode.getTextContent();
			
					Country c = new Country(identificationCode);
					
					Element countryName = (Element) country.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
					if (countryName != null){
						c.setName(countryName.getTextContent());
					}

					ad.setCountry(c);
				}
			}
			this.realizedLocation.setAddress(ad);
		}
	}
	
	public void readContractExtension(Element pp, int POS_UNICO_ELEMENTO){
		this.contractExtension = null;
		
		Element ce = (Element) pp.getElementsByTagName("cac:ContractExtension").item(POS_UNICO_ELEMENTO);
		if (ce != null){
			contractExtension = new ContractExtension();
			
			Element optionsDesc = (Element) ce.getElementsByTagName("cbc:OptionsDescription").item(POS_UNICO_ELEMENTO);
			if (optionsDesc != null){
				this.contractExtension.setOptionsDescription(optionsDesc.getTextContent());
			}
			
			Element ovp = (Element) ce.getElementsByTagName("cac:OptionValidityPeriod").item(POS_UNICO_ELEMENTO);
			OptionValidityPeriod optionValidityPeriod = null;
			
			if (ovp != null){
				optionValidityPeriod = new OptionValidityPeriod();
				String description = ovp.getTextContent();
				optionValidityPeriod.setDescription(description.trim());
			}

			this.contractExtension.setOptionValidityPeriod(optionValidityPeriod);
		}
	}
	
	public void print(){
		try{
			System.out.print("* PROCUREMENT PROJECT *\n" +
					"-> Name: " + name + "\n" +
					"-> TypeCode: " + typeCode + "\n" +
					"-> SubTypeCode: " + subTypeCode + "\n" +
					"--------------------------------\n");
			
			if (budgetAmount != null){
				budgetAmount.print();
			}else{
				System.out.print("** BUDGET AMOUNT: null **\n" +
						"--------------------------------\n");
			}
			
			if (plannedPeriod.getStartDate() != null ||
					plannedPeriod.getEndDate() != null || 
					plannedPeriod.getDurationMeasure() != 0.0){
				plannedPeriod.print();
			}else{
				System.out.print("** PLANNED PERIOD: no existen medidores **\n" +
						"--------------------------------\n");
			}
			
			if (requiredCommodityClassificationList != null){
				for (RequiredCommodityClassification rcc : requiredCommodityClassificationList){
					rcc.print();
				}
			}else{
				System.out.print("** REQUIRED COMMODITY CLASSIFICATION: null **\n" +
						"--------------------------------\n");
			}
			
			if (realizedLocation != null){
				realizedLocation.print();
			}else{
				System.out.print("** REALIZED LOCATION: null **\n" +
						"--------------------------------\n");
			}
			
			if (contractExtension != null){
				contractExtension.print();
			}else{
				System.out.print("** CONTRACT EXTENSION: null **\n" +
						"--------------------------------\n");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
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
