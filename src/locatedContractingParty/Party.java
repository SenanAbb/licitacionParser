package locatedContractingParty;

import java.sql.Connection;
import java.sql.SQLException;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.mysql.cj.jdbc.CallableStatement;

import utils.Contact;
import utils.PartyIdentification;
import utils.PartyName;
import utils.PostalAddress;

/**
 * @params
 * 		websiteURI: String[0..1]
 * 		buyerProfileURIID: String[0..1]
 * 		partyName: PartyName[1]
 * 		partyIdentificationList: PartyIdentification[] [1..3]
 * 		contact: Contact[0..1]
 * 		postalAddress: PostalAddress[0..1]
 */
public class Party {
	private String websiteURI, buyerProfileURIID;
	private PartyName partyName;
	private PartyIdentification[] partyIdentificationList;
	private Contact contact;
	private PostalAddress postalAddress;
	
	//ID
	private int party;
	
	public void readAttributes(Element p, int POS_UNICO_ELEMENTO){
		this.websiteURI = null;
		this.buyerProfileURIID = null;
		this.partyName = null;
		this.partyIdentificationList = null;
		this.contact = null;
		this.postalAddress = null;
		
		/* Website URI */
		Element wsURI = (Element) p.getElementsByTagName("cbc:WebsiteURI").item(POS_UNICO_ELEMENTO);
		if (wsURI != null){
			this.websiteURI = wsURI.getTextContent();
		}
		
		/* Buyer Profile URI ID */
		Element bpUID = (Element) p.getElementsByTagName("cbc:BuyerProfileURIID").item(POS_UNICO_ELEMENTO);
		if (bpUID != null){
			this.buyerProfileURIID = bpUID.getTextContent();
		}
		
		/* Party Name */
		Element pn = (Element) p.getElementsByTagName("cac:PartyName").item(POS_UNICO_ELEMENTO);
		try{
			this.partyName = new PartyName();
			this.partyName.readAttributes(pn, POS_UNICO_ELEMENTO);
		}catch (NullPointerException e){
			System.err.print("ERROR FATAL: LocatedContractingParty -> Party -> PARTY NAME no existe\n");
		}
		
		/* Party Identification */
		NodeList piNodeList = p.getElementsByTagName("cac:PartyIdentification");
		if(piNodeList.getLength() > 0){
			this.partyIdentificationList = new PartyIdentification[piNodeList.getLength()];
			for (int i = 0; i < piNodeList.getLength(); i++){
				PartyIdentification partyIdentification = new PartyIdentification();
				
				Element pi = (Element) piNodeList.item(i);
				partyIdentification.readAttributes(pi, POS_UNICO_ELEMENTO);
				
				this.partyIdentificationList[i] = partyIdentification;
			}
		}else{
			System.err.print("ERROR FATAL: LocatedContractingParty -> Party -> PARTY IDENTIFICATION no existe\n");
		}
		
		/* Contact  */
		Element c = (Element) p.getElementsByTagName("cac:Contact").item(POS_UNICO_ELEMENTO);
		if (c != null){
			this.contact = new Contact();
			this.contact.readAttributes(c, POS_UNICO_ELEMENTO);
		}
		
		/* Postal Address */
		Element pa = (Element) p.getElementsByTagName("cac:PostalAddress").item(POS_UNICO_ELEMENTO);
		if (pa != null){
			this.postalAddress = new PostalAddress();
			this.postalAddress.readAttributes(pa, POS_UNICO_ELEMENTO, false);
		}
	}
	
	public void print(){
		System.out.print("*** PARTY ***\n" +
						 "---> Website URI: " + websiteURI + "\n" + 
						 "---> Buyer Profile URI ID: " + buyerProfileURIID + "\n" +
						 "--------------------------------\n");
		partyName.print();
		System.out.print("**** PARTY IDENTIFICATION ****\n");
		for (PartyIdentification p : partyIdentificationList){
			p.print();
		}
		System.out.print("--------------------------------\n");
		contact.print();
		postalAddress.print();
		System.out.print("--------------------------------\n");
	}

	public void writeData(int located_contracting_party, Connection conn) {
		CallableStatement sentencia = null;
		
		try {
			sentencia = (CallableStatement) conn.prepareCall("{call newParty(?, ?, ?, ?)}");
			
			// Parametros del procedimiento almacenado
			sentencia.setString("websiteuri", this.websiteURI);
			sentencia.setString("buyerprofileuriid", this.buyerProfileURIID);
			sentencia.setInt("located_contracting_party", located_contracting_party);
			
			// Definimos los tipos de los params de salida del procedimiento almacenado
			sentencia.registerOutParameter("party", java.sql.Types.INTEGER);
			
			// Ejecutamos el procedimiento
			sentencia.execute();
			
			// Se obtiene la salida
			this.party = sentencia.getInt("party");

			// Graban las subclases
			this.partyName.writeDataTBLPartyPartyname(party, conn);
			
			for (PartyIdentification p : partyIdentificationList){
				p.writeDataTBLPartyPartyidentification(party, conn);
			}
			
			if (contact != null){
				contact.writeDataTBLPartyContact(party, conn);
			}
			
			if (postalAddress != null){
				postalAddress.writeDataTBLPartyPostalAddress(party, conn);
			}
			
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
