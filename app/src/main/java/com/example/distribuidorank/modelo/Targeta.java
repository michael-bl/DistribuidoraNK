package com.example.distribuidorank.modelo;

public class Targeta {

    private String nombre;
    private String imagen;
    private int idProducto;
    private String porcentajeUtilidad;
    private String precioCompra;
    private String precioVenta;
    private String dineroUtilidad;
    private String cantidad;

    public Targeta(){
    }
    public String getPrecioCompra() {
        return precioCompra;
    }

    public void setPrecioCompra(String precioCompra) {
        this.precioCompra = precioCompra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getPorcentajeUtilidad() {
        return porcentajeUtilidad;
    }

    public void setPorcentajeUtilidad(String porcentajeUtilidad) {
        this.porcentajeUtilidad = porcentajeUtilidad;
    }

    public String getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(String precioVenta) {
        this.precioVenta = precioVenta;
    }


    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getDineroUtilidad() {
        return dineroUtilidad;
    }

    public void setDineroUtilidad(String dineroUtilidad) {
        this.dineroUtilidad = dineroUtilidad;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }
}
