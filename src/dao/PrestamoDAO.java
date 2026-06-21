package dao;

import conexion.ConexionBD;
import modelo.Prestamo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public boolean insertar(Prestamo p) {
        String sql = "INSERT INTO R1T_PRESTAMO (PreTraCod, PreTipCod, PreFecPre, PreMnt, PreCanCuo, PreMntCuo, PreEstReg) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getPreTraCod());
            ps.setInt(2, p.getPreTipCod());
            ps.setInt(3, p.getPreFecPre());
            ps.setDouble(4, p.getPreMnt());
            ps.setInt(5, p.getPreCanCuo());
            ps.setDouble(6, p.getPreMntCuo());

            if (p.getPreEstReg() == null) {
                ps.setNull(7, Types.CHAR);
            } else {
                ps.setString(7, p.getPreEstReg());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: El préstamo ya existe o hay un problema de integridad (Llave Foránea).");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(Prestamo p) {
        // En modificar, solo podemos cambiar datos que NO son llaves primarias
        String sql = "UPDATE R1T_PRESTAMO SET PreMnt = ?, PreCanCuo = ?, PreMntCuo = ? WHERE PreTraCod = ? AND PreTipCod = ? AND PreFecPre = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, p.getPreMnt());
            ps.setInt(2, p.getPreCanCuo());
            ps.setDouble(3, p.getPreMntCuo());

            // Llaves en el WHERE
            ps.setInt(4, p.getPreTraCod());
            ps.setInt(5, p.getPreTipCod());
            ps.setInt(6, p.getPreFecPre());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, int tipCod, int fecPre, String nuevoEstado) {
        String sql = "UPDATE R1T_PRESTAMO SET PreEstReg = ? WHERE PreTraCod = ? AND PreTipCod = ? AND PreFecPre = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            if (nuevoEstado == null) {
                ps.setNull(1, Types.CHAR);
            } else {
                ps.setString(1, nuevoEstado);
            }

            ps.setInt(2, traCod);
            ps.setInt(3, tipCod);
            ps.setInt(4, fecPre);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public Prestamo buscarPorIds(int traCod, int tipCod, int fecPre) {
        String sql = "SELECT * FROM R1T_PRESTAMO WHERE PreTraCod = ? AND PreTipCod = ? AND PreFecPre = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, traCod);
            ps.setInt(2, tipCod);
            ps.setInt(3, fecPre);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                            rs.getInt("PreTraCod"),
                            rs.getInt("PreTipCod"),
                            rs.getInt("PreFecPre"),
                            rs.getDouble("PreMnt"),
                            rs.getInt("PreCanCuo"),
                            rs.getDouble("PreMntCuo"),
                            rs.getString("PreEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar préstamo: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, int tipCod, int fecPre) {
        return buscarPorIds(traCod, tipCod, fecPre) != null;
    }

    public List<Prestamo> listarTodos() {
        List<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_PRESTAMO ORDER BY PreTraCod, PreTipCod, PreFecPre";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Prestamo(
                        rs.getInt("PreTraCod"),
                        rs.getInt("PreTipCod"),
                        rs.getInt("PreFecPre"),
                        rs.getDouble("PreMnt"),
                        rs.getInt("PreCanCuo"),
                        rs.getDouble("PreMntCuo"),
                        rs.getString("PreEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar préstamos: " + e.getMessage());
        }
        return lista;
    }
}