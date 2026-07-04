package dao;

import conexion.ConexionBD;
import modelo.Empresa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public boolean insertar(Empresa empresa) {
        String sql = "INSERT INTO R1M_EMPRESA (EmpCod, EmpRazSoc, EmpRuc, EmpEstReg) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empresa.getEmpCod());
            ps.setString(2, empresa.getEmpRazSoc());
            ps.setString(3, empresa.getEmpRuc());

            if (empresa.getEmpEstReg() == null) {
                ps.setNull(4, Types.CHAR);
            } else {
                ps.setString(4, empresa.getEmpEstReg());
            }

            int filas = ps.executeUpdate();
            return filas > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código de empresa ya existe.");
            System.out.println("Detalle: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(int codigo, String nuevaRazonSocial, String nuevoRuc) {
        String sql = "UPDATE R1M_EMPRESA SET EmpRazSoc = ?, EmpRuc = ? WHERE EmpCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevaRazonSocial);
            ps.setString(2, nuevoRuc);
            ps.setInt(3, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar empresa: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int codigo, String nuevoEstado) {
        String sql = "UPDATE R1M_EMPRESA SET EmpEstReg = ? WHERE EmpCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nuevoEstado == null) {
                ps.setNull(1, Types.CHAR);
            } else {
                ps.setString(1, nuevoEstado);
            }
            ps.setInt(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public Empresa buscarPorCodigo(int codigo) {
        String sql = "SELECT EmpCod, EmpRazSoc, EmpRuc, EmpEstReg FROM R1M_EMPRESA WHERE EmpCod = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("EmpCod"),
                            rs.getString("EmpRazSoc"),
                            rs.getString("EmpRuc"),
                            rs.getString("EmpEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar empresa: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<Empresa> listarTodos() {
        List<Empresa> lista = new ArrayList<>();
        String sql = "SELECT EmpCod, EmpRazSoc, EmpRuc, EmpEstReg FROM R1M_EMPRESA ORDER BY EmpCod";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Empresa(
                        rs.getInt("EmpCod"),
                        rs.getString("EmpRazSoc"),
                        rs.getString("EmpRuc"),
                        rs.getString("EmpEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar empresas: " + e.getMessage());
        }
        return lista;
    }
}