package tenderingTerms;

import org.w3c.dom.Element;

/*
 * 		description: String[1]
 *		weightNumeric: double[0..1]
 */
public class AwardingCriteria {
	private String description;
	private double weightNumeric;

	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param ac El cac:AwardingCriteria que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element ac, int POS_UNICO_ELEMENTO) {
		this.description = null;
		this.weightNumeric = -1;
		
		/* DESCRIPTION */
		Element description = (Element) ac.getElementsByTagName("cbc:Description").item(POS_UNICO_ELEMENTO);
		try {
			this.description = description.getTextContent();
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: TenderingTerms -> AwardingTerms -> AwardingCriteria -> DESCRIPTION no existe\n");
		}
		
		/* WEIGHT NUMERIC */
		Element wn = (Element) ac.getElementsByTagName("cbc:WeightNumeric").item(POS_UNICO_ELEMENTO);
		if (wn != null){
			this.weightNumeric = Double.parseDouble(wn.getTextContent());
		}
	}

	public String getDescription() {
		return description;
	}

	public double getWeightNumeric() {
		return weightNumeric;
	}
}
