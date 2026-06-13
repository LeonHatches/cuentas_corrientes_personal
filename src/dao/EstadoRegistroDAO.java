package dao;

import conexion.ConexionBD;
import modelo.EstadoRegistro;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstadoRegistroDAO {

    public boolean insertar(EstadoRegistro estado) {
        String sql = "INSERT INTO GZZ_ESTADO_REGISTRO (EstReg, EstRegNom, EstRegEstReg) VALUES (?, ?, ?)";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, estado.getEstReg());
            ps.setString(2, estado.getEstRegNom());

            if (estado.getEstRegEstReg() == null) {
                ps.setNull(3, Types.CHAR);
            } else {
                ps.setString(3, estado.getEstRegEstReg());
            }

            int filas = ps.executeUpdate();


            if ("A".equals(estado.getEstReg()) && estado.getEstRegEstReg() == null) {
                cambiarEstadoRegistro("A", "A");
            }

            return filas > 0;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Error: el código ya existe o el estado de registro no existe.");
            System.out.println("Detalle: " + e.getMessage());
            return false;
        } catch (SQLException e) {
            System.out.println("Error al insertar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean modificarNombre(String codigo, String nuevoNombre) {
        String sql = "UPDATE GZZ_ESTADO_REGISTRO SET EstRegNom = ? WHERE EstReg = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setString(2, codigo);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al modificar estado de registro: " + e.getMessage());
            return false;
        }
    }

    public boolean cambiarEstadoRegistro(String codigo, String nuevoEstado) {
        String sql = "UPDATE GZZ_ESTADO_REGISTRO SET EstRegEstReg = ? WHERE EstReg = ?";

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

    public EstadoRegistro buscarPorCodigo(String codigo) {
        String sql = "SELECT EstReg, EstRegNom, EstRegEstReg FROM GZZ_ESTADO_REGISTRO WHERE EstReg = ?";

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new EstadoRegistro(
                            rs.getString("EstReg"),
                            rs.getString("EstRegNom"),
                            rs.getString("EstRegEstReg")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar estado de registro: " + e.getMessage());
        }

        return null;
    }

    public boolean existe(String codigo) {
        return buscarPorCodigo(codigo) != null;
    }

    public List<EstadoRegistro> listarTodos() {
        List<EstadoRegistro> lista = new ArrayList<>();

        String sql = """
                SELECT EstReg, EstRegNom, EstRegEstReg
                FROM GZZ_ESTADO_REGISTRO
                ORDER BY
                    CASE EstReg
                        WHEN 'A' THEN 1
                        WHEN 'I' THEN 2
                        WHEN '*' THEN 3
                        ELSE 4
                    END
                """;

        try (Connection con = ConexionBD.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EstadoRegistro estado = new EstadoRegistro(
                        rs.getString("EstReg"),
                        rs.getString("EstRegNom"),
                        rs.getString("EstRegEstReg")
                );

                lista.add(estado);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar estados de registro: " + e.getMessage());
        }

        return lista;
    }
}