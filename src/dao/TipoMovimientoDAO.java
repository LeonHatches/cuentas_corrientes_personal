package dao;

import conexion.ConexionBD;
import modelo.TipoMovimiento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoMovimientoDAO {

    public boolean insertar(TipoMovimiento tipo) {
        String sql = "INSERT INTO R1Z_TIPO_MOVIMIENTO (TipMovCod, TipMovNom, TipMovEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.getTipMovCod());
            ps.setString(2, tipo.getTipMovNom());
            ps.setString(3, tipo.getTipMovEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de movimiento ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de movimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(String codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_MOVIMIENTO SET TipMovNom = ? WHERE TipMovCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar tipo de movimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(String codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_MOVIMIENTO SET TipMovEstReg = ? WHERE TipMovCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public TipoMovimiento buscarPorCodigo(String codigo) {
        String sql = "SELECT TipMovCod, TipMovNom, TipMovEstReg FROM R1Z_TIPO_MOVIMIENTO WHERE TipMovCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoMovimiento(
                            rs.getString("TipMovCod"),
                            rs.getString("TipMovNom"),
                            rs.getString("TipMovEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de movimiento: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoMovimiento> listarTodos() {
        List<TipoMovimiento> lista = new ArrayList<>();
        String sql = "SELECT TipMovCod, TipMovNom, TipMovEstReg FROM R1Z_TIPO_MOVIMIENTO ORDER BY TipMovCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoMovimiento tipo = new TipoMovimiento(
                        rs.getString("TipMovCod"),
                        rs.getString("TipMovNom"),
                        rs.getString("TipMovEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de movimiento: " + e.getMessage());
        }

        return lista;
    }
}