package dao;

import conexion.ConexionBD;
import modelo.Prestamo;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public boolean insertar(Prestamo p) {
        String sql = "INSERT INTO R1T_PRESTAMO (PreTraCod, PreTipCod, PreSec, PreCtaCod, PreFec, PreMon, PreCuo, PreMonCuo, PreMonAcuDes, PreEstReg) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getPreTraCod());
            ps.setInt(2, p.getPreTipCod());
            ps.setInt(3, p.getPreSec());
            ps.setInt(4, p.getPreCtaCod());
            ps.setInt(5, p.getPreFec());
            ps.setDouble(6, p.getPreMon());
            ps.setInt(7, p.getPreCuo());
            ps.setDouble(8, p.getPreMonCuo());
            ps.setDouble(9, p.getPreMonAcuDes());
            ps.setString(10, p.getPreEstReg() == null ? "A" : p.getPreEstReg());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL INSERTAR PRESTAMO: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Prestamo p) {
        String sql = "UPDATE R1T_PRESTAMO SET PreCtaCod=?, PreFec=?, PreMon=?, PreCuo=?, PreMonCuo=?, PreMonAcuDes=? WHERE PreTraCod=? AND PreTipCod=? AND PreSec=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, p.getPreCtaCod());
            ps.setInt(2, p.getPreFec());
            ps.setDouble(3, p.getPreMon());
            ps.setInt(4, p.getPreCuo());
            ps.setDouble(5, p.getPreMonCuo());
            ps.setDouble(6, p.getPreMonAcuDes());
            ps.setInt(7, p.getPreTraCod());
            ps.setInt(8, p.getPreTipCod());
            ps.setInt(9, p.getPreSec());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL MODIFICAR PRESTAMO: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, int tipCod, int sec, String nuevoEstado) {
        String sql = "UPDATE R1T_PRESTAMO SET PreEstReg=? WHERE PreTraCod=? AND PreTipCod=? AND PreSec=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, traCod);
            ps.setInt(3, tipCod);
            ps.setInt(4, sec);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR] AL CAMBIAR ESTADO DE PRESTAMO: " + e.getMessage());
            return false;
        }
    }

    public Prestamo buscarPorIds(int traCod, int tipCod, int sec) {
        String sql = "SELECT * FROM R1T_PRESTAMO WHERE PreTraCod=? AND PreTipCod=? AND PreSec=?";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, traCod); ps.setInt(2, tipCod); ps.setInt(3, sec);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(rs.getInt("PreTraCod"), rs.getInt("PreTipCod"), rs.getInt("PreSec"), rs.getInt("PreCtaCod"), rs.getInt("PreFec"), rs.getDouble("PreMon"), rs.getInt("PreCuo"), rs.getDouble("PreMonCuo"), rs.getDouble("PreMonAcuDes"), rs.getString("PreEstReg"));
                }
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] AL BUSCAR PRESTAMO: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, int tipCod, int sec) {
        return buscarPorIds(traCod, tipCod, sec) != null;
    }

    public List<Prestamo> listarTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_PRESTAMO ORDER BY PreTraCod, PreTipCod, PreSec";
        try (Connection con = ConexionBD.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Prestamo(rs.getInt("PreTraCod"), rs.getInt("PreTipCod"), rs.getInt("PreSec"), rs.getInt("PreCtaCod"), rs.getInt("PreFec"), rs.getDouble("PreMon"), rs.getInt("PreCuo"), rs.getDouble("PreMonCuo"), rs.getDouble("PreMonAcuDes"), rs.getString("PreEstReg")));
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] AL LISTAR PRESTAMOS: " + e.getMessage());
        }
        return lista;
    }
}