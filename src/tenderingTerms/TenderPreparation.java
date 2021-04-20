package tenderingTerms;

import org.w3c.dom.Element;

/**
 * @params
 *		tenderEnvelopeID: String[1]
 *		tenderEnvelopeTypeCode: int[1]
 *		openEventID: String[0..1]
 *		description: String[0..1]
 */
public class TenderPreparation {
	private String tenderEnvelopeID, openEventID, description;
	private int tenderEnvelopeTypeCode;
	
	public void readAttributes(Element tpl, int POS_UNICO_ELEMENTO) {
		this.tenderEnvelopeID = null;
		this.openEventID = null;
		this.description = null;
		this.tenderEnvelopeTypeCode = -1;
		
		/* TENDER ENVELOPE ID */
		Element teid = (Element) tpl.getElementsByTagName("cbc:TenderEnvelopeID").item(POS_UNICO_ELEMENTO);
		if (teid != null){
			this.tenderEnvelopeID = teid.getTextContent();
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> TenderPreparation -> TENDER ENVELOPE ID no existe\n");
		}
		
		/* TENDER ENVELOPE TYPE CODE */
		Element tetc = (Element) tpl.getElementsByTagName("cbc:TenderEnvelopeTypeCode").item(POS_UNICO_ELEMENTO);
		if (tetc != null){
			this.tenderEnvelopeTypeCode = Integer.parseInt(tetc.getTextContent());
		}else{
			System.err.print("ERROR FATAL: TenderingTerms -> TenderPreparation -> TENDER ENVELOPE TYPE CODE no existe\n");
		}
		
		/* OPEN EVENT ID */
		Element oeid = (Element) tpl.getElementsByTagName("cbc:OpenEventID").item(POS_UNICO_ELEMENTO);
		if (oeid != null){
			this.openEventID = oeid.getTextContent();
		}
		
		/* DESCRIPTION */
		Element description = (Element) tpl.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}

	public void print(){
		System.out.print("*** TENDER PREPARATION ***\n" +
						 "---> Tender Envelope ID: " + tenderEnvelopeID + "\n" +
						 "---> Tender Envelope Type Code: " + tenderEnvelopeTypeCode + "\n" +
						 "---> Open Event ID: " + openEventID + "\n" +
						 "---> Description: " + description + "\n" +
						 "--------------------------------\n");
	}
}
