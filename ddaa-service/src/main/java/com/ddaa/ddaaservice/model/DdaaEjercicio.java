package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "DDAA_EJERCICIO", indexes = @Index(name = "IX_DDAA_EJERCICIO_FK_ID_DDAA", columnList = "FK_ID_DDAA"))
public class DdaaEjercicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Ejercicio")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_DDAA", nullable = false)
    private Ddaa ddaa;

    @Column(name = "EjercicioDerecho")
    private String ejercicioDerecho;

    @Column(name = "ContinuidadDerecho")
    private String continuidadDerecho;

    @ManyToMany
    @JoinTable(name = "DDAA_EJERCICIO_OBRA",
            joinColumns = @JoinColumn(name = "FK_ID_EJERCICIO"),
            inverseJoinColumns = @JoinColumn(name = "FK_ID_OBRA"))
    private Set<DdaaObra> obras = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Ddaa getDdaa() {
        return ddaa;
    }

    public void setDdaa(Ddaa ddaa) {
        this.ddaa = ddaa;
    }

    public String getEjercicioDerecho() {
        return ejercicioDerecho;
    }

    public void setEjercicioDerecho(String ejercicioDerecho) {
        this.ejercicioDerecho = ejercicioDerecho;
    }

    public String getContinuidadDerecho() {
        return continuidadDerecho;
    }

    public void setContinuidadDerecho(String continuidadDerecho) {
        this.continuidadDerecho = continuidadDerecho;
    }

    public Set<DdaaObra> getObras() {
        return obras;
    }

    public void setObras(Set<DdaaObra> obras) {
        this.obras = obras;
    }
}
