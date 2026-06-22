package dao;

import conexion.ConexionBD;
import modelo.Acta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActaDAO {

    public boolean insertar(Acta a) {
        String sql = "INSERT INTO R1T_ACTA (ActCod, ActRef, ActFec, ActEstReg) VALUES (?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getActCod());
            ps.setString(2, a.getActRef());
            ps.setInt(3, a.getActFec());
            ps.setString(4, a.getActEstReg() == null ? "A" : a.getActEstReg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean modificarDatos(Acta a) {
        String sql = "UPDATE R1T_ACTA SET ActRef=?, ActFec=? WHERE ActCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getActRef());
            ps.setInt(2, a.getActFec());
            ps.setInt(3, a.getActCod());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean cambiarEstadoRegistro(int actCod, String nuevoEstado) {
        String sql = "UPDATE R1T_ACTA SET ActEstReg=? WHERE ActCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, actCod);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public Acta buscarPorCodigo(int actCod) {
        String sql = "SELECT * FROM R1T_ACTA WHERE ActCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, actCod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return new Acta(rs.getInt("ActCod"), rs.getString("ActRef"), rs.getInt("ActFec"), rs.getString("ActEstReg"));
            }
        } catch (SQLException e) {}
        return null;
    }

    public boolean existe(int actCod) {
        return buscarPorCodigo(actCod) != null;
    }

    public List<Acta> listarTodos() {
        List<Acta> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_ACTA ORDER BY ActCod";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Acta(rs.getInt("ActCod"), rs.getString("ActRef"), rs.getInt("ActFec"), rs.getString("ActEstReg")));
            }
        } catch (SQLException e) {}
        return lista;
    }
}