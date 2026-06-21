package dao;

import conexion.ConexionBD;
import modelo.Acta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActaDAO {

    public boolean insertar(Acta a) {
        String sql = "INSERT INTO R1T_ACTA (ActTraCod, ActCenCosCod, ActFec, ActDes, ActEstReg) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, a.getActTraCod());
            ps.setString(2, a.getActCenCosCod());
            ps.setInt(3, a.getActFec());
            ps.setString(4, a.getActDes());

            if (a.getActEstReg() == null) {
                ps.setNull(5, Types.CHAR);
            } else {
                ps.setString(5, a.getActEstReg());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: El acta ya existe o hay un problema de llave foránea.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar acta: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Acta a) {
        // Solo podemos modificar la descripción, ya que los demás son PK
        String sql = "UPDATE R1T_ACTA SET ActDes = ? WHERE ActTraCod = ? AND ActCenCosCod = ? AND ActFec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, a.getActDes());

            // Llaves en el WHERE
            ps.setInt(2, a.getActTraCod());
            ps.setString(3, a.getActCenCosCod());
            ps.setInt(4, a.getActFec());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar acta: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, String cenCosCod, int fec, String nuevoEstado) {
        String sql = "UPDATE R1T_ACTA SET ActEstReg = ? WHERE ActTraCod = ? AND ActCenCosCod = ? AND ActFec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nuevoEstado == null) {
                ps.setNull(1, Types.CHAR);
            } else {
                ps.setString(1, nuevoEstado);
            }

            ps.setInt(2, traCod);
            ps.setString(3, cenCosCod);
            ps.setInt(4, fec);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public Acta buscarPorIds(int traCod, String cenCosCod, int fec) {
        String sql = "SELECT * FROM R1T_ACTA WHERE ActTraCod = ? AND ActCenCosCod = ? AND ActFec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, traCod);
            ps.setString(2, cenCosCod);
            ps.setInt(3, fec);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Acta(
                            rs.getInt("ActTraCod"),
                            rs.getString("ActCenCosCod"),
                            rs.getInt("ActFec"),
                            rs.getString("ActDes"),
                            rs.getString("ActEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar acta: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, String cenCosCod, int fec) {
        return buscarPorIds(traCod, cenCosCod, fec) != null;
    }

    public List<Acta> listarTodos() {
        List<Acta> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_ACTA ORDER BY ActTraCod, ActCenCosCod, ActFec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Acta(
                        rs.getInt("ActTraCod"),
                        rs.getString("ActCenCosCod"),
                        rs.getInt("ActFec"),
                        rs.getString("ActDes"),
                        rs.getString("ActEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar actas: " + e.getMessage());
        }
        return lista;
    }
}