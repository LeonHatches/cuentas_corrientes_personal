package dao;

import conexion.ConexionBD;
import modelo.TipoPrestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoPrestamoDAO {

    public boolean insertar(TipoPrestamo tipo) {
        String sql = "INSERT INTO R1Z_TIPO_PRESTAMO (TipPreCod, TipPreNom, TipPreEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tipo.getTipPreCod());
            ps.setString(2, tipo.getTipPreNom());
            ps.setString(3, tipo.getTipPreEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de préstamo ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(int codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_PRESTAMO SET TipPreNom = ? WHERE TipPreCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar tipo de préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_PRESTAMO SET TipPreEstReg = ? WHERE TipPreCod = ?";

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

    public TipoPrestamo buscarPorCodigo(int codigo) {
        String sql = "SELECT TipPreCod, TipPreNom, TipPreEstReg FROM R1Z_TIPO_PRESTAMO WHERE TipPreCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoPrestamo(
                            rs.getInt("TipPreCod"),
                            rs.getString("TipPreNom"),
                            rs.getString("TipPreEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de préstamo: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoPrestamo> listarTodos() {
        List<TipoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT TipPreCod, TipPreNom, TipPreEstReg FROM R1Z_TIPO_PRESTAMO ORDER BY TipPreCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoPrestamo tipo = new TipoPrestamo(
                        rs.getInt("TipPreCod"),
                        rs.getString("TipPreNom"),
                        rs.getString("TipPreEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de préstamo: " + e.getMessage());
        }

        return lista;
    }
}