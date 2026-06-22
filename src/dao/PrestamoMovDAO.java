package dao;

import conexion.ConexionBD;
import modelo.PrestamoMov;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoMovDAO {

    public boolean insertar(PrestamoMov pm) {
        String sql = "INSERT INTO R1T_PRESTAMO_MOV (PreMovTraCod, PreMovTipCod, PreMovPreSec, PreMovPlaAño, PreMovPlaMes, PreMovPlaNum, PreMovTipMovCod, PreMovMonDes, PreMovEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, pm.getPreMovTraCod());
            ps.setInt(2, pm.getPreMovTipCod());
            ps.setInt(3, pm.getPreMovPreSec());
            ps.setInt(4, pm.getPreMovPlaAnio());
            ps.setInt(5, pm.getPreMovPlaMes());
            ps.setInt(6, pm.getPreMovPlaNum());
            ps.setString(7, pm.getPreMovTipMovCod());
            ps.setDouble(8, pm.getPreMovMonDes());
            ps.setString(9, pm.getPreMovEstReg() == null ? "A" : pm.getPreMovEstReg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL INSERTAR MOVIMIENTO: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(PrestamoMov pm) {
        String sql = "UPDATE R1T_PRESTAMO_MOV SET PreMovTipMovCod=?, PreMovMonDes=? WHERE PreMovTraCod=? AND PreMovTipCod=? AND PreMovPreSec=? AND PreMovPlaAño=? AND PreMovPlaMes=? AND PreMovPlaNum=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, pm.getPreMovTipMovCod());
            ps.setDouble(2, pm.getPreMovMonDes());
            ps.setInt(3, pm.getPreMovTraCod());
            ps.setInt(4, pm.getPreMovTipCod());
            ps.setInt(5, pm.getPreMovPreSec());
            ps.setInt(6, pm.getPreMovPlaAnio());
            ps.setInt(7, pm.getPreMovPlaMes());
            ps.setInt(8, pm.getPreMovPlaNum());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL MODIFICAR MOVIMIENTO: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, int tipCod, int sec, int anio, int mes, int num, String est) {
        String sql = "UPDATE R1T_PRESTAMO_MOV SET PreMovEstReg=? WHERE PreMovTraCod=? AND PreMovTipCod=? AND PreMovPreSec=? AND PreMovPlaAño=? AND PreMovPlaMes=? AND PreMovPlaNum=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, est);
            ps.setInt(2, traCod); ps.setInt(3, tipCod); ps.setInt(4, sec);
            ps.setInt(5, anio); ps.setInt(6, mes); ps.setInt(7, num);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL CAMBIAR ESTADO DE MOVIMIENTO: " + e.getMessage());
            return false;
        }
    }

    public PrestamoMov buscarPorIds(int traCod, int tipCod, int sec, int anio, int mes, int num) {
        String sql = "SELECT * FROM R1T_PRESTAMO_MOV WHERE PreMovTraCod=? AND PreMovTipCod=? AND PreMovPreSec=? AND PreMovPlaAño=? AND PreMovPlaMes=? AND PreMovPlaNum=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, traCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            ps.setInt(4, anio); ps.setInt(5, mes); ps.setInt(6, num);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PrestamoMov(rs.getInt("PreMovTraCod"), rs.getInt("PreMovTipCod"), rs.getInt("PreMovPreSec"), rs.getInt("PreMovPlaAño"), rs.getInt("PreMovPlaMes"), rs.getInt("PreMovPlaNum"), rs.getString("PreMovTipMovCod"), rs.getDouble("PreMovMonDes"), rs.getString("PreMovEstReg"));
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] AL BUSCAR MOVIMIENTO: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, int tipCod, int sec, int anio, int mes, int num) {
        return buscarPorIds(traCod, tipCod, sec, anio, mes, num) != null;
    }

    public List<PrestamoMov> listarTodos() {
        List<PrestamoMov> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_PRESTAMO_MOV ORDER BY PreMovTraCod, PreMovTipCod, PreMovPreSec, PreMovPlaAño, PreMovPlaMes, PreMovPlaNum";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new PrestamoMov(rs.getInt("PreMovTraCod"), rs.getInt("PreMovTipCod"), rs.getInt("PreMovPreSec"), rs.getInt("PreMovPlaAño"), rs.getInt("PreMovPlaMes"), rs.getInt("PreMovPlaNum"), rs.getString("PreMovTipMovCod"), rs.getDouble("PreMovMonDes"), rs.getString("PreMovEstReg")));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] AL LISTAR MOVIMIENTOS: " + e.getMessage());
        }
        return lista;
    }
}