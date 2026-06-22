package com.ddaa.ddaaservice.repository;

import com.ddaa.ddaaservice.dto.CatalogDtos.CuencaDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.FuenteDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.SubcuencaDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaSummaryDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.CaudalDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.CaudalEcologicoDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.EjercicioDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.ExpedienteDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.ObraDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.PagoNoUsoDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.PlazoDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DdaaQueryRepository {

    private final JdbcTemplate jdbcTemplate;

    public DdaaQueryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DdaaSummaryDto> findAllDdaa() {
        return jdbcTemplate.query(DDAA_SUMMARY_SQL, this::mapSummary);
    }

    public Optional<DdaaSummaryDto> findDdaaById(long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(DDAA_SUMMARY_SQL + " WHERE d.ID_ddaa = ?", this::mapSummary, id));
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<ExpedienteDto> findExpedientesByDdaaId(long ddaaId) {
        return jdbcTemplate.query(EXPEDIENTES_BY_DDAA_SQL, this::mapExpediente, ddaaId);
    }

    public List<PagoNoUsoDto> findPagosNoUsoByDdaaId(long ddaaId) {
        return jdbcTemplate.query(PAGOS_NO_USO_SQL, this::mapPagoNoUso, ddaaId);
    }

    public List<EjercicioDto> findEjerciciosByDdaaId(long ddaaId) {
        List<EjercicioRow> exercises = jdbcTemplate.query(EJERCICIOS_BY_DDAA_SQL, (rs, rowNum) -> new EjercicioRow(
                rs.getLong("id"),
                rs.getLong("ddaaId"),
                rs.getString("ejercicioDerecho"),
                rs.getString("continuidadDerecho")
        ), ddaaId);

        List<EjercicioDto> result = new ArrayList<>();
        for (EjercicioRow exercise : exercises) {
            List<CaudalDto> caudales = jdbcTemplate.query(CAUDALES_BY_EJERCICIO_SQL, this::mapCaudal, exercise.id());
            List<CaudalEcologicoDto> caudalesEcologicos = jdbcTemplate.query(CAUDALES_ECOLOGICOS_BY_EJERCICIO_SQL, this::mapCaudalEcologico, exercise.id());
            List<ObraDto> obras = jdbcTemplate.query(OBRAS_BY_EJERCICIO_SQL, this::mapObra, exercise.id());
            result.add(new EjercicioDto(exercise.id(), exercise.ddaaId(), exercise.ejercicioDerecho(), exercise.continuidadDerecho(), caudales, caudalesEcologicos, obras));
        }
        return result;
    }

    public List<CuencaDto> findCuencas() {
        List<CuencaRow> rows = jdbcTemplate.query(CUENCAS_SQL, (rs, rowNum) -> new CuencaRow(
                rs.getLong("id"),
                rs.getString("nombre")
        ));

        Map<Long, List<SubcuencaDto>> subcuencasByCuenca = new HashMap<>();
        for (SubcuencaDto subcuenca : findSubcuencas()) {
            subcuencasByCuenca.computeIfAbsent(subcuenca.cuencaId(), key -> new ArrayList<>()).add(subcuenca);
        }

        List<CuencaDto> result = new ArrayList<>();
        for (CuencaRow row : rows) {
            result.add(new CuencaDto(row.id(), row.nombre(), subcuencasByCuenca.getOrDefault(row.id(), List.of())));
        }
        return result;
    }

    public List<SubcuencaDto> findSubcuencas() {
        return jdbcTemplate.query(SUBCUENCAS_SQL, this::mapSubcuenca);
    }

    public List<FuenteDto> findFuentes() {
        return jdbcTemplate.query(FUENTES_SQL, this::mapFuente);
    }

    private DdaaSummaryDto mapSummary(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new DdaaSummaryDto(
                rs.getLong("id"),
                rs.getString("comunaId"),
                rs.getString("comunaNombre"),
                rs.getLong("rutTitular"),
                rs.getString("titularNombre"),
                nullableLong(rs, "instalacionId"),
                rs.getString("instalacionNombre"),
                rs.getLong("fuenteId"),
                rs.getString("fuenteNombre"),
                rs.getString("fuenteTipo"),
                rs.getString("nombreFuenteDerecho"),
                rs.getString("naturalezaDerecho"),
                rs.getString("tipoDerecho"),
                rs.getString("estadoDerecho"),
                nullableLong(rs, "cuencaId"),
                rs.getString("cuencaNombre"),
                nullableLong(rs, "subcuencaId"),
                rs.getString("subcuencaNombre")
        );
    }

    private ExpedienteDto mapExpediente(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new ExpedienteDto(
                rs.getLong("id"),
                rs.getString("codigo"),
                rs.getString("tipo"),
                rs.getString("estado"),
                rs.getString("resolucionDgaNumero"),
                rs.getDate("resolucionDgaFecha") != null ? rs.getDate("resolucionDgaFecha").toLocalDate() : null,
                rs.getString("resolucionDgaLink"),
                nullableInteger(rs, "catastroNumero"),
                rs.getDate("catastroFecha") != null ? rs.getDate("catastroFecha").toLocalDate() : null,
                rs.getString("catastroLink")
        );
    }

    private PagoNoUsoDto mapPagoNoUso(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new PagoNoUsoDto(
                rs.getLong("folioTgr"),
                rs.getLong("ddaaId"),
                rs.getDate("fechaCobro") != null ? rs.getDate("fechaCobro").toLocalDate() : null,
                rs.getBigDecimal("caudalAplicadoLs"),
                nullableInteger(rs, "factorAplicado"),
                rs.getBigDecimal("patenteUtm"),
                rs.getBigDecimal("patenteClp")
        );
    }

    private CaudalDto mapCaudal(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new CaudalDto(
                rs.getLong("id"),
                rs.getLong("ejercicioId"),
                rs.getInt("mes"),
                rs.getBigDecimal("caudalMensual")
        );
    }

    private CaudalEcologicoDto mapCaudalEcologico(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new CaudalEcologicoDto(
                rs.getLong("id"),
                rs.getLong("ejercicioId"),
                rs.getInt("mes"),
                rs.getBigDecimal("caudalEcologico")
        );
    }

    private ObraDto mapObra(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        Long plazoId = nullableLong(rs, "plazoId");
        PlazoDto plazo = null;
        if (plazoId != null) {
            plazo = new PlazoDto(
                    plazoId,
                    rs.getString("clase"),
                    rs.getBigDecimal("caudalLsMin"),
                    rs.getBigDecimal("caudalLsMax"),
                    rs.getString("resolucionExtracEfect"),
                    rs.getString("linkResolucionExtracEfect"),
                    rs.getDate("fechaResolucionExtracEfect") != null ? rs.getDate("fechaResolucionExtracEfect").toLocalDate() : null,
                    rs.getString("plazoMedReg"),
                    rs.getString("plazoTransmision"),
                    rs.getDate("fechaMedicionReg") != null ? rs.getDate("fechaMedicionReg").toLocalDate() : null,
                    rs.getDate("fechaTransmision") != null ? rs.getDate("fechaTransmision").toLocalDate() : null
            );
        }

        return new ObraDto(
                rs.getLong("id"),
                nullableLong(rs, "rutProveedor"),
                plazoId,
                rs.getString("tipoObra"),
                nullableBoolean(rs, "estadoObra"),
                rs.getDate("fechaSolicitudObra") != null ? rs.getDate("fechaSolicitudObra").toLocalDate() : null,
                rs.getString("carpetaSolicitud"),
                rs.getString("coordenadaObra"),
                rs.getString("resolucionObra"),
                rs.getString("linkResolucionObra"),
                nullableBoolean(rs, "conInstrumento"),
                rs.getString("codigoObraDga"),
                rs.getString("linkQr"),
                nullableBoolean(rs, "reportaDga"),
                plazo
        );
    }

    private SubcuencaDto mapSubcuenca(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new SubcuencaDto(
                rs.getLong("id"),
                rs.getLong("cuencaId"),
                rs.getString("cuencaNombre"),
                rs.getString("nombre"),
                rs.getString("shac"),
                rs.getString("reserva"),
                nullableBoolean(rs, "declaracionAgotamiento"),
                nullableBoolean(rs, "zonaRestriccionSuperficial"),
                nullableBoolean(rs, "zonaRestriccionSubterranea"),
                rs.getString("estadoShac"),
                nullableBoolean(rs, "planGestionHidrica")
        );
    }

    private FuenteDto mapFuente(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
        return new FuenteDto(
                rs.getLong("id"),
                nullableLong(rs, "subcuencaId"),
                rs.getString("subcuencaNombre"),
                rs.getString("nombre"),
                rs.getString("tipo")
        );
    }

    private Long nullableLong(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        Number value = (Number) rs.getObject(column);
        return value != null ? value.longValue() : null;
    }

    private Integer nullableInteger(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        Number value = (Number) rs.getObject(column);
        return value != null ? value.intValue() : null;
    }

    private Boolean nullableBoolean(java.sql.ResultSet rs, String column) throws java.sql.SQLException {
        boolean value = rs.getBoolean(column);
        return rs.wasNull() ? null : value;
    }

    private record EjercicioRow(Long id, Long ddaaId, String ejercicioDerecho, String continuidadDerecho) {
    }

    private record CuencaRow(Long id, String nombre) {
    }

    private static final String DDAA_SUMMARY_SQL = """
            SELECT d.ID_ddaa AS id,
                   d.FK_ID_COMUNA AS comunaId,
                   c.Nombre AS comunaNombre,
                   d.FK_ID_RUT_TITULAR AS rutTitular,
                   r.Nombre AS titularNombre,
                   d.FK_ID_INSTALACION AS instalacionId,
                   i.Nombre AS instalacionNombre,
                   d.FK_ID_FUENTE AS fuenteId,
                   f.Nombre AS fuenteNombre,
                   f.Tipo AS fuenteTipo,
                   d.NombreFuenteDerecho AS nombreFuenteDerecho,
                   d.Naturaleza_derecho AS naturalezaDerecho,
                   d.Tipo_Derecho AS tipoDerecho,
                   d.EstadoDerecho AS estadoDerecho,
                   cu.ID_Cuenca AS cuencaId,
                   cu.Nombre AS cuencaNombre,
                   s.ID_Subcuenca AS subcuencaId,
                   s.NombreSubcuenca AS subcuencaNombre
            FROM DDAA d
            INNER JOIN COMUNA c ON c.ID_Comuna = d.FK_ID_COMUNA
            INNER JOIN RUTS r ON r.Rut = d.FK_ID_RUT_TITULAR
            LEFT JOIN INSTALACION i ON i.ID_Instalacion = d.FK_ID_INSTALACION
            INNER JOIN FUENTE f ON f.ID_Fuente = d.FK_ID_FUENTE
            LEFT JOIN SUBCUENCA s ON s.ID_Subcuenca = f.FK_ID_SUBCUENCA
            LEFT JOIN CUENCA cu ON cu.ID_Cuenca = s.FK_ID_CUENCA
            """;

    private static final String EXPEDIENTES_BY_DDAA_SQL = """
            SELECT e.id AS id,
                   e.codigo,
                   e.tipo,
                   e.estado,
                   e.resolucion_dga_n AS resolucionDgaNumero,
                   e.resolucion_dga_fecha AS resolucionDgaFecha,
                   e.resolucion_dga_link AS resolucionDgaLink,
                   e.catastro_n AS catastroNumero,
                   e.catastro_fecha AS catastroFecha,
                   e.catastro_link AS catastroLink
            FROM DDAA_DDAA_EXPEDIENTE rel
            INNER JOIN DDAA_EXPEDIENTE e ON e.id = rel.id_expediente
            WHERE rel.id_ddaa = ?
            ORDER BY e.id
            """;

    private static final String PAGOS_NO_USO_SQL = """
            SELECT p.folio_tgr AS folioTgr,
                   p.FK_ID_DDAA AS ddaaId,
                   p.fecha_cobro AS fechaCobro,
                   p.caudal_aplicado_ls AS caudalAplicadoLs,
                   p.factor_aplicado AS factorAplicado,
                   p.patente_utm AS patenteUtm,
                   p.patente_clp AS patenteClp
            FROM DDAA_PAGO_NO_USO p
            WHERE p.FK_ID_DDAA = ?
            ORDER BY p.fecha_cobro DESC, p.folio_tgr DESC
            """;

    private static final String EJERCICIOS_BY_DDAA_SQL = """
            SELECT e.ID_Ejercicio AS id,
                   e.FK_ID_DDAA AS ddaaId,
                   e.EjercicioDerecho AS ejercicioDerecho,
                   e.ContinuidadDerecho AS continuidadDerecho
            FROM DDAA_EJERCICIO e
            WHERE e.FK_ID_DDAA = ?
            ORDER BY e.ID_Ejercicio
            """;

    private static final String CAUDALES_BY_EJERCICIO_SQL = """
            SELECT c.ID_Caudal AS id,
                   c.FK_ID_EJERCICIO AS ejercicioId,
                   c.Mes AS mes,
                   c.CaudalMensual AS caudalMensual
            FROM DDAA_CAUDAL c
            WHERE c.FK_ID_EJERCICIO = ?
            ORDER BY c.Mes
            """;

    private static final String CAUDALES_ECOLOGICOS_BY_EJERCICIO_SQL = """
            SELECT c.id_caudal_ecologico AS id,
                   c.FK_ID_EJERCICIO AS ejercicioId,
                   c.mes AS mes,
                   c.caudalEcologico AS caudalEcologico
            FROM DDAA_CAUDAL_ECOLOGICO c
            WHERE c.FK_ID_EJERCICIO = ?
            ORDER BY c.mes
            """;

    private static final String OBRAS_BY_EJERCICIO_SQL = """
            SELECT o.id AS id,
                   o.FK_ID_RUT_PROVEEDOR AS rutProveedor,
                   o.TipoObra AS tipoObra,
                   o.EstadoObra AS estadoObra,
                   o.Fecha_Sol_Obra AS fechaSolicitudObra,
                   o.Carpeta_Solicitud AS carpetaSolicitud,
                   o.CoordenadaObra AS coordenadaObra,
                   o.ResolucionObra AS resolucionObra,
                   o.LinkResolucionObra AS linkResolucionObra,
                   o.Con_Instrumento AS conInstrumento,
                   o.CodigoObraDGA AS codigoObraDga,
                   o.LinkQR AS linkQr,
                   o.ReportaDGA AS reportaDga,
                   p.ID_Plazo AS plazoId,
                   p.Clase AS clase,
                   p.caudal_ls_min AS caudalLsMin,
                   p.caudal_ls_max AS caudalLsMax,
                   p.Resol_Extrac_Efect AS resolucionExtracEfect,
                   p.Link_Resol_Extrac_efect AS linkResolucionExtracEfect,
                   p.Fecha_Resol_Extrac_efect AS fechaResolucionExtracEfect,
                   p.Plazo_Med_Reg AS plazoMedReg,
                   p.Plazo_Transmision AS plazoTransmision,
                   p.Fecha_medicion_reg AS fechaMedicionReg,
                   p.Fecha_transmision AS fechaTransmision
            FROM DDAA_EJERCICIO_OBRA eo
            INNER JOIN DDAA_OBRA o ON o.id = eo.FK_ID_OBRA
            LEFT JOIN DDAA_PLAZO p ON p.ID_Plazo = o.FK_ID_DDAA_PLAZO
            WHERE eo.FK_ID_EJERCICIO = ?
            ORDER BY o.id
            """;

    private static final String CUENCAS_SQL = """
            SELECT ID_Cuenca AS id, Nombre AS nombre
            FROM CUENCA
            ORDER BY Nombre
            """;

    private static final String SUBCUENCAS_SQL = """
            SELECT s.ID_Subcuenca AS id,
                   s.FK_ID_CUENCA AS cuencaId,
                   c.Nombre AS cuencaNombre,
                   s.NombreSubcuenca AS nombre,
                   s.SHAC AS shac,
                   s.Reserva AS reserva,
                   s.DeclaracionAgotamiento AS declaracionAgotamiento,
                   s.ZonaRestriccionSuperficial AS zonaRestriccionSuperficial,
                   s.ZonaRestriccionSubterranea AS zonaRestriccionSubterranea,
                   s.EstadoSHAC AS estadoShac,
                   s.PlanGestionHidrica AS planGestionHidrica
            FROM SUBCUENCA s
            INNER JOIN CUENCA c ON c.ID_Cuenca = s.FK_ID_CUENCA
            ORDER BY c.Nombre, s.NombreSubcuenca
            """;

    private static final String FUENTES_SQL = """
            SELECT f.ID_Fuente AS id,
                   f.FK_ID_SUBCUENCA AS subcuencaId,
                   s.NombreSubcuenca AS subcuencaNombre,
                   f.Nombre AS nombre,
                   f.Tipo AS tipo
            FROM FUENTE f
            LEFT JOIN SUBCUENCA s ON s.ID_Subcuenca = f.FK_ID_SUBCUENCA
            ORDER BY f.Nombre
            """;
}
