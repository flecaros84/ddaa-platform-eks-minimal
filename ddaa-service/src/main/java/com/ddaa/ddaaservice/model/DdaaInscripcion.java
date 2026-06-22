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

import java.time.LocalDate;

@Entity
@Table(name = "DDAA_INSCRIPCION", indexes = @Index(name = "IX_DDAA_INSCRIPCION_EXPEDIENTE", columnList = "fk_id_ddaa_expediente"))
public class DdaaInscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fk_id_ddaa_expediente", nullable = false)
    private DdaaExpediente expediente;

    @Column(name = "cbr", length = 100)
    private String cbr;

    @Column(name = "cbr_fojas", length = 50)
    private String cbrFojas;

    @Column(name = "cbr_numero")
    private Integer cbrNumero;

    @Column(name = "cbr_fecha")
    private LocalDate cbrFecha;

    @Column(name = "cbr_link", length = 2048)
    private String cbrLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DdaaExpediente getExpediente() {
        return expediente;
    }

    public void setExpediente(DdaaExpediente expediente) {
        this.expediente = expediente;
    }

    public String getCbr() {
        return cbr;
    }

    public void setCbr(String cbr) {
        this.cbr = cbr;
    }

    public String getCbrFojas() {
        return cbrFojas;
    }

    public void setCbrFojas(String cbrFojas) {
        this.cbrFojas = cbrFojas;
    }

    public Integer getCbrNumero() {
        return cbrNumero;
    }

    public void setCbrNumero(Integer cbrNumero) {
        this.cbrNumero = cbrNumero;
    }

    public LocalDate getCbrFecha() {
        return cbrFecha;
    }

    public void setCbrFecha(LocalDate cbrFecha) {
        this.cbrFecha = cbrFecha;
    }

    public String getCbrLink() {
        return cbrLink;
    }

    public void setCbrLink(String cbrLink) {
        this.cbrLink = cbrLink;
    }
}
