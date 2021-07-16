package documents;

import org.w3c.dom.Element;

/*
 * 		URI: String[0..1]
 * 		documentHash: String[0..1]
 *		fileName: String[0..1]
 */
public class ExternalReference {
	private String URI, documentHash, fileName;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param er El cac:ExternalReference que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element er, int POS_UNICO_ELEMENTO) {
		this.URI = null;
		this.documentHash = null;
		this.fileName = null;
		
		/* URI */
		Element uri = (Element) er.getElementsByTagName("cbc:URI").item(POS_UNICO_ELEMENTO);
		if (uri != null){
			this.URI = uri.getTextContent();
		}

		/* DOCUMENT HASH */
		Element dh = (Element) er.getElementsByTagName("cbc:DocumentHash").item(POS_UNICO_ELEMENTO);
		if (dh != null){
			this.documentHash = dh.getTextContent();
		}
		
		/* FILE NAME */
		Element fn = (Element) er.getElementsByTagName("cbc:FileName").item(POS_UNICO_ELEMENTO);
		if (fn != null){
			this.fileName = fn.getTextContent();
		}
	}

	public String getURI() {
		return URI;
	}

	public String getDocumentHash() {
		return documentHash;
	}

	public String getFileName() {
		return fileName;
	}
}
