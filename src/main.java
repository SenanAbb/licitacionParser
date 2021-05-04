import java.io.File;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import Conexion.ConexionSQL;
import Parser.Parser;

public class main {
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

//		/** PARSER DE DIRECTORIO */
//		Parser p = new Parser("P2900000G");
//		String path = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/LicitacionParser/Licitaciones 20-21/NOV 2020";
//
//        String[] files = getFiles( path );
//
//        if ( files != null ) {
//            int size = files.length;
//            for ( int i = 0; i < size; i ++ ) {
//            	System.out.println("///////////////////////////////////////");
//            	System.out.println(files[i]);
//            	System.out.println("///////////////////////////////////////");
//            	File file = new File(files[i]);
//            	p.setURL(file);
//            	p.start();
//            	p.readAllEntries();
//            }
//        }
		
		
		/** PARSER DE UN SOLO ARCHIVO */
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Documentacion/Informacion/pruebas_licitacion.atom";
		Parser p = new Parser(URL, "P2900000G");
		
		p.start();
		p.readAllEntries();
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