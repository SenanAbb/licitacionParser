package procurementProject;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class BudgetAmount {
	private double estimatedOverallContractAmount, totalAmount, taxExclusiveAmount;
	//Atributos
	private String estimatedOverallContractAmountCurrencyID,
		totalAmountCurrencyID,
		taxExclusiveAmountCurrencyID;

	public void readAttributes(Element budgetAmount, int POS_UNICO_ELEMENTO) {
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
			
			this.estimatedOverallContractAmount = estimatedOverallContractAmount;
			this.estimatedOverallContractAmountCurrencyID = estimatedOverallContractAmountCurrencyID;
		}else{
			System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> ESTIMATED OVERALL CONTRACT AMOUNT no existe");
		}
		
		totalAmountNode = budgetAmount.getElementsByTagName("cbc:TotalAmount").item(POS_UNICO_ELEMENTO);
		if (totalAmountNode != null){
			totalAmount = Double.parseDouble(totalAmountNode.getTextContent());
			totalAmountCurrencyID = totalAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
			
			this.totalAmount = totalAmount;
			this.totalAmountCurrencyID = totalAmountCurrencyID;
		}else{
			System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> TOTAL AMOUNT no existe");
		}
		
		taxExclusiveAmountNode = budgetAmount.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		if (taxExclusiveAmountNode != null){
			taxExclusiveAmount = Double.parseDouble(taxExclusiveAmountNode.getTextContent());
			taxExclusiveAmountCurrencyID = taxExclusiveAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
			
			this.taxExclusiveAmount = taxExclusiveAmount;
			this.taxExclusiveAmountCurrencyID = taxExclusiveAmountCurrencyID;
		}
	}
	
	public void print(){
		System.out.print("*** BUDGET AMOUNT ***\n" +
				"---> Estimated Overall Contract Amount: " + estimatedOverallContractAmount + " " + estimatedOverallContractAmountCurrencyID + "\n" +
				"---> Total Amount: " + totalAmount + " " + totalAmountCurrencyID + "\n" +
				"---> Tax Exclusive Amount: " + taxExclusiveAmount + " " + taxExclusiveAmountCurrencyID + "\n" +
				"--------------------------------\n");
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public BudgetAmount(){}
	
	public BudgetAmount(double estimatedOverallContractAmount, double totalAmount){
		this.estimatedOverallContractAmount = estimatedOverallContractAmount;
		this.totalAmount = totalAmount;
	}

	
	/*************************/
	/** GETTERS AND SETTERS **/
	/*************************/
	
	
	public double getEstimatedOverallContractAmount() {
		return estimatedOverallContractAmount;
	}

	public void setEstimatedOverallContractAmount(
			double estimatedOverallContractAmount) {
		this.estimatedOverallContractAmount = estimatedOverallContractAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTaxExclusiveAmount() {
		return taxExclusiveAmount;
	}

	public void setTaxExclusiveAmount(double taxExclusiveAmount) {
		this.taxExclusiveAmount = taxExclusiveAmount;
	}


	public String getEstimatedOverallContractAmountCurrencyID() {
		return estimatedOverallContractAmountCurrencyID;
	}

	public void setEstimatedOverallContractAmountCurrencyID(
			String estimatedOverallContractAmountCurrencyID) {
		this.estimatedOverallContractAmountCurrencyID = estimatedOverallContractAmountCurrencyID;
	}

	public String getTotalAmountCurrencyID() {
		return totalAmountCurrencyID;
	}

	public void setTotalAmountCurrencyID(String totalAmountCurrencyID) {
		this.totalAmountCurrencyID = totalAmountCurrencyID;
	}

	public String getTaxExclusiveAmountCurrencyID() {
		return taxExclusiveAmountCurrencyID;
	}

	public void setTaxExclusiveAmountCurrencyID(String taxExclusiveAmountCurrencyID) {
		this.taxExclusiveAmountCurrencyID = taxExclusiveAmountCurrencyID;
	}
}
