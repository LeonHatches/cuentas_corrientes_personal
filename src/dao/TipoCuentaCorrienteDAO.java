package dao;

import conexion.ConexionBD;
import modelo.TipoCuentaCorriente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoCuentaCorrienteDAO {

    public boolean insertar(TipoCuentaCorriente tipo) {
        String sql = "INSERT INTO R1Z_TIPO_CUENTA_CORRIENTE (TipCtaCod, TipCtaNom, TipCtaEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tipo.getTipCtaCod());
            ps.setString(2, tipo.getTipCtaNom());
            ps.setString(3, tipo.getTipCtaEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de cuenta ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de cuenta corriente: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(int codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_CUENTA_CORRIENTE SET TipCtaNom = ? WHERE TipCtaCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar tipo de cuenta corriente: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_CUENTA_CORRIENTE SET TipCtaEstReg = ? WHERE TipCtaCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public TipoCuentaCorriente buscarPorCodigo(int codigo) {
        String sql = "SELECT TipCtaCod, TipCtaNom, TipCtaEstReg FROM R1Z_TIPO_CUENTA_CORRIENTE WHERE TipCtaCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoCuentaCorriente(
                            rs.getInt("TipCtaCod"),
                            rs.getString("TipCtaNom"),
                            rs.getString("TipCtaEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de cuenta corriente: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoCuentaCorriente> listarTodos() {
        List<TipoCuentaCorriente> lista = new ArrayList<>();
        String sql = "SELECT TipCtaCod, TipCtaNom, TipCtaEstReg FROM R1Z_TIPO_CUENTA_CORRIENTE ORDER BY TipCtaCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoCuentaCorriente tipo = new TipoCuentaCorriente(
                        rs.getInt("TipCtaCod"),
                        rs.getString("TipCtaNom"),
                        rs.getString("TipCtaEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de cuenta corriente: " + e.getMessage());
        }

        return lista;
    }
}