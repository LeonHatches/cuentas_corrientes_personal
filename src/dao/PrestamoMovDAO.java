package dao;

import conexion.ConexionBD;
import modelo.PrestamoMov;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoMovDAO {

    public boolean insertar(PrestamoMov pm) {
        String sql = "INSERT INTO R1T_PRESTAMO_MOV (PreMovTraCod, PreMovTipCod, PreMovFecPre, PreMovNumCuo, PreMovFecPag, PreMovMntCuo, PreMovEstReg) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pm.getPreMovTraCod());
            ps.setInt(2, pm.getPreMovTipCod());
            ps.setInt(3, pm.getPreMovFecPre());
            ps.setInt(4, pm.getPreMovNumCuo());
            ps.setInt(5, pm.getPreMovFecPag());
            ps.setDouble(6, pm.getPreMovMntCuo());

            if (pm.getPreMovEstReg() == null) {
                ps.setNull(7, Types.CHAR);
            } else {
                ps.setString(7, pm.getPreMovEstReg());
            }

            return ps.executeUpdate() > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: El movimiento ya existe o hay un problema de llave foránea.");
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar movimiento de préstamo: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarDatos(PrestamoMov pm) {
        // En modificar, solo cambiamos los datos transaccionales (fecha de pago y monto)
        String sql = "UPDATE R1T_PRESTAMO_MOV SET PreMovFecPag = ?, PreMovMntCuo = ? WHERE PreMovTraCod = ? AND PreMovTipCod = ? AND PreMovFecPre = ? AND PreMovNumCuo = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, pm.getPreMovFecPag());
            ps.setDouble(2, pm.getPreMovMntCuo());

            ps.setInt(3, pm.getPreMovTraCod());
            ps.setInt(4, pm.getPreMovTipCod());
            ps.setInt(5, pm.getPreMovFecPre());
            ps.setInt(6, pm.getPreMovNumCuo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar movimiento: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(int traCod, int tipCod, int fecPre, int numCuo, String nuevoEstado) {
        String sql = "UPDATE R1T_PRESTAMO_MOV SET PreMovEstReg = ? WHERE PreMovTraCod = ? AND PreMovTipCod = ? AND PreMovFecPre = ? AND PreMovNumCuo = ?";

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
            ps.setInt(5, numCuo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al cambiar estado: " + e.getMessage());
            return false;
        }
    }

    public PrestamoMov buscarPorIds(int traCod, int tipCod, int fecPre, int numCuo) {
        String sql = "SELECT * FROM R1T_PRESTAMO_MOV WHERE PreMovTraCod = ? AND PreMovTipCod = ? AND PreMovFecPre = ? AND PreMovNumCuo = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, traCod);
            ps.setInt(2, tipCod);
            ps.setInt(3, fecPre);
            ps.setInt(4, numCuo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new PrestamoMov(
                            rs.getInt("PreMovTraCod"),
                            rs.getInt("PreMovTipCod"),
                            rs.getInt("PreMovFecPre"),
                            rs.getInt("PreMovNumCuo"),
                            rs.getInt("PreMovFecPag"),
                            rs.getDouble("PreMovMntCuo"),
                            rs.getString("PreMovEstReg")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar movimiento: " + e.getMessage());
        }
        return null;
    }

    public boolean existe(int traCod, int tipCod, int fecPre, int numCuo) {
        return buscarPorIds(traCod, tipCod, fecPre, numCuo) != null;
    }

    public List<PrestamoMov> listarTodos() {
        List<PrestamoMov> lista = new ArrayList<>();
        String sql = "SELECT * FROM R1T_PRESTAMO_MOV ORDER BY PreMovTraCod, PreMovTipCod, PreMovFecPre, PreMovNumCuo";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new PrestamoMov(
                        rs.getInt("PreMovTraCod"),
                        rs.getInt("PreMovTipCod"),
                        rs.getInt("PreMovFecPre"),
                        rs.getInt("PreMovNumCuo"),
                        rs.getInt("PreMovFecPag"),
                        rs.getDouble("PreMovMntCuo"),
                        rs.getString("PreMovEstReg")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al listar movimientos: " + e.getMessage());
        }
        return lista;
    }
}