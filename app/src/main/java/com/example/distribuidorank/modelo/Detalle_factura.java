package com.example.distribuidorank.modelo;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

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
    private float utilidad;

    @SerializedName("precio_compra")
    @Expose
    private float precio_compra;

    @SerializedName("precio_venta")
    @Expose
    private float precio_venta;

    @SerializedName("ultima_actualizacion")
    @Expose
    private Date ultima_actualizacion;

    @Expose(serialize = false)
    private static final DecimalFormat decimal_format =new DecimalFormat("#############.00");



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

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(String utilidad) {
        try {
            this.utilidad = decimal_format.parse(utilidad).floatValue();
        } catch (ParseException | NullPointerException e) {
            this.utilidad = 0;
            Log.e("Parse Exception", String.format("Clase: Detalle_factura\tMétodo: setUtilidad\nError:%s", e.toString()));
        }
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = Float.parseFloat(decimal_format.format(utilidad)); // Permite cortar cualquier cola de decimales que venga
    }

    public float getPrecio_compra() {
        return precio_compra;
    }

    public void setPrecio_compra(String precio_compra) {
        try {
            this.precio_compra = decimal_format.parse(precio_compra).floatValue();
        } catch (ParseException | NullPointerException e) {
            this.precio_compra = 0;
            Log.e("Parse Exception", String.format("Clase: Detalle_factura\tMétodo: setPrecio_compra\nError:%s", e.toString()));
        }
    }

    public void setPrecio_compra(float precio_compra) {
        this.precio_compra = Float.parseFloat(decimal_format.format(precio_compra)); // Permite cortar cualquier cola de decimales que venga
    }

    public float getPrecio_venta() {
        return precio_venta;
    }

    public void setPrecio_venta(String precio_venta) {
        try {
            this.precio_venta = decimal_format.parse(precio_venta).floatValue();
        } catch (ParseException | NullPointerException e) {
            this.precio_venta = 0;
            Log.e("Parse Exception", String.format("Clase: Detalle_factura\tMétodo: setPrecio_venta\nError:%s", e.toString()));
        }
    }

    public void setPrecio_venta(float precio_venta) {
        this.precio_venta = Float.parseFloat(decimal_format.format(precio_venta)); // Permite cortar cualquier cola de decimales que venga
    }

    public Date getUltima_actualizacion() {
        return this.ultima_actualizacion;
    }

    public void setUltima_actualizacion(Date ultima_actualizacion) {
        this.ultima_actualizacion = ultima_actualizacion;
    }
}
