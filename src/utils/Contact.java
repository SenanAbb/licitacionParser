package utils;

import org.w3c.dom.Element;

public class Contact {
	private String name, electronicMail, telephone, telefax; 
	
	public void readAttributes(Element c, int POS_UNICO_ELEMENTO) {
		this.name = null;
		this.electronicMail = null;
		this.telephone = null;
		this.telefax = null;
		
		/* Name */
		Element name = (Element) c.getElementsByTagName("cbc:Name").item(POS_UNICO_ELEMENTO);
		if (name != null){
			this.name = name.getTextContent();
		}
		
		/* Email */
		Element email = (Element) c.getElementsByTagName("cbc:ElectronicMail").item(POS_UNICO_ELEMENTO);
		if (email != null){
			this.electronicMail = email.getTextContent();
		}
		
		/* Telephone */
		Element telephone = (Element) c.getElementsByTagName("cbc:Telephone").item(POS_UNICO_ELEMENTO);
		if (telephone != null){
			this.telephone = telephone.getTextContent();
		}
		
		/* Telefax */
		Element telefax = (Element) c.getElementsByTagName("cbc:Telefax").item(POS_UNICO_ELEMENTO);
		if (telefax != null){
			this.telefax = telefax.getTextContent();
		}
	}

	public void print(){
		System.out.print("**** CONTACT ****\n" + 
						 "----> Name: " + name + "\n" +
						 "----> Electronic mail: " + electronicMail + "\n" +
						 "----> Telephone: " + telephone + "\n" +
						 "----> Telefax: " + telefax + "\n" +
						 "--------------------------------\n");	
	}
	
	
	public Contact(){}
}
