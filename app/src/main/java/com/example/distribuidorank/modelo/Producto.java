package com.example.distribuidorank.modelo;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;

public class Producto implements Serializable {
    public Producto(){
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("fk_unidad")
    @Expose
    private int fk_unidad;

    @SerializedName("descripcion")
    @Expose
    private String descripcion;

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

    public int getFk_unidad() {
        return fk_unidad;
    }

    public void setFk_unidad(int fk_unidad) {
        this.fk_unidad = fk_unidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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

    @SerializedName("accion")
    @Expose
    private int accion;

    @SerializedName("estado")
    @Expose
    private int estado;

    @SerializedName("detalle")
    @Expose
    private String detalle;

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
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
}
