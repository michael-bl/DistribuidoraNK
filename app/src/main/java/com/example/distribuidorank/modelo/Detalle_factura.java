package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;

public class Detalle_factura {
    public Detalle_factura(){
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("fk_cabecera")
    @Expose
    private int fk_cabecera;

    @SerializedName("fk_producto")
    @Expose
    private int fk_producto;

    @SerializedName("utilidad")
    @Expose
    private DecimalFormat utilidad;

    @SerializedName("precio_k")
    @Expose
    private DecimalFormat precio_k;

    @SerializedName("precio_cliente")
    @Expose
    private DecimalFormat precio_cliente;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFk_cabecera() {
        return fk_cabecera;
    }

    public void setFk_cabecera(int fk_cabecera) {
        this.fk_cabecera = fk_cabecera;
    }

    public int getFk_producto() {
        return fk_producto;
    }

    public void setFk_producto(int fk_producto) {
        this.fk_producto = fk_producto;
    }

    public DecimalFormat getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(DecimalFormat utilidad) {
        this.utilidad = utilidad;
    }

    public DecimalFormat getPrecio_k() {
        return precio_k;
    }

    public void setPrecio_k(DecimalFormat precio_k) {
        this.precio_k = precio_k;
    }

    public DecimalFormat getPrecio_cliente() {
        return precio_cliente;
    }

    public void setPrecio_cliente(DecimalFormat precio_cliente) {
        this.precio_cliente = precio_cliente;
    }
}
