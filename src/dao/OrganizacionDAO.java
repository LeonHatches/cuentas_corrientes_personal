package dao;

import conexion.ConexionBD;
import modelo.Organizacion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizacionDAO {

    public boolean insertar(Organizacion organizacion) {
        String sql = "INSERT INTO R1M_ORGANIZACION (OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, organizacion.getOrgCod());
            ps.setString(2, organizacion.getOrgNom());
            ps.setString(3, organizacion.getOrgRuc());
            ps.setInt(4, organizacion.getTipOrgCod());
            ps.setString(5, organizacion.getEstReg());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar organizacion: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Organizacion organizacion) {
        String sql = "UPDATE R1M_ORGANIZACION SET OrgNom = ?, OrgRuc = ?, OrgTipOrgCod = ? WHERE OrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, organizacion.getOrgNom());
            ps.setString(2, organizacion.getOrgRuc());
            ps.setInt(3, organizacion.getTipOrgCod());
            ps.setInt(4, organizacion.getOrgCod());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar organizacion: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1M_ORGANIZACION SET OrgEstReg = ? WHERE OrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de organizacion: " + e.getMessage());
            return false;
        }
    }

    public Organizacion buscarPorCodigo(int codigo) {
        String sql = "SELECT OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg FROM R1M_ORGANIZACION WHERE OrgCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Organizacion(
                            rs.getInt("OrgCod"),
                            rs.getString("OrgNom"),
                            rs.getString("OrgRuc"),
                            rs.getInt("OrgTipOrgCod"),
                            rs.getString("OrgEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar organizacion: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<Organizacion> listarTodos() {
        List<Organizacion> lista = new ArrayList<>();
        String sql = "SELECT OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg FROM R1M_ORGANIZACION ORDER BY OrgCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Organizacion(
                        rs.getInt("OrgCod"),
                        rs.getString("OrgNom"),
                        rs.getString("OrgRuc"),
                        rs.getInt("OrgTipOrgCod"),
                        rs.getString("OrgEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar organizaciones: " + e.getMessage());
        }

        return lista;
    }
}
