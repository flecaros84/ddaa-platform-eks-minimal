package com.ddaa.ddaaservice.config;

import com.ddaa.ddaaservice.model.Comuna;
import com.ddaa.ddaaservice.model.Cuenca;
import com.ddaa.ddaaservice.model.Ddaa;
import com.ddaa.ddaaservice.model.DdaaCaudal;
import com.ddaa.ddaaservice.model.DdaaEjercicio;
import com.ddaa.ddaaservice.model.DdaaExpediente;
import com.ddaa.ddaaservice.model.DdaaPagoNoUso;
import com.ddaa.ddaaservice.model.Fuente;
import com.ddaa.ddaaservice.model.Instalacion;
import com.ddaa.ddaaservice.model.Rut;
import com.ddaa.ddaaservice.model.Subcuenca;
import com.ddaa.ddaaservice.repository.ComunaRepository;
import com.ddaa.ddaaservice.repository.CuencaRepository;
import com.ddaa.ddaaservice.repository.DdaaCaudalRepository;
import com.ddaa.ddaaservice.repository.DdaaEjercicioRepository;
import com.ddaa.ddaaservice.repository.DdaaExpedienteRepository;
import com.ddaa.ddaaservice.repository.DdaaPagoNoUsoRepository;
import com.ddaa.ddaaservice.repository.DdaaRepository;
import com.ddaa.ddaaservice.repository.FuenteRepository;
import com.ddaa.ddaaservice.repository.InstalacionRepository;
import com.ddaa.ddaaservice.repository.RutRepository;
import com.ddaa.ddaaservice.repository.SubcuencaRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@ConditionalOnProperty(prefix = "app.sample-data", name = "enabled", havingValue = "true", matchIfMissing = true)
public class DdaaSampleDataLoader implements ApplicationRunner {

    private final ComunaRepository comunaRepository;
    private final RutRepository rutRepository;
    private final InstalacionRepository instalacionRepository;
    private final CuencaRepository cuencaRepository;
    private final SubcuencaRepository subcuencaRepository;
    private final FuenteRepository fuenteRepository;
    private final DdaaRepository ddaaRepository;
    private final DdaaExpedienteRepository expedienteRepository;
    private final DdaaEjercicioRepository ejercicioRepository;
    private final DdaaCaudalRepository caudalRepository;
    private final DdaaPagoNoUsoRepository pagoNoUsoRepository;

