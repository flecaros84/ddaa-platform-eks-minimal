package com.ddaa.ddaaservice.service;

import com.ddaa.ddaaservice.dto.CatalogDtos.CuencaDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.FuenteDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.ComunaDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.InstalacionDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.RutDto;
import com.ddaa.ddaaservice.dto.CatalogDtos.SubcuencaDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaCreateDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaDetailDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaSummaryDto;
import com.ddaa.ddaaservice.dto.DdaaDtos.DdaaUpdateDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.EjercicioDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.ExpedienteDto;
import com.ddaa.ddaaservice.dto.DocumentDtos.PagoNoUsoDto;
import com.ddaa.ddaaservice.model.Ddaa;
import com.ddaa.ddaaservice.model.DdaaEjercicio;
import com.ddaa.ddaaservice.repository.ComunaRepository;
import com.ddaa.ddaaservice.repository.DdaaCaudalEcologicoRepository;
import com.ddaa.ddaaservice.repository.DdaaCaudalRepository;
import com.ddaa.ddaaservice.repository.DdaaEjercicioRepository;
import com.ddaa.ddaaservice.repository.DdaaPagoNoUsoRepository;
import com.ddaa.ddaaservice.repository.DdaaQueryRepository;
import com.ddaa.ddaaservice.repository.DdaaRepository;
import com.ddaa.ddaaservice.repository.FuenteRepository;
import com.ddaa.ddaaservice.repository.InstalacionRepository;
import com.ddaa.ddaaservice.repository.RutRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
public class DdaaQueryService {

    private final DdaaQueryRepository queryRepository;
    private final DdaaRepository ddaaRepository;
    private final ComunaRepository comunaRepository;
    private final RutRepository rutRepository;
    private final InstalacionRepository instalacionRepository;
    private final FuenteRepository fuenteRepository;
    private final DdaaPagoNoUsoRepository pagoNoUsoRepository;
    private final DdaaEjercicioRepository ejercicioRepository;
    private final DdaaCaudalRepository caudalRepository;
    private final DdaaCaudalEcologicoRepository caudalEcologicoRepository;

    public DdaaQueryService(DdaaQueryRepository queryRepository, DdaaRepository ddaaRepository,
                            ComunaRepository comunaRepository, RutRepository rutRepository,
                            InstalacionRepository instalacionRepository, FuenteRepository fuenteRepository,
                            DdaaPagoNoUsoRepository pagoNoUsoRepository,
                            DdaaEjercicioRepository ejercicioRepository,
                            DdaaCaudalRepository caudalRepository,
                            DdaaCaudalEcologicoRepository caudalEcologicoRepository) {
        this.queryRepository = queryRepository;
        this.ddaaRepository = ddaaRepository;
        this.comunaRepository = comunaRepository;
        this.rutRepository = rutRepository;
        this.instalacionRepository = instalacionRepository;
        this.fuenteRepository = fuenteRepository;
        this.pagoNoUsoRepository = pagoNoUsoRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.caudalRepository = caudalRepository;
        this.caudalEcologicoRepository = caudalEcologicoRepository;
    }

    public List<DdaaSummaryDto> findAllDdaa() {
        return queryRepository.findAllDdaa();
    }

    public DdaaDetailDto findDdaaDetail(long id) {
        DdaaSummaryDto ddaa = queryRepository.findDdaaById(id)
                .orElseThrow(() -> new IllegalArgumentException("No se encontro el derecho de agua " + id));

        List<ExpedienteDto> expedientes = queryRepository.findExpedientesByDdaaId(id);
        List<PagoNoUsoDto> pagosNoUso = queryRepository.findPagosNoUsoByDdaaId(id);
        List<EjercicioDto> ejercicios = queryRepository.findEjerciciosByDdaaId(id);

        return new DdaaDetailDto(ddaa, expedientes, pagosNoUso, ejercicios);
    }

    public List<ExpedienteDto> findExpedientesByDdaaId(long ddaaId) {
        return queryRepository.findExpedientesByDdaaId(ddaaId);
    }

