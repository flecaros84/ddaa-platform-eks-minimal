package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "RUTS")
public class Rut {

    @Id
    @Column(name = "Rut")
    private Long rut;

    @Column(name = "Nombre", nullable = false)
    private String nombre;

    public Long getRut() {
        return rut;
    }

    public void setRut(Long rut) {
        this.rut = rut;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
