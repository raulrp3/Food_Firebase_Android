package com.example.proyectocomidas;


public class Usuario {
    private String nombre;
    private String email;
    private String direccion;
    private String telefono;
    private Boolean isGoogle;


    public Usuario(String nombre, String email, String direccion, String telefono, Boolean isGoogle) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = direccion;
        this.telefono = telefono;
        this.isGoogle = isGoogle;

    }

    public Usuario(String nombre, String email, Boolean isGoogle) {
        this.nombre = nombre;
        this.email = email;
        this.direccion = "";
        this.telefono = "";
        this.isGoogle = isGoogle;
    }

    public Usuario(String email, Boolean isGoogle) {
        this.nombre = "";
        this.email = email;
        this.direccion = "";
        this.telefono = "";
        this.isGoogle = isGoogle;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getGoogle() {
        return isGoogle;
    }

    public void setGoogle(Boolean google) {
        isGoogle = google;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", isGoogle=" + isGoogle +
                '}';
    }
}
