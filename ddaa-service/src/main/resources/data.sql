INSERT INTO COMUNA (ID_Comuna, Nombre) VALUES ('13101', 'Santiago');
INSERT INTO RUTS (Rut, Nombre) VALUES (76012345, 'Titular Demo SpA');
INSERT INTO RUTS (Rut, Nombre) VALUES (76054321, 'Proveedor Obras Ltda.');
INSERT INTO INSTALACION (Nombre) VALUES ('Planta Demo');
INSERT INTO CUENCA (Nombre) VALUES ('Cuenca Maipo');
INSERT INTO SUBCUENCA (FK_ID_CUENCA, NombreSubcuenca, SHAC, Reserva, DeclaracionAgotamiento, ZonaRestriccionSuperficial, ZonaRestriccionSubterranea, EstadoSHAC, PlanGestionHidrica)
VALUES (1, 'Subcuenca Alto Maipo', 'SHAC-01', 'No', 0, 1, 0, 'Vigente', 1);
INSERT INTO FUENTE (FK_ID_SUBCUENCA, Nombre, Tipo) VALUES (1, 'Río Maipo', 'Superficial');
INSERT INTO DDAA (FK_ID_COMUNA, FK_ID_RUT_TITULAR, FK_ID_INSTALACION, FK_ID_FUENTE, NombreFuenteDerecho, Naturaleza_derecho, Tipo_Derecho, EstadoDerecho)
VALUES ('13101', 76012345, 1, 1, 'Río Maipo', 'Consuntivo', 'Aprovechamiento', 'Vigente');
INSERT INTO DDAA_EXPEDIENTE (codigo, tipo, estado, resolucion_dga_n, resolucion_dga_fecha, resolucion_dga_link, catastro_n, catastro_fecha, catastro_link)
VALUES ('EXP-001', 'Constitución', 'En revisión', 'DGA-2024-01', '2024-05-12', 'https://example.com/dga-001', 101, '2024-05-15', 'https://example.com/catastro-101');
INSERT INTO DDAA_DDAA_EXPEDIENTE (id_ddaa, id_expediente) VALUES (1, 1);
INSERT INTO DDAA_INSCRIPCION (fk_id_ddaa_expediente, cbr, cbr_fojas, cbr_numero, cbr_fecha, cbr_link)
VALUES (1, 'Santiago', '123', 456, '2024-06-01', 'https://example.com/cbr-456');
INSERT INTO DDAA_PLAZO (Clase, caudal_ls_min, caudal_ls_max, Resol_Extrac_Efect, Link_Resol_Extrac_efect, Fecha_Resol_Extrac_efect, Plazo_Med_Reg, Plazo_Transmision, Fecha_medicion_reg, Fecha_transmision)
VALUES ('Base', 1.0000, 50.0000, 'RES-PL-01', 'https://example.com/plazo-01', '2024-04-30', 'Medir y registrar mensualmente', 'Transmitir mensualmente', '2024-05-01', '2024-05-15');
INSERT INTO DDAA_OBRA (FK_ID_RUT_PROVEEDOR, FK_ID_DDAA_PLAZO, TipoObra, EstadoObra, Fecha_Sol_Obra, Carpeta_Solicitud, CoordenadaObra, ResolucionObra, LinkResolucionObra, Con_Instrumento, CodigoObraDGA, LinkQR, ReportaDGA)
VALUES (76054321, 1, 'Captación', 1, '2024-05-20', '/carpetas/obra-01', '-33.45,-70.66', 'OB-2024-01', 'https://example.com/obra-01', 1, 'DGA-OB-001', 'https://example.com/qr-001', 1);
INSERT INTO DDAA_EJERCICIO (FK_ID_DDAA, EjercicioDerecho, ContinuidadDerecho) VALUES (1, 'Ejercicio normal', 'Continuidad anual');
INSERT INTO DDAA_EJERCICIO_OBRA (FK_ID_EJERCICIO, FK_ID_OBRA) VALUES (1, 1);
INSERT INTO DDAA_CAUDAL (FK_ID_EJERCICIO, Mes, CaudalMensual) VALUES (1, 1, 10.5000);
INSERT INTO DDAA_CAUDAL (FK_ID_EJERCICIO, Mes, CaudalMensual) VALUES (1, 2, 11.2500);
INSERT INTO DDAA_CAUDAL_ECOLOGICO (FK_ID_EJERCICIO, mes, caudalEcologico) VALUES (1, 1, 2.1000);
INSERT INTO DDAA_CAUDAL_ECOLOGICO (FK_ID_EJERCICIO, mes, caudalEcologico) VALUES (1, 2, 2.2500);
INSERT INTO DDAA_PAGO_NO_USO (folio_tgr, FK_ID_DDAA, fecha_cobro, caudal_aplicado_ls, factor_aplicado, patente_utm, patente_clp)
VALUES (1001, 1, '2024-07-01', 10.5000, 2, 1.5000, 90000.00);