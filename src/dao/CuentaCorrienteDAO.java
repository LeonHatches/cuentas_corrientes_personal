package dao;

import conexion.ConexionBD;
import modelo.CuentaCorriente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaCorrienteDAO {

    public boolean insertar(CuentaCorriente c) {
        String sql = "INSERT INTO R1T_CUENTA_CORRIENTE (CtaCod, CtaTraCod, CtaTipCtaCod, CtaFecApe, CtaSalIni, CtaSalAct, CtaEstReg) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getCtaCod());
            ps.setInt(2, c.getCtaTraCod());
            ps.setInt(3, c.getCtaTipCtaCod());
            ps.setInt(4, c.getCtaFecApe());
            ps.setDouble(5, c.getCtaSalIni());
            ps.setDouble(6, c.getCtaSalAct());
            ps.setString(7, c.getCtaEstReg() == null ? "A" : c.getCtaEstReg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(CuentaCorriente c) {
        String sql = "UPDATE R1T_CUENTA_CORRIENTE SET CtaTraCod=?, CtaTipCtaCod=?, CtaFecApe=?, CtaSalIni=?, CtaSalAct=? WHERE CtaCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getCtaTraCod());
            ps.setInt(2, c.getCtaTipCtaCod());
            ps.setInt(3, c.getCtaFecApe());
            ps.setDouble(4, c.getCtaSalIni());
            ps.setDouble(5, c.getCtaSalAct());
            ps.setInt(6, c.getCtaCod());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public boolean cambiarEstadoRegistro(int ctaCod, String nuevoEstado) {
        String sql = "UPDATE R1T_CUENTA_CORRIENTE SET CtaEstReg=? WHERE CtaCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, ctaCod);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    public CuentaCorriente buscarPorCodigo(int ctaCod) {
        String sql = "SELECT * FROM R1T_CUENTA_CORRIENTE WHERE CtaCod=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ctaCod);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CuentaCorriente(
                            rs.getInt("CtaCod"), rs.getInt("CtaTraCod"), rs.getInt("CtaTipCtaCod"),
                            rs.getInt("CtaFecApe"), rs.getDouble("CtaSalIni"), rs.getDouble("CtaSalAct"),
                            rs.getString("CtaEstReg")
                    );
                }
            }
        } catch (SQLException e) {}
        return null;
    }

    public boolean existe(int ctaCod) {
        return buscarPorCodigo(ctaCod) != null;
    }

    public List<CuentaCorriente> listarTodos() {
        List<CuentaCorriente> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_CUENTA_CORRIENTE ORDER BY CtaCod";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CuentaCorriente(
                        rs.getInt("CtaCod"), rs.getInt("CtaTraCod"), rs.getInt("CtaTipCtaCod"),
                        rs.getInt("CtaFecApe"), rs.getDouble("CtaSalIni"), rs.getDouble("CtaSalAct"),
                        rs.getString("CtaEstReg")
                ));
            }
        } catch (SQLException e) {}
        return lista;
    }
}