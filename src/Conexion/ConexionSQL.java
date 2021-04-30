package Conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionSQL {
	// Librería de MySQL
    private String driver = "com.mysql.jdbc.Driver";

    // Nombre de la base de datos
    private String database = "licitacion";

    // Host
    private String hostname = "localhost";

    // Puerto
    private String port = "3306";

    // Ruta de nuestra base de datos (desactivamos el uso de SSL con "?useSSL=false")
    private String url = "jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=false";

    // Nombre de usuario
    private String username = "root";

    // Clave de usuario
    private String password = "root";
    
    public Connection conectarMySQL(){
    	Connection conn = null;
    	
    	try{
    		Class.forName(driver);
    		conn = DriverManager.getConnection(url, username, password);
    	} catch (ClassNotFoundException | SQLException e){
    		e.printStackTrace();
    	}
    	
    	return conn;
    }
}
