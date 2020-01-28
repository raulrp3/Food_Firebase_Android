package com.example.proyectocomidas;

import com.google.firebase.Timestamp;

public class Pedido {
    private Timestamp fechaPedido;
    private String idUser;
    private String nombre;
    private String nombrePedido;
    private String direccion;
    private String telefono;
    private String horaRecogida;
    private String comentarios;
    private Boolean favorito;

    public Pedido(Timestamp fechaPedido, String idUser, String nombre, String nombrePedido, String direccion, String telefono, String horaRecogida, String comentarios, Boolean favorito) {
        this.fechaPedido = fechaPedido;
        this.idUser = idUser;
        this.nombre = nombre;
        this.nombrePedido = nombrePedido;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horaRecogida = horaRecogida;
        this.comentarios = comentarios;
        this.favorito = favorito;
    }

    public Pedido(Timestamp fechaPedido, String nombre, String direccion, String telefono, String horaRecogida) {
        this.fechaPedido = fechaPedido;
        this.idUser = "";
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horaRecogida = horaRecogida;
        this.nombrePedido = "";
        this.comentarios = "";
        this.favorito = false;
    }

    public Pedido(Timestamp fechaPedido, String nombre, String direccion, String telefono, String comentarios, String horaRecogida) {
        this.fechaPedido = fechaPedido;
        this.idUser = "";
        this.nombre = nombre;
        this.direccion = direccion;
        this.telefono = telefono;
        this.horaRecogida = horaRecogida;
        this.nombrePedido = "";
        this.comentarios = comentarios;
        this.favorito = false;
    }

    public Timestamp getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Timestamp fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombrePedido() {
        return nombrePedido;
    }

    public void setNombrePedido(String nombrePedido) {
        this.nombrePedido = nombrePedido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getHoraRecogida() {
        return horaRecogida;
    }

    public void setHoraRecogida(String horaRecogida) {
        this.horaRecogida = horaRecogida;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Boolean getFavorito() {
        return favorito;
    }

    public void setFavorito(Boolean favorito) {
        this.favorito = favorito;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "fechaPedido=" + fechaPedido +
                ", idUser='" + idUser + '\'' +
                ", nombre='" + nombre + '\'' +
                ", nombrePedido='" + nombrePedido + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", horaRecogida='" + horaRecogida + '\'' +
                ", comentarios='" + comentarios + '\'' +
                ", favorito=" + favorito +
                '}';
    }
}
