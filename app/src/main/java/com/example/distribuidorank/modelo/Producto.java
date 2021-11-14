package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class Producto {
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
    private DecimalFormat utilidad;

    @SerializedName("costo")
    @Expose
    private DecimalFormat costo;

    @SerializedName("precio_venta")
    @Expose
    private DecimalFormat precio_venta;

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

    public DecimalFormat getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(DecimalFormat utilidad) {
        this.utilidad = utilidad;
    }

    public DecimalFormat getCosto() {
        return costo;
    }

    public void setCosto(DecimalFormat costo) {
        this.costo = costo;
    }

    public DecimalFormat getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(DecimalFormat precio_venta) {
        this.precio_venta = precio_venta;
    }
}
