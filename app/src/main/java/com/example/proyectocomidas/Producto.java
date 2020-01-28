package com.example.proyectocomidas;

public class Producto {
    private String id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private Boolean disponible;
    private String idCategorias;
    private double precio;


    public Producto(String id, String nombre, String descripcion, String imagen, Boolean disponible, String idCategorias, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.disponible = disponible;
        this.idCategorias = idCategorias;
        this.precio = precio;
    }

    public Producto(String nombre, String descripcion, String imagen, Boolean disponible, String idCategorias, Double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.disponible = disponible;
        this.idCategorias = idCategorias;
        this.precio = precio;
    }

    public String getId() { return id; }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Boolean getDisponible() {
        return disponible;
    }

    public void setDisponible(Boolean disponible) {
        this.disponible = disponible;
    }

    public String getIdCategorias() {
        return idCategorias;
    }

    public void setIdCategorias(String idCategorias) {
        this.idCategorias = idCategorias;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", imagen='" + imagen + '\'' +
                ", disponible=" + disponible +
                ", idCategorias='" + idCategorias + '\'' +
                ", precio=" + precio +
                '}';
    }
}
