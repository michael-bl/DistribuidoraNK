package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Producto implements Serializable {
    public Producto(){
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("fk_familia")
    @Expose
    private int fk_familia;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

    @SerializedName("utilidad")
    @Expose
    private Float utilidad;

    @SerializedName("precio_compra")
    @Expose
    private Float precio_compra;

    @SerializedName("precio_venta")
    @Expose
    private Float precio_venta;

    @SerializedName("accion")
    @Expose
    private int accion;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_familia() {
        return fk_familia;
    }

    public void setFk_familia(int fk_familia) {
        this.fk_familia = fk_familia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(Float utilidad) {
        this.utilidad = utilidad;
    }

    public Float getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(Float precio_compra) {
        this.precio_compra = precio_compra;
    }

    public Float getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(Float precio_venta) {
        this.precio_venta = precio_venta;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }
}
