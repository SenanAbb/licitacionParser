package tenderResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

public class Contract {
	private String id;
	private Date issueDate;
	
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
	
	public void print(){
		System.out.print("*** CONTRACT " + id + "***\n" +
				"---> ID: " + id + "\n" +
				"---> Issue Date: " + issueDate + "\n" +
				"--------------------------------\n");
	}
	
	
	/******************/
	/** CONSTRUCTORS **/
	/******************/
	
	
	public Contract(){}
}
