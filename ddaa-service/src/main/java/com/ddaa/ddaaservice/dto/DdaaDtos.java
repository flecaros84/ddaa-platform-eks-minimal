package com.ddaa.ddaaservice.dto;

import java.util.List;

public final class DdaaDtos {

    private DdaaDtos() {
    }

    public record DdaaSummaryDto(Long id, String comunaId, String comunaNombre, Long rutTitular, String titularNombre,
                                 Long instalacionId, String instalacionNombre, Long fuenteId, String fuenteNombre,
                                 String fuenteTipo, String nombreFuenteDerecho, String naturalezaDerecho,
                                 String tipoDerecho, String estadoDerecho, Long cuencaId, String cuencaNombre,
                                 Long subcuencaId, String subcuencaNombre) {
    }

    public record DdaaDetailDto(DdaaSummaryDto ddaa, List<DocumentDtos.ExpedienteDto> expedientes,
                                List<DocumentDtos.PagoNoUsoDto> pagosNoUso,
                                List<DocumentDtos.EjercicioDto> ejercicios) {
    }

    public record DdaaCreateDto(String comunaId, Long rutTitular, Long instalacionId, Long fuenteId,
                                String nombreFuenteDerecho, String naturalezaDerecho, String tipoDerecho,
                                String estadoDerecho) {
    }

    public record DdaaUpdateDto(String comunaId, Long rutTitular, Long instalacionId, Long fuenteId,
                                String nombreFuenteDerecho, String naturalezaDerecho, String tipoDerecho,
                                String estadoDerecho) {
    }
}