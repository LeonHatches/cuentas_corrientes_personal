-- =====================================================================
-- SCRIPT DE CARGA DE DATOS - TABLAS ASIGNADAS PAOLA
-- Proyecto: Cuentas Corrientes de Personal
-- Tablas: ORGANIZACION, CONVENIO, DESCUENTO y DESCUENTO_MOV
-- =====================================================================

USE BD_CUENTAS_CORRIENTES_PERSONAL;

SET NAMES utf8mb4;
SET SQL_SAFE_UPDATES = 0;
SET FOREIGN_KEY_CHECKS = 0;

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
('A', 'Abono', 'A'),
('C', 'Cargo', 'A'),
('D', 'Descuento planilla', 'A');

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
-- 2. DEPENDENCIAS PARA LAS TABLAS ASIGNADAS
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1M_EMPRESA (EmpCod, EmpRazSoc, EmpRuc, EmpEstReg) VALUES
(1, 'Agroindustrial Santa Rosa S.A.C.', '20601234561', 'A'),
(2, 'Servicios Generales del Sur S.A.C.', '20567890123', 'A');

INSERT IGNORE INTO R1M_CENTRO_COSTO (CenCosCod, CenCosNom, CenCosEstReg) VALUES
('ADM1', 'Administracion Central', 'A'),
('LOG1', 'Logistica y Almacen', 'A'),
('PRO1', 'Produccion Planta', 'A');

INSERT IGNORE INTO R1M_TRABAJADOR
(TraCod, TraNom, TraFecIng, TraFecCes, TraFecUltSalVac, TraEmpCod, TraEstCod, TraCenCosCod, TraTipCod, TraEstReg)
VALUES
(100001, 'MARIA FERNANDA QUISPE RAMOS', 20250110, NULL, NULL, 1, 'A', 'ADM1', 1, 'A'),
(100002, 'DIEGO ALONSO MAMANI APAZA', 20250115, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100003, 'LUCIANA BELEN HUANCA CONDORI', 20250120, NULL, NULL, 1, 'A', 'LOG1', 2, 'A'),
(100004, 'JAVIER EDUARDO MENDOZA SALAS', 20250125, NULL, NULL, 1, 'A', 'LOG1', 1, 'A'),
(100005, 'VALERIA SOFIA PARDO VERA', 20250201, NULL, NULL, 1, 'A', 'PRO1', 3, 'A'),
(100006, 'MATEO SEBASTIAN VEGA RIOS', 20250205, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100007, 'ANDREA XIMENA TORRES LUNA', 20250210, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100008, 'SEBASTIAN IGNACIO RIOS CASTRO', 20250215, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100009, 'GABRIELA INES CASTRO RUIZ', 20250220, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100010, 'NICOLAS ANDRES LUNA SANZ', 20250225, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100011, 'MARIANA ISABEL RUIZ YANEZ', 20250301, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100012, 'GABRIEL ALEJANDRO SANZ CANO', 20250305, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100013, 'RENATA MILAGROS YANEZ VERA', 20250310, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100014, 'ADRIAN MANUEL CANO MEZA', 20250315, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100015, 'CAMILA ANDREA VERA CHOQUE', 20250320, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100016, 'JOAQUIN ALONSO MEZA LAZO', 20250325, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100017, 'BEATRIZ ELENA CHOQUE PAZ', 20250401, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100018, 'FERNANDO JOSE LAZO CARO', 20250405, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100019, 'LUCIA FERNANDA PAZ TICONA', 20250410, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100020, 'BRUNO SANTIAGO CARO DAVILA', 20250415, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100021, 'DANIELA ISABEL SALAS PINEDA', 20250420, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100022, 'MARCO ANTONIO PINEDA ROJAS', 20250425, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100023, 'ELENA PATRICIA ROJAS VARGAS', 20250501, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100024, 'CARLOS EDUARDO VARGAS LOPEZ', 20250505, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100025, 'SOFIA MILAGROS LOPEZ NUNEZ', 20250510, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100026, 'PABLO ANDRES NUNEZ RAMOS', 20250515, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100027, 'CLAUDIA MARIA RAMOS ORTEGA', 20250520, NULL, NULL, 1, 'A', 'PRO1', 1, 'A'),
(100028, 'HUGO RAFAEL ORTEGA MOLINA', 20250525, NULL, NULL, 1, 'A', 'ADM1', 2, 'A'),
(100029, 'ANA LUCIA MOLINA CAMPOS', 20250601, NULL, NULL, 1, 'A', 'LOG1', 3, 'A'),
(100030, 'LUIS MIGUEL CAMPOS HUAMAN', 20250605, NULL, NULL, 1, 'A', 'PRO1', 1, 'A');

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
(90000020, 100020, 1, 20260101, 640.00, 640.00, 'A'),
(90000021, 100021, 1, 20260101, 900.00, 900.00, 'A'),
(90000022, 100022, 1, 20260101, 600.00, 600.00, 'A'),
(90000023, 100023, 1, 20260101, 720.00, 720.00, 'A'),
(90000024, 100024, 1, 20260101, 480.00, 480.00, 'A'),
(90000025, 100025, 1, 20260101, 540.00, 540.00, 'A'),
(90000026, 100026, 1, 20260101, 960.00, 960.00, 'A'),
(90000027, 100027, 1, 20260101, 450.00, 450.00, 'A'),
(90000028, 100028, 1, 20260101, 720.00, 720.00, 'A'),
(90000029, 100029, 1, 20260101, 800.00, 800.00, 'A'),
(90000030, 100030, 1, 20260101, 1000.00, 1000.00, 'A');

