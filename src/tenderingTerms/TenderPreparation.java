package tenderingTerms;

import org.w3c.dom.Element;

/*
 *		tenderEnvelopeID: String[1]
 *		tenderEnvelopeTypeCode: int[1]
 *		openEventID: String[0..1]
 *		description: String[0..1]
 */
public class TenderPreparation {
	private String tenderEnvelopeID, openEventID, description;
	private int tenderEnvelopeTypeCode;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param tp El cac:TenderPreparation que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element tp, int POS_UNICO_ELEMENTO) {
		this.tenderEnvelopeID = null;
		this.openEventID = null;
		this.description = null;
		this.tenderEnvelopeTypeCode = -1;
		
		/* TENDER ENVELOPE ID */
		Element teid = (Element) tp.getElementsByTagName("cbc:TenderEnvelopeID").item(POS_UNICO_ELEMENTO);
		try{
			this.tenderEnvelopeID = teid.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> TenderPreparation -> TENDER ENVELOPE ID no existe\n");
		}
		
		/* TENDER ENVELOPE TYPE CODE */
		Element tetc = (Element) tp.getElementsByTagName("cbc:TenderEnvelopeTypeCode").item(POS_UNICO_ELEMENTO);
		try{
			this.tenderEnvelopeTypeCode = Integer.parseInt(tetc.getTextContent());
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> TenderPreparation -> TENDER ENVELOPE TYPE CODE no existe\n");
		}
		
		/* OPEN EVENT ID */
		Element oeid = (Element) tp.getElementsByTagName("cbc:OpenEventID").item(POS_UNICO_ELEMENTO);
		if (oeid != null){
			this.openEventID = oeid.getTextContent();
		}
		
		/* DESCRIPTION */
		Element description = (Element) tp.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		if (description != null){
			this.description = description.getTextContent();
		}
	}
}
