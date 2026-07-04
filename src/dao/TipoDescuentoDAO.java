package dao;

import conexion.ConexionBD;
import modelo.TipoDescuento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoDescuentoDAO {

    public boolean insertar(TipoDescuento tipo) {
        String sql = "INSERT INTO R1Z_TIPO_DESCUENTO (TipDesCod, TipDesNom, TipDesEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo.getTipDesCod());
            ps.setString(2, tipo.getTipDesNom());
            ps.setString(3, tipo.getTipDesEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de descuento ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(String codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_DESCUENTO SET TipDesNom = ? WHERE TipDesCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar tipo de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(String codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_DESCUENTO SET TipDesEstReg = ? WHERE TipDesCod = ?";

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

    public TipoDescuento buscarPorCodigo(String codigo) {
        String sql = "SELECT TipDesCod, TipDesNom, TipDesEstReg FROM R1Z_TIPO_DESCUENTO WHERE TipDesCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoDescuento(
                            rs.getString("TipDesCod"),
                            rs.getString("TipDesNom"),
                            rs.getString("TipDesEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de descuento: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoDescuento> listarTodos() {
        List<TipoDescuento> lista = new ArrayList<>();
        String sql = "SELECT TipDesCod, TipDesNom, TipDesEstReg FROM R1Z_TIPO_DESCUENTO ORDER BY TipDesCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoDescuento tipo = new TipoDescuento(
                        rs.getString("TipDesCod"),
                        rs.getString("TipDesNom"),
                        rs.getString("TipDesEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de descuento: " + e.getMessage());
        }

        return lista;
    }
}