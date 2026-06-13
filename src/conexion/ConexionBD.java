package conexion;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionBD {

    private static final String ARCHIVO_CONFIG = "config.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        try (FileInputStream archivo = new FileInputStream(ARCHIVO_CONFIG)) {
            props.load(archivo);
        } catch (IOException e) {
            throw new SQLException("No se pudo leer config.properties: " + e.getMessage());
        }

        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port");
        String name = props.getProperty("db.name");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        if (host == null || port == null || name == null || user == null || password == null) {
            throw new SQLException("Faltan datos en config.properties.");
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;

        return DriverManager.getConnection(url, user, password);
    }
}