INSERT IGNORE INTO R1T_ACTA (ActCod, ActRef, ActFec, ActEstReg) VALUES
(500001, 'ACTA-BEN-001-2026', 20260105, 'A'),
(500002, 'ACTA-SER-002-2026', 20260108, 'A'),
(500003, 'ACTA-BEN-003-2026', 20260109, 'A'),
(500004, 'ACTA-SER-004-2026', 20260110, 'A'),
(500005, 'ACTA-BEN-005-2026', 20260111, 'A'),
(500006, 'ACTA-BEN-006-2026', 20260112, 'A'),
(500007, 'ACTA-SER-007-2026', 20260113, 'A'),
(500008, 'ACTA-SER-008-2026', 20260114, 'A'),
(500009, 'ACTA-BEN-009-2026', 20260115, 'A'),
(500010, 'ACTA-SER-010-2026', 20260116, 'A'),
(500011, 'ACTA-BEN-011-2026', 20260117, 'A'),
(500012, 'ACTA-SER-012-2026', 20260118, 'A'),
(500013, 'ACTA-SER-013-2026', 20260119, 'A'),
(500014, 'ACTA-BEN-014-2026', 20260120, 'A'),
(500015, 'ACTA-BEN-015-2026', 20260121, 'A'),
(500016, 'ACTA-SER-016-2026', 20260122, 'A'),
(500017, 'ACTA-BEN-017-2026', 20260123, 'A'),
(500018, 'ACTA-SER-018-2026', 20260124, 'A'),
(500019, 'ACTA-SER-019-2026', 20260125, 'A'),
(500020, 'ACTA-BEN-020-2026', 20260126, 'A'),
(500021, 'ACTA-BEN-021-2026', 20260127, 'A'),
(500022, 'ACTA-SER-022-2026', 20260128, 'A'),
(500023, 'ACTA-BEN-023-2026', 20260129, 'A'),
(500024, 'ACTA-SER-024-2026', 20260130, 'A'),
(500025, 'ACTA-BEN-025-2026', 20260131, 'A'),
(500026, 'ACTA-BEN-026-2026', 20260201, 'A'),
(500027, 'ACTA-SER-027-2026', 20260202, 'A'),
(500028, 'ACTA-SER-028-2026', 20260203, 'A'),
(500029, 'ACTA-BEN-029-2026', 20260204, 'A'),
(500030, 'ACTA-SER-030-2026', 20260205, 'A');

