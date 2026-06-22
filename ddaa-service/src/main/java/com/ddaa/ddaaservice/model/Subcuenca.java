package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "SUBCUENCA", indexes = @Index(name = "IX_SUBCUENCA_FK_ID_CUENCA", columnList = "FK_ID_CUENCA"))
public class Subcuenca {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Subcuenca")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_CUENCA", nullable = false)
    private Cuenca cuenca;

    @Column(name = "NombreSubcuenca", nullable = false)
    private String nombreSubcuenca;

    @Column(name = "SHAC")
    private String shac;

    @Column(name = "Reserva")
    private String reserva;

    @Column(name = "DeclaracionAgotamiento")
    private Boolean declaracionAgotamiento;

    @Column(name = "ZonaRestriccionSuperficial")
    private Boolean zonaRestriccionSuperficial;

    @Column(name = "ZonaRestriccionSubterranea")
    private Boolean zonaRestriccionSubterranea;

    @Column(name = "EstadoSHAC")
    private String estadoShac;

    @Column(name = "PlanGestionHidrica")
    private Boolean planGestionHidrica;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Cuenca getCuenca() {
        return cuenca;
    }

    public void setCuenca(Cuenca cuenca) {
        this.cuenca = cuenca;
    }

    public String getNombreSubcuenca() {
        return nombreSubcuenca;
    }

    public void setNombreSubcuenca(String nombreSubcuenca) {
        this.nombreSubcuenca = nombreSubcuenca;
    }

    public String getShac() {
        return shac;
    }

    public void setShac(String shac) {
        this.shac = shac;
    }

    public String getReserva() {
        return reserva;
    }

    public void setReserva(String reserva) {
        this.reserva = reserva;
    }

    public Boolean getDeclaracionAgotamiento() {
        return declaracionAgotamiento;
    }

    public void setDeclaracionAgotamiento(Boolean declaracionAgotamiento) {
        this.declaracionAgotamiento = declaracionAgotamiento;
    }

    public Boolean getZonaRestriccionSuperficial() {
        return zonaRestriccionSuperficial;
    }

    public void setZonaRestriccionSuperficial(Boolean zonaRestriccionSuperficial) {
        this.zonaRestriccionSuperficial = zonaRestriccionSuperficial;
    }

    public Boolean getZonaRestriccionSubterranea() {
        return zonaRestriccionSubterranea;
    }

    public void setZonaRestriccionSubterranea(Boolean zonaRestriccionSubterranea) {
        this.zonaRestriccionSubterranea = zonaRestriccionSubterranea;
    }

    public String getEstadoShac() {
        return estadoShac;
    }

    public void setEstadoShac(String estadoShac) {
        this.estadoShac = estadoShac;
    }

    public Boolean getPlanGestionHidrica() {
        return planGestionHidrica;
    }

    public void setPlanGestionHidrica(Boolean planGestionHidrica) {
        this.planGestionHidrica = planGestionHidrica;
    }
}
