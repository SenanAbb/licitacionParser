package tenderingProcess;

import org.w3c.dom.Element;

/**
 * @params
 *		reasonCode: int[0..1]
 *		description: String[0..1]
 */
public class ProcessJustification {
	private int reasonCode;
	private String description;
	
	public void readAttributes(Element pj, int POS_UNICO_ELEMENTO) {
		this.reasonCode = -1;
		this.description = null;
		
		Element rc = (Element) pj.getElementsByTagName("cbc:ReasonCode").item(POS_UNICO_ELEMENTO);
		if (rc != null){
			this.reasonCode = Integer.parseInt(rc.getTextContent());
		}
		
		Element desc = (Element) pj.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (desc != null){
			this.description = desc.getTextContent();
		}
	}

	public void print(){
		System.out.print("*** PROCESS JUSTIFICATION ***\n" +
						 "---> Reason Code: " + reasonCode + "\n" +
						 "---> Description: " + description + "\n" +
						 "--------------------------------\n");
	}
}