    public DdaaSampleDataLoader(ComunaRepository comunaRepository, RutRepository rutRepository,
                                InstalacionRepository instalacionRepository, CuencaRepository cuencaRepository,
                                SubcuencaRepository subcuencaRepository, FuenteRepository fuenteRepository,
                                DdaaRepository ddaaRepository, DdaaExpedienteRepository expedienteRepository,
                                DdaaEjercicioRepository ejercicioRepository, DdaaCaudalRepository caudalRepository,
                                DdaaPagoNoUsoRepository pagoNoUsoRepository) {
        this.comunaRepository = comunaRepository;
        this.rutRepository = rutRepository;
        this.instalacionRepository = instalacionRepository;
        this.cuencaRepository = cuencaRepository;
        this.subcuencaRepository = subcuencaRepository;
        this.fuenteRepository = fuenteRepository;
        this.ddaaRepository = ddaaRepository;
        this.expedienteRepository = expedienteRepository;
        this.ejercicioRepository = ejercicioRepository;
        this.caudalRepository = caudalRepository;
        this.pagoNoUsoRepository = pagoNoUsoRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        Comuna puertoMontt = ensureComuna("10101", "Puerto Montt");
        Comuna calbuco = ensureComuna("10102", "Calbuco");
        Comuna castro = ensureComuna("10201", "Castro");

        Rut camanchaca = ensureRut(76123456L, "Camanchaca Cultivos Sur S.A.");
        Rut aguasSur = ensureRut(76789012L, "Aguas del Sur SpA");
        Rut marinaAustral = ensureRut(76987654L, "Marina Austral Ltda.");

        Instalacion plantaPuertoMontt = ensureInstalacion("Planta Puerto Montt");
        Instalacion centroCalbuco = ensureInstalacion("Centro Calbuco");
        Instalacion centroChiloe = ensureInstalacion("Centro Chiloe");

        Cuenca maullin = ensureCuenca("Cuenca Rio Maullin");
        Cuenca chiloe = ensureCuenca("Cuencas e islas de Chiloe");

        Subcuenca lagoLlanquihue = ensureSubcuenca(maullin, "Lago Llanquihue", "SHAC-LLA-01");
        Subcuenca senoReloncavi = ensureSubcuenca(maullin, "Seno de Reloncavi", "SHAC-REL-02");
        Subcuenca esteroCastro = ensureSubcuenca(chiloe, "Estero Castro", "SHAC-CAS-03");

        Fuente rioMaullin = ensureFuente(lagoLlanquihue, "Rio Maullin", "Superficial");
        Fuente pozoCalbuco = ensureFuente(senoReloncavi, "Pozo Calbuco Norte", "Subterranea");
        Fuente esteroGamboa = ensureFuente(esteroCastro, "Estero Gamboa", "Superficial");

        if (ddaaRepository.count() > 0) {
            return;
        }

        Ddaa ddaa1 = createDdaa(puertoMontt, camanchaca, plantaPuertoMontt, rioMaullin,
                "Bocatoma Rio Maullin", "Consuntivo", "Aprovechamiento", "Vigente");
        Ddaa ddaa2 = createDdaa(calbuco, aguasSur, centroCalbuco, pozoCalbuco,
                "Pozo profundo Calbuco Norte", "Consuntivo", "Exploracion", "En revision");
        Ddaa ddaa3 = createDdaa(castro, marinaAustral, centroChiloe, esteroGamboa,
                "Captacion Estero Gamboa", "No consuntivo", "Aprovechamiento", "Vigente");

        createDetail(ddaa1, "EXP-MAU-001", "Constitucion", "Aprobado", 21001, 1001, "Ejercicio permanente", "Continuo");
        createDetail(ddaa2, "EXP-CAL-002", "Regularizacion", "En tramite", 21002, 1002, "Ejercicio provisional", "Discontinuo");
        createDetail(ddaa3, "EXP-CAS-003", "Traslado", "Aprobado", 21003, 1003, "Ejercicio estacional", "Continuo");
    }

    private Comuna ensureComuna(String id, String nombre) {
        return comunaRepository.findById(id).orElseGet(() -> {
            Comuna comuna = new Comuna();
            comuna.setId(id);
            comuna.setNombre(nombre);
            return comunaRepository.save(comuna);
        });
    }

    private Rut ensureRut(Long rutValue, String nombre) {
        return rutRepository.findById(rutValue).orElseGet(() -> {
            Rut rut = new Rut();
            rut.setRut(rutValue);
            rut.setNombre(nombre);
            return rutRepository.save(rut);
        });
    }

    private Instalacion ensureInstalacion(String nombre) {
        return instalacionRepository.findAll().stream()
                .filter(instalacion -> nombre.equalsIgnoreCase(instalacion.getNombre()))
                .findFirst()
                .orElseGet(() -> {
                    Instalacion instalacion = new Instalacion();
                    instalacion.setNombre(nombre);
                    return instalacionRepository.save(instalacion);
                });
    }

    private Cuenca ensureCuenca(String nombre) {
        return cuencaRepository.findAll().stream()
                .filter(cuenca -> nombre.equalsIgnoreCase(cuenca.getNombre()))
                .findFirst()
                .orElseGet(() -> {
                    Cuenca cuenca = new Cuenca();
                    cuenca.setNombre(nombre);
                    return cuencaRepository.save(cuenca);
                });
    }

