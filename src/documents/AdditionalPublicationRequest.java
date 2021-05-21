package documents;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.w3c.dom.Element;

/**
 * @params
 * 		agencyID: String[0..1]
 *		sendDate: Date[0..1]
 */
public class AdditionalPublicationRequest {
	private String agencyID;
	private Date sendDate;
	
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
	
	public void print(){
		System.out.print("**** ADDITIONAL PUBLICATION REQUEST ****\n" +
						 "----> Agency ID: " + agencyID + "\n" + 
						 "----> Send Date: " + sendDate + "\n" +
						 "--------------------------------\n");
	}

	public java.sql.Date getSendDate() {
		if (sendDate != null){
			return new java.sql.Date(sendDate.getTime());	
		}else{
			return null;
		}
	}
}
