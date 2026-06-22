package com.ddaa.ddaaservice.dto;

import java.util.List;

public final class CatalogDtos {

    private CatalogDtos() {
    }

    public record CuencaDto(Long id, String nombre, List<SubcuencaDto> subcuencas) {
    }

    public record SubcuencaDto(Long id, Long cuencaId, String cuencaNombre, String nombre, String shac,
                               String reserva, Boolean declaracionAgotamiento,
                               Boolean zonaRestriccionSuperficial, Boolean zonaRestriccionSubterranea,
                               String estadoShac, Boolean planGestionHidrica) {
    }

    public record FuenteDto(Long id, Long subcuencaId, String subcuencaNombre, String nombre, String tipo) {
    }

    public record ComunaDto(String id, String nombre) {
    }

    public record RutDto(Long rut, String nombre) {
    }

    public record InstalacionDto(Long id, String nombre) {
    }
}
