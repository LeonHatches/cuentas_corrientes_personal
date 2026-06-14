package dao;

import conexion.ConexionBD;
import modelo.TipoOrganizacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoOrganizacionDAO {

    public boolean insertar(TipoOrganizacion tipo) {
        String sql = "INSERT INTO R1Z_TIPO_ORGANIZACION (TipOrgCod, TipOrgNom, TipOrgEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, tipo.getTipOrgCod());
            ps.setString(2, tipo.getTipOrgNom());
            ps.setString(3, tipo.getTipOrgEstReg());

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de organización ya existe.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(int codigo, String nuevoNombre) {
        String sql = "UPDATE R1Z_TIPO_ORGANIZACION SET TipOrgNom = ? WHERE TipOrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1Z_TIPO_ORGANIZACION SET TipOrgEstReg = ? WHERE TipOrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public TipoOrganizacion buscarPorCodigo(int codigo) {
        String sql = "SELECT TipOrgCod, TipOrgNom, TipOrgEstReg FROM R1Z_TIPO_ORGANIZACION WHERE TipOrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new TipoOrganizacion(
                            rs.getInt("TipOrgCod"),
                            rs.getString("TipOrgNom"),
                            rs.getString("TipOrgEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<TipoOrganizacion> listarTodos() {
        List<TipoOrganizacion> lista = new ArrayList<>();
        String sql = "SELECT TipOrgCod, TipOrgNom, TipOrgEstReg FROM R1Z_TIPO_ORGANIZACION ORDER BY TipOrgCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                TipoOrganizacion tipo = new TipoOrganizacion(
                        rs.getInt("TipOrgCod"),
                        rs.getString("TipOrgNom"),
                        rs.getString("TipOrgEstReg")
                );
                lista.add(tipo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }

        return lista;
    }
}