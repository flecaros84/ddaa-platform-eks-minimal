package com.ddaa.ddaaservice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public final class DocumentDtos {

    private DocumentDtos() {
    }

    public record ExpedienteDto(Long id, String codigo, String tipo, String estado,
                                String resolucionDgaNumero, LocalDate resolucionDgaFecha, String resolucionDgaLink,
                                Integer catastroNumero, LocalDate catastroFecha, String catastroLink) {
    }

    public record PagoNoUsoDto(Long folioTgr, Long ddaaId, LocalDate fechaCobro, BigDecimal caudalAplicadoLs,
                               Integer factorAplicado, BigDecimal patenteUtm, BigDecimal patenteClp) {
    }

    public record EjercicioDto(Long id, Long ddaaId, String ejercicioDerecho, String continuidadDerecho,
                               List<CaudalDto> caudales, List<CaudalEcologicoDto> caudalesEcologicos,
                               List<ObraDto> obras) {
    }

    public record CaudalDto(Long id, Long ejercicioId, Integer mes, BigDecimal caudalMensual) {
    }

    public record CaudalEcologicoDto(Long id, Long ejercicioId, Integer mes, BigDecimal caudalEcologico) {
    }

    public record ObraDto(Long id, Long rutProveedor, Long plazoId, String tipoObra, Boolean estadoObra,
                          LocalDate fechaSolicitudObra, String carpetaSolicitud, String coordenadaObra,
                          String resolucionObra, String linkResolucionObra, Boolean conInstrumento,
                          String codigoObraDga, String linkQr, Boolean reportaDga, PlazoDto plazo) {
    }

    public record PlazoDto(Long id, String clase, BigDecimal caudalLsMin, BigDecimal caudalLsMax,
                           String resolucionExtracEfect, String linkResolucionExtracEfect,
                           LocalDate fechaResolucionExtracEfect, String plazoMedReg, String plazoTransmision,
                           LocalDate fechaMedicionReg, LocalDate fechaTransmision) {
    }
}