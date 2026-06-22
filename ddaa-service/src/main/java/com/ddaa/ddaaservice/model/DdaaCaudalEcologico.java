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
import jakarta.persistence.UniqueConstraint;

import java.math.BigDecimal;

@Entity
@Table(name = "DDAA_CAUDAL_ECOLOGICO",
        indexes = @Index(name = "IX_DDAA_CAUDAL_ECOLOGICO_FK_ID_EJERCICIO", columnList = "FK_ID_EJERCICIO"),
        uniqueConstraints = @UniqueConstraint(name = "UQ_DDAA_CAUDAL_ECOLOGICO_EJERCICIO_MES", columnNames = {"FK_ID_EJERCICIO", "mes"}))
public class DdaaCaudalEcologico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_caudal_ecologico")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "FK_ID_EJERCICIO", nullable = false)
    private DdaaEjercicio ejercicio;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "caudalEcologico", precision = 18, scale = 4)
    private BigDecimal caudalEcologico;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DdaaEjercicio getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(DdaaEjercicio ejercicio) {
        this.ejercicio = ejercicio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public BigDecimal getCaudalEcologico() {
        return caudalEcologico;
    }

    public void setCaudalEcologico(BigDecimal caudalEcologico) {
        this.caudalEcologico = caudalEcologico;
    }
}
