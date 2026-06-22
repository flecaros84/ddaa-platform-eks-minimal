package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "DDAA_PAGO_NO_USO", indexes = @Index(name = "IX_DDAA_PAGO_NO_USO_FK_ID_DDAA", columnList = "FK_ID_DDAA"))
public class DdaaPagoNoUso {

    @Id
    @Column(name = "folio_tgr")
    private Integer folioTgr;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_DDAA", nullable = false)
    private Ddaa ddaa;

    @Column(name = "fecha_cobro")
    private LocalDate fechaCobro;

    @Column(name = "caudal_aplicado_ls", precision = 18, scale = 4)
    private BigDecimal caudalAplicadoLs;

    @Column(name = "factor_aplicado")
    private Integer factorAplicado;

    @Column(name = "patente_utm", precision = 18, scale = 4)
    private BigDecimal patenteUtm;

    @Column(name = "patente_clp", precision = 18, scale = 2)
    private BigDecimal patenteClp;

    public Integer getFolioTgr() {
        return folioTgr;
    }

    public void setFolioTgr(Integer folioTgr) {
        this.folioTgr = folioTgr;
    }

    public Ddaa getDdaa() {
        return ddaa;
    }

    public void setDdaa(Ddaa ddaa) {
        this.ddaa = ddaa;
    }

    public LocalDate getFechaCobro() {
        return fechaCobro;
    }

    public void setFechaCobro(LocalDate fechaCobro) {
        this.fechaCobro = fechaCobro;
    }

    public BigDecimal getCaudalAplicadoLs() {
        return caudalAplicadoLs;
    }

    public void setCaudalAplicadoLs(BigDecimal caudalAplicadoLs) {
        this.caudalAplicadoLs = caudalAplicadoLs;
    }

    public Integer getFactorAplicado() {
        return factorAplicado;
    }

    public void setFactorAplicado(Integer factorAplicado) {
        this.factorAplicado = factorAplicado;
    }

    public BigDecimal getPatenteUtm() {
        return patenteUtm;
    }

    public void setPatenteUtm(BigDecimal patenteUtm) {
        this.patenteUtm = patenteUtm;
    }

    public BigDecimal getPatenteClp() {
        return patenteClp;
    }

    public void setPatenteClp(BigDecimal patenteClp) {
        this.patenteClp = patenteClp;
    }
}
