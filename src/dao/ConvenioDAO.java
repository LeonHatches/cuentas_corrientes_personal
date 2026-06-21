package dao;

import conexion.ConexionBD;
import modelo.Convenio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConvenioDAO {

    public boolean insertar(Convenio convenio) {
        String sql = "INSERT INTO R1T_CONVENIO (ConEmpCod, ConOrgCod, ConTipDesCod, ConSec, ConDes, ConActCod, ConCon, ConEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            cargarParametros(ps, convenio);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar convenio: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Convenio convenio) {
        String sql = "UPDATE R1T_CONVENIO SET ConDes = ?, ConActCod = ?, ConCon = ? " +
                "WHERE ConEmpCod = ? AND ConOrgCod = ? AND ConTipDesCod = ? AND ConSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, convenio.getConDes());
            ps.setInt(2, convenio.getConActCod());
            ps.setString(3, convenio.getConCon());
            ps.setInt(4, convenio.getConEmpCod());
            ps.setInt(5, convenio.getConOrgCod());
            ps.setString(6, convenio.getConTipDesCod());
            ps.setInt(7, convenio.getConSec());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar convenio: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int empCod, int orgCod, String tipDesCod, int sec, String nuevoEstado) {
        String sql = "UPDATE R1T_CONVENIO SET ConEstReg = ? WHERE ConEmpCod = ? AND ConOrgCod = ? AND ConTipDesCod = ? AND ConSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, empCod);
            ps.setInt(3, orgCod);
            ps.setString(4, tipDesCod);
            ps.setInt(5, sec);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de convenio: " + e.getMessage());
            return false;
        }
    }

    public Convenio buscarPorCodigo(int empCod, int orgCod, String tipDesCod, int sec) {
        String sql = "SELECT ConEmpCod, ConOrgCod, ConTipDesCod, ConSec, ConDes, ConActCod, ConCon, ConEstReg " +
                "FROM R1T_CONVENIO WHERE ConEmpCod = ? AND ConOrgCod = ? AND ConTipDesCod = ? AND ConSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empCod);
            ps.setInt(2, orgCod);
            ps.setString(3, tipDesCod);
            ps.setInt(4, sec);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerConvenio(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar convenio: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int empCod, int orgCod, String tipDesCod, int sec) {
        return buscarPorCodigo(empCod, orgCod, tipDesCod, sec) != null;
    }

    public List<Convenio> listarTodos() {
        List<Convenio> lista = new ArrayList<>();
        String sql = "SELECT ConEmpCod, ConOrgCod, ConTipDesCod, ConSec, ConDes, ConActCod, ConCon, ConEstReg " +
                "FROM R1T_CONVENIO ORDER BY ConEmpCod, ConOrgCod, ConTipDesCod, ConSec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(leerConvenio(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar convenios: " + e.getMessage());
        }

        return lista;
    }

    private void cargarParametros(PreparedStatement ps, Convenio convenio) throws SQLException {
        ps.setInt(1, convenio.getConEmpCod());
        ps.setInt(2, convenio.getConOrgCod());
        ps.setString(3, convenio.getConTipDesCod());
        ps.setInt(4, convenio.getConSec());
        ps.setString(5, convenio.getConDes());
        ps.setInt(6, convenio.getConActCod());
        ps.setString(7, convenio.getConCon());
        ps.setString(8, convenio.getConEstReg());
    }

    private Convenio leerConvenio(ResultSet rs) throws SQLException {
        return new Convenio(
                rs.getInt("ConEmpCod"),
                rs.getInt("ConOrgCod"),
                rs.getString("ConTipDesCod"),
                rs.getInt("ConSec"),
                rs.getString("ConDes"),
                rs.getInt("ConActCod"),
                rs.getString("ConCon"),
                rs.getString("ConEstReg")
        );
    }
}
