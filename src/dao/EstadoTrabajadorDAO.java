package dao;

import conexion.ConexionBD;
import modelo.EstadoTrabajador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoTrabajadorDAO {

    public boolean insertar(EstadoTrabajador estado) {
        String sql = "INSERT INTO R1Z_ESTADO_TRABAJADOR (EstTraCod, EstTraNom, EstTraEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado.getEstTraCod());
            ps.setString(2, estado.getEstTraNom());
            ps.setString(3, estado.getEstTraEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar estado de trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(String codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_ESTADO_TRABAJADOR SET EstTraNom = ? WHERE EstTraCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar estado de trabajador: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(String codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_ESTADO_TRABAJADOR SET EstTraEstReg = ? WHERE EstTraCod = ?";

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

    public EstadoTrabajador buscarPorCodigo(String codigo) {
        String sql = "SELECT EstTraCod, EstTraNom, EstTraEstReg FROM R1Z_ESTADO_TRABAJADOR WHERE EstTraCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EstadoTrabajador(
                            rs.getString("EstTraCod"),
                            rs.getString("EstTraNom"),
                            rs.getString("EstTraEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar estado de trabajador: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<EstadoTrabajador> listarTodos() {
        List<EstadoTrabajador> lista = new ArrayList<>();
        String sql = "SELECT EstTraCod, EstTraNom, EstTraEstReg FROM R1Z_ESTADO_TRABAJADOR ORDER BY EstTraCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoTrabajador estado = new EstadoTrabajador(
                        rs.getString("EstTraCod"),
                        rs.getString("EstTraNom"),
                        rs.getString("EstTraEstReg")
                );
                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estados de trabajador: " + e.getMessage());
        }

        return lista;
    }
}