    private Subcuenca ensureSubcuenca(Cuenca cuenca, String nombre, String shac) {
        return subcuencaRepository.findAll().stream()
                .filter(subcuenca -> nombre.equalsIgnoreCase(subcuenca.getNombreSubcuenca()))
                .findFirst()
                .orElseGet(() -> {
                    Subcuenca subcuenca = new Subcuenca();
                    subcuenca.setCuenca(cuenca);
                    subcuenca.setNombreSubcuenca(nombre);
                    subcuenca.setShac(shac);
                    subcuenca.setReserva("No");
                    subcuenca.setDeclaracionAgotamiento(false);
                    subcuenca.setZonaRestriccionSuperficial(false);
                    subcuenca.setZonaRestriccionSubterranea(false);
                    subcuenca.setEstadoShac("Vigente");
                    subcuenca.setPlanGestionHidrica(true);
                    return subcuencaRepository.save(subcuenca);
                });
    }

    private Fuente ensureFuente(Subcuenca subcuenca, String nombre, String tipo) {
        return fuenteRepository.findAll().stream()
                .filter(fuente -> nombre.equalsIgnoreCase(fuente.getNombre()))
                .findFirst()
                .orElseGet(() -> {
                    Fuente fuente = new Fuente();
                    fuente.setSubcuenca(subcuenca);
                    fuente.setNombre(nombre);
                    fuente.setTipo(tipo);
                    return fuenteRepository.save(fuente);
                });
    }

    private Ddaa createDdaa(Comuna comuna, Rut titular, Instalacion instalacion, Fuente fuente,
                            String nombreFuenteDerecho, String naturaleza, String tipo, String estado) {
        Ddaa ddaa = new Ddaa();
        ddaa.setComuna(comuna);
        ddaa.setTitular(titular);
        ddaa.setInstalacion(instalacion);
        ddaa.setFuente(fuente);
        ddaa.setNombreFuenteDerecho(nombreFuenteDerecho);
        ddaa.setNaturalezaDerecho(naturaleza);
        ddaa.setTipoDerecho(tipo);
        ddaa.setEstadoDerecho(estado);
        return ddaaRepository.save(ddaa);
    }

    private void createDetail(Ddaa ddaa, String codigoExpediente, String tipoExpediente, String estadoExpediente,
                              Integer catastroNumero, Integer folioTgr, String ejercicioDerecho,
                              String continuidadDerecho) {
        DdaaExpediente expediente = new DdaaExpediente();
        expediente.setCodigo(codigoExpediente);
        expediente.setTipo(tipoExpediente);
        expediente.setEstado(estadoExpediente);
        expediente.setResolucionDgaNumero("DGA-" + codigoExpediente);
        expediente.setResolucionDgaFecha(LocalDate.of(2024, 5, 15));
        expediente.setResolucionDgaLink("https://example.com/" + codigoExpediente.toLowerCase());
        expediente.setCatastroNumero(catastroNumero);
        expediente.setCatastroFecha(LocalDate.of(2024, 6, 1));
        expediente.setCatastroLink("https://example.com/catastro-" + catastroNumero);
        expediente = expedienteRepository.save(expediente);

        ddaa.getExpedientes().add(expediente);
        ddaaRepository.save(ddaa);

        DdaaEjercicio ejercicio = new DdaaEjercicio();
        ejercicio.setDdaa(ddaa);
        ejercicio.setEjercicioDerecho(ejercicioDerecho);
        ejercicio.setContinuidadDerecho(continuidadDerecho);
        ejercicio = ejercicioRepository.save(ejercicio);

        DdaaCaudal caudal = new DdaaCaudal();
        caudal.setEjercicio(ejercicio);
        caudal.setMes(1);
        caudal.setCaudalMensual(new BigDecimal("12.5000"));
        caudalRepository.save(caudal);

        DdaaPagoNoUso pago = new DdaaPagoNoUso();
        pago.setFolioTgr(folioTgr);
        pago.setDdaa(ddaa);
        pago.setFechaCobro(LocalDate.of(2025, 1, 20));
        pago.setCaudalAplicadoLs(new BigDecimal("12.5000"));
        pago.setFactorAplicado(2);
        pago.setPatenteUtm(new BigDecimal("1.7500"));
        pago.setPatenteClp(new BigDecimal("115000.00"));
        pagoNoUsoRepository.save(pago);
    }
}