-- ---------------------------------------------------------------------
-- 3. TABLA MAESTRA: ORGANIZACION
-- Codigos segun diccionario: 0001 -- 9999
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1M_ORGANIZACION (OrgCod, OrgNom, OrgRuc, OrgTipOrgCod, OrgEstReg) VALUES
(1, 'Cooperativa San Martin', '20234567891', 1, 'A'),
(2, 'Supermercado El Sol', '20456789123', 2, 'A'),
(3, 'Cooperativa Los Andes', '20111222333', 1, 'A'),
(4, 'Supermercado Plaza Norte', '20444555666', 2, 'A'),
(5, 'Caja de Beneficios Laborales', '20555777888', 99, 'A'),
(6, 'Cooperativa Santa Rosa', '20666111222', 1, 'A'),
(7, 'Supermercado La Familia', '20777333444', 2, 'A'),
(8, 'Asociacion de Servicios Sur', '20888555666', 99, 'A'),
(9, 'Cooperativa Union Obrera', '20999777888', 1, 'A'),
(10, 'Supermercado Economico', '20123456780', 2, 'A'),
(11, 'Cooperativa Nueva Vida', '20223456781', 1, 'A'),
(12, 'Servicios Integrales Lima', '20323456782', 99, 'A'),
(13, 'Supermercado San Jose', '20423456783', 2, 'A'),
(14, 'Cooperativa El Progreso', '20523456784', 1, 'A'),
(15, 'Organizacion Bienestar Total', '20623456785', 99, 'A'),
(16, 'Supermercado Los Pinos', '20723456786', 2, 'A'),
(17, 'Cooperativa Virgen del Carmen', '20823456787', 1, 'A'),
(18, 'Servicios Generales Andinos', '20923456788', 99, 'A'),
(19, 'Supermercado Santa Lucia', '20134567890', 2, 'A'),
(20, 'Cooperativa Trabajadores Unidos', '20245678901', 1, 'A'),
(21, 'Supermercado Valle Verde', '20345678902', 2, 'A'),
(22, 'Cooperativa Esperanza', '20445678903', 1, 'A');

-- ---------------------------------------------------------------------
-- 4. TABLA FUNDAMENTAL: CONVENIO
-- 2 registros base + 30 registros extra usados en pruebas.
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1T_CONVENIO
(ConEmpCod, ConOrgCod, ConTipDesCod, ConSec, ConDes, ConActCod, ConCon, ConEstReg)
VALUES
(1, 1, 'B', 1, 'Convenio de beneficio laboral', 500001, 'Beneficio otorgado al trabajador con descuento por planilla', 'A'),
(1, 2, 'S', 1, 'Convenio de servicio institucional', 500002, 'Servicio registrado para descuento por planilla', 'A'),
(1, 3, 'B', 1, 'Convenio de beneficio cooperativo', 500003, 'Beneficio mensual autorizado para trabajadores afiliados', 'A'),
(1, 4, 'S', 1, 'Convenio de servicio comercial', 500004, 'Servicio de consumo autorizado con descuento por planilla', 'A'),
(1, 5, 'B', 1, 'Convenio de apoyo laboral', 500005, 'Beneficio institucional aplicado al personal registrado', 'A'),
(1, 6, 'B', 1, 'Convenio cooperativo Santa Rosa', 500006, 'Beneficio por afiliacion cooperativa del trabajador', 'A'),
(1, 7, 'S', 1, 'Convenio supermercado familiar', 500007, 'Servicio de compra mensual con cargo por planilla', 'A'),
(1, 8, 'S', 1, 'Convenio servicios integrales', 500008, 'Servicio institucional autorizado por convenio', 'A'),
(1, 9, 'B', 1, 'Convenio union obrera', 500009, 'Beneficio laboral para trabajadores asociados', 'A'),
(1, 10, 'S', 1, 'Convenio supermercado economico', 500010, 'Servicio de consumo personal con descuento mensual', 'A'),
(1, 11, 'B', 1, 'Convenio nueva vida', 500011, 'Beneficio cooperativo registrado para el personal', 'A'),
(1, 12, 'S', 1, 'Convenio servicios Lima', 500012, 'Servicio mensual autorizado para trabajadores', 'A'),
(1, 13, 'S', 1, 'Convenio supermercado San Jose', 500013, 'Servicio comercial descontado por planilla', 'A'),
(1, 14, 'B', 1, 'Convenio el progreso', 500014, 'Beneficio de apoyo economico institucional', 'A'),
(1, 15, 'B', 1, 'Convenio bienestar total', 500015, 'Beneficio laboral registrado por convenio', 'A'),
(1, 16, 'S', 1, 'Convenio supermercado Los Pinos', 500016, 'Servicio de consumo mediante descuento programado', 'A'),
(1, 17, 'B', 1, 'Convenio Virgen del Carmen', 500017, 'Beneficio cooperativo para trabajadores activos', 'A'),
(1, 18, 'S', 1, 'Convenio servicios andinos', 500018, 'Servicio general autorizado para descuento', 'A'),
(1, 19, 'S', 1, 'Convenio supermercado Santa Lucia', 500019, 'Servicio de compras con descuento por planilla', 'A'),
(1, 20, 'B', 1, 'Convenio trabajadores unidos', 500020, 'Beneficio institucional para personal afiliado', 'A'),
(1, 21, 'S', 1, 'Convenio Valle Verde', 500021, 'Servicio de compras autorizado con descuento mensual', 'A'),
(1, 22, 'B', 1, 'Convenio Esperanza', 500022, 'Beneficio cooperativo por afiliacion del trabajador', 'A'),
(1, 1, 'B', 2, 'Segundo convenio San Martin', 500023, 'Beneficio adicional para trabajadores afiliados', 'A'),
(1, 2, 'S', 2, 'Segundo convenio El Sol', 500024, 'Servicio adicional autorizado por planilla', 'A'),
(1, 3, 'B', 2, 'Segundo convenio Los Andes', 500025, 'Beneficio complementario mensual', 'A'),
(1, 4, 'S', 2, 'Segundo convenio Plaza Norte', 500026, 'Servicio complementario para trabajadores', 'A'),
(1, 5, 'B', 2, 'Segundo convenio Caja Beneficios', 500027, 'Beneficio de apoyo extraordinario', 'A'),
(1, 6, 'B', 2, 'Segundo convenio Santa Rosa', 500028, 'Beneficio adicional cooperativo', 'A'),
(1, 7, 'S', 2, 'Segundo convenio La Familia', 500029, 'Servicio de consumo complementario', 'A'),
(1, 8, 'S', 2, 'Segundo convenio Servicios Sur', 500030, 'Servicio institucional complementario', 'A'),
(1, 9, 'B', 2, 'Segundo convenio Union Obrera', 500001, 'Beneficio adicional por afiliacion', 'A'),
(1, 10, 'S', 2, 'Segundo convenio Economico', 500002, 'Servicio adicional por descuento mensual', 'A');

