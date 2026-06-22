package com.ddaa.ddaaservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDate;

@Entity
@Table(name = "DDAA_EXPEDIENTE", uniqueConstraints = @UniqueConstraint(name = "UQ_DDAA_EXPEDIENTE_codigo", columnNames = "codigo"))
public class DdaaExpediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "codigo", nullable = false, length = 50)
    private String codigo;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo;

    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    @Column(name = "resolucion_dga_n", length = 50)
    private String resolucionDgaNumero;

    @Column(name = "resolucion_dga_fecha")
    private LocalDate resolucionDgaFecha;

    @Column(name = "resolucion_dga_link", length = 2048)
    private String resolucionDgaLink;

    @Column(name = "catastro_n")
    private Integer catastroNumero;

    @Column(name = "catastro_fecha")
    private LocalDate catastroFecha;

    @Column(name = "catastro_link", length = 2048)
    private String catastroLink;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResolucionDgaNumero() {
        return resolucionDgaNumero;
    }

    public void setResolucionDgaNumero(String resolucionDgaNumero) {
        this.resolucionDgaNumero = resolucionDgaNumero;
    }

    public LocalDate getResolucionDgaFecha() {
        return resolucionDgaFecha;
    }

    public void setResolucionDgaFecha(LocalDate resolucionDgaFecha) {
        this.resolucionDgaFecha = resolucionDgaFecha;
    }

    public String getResolucionDgaLink() {
        return resolucionDgaLink;
    }

    public void setResolucionDgaLink(String resolucionDgaLink) {
        this.resolucionDgaLink = resolucionDgaLink;
    }

    public Integer getCatastroNumero() {
        return catastroNumero;
    }

    public void setCatastroNumero(Integer catastroNumero) {
        this.catastroNumero = catastroNumero;
    }

    public LocalDate getCatastroFecha() {
        return catastroFecha;
    }

    public void setCatastroFecha(LocalDate catastroFecha) {
        this.catastroFecha = catastroFecha;
    }

    public String getCatastroLink() {
        return catastroLink;
    }

    public void setCatastroLink(String catastroLink) {
        this.catastroLink = catastroLink;
    }
}
