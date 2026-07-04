-- =====================================================================
-- SCRIPT DE CARGA DE DATOS - TABLAS ASIGNADAS
-- Proyecto: Cuentas Corrientes de Personal
-- Tablas: ORGANIZACION, CONVENIO, DESCUENTO y DESCUENTO_MOV
-- =====================================================================

-- Ejecutar dentro de la base de datos del proyecto.
-- Ejemplo:
-- USE BD_CUENTAS_CORRIENTES_PERSONAL;
-- SOURCE C:/Users/USUARIO/OneDrive/Documentos/IngenieriaDeSistemas/cuentas_corrientes_personal/sql/carga_datos_tablas_paola.sql;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

-- ---------------------------------------------------------------------
-- 1. DATOS REFERENCIALES NECESARIOS
-- ---------------------------------------------------------------------

INSERT IGNORE INTO GZZ_ESTADO_REGISTRO (EstReg, EstRegNom, EstRegEstReg) VALUES
('A', 'Activo', 'A'),
('I', 'Inactivo', 'A'),
('*', 'Eliminado', 'A');

INSERT IGNORE INTO R1Z_TIPO_ORGANIZACION (TipOrgCod, TipOrgNom, TipOrgEstReg) VALUES
(1, 'Cooperativa', 'A'),
(2, 'Supermercado', 'A'),
(99, 'Otra', 'A');

INSERT IGNORE INTO R1Z_TIPO_DESCUENTO (TipDesCod, TipDesNom, TipDesEstReg) VALUES
('B', 'Beneficio', 'A'),
('S', 'Servicio', 'A');

INSERT IGNORE INTO R1Z_TIPO_MOVIMIENTO (TipMovCod, TipMovNom, TipMovEstReg) VALUES
('C', 'Cargo', 'A'),
('A', 'Abono', 'A');

INSERT IGNORE INTO R1Z_TIPO_TRABAJADOR (TipTraCod, TipTraNom, TipTraEstReg) VALUES
(1, 'Funcionario', 'A'),
(2, 'Empleado', 'A'),
(3, 'Obrero', 'A');

INSERT IGNORE INTO R1Z_ESTADO_TRABAJADOR (EstTraCod, EstTraNom, EstTraEstReg) VALUES
('A', 'Activo', 'A'),
('C', 'Cesado', 'A');

INSERT IGNORE INTO R1Z_TIPO_CUENTA_CORRIENTE (TipCtaCod, TipCtaNom, TipCtaEstReg) VALUES
(1, 'Planilla', 'A'),
(2, 'Vacacional', 'A'),
(3, 'Salud', 'A'),
(99, 'Otra', 'A');

-- ---------------------------------------------------------------------
-- 2. DEPENDENCIAS MINIMAS PARA QUE LAS TABLAS ASIGNADAS NO FALLEN
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1M_EMPRESA (EmpCod, EmpRazSoc, EmpRuc, EmpEstReg) VALUES
(1, 'Minera Cerro Verde S.A.A.', '20100039207', 'A');

INSERT IGNORE INTO R1M_CENTRO_COSTO (CenCosCod, CenCosNom, CenCosEstReg) VALUES
('A001', 'Gerencia General', 'A');

INSERT IGNORE INTO R1M_TRABAJADOR
(TraCod, TraNom, TraFecIng, TraFecCes, TraFecUltSalVac, TraEmpCod, TraEstCod, TraCenCosCod, TraTipCod, TraEstReg)
VALUES
(100001, 'QUISPE FLORES CAMILA FERNANDA', 20260110, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100002, 'MAMANI APAZA DIEGO ALONSO', 20250615, NULL, 20260501, 1, 'A', 'A001', 1, 'A'),
(100003, 'HUANCA CONDORI LUCIANA BELEN', 20260105, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100004, 'MENDOZA SALAS JAVIER EDUARDO', 20260112, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100005, 'PARDO VERA VALERIA SOFIA', 20260205, NULL, NULL, 1, 'A', 'A001', 3, 'A'),
(100006, 'VEGA RIOS MATEO SEBASTIAN', 20250218, NULL, 20260115, 1, 'A', 'A001', 1, 'A'),
(100007, 'TORRES LUNA ANDREA XIMENA', 20260303, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100008, 'RIOS CASTRO SEBASTIAN IGNACIO', 20250312, NULL, 20260210, 1, 'A', 'A001', 3, 'A'),
(100009, 'CASTRO RUIZ GABRIELA INES', 20260322, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100010, 'LUNA SANZ NICOLAS ANDRES', 20260405, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100011, 'RUIZ YAÑEZ MARIANA ISABEL', 20250411, NULL, 20260301, 1, 'A', 'A001', 3, 'A'),
(100012, 'SANZ CANO GABRIEL ALEJANDRO', 20260420, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100013, 'YAÑEZ VERA RENATA MILAGROS', 20260502, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100014, 'CANO MEZA ADRIAN MANUEL', 20250510, NULL, 20260405, 1, 'A', 'A001', 3, 'A'),
(100015, 'VERA CHOQUE CAMILA ANDREA', 20260515, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100016, 'MEZA LAZO JOAQUIN ALONSO', 20260520, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100017, 'CHOQUE PAZ BEATRIZ ELENA', 20250601, NULL, 20260501, 1, 'A', 'A001', 3, 'A'),
(100018, 'LAZO CARO FERNANDO JOSE', 20260605, NULL, NULL, 1, 'A', 'A001', 1, 'A'),
(100019, 'PAZ TICONA LUCIA FERNANDA', 20260610, NULL, NULL, 1, 'A', 'A001', 2, 'A'),
(100020, 'CARO DAVILA BRUNO SANTIAGO', 20250615, NULL, 20260510, 1, 'A', 'A001', 3, 'A');

