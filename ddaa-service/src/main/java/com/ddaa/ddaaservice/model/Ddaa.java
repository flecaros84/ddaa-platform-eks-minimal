package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "DDAA", indexes = {
        @Index(name = "IX_DDAA_FK_ID_COMUNA", columnList = "FK_ID_COMUNA"),
        @Index(name = "IX_DDAA_FK_ID_RUT_TITULAR", columnList = "FK_ID_RUT_TITULAR"),
        @Index(name = "IX_DDAA_FK_ID_INSTALACION", columnList = "FK_ID_INSTALACION"),
        @Index(name = "IX_DDAA_FK_ID_FUENTE", columnList = "FK_ID_FUENTE")
})
public class Ddaa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_ddaa")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_COMUNA", nullable = false)
    private Comuna comuna;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_RUT_TITULAR", nullable = false)
    private Rut titular;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_INSTALACION")
    private Instalacion instalacion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_FUENTE", nullable = false)
    private Fuente fuente;

    @Column(name = "NombreFuenteDerecho")
    private String nombreFuenteDerecho;

    @Column(name = "Naturaleza_derecho")
    private String naturalezaDerecho;

    @Column(name = "Tipo_Derecho")
    private String tipoDerecho;

    @Column(name = "EstadoDerecho")
    private String estadoDerecho;

    @ManyToMany
    @JoinTable(name = "DDAA_DDAA_EXPEDIENTE",
            joinColumns = @JoinColumn(name = "id_ddaa"),
            inverseJoinColumns = @JoinColumn(name = "id_expediente"))
    private Set<DdaaExpediente> expedientes = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Comuna getComuna() {
        return comuna;
    }

    public void setComuna(Comuna comuna) {
        this.comuna = comuna;
    }

    public Rut getTitular() {
        return titular;
    }

    public void setTitular(Rut titular) {
        this.titular = titular;
    }

    public Instalacion getInstalacion() {
        return instalacion;
    }

    public void setInstalacion(Instalacion instalacion) {
        this.instalacion = instalacion;
    }

    public Fuente getFuente() {
        return fuente;
    }

    public void setFuente(Fuente fuente) {
        this.fuente = fuente;
    }

    public String getNombreFuenteDerecho() {
        return nombreFuenteDerecho;
    }

    public void setNombreFuenteDerecho(String nombreFuenteDerecho) {
        this.nombreFuenteDerecho = nombreFuenteDerecho;
    }

    public String getNaturalezaDerecho() {
        return naturalezaDerecho;
    }

    public void setNaturalezaDerecho(String naturalezaDerecho) {
        this.naturalezaDerecho = naturalezaDerecho;
    }

    public String getTipoDerecho() {
        return tipoDerecho;
    }

    public void setTipoDerecho(String tipoDerecho) {
        this.tipoDerecho = tipoDerecho;
    }

    public String getEstadoDerecho() {
        return estadoDerecho;
    }

    public void setEstadoDerecho(String estadoDerecho) {
        this.estadoDerecho = estadoDerecho;
    }

    public Set<DdaaExpediente> getExpedientes() {
        return expedientes;
    }

    public void setExpedientes(Set<DdaaExpediente> expedientes) {
        this.expedientes = expedientes;
    }
}
