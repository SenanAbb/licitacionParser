package tenderingProcess;

import org.w3c.dom.Element;

/**
 * @params
 * 		limitationDescription: String[0..1]
 * 		expectedQuantity: int[0..1]
 *		maximumQuantity: int[0..1]
 *		minimumQuantity: int[0..1]
 */
public class EconomicOperatorShortList {
	private String limitationDescription;
	private int expectedQuantity, maximumQuantity, minimumQuantity;
	
	public void readAttributes(Element eosl, int POS_UNICO_ELEMENTO) {
		this.limitationDescription = null;
		this.expectedQuantity = -1;
		this.maximumQuantity = -1;
		this.minimumQuantity = -1;
		
		/* LIMITATION DESCRIPTION */
		Element description = (Element) eosl.getElementsByTagName("cbc:LimitationDescription").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.limitationDescription = description.getTextContent();
		}
		
		/* EXPECTED QUANTITY */
		Element eq = (Element) eosl.getElementsByTagName("cbc:ExpectedQuantity").item(POS_UNICO_ELEMENTO);
		if (eq != null){
			this.expectedQuantity = Integer.parseInt(eq.getTextContent());
		}
		
		/* MINIMUM QUANTITY */
		Element minq = (Element) eosl.getElementsByTagName("cbc:MinimumQuantity").item(POS_UNICO_ELEMENTO);
		if (minq != null){
			this.minimumQuantity = Integer.parseInt(minq.getTextContent());
		}
		
		/* MINIMUM QUANTITY */
		Element maxq = (Element) eosl.getElementsByTagName("cbc:MaximumQuantity").item(POS_UNICO_ELEMENTO);
		if (maxq != null){
			this.maximumQuantity = Integer.parseInt(maxq.getTextContent());
		}
	}

	public void print(){
		System.out.print("*** ECONOMIC OPERATOR SHORT LIST ***\n" +
						 "---> Limitation Description: " + limitationDescription + "\n" +
						 "---> Expected Quantity: " + expectedQuantity + "\n" +
						 "---> Minimum Quantity: " + minimumQuantity + "\n" +
						 "---> Maximum Quantity: " + maximumQuantity + "\n" +
						 "--------------------------------\n");
	}
}