-- ---------------------------------------------------------------------
-- 5. TABLA TRANSACCIONAL: DESCUENTO
-- 2 registros base + 30 registros extra usados en pruebas.
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1T_DESCUENTO
(DesConEmpCod, DesConOrgCod, DesConTipDesCod, DesConSec, DesSec, DesCtaCod, DesFec,
 DesMonTot, DesNumCuo, DesCuoDes, DesMonCuo, DesMonCuoAcu, DesEstReg)
VALUES
(1, 1, 'B', 1, 1, 90000001, 20260120, 1500.00, 6, 0, 250.00, 0.00, 'A'),
(1, 2, 'S', 1, 1, 90000002, 20260125, 500.00, 5, 0, 100.00, 0.00, 'A'),
(1, 3, 'B', 1, 1, 90000003, 20260126, 1200.00, 6, 0, 200.00, 0.00, 'A'),
(1, 4, 'S', 1, 1, 90000004, 20260127, 980.00, 5, 0, 196.00, 0.00, 'A'),
(1, 5, 'B', 1, 1, 90000005, 20260128, 760.00, 4, 0, 190.00, 0.00, 'A'),
(1, 6, 'B', 1, 1, 90000006, 20260129, 1440.00, 6, 0, 240.00, 0.00, 'A'),
(1, 7, 'S', 1, 1, 90000007, 20260130, 600.00, 3, 0, 200.00, 0.00, 'A'),
(1, 8, 'S', 1, 1, 90000008, 20260201, 1800.00, 6, 0, 300.00, 0.00, 'A'),
(1, 9, 'B', 1, 1, 90000009, 20260202, 950.00, 5, 0, 190.00, 0.00, 'A'),
(1, 10, 'S', 1, 1, 90000010, 20260203, 1320.00, 6, 0, 220.00, 0.00, 'A'),
(1, 11, 'B', 1, 1, 90000011, 20260204, 700.00, 5, 0, 140.00, 0.00, 'A'),
(1, 12, 'S', 1, 1, 90000012, 20260205, 1600.00, 5, 0, 320.00, 0.00, 'A'),
(1, 13, 'S', 1, 1, 90000013, 20260206, 1100.00, 5, 0, 220.00, 0.00, 'A'),
(1, 14, 'B', 1, 1, 90000014, 20260207, 500.00, 5, 0, 100.00, 0.00, 'A'),
(1, 15, 'B', 1, 1, 90000015, 20260208, 2100.00, 6, 0, 350.00, 0.00, 'A'),
(1, 16, 'S', 1, 1, 90000016, 20260209, 840.00, 6, 0, 140.00, 0.00, 'A'),
(1, 17, 'B', 1, 1, 90000017, 20260210, 1260.00, 6, 0, 210.00, 0.00, 'A'),
(1, 18, 'S', 1, 1, 90000018, 20260211, 990.00, 6, 0, 165.00, 0.00, 'A'),
(1, 19, 'S', 1, 1, 90000019, 20260212, 1750.00, 5, 0, 350.00, 0.00, 'A'),
(1, 20, 'B', 1, 1, 90000020, 20260213, 640.00, 4, 0, 160.00, 0.00, 'A'),
(1, 21, 'S', 1, 1, 90000021, 20260214, 900.00, 6, 0, 150.00, 0.00, 'A'),
(1, 22, 'B', 1, 1, 90000022, 20260215, 600.00, 6, 0, 100.00, 0.00, 'A'),
(1, 1, 'B', 2, 1, 90000023, 20260216, 720.00, 6, 0, 120.00, 0.00, 'A'),
(1, 2, 'S', 2, 1, 90000024, 20260217, 480.00, 4, 0, 120.00, 0.00, 'A'),
(1, 3, 'B', 2, 1, 90000025, 20260218, 540.00, 3, 0, 180.00, 0.00, 'A'),
(1, 4, 'S', 2, 1, 90000026, 20260219, 960.00, 6, 0, 160.00, 0.00, 'A'),
(1, 5, 'B', 2, 1, 90000027, 20260220, 450.00, 3, 0, 150.00, 0.00, 'A'),
(1, 6, 'B', 2, 1, 90000028, 20260221, 720.00, 6, 0, 120.00, 0.00, 'A'),
(1, 7, 'S', 2, 1, 90000029, 20260222, 800.00, 5, 0, 160.00, 0.00, 'A'),
(1, 8, 'S', 2, 1, 90000030, 20260223, 1000.00, 5, 0, 200.00, 0.00, 'A'),
(1, 9, 'B', 2, 1, 90000001, 20260224, 580.00, 5, 0, 116.00, 0.00, 'A'),
(1, 10, 'S', 2, 1, 90000002, 20260225, 750.00, 5, 0, 150.00, 0.00, 'A');

