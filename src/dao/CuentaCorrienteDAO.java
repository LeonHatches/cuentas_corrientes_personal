package dao;

import conexion.ConexionBD;
import modelo.CuentaCorriente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaCorrienteDAO {

    public boolean insertar(CuentaCorriente c) {
        String sql = "INSERT INTO R1T_CUENTA_CORRIENTE (CtaCorTraCod, CtaCorCtaCod, CtaCorSec, CtaCorFec, CtaCorDocRfe, CtaCorMnt, CtaCorEstReg) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getCtaCorTraCod());
            ps.setInt(2, c.getCtaCorCtaCod());
            ps.setInt(3, c.getCtaCorSec());
            ps.setInt(4, c.getCtaCorFec());
            ps.setString(5, c.getCtaCorDocRfe());
            ps.setDouble(6, c.getCtaCorMnt());

            if (c.getCtaCorEstReg() == null) {
                ps.setNull(7, Types.CHAR);
            } else {
                ps.setString(7, c.getCtaCorEstReg());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: El registro ya existe o hay un problema de llave foránea.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar en cuenta corriente: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(CuentaCorriente c) {
        // En modificar, solo actualizamos los datos que NO son llave primaria
        String sql = "UPDATE R1T_CUENTA_CORRIENTE SET CtaCorFec = ?, CtaCorDocRfe = ?, CtaCorMnt = ? WHERE CtaCorTraCod = ? AND CtaCorCtaCod = ? AND CtaCorSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, c.getCtaCorFec());
            ps.setString(2, c.getCtaCorDocRfe());
            ps.setDouble(3, c.getCtaCorMnt());

            // Llaves en el WHERE
            ps.setInt(4, c.getCtaCorTraCod());
            ps.setInt(5, c.getCtaCorCtaCod());
            ps.setInt(6, c.getCtaCorSec());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar cuenta corriente: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, int ctaCod, int sec, String nuevoEstado) {
        String sql = "UPDATE R1T_CUENTA_CORRIENTE SET CtaCorEstReg = ? WHERE CtaCorTraCod = ? AND CtaCorCtaCod = ? AND CtaCorSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nuevoEstado == null) {
                ps.setNull(1, Types.CHAR);
            } else {
                ps.setString(1, nuevoEstado);
            }

            ps.setInt(2, traCod);
            ps.setInt(3, ctaCod);
            ps.setInt(4, sec);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public CuentaCorriente buscarPorIds(int traCod, int ctaCod, int sec) {
        String sql = "SELECT * FROM R1T_CUENTA_CORRIENTE WHERE CtaCorTraCod = ? AND CtaCorCtaCod = ? AND CtaCorSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, traCod);
            ps.setInt(2, ctaCod);
            ps.setInt(3, sec);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CuentaCorriente(
                            rs.getInt("CtaCorTraCod"),
                            rs.getInt("CtaCorCtaCod"),
                            rs.getInt("CtaCorSec"),
                            rs.getInt("CtaCorFec"),
                            rs.getString("CtaCorDocRfe"),
                            rs.getDouble("CtaCorMnt"),
                            rs.getString("CtaCorEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cuenta corriente: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, int ctaCod, int sec) {
        return buscarPorIds(traCod, ctaCod, sec) != null;
    }

    public List<CuentaCorriente> listarTodos() {
        List<CuentaCorriente> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_CUENTA_CORRIENTE ORDER BY CtaCorTraCod, CtaCorCtaCod, CtaCorSec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new CuentaCorriente(
                        rs.getInt("CtaCorTraCod"),
                        rs.getInt("CtaCorCtaCod"),
                        rs.getInt("CtaCorSec"),
                        rs.getInt("CtaCorFec"),
                        rs.getString("CtaCorDocRfe"),
                        rs.getDouble("CtaCorMnt"),
                        rs.getString("CtaCorEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar cuenta corriente: " + e.getMessage());
        }
        return lista;
    }
}