package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Unidad implements Serializable {
    public Unidad() {
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    @SerializedName("ultima_actualizacion")
    @Expose
    private Date ultima_actualizacion;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("accion")
    @Expose
    private int accion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public Date getUltima_actualizacion() {
        return this.ultima_actualizacion;
    }

    public void setUltima_actualizacion(Date ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }
}
