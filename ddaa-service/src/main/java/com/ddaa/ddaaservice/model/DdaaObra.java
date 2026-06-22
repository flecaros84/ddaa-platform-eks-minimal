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
@Table(name = "DDAA_OBRA", indexes = {
        @Index(name = "IX_DDAA_OBRA_FK_ID_RUT_PROVEEDOR", columnList = "FK_ID_RUT_PROVEEDOR"),
        @Index(name = "IX_DDAA_OBRA_FK_ID_DDAA_PLAZO", columnList = "FK_ID_DDAA_PLAZO")
})
public class DdaaObra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_RUT_PROVEEDOR")
    private Rut proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FK_ID_DDAA_PLAZO")
    private DdaaPlazo plazo;

    @Column(name = "TipoObra", length = 50)
    private String tipoObra;

    @Column(name = "EstadoObra")
    private Boolean estadoObra;

    @Column(name = "Fecha_Sol_Obra")
    private LocalDate fechaSolicitudObra;

    @Column(name = "Carpeta_Solicitud")
    private String carpetaSolicitud;

    @Column(name = "CoordenadaObra", length = 50)
    private String coordenadaObra;

    @Column(name = "ResolucionObra", length = 50)
    private String resolucionObra;

    @Column(name = "LinkResolucionObra", length = 2048)
    private String linkResolucionObra;

    @Column(name = "Con_Instrumento")
    private Boolean conInstrumento;

    @Column(name = "CodigoObraDGA", length = 50)
    private String codigoObraDga;

    @Column(name = "LinkQR", length = 2048)
    private String linkQr;

    @Column(name = "ReportaDGA")
    private Boolean reportaDga;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Rut getProveedor() {
        return proveedor;
    }

    public void setProveedor(Rut proveedor) {
        this.proveedor = proveedor;
    }

    public DdaaPlazo getPlazo() {
        return plazo;
    }

    public void setPlazo(DdaaPlazo plazo) {
        this.plazo = plazo;
    }

    public String getTipoObra() {
        return tipoObra;
    }

    public void setTipoObra(String tipoObra) {
        this.tipoObra = tipoObra;
    }

    public Boolean getEstadoObra() {
        return estadoObra;
    }

    public void setEstadoObra(Boolean estadoObra) {
        this.estadoObra = estadoObra;
    }

    public LocalDate getFechaSolicitudObra() {
        return fechaSolicitudObra;
    }

    public void setFechaSolicitudObra(LocalDate fechaSolicitudObra) {
        this.fechaSolicitudObra = fechaSolicitudObra;
    }

    public String getCarpetaSolicitud() {
        return carpetaSolicitud;
    }

    public void setCarpetaSolicitud(String carpetaSolicitud) {
        this.carpetaSolicitud = carpetaSolicitud;
    }

    public String getCoordenadaObra() {
        return coordenadaObra;
    }

    public void setCoordenadaObra(String coordenadaObra) {
        this.coordenadaObra = coordenadaObra;
    }

    public String getResolucionObra() {
        return resolucionObra;
    }

    public void setResolucionObra(String resolucionObra) {
        this.resolucionObra = resolucionObra;
    }

    public String getLinkResolucionObra() {
        return linkResolucionObra;
    }

    public void setLinkResolucionObra(String linkResolucionObra) {
        this.linkResolucionObra = linkResolucionObra;
    }

    public Boolean getConInstrumento() {
        return conInstrumento;
    }

    public void setConInstrumento(Boolean conInstrumento) {
        this.conInstrumento = conInstrumento;
    }

    public String getCodigoObraDga() {
        return codigoObraDga;
    }

    public void setCodigoObraDga(String codigoObraDga) {
        this.codigoObraDga = codigoObraDga;
    }

    public String getLinkQr() {
        return linkQr;
    }

    public void setLinkQr(String linkQr) {
        this.linkQr = linkQr;
    }

    public Boolean getReportaDga() {
        return reportaDga;
    }

    public void setReportaDga(Boolean reportaDga) {
        this.reportaDga = reportaDga;
    }
}
