package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable {

    public Cliente(){
    }

    @SerializedName("arrayCliente")
    @Expose
    private ArrayList<Cliente> arrayCliente = null;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fk_localidad")
    @Expose
    private String fk_localidad;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("telefono")
    @Expose
    private String telefono;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("direccion")
    @Expose
    private String direccion;

    @SerializedName("accion")
    @Expose
    private int accion;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFk_localidad() {
        return fk_localidad;
    }

    public void setFk_localidad(String fk_localidad) {
        this.fk_localidad = fk_localidad;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getAccion() {
        return accion;
    }

    public void setAccion(int accion) {
        this.accion = accion;
    }

    public ArrayList<Cliente> getArrayCliente() {
        return arrayCliente;
    }

    public void setArrayCliente(ArrayList<Cliente> arrayCliente) {
        this.arrayCliente = arrayCliente;
    }
}
