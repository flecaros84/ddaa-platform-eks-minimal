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
@Table(name = "FUENTE", indexes = @Index(name = "IX_FUENTE_FK_ID_SUBCUENCA", columnList = "FK_ID_SUBCUENCA"))
public class Fuente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_Fuente")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_SUBCUENCA")
    private Subcuenca subcuenca;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    @Column(name = "Tipo", length = 100)
    private String tipo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Subcuenca getSubcuenca() {
        return subcuenca;
    }

    public void setSubcuenca(Subcuenca subcuenca) {
        this.subcuenca = subcuenca;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
