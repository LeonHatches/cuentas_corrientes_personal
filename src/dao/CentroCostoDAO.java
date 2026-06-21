package dao;

import conexion.ConexionBD;
import modelo.CentroCosto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CentroCostoDAO {

    public boolean insertar(CentroCosto centro) {
        String sql = "INSERT INTO R1M_CENTRO_COSTO (CenCosCod, CenCosNom, CenCosEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, centro.getCenCosCod());
            ps.setString(2, centro.getCenCosNom());

            if (centro.getCenCosEstReg() == null) {
                ps.setNull(3, Types.CHAR);
            } else {
                ps.setString(3, centro.getCenCosEstReg());
            }

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de centro de costo ya existe o el estado no es válido.");
            System.out.println("Detalle: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar centro de costo: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(String codigo, String nuevoNombre) {
        String sql = "UPDATE R1M_CENTRO_COSTO SET CenCosNom = ? WHERE CenCosCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar centro de costo: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(String codigo, String nuevoEstado) {
        String sql = "UPDATE R1M_CENTRO_COSTO SET CenCosEstReg = ? WHERE CenCosCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nuevoEstado == null) {
                ps.setNull(1, Types.CHAR);
            } else {
                ps.setString(1, nuevoEstado);
            }
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public CentroCosto buscarPorCodigo(String codigo) {
        String sql = "SELECT CenCosCod, CenCosNom, CenCosEstReg FROM R1M_CENTRO_COSTO WHERE CenCosCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CentroCosto(
                            rs.getString("CenCosCod"),
                            rs.getString("CenCosNom"),
                            rs.getString("CenCosEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar centro de costo: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<CentroCosto> listarTodos() {
        List<CentroCosto> lista = new ArrayList<>();
        String sql = "SELECT CenCosCod, CenCosNom, CenCosEstReg FROM R1M_CENTRO_COSTO ORDER BY CenCosCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new CentroCosto(
                        rs.getString("CenCosCod"),
                        rs.getString("CenCosNom"),
                        rs.getString("CenCosEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar centros de costo: " + e.getMessage());
        }
        return lista;
    }
}