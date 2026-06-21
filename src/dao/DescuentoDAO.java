package dao;

import conexion.ConexionBD;
import modelo.Descuento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DescuentoDAO {

    public boolean insertar(Descuento descuento) {
        String sql = "INSERT INTO R1T_DESCUENTO (DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec, DesCtaCod, DesFec, DesMonTot, DesNumCuo, DesCuoDes, DesMonCuo, DesMonCuoAcu, DesEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            cargarParametros(ps, descuento);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Descuento descuento) {
        String sql = "UPDATE R1T_DESCUENTO SET DesCtaCod = ?, DesFec = ?, DesMonTot = ?, DesNumCuo = ?, " +
                "DesCuoDes = ?, DesMonCuo = ?, DesMonCuoAcu = ? " +
                "WHERE DesConEmpCod = ? AND DesConOrgCod = ? AND DesConTipDesCod = ? AND DesConSec = ? AND DesSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, descuento.getDesCtaCod());
            ps.setInt(2, descuento.getDesFec());
            ps.setBigDecimal(3, descuento.getDesMonTot());
            ps.setInt(4, descuento.getDesNumCuo());
            ps.setInt(5, descuento.getDesCuoDes());
            ps.setBigDecimal(6, descuento.getDesMonCuo());
            ps.setBigDecimal(7, descuento.getDesMonCuoAcu());
            ps.setInt(8, descuento.getDesConEmpCod());
            ps.setInt(9, descuento.getDesConOrgCod());
            ps.setString(10, descuento.getDesConTipDesCod());
            ps.setInt(11, descuento.getDesConSec());
            ps.setInt(12, descuento.getDesSec());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int empCod, int orgCod, String tipDesCod, int conSec, int desSec, String nuevoEstado) {
        String sql = "UPDATE R1T_DESCUENTO SET DesEstReg = ? WHERE DesConEmpCod = ? AND DesConOrgCod = ? AND DesConTipDesCod = ? AND DesConSec = ? AND DesSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            ps.setInt(2, empCod);
            ps.setInt(3, orgCod);
            ps.setString(4, tipDesCod);
            ps.setInt(5, conSec);
            ps.setInt(6, desSec);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de descuento: " + e.getMessage());
            return false;
        }
    }

    public Descuento buscarPorCodigo(int empCod, int orgCod, String tipDesCod, int conSec, int desSec) {
        String sql = "SELECT DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec, DesCtaCod, DesFec, " +
                "DesMonTot, DesNumCuo, DesCuoDes, DesMonCuo, DesMonCuoAcu, DesEstReg FROM R1T_DESCUENTO " +
                "WHERE DesConEmpCod = ? AND DesConOrgCod = ? AND DesConTipDesCod = ? AND DesConSec = ? AND DesSec = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empCod);
            ps.setInt(2, orgCod);
            ps.setString(3, tipDesCod);
            ps.setInt(4, conSec);
            ps.setInt(5, desSec);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerDescuento(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar descuento: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int empCod, int orgCod, String tipDesCod, int conSec, int desSec) {
        return buscarPorCodigo(empCod, orgCod, tipDesCod, conSec, desSec) != null;
    }

    public List<Descuento> listarTodos() {
        List<Descuento> lista = new ArrayList<>();
        String sql = "SELECT DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec, DesCtaCod, DesFec, " +
                "DesMonTot, DesNumCuo, DesCuoDes, DesMonCuo, DesMonCuoAcu, DesEstReg FROM R1T_DESCUENTO " +
                "ORDER BY DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(leerDescuento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar descuentos: " + e.getMessage());
        }

        return lista;
    }

    private void cargarParametros(PreparedStatement ps, Descuento descuento) throws SQLException {
        ps.setInt(1, descuento.getDesConEmpCod());
        ps.setInt(2, descuento.getDesConOrgCod());
        ps.setString(3, descuento.getDesConTipDesCod());
        ps.setInt(4, descuento.getDesConSec());
        ps.setInt(5, descuento.getDesSec());
        ps.setInt(6, descuento.getDesCtaCod());
        ps.setInt(7, descuento.getDesFec());
        ps.setBigDecimal(8, descuento.getDesMonTot());
        ps.setInt(9, descuento.getDesNumCuo());
        ps.setInt(10, descuento.getDesCuoDes());
        ps.setBigDecimal(11, descuento.getDesMonCuo());
        ps.setBigDecimal(12, descuento.getDesMonCuoAcu());
        ps.setString(13, descuento.getDesEstReg());
    }

    private Descuento leerDescuento(ResultSet rs) throws SQLException {
        return new Descuento(
                rs.getInt("DesConEmpCod"),
                rs.getInt("DesConOrgCod"),
                rs.getString("DesConTipDesCod"),
                rs.getInt("DesConSec"),
                rs.getInt("DesSec"),
                rs.getInt("DesCtaCod"),
                rs.getInt("DesFec"),
                rs.getBigDecimal("DesMonTot"),
                rs.getInt("DesNumCuo"),
                rs.getInt("DesCuoDes"),
                rs.getBigDecimal("DesMonCuo"),
                rs.getBigDecimal("DesMonCuoAcu"),
                rs.getString("DesEstReg")
        );
    }
}
