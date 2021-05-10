import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

import Parser.Parser;

public class main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, TransformerException {

		
		
		/** PARSER DE DIRECTORIO */
		ArrayList<String> exp = new ArrayList<String>();
		exp.add("PDT.-3.9/19");
		exp.add("PDT.-3.8/19");
		
		Parser p = new Parser();
		
		// Rellenar las tablas TypeCode
		p.writeSubtypeCodes();
		p.writeCPV();
		
//		String path = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/LicitacionParser/Licitaciones 20-21/";
//		String[] files = null;
//		
//		File f = new File(path);
//		File[] listado = f.listFiles();
//		if (listado == null || listado.length == 0) {
//		    System.out.println("No hay elementos dentro de la carpeta actual");
//		}else {
//		    for (int i=0; i< listado.length; i++) {
//		    	String sub_path = path.concat(listado[i].getName());
//		    	
//		    	if (!listado[i].isDirectory()){
//		    		System.out.println("==============");
//                	System.out.println(sub_path);
//                	System.out.println("==============");
//                	File file = new File(sub_path);
//                	p.setURL(file);
//                	p.start();
//                	p.readEntries();           
//		    	}else{
//		    		System.out.println("----> " + listado[i].getName() + " <-----");
//		    		files = getFiles(sub_path);
//		    		if ( files != null ) {
//		                int size = files.length;
//		                for ( int j = 0; j < size; j ++ ) {
//		                	System.out.println("==============");
//		                	System.out.println("ARCHIVO Nº " + (j+1) + " DE " + size);
//		                	System.out.println(files[j]);
//		                	System.out.println("==============");
//		                	File file = new File(files[j]);
//		                	p.setURL(file);
//		                	p.start();
//		                	p.readEntrie(6340912);
//		                }
//		            }
//		    	}
//		    }
//		}
		
//		/** PARSER DE UN SOLO ARCHIVO */
//		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Documentacion/Informacion/pruebas_licitacion.atom";
//		//String URL = "Licitaciones 20-21/2020_12/licitacionesPerfilesContratanteCompleto3_20201230_181906_1";
//		ArrayList<String> exp = new ArrayList<String>();
//		exp.add("PDT.-3.9/19");
//		exp.add("PDT.-3.8/19");
//		
//		Parser p = new Parser(URL, exp, "EXP");
//		
//		p.start();
//		p.readAllEntries();
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