INSERT IGNORE INTO R1T_CUENTA_CORRIENTE
(CtaCod, CtaTraCod, CtaTipCtaCod, CtaFecApe, CtaSalIni, CtaSalAct, CtaEstReg)
VALUES
(90000001, 100001, 1, 20260101, 1500.00, 1500.00, 'A'),
(90000002, 100002, 1, 20260101, 850.00, 850.00, 'A'),
(90000003, 100003, 1, 20260101, 1200.00, 1200.00, 'A'),
(90000004, 100004, 1, 20260101, 980.00, 980.00, 'A'),
(90000005, 100005, 1, 20260101, 760.00, 760.00, 'A'),
(90000006, 100006, 1, 20260101, 1440.00, 1440.00, 'A'),
(90000007, 100007, 1, 20260101, 600.00, 600.00, 'A'),
(90000008, 100008, 1, 20260101, 1800.00, 1800.00, 'A'),
(90000009, 100009, 1, 20260101, 950.00, 950.00, 'A'),
(90000010, 100010, 1, 20260101, 1320.00, 1320.00, 'A'),
(90000011, 100011, 1, 20260101, 700.00, 700.00, 'A'),
(90000012, 100012, 1, 20260101, 1600.00, 1600.00, 'A'),
(90000013, 100013, 1, 20260101, 1100.00, 1100.00, 'A'),
(90000014, 100014, 1, 20260101, 500.00, 500.00, 'A'),
(90000015, 100015, 1, 20260101, 2100.00, 2100.00, 'A'),
(90000016, 100016, 1, 20260101, 840.00, 840.00, 'A'),
(90000017, 100017, 1, 20260101, 1260.00, 1260.00, 'A'),
(90000018, 100018, 1, 20260101, 990.00, 990.00, 'A'),
(90000019, 100019, 1, 20260101, 1750.00, 1750.00, 'A'),
(90000020, 100020, 1, 20260101, 640.00, 640.00, 'A');

INSERT IGNORE INTO R1T_ACTA (ActCod, ActRef, ActFec, ActEstReg) VALUES
(500001, 'ACTA-BEN-001-2026', 20260105, 'A'),
(500002, 'ACTA-SER-002-2026', 20260108, 'A');

-- ---------------------------------------------------------------------
-- 3. TABLA MAESTRA: ORGANIZACION
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1M_ORGANIZACION (OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg) VALUES
(1001, 'Cooperativa San Martin', '20234567891', 1, 'A'),
(1002, 'Supermercado El Sol', '20456789123', 2, 'A'),
(1003, 'Cooperativa Los Andes', '20111222333', 1, 'A'),
(1004, 'Supermercado Plaza Norte', '20444555666', 2, 'A'),
(1005, 'Caja de Beneficios Laborales', '20555777888', 99, 'A'),
(1006, 'Cooperativa Santa Rosa', '20666111222', 1, 'A'),
(1007, 'Supermercado La Familia', '20777333444', 2, 'A'),
(1008, 'Asociacion de Servicios Sur', '20888555666', 99, 'A'),
(1009, 'Cooperativa Union Obrera', '20999777888', 1, 'A'),
(1010, 'Supermercado Economico', '20123456780', 2, 'A'),
(1011, 'Cooperativa Nueva Vida', '20223456781', 1, 'A'),
(1012, 'Servicios Integrales Lima', '20323456782', 99, 'A'),
(1013, 'Supermercado San Jose', '20423456783', 2, 'A'),
(1014, 'Cooperativa El Progreso', '20523456784', 1, 'A'),
(1015, 'Organizacion Bienestar Total', '20623456785', 99, 'A'),
(1016, 'Supermercado Los Pinos', '20723456786', 2, 'A'),
(1017, 'Cooperativa Virgen del Carmen', '20823456787', 1, 'A'),
(1018, 'Servicios Generales Andinos', '20923456788', 99, 'A'),
(1019, 'Supermercado Santa Lucia', '20134567890', 2, 'A'),
(1020, 'Cooperativa Trabajadores Unidos', '20245678901', 1, 'A');