-- ---------------------------------------------------------------------
-- 6. TABLA FUNDAMENTAL: DESCUENTO MOV
-- 2 registros base + 30 registros extra usados en pruebas.
-- ---------------------------------------------------------------------

INSERT IGNORE INTO R1T_DESCUENTO_MOV
(DesMovConEmpCod, DesMovConOrgCod, DesMovConTipDesCod, DesMovConSec, DesMovDesSec,
 `DesMovPlaAño`, DesMovPlaMes, DesMovPlaNum, DesMovTipMovCod, DesMovMon, DesMovEstReg)
VALUES
(1, 1, 'B', 1, 1, 2026, 1, 1, 'C', 250.00, 'A'),
(1, 1, 'B', 1, 1, 2026, 2, 1, 'C', 250.00, 'A'),
(1, 1, 'B', 1, 1, 2026, 3, 1, 'C', 250.00, 'A'),
(1, 1, 'B', 1, 1, 2026, 4, 1, 'C', 250.00, 'A'),
(1, 1, 'B', 1, 1, 2026, 5, 1, 'C', 250.00, 'A'),
(1, 1, 'B', 1, 1, 2026, 6, 1, 'C', 250.00, 'A'),
(1, 2, 'S', 1, 1, 2026, 1, 1, 'C', 100.00, 'A'),
(1, 2, 'S', 1, 1, 2026, 2, 1, 'C', 100.00, 'A'),
(1, 3, 'B', 1, 1, 2026, 1, 1, 'C', 200.00, 'A'),
(1, 3, 'B', 1, 1, 2026, 2, 1, 'C', 200.00, 'A'),
(1, 4, 'S', 1, 1, 2026, 1, 1, 'C', 196.00, 'A'),
(1, 4, 'S', 1, 1, 2026, 2, 1, 'C', 196.00, 'A'),
(1, 5, 'B', 1, 1, 2026, 1, 1, 'C', 190.00, 'A'),
(1, 5, 'B', 1, 1, 2026, 2, 1, 'C', 190.00, 'A'),
(1, 6, 'B', 1, 1, 2026, 1, 1, 'C', 240.00, 'A'),
(1, 6, 'B', 1, 1, 2026, 2, 1, 'C', 240.00, 'A'),
(1, 7, 'S', 1, 1, 2026, 1, 1, 'C', 200.00, 'A'),
(1, 7, 'S', 1, 1, 2026, 2, 1, 'C', 200.00, 'A'),
(1, 8, 'S', 1, 1, 2026, 1, 1, 'C', 300.00, 'A'),
(1, 8, 'S', 1, 1, 2026, 2, 1, 'C', 300.00, 'A'),
(1, 9, 'B', 1, 1, 2026, 1, 1, 'C', 190.00, 'A'),
(1, 9, 'B', 1, 1, 2026, 2, 1, 'C', 190.00, 'A'),
(1, 10, 'S', 1, 1, 2026, 1, 1, 'C', 220.00, 'A'),
(1, 10, 'S', 1, 1, 2026, 2, 1, 'C', 220.00, 'A'),
(1, 11, 'B', 1, 1, 2026, 1, 1, 'C', 140.00, 'A'),
(1, 11, 'B', 1, 1, 2026, 2, 1, 'C', 140.00, 'A'),
(1, 12, 'S', 1, 1, 2026, 1, 1, 'C', 320.00, 'A'),
(1, 12, 'S', 1, 1, 2026, 2, 1, 'C', 320.00, 'A'),
(1, 13, 'S', 1, 1, 2026, 1, 1, 'C', 220.00, 'A'),
(1, 13, 'S', 1, 1, 2026, 2, 1, 'C', 220.00, 'A'),
(1, 14, 'B', 1, 1, 2026, 1, 1, 'C', 100.00, 'A'),
(1, 14, 'B', 1, 1, 2026, 2, 1, 'C', 100.00, 'A');

