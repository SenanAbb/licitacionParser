package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/*
 *		line: String [1]
 */
public class AddressLine {
	private String line;

	/**
	 * Lee los atributos (las etiquitas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param al El cac:AddressLine que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element al, int POS_UNICO_ELEMENTO) {
		Element addressLine = (Element) al.getElementsByTagName("cbc:Line").item(POS_UNICO_ELEMENTO);
		if (addressLine != null){
			this.line = addressLine.getTextContent();
		}else{
			System.err.print("ERROR FATAL: Postal Address -> AddressLine -> LINE no existe\n");
		}
	}
	
	public String getLine() {
		return line;
	}
}
