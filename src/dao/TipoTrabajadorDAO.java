package dao;

import conexion.ConexionBD;
import modelo.TipoTrabajador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoTrabajadorDAO {

    public boolean insertar(TipoTrabajador tipo) {
        String sql = "INSERT INTO R1Z_TIPO_TRABAJADOR (TipTraCod, TipTraNom, TipTraEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tipo.getTipTraCod());
            ps.setString(2, tipo.getTipTraNom());
            ps.setString(3, tipo.getTipTraEstReg()); // Todos son NO NULL

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar tipo de trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(int codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_TRABAJADOR SET TipTraNom = ? WHERE TipTraCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar tipo de trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_TRABAJADOR SET TipTraEstReg = ? WHERE TipTraCod = ?";

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

    public TipoTrabajador buscarPorCodigo(int codigo) {
        String sql = "SELECT TipTraCod, TipTraNom, TipTraEstReg FROM R1Z_TIPO_TRABAJADOR WHERE TipTraCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoTrabajador(
                            rs.getInt("TipTraCod"),
                            rs.getString("TipTraNom"),
                            rs.getString("TipTraEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar tipo de trabajador: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoTrabajador> listarTodos() {
        List<TipoTrabajador> lista = new ArrayList<>();
        String sql = "SELECT TipTraCod, TipTraNom, TipTraEstReg FROM R1Z_TIPO_TRABAJADOR ORDER BY TipTraCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoTrabajador tipo = new TipoTrabajador(
                        rs.getInt("TipTraCod"),
                        rs.getString("TipTraNom"),
                        rs.getString("TipTraEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar tipos de trabajador: " + e.getMessage());
        }

        return lista;
    }
}