-- Sincroniza cuotas descontadas y monto acumulado cuando los movimientos
-- se insertan por SQL directo, fuera de la interfaz Java.
UPDATE R1T_DESCUENTO d
SET
    d.DesCuoDes = (
        SELECT COUNT(*)
        FROM R1T_DESCUENTO_MOV m
        WHERE m.DesMovConEmpCod = d.DesConEmpCod
          AND m.DesMovConOrgCod = d.DesConOrgCod
          AND m.DesMovConTipDesCod = d.DesConTipDesCod
          AND m.DesMovConSec = d.DesConSec
          AND m.DesMovDesSec = d.DesSec
          AND m.DesMovTipMovCod = 'C'
          AND m.DesMovEstReg = 'A'
    ),
    d.DesMonCuoAcu = COALESCE((
        SELECT SUM(
            CASE
                WHEN m.DesMovTipMovCod = 'C' THEN m.DesMovMon
                WHEN m.DesMovTipMovCod = 'A' THEN -m.DesMovMon
                ELSE 0
            END
        )
        FROM R1T_DESCUENTO_MOV m
        WHERE m.DesMovConEmpCod = d.DesConEmpCod
          AND m.DesMovConOrgCod = d.DesConOrgCod
          AND m.DesMovConTipDesCod = d.DesConTipDesCod
          AND m.DesMovConSec = d.DesConSec
          AND m.DesMovDesSec = d.DesSec
          AND m.DesMovEstReg = 'A'
    ), 0);

SET FOREIGN_KEY_CHECKS = 1;
SET SQL_SAFE_UPDATES = 1;

