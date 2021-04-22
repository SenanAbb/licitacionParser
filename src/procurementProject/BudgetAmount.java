package procurementProject;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @params
 * 		estimatedOverallContractAmount: double [1]
 * 		totalAmount: double [1]
 * 		taxExclusiveAmount: double [1]
 */
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
		try{
			estimatedOverallContractAmount = Double.parseDouble(estimatedOverallContractAmountNode.getTextContent());
			estimatedOverallContractAmountCurrencyID = estimatedOverallContractAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
			
			this.estimatedOverallContractAmount = estimatedOverallContractAmount;
			this.estimatedOverallContractAmountCurrencyID = estimatedOverallContractAmountCurrencyID;
		}catch (NullPointerException e){
			System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> ESTIMATED OVERALL CONTRACT AMOUNT no existe");
		}
		
		totalAmountNode = budgetAmount.getElementsByTagName("cbc:TotalAmount").item(POS_UNICO_ELEMENTO);
		try{
			totalAmount = Double.parseDouble(totalAmountNode.getTextContent());
			totalAmountCurrencyID = totalAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
			
			this.totalAmount = totalAmount;
			this.totalAmountCurrencyID = totalAmountCurrencyID;
		}catch (NullPointerException e){
			System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> TOTAL AMOUNT no existe");
		}
		
		taxExclusiveAmountNode = budgetAmount.getElementsByTagName("cbc:TaxExclusiveAmount").item(POS_UNICO_ELEMENTO);
		try{
			taxExclusiveAmount = Double.parseDouble(taxExclusiveAmountNode.getTextContent());
			taxExclusiveAmountCurrencyID = taxExclusiveAmountNode.getAttributes().getNamedItem("currencyID").getTextContent();
			
			this.taxExclusiveAmount = taxExclusiveAmount;
			this.taxExclusiveAmountCurrencyID = taxExclusiveAmountCurrencyID;
		}catch (NullPointerException e){
			System.err.println("ERROR FATAL: ProcurementProject -> BudgetAmount -> TAX EXCLUSIVE AMOUNT no existe");
		}
	}
	
	public void print(){
		System.out.print("*** BUDGET AMOUNT ***\n" +
				"---> Estimated Overall Contract Amount: " + estimatedOverallContractAmount + " " + estimatedOverallContractAmountCurrencyID + "\n" +
				"---> Total Amount: " + totalAmount + " " + totalAmountCurrencyID + "\n" +
				"---> Tax Exclusive Amount: " + taxExclusiveAmount + " " + taxExclusiveAmountCurrencyID + "\n" +
				"--------------------------------\n");
	}
}
