import Parser.Parser;

public class main {

	public static void main(String[] args) {
		String URL = "C:/Users/senan/OneDrive/Escritorio/LicitacionParser/Documentacion/Informacion/pruebas_licitacion.atom";
		Parser p = new Parser(URL, "P2900000G");
		
		p.start();
		p.readAllEntries();
	}
}