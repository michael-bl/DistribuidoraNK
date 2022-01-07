package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class Proveedor implements Serializable {
    public Proveedor() {
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("telefono")
    @Expose
    private String telefono;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("ultima_actualizacion")
    @Expose
    private Date ultima_actualizacion;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("accion")
    @Expose
    private int accion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
