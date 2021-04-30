package pruebas;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import Parser.Parser;

public class ParserTest {
	
	@Test (expected = Error.class)
	public void testConstructorVacío() {
		Parser p = new Parser();
		assertNull(p);
	}
	
	@Test 
	public void testConstructorCorrecto() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Documentacion/Informacion/pruebas_licitacion.atom";
		Parser p = new Parser(URL, "P2900000G");
		p.start();
	}

	@Test (expected = Error.class)
	public void testContructorStringInvalido(){
		Parser p = new Parser(2);
		assertNull(p);
	}
	
	@Test (expected = SAXParseException.class)
	public void testConstructorStringVacío() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		Parser p = new Parser("", "");
		p.start();
	}
	
	@Test (expected = FileNotFoundException.class)
	public void testURLInvalido() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String NIF = "P2900000G";
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Documentacion/incorrecto.atom";
		
		Parser p = new Parser(URL, NIF);
		p.start();
	}
	
	@Test (expected = SAXParseException.class)
	public void testURLVacío() throws FileNotFoundException, ParserConfigurationException, SAXException, IOException{
		String NIF = "P2900000G";
		String URL = "";
		
		Parser p = new Parser(URL, NIF);
		p.start();
	}
}