    public List<CuencaDto> findCuencas() {
        return queryRepository.findCuencas();
    }

    public List<SubcuencaDto> findSubcuencas() {
        return queryRepository.findSubcuencas();
    }

    public List<FuenteDto> findFuentes() {
        return queryRepository.findFuentes();
    }

    public List<ComunaDto> findComunas() {
        return comunaRepository.findAll(Sort.by("nombre")).stream()
                .map(comuna -> new ComunaDto(comuna.getId(), comuna.getNombre()))
                .toList();
    }

    public List<RutDto> findRuts() {
        return rutRepository.findAll(Sort.by("nombre")).stream()
                .map(rut -> new RutDto(rut.getRut(), rut.getNombre()))
                .toList();
    }

    public List<InstalacionDto> findInstalaciones() {
        return instalacionRepository.findAll(Sort.by("nombre")).stream()
                .map(instalacion -> new InstalacionDto(instalacion.getId().longValue(), instalacion.getNombre()))
                .toList();
    }

    @Transactional
    public long createDdaa(DdaaCreateDto dto) {
        Ddaa ddaa = new Ddaa();
        applyDdaaFields(ddaa, dto.comunaId(), dto.rutTitular(), dto.instalacionId(), dto.fuenteId(),
                dto.nombreFuenteDerecho(), dto.naturalezaDerecho(), dto.tipoDerecho(), dto.estadoDerecho());
        return ddaaRepository.save(ddaa).getId();
    }

    @Transactional
    public int updateDdaa(long id, DdaaUpdateDto dto) {
        return ddaaRepository.findById(toIntegerId(id))
                .map(ddaa -> {
                    applyDdaaFields(ddaa, dto.comunaId(), dto.rutTitular(), dto.instalacionId(), dto.fuenteId(),
                            dto.nombreFuenteDerecho(), dto.naturalezaDerecho(), dto.tipoDerecho(), dto.estadoDerecho());
                    ddaaRepository.save(ddaa);
                    return 1;
                })
                .orElse(0);
    }

    @Transactional
    public int deleteDdaa(long id) {
        Integer entityId = toIntegerId(id);
        if (!ddaaRepository.existsById(entityId)) {
            return 0;
        }
        Ddaa ddaa = ddaaRepository.getReferenceById(entityId);
        ddaa.getExpedientes().clear();
        ddaaRepository.save(ddaa);

        pagoNoUsoRepository.deleteByDdaa_Id(entityId);
        for (DdaaEjercicio ejercicio : ejercicioRepository.findByDdaa_Id(entityId)) {
            caudalRepository.deleteByEjercicio_Id(ejercicio.getId());
            caudalEcologicoRepository.deleteByEjercicio_Id(ejercicio.getId());
        }
        ejercicioRepository.deleteByDdaa_Id(entityId);
        ddaaRepository.delete(ddaa);
        return 1;
    }

    private void applyDdaaFields(Ddaa ddaa, String comunaId, Long rutTitular, Long instalacionId, Long fuenteId,
                                 String nombreFuenteDerecho, String naturalezaDerecho, String tipoDerecho,
                                 String estadoDerecho) {
        ddaa.setComuna(comunaRepository.getReferenceById(comunaId));
        ddaa.setTitular(rutRepository.getReferenceById(rutTitular));
        ddaa.setInstalacion(instalacionId != null ? instalacionRepository.getReferenceById(toIntegerId(instalacionId)) : null);
        ddaa.setFuente(fuenteRepository.getReferenceById(toIntegerId(fuenteId)));
        ddaa.setNombreFuenteDerecho(nombreFuenteDerecho);
        ddaa.setNaturalezaDerecho(naturalezaDerecho);
        ddaa.setTipoDerecho(tipoDerecho);
        ddaa.setEstadoDerecho(estadoDerecho);
    }

    private Integer toIntegerId(Long id) {
        if (id == null) {
            return null;
        }
        return Math.toIntExact(id);
    }

    private Integer toIntegerId(long id) {
        return Math.toIntExact(id);
    }
}
