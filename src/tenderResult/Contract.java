package tenderResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 *		id: String [0..1]
 *		issueDate: Date [0..1]
 */
public class Contract {
	private String id;
	private Date issueDate;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param contract El cac:Contract que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element contract, int POS_UNICO_ELEMENTO) {
		this.id = null;
		this.issueDate = null;
		
		Element id = (Element) contract.getElementsByTagName("cbc:ID").item(POS_UNICO_ELEMENTO);
		if (id != null){
			this.id = id.getTextContent();
		}
		
		Element issueDateElement = (Element) contract.getElementsByTagName("cbc:IssueDate").item(POS_UNICO_ELEMENTO);
		if (issueDateElement != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date issueDate = null;
			try {
				issueDate = format.parse(issueDateElement.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.issueDate = issueDate;
		}
	}

	public String getId() {
		return id;
	}

	public java.sql.Date getIssueDate() {
		if (issueDate != null){
			return new java.sql.Date(issueDate.getTime());	
		}else{
			return null;
		}
	}
}
