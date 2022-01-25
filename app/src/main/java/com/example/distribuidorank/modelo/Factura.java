package com.example.distribuidorank.modelo;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class Factura implements Serializable {

    /***** Cabecera de la factura *****/
    private int idCabeceraFactura;
    //Para un cliente o para proveedor
    private int tipoPedido;
    private String fkCliente;
    private Date fechaDelPedido;
    private Time horaDelPedido;

    /***** Detalle de la factura *****/
    private int fkProducto;
    private float utilidad;
    private float precioCompra;
    private float precioVenta;
    private int accion;

    public Factura(){
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public int getIdCabeceraFactura() {
        return idCabeceraFactura;
    }

    public void setIdCabeceraFactura(int idCabeceraFactura) {
        this.idCabeceraFactura = idCabeceraFactura;
    }

    public int getTipoPedido() {
        return tipoPedido;
    }

    public void setTipoPedido(int tipoPedido) {
        this.tipoPedido = tipoPedido;
    }

    public String getFkCliente() {
        return fkCliente;
    }

    public void setFkCliente(String fkCliente) {
        this.fkCliente = fkCliente;
    }

    public Date getFechaDelPedido() {
        return fechaDelPedido;
    }

    public void setFechaDelPedido(Date fechaDelPedido) {
        this.fechaDelPedido = fechaDelPedido;
    }

    public Time getHoraDelPedido() {
        return horaDelPedido;
    }

    public void setHoraDelPedido(Time horaDelPedido) {
        this.horaDelPedido = horaDelPedido;
    }

    public int getFkProducto() {
        return fkProducto;
    }

    public void setFkProducto(int fkProducto) {
        this.fkProducto = fkProducto;
    }

    public float getUtilidad() {
        return utilidad;
    }

    public void setUtilidad(float utilidad) {
        this.utilidad = utilidad;
    }

    public float getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(float precioCompra) {
        this.precioCompra = precioCompra;
    }

    public float getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(float precioVenta) {
        this.precioVenta = precioVenta;
    }

}
