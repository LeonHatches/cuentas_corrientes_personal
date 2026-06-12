import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    private static Properties databaseProperties () {
        Properties p = new Properties ();

        try {
            FileInputStream archivo = new FileInputStream("config.properties");
            p.load(archivo);
            archivo.close();
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return p;
    }

    public static Connection getConnection() {
        Connection c = null;

        Properties props = databaseProperties();

        String host     = props.getProperty("db.host");
        String port     = props.getProperty("db.port");
        String name     = props.getProperty("db.name");
        String user     = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;

        try {
            c = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión establecida con éxito.");
        } catch (Exception e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        return c;
    }

    private static void insercionR1M_ORGANIZACION (Connection c, int OrgCod, String OrgNom, String OrgRuc, int OrgTipOrgCod, String OrgEstReg)
    {
        String sql = "INSERT INTO R1M_ORGANIZACION(OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg) VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, OrgCod);
            ps.setString(2, OrgNom);
            ps.setString(3, OrgRuc);
            ps.setInt(4, OrgTipOrgCod);
            ps.setString(5, OrgEstReg);

            int res = ps.executeUpdate();
            System.out.println("-> Se ha insertado " + res + "fila(s).");

        } catch (SQLException e) {
            System.out.println("Error al insertar en R1M_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void insercionGZZ_ESTADO_REGISTRO (Connection c, String EstReg, String EstRegNom, String EstRegEstReg)
    {
        String sql = "INSERT INTO GZZ_ESTADO_REGISTRO(EstReg, EstRegNom, EstRegEstReg) VALUES(?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, EstReg);
            ps.setString(2, EstRegNom);

            if (EstRegEstReg == null) {
                ps.setNull(3, Types.VARCHAR);
            } else {
                ps.setString(3, EstRegEstReg);
            }

            int res = ps.executeUpdate();
            System.out.println("-> Se ha insertado " + res + "fila(s).");

        } catch (SQLException e) {
            System.out.println("Error al insertar en GZZ_ESTADO _REGISTRO: " + e.getMessage());
        }
    }

    private static void insercionR1Z_TIPO_ORGANIZACION (Connection c, int TipOrgCod, String TipOrgNom, String TipOrgEstReg)
    {
        String sql = "INSERT INTO R1Z_TIPO_ORGANIZACION(TipOrgCod, TipOrgNom, TipOrgEstReg) VALUES(?, ?, ?)";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, TipOrgCod);
            ps.setString(2, TipOrgNom);
            ps.setString(3, TipOrgEstReg);

            int res = ps.executeUpdate();
            System.out.println("-> Se ha insertado " + res + "fila(s).");

        } catch (SQLException e) {
            System.out.println("Error al insertar en R1Z_TIPO_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void actualizacionR1M_ORGANIZACION(Connection c, int OrgCod, String nuevoNombre) {
        String sql = "UPDATE R1M_ORGANIZACION SET OrgNom = ? WHERE OrgCod = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, OrgCod);
            int res = ps.executeUpdate();
            System.out.println("-> Organización actualizada: " + res + " fila(s).");
        } catch (SQLException e) {
            System.out.println("Error al actualizar R1M_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void actualizacionGZZ_ESTADO_REGISTRO(Connection c, String EstReg, String nuevoNombre) {
        String sql = "UPDATE GZZ_ESTADO_REGISTRO SET EstRegNom = ? WHERE EstReg = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setString(2, EstReg);
            int res = ps.executeUpdate();
            System.out.println("-> Estado de registro actualizado: " + res + " fila(s).");
        } catch (SQLException e) {
            System.out.println("Error al actualizar GZZ_ESTADO_REGISTRO: " + e.getMessage());
        }
    }

    private static void actualizacionR1Z_TIPO_ORGANIZACION(Connection c, int TipOrgCod, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_ORGANIZACION SET TipOrgNom = ? WHERE TipOrgCod = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, nuevoNombre);
            ps.setInt(2, TipOrgCod);
            int res = ps.executeUpdate();
            System.out.println("-> Tipo de organizacion actualizado: " + res + " fila(s).");
        } catch (SQLException e) {
            System.out.println("Error al actualizar R1Z_TIPO_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void borradoR1M_ORGANIZACION(Connection c) {
        String sql = "DELETE FROM R1M_ORGANIZACION";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int res = ps.executeUpdate();
            System.out.println("-> Tabla R1M_ORGANIZACION vaciada. Filas eliminadas: " + res);
        } catch (SQLException e) {
            System.out.println("Error al vaciar R1M_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void borradoR1Z_TIPO_ORGANIZACION(Connection c) {
        String sql = "DELETE FROM R1Z_TIPO_ORGANIZACION";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            int res = ps.executeUpdate();
            System.out.println("-> Tabla R1Z_TIPO_ORGANIZACION vaciada. Filas eliminadas: " + res);
        } catch (SQLException e) {
            System.out.println("Error al vaciar R1Z_TIPO_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void borradoGZZ_ESTADO_REGISTRO(Connection c) {
        String romperRelacionSql = "UPDATE GZZ_ESTADO_REGISTRO SET EstRegEstReg = NULL";
        String borrarSql = "DELETE FROM GZZ_ESTADO_REGISTRO";

        try (PreparedStatement ps1 = c.prepareStatement(romperRelacionSql);
             PreparedStatement ps2 = c.prepareStatement(borrarSql)) {

            ps1.executeUpdate();
            int res = ps2.executeUpdate();
            System.out.println("-> Tabla GZZ_ESTADO_REGISTRO vaciada. Filas eliminadas: " + res);

        } catch (SQLException e) {
            System.out.println("Error al vaciar GZZ_ESTADO_REGISTRO: " + e.getMessage());
        }
    }

    private static void consultaR1M_ORGANIZACION(Connection c) {
        String sql = "SELECT * FROM R1M_ORGANIZACION";
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            while (rs.next()) {
                System.out.println("Código de Organización: " + rs.getInt("OrgCod"));
                System.out.println("Nombre de Organización: " + rs.getString("OrgNom"));
                System.out.println("RUC de Organización: " + rs.getString("OrgRuc"));
                System.out.println("Código Tipo Organización: " + rs.getInt("OrgTipOrgCod"));
                System.out.println("Estado de Registro de Organización: " + rs.getString("OrgEstReg"));
                System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar R1M_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void consultaR1Z_TIPO_ORGANIZACION(Connection c) {
        String sql = "SELECT * FROM R1Z_TIPO_ORGANIZACION";
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            while (rs.next()) {
                System.out.println("Código Tipo Organización: " + rs.getInt("TipOrgCod"));
                System.out.println("Nombre del Tipo: " + rs.getString("TipOrgNom"));
                System.out.println("Estado de Registro: " + rs.getString("TipOrgEstReg"));
                System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar R1Z_TIPO_ORGANIZACION: " + e.getMessage());
        }
    }

    private static void consultaGZZ_ESTADO_REGISTRO(Connection c) {
        String sql = "SELECT * FROM GZZ_ESTADO_REGISTRO";
        try (PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            while (rs.next()) {
                System.out.println("Código Estado Registro: " + rs.getString("EstReg"));
                System.out.println("Nombre del Estado: " + rs.getString("EstRegNom"));
                System.out.println("Código Estado Padre: " + rs.getString("EstRegEstReg"));
                System.out.println(":::::::::::::::::::::::::::::::::::::::::::::::::::");
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar GZZ_ESTADO_REGISTRO: " + e.getMessage());
        }
    }

    private static void iniciarMenu(Connection c) {
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n::::::::::::::::: MENÚ PRINCIPAL ::::::::::::::::::");
            System.out.println("1. Tabla R1M_ORGANIZACION");
            System.out.println("2. Tabla R1Z_TIPO_ORGANIZACION");
            System.out.println("3. Tabla GZZ_ESTADO_REGISTRO");
            System.out.println("0. Salir");
            System.out.print("Seleccione la tabla a gestionar: ");
            opcion = sc.nextInt(); sc.nextLine();

            switch (opcion) {
                case 1: gestionarOrganizacion(c, sc); break;
                case 2: gestionarTipoOrganizacion(c, sc); break;
                case 3: gestionarEstadoRegistro(c, sc); break;
                case 0: System.out.println("Cerrando sistema..."); break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);

        sc.close();
    }

    private static void gestionarOrganizacion(Connection c, Scanner sc) {
        System.out.println("\n::::::::::: OPERACIONES R1M_ORGANIZACION ::::::::::");
        opcionesGestionar();
        int op = sc.nextInt(); sc.nextLine();

        if (op == 1) {
            System.out.print("Escriba Código de Organización\t\t\t\t: ");
            int cod = sc.nextInt(); sc.nextLine();
            System.out.print("Escriba Nombre de Organización\t\t\t\t: ");
            String nom = sc.nextLine();
            System.out.print("Escriba RUC de Organización\t\t\t\t\t: ");
            String ruc = sc.nextLine();
            System.out.print("Escriba Código Tipo Organización\t\t\t: ");
            int tip = sc.nextInt(); sc.nextLine();
            System.out.print("Escriba Estado de Registro de Organización\t: ");
            String est = sc.nextLine();
            insercionR1M_ORGANIZACION(c, cod, nom, ruc, tip, est);
        } else if (op == 2) {
            System.out.print("Escriba Código de Organización a modificar\t: ");
            int cod = sc.nextInt(); sc.nextLine();
            System.out.print("Escriba el Nuevo Nombre\t\t\t\t\t\t: ");
            String nom = sc.nextLine();
            actualizacionR1M_ORGANIZACION(c, cod, nom);
        } else if (op == 3) {
            borradoR1M_ORGANIZACION(c);
        } else if (op == 4) {
            consultaR1M_ORGANIZACION(c);
        }
    }

    private static void gestionarTipoOrganizacion(Connection c, Scanner sc) {
        System.out.println("\n:::::::: OPERACIONES R1Z_TIPO_ORGANIZACION ::::::::");
        opcionesGestionar();
        int op = sc.nextInt(); sc.nextLine();

        if (op == 1) {
            System.out.print("Escriba Código Tipo Organización\t\t\t: ");
            int cod = sc.nextInt(); sc.nextLine();
            System.out.print("Escriba Nombre del Tipo\t\t\t\t\t\t: ");
            String nom = sc.nextLine();
            System.out.print("Escriba Estado de Registro\t\t\t\t\t: ");
            String est = sc.nextLine();
            insercionR1Z_TIPO_ORGANIZACION(c, cod, nom, est);
        } else if (op == 2) {
            System.out.print("Escriba Código de Tipo a modificar\t\t\t: ");
            int cod = sc.nextInt(); sc.nextLine();
            System.out.print("Escriba el Nuevo Nombre\t\t\t\t\t\t: ");
            String nom = sc.nextLine();
            actualizacionR1Z_TIPO_ORGANIZACION(c, cod, nom);
        } else if (op == 3) {
            borradoR1Z_TIPO_ORGANIZACION(c);
        } else if (op == 4) {
            consultaR1Z_TIPO_ORGANIZACION(c);
        }
    }

    private static void gestionarEstadoRegistro(Connection c, Scanner sc) {
        System.out.println("\n::::::::: OPERACIONES GZZ_ESTADO_REGISTRO :::::::::");
        opcionesGestionar();
        int op = sc.nextInt(); sc.nextLine();

        if (op == 1) {
            System.out.print("Escriba Código Estado Registro\t\t\t\t: ");
            String cod = sc.nextLine();
            System.out.print("Escriba Nombre del Estado\t\t\t\t\t: ");
            String nom = sc.nextLine();
            System.out.print("Escriba Código Estado Padre (o presione Enter para nulo): ");
            String padre = sc.nextLine();
            if (padre.trim().isEmpty()) { padre = null; }

            insercionGZZ_ESTADO_REGISTRO(c, cod, nom, padre);
        } else if (op == 2) {
            System.out.print("Escriba Código de Estado a modificar\t\t: ");
            String cod = sc.nextLine();
            System.out.print("Escriba el Nuevo Nombre\t\t\t\t\t\t: ");
            String nom = sc.nextLine();
            actualizacionGZZ_ESTADO_REGISTRO(c, cod, nom);
        } else if (op == 3) {
            borradoGZZ_ESTADO_REGISTRO(c);
        } else if (op == 4) {
            consultaGZZ_ESTADO_REGISTRO(c);
        }
    }

    private static void opcionesGestionar () {
        System.out.println("1. Insertar");
        System.out.println("2. Actualizar Nombre");
        System.out.println("3. Vaciar Tabla");
        System.out.println("4. Consultar Tabla");
        System.out.print("Seleccione una operación: ");
    }

    public static void main(String[] args) {
        System.out.println("Iniciando conexión");
        Connection c = getConnection();

        if (c == null) {
            System.out.println("No se pudo establecer la conexión");
            return;
        }

        System.out.println("Conexión Exitosa");

        iniciarMenu(c);

        try {
            c.close();
            System.out.println("Prueba finalizada y conexión cerrada.");
        } catch (SQLException e) {
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }
}