import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import Parser.Parser;

public class main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException, ParseException {
		Parser p = new Parser();
		boolean primera_lectura = p.getPrimeraLectura();
//		boolean primera_lectura = true;
		p.createIds();
		p.setFechaLimite(primera_lectura);
		
		String URL = "https://contrataciondelsectorpublico.gob.es/sindicacion/sindicacion_643/licitacionesPerfilesContratanteCompleto3.atom";
		
		p.readOpenData(primera_lectura, URL);
		
//		rellenar(p);
//		leerDirectorio(primera_lectura);
//		leerArchivo(primera_lectura);
	}
	
	private static void leerDirectorio(boolean primera_lectura) throws FileNotFoundException, SAXException, IOException, SQLException, ParserConfigurationException {
		ArrayList<String> exp = new ArrayList<String>();
		//exp.add("PDT.-3.9/19");
		exp.add("PDT.-3.8/19");
		
		/* PARSER DE DIRECTORIO */
		String path = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Licitaciones 20-21/PRUEBAS_LOTES/";
		String[] files = null;
		
		File f = new File(path);
		File[] listado = f.listFiles();
		
		Parser p = new Parser();
		
		p.createIds();
		p.createFeeds(path);
		try {
			p.setFechaLimite(primera_lectura);
		} catch (ParseException e) {e.printStackTrace();}
		
		if (listado == null || listado.length == 0) {
		    System.out.println("No hay elementos dentro de la carpeta actual");
		}else {
		    for (int i=0; i< listado.length; i++) {
		    	String sub_path = path.concat(listado[i].getName());
		    	
		    	if (!listado[i].isDirectory()){
		    		System.out.println("==============");
                	System.out.println(sub_path);
                	System.out.println("==============");
                	File file = new File(sub_path);
                	p.setURL(file);
                	p.readEntries(primera_lectura, null);           
		    	}else{
		    		System.out.println("----> " + listado[i].getName() + " <-----");
		    		files = getFiles(sub_path);
		    		if ( files != null ) {
		                int size = files.length;
		                for ( int j = 0; j < size; j ++ ) {
		                	System.out.println("==============");
		                	System.out.println("ARCHIVO Nº " + (j+1) + " DE " + size);
		                	System.out.println(files[j]);
		                	System.out.println("==============");
		                	File file = new File(files[j]);
		                	p.setURL(file);
		                	p.readEntries(primera_lectura, null);
		                }
		            }
		    	}
		    }
		}
	}
	
	private static void leerArchivo(boolean primera_lectura) throws FileNotFoundException, ParserConfigurationException, SAXException, IOException, SQLException{
		/** PARSER DE UN SOLO ARCHIVO */
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/LicitacionParser/Licitaciones 20-21/PRUEBAS/2.atom";
		//String URL = "Licitaciones 20-21/2020_12/licitacionesPerfilesContratanteCompleto3_20201230_181906_1";
		ArrayList<String> exp = new ArrayList<String>();
		exp.add("PDT.-3.9/19");
		exp.add("PDT.-3.8/19");
		
		Parser p = new Parser();
		p.setURL(new File(URL));
		p.createIds();
		p.readEntries(primera_lectura, null);
	}
	
	private static void rellenar(Parser p) throws ParserConfigurationException, SAXException, TransformerException{
		// Rellenar las tablas TypeCode
		System.out.println("ModosID"); p.writeModosId();
		System.out.println("TypeCode"); p.writeTypeCode();
		System.out.println("SubtypeCode"); p.writeSubtypeCodes();
		System.out.println("CPV"); p.writeCPV();
		System.out.println("CountryID"); p.writeCountryIdentificationCode();
		System.out.println("CountrySubID"); p.writeCountrySubentityCode();
		System.out.println("ProcedureCode"); p.writeProcedureCode();
		System.out.println("ContractingSystem"); p.writeContractingSystemTypeCode();
		System.out.println("Urgency"); p.writeUrgencyCode();
		System.out.println("Submission"); p.writeSubmissionMethodCode();
		System.out.println("Language"); p.writeLanguage();
		System.out.println("Procurement"); p.writeProcurementLegislation();
		System.out.println("ContractingParty"); p.writeContractingPartyTypeCode();
		System.out.println("ContractFolderStatus"); p.writeContractFolderStatusCode();
		System.out.println("TipoPliego"); p.writeTipoPliego();
		System.out.println("TipoPlazo"); p.writeTipoPlazo();
		System.out.println("FundingProgram"); p.writeFundingProgramCode();
		System.out.println("Guarantee"); p.writeGuaranteeTypeCode();
		System.out.println("RequiredBusiness"); p.writeRequiredBusinessProfileCode();
		System.out.println("Declaration"); p.writeDeclarationTypeCode();
		System.out.println("Technical"); p.writeTechnicalCapabilityTypeCode();
		System.out.println("Financial"); p.writeFinancialCapabilityTypeCode();
		System.out.println("TipoEvaluacion"); p.writeTipoEvaluacion();
		System.out.println("TenderResult"); p.writeTenderResultCode();
		System.out.println("ReasonCode"); p.writeReasonCode();
		System.out.println("NoticeTypeCode"); p.writeNoticeTypeCode();
		System.out.println("DocumentTypeCode"); p.writeDocumentTypeCode();
	}
	
	private static String[] getFiles(String path) {
		String[] arr_res = null;

        File f = new File(path);
        if ( f.isDirectory( )) {
            List<String> res   = new ArrayList<>();
            File[] arr_content = f.listFiles();

            int size = arr_content.length;

            for ( int i = 0; i < size; i ++ ) {
                if ( arr_content[ i ].isFile( ))
                res.add( arr_content[ i ].toString( ));
            }

            arr_res = res.toArray(new String[0]);
        } else {
        	System.err.println( "¡ Path NO válido !" );
        }
        return arr_res;
	}

}