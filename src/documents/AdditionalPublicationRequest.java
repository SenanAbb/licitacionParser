package documents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/*
 * 		agencyID: String[0..1]
 *		sendDate: Date[0..1]
 */
public class AdditionalPublicationRequest {
	private String agencyID;
	private Date sendDate;
	
	/**
	 * Lee los atributos (las etiquetas cbc:...) del documento correspondiente a las variables de esta clase
	 * 
	 * @param apr El cac:AdditionalPublicationRequest que contiene la información
	 * @param POS_UNICO_ELEMENTO Constante que se refiere a la posición del array donde coger un dato
	 */
	public void readAttributes(Element apr, int POS_UNICO_ELEMENTO){
		this.agencyID = null;
		this.sendDate = null;
		
		Element aid = (Element) apr.getElementsByTagName("AgencyID").item(POS_UNICO_ELEMENTO);
		if (aid != null){
			this.agencyID = aid.getTextContent();
		}
		
		Element sd = (Element) apr.getElementsByTagName("cbc-place-ext:SendDate").item(POS_UNICO_ELEMENTO);
		if (sd != null){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date sendDate = null;
			try {
				sendDate = format.parse(sd.getTextContent());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.sendDate = sendDate;
		}
	}

	public java.sql.Date getSendDate() {
		if (sendDate != null){
			return new java.sql.Date(sendDate.getTime());	
		}else{
			return null;
		}
	}
}
