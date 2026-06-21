package dao;

import conexion.ConexionBD;
import modelo.Trabajador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {

    public boolean insertar(Trabajador t) {
        String sql = "INSERT INTO R1M_TRABAJADOR (TraCod, TraNom, TraFecIng, TraFecCes, TraFecUltSalVac, TraEmpCod, TraEstCod, TraCenCosCod, TraTipCod, TraEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, t.getTraCod());
            ps.setString(2, t.getTraNom());
            ps.setInt(3, t.getTraFecIng());
            if (t.getTraFecCes() != null) ps.setInt(4, t.getTraFecCes()); else ps.setNull(4, Types.INTEGER);
            if (t.getTraFecUltSalVac() != null) ps.setInt(5, t.getTraFecUltSalVac()); else ps.setNull(5, Types.INTEGER);
            ps.setInt(6, t.getTraEmpCod());
            ps.setString(7, t.getTraEstCod());
            ps.setString(8, t.getTraCenCosCod());
            ps.setInt(9, t.getTraTipCod());
            ps.setString(10, t.getTraEstReg() == null ? "A" : t.getTraEstReg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Trabajador t) {
        String sql = "UPDATE R1M_TRABAJADOR SET TraNom=?, TraFecIng=?, TraEmpCod=?, TraEstCod=?, TraCenCosCod=?, TraTipCod=? WHERE TraCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, t.getTraNom());
            ps.setInt(2, t.getTraFecIng());
            ps.setInt(3, t.getTraEmpCod());
            ps.setString(4, t.getTraEstCod());
            ps.setString(5, t.getTraCenCosCod());
            ps.setInt(6, t.getTraTipCod());
            ps.setInt(7, t.getTraCod());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1M_TRABAJADOR SET TraEstReg = ? WHERE TraCod = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (nuevoEstado == null) ps.setNull(1, Types.CHAR); else ps.setString(1, nuevoEstado);
            ps.setInt(2, codigo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public Trabajador buscarPorCodigo(int codigo) {
        String sql = "SELECT * FROM R1M_TRABAJADOR WHERE TraCod = ?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Trabajador(rs.getInt("TraCod"), rs.getString("TraNom"), rs.getInt("TraFecIng"),
                            (Integer) rs.getObject("TraFecCes"), (Integer) rs.getObject("TraFecUltSalVac"),
                            rs.getInt("TraEmpCod"), rs.getString("TraEstCod"), rs.getString("TraCenCosCod"),
                            rs.getInt("TraTipCod"), rs.getString("TraEstReg"));
                }
            }
        } catch (SQLException e) { System.out.println("Error buscar trabajador: " + e.getMessage()); }
        return null;
    }

    public List<Trabajador> listarTodos() {
        List<Trabajador> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1M_TRABAJADOR ORDER BY TraCod";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Trabajador(rs.getInt("TraCod"), rs.getString("TraNom"), rs.getInt("TraFecIng"),
                        (Integer) rs.getObject("TraFecCes"), (Integer) rs.getObject("TraFecUltSalVac"),
                        rs.getInt("TraEmpCod"), rs.getString("TraEstCod"), rs.getString("TraCenCosCod"),
                        rs.getInt("TraTipCod"), rs.getString("TraEstReg")));
            }
        } catch (SQLException e) { System.out.println("Error listar trabajador: " + e.getMessage()); }
        return lista;
    }
}