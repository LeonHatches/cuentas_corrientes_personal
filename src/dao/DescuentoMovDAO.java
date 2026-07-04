package dao;

import conexion.ConexionBD;
import modelo.DescuentoMov;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DescuentoMovDAO {

    private static final String CAMPO_ANIO = "DesMovPlaA\u251C\u2592o";
    private static final String CAMPO_ANIO_SQL = "`" + CAMPO_ANIO + "`";

    public boolean insertar(DescuentoMov mov) {
        String sql = "INSERT INTO R1T_DESCUENTO_MOV (DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, " +
                "DesMovConSec, DesMovDesSec, " + CAMPO_ANIO_SQL + ", DesMovPlaMes, DesMovPlaNum, " +
                "DesMovTipMovCod, DesMovMon, DesMovEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            cargarParametros(ps, mov);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al insertar movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(DescuentoMov mov) {
        String sql = "UPDATE R1T_DESCUENTO_MOV SET DesMovTipMovCod = ?, DesMovMon = ? " +
                "WHERE DesMovConEmpCod = ? AND DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND " +
                "DesMovConSec = ? AND DesMovDesSec = ? AND " + CAMPO_ANIO_SQL + " = ? AND " +
                "DesMovPlaMes = ? AND DesMovPlaNum = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mov.getDesMovTipMovCod());
            ps.setBigDecimal(2, mov.getDesMovMon());
            cargarClave(ps, mov, 3);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al modificar movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(DescuentoMov mov, String nuevoEstado) {
        String sql = "UPDATE R1T_DESCUENTO_MOV SET DesMovEstReg = ? " +
                "WHERE DesMovConEmpCod = ? AND DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND " +
                "DesMovConSec = ? AND DesMovDesSec = ? AND " + CAMPO_ANIO_SQL + " = ? AND " +
                "DesMovPlaMes = ? AND DesMovPlaNum = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado);
            cargarClave(ps, mov, 2);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public DescuentoMov buscarPorCodigo(int empCod, int orgCod, String tipDesCod, int conSec, int desSec,
                                        int anio, int mes, int plaNum) {
        String sql = "SELECT DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, DesMovConSec, " +
                "DesMovDesSec, " + CAMPO_ANIO_SQL + ", DesMovPlaMes, DesMovPlaNum, DesMovTipMovCod, " +
                "DesMovMon, DesMovEstReg FROM R1T_DESCUENTO_MOV WHERE DesMovConEmpCod = ? AND " +
                "DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND DesMovConSec = ? AND DesMovDesSec = ? AND " +
                CAMPO_ANIO_SQL + " = ? AND DesMovPlaMes = ? AND DesMovPlaNum = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, empCod);
            ps.setInt(2, orgCod);
            ps.setString(3, tipDesCod);
            ps.setInt(4, conSec);
            ps.setInt(5, desSec);
            ps.setInt(6, anio);
            ps.setInt(7, mes);
            ps.setInt(8, plaNum);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return leerMovimiento(rs);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar movimiento de descuento: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(int empCod, int orgCod, String tipDesCod, int conSec, int desSec,
                          int anio, int mes, int plaNum) {
        return buscarPorCodigo(empCod, orgCod, tipDesCod, conSec, desSec, anio, mes, plaNum) != null;
    }

    public List<DescuentoMov> listarTodos() {
        List<DescuentoMov> lista = new ArrayList<>();
        String sql = "SELECT DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, DesMovConSec, " +
                "DesMovDesSec, " + CAMPO_ANIO_SQL + ", DesMovPlaMes, DesMovPlaNum, DesMovTipMovCod, " +
                "DesMovMon, DesMovEstReg FROM R1T_DESCUENTO_MOV ORDER BY DesMovConEmpCod, DesMovConOrgCod, " +
                "DesMovConTipDesCod, DesMovConSec, DesMovDesSec, " + CAMPO_ANIO_SQL + ", DesMovPlaMes, DesMovPlaNum";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(leerMovimiento(rs));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos de descuento: " + e.getMessage());
        }

        return lista;
    }

    private void cargarParametros(PreparedStatement ps, DescuentoMov mov) throws SQLException {
        ps.setInt(1, mov.getDesMovConEmpCod());
        ps.setInt(2, mov.getDesMovConOrgCod());
        ps.setString(3, mov.getDesMovConTipDesCod());
        ps.setInt(4, mov.getDesMovConSec());
        ps.setInt(5, mov.getDesMovDesSec());
        ps.setInt(6, mov.getDesMovPlaAnio());
        ps.setInt(7, mov.getDesMovPlaMes());
        ps.setInt(8, mov.getDesMovPlaNum());
        ps.setString(9, mov.getDesMovTipMovCod());
        ps.setBigDecimal(10, mov.getDesMovMon());
        ps.setString(11, mov.getDesMovEstReg());
    }

    private void cargarClave(PreparedStatement ps, DescuentoMov mov, int inicio) throws SQLException {
        ps.setInt(inicio, mov.getDesMovConEmpCod());
        ps.setInt(inicio + 1, mov.getDesMovConOrgCod());
        ps.setString(inicio + 2, mov.getDesMovConTipDesCod());
        ps.setInt(inicio + 3, mov.getDesMovConSec());
        ps.setInt(inicio + 4, mov.getDesMovDesSec());
        ps.setInt(inicio + 5, mov.getDesMovPlaAnio());
        ps.setInt(inicio + 6, mov.getDesMovPlaMes());
        ps.setInt(inicio + 7, mov.getDesMovPlaNum());
    }

    private DescuentoMov leerMovimiento(ResultSet rs) throws SQLException {
        return new DescuentoMov(
                rs.getInt("DesMovConEmpCod"),
                rs.getInt("DesMovConOrgCod"),
                rs.getString("DesMovConTipDesCod"),
                rs.getInt("DesMovConSec"),
                rs.getInt("DesMovDesSec"),
                rs.getInt(CAMPO_ANIO),
                rs.getInt("DesMovPlaMes"),
                rs.getInt("DesMovPlaNum"),
                rs.getString("DesMovTipMovCod"),
                rs.getBigDecimal("DesMovMon"),
                rs.getString("DesMovEstReg")
        );
    }
}
