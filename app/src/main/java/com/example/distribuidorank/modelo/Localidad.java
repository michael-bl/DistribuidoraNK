package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Localidad implements Serializable {
    public Localidad() {
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("localidad")
    @Expose
    private String localidad;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("ultima_actualizacion")
    @Expose
    private Date ultima_actualizacion;

    @SerializedName("accion")
    @Expose
    private int accion;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
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
