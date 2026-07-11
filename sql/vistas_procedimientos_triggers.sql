USE BD_CUENTAS_CORRIENTES_PERSONAL;
-- =====================================================
-- VISTA 1
-- =====================================================

CREATE VIEW V_ORGANIZACION_DETALLE AS
SELECT
    o.OrgCod AS OrgDetCod,
    o.OrgNom AS OrgDetNom,
    o.OrgRuc AS OrgDetRuc,
    t.TipOrgNom AS OrgDetTipDes,
    e.EstRegNom AS OrgDetEstDes
FROM
    R1M_ORGANIZACION o
        INNER JOIN
    R1Z_TIPO_ORGANIZACION t ON o.OrgTipOrgCod = t.TipOrgCod
        INNER JOIN
    GZZ_ESTADO_REGISTRO e ON o.OrgEstReg = e.EstReg;

-- =====================================================
-- VISTA 2
-- =====================================================

CREATE VIEW V_TRABAJADOR_CUENTA AS
SELECT
    t.TraCod AS TraCtaTraCod,
    t.TraNom AS TraCtaTraNom,
    tt.TipTraNom AS TraCtaTipTraDes,
    c.CtaCod AS TraCtaNumCta,
    tc.TipCtaNom AS TraCtaTipCtaDes,
    c.CtaSalAct AS TraCtaSalAct
FROM
    R1M_TRABAJADOR t
        INNER JOIN
    R1T_CUENTA_CORRIENTE c ON t.TraCod = c.CtaTraCod
        INNER JOIN
    R1Z_TIPO_TRABAJADOR tt ON t.TraTipCod = tt.TipTraCod
        INNER JOIN
    R1Z_TIPO_CUENTA_CORRIENTE tc ON c.CtaTipCtaCod = tc.TipCtaCod
WHERE
    t.TraEstCod = 'A' AND c.CtaEstReg = 'A';

-- =====================================================
-- VISTA 3
-- =====================================================

CREATE VIEW V_PRESTAMO_MOV_COMPLETO AS
SELECT
    m.PreMovPlaAnio AS PreMovComPlaAnio,
    m.PreMovPlaMes AS PreMovComPlaMes,
    t.TraNom AS PreMovComTraNom,
    m.PreMovMonDes AS PreMovComMonDes,
    tm.TipMovNom AS PreMovComTipDesc,
    e.EstRegNom AS PreMovComEstDesc
FROM
    R1T_PRESTAMO_MOV m
        INNER JOIN
    R1M_TRABAJADOR t ON m.PreMovTraCod = t.TraCod
        INNER JOIN
    R1Z_TIPO_MOVIMIENTO tm ON m.PreMovTipMovCod = tm.TipMovCod
        INNER JOIN
    GZZ_ESTADO_REGISTRO e ON m.PreMovEstReg = e.EstReg;

-- =====================================================
-- PROCEDIMIENTO 1: SP_Reporte_Convenios_Activos
-- =====================================================

DELIMITER //

CREATE PROCEDURE SP_Reporte_Convenios_Activos()
BEGIN
SELECT
    c.ConSec AS ID_Convenio,
    o.OrgNom AS Organizacion,
    c.ConDes AS Descripcion,
    c.ConCon AS Condicion
FROM R1T_CONVENIO c
         INNER JOIN R1M_ORGANIZACION o
                    ON c.ConOrgCod = o.OrgCod
WHERE c.ConEstReg = 'A';
END //

DELIMITER ;

-- =====================================================
-- PROCEDIMIENTO 2: SP_Historial_Descuentos_Trabajador
-- =====================================================

DELIMITER //

CREATE PROCEDURE SP_Historial_Descuentos_Trabajador(IN p_TraCod INT)
BEGIN
SELECT
    cc.CtaTraCod AS Cod_Trabajador,
    d.DesFec AS Fecha_Descuento,
    o.OrgNom AS Organizacion,
    d.DesMonTot AS Deuda_Total,
    d.DesMonCuoAcu AS Pagado_Acumulado
FROM R1T_DESCUENTO d
         INNER JOIN R1T_CUENTA_CORRIENTE cc
                    ON d.DesCtaCod = cc.CtaCod
         INNER JOIN R1M_ORGANIZACION o
                    ON d.DesConOrgCod = o.OrgCod
WHERE cc.CtaTraCod = p_TraCod;
END //

DELIMITER ;

-- =====================================================
-- PROCEDIMIENTO 3: SP_Resumen_Financiero_General
-- =====================================================

DELIMITER //

CREATE PROCEDURE SP_Resumen_Financiero_General()
BEGIN

    -- Reporte 1: Préstamos

SELECT
    PreTipCod AS Tipo_Prestamo,
    COUNT(*) AS Cantidad_Prestamos,
    SUM(PreMon) AS Monto_Total_Prestado
FROM R1T_PRESTAMO
WHERE PreEstReg = 'A'
GROUP BY PreTipCod;

-- Reporte 2: Descuentos

SELECT
    o.OrgNom AS Organizacion,
    SUM(d.DesMonTot) AS Total_Descontado
FROM R1T_DESCUENTO d
         INNER JOIN R1M_ORGANIZACION o
                    ON d.DesConOrgCod = o.OrgCod
WHERE d.DesEstReg = 'A'
GROUP BY o.OrgNom;

END //

DELIMITER ;

-- =====================================================
-- PROCEDIMIENTO 4: SP_ValidarCuotas
-- =====================================================

DELIMITER $$

CREATE PROCEDURE SP_ValidarCuotas(
    IN pCuotas INT
)
BEGIN
    IF pCuotas > 10 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El número máximo de cuotas permitidas es 10.';
END IF;
END$$

DELIMITER ;

-- =====================================================
-- TRIGGER SIMPLE
-- =====================================================

DELIMITER $$

CREATE TRIGGER TRG_ValidarMontoDescuentoMov_BI
    BEFORE INSERT ON R1T_DESCUENTO_MOV
    FOR EACH ROW
BEGIN
    IF NEW.DesMonTot <= 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El monto no puede ser negativo.';
END IF;
END$$

DELIMITER ;

-- =====================================================
-- TRIGGER MEDIANA COMPLEJIDAD
-- =====================================================

DELIMITER //

CREATE TRIGGER TRG_ValidarEstadoTrabajador_BI
    BEFORE INSERT ON R1T_PRESTAMO
    FOR EACH ROW
BEGIN
    DECLARE estado_actual CHAR(1);

    SELECT TraEstCod
    INTO estado_actual
    FROM R1M_TRABAJADOR
    WHERE TraCod = NEW.PreTraCod;

    IF estado_actual = 'C' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Operación Denegada: No se pueden registrar préstamos a trabajadores cesados.';
END IF;
END //

DELIMITER ;

-- =====================================================
-- TRIGGER MAYOR COMPLEJIDAD
-- =====================================================

DELIMITER $$

CREATE TRIGGER TRG_AlertaTopeCuotas_BI
    BEFORE INSERT ON R1T_PRESTAMO
    FOR EACH ROW
BEGIN
    CALL SP_ValidarCuotas(NEW.PreCuo);
    END$$

    DELIMITER ;