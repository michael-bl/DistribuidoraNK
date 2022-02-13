package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Cabecera_factura {
    public Cabecera_factura(){
    }

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("tipo")
    @Expose
    private String tipo;

    @SerializedName("fk_cliente")
    @Expose
    private String fk_cliente;

    @SerializedName("fk_usuario")
    @Expose
    private String fk_usuario;

    @SerializedName("fecha")
    @Expose
    private Date fecha;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFk_cliente() {
        return fk_cliente;
    }

    public void setFk_cliente(String fk_cliente) {
        this.fk_cliente = fk_cliente;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getFk_usuario() {
        return fk_usuario;
    }

    public void setFk_usuario(String fk_usuario) {
        this.fk_usuario = fk_usuario;
    }
}