-- ---------------------------------------------------------------------
-- 4. TABLA FUNDAMENTAL: CONVENIO
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1T_CONVENIO
(ConEmpCod, ConOrgCod, ConTipDesCod, ConSec, ConDes, ConActCod, ConCon, ConEstReg)
VALUES
(1, 1001, 'B', 1, 'Convenio de beneficio laboral', 500001, 'Beneficio otorgado al trabajador con descuento por planilla', 'A'),
(1, 1002, 'S', 1, 'Convenio de servicio institucional', 500002, 'Servicio registrado para descuento por planilla', 'A');

-- ---------------------------------------------------------------------
-- 5. TABLA FUNDAMENTAL: DESCUENTO
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1T_DESCUENTO
(DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec, DesCtaCod, DesFec,
 DesMonTot, DesNumCuo, DesCuoDes, DesMonCuo, DesMonCuoAcu, DesEstReg)
VALUES
(1, 1001, 'B', 1, 1, 90000001, 20260120, 1500.00, 6, 0, 250.00, 0.00, 'A'),
(1, 1002, 'S', 1, 1, 90000002, 20260125, 850.00, 5, 0, 170.00, 0.00, 'A'),
(1, 1001, 'B', 1, 2, 90000003, 20260126, 1200.00, 6, 0, 200.00, 0.00, 'A'),
(1, 1001, 'B', 1, 3, 90000004, 20260127, 980.00, 5, 0, 196.00, 0.00, 'A'),
(1, 1001, 'B', 1, 4, 90000005, 20260128, 760.00, 4, 0, 190.00, 0.00, 'A'),
(1, 1001, 'B', 1, 5, 90000006, 20260129, 1440.00, 6, 0, 240.00, 0.00, 'A'),
(1, 1001, 'B', 1, 6, 90000007, 20260130, 600.00, 3, 0, 200.00, 0.00, 'A'),
(1, 1001, 'B', 1, 7, 90000008, 20260201, 1800.00, 6, 0, 300.00, 0.00, 'A'),
(1, 1001, 'B', 1, 8, 90000009, 20260202, 950.00, 5, 0, 190.00, 0.00, 'A'),
(1, 1001, 'B', 1, 9, 90000010, 20260203, 1320.00, 6, 0, 220.00, 0.00, 'A'),
(1, 1001, 'B', 1, 10, 90000011, 20260204, 700.00, 5, 0, 140.00, 0.00, 'A'),
(1, 1001, 'B', 1, 11, 90000012, 20260205, 1600.00, 5, 0, 320.00, 0.00, 'A'),
(1, 1001, 'B', 1, 12, 90000013, 20260206, 1100.00, 5, 0, 220.00, 0.00, 'A'),
(1, 1001, 'B', 1, 13, 90000014, 20260207, 500.00, 5, 0, 100.00, 0.00, 'A'),
(1, 1001, 'B', 1, 14, 90000015, 20260208, 2100.00, 6, 0, 350.00, 0.00, 'A'),
(1, 1001, 'B', 1, 15, 90000016, 20260209, 840.00, 6, 0, 140.00, 0.00, 'A'),
(1, 1001, 'B', 1, 16, 90000017, 20260210, 1260.00, 6, 0, 210.00, 0.00, 'A'),
(1, 1001, 'B', 1, 17, 90000018, 20260211, 990.00, 6, 0, 165.00, 0.00, 'A'),
(1, 1001, 'B', 1, 18, 90000019, 20260212, 1750.00, 5, 0, 350.00, 0.00, 'A'),
(1, 1001, 'B', 1, 19, 90000020, 20260213, 640.00, 4, 0, 160.00, 0.00, 'A');

-- ---------------------------------------------------------------------
-- 6. TABLA FUNDAMENTAL: DESCUENTO MOV
-- ---------------------------------------------------------------------
-- Nota: en algunas bases la columna del anio de planilla aparece con
-- caracteres codificados. Por eso se inserta sin lista de columnas,
-- respetando el orden fisico de la tabla.

INSERT IGNORE INTO R1T_DESCUENTO_MOV VALUES
(1, 1001, 'B', 1, 1, 2026, 1, 1, 'C', 250.00, 'A'),
(1, 1001, 'B', 1, 1, 2026, 2, 1, 'C', 250.00, 'A'),
(1, 1001, 'B', 1, 1, 2026, 3, 1, 'C', 250.00, 'A'),
(1, 1001, 'B', 1, 1, 2026, 4, 1, 'C', 250.00, 'A'),
(1, 1001, 'B', 1, 1, 2026, 5, 1, 'C', 250.00, 'A'),
(1, 1001, 'B', 1, 1, 2026, 6, 1, 'C', 250.00, 'A'),
(1, 1002, 'S', 1, 1, 2026, 1, 1, 'C', 170.00, 'A'),
(1, 1002, 'S', 1, 1, 2026, 2, 1, 'C', 170.00, 'A'),
(1, 1002, 'S', 1, 1, 2026, 3, 1, 'C', 170.00, 'A'),
(1, 1002, 'S', 1, 1, 2026, 4, 1, 'C', 170.00, 'A');

SET FOREIGN_KEY_CHECKS=1;

SELECT 'Carga de datos de Organizacion, Convenio, Descuento y Descuento Mov finalizada.' AS Resultado;
