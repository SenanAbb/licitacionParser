package utils;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;

import com.mysql.cj.jdbc.CallableStatement;

/**
 * @params
 *		name: String [0..1]
 *		electronicMail: String [0..1]
 *		telephone: String [0..1]
 *		telefax: String [0..1]
 */
public class Contact {
	private String name, electronicMail, telephone, telefax; 
	
	// ID
	private int contact;
	
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

	public void writeDataTBLPartyContact(int party, Connection conn) {
		writeData(conn);
		TablasAuxiliares.writeDataTBLPartyContact(party, contact, conn);
	}

	private void writeData(Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newContact(?, ?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("name", this.name);
			sentencia.setString("electronicmail", this.electronicMail);
			sentencia.setString("telephone", this.telephone);
			sentencia.setString("telefax", this.telefax);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("contact", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			this.contact = sentencia.getInt("contact");	
		} catch (SQLException e){
			System.out.println("[Party] Error para rollback: " + e.getMessage());
			e.printStackTrace();
			
			// Si algo ha fallado, hacemos rollback para deshacer todo y no grabar nada en la BD
			if (conn != null){
				try {
					conn.rollback();
				} catch (SQLException e1) {
					System.out.println("[Party] Error haciendo rollback: " + e.getMessage());
					e1.printStackTrace();
				}
			}
		}
	}
}
