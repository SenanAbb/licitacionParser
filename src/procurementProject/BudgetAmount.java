package procurementProject;

public class BudgetAmount {
	private double estimatedOverallContractAmount, totalAmount, taxExclusiveAmount;
	//Atributos
	private String estimatedOverallContractAmountCurrencyID,
		totalAmountCurrencyID,
		taxExclusiveAmountCurrencyID;
	
	public void print(){
		System.out.print("** BUDGET AMOUNT **\n" +
				"--> Estimated Overall Contract Amount: " + estimatedOverallContractAmount + " " + estimatedOverallContractAmountCurrencyID + "\n" +
				"--> Total Amount: " + totalAmount + " " + totalAmountCurrencyID + "\n" +
				"--> Tax Exclusive Amount: " + taxExclusiveAmount + " " + taxExclusiveAmountCurrencyID + "\n" +
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
