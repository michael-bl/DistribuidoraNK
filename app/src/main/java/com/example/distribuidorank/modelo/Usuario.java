package com.example.distribuidorank.modelo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usuario {
    public Usuario(){

    }
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("fk_localidad")
    @Expose
    private int fk_localidad;

    @SerializedName("nombre")
    @Expose
    private String nombre;

    @SerializedName("pass")
    @Expose
    private String pass;

    @SerializedName("telefono")
    @Expose
    private String telefono;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("direccion")
    @Expose
    private String direccion;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getFk_localidad() {
        return fk_localidad;
    }

    public void setFk_localidad(int fk_localidad) {
        this.fk_localidad = fk_localidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
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
}
