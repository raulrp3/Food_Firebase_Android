package com.example.proyectocomidas;

public class PedidoFavorito {
    private String id;
    private String nombre;
    private String comments;

    public PedidoFavorito(String id, String nombre, String comments) {
        this.id = id;
        this.nombre = nombre;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "PedidoFavorito{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
