package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DDAA_PLAZO")
public class DdaaPlazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Plazo")
    private Integer id;

    @Column(name = "Clase", nullable = false, length = 30)
    private String clase;

    @Column(name = "caudal_ls_min", precision = 18, scale = 4)
    private BigDecimal caudalLsMin;

    @Column(name = "caudal_ls_max", precision = 18, scale = 4)
    private BigDecimal caudalLsMax;

    @Column(name = "Resol_Extrac_Efect", length = 50)
    private String resolucionExtracEfect;

    @Column(name = "Link_Resol_Extrac_efect", length = 2048)
    private String linkResolucionExtracEfect;

    @Column(name = "Fecha_Resol_Extrac_efect")
    private LocalDate fechaResolucionExtracEfect;

    @Column(name = "Plazo_Med_Reg", nullable = false)
    private String plazoMedReg;

    @Column(name = "Plazo_Transmision", nullable = false)
    private String plazoTransmision;

    @Column(name = "Fecha_medicion_reg")
    private LocalDate fechaMedicionReg;

    @Column(name = "Fecha_transmision")
    private LocalDate fechaTransmision;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public BigDecimal getCaudalLsMin() {
        return caudalLsMin;
    }

    public void setCaudalLsMin(BigDecimal caudalLsMin) {
        this.caudalLsMin = caudalLsMin;
    }

    public BigDecimal getCaudalLsMax() {
        return caudalLsMax;
    }

    public void setCaudalLsMax(BigDecimal caudalLsMax) {
        this.caudalLsMax = caudalLsMax;
    }

    public String getResolucionExtracEfect() {
        return resolucionExtracEfect;
    }

    public void setResolucionExtracEfect(String resolucionExtracEfect) {
        this.resolucionExtracEfect = resolucionExtracEfect;
    }

    public String getLinkResolucionExtracEfect() {
        return linkResolucionExtracEfect;
    }

    public void setLinkResolucionExtracEfect(String linkResolucionExtracEfect) {
        this.linkResolucionExtracEfect = linkResolucionExtracEfect;
    }

    public LocalDate getFechaResolucionExtracEfect() {
        return fechaResolucionExtracEfect;
    }

    public void setFechaResolucionExtracEfect(LocalDate fechaResolucionExtracEfect) {
        this.fechaResolucionExtracEfect = fechaResolucionExtracEfect;
    }

    public String getPlazoMedReg() {
        return plazoMedReg;
    }

    public void setPlazoMedReg(String plazoMedReg) {
        this.plazoMedReg = plazoMedReg;
    }

    public String getPlazoTransmision() {
        return plazoTransmision;
    }

    public void setPlazoTransmision(String plazoTransmision) {
        this.plazoTransmision = plazoTransmision;
    }

    public LocalDate getFechaMedicionReg() {
        return fechaMedicionReg;
    }

    public void setFechaMedicionReg(LocalDate fechaMedicionReg) {
        this.fechaMedicionReg = fechaMedicionReg;
    }

    public LocalDate getFechaTransmision() {
        return fechaTransmision;
    }

    public void setFechaTransmision(LocalDate fechaTransmision) {
        this.fechaTransmision = fechaTransmision;
    }
}
