package dao;

import conexion.ConexionBD;
import modelo.DescuentoMov;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DescuentoMovDAO {

    private static String campoAnioDetectado;

    public boolean insertar(DescuentoMov mov) {
        try (Connection con = ConexionBD.getConnection()) {
            String campoAnioSql = campoAnioSql(con);
            String sql = "INSERT INTO R1T_DESCUENTO_MOV (DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, " +
                    "DesMovConSec, DesMovDesSec, " + campoAnioSql + ", DesMovPlaMes, DesMovPlaNum, " +
                    "DesMovTipMovCod, DesMovMon, DesMovEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = con.prepareStatement(sql);
            cargarParametros(ps, mov);
            try (ps) {
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al insertar movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(DescuentoMov mov) {
        try (Connection con = ConexionBD.getConnection()) {
            String campoAnioSql = campoAnioSql(con);
            String sql = "UPDATE R1T_DESCUENTO_MOV SET DesMovTipMovCod = ?, DesMovMon = ? " +
                    "WHERE DesMovConEmpCod = ? AND DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND " +
                    "DesMovConSec = ? AND DesMovDesSec = ? AND " + campoAnioSql + " = ? AND " +
                    "DesMovPlaMes = ? AND DesMovPlaNum = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, mov.getDesMovTipMovCod());
            ps.setBigDecimal(2, mov.getDesMovMon());
            cargarClave(ps, mov, 3);

            try (ps) {
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(DescuentoMov mov, String nuevoEstado) {
        try (Connection con = ConexionBD.getConnection()) {
            String campoAnioSql = campoAnioSql(con);
            String sql = "UPDATE R1T_DESCUENTO_MOV SET DesMovEstReg = ? " +
                    "WHERE DesMovConEmpCod = ? AND DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND " +
                    "DesMovConSec = ? AND DesMovDesSec = ? AND " + campoAnioSql + " = ? AND " +
                    "DesMovPlaMes = ? AND DesMovPlaNum = ?";

            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nuevoEstado);
            cargarClave(ps, mov, 2);

            try (ps) {
                return ps.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al cambiar estado de movimiento de descuento: " + e.getMessage());
            return false;
        }
    }

    public DescuentoMov buscarPorCodigo(int empCod, int orgCod, String tipDesCod, int conSec, int desSec,
                                        int anio, int mes, int plaNum) {
        try (Connection con = ConexionBD.getConnection()) {
            String campoAnioSql = campoAnioSql(con);
            String sql = "SELECT DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, DesMovConSec, " +
                    "DesMovDesSec, " + campoAnioSql + " AS DesMovPlaAnio, DesMovPlaMes, DesMovPlaNum, DesMovTipMovCod, " +
                    "DesMovMon, DesMovEstReg FROM R1T_DESCUENTO_MOV WHERE DesMovConEmpCod = ? AND " +
                    "DesMovConOrgCod = ? AND DesMovConTipDesCod = ? AND DesMovConSec = ? AND DesMovDesSec = ? AND " +
                    campoAnioSql + " = ? AND DesMovPlaMes = ? AND DesMovPlaNum = ?";

            PreparedStatement ps = con.prepareStatement(sql);
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
            ps.close();
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
        try (Connection con = ConexionBD.getConnection()) {
            String campoAnioSql = campoAnioSql(con);
            String sql = "SELECT DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, DesMovConSec, " +
                    "DesMovDesSec, " + campoAnioSql + " AS DesMovPlaAnio, DesMovPlaMes, DesMovPlaNum, DesMovTipMovCod, " +
                    "DesMovMon, DesMovEstReg FROM R1T_DESCUENTO_MOV ORDER BY DesMovConEmpCod, DesMovConOrgCod, " +
                    "DesMovConTipDesCod, DesMovConSec, DesMovDesSec, DesMovPlaAnio, DesMovPlaMes, DesMovPlaNum";

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    lista.add(leerMovimiento(rs));
                }
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
                rs.getInt("DesMovPlaAnio"),
                rs.getInt("DesMovPlaMes"),
                rs.getInt("DesMovPlaNum"),
                rs.getString("DesMovTipMovCod"),
                rs.getBigDecimal("DesMovMon"),
                rs.getString("DesMovEstReg")
        );
    }

    private String campoAnioSql(Connection con) throws SQLException {
        if (campoAnioDetectado != null) {
            return "`" + campoAnioDetectado + "`";
        }

        DatabaseMetaData metaData = con.getMetaData();
        try (ResultSet columnas = metaData.getColumns(con.getCatalog(), null, "R1T_DESCUENTO_MOV", null)) {
            while (columnas.next()) {
                String nombre = columnas.getString("COLUMN_NAME");
                if (nombre != null && nombre.startsWith("DesMovPlaA")) {
                    campoAnioDetectado = nombre;
                    return "`" + campoAnioDetectado + "`";
                }
            }
        }

        throw new SQLException("No se encontro la columna de anio de planilla en R1T_DESCUENTO_MOV.");
    